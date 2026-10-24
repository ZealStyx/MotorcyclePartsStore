/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.service.PredictionService;
import motorcyclepartsstore.service.PredictionService.Metrics;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Icons;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.Screen;
import motorcyclepartsstore.ui.Styler;

/**
 * Instructor dashboard (reference page 8). Layout lives in InstructorDashboardPanel.form.
 */
public class InstructorDashboardPanel extends javax.swing.JPanel implements Screen {

    private final DataTable.ListModel previewModel =
            new DataTable.ListModel("date", "product", "category", "qty_sold", "unit_price", "stock");

    /** Creates new form InstructorDashboardPanel */
    public InstructorDashboardPanel(Navigator nav) {
        initComponents();
        Styler.apply(this);
        roleBadge.set(motorcyclepartsstore.service.Session.current().getRole().getLabel(),
                motorcyclepartsstore.service.Session.current().getRole() == motorcyclepartsstore.model.Role.ADMIN
                        ? motorcyclepartsstore.ui.Chip.Kind.GREEN : motorcyclepartsstore.ui.Chip.Kind.BLUE);
        records.setIconKind(Icons.Kind.BOX);
        bestR2.setIconKind(Icons.Kind.BARS);
        bestRmse.setIconKind(Icons.Kind.TREND);
        saved.setIconKind(Icons.Kind.PLUS);
        previewTable.bind(previewModel, previewHost).weights(16, 20, 17, 15, 17, 15);
    }

