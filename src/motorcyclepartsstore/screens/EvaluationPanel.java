package motorcyclepartsstore.screens;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.service.PredictionService;
import motorcyclepartsstore.ui.Dialogs;
import motorcyclepartsstore.ui.LineAreaChart;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.RoundButton;
import motorcyclepartsstore.ui.ScreenPanel;
import motorcyclepartsstore.ui.SummaryCard;
import motorcyclepartsstore.ui.Theme;
import motorcyclepartsstore.ui.Chip;

/** Instructor model evaluation (reference page 10): split, metrics, chart, load/save model. */
public class EvaluationPanel extends ScreenPanel {

    private final SummaryCard split = new SummaryCard("Data split", (Chip) null);
    private final SummaryCard training = new SummaryCard("Training records", (Chip) null);
    private final SummaryCard testing = new SummaryCard("Testing records", (Chip) null);
    private final MetricsPanel metrics = new MetricsPanel("Combined model: average of Simple Linear Regression and Random Forest predictions.");
    private final LineAreaChart chart = new LineAreaChart();

    public EvaluationPanel(Navigator nav) {
        this(nav, RoundButton.secondary("Load model"), RoundButton.primary("Save model"));
    }

    private EvaluationPanel(Navigator nav, RoundButton load, RoundButton saveBtn) {
        super(nav, "Model evaluation", "Split, training, testing and 10-fold cross validation", false, load, saveBtn);
        load.addActionListener(e -> loadAndRefresh());
        saveBtn.addActionListener(e -> save(nav));
        JPanel body = new JPanel(new BorderLayout(0, Theme.GAP));
        body.setOpaque(false);
        body.add(row(Theme.GAP, split, training, testing), BorderLayout.NORTH);
        body.add(split(titledCard("Evaluation metrics and description", metrics),
                titledCard("Actual vs predicted", AdminDashboardPanel.chartWithCaption(chart,
                        "Demo dataset: test split, units per quarter, combined model.")), 0.68), BorderLayout.CENTER);
        setBody(body);
    }

    private static JFileChooser chooser() {
        PredictionService.MODELS_DIR.mkdirs();
        JFileChooser fc = new JFileChooser(PredictionService.MODELS_DIR.getAbsoluteFile());
        fc.setFileFilter(new FileNameExtensionFilter("One Prompt Away model (*.model)", "model"));
        return fc;
    }

    private static void save(Navigator nav) {
        PredictionService ps = PredictionService.get();
        if (!ps.isReady()) {
            Dialogs.message(nav.window(), "Save model", "The demo models are still training.");
            return;
        }
        JFileChooser fc = chooser();
        fc.setSelectedFile(new File(fc.getCurrentDirectory(), "demand-models.model"));
        if (fc.showSaveDialog(nav.window()) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = fc.getSelectedFile();
        if (!f.getName().endsWith(".model")) {
            f = new File(f.getParentFile(), f.getName() + ".model");
        }
        if (f.exists() && !Dialogs.confirm(nav.window(), "Replace file?", f.getName() + " already exists.",
                "Cancel", "Replace", false)) {
            return;
        }
        try {
            ps.saveModels(f);
            Dialogs.message(nav.window(), "Model saved", "Saved to " + f.getAbsolutePath());
        } catch (Exception ex) {
            Dialogs.message(nav.window(), "Save failed", String.valueOf(ex.getMessage()));
        }
    }

    private void loadAndRefresh() {
        PredictionService ps = PredictionService.get();
        if (!ps.isReady()) {
            Dialogs.message(nav.window(), "Load model", "The demo models are still training.");
            return;
        }
        JFileChooser fc = chooser();
        if (fc.showOpenDialog(nav.window()) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            ps.loadModels(fc.getSelectedFile());
            onShow();
            Dialogs.message(nav.window(), "Model loaded", "Metrics were recalculated on the test split.");
        } catch (Exception ex) {
            Dialogs.message(nav.window(), "Load failed", String.valueOf(ex.getMessage()));
        }
    }

    @Override
    public void onShow() {
        PredictionService ps = PredictionService.get();
        if (!ps.isReady()) {
            chart.setMessage(ps.getError() != null ? ps.getError() : "Training demo models...");
            return;
        }
        split.setValue(ps.splitLabel());
        training.setValue(Money.number(ps.trainingSize()));
        testing.setValue(Money.number(ps.testingSize()));
        metrics.refresh();
        chart.setSeries(ps.getTestSeries());
    }
}
