package motorcyclepartsstore.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import motorcyclepartsstore.model.DatasetRow;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.service.PredictionService;
import motorcyclepartsstore.ui.Chip;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Dialogs;
import motorcyclepartsstore.ui.Forms;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.RoundButton;
import motorcyclepartsstore.ui.RoundedPanel;
import motorcyclepartsstore.ui.ScreenPanel;
import motorcyclepartsstore.ui.SummaryCard;
import motorcyclepartsstore.ui.Theme;

/** Knowledge analysis and prediction: Admin (page 5) and Instructor (page 9) variants. */
public class PredictionPanel extends ScreenPanel {

    private final boolean instructor;
    private final JComboBox<String> type = Forms.combo(PredictionService.TYPES);
    private final JComboBox<String> modelBox = Forms.combo(PredictionService.MODELS);
    private final JComboBox<String> product = Forms.combo(new String[]{PredictionService.ALL_PRODUCTS});
    private final JComboBox<String> period = Forms.combo(PredictionService.PERIODS);
    private final JLabel status = Forms.message();
    private final SummaryCard linear = new SummaryCard("Simple Linear Regression", new Chip("Linear", Chip.Kind.BLUE));
    private final SummaryCard forest = new SummaryCard("Random Forest Regression", new Chip("Forest", Chip.Kind.BLUE));
    private final SummaryCard combined = new SummaryCard("Combined result", new Chip("Combined", Chip.Kind.GREEN));
    private final DataTable.ListModel resultModel = new DataTable.ListModel("Part", "Predicted units", "Class");
    private final DataTable.ListModel previewModel = new DataTable.ListModel("date", "product", "category", "qty_sold", "unit_price", "stock");
    private MetricsPanel metrics;
    private boolean ranOnce;

    public PredictionPanel(Navigator nav, boolean instructor) {
        super(nav, "Knowledge analysis and prediction",
                instructor ? "Instructor: inputs, outputs, dataset, metrics and description"
                        : "Admin: input and prediction output", true);
        this.instructor = instructor;
        modelBox.setSelectedIndex(2);
        period.setSelectedIndex(1);
        type.addActionListener(e -> refillProducts());
        refillProducts();

        RoundButton run = RoundButton.primary("Run prediction");
        run.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        run.setPreferredSize(new Dimension(100, 44));
        run.addActionListener(e -> run());

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        for (JComponent c : new JComponent[]{Forms.labeled("Prediction type", type), Forms.labeled("Model", modelBox),
            Forms.labeled("Product", product), Forms.labeled("Period", period)}) {
            c.setAlignmentX(0f);
            form.add(c);
            form.add(javax.swing.Box.createVerticalStrut(14));
        }
        run.setAlignmentX(0f);
        form.add(run);
        form.add(javax.swing.Box.createVerticalStrut(10));
        status.setAlignmentX(0f);
        form.add(status);
        JLabel demo = Forms.muted("<html><body style='width:230px'>Demo data: Weka models trained on a generated weekly dataset, not real shop sales.</body></html>", 12);
        demo.setAlignmentX(0f);
        form.add(javax.swing.Box.createVerticalStrut(6));
        form.add(demo);
        RoundedPanel input = titledCard("Input", form);
        input.setPreferredSize(new Dimension(300, 100));

        DataTable results = new DataTable(resultModel).weights(36, 28, 36).chipColumn(2);
        RoundedPanel resultCard = titledCard("Prediction results", top(results.wrapper()));

        JPanel right = new JPanel(new BorderLayout(0, Theme.GAP));
        right.setOpaque(false);
        right.add(row(Theme.GAP, linear, forest, combined), BorderLayout.NORTH);
        if (instructor) {
            DataTable preview = new DataTable(previewModel).weights(19, 20, 17, 12, 17, 12);
            RoundedPanel previewCard = titledCard("Dataset preview", top(preview.wrapper()));
            metrics = new MetricsPanel(null);
            JPanel mid = new JPanel(new BorderLayout(0, Theme.GAP));
            mid.setOpaque(false);
            mid.add(split(resultCard, previewCard, 0.46), BorderLayout.NORTH);
            mid.add(titledCard("Evaluation metrics", metrics), BorderLayout.CENTER);
            right.add(mid, BorderLayout.CENTER);
        } else {
            right.add(resultCard, BorderLayout.CENTER);
        }
        JPanel body = new JPanel(new BorderLayout(Theme.GAP, 0));
        body.setOpaque(false);
        body.add(input, BorderLayout.WEST);
        body.add(right, BorderLayout.CENTER);
        setBody(body);
    }

    private void refillProducts() {
        PredictionService ps = PredictionService.get();
        boolean cat = type.getSelectedIndex() == 1;
        product.removeAllItems();
        product.addItem(cat ? PredictionService.ALL_CATEGORIES : PredictionService.ALL_PRODUCTS);
        for (String s : cat ? ps.categoryNames() : ps.productNames()) {
            product.addItem(s);
        }
        resultModel.setColumnName(0, cat ? "Category" : "Part");
    }

    @Override
    public void onShow() {
        PredictionService ps = PredictionService.get();
        if (!ps.isReady()) {
            Forms.error(status, ps.getError() != null ? ps.getError() : "Demo models are still training...");
            return;
        }
        if (status.getText().contains("training")) {
            status.setText(" ");
        }
        if (instructor) {
            DateFmt.fillPreview(previewModel, ps.preview(3));
            metrics.refresh();
        }
        if (!ranOnce) {
            run();
        }
    }

    private void run() {
        PredictionService ps = PredictionService.get();
        if (!ps.isReady()) {
            Forms.error(status, ps.getError() != null ? ps.getError() : "Demo models are still training. Try again in a moment.");
            return;
        }
        try {
            PredictionService.Result r = ps.predict((String) type.getSelectedItem(), modelBox.getSelectedIndex(),
                    (String) product.getSelectedItem(), (String) period.getSelectedItem());
            int mi = modelBox.getSelectedIndex();
            List<Object[]> rows = new ArrayList<>();
            for (PredictionService.ResultRow x : r.rows) {
                rows.add(new Object[]{x.name, Money.number(x.units(mi)) + " units", x.cls});
            }
            resultModel.setRows(rows);
            linear.setValue(Money.number(r.totalLinear) + " units");
            forest.setValue(Money.number(r.totalForest) + " units");
            combined.setValue(Money.number(r.totalCombined) + " units");
            ranOnce = true;
            Forms.success(status, "Prediction updated.");
        } catch (Exception | LinkageError ex) {
            Dialogs.message(nav.window(), "Prediction failed", String.valueOf(ex.getMessage()));
        }
    }

    /** Date/number formatting for dataset preview rows. */
    static final class DateFmt {

        private DateFmt() {
        }

        static void fillPreview(DataTable.ListModel model, List<DatasetRow> data) {
            java.time.format.DateTimeFormatter f = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd", java.util.Locale.ENGLISH);
            List<Object[]> rows = new ArrayList<>();
            for (DatasetRow r : data) {
                rows.add(new Object[]{r.date.format(f), r.product, r.category, r.qtySold,
                    Math.round(r.unitPrice), r.stock});
            }
            model.setRows(rows);
        }
    }
}
