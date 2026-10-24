package motorcyclepartsstore.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import javax.swing.SwingUtilities;
import motorcyclepartsstore.model.DatasetRow;
import motorcyclepartsstore.model.Role;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SimpleLinearRegression;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Demand prediction using the Weka library that is already part of the project.
 *
 * The data is a synthetic weekly sales CSV bundled in the datasets/ folder, with a deterministic
 * generated fallback if that file is missing or invalid. It is NOT real shop data. The models
 * (Simple Linear Regression and Random Forest) are trained with Weka and evaluated on a held-out
 * test split and with manual 10-fold cross-validation.
 * The combined model is the average of the two predictions.
 */
public final class PredictionService {

    public static final String[] TYPES = {"Product demand", "Category demand"};
    public static final String[] MODELS = {"Simple Linear Regression", "Random Forest Regression",
        "Combined (Linear + Random Forest)"};
    public static final String[] PERIODS = {"Next week", "Next month", "Next quarter"};
    public static final String ALL_PRODUCTS = "All products";
    public static final String ALL_CATEGORIES = "All categories";
    public static final File MODELS_DIR = new File("models");

    private static final int WEEKS = 104;
    private static final int BLOCK = 13; // weeks per chart point
    private static final PredictionService INSTANCE = new PredictionService();

    /** Evaluation numbers for one model. */
    public static final class Metrics {
        public double mae, rmse, r2, r, accuracy, precision, recall, f1, cv;
        public int tn, fp, fn, tp;
    }

    /** Chart series. */
    public static final class Series {
        public final String[] labels;
        public final double[] actual;
        public final double[] predicted;

        Series(String[] labels, double[] actual, double[] predicted) {
            this.labels = labels;
            this.actual = actual;
            this.predicted = predicted;
        }
    }

    /** One line of the prediction results table. */
    public static final class ResultRow {
        public final String name;
        public final long linear, forest, combined;
        public final String cls;

        ResultRow(String name, long linear, long forest, long combined, String cls) {
            this.name = name;
            this.linear = linear;
            this.forest = forest;
            this.combined = combined;
            this.cls = cls;
        }

        public long units(int modelIndex) {
            return modelIndex == 0 ? linear : modelIndex == 1 ? forest : combined;
        }
    }

    public static final class Result {
        public final List<ResultRow> rows;
        public final long totalLinear, totalForest, totalCombined;

        Result(List<ResultRow> rows) {
            this.rows = rows;
            long a = 0, b = 0, c = 0;
            for (ResultRow r : rows) {
                a += r.linear;
                b += r.forest;
                c += r.combined;
            }
            totalLinear = a;
            totalForest = b;
            totalCombined = c;
        }
    }

    /** Serializable container for saving/loading the trained models. */
    static final class ModelBundle implements Serializable {
        private static final long serialVersionUID = 1L;
        final Classifier linear;
        final Classifier forest;

        ModelBundle(Classifier linear, Classifier forest) {
            this.linear = linear;
            this.forest = forest;
        }
    }

    private volatile boolean ready;
    private volatile String error;
    private boolean started;

    private List<DatasetRow> rows;
    private Instances train;
    private Instances test;
    private Instances template;
    private LocalDate lastMonday;
    private volatile Classifier linear;
    private volatile Classifier forest;
    private double fastThreshold;
    private double hiWeekly;
    private double loWeekly;
    private double[] cvScores = new double[3];
    private volatile Metrics[] metrics = new Metrics[3];
    private volatile Series trend;
    private volatile Series testSeries;

    private PredictionService() {
    }

    public static PredictionService get() {
        return INSTANCE;
    }

    public boolean isReady() {
        return ready;
    }

    public String getError() {
        return error;
    }

    /** Starts demo-data generation and training on a background thread; onDone runs on the EDT. */
    public synchronized void start(Runnable onDone) {
        if (started) {
            return;
        }
        started = true;
        Thread t = new Thread(() -> {
            try {
                buildDataset();
                trainModels();
                ready = true;
            } catch (Exception | LinkageError ex) {
                error = "Demo models could not be trained: " + ex;
            }
            if (onDone != null) {
                SwingUtilities.invokeLater(onDone);
            }
        }, "demo-model-training");
        t.setDaemon(true);
        t.start();
    }

    // ------------------------------------------------------------------ dataset

