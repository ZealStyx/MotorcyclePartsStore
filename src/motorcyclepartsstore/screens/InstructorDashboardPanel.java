package motorcyclepartsstore.screens;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.service.PredictionService;
import motorcyclepartsstore.service.PredictionService.Metrics;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.DonutChart;
import motorcyclepartsstore.ui.Icons;
import motorcyclepartsstore.ui.LineAreaChart;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.ScreenPanel;
import motorcyclepartsstore.ui.SummaryCard;
import motorcyclepartsstore.ui.Theme;

/** Instructor dashboard (reference page 8). */
public class InstructorDashboardPanel extends ScreenPanel {

    private final SummaryCard records = new SummaryCard("Dataset records", Icons.Kind.BOX);
    private final SummaryCard bestR2 = new SummaryCard("Best R\u00B2", Icons.Kind.BARS);
    private final SummaryCard bestRmse = new SummaryCard("Best RMSE", Icons.Kind.TREND);
    private final SummaryCard saved = new SummaryCard("Models saved", Icons.Kind.PLUS);
    private final LineAreaChart chart = new LineAreaChart();
    private final DonutChart donut = new DonutChart();
    private final DataTable.ListModel previewModel = new DataTable.ListModel("date", "product", "category", "qty_sold", "unit_price", "stock");

    public InstructorDashboardPanel(Navigator nav) {
        super(nav, "Dashboard", "Instructor overview, models and data", true);
        DataTable preview = new DataTable(previewModel).weights(16, 20, 17, 15, 17, 15);
        JPanel lower = new JPanel(new BorderLayout(0, Theme.GAP));
        lower.setOpaque(false);
        lower.add(split(titledCard("Actual vs predicted",
                AdminDashboardPanel.chartWithCaption(chart, "Demo dataset: test split, units per quarter, combined model.")),
                titledCard("Model share", donut), 0.68), BorderLayout.CENTER);
        lower.add(titledCard("Dataset preview", preview.wrapper()), BorderLayout.SOUTH);
        JPanel body = new JPanel(new BorderLayout(0, Theme.GAP));
        body.setOpaque(false);
        body.add(row(Theme.GAP, records, bestR2, bestRmse, saved), BorderLayout.NORTH);
        body.add(lower, BorderLayout.CENTER);
        setBody(body);
    }

    @Override
    public void onShow() {
        PredictionService ps = PredictionService.get();
        saved.setValue(String.valueOf(ps.modelsSaved()));
        if (!ps.isReady()) {
            String msg = ps.getError() != null ? ps.getError() : "Training demo models...";
            chart.setMessage(msg);
            records.setValue("-");
            bestR2.setValue("-");
            bestRmse.setValue("-");
            return;
        }
        records.setValue(Money.number(ps.datasetSize()));
        Metrics[] m = ps.getMetrics();
        double r2 = Math.max(m[0].r2, Math.max(m[1].r2, m[2].r2));
        double rmse = Math.min(m[0].rmse, Math.min(m[1].rmse, m[2].rmse));
        bestR2.setValue(String.format("%.3f", r2));
        bestRmse.setValue(String.format("%.2f", rmse));
        chart.setSeries(ps.getTestSeries());
        double[] share = {Math.max(0, m[0].r2), Math.max(0, m[1].r2), Math.max(0, m[2].r2)};
        donut.setData(new String[]{"Random Forest", "Combined", "Linear"}, new double[]{share[1], share[2], share[0]});
        PredictionPanel.DateFmt.fillPreview(previewModel, ps.preview(3));
    }
}
