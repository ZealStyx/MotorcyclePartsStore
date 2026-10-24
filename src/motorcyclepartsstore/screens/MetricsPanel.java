package motorcyclepartsstore.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import motorcyclepartsstore.service.PredictionService;
import motorcyclepartsstore.service.PredictionService.Metrics;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Forms;
import motorcyclepartsstore.ui.Theme;

/** Evaluation metrics table plus the three confusion matrices (shared by two instructor pages). */
public class MetricsPanel extends JPanel {

    private final DataTable.ListModel model = new DataTable.ListModel("Metric", "Linear", "Random Forest", "Combined", "Description");
    private final Matrix[] matrices = {new Matrix("Linear"), new Matrix("Random Forest"), new Matrix("Combined")};

    public MetricsPanel(String note) {
        super(new BorderLayout(0, 16));
        setOpaque(false);
        DataTable t = new DataTable(model).weights(26, 11, 14, 12, 37);
        add(t.wrapper(), BorderLayout.NORTH);
        JPanel south = new JPanel(new BorderLayout(0, 10));
        south.setOpaque(false);
        JPanel grid = new JPanel(new GridLayout(1, 3, 20, 0));
        grid.setOpaque(false);
        for (Matrix m : matrices) {
            grid.add(m);
        }
        south.add(grid, BorderLayout.NORTH);
        if (note != null) {
            south.add(Forms.muted(note, 12), BorderLayout.CENTER);
        }
        add(south, BorderLayout.CENTER);
    }

    public void refresh() {
        Metrics[] m = PredictionService.get().getMetrics();
        List<Object[]> rows = new ArrayList<>();
        if (m == null) {
            model.setRows(rows);
            return;
        }
        rows.add(row("MAE", m, 0, 2, "Average absolute error"));
        rows.add(row("RMSE", m, 1, 2, "Penalizes large errors"));
        rows.add(row("R\u00B2", m, 2, 3, "Variance explained"));
        rows.add(row("Correlation coefficient (r)", m, 3, 3, "Strength of actual vs predicted relation"));
        rows.add(row("Accuracy", m, 4, 3, "Correct slow/fast classifications"));
        rows.add(row("Precision", m, 5, 3, "Share of predicted fast that are fast"));
        rows.add(row("Recall", m, 6, 3, "Share of actual fast found"));
        rows.add(row("F1 score", m, 7, 3, "Balance of precision and recall"));
        rows.add(row("10-fold CV", m, 8, 3, "Mean R\u00B2 score over 10 folds"));
        model.setRows(rows);
        for (int i = 0; i < 3; i++) {
            matrices[i].set(m[i]);
        }
    }

    private static double value(Metrics x, int k) {
        switch (k) {
            case 0: return x.mae;
            case 1: return x.rmse;
            case 2: return x.r2;
            case 3: return x.r;
            case 4: return x.accuracy;
            case 5: return x.precision;
            case 6: return x.recall;
            case 7: return x.f1;
            default: return x.cv;
        }
    }

    private static Object[] row(String name, Metrics[] m, int k, int decimals, String desc) {
        String f = "%." + decimals + "f";
        return new Object[]{name, String.format(f, value(m[0], k)), String.format(f, value(m[1], k)),
            String.format(f, value(m[2], k)), desc};
    }

    /** One 2x2 confusion matrix. */
    private static final class Matrix extends JPanel {

        private final JLabel[] cells = new JLabel[4];

        Matrix(String name) {
            super(new BorderLayout(0, 6));
            setOpaque(false);
            add(Forms.muted("Confusion matrix: " + name, 12), BorderLayout.NORTH);
            JPanel g = new JPanel(new GridLayout(3, 3));
            g.setOpaque(false);
            g.add(cell("", false, false));
            g.add(cell("Pred. slow", false, true));
            g.add(cell("Pred. fast", false, true));
            g.add(cell("Actual slow", false, true));
            cells[0] = cell("-", true, false);
            cells[1] = cell("-", false, false);
            g.add(cells[0]);
            g.add(cells[1]);
            g.add(cell("Actual fast", false, true));
            cells[2] = cell("-", false, false);
            cells[3] = cell("-", true, false);
            g.add(cells[2]);
            g.add(cells[3]);
            cells[0].setToolTipText("True negative (TN)");
            cells[1].setToolTipText("False positive (FP)");
            cells[2].setToolTipText("False negative (FN)");
            cells[3].setToolTipText("True positive (TP)");
            add(g, BorderLayout.CENTER);
        }

        private static JLabel cell(String text, boolean tint, boolean header) {
            JLabel l = new JLabel(text, SwingConstants.CENTER);
            l.setFont(header ? Theme.bold(12) : Theme.body(14));
            l.setForeground(header ? Theme.HEADER_TEXT : Theme.INK);
            l.setOpaque(true);
            l.setBackground(tint ? Theme.ACCENT_TINT : Color.WHITE);
            l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));
            l.setPreferredSize(new java.awt.Dimension(70, 40));
            return l;
        }

        void set(Metrics m) {
            cells[0].setText(String.valueOf(m.tn));
            cells[1].setText(String.valueOf(m.fp));
            cells[2].setText(String.valueOf(m.fn));
            cells[3].setText(String.valueOf(m.tp));
        }
    }
}
