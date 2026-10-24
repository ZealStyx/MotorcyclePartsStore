package motorcyclepartsstore.screens;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import motorcyclepartsstore.service.PredictionService;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Dialogs;
import motorcyclepartsstore.ui.Forms;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.SummaryCard;

/** Behavior shared by the Admin and Instructor prediction forms (inputs, run, results table). */
final class PredictionController {

    private final Navigator nav;
    private final JComboBox<String> type;
    private final JComboBox<String> model;
    private final JComboBox<String> product;
    private final JComboBox<String> period;
    private final JLabel status;
    private final SummaryCard linear;
    private final SummaryCard forest;
    private final SummaryCard combined;
    private final DataTable.ListModel resultModel;
    private final DataTable resultsTable;
    private boolean ranOnce;
    private boolean refilling;

    PredictionController(Navigator nav, JComboBox<String> type, JComboBox<String> model, JComboBox<String> product,
            JComboBox<String> period, JLabel status, SummaryCard linear, SummaryCard forest, SummaryCard combined,
            DataTable.ListModel resultModel, DataTable resultsTable) {
        this.nav = nav;
        this.type = type;
        this.model = model;
        this.product = product;
        this.period = period;
        this.status = status;
        this.linear = linear;
        this.forest = forest;
        this.combined = combined;
        this.resultModel = resultModel;
        this.resultsTable = resultsTable;
        model.setSelectedIndex(2);   // Combined, as in the reference design
        period.setSelectedIndex(1);  // Next month
        type.addActionListener(e -> refillProducts());
        refillProducts();
    }

    private void refillProducts() {
        PredictionService ps = PredictionService.get();
        boolean cat = type.getSelectedIndex() == 1;
        refilling = true;
        product.removeAllItems();
        product.addItem(cat ? PredictionService.ALL_CATEGORIES : PredictionService.ALL_PRODUCTS);
        for (String s : cat ? ps.categoryNames() : ps.productNames()) {
            product.addItem(s);
        }
        refilling = false;
        resultModel.setColumnName(0, cat ? "Category" : "Part");
        resultsTable.weights(36, 28, 36).chipColumn(2);
    }

    /** Called from Screen.onShow; extra runs once models are ready. */
    void onShow(Runnable whenReady) {
        PredictionService ps = PredictionService.get();
        if (!ps.isReady()) {
            Forms.error(status, ps.getError() != null ? ps.getError() : "Demo models are still training...");
            return;
        }
        if (status.getText().contains("training")) {
            status.setText(" ");
        }
        if (whenReady != null) {
            whenReady.run();
        }
        if (!ranOnce) {
            run();
        }
    }

    void run() {
        PredictionService ps = PredictionService.get();
        if (!ps.isReady()) {
            Forms.error(status, ps.getError() != null ? ps.getError() : "Demo models are still training. Try again in a moment.");
            return;
        }
        try {
            PredictionService.Result r = ps.predict((String) type.getSelectedItem(), model.getSelectedIndex(),
                    (String) product.getSelectedItem(), (String) period.getSelectedItem());
            int mi = model.getSelectedIndex();
            List<Object[]> rows = new ArrayList<>();
            for (PredictionService.ResultRow x : r.rows) {
                rows.add(new Object[]{x.name, PredictionPanelSupport.units(x.units(mi)), x.cls});
            }
            resultModel.setRows(rows);
            linear.setValue(PredictionPanelSupport.units(r.totalLinear));
            forest.setValue(PredictionPanelSupport.units(r.totalForest));
            combined.setValue(PredictionPanelSupport.units(r.totalCombined));
            ranOnce = true;
            Forms.success(status, "Prediction updated.");
        } catch (Exception | LinkageError ex) {
            Dialogs.message(nav.window(), "Prediction failed", String.valueOf(ex.getMessage()));
        }
    }
}
