/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import motorcyclepartsstore.service.PredictionService.Metrics;
import motorcyclepartsstore.ui.Styler;
import motorcyclepartsstore.ui.Theme;

/**
 * One 2x2 confusion matrix (TN/FP/FN/TP). Layout lives in ConfusionMatrix.form.
 */
public class ConfusionMatrix extends javax.swing.JPanel {

    private String modelName = "Linear";

    /** Creates new form ConfusionMatrix */
    public ConfusionMatrix() {
        initComponents();
        Styler.apply(this);
        for (JLabel l : new JLabel[]{cornerCell, predSlowHead, predFastHead, actualSlowHead, tnCell, fpCell,
            actualFastHead, fnCell, tpCell}) {
            l.setOpaque(true);
            l.setBackground(java.awt.Color.WHITE);
            l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));
            l.setPreferredSize(new java.awt.Dimension(70, 40));
        }
        tnCell.setBackground(Theme.ACCENT_TINT);
        tpCell.setBackground(Theme.ACCENT_TINT);
        tnCell.setToolTipText("True negative (TN)");
        fpCell.setToolTipText("False positive (FP)");
        fnCell.setToolTipText("False negative (FN)");
        tpCell.setToolTipText("True positive (TP)");
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String name) {
        this.modelName = name;
        titleLabel.setText("Confusion matrix: " + name);
    }

    public void setMetrics(Metrics m) {
        tnCell.setText(String.valueOf(m.tn));
        fpCell.setText(String.valueOf(m.fp));
        fnCell.setText(String.valueOf(m.fn));
        tpCell.setText(String.valueOf(m.tp));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        grid = new javax.swing.JPanel();
        cornerCell = new javax.swing.JLabel();
        predSlowHead = new javax.swing.JLabel();
        predFastHead = new javax.swing.JLabel();
        actualSlowHead = new javax.swing.JLabel();
        tnCell = new javax.swing.JLabel();
        fpCell = new javax.swing.JLabel();
        actualFastHead = new javax.swing.JLabel();
        fnCell = new javax.swing.JLabel();
        tpCell = new javax.swing.JLabel();

        titleLabel.setName("muted");
        titleLabel.setText("Confusion matrix");
        cornerCell.setName("matrixHead");
        cornerCell.setText("");
        cornerCell.setHorizontalAlignment(0);
        predSlowHead.setName("matrixHead");
        predSlowHead.setText("Pred. slow");
        predSlowHead.setHorizontalAlignment(0);
        predFastHead.setName("matrixHead");
        predFastHead.setText("Pred. fast");
        predFastHead.setHorizontalAlignment(0);
        actualSlowHead.setName("matrixHead");
        actualSlowHead.setText("Actual slow");
        actualSlowHead.setHorizontalAlignment(0);
        tnCell.setName("matrixCell");
        tnCell.setText("-");
        tnCell.setHorizontalAlignment(0);
        fpCell.setName("matrixCell");
        fpCell.setText("-");
        fpCell.setHorizontalAlignment(0);
        actualFastHead.setName("matrixHead");
        actualFastHead.setText("Actual fast");
        actualFastHead.setHorizontalAlignment(0);
        fnCell.setName("matrixCell");
        fnCell.setText("-");
        fnCell.setHorizontalAlignment(0);
        tpCell.setName("matrixCell");
        tpCell.setText("-");
        tpCell.setHorizontalAlignment(0);
        grid.setOpaque(false);
        grid.setLayout(new java.awt.GridLayout(3, 3, 0, 0));
        grid.add(cornerCell);
        grid.add(predSlowHead);
        grid.add(predFastHead);
        grid.add(actualSlowHead);
        grid.add(tnCell);
        grid.add(fpCell);
        grid.add(actualFastHead);
        grid.add(fnCell);
        grid.add(tpCell);
        setOpaque(false);
        setLayout(new java.awt.BorderLayout());
        add(titleLabel, java.awt.BorderLayout.NORTH);
        add(grid, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel actualFastHead;
    private javax.swing.JLabel actualSlowHead;
    private javax.swing.JLabel cornerCell;
    private javax.swing.JLabel fnCell;
    private javax.swing.JLabel fpCell;
    private javax.swing.JPanel grid;
    private javax.swing.JLabel predFastHead;
    private javax.swing.JLabel predSlowHead;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel tnCell;
    private javax.swing.JLabel tpCell;
    // End of variables declaration//GEN-END:variables
}