    @Override
    public void onShow() {
        PredictionService ps = PredictionService.get();
        saved.setValue(String.valueOf(ps.modelsSaved()));
        if (!ps.isReady()) {
            chart.setMessage(ps.getError() != null ? ps.getError() : "Training demo models...");
            records.setValue("-");
            bestR2.setValue("-");
            bestRmse.setValue("-");
            return;
        }
        records.setValue(Money.number(ps.datasetSize()));
        Metrics[] m = ps.getMetrics();
        bestR2.setValue(String.format("%.3f", Math.max(m[0].r2, Math.max(m[1].r2, m[2].r2))));
        bestRmse.setValue(String.format("%.2f", Math.min(m[0].rmse, Math.min(m[1].rmse, m[2].rmse))));
        chart.setSeries(ps.getTestSeries());
        donut.setData(new String[]{"Random Forest", "Combined", "Linear"},
                new double[]{Math.max(0, m[1].r2), Math.max(0, m[2].r2), Math.max(0, m[0].r2)});
        PredictionPanelSupport.fillPreview(previewModel, ps.preview(3));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        java.awt.GridBagConstraints gridBagConstraints;

        headerPanel = new javax.swing.JPanel();
        titleBox = new javax.swing.JPanel();
        pageTitle = new javax.swing.JLabel();
        pageSubtitle = new javax.swing.JLabel();
        actionBox = new javax.swing.JPanel();
        roleBadge = new motorcyclepartsstore.ui.Chip();
        body = new javax.swing.JPanel();
        cardsRow = new javax.swing.JPanel();
        records = new motorcyclepartsstore.ui.SummaryCard();
        bestR2 = new motorcyclepartsstore.ui.SummaryCard();
        bestRmse = new motorcyclepartsstore.ui.SummaryCard();
        saved = new motorcyclepartsstore.ui.SummaryCard();
        midRow = new javax.swing.JPanel();
        chartCard = new motorcyclepartsstore.ui.RoundedPanel();
        chartTitle = new javax.swing.JLabel();
        chart = new motorcyclepartsstore.ui.LineAreaChart();
        chartCaption = new javax.swing.JLabel();
        shareCard = new motorcyclepartsstore.ui.RoundedPanel();
        shareTitle = new javax.swing.JLabel();
        donut = new motorcyclepartsstore.ui.DonutChart();
        previewCard = new motorcyclepartsstore.ui.RoundedPanel();
        previewTitle = new javax.swing.JLabel();
        previewHost = new javax.swing.JPanel();
        previewTable = new motorcyclepartsstore.ui.DataTable();

        pageTitle.setName("pageTitle");
        pageTitle.setText("Dashboard");
        pageSubtitle.setName("pageSubtitle");
        pageSubtitle.setText("Instructor overview, models and data");
        titleBox.setOpaque(false);
        titleBox.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        titleBox.add(pageTitle, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        titleBox.add(pageSubtitle, gridBagConstraints);
        actionBox.setOpaque(false);
        actionBox.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        actionBox.add(roleBadge, gridBagConstraints);
        headerPanel.setOpaque(false);
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 14, 0));
        headerPanel.setLayout(new java.awt.BorderLayout());
        headerPanel.add(titleBox, java.awt.BorderLayout.WEST);
        headerPanel.add(actionBox, java.awt.BorderLayout.EAST);
        records.setTitle("Dataset records");
        bestR2.setTitle("Best R²");
        bestRmse.setTitle("Best RMSE");
        saved.setTitle("Models saved");
        cardsRow.setOpaque(false);
        cardsRow.setLayout(new java.awt.GridLayout(1, 4, 16, 0));
        cardsRow.add(records);
        cardsRow.add(bestR2);
        cardsRow.add(bestRmse);
        cardsRow.add(saved);
        chartTitle.setName("cardTitle");
        chartTitle.setText("Actual vs predicted");
        chartCaption.setName("muted");
        chartCaption.setText("Demo dataset: test split, units per quarter, combined model.");
        chartCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        chartCard.setLayout(new java.awt.BorderLayout());
        chartCard.add(chartTitle, java.awt.BorderLayout.NORTH);
        chartCard.add(chart, java.awt.BorderLayout.CENTER);
        chartCard.add(chartCaption, java.awt.BorderLayout.SOUTH);
        shareTitle.setName("cardTitle");
        shareTitle.setText("Model share");
        shareCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        shareCard.setLayout(new java.awt.BorderLayout());
        shareCard.add(shareTitle, java.awt.BorderLayout.NORTH);
        shareCard.add(donut, java.awt.BorderLayout.CENTER);
        midRow.setOpaque(false);
        midRow.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.68;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        midRow.add(chartCard, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.31999999999999995;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        midRow.add(shareCard, gridBagConstraints);
        previewTitle.setName("cardTitle");
        previewTitle.setText("Dataset preview");
        previewHost.setOpaque(false);
        previewHost.setLayout(new java.awt.BorderLayout());
        previewHost.add(previewTable, java.awt.BorderLayout.CENTER);
        previewCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        previewCard.setLayout(new java.awt.BorderLayout());
        previewCard.add(previewTitle, java.awt.BorderLayout.NORTH);
        previewCard.add(previewHost, java.awt.BorderLayout.CENTER);
        body.setOpaque(false);
        body.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 16, 0);
        body.add(cardsRow, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 16, 0);
        body.add(midRow, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        body.add(previewCard, gridBagConstraints);
        setOpaque(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(28, 34, 28, 34));
        setLayout(new java.awt.BorderLayout());
        add(headerPanel, java.awt.BorderLayout.NORTH);
        add(body, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionBox;
    private motorcyclepartsstore.ui.SummaryCard bestR2;
    private motorcyclepartsstore.ui.SummaryCard bestRmse;
    private javax.swing.JPanel body;
    private javax.swing.JPanel cardsRow;
    private motorcyclepartsstore.ui.LineAreaChart chart;
    private javax.swing.JLabel chartCaption;
    private motorcyclepartsstore.ui.RoundedPanel chartCard;
    private javax.swing.JLabel chartTitle;
    private motorcyclepartsstore.ui.DonutChart donut;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel midRow;
    private javax.swing.JLabel pageSubtitle;
    private javax.swing.JLabel pageTitle;
    private motorcyclepartsstore.ui.RoundedPanel previewCard;
    private javax.swing.JPanel previewHost;
    private motorcyclepartsstore.ui.DataTable previewTable;
    private javax.swing.JLabel previewTitle;
    private motorcyclepartsstore.ui.SummaryCard records;
    private motorcyclepartsstore.ui.Chip roleBadge;
    private motorcyclepartsstore.ui.SummaryCard saved;
    private motorcyclepartsstore.ui.RoundedPanel shareCard;
    private javax.swing.JLabel shareTitle;
    private javax.swing.JPanel titleBox;
    // End of variables declaration//GEN-END:variables
}