    private void buildDataset() {
        rows = new ArrayList<>();
        if (!loadWeeklySalesCsv(new File("datasets/weekly_sales.csv"))) {
            buildGeneratedFallbackDataset();
        }
        // Per-product weekly means define the Best selling / Slow moving cut-offs.
        double[] means = new double[DemoCatalog.size()];
        for (DatasetRow r : rows) {
            means[r.productId] += r.qtySold / (double) WEEKS;
        }
        Arrays.sort(means);
        loWeekly = means[(int) Math.floor((means.length - 1) * 0.33)];
        hiWeekly = means[(int) Math.ceil((means.length - 1) * 0.67)];
    }

    /** Loads the bundled CSV only when it has the full, expected product/week matrix. */
    private boolean loadWeeklySalesCsv(File file) {
        if (!file.isFile()) {
            return false;
        }
        List<DatasetRow> loaded = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null || !header.replace("\uFEFF", "").trim().equals(
                    "date,product_id,sku,product,category,qty_sold,unit_price,stock,week_index,week_of_year")) {
                return false;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] f = line.split(",", -1);
                if (f.length != 10) {
                    return false;
                }
                int id = Integer.parseInt(f[1].trim());
                if (id < 0 || id >= DemoCatalog.size()) {
                    return false;
                }
                if (!f[2].equals(String.valueOf(DemoCatalog.ITEMS[id][0]))
                        || !f[3].equals(DemoCatalog.name(id))
                        || !f[4].equals(DemoCatalog.category(id))) {
                    return false;
                }
                LocalDate date = LocalDate.parse(f[0].trim());
                int qty = Integer.parseInt(f[5].trim());
                double price = Double.parseDouble(f[6].trim());
                int stock = Integer.parseInt(f[7].trim());
                int weekIndex = Integer.parseInt(f[8].trim());
                int weekOfYear = Integer.parseInt(f[9].trim());
                if (qty < 0 || price < 0 || stock < 0 || weekIndex < 0 || weekIndex >= WEEKS
                        || weekOfYear < 1 || weekOfYear > 52) {
                    return false;
                }
                loaded.add(new DatasetRow(date, id, f[3], f[4], qty, price, stock, weekIndex, weekOfYear));
            }
            if (loaded.size() != WEEKS * DemoCatalog.size()) {
                return false;
            }
            loaded.sort((a, b) -> {
                int dateOrder = a.date.compareTo(b.date);
                return dateOrder != 0 ? dateOrder : Integer.compare(a.productId, b.productId);
            });
            LocalDate firstDate = loaded.get(0).date;
            LocalDate latestDate = loaded.get(loaded.size() - 1).date;
            if (firstDate.getDayOfWeek() != DayOfWeek.MONDAY
                    || latestDate.getDayOfWeek() != DayOfWeek.MONDAY
                    || !latestDate.minusWeeks(WEEKS - 1).equals(firstDate)) {
                return false;
            }
            // Confirm every week has one row for every product and no duplicate product/week pair.
            boolean[][] seen = new boolean[WEEKS][DemoCatalog.size()];
            for (DatasetRow r : loaded) {
                if (seen[r.weekIndex][r.productId]) {
                    return false;
                }
                seen[r.weekIndex][r.productId] = true;
            }
            for (boolean[] week : seen) {
                for (boolean productSeen : week) {
                    if (!productSeen) {
                        return false;
                    }
                }
            }
            rows = loaded;
            lastMonday = latestDate;
            return true;
        } catch (IOException | RuntimeException ex) {
            return false;
        }
    }

    /** Deterministic fallback keeps predictions working if the bundled CSV is unavailable. */
    private void buildGeneratedFallbackDataset() {
        lastMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        Random rnd = new Random(2026);
        rows = new ArrayList<>();
        for (int w = 0; w < WEEKS; w++) {
            LocalDate date = lastMonday.minusWeeks(WEEKS - 1 - w);
            int woy = Math.min(52, date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
            for (int p = 0; p < DemoCatalog.size(); p++) {
                double base = DemoCatalog.baseDemand(p);
                double season = 1 + 0.18 * Math.sin(2 * Math.PI * woy / 52.0 + p * 0.7);
                double mean = base * (1 + 0.004 * w) * season;
                double noise = rnd.nextGaussian() * (0.12 * mean + 0.5);
                int qty = (int) Math.max(0, Math.round(mean + noise));
                double price = Math.round(DemoCatalog.price(p) * (1 + 0.0004 * w) * 100.0) / 100.0;
                rows.add(new DatasetRow(date, p, DemoCatalog.name(p), DemoCatalog.category(p), qty, price,
                        5 + rnd.nextInt(80), w, woy));
            }
        }
    }

    private Instances newHeader() {
        ArrayList<Attribute> atts = new ArrayList<>();
        atts.add(new Attribute("product_id"));
        atts.add(new Attribute("week_index"));
        atts.add(new Attribute("week_of_year"));
        atts.add(new Attribute("unit_price"));
        atts.add(new Attribute("qty_sold"));
        Instances ds = new Instances("weekly_demand", atts, 0);
        ds.setClassIndex(4);
        return ds;
    }

    private Instance instance(Instances ds, int productId, int weekIndex, int woy, double price, Double qty) {
        Instance in = new DenseInstance(5);
        in.setDataset(ds);
        in.setValue(0, productId);
        in.setValue(1, weekIndex);
        in.setValue(2, woy);
        in.setValue(3, price);
        if (qty != null) {
            in.setValue(4, qty);
        }
        return in;
    }

    // ------------------------------------------------------------------ training / evaluation

    private static Classifier newLinear() {
        return new SimpleLinearRegression();
    }

    private static Classifier newForest() {
        RandomForest rf = new RandomForest();
        rf.setNumIterations(60);
        rf.setSeed(1);
        rf.setNumExecutionSlots(1);
        return rf;
    }

    private void trainModels() throws Exception {
        template = newHeader();
        Instances all = newHeader();
        for (DatasetRow r : rows) {
            all.add(instance(all, r.productId, r.weekIndex, r.weekOfYear, r.unitPrice, (double) r.qtySold));
        }
        List<Integer> idx = new ArrayList<>();
        for (int i = 0; i < all.numInstances(); i++) {
            idx.add(i);
        }
        Collections.shuffle(idx, new Random(7));
        int cut = (int) Math.round(idx.size() * 0.8);
        train = newHeader();
        test = newHeader();
        for (int i = 0; i < idx.size(); i++) {
            (i < cut ? train : test).add(all.instance(idx.get(i)));
        }
        double[] q = new double[train.numInstances()];
        for (int i = 0; i < q.length; i++) {
            q[i] = train.instance(i).classValue();
        }
        Arrays.sort(q);
        fastThreshold = q[q.length / 2];

        Classifier l = newLinear();
        l.buildClassifier(train);
        Classifier f = newForest();
        f.buildClassifier(train);
        linear = l;
        forest = f;
        cvScores = crossValidate();
        evaluateTestSet();
        trend = quarterly(false);
    }

    private double[] crossValidate() throws Exception {
        int k = 10;
        List<Integer> idx = new ArrayList<>();
        for (int i = 0; i < train.numInstances(); i++) {
            idx.add(i);
        }
        Collections.shuffle(idx, new Random(11));
        double[] sum = new double[3];
        for (int fold = 0; fold < k; fold++) {
            Instances tr = new Instances(train, 0);
            Instances te = new Instances(train, 0);
            for (int pos = 0; pos < idx.size(); pos++) {
                (pos % k == fold ? te : tr).add(train.instance(idx.get(pos)));
            }
            Classifier l = newLinear();
            l.buildClassifier(tr);
            Classifier f = newForest();
            f.buildClassifier(tr);
            int n = te.numInstances();
            double[] a = new double[n], pl = new double[n], pf = new double[n], pc = new double[n];
            for (int i = 0; i < n; i++) {
                a[i] = te.instance(i).classValue();
                pl[i] = Math.max(0, l.classifyInstance(te.instance(i)));
                pf[i] = Math.max(0, f.classifyInstance(te.instance(i)));
                pc[i] = (pl[i] + pf[i]) / 2.0;
            }
            sum[0] += r2(a, pl);
            sum[1] += r2(a, pf);
            sum[2] += r2(a, pc);
        }
        for (int i = 0; i < 3; i++) {
            sum[i] /= k;
        }
        return sum;
    }

    private void evaluateTestSet() throws Exception {
        int n = test.numInstances();
        double[] a = new double[n], pl = new double[n], pf = new double[n], pc = new double[n];
        for (int i = 0; i < n; i++) {
            Instance in = test.instance(i);
            a[i] = in.classValue();
            pl[i] = Math.max(0, linear.classifyInstance(in));
            pf[i] = Math.max(0, forest.classifyInstance(in));
            pc[i] = (pl[i] + pf[i]) / 2.0;
        }
        Metrics[] m = new Metrics[3];
        m[0] = computeMetrics(a, pl, cvScores[0]);
        m[1] = computeMetrics(a, pf, cvScores[1]);
        m[2] = computeMetrics(a, pc, cvScores[2]);
        metrics = m;
        testSeries = quarterly(true);
    }

    private Metrics computeMetrics(double[] a, double[] p, double cv) {
        Metrics m = new Metrics();
        int n = a.length;
        double abs = 0, sq = 0;
        for (int i = 0; i < n; i++) {
            abs += Math.abs(a[i] - p[i]);
            sq += (a[i] - p[i]) * (a[i] - p[i]);
            boolean actualFast = a[i] >= fastThreshold;
            boolean predFast = p[i] >= fastThreshold;
            if (actualFast && predFast) {
                m.tp++;
            } else if (actualFast) {
                m.fn++;
            } else if (predFast) {
                m.fp++;
            } else {
                m.tn++;
            }
        }
        m.mae = abs / n;
        m.rmse = Math.sqrt(sq / n);
        m.r2 = r2(a, p);
        m.r = pearson(a, p);
        m.accuracy = (m.tp + m.tn) / (double) n;
        m.precision = m.tp + m.fp == 0 ? 0 : m.tp / (double) (m.tp + m.fp);
        m.recall = m.tp + m.fn == 0 ? 0 : m.tp / (double) (m.tp + m.fn);
        m.f1 = m.precision + m.recall == 0 ? 0 : 2 * m.precision * m.recall / (m.precision + m.recall);
        m.cv = cv;
        return m;
    }

    private static double r2(double[] a, double[] p) {
        double mean = 0;
        for (double v : a) {
            mean += v;
        }
        mean /= a.length;
        double sse = 0, sst = 0;
        for (int i = 0; i < a.length; i++) {
            sse += (a[i] - p[i]) * (a[i] - p[i]);
            sst += (a[i] - mean) * (a[i] - mean);
        }
        return sst == 0 ? 0 : 1 - sse / sst;
    }

    private static double pearson(double[] a, double[] p) {
        int n = a.length;
        double ma = 0, mp = 0;
        for (int i = 0; i < n; i++) {
            ma += a[i];
            mp += p[i];
        }
        ma /= n;
        mp /= n;
        double sab = 0, saa = 0, spp = 0;
        for (int i = 0; i < n; i++) {
            sab += (a[i] - ma) * (p[i] - mp);
            saa += (a[i] - ma) * (a[i] - ma);
            spp += (p[i] - mp) * (p[i] - mp);
        }
        return saa == 0 || spp == 0 ? 0 : sab / Math.sqrt(saa * spp);
    }

    /** Units per 13-week block, actual vs the combined model; testOnly limits it to the test split. */
    private Series quarterly(boolean testOnly) throws Exception {
        int blocks = WEEKS / BLOCK;
        double[] act = new double[blocks];
        double[] pred = new double[blocks];
        String[] labels = new String[blocks];
        DateTimeFormatter f = DateTimeFormatter.ofPattern("MMM yy", Locale.ENGLISH);
        for (int b = 0; b < blocks; b++) {
            labels[b] = lastMonday.minusWeeks(WEEKS - 1 - b * BLOCK).format(f);
        }
        Instances source = testOnly ? test : null;
        if (testOnly) {
            for (int i = 0; i < source.numInstances(); i++) {
                Instance in = source.instance(i);
                int b = Math.min(blocks - 1, (int) in.value(1) / BLOCK);
                act[b] += in.classValue();
                pred[b] += (Math.max(0, linear.classifyInstance(in)) + Math.max(0, forest.classifyInstance(in))) / 2.0;
            }
        } else {
            for (DatasetRow r : rows) {
                Instance in = instance(template, r.productId, r.weekIndex, r.weekOfYear, r.unitPrice, null);
                int b = Math.min(blocks - 1, r.weekIndex / BLOCK);
                act[b] += r.qtySold;
                pred[b] += (Math.max(0, linear.classifyInstance(in)) + Math.max(0, forest.classifyInstance(in))) / 2.0;
            }
        }
        return new Series(labels, act, pred);
    }

    // ------------------------------------------------------------------ queries

    private void requireReady() {
        if (!ready) {
            throw new IllegalStateException(error != null ? error : "The demo models are still training.");
        }
    }

    public Metrics[] getMetrics() {
        return ready ? metrics : null;
    }

    public Series getTrend() {
        return ready ? trend : null;
    }

    public Series getTestSeries() {
        return ready ? testSeries : null;
    }

    public int datasetSize() {
        return rows == null ? 0 : rows.size();
    }

    public int trainingSize() {
        return train == null ? 0 : train.numInstances();
    }

    public int testingSize() {
        return test == null ? 0 : test.numInstances();
    }

    public String splitLabel() {
        return "80 / 20";
    }

    public double fastThreshold() {
        return fastThreshold;
    }

    /** Latest weekly records of a few products for the dataset preview tables. */
    public List<DatasetRow> preview(int count) {
        List<DatasetRow> out = new ArrayList<>();
        if (rows == null) {
            return out;
        }
        int[] ids = {0, 3, 5, 7, 1, 2, 4};
        for (int i = 0; i < count && i < ids.length; i++) {
            out.add(rows.get(rows.size() - DemoCatalog.size() + ids[i]));
        }
        return out;
    }

    public List<String> productNames() {
        List<String> n = new ArrayList<>();
        for (int i = 0; i < DemoCatalog.size(); i++) {
            n.add(DemoCatalog.name(i));
        }
        return n;
    }

    public List<String> categoryNames() {
        List<String> n = new ArrayList<>();
        for (int i = 0; i < DemoCatalog.size(); i++) {
            if (!n.contains(DemoCatalog.category(i))) {
                n.add(DemoCatalog.category(i));
            }
        }
        return n;
    }

    public int modelsSaved() {
        File[] files = MODELS_DIR.listFiles((d, name) -> name.endsWith(".model"));
        return files == null ? 0 : files.length;
    }

    /**
     * Runs the demand forecast. type/product/period come from the combo boxes;
     * modelIndex (0 linear, 1 forest, 2 combined) decides which model's units drive the class column.
     */
    public Result predict(String type, int modelIndex, String selection, String period) throws Exception {
        Session.require(Role.ADMIN, Role.INSTRUCTOR);
        requireReady();
        int weeks = period.contains("week") ? 1 : period.contains("month") ? 4 : 13;
        int products = DemoCatalog.size();
        double[] lin = new double[products];
        double[] frs = new double[products];
        for (int k = 1; k <= weeks; k++) {
            LocalDate d = lastMonday.plusWeeks(k);
            int woy = Math.min(52, d.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
            for (int p = 0; p < products; p++) {
                Instance in = instance(template, p, WEEKS - 1 + k, woy, DemoCatalog.price(p), null);
                lin[p] += Math.max(0, linear.classifyInstance(in));
                frs[p] += Math.max(0, forest.classifyInstance(in));
            }
        }
        boolean byCategory = type.startsWith("Category");
        List<String> keys = byCategory ? categoryNames() : productNames();
        List<ResultRow> out = new ArrayList<>();
        for (String key : keys) {
            if (!(selection == null || selection.equals(ALL_PRODUCTS) || selection.equals(ALL_CATEGORIES)
                    || selection.equals(key))) {
                continue;
            }
            double sl = 0, sf = 0;
            int count = 0;
            for (int p = 0; p < products; p++) {
                String k = byCategory ? DemoCatalog.category(p) : DemoCatalog.name(p);
                if (k.equals(key)) {
                    sl += lin[p];
                    sf += frs[p];
                    count++;
                }
            }
            long l = Math.round(sl);
            long f = Math.round(sf);
            long c = Math.round((sl + sf) / 2.0);
            long chosen = modelIndex == 0 ? l : modelIndex == 1 ? f : c;
            double weeklyPerProduct = chosen / (double) weeks / Math.max(1, count);
            String cls = weeklyPerProduct >= hiWeekly ? "Best selling"
                    : weeklyPerProduct <= loWeekly ? "Slow moving" : "Average";
            out.add(new ResultRow(key, l, f, c, cls));
        }
        return new Result(out);
    }

    // ------------------------------------------------------------------ save / load

    public void saveModels(File file) throws IOException {
        Session.require(Role.INSTRUCTOR);
        requireReady();
        File parent = file.getAbsoluteFile().getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(new ModelBundle(linear, forest));
        }
    }

    /** Loads a model file written by saveModels and re-evaluates it on the test split. */
    public void loadModels(File file) throws IOException {
        Session.require(Role.INSTRUCTOR);
        requireReady();
        ModelBundle bundle;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            in.setObjectInputFilter(ObjectInputFilter.Config.createFilter(
                    "weka.**;java.**;motorcyclepartsstore.**;!*"));
            Object o = in.readObject();
            if (!(o instanceof ModelBundle)) {
                throw new IOException("This is not a One Prompt Away model file.");
            }
            bundle = (ModelBundle) o;
        } catch (ClassNotFoundException | java.io.InvalidClassException ex) {
            throw new IOException("This model file is not compatible with this version.", ex);
        }
        Classifier oldL = linear;
        Classifier oldF = forest;
        try {
            linear = bundle.linear;
            forest = bundle.forest;
            evaluateTestSet();
            trend = quarterly(false);
        } catch (Exception ex) {
            linear = oldL;
            forest = oldF;
            throw new IOException("The model could not be used on the demo dataset: " + ex.getMessage(), ex);
        }
    }
}
