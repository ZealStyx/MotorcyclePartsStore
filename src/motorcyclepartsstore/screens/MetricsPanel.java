/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import java.util.ArrayList;
import java.util.List;
import motorcyclepartsstore.service.PredictionService;
import motorcyclepartsstore.service.PredictionService.Metrics;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Styler;

/**
 * Evaluation metrics table plus the three confusion matrices, shared by the Instructor prediction and
 * evaluation pages. Layout lives in MetricsPanel.form.
 */
public class MetricsPanel extends javax.swing.JPanel {

    private final DataTable.ListModel model =
            new DataTable.ListModel("Metric", "Linear", "Random Forest", "Combined", "Description");

    /** Creates new form MetricsPanel */
    public MetricsPanel() {
        initComponents();
        Styler.apply(this);
        metricsTable.bind(model, metricsHost).weights(26, 11, 14, 12, 37);
    }

    public String getNote() {
        return noteLabel.getText();
    }

    public void setNote(String note) {
        noteLabel.setText(note == null || note.isEmpty() ? " " : note);
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
        linearMatrix.setMetrics(m[0]);
        forestMatrix.setMetrics(m[1]);
        combinedMatrix.setMetrics(m[2]);
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        java.awt.GridBagConstraints gridBagConstraints;

        metricsHost = new javax.swing.JPanel();
        metricsTable = new motorcyclepartsstore.ui.DataTable();
        matricesRow = new javax.swing.JPanel();
        linearMatrix = new motorcyclepartsstore.screens.ConfusionMatrix();
        forestMatrix = new motorcyclepartsstore.screens.ConfusionMatrix();
        combinedMatrix = new motorcyclepartsstore.screens.ConfusionMatrix();
        noteLabel = new javax.swing.JLabel();

        metricsHost.setOpaque(false);
        metricsHost.setLayout(new java.awt.BorderLayout());
        metricsHost.add(metricsTable, java.awt.BorderLayout.CENTER);
        linearMatrix.setModelName("Linear");
        forestMatrix.setModelName("Random Forest");
        combinedMatrix.setModelName("Combined");
        matricesRow.setOpaque(false);
        matricesRow.setLayout(new java.awt.GridLayout(1, 3, 20, 0));
        matricesRow.add(linearMatrix);
        matricesRow.add(forestMatrix);
        matricesRow.add(combinedMatrix);
        noteLabel.setName("muted");
        noteLabel.setText(" ");
        setOpaque(false);
        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 16, 0);
        add(metricsHost, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(matricesRow, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(noteLabel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private motorcyclepartsstore.screens.ConfusionMatrix combinedMatrix;
    private motorcyclepartsstore.screens.ConfusionMatrix forestMatrix;
    private motorcyclepartsstore.screens.ConfusionMatrix linearMatrix;
    private javax.swing.JPanel matricesRow;
    private javax.swing.JPanel metricsHost;
    private motorcyclepartsstore.ui.DataTable metricsTable;
    private javax.swing.JLabel noteLabel;
    // End of variables declaration//GEN-END:variables
}
