/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.service.PredictionService;
import motorcyclepartsstore.ui.Dialogs;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.Screen;
import motorcyclepartsstore.ui.Styler;

/**
 * Instructor model evaluation (reference page 10). Layout lives in EvaluationPanel.form.
 */
public class EvaluationPanel extends javax.swing.JPanel implements Screen {

    private final Navigator nav;

    /** Creates new form EvaluationPanel */
    public EvaluationPanel(Navigator nav) {
        this.nav = nav;
        initComponents();
        Styler.apply(this);
    }

    private static JFileChooser chooser() {
        PredictionService.MODELS_DIR.mkdirs();
        JFileChooser fc = new JFileChooser(PredictionService.MODELS_DIR.getAbsoluteFile());
        fc.setFileFilter(new FileNameExtensionFilter("One Prompt Away model (*.model)", "model"));
        return fc;
    }

    private void saveModel() {
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

    private void loadModel() {
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
        metricsPanel.refresh();
        chart.setSeries(ps.getTestSeries());
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
        loadBtn = new motorcyclepartsstore.ui.SecondaryButton();
        saveBtn = new motorcyclepartsstore.ui.RoundButton();
        body = new javax.swing.JPanel();
        cardsRow = new javax.swing.JPanel();
        split = new motorcyclepartsstore.ui.SummaryCard();
        training = new motorcyclepartsstore.ui.SummaryCard();
        testing = new motorcyclepartsstore.ui.SummaryCard();
        midRow = new javax.swing.JPanel();
        metricsCard = new motorcyclepartsstore.ui.RoundedPanel();
        metricsTitle = new javax.swing.JLabel();
        metricsPanel = new motorcyclepartsstore.screens.MetricsPanel();
        chartCard = new motorcyclepartsstore.ui.RoundedPanel();
        chartTitle = new javax.swing.JLabel();
        chart = new motorcyclepartsstore.ui.LineAreaChart();
        chartCaption = new javax.swing.JLabel();

        pageTitle.setName("pageTitle");
        pageTitle.setText("Model evaluation");
        pageSubtitle.setName("pageSubtitle");
        pageSubtitle.setText("Split, training, testing and 10-fold cross validation");
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
        loadBtn.setText("Load model");
        loadBtn.addActionListener(this::loadBtnActionPerformed);
        saveBtn.setText("Save model");
        saveBtn.addActionListener(this::saveBtnActionPerformed);
        actionBox.setOpaque(false);
        actionBox.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        actionBox.add(loadBtn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        actionBox.add(saveBtn, gridBagConstraints);
        headerPanel.setOpaque(false);
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 14, 0));
        headerPanel.setLayout(new java.awt.BorderLayout());
        headerPanel.add(titleBox, java.awt.BorderLayout.WEST);
        headerPanel.add(actionBox, java.awt.BorderLayout.EAST);
        split.setTitle("Data split");
        training.setTitle("Training records");
        testing.setTitle("Testing records");
        cardsRow.setOpaque(false);
        cardsRow.setLayout(new java.awt.GridLayout(1, 3, 16, 0));
        cardsRow.add(split);
        cardsRow.add(training);
        cardsRow.add(testing);
        metricsTitle.setName("cardTitle");
        metricsTitle.setText("Evaluation metrics and description");
        metricsPanel.setNote("Combined model: average of Simple Linear Regression and Random Forest predictions.");
        metricsCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        metricsCard.setLayout(new java.awt.BorderLayout());
        metricsCard.add(metricsTitle, java.awt.BorderLayout.NORTH);
        metricsCard.add(metricsPanel, java.awt.BorderLayout.CENTER);
        chartTitle.setName("cardTitle");
        chartTitle.setText("Actual vs predicted");
        chartCaption.setName("muted");
        chartCaption.setText("Demo dataset: test split, units per quarter, combined model.");
        chartCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        chartCard.setLayout(new java.awt.BorderLayout());
        chartCard.add(chartTitle, java.awt.BorderLayout.NORTH);
        chartCard.add(chart, java.awt.BorderLayout.CENTER);
        chartCard.add(chartCaption, java.awt.BorderLayout.SOUTH);
        midRow.setOpaque(false);
        midRow.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.68;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        midRow.add(metricsCard, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.31999999999999995;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        midRow.add(chartCard, gridBagConstraints);
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
        body.add(midRow, gridBagConstraints);
        setOpaque(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(28, 34, 28, 34));
        setLayout(new java.awt.BorderLayout());
        add(headerPanel, java.awt.BorderLayout.NORTH);
        add(body, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void loadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadBtnActionPerformed
        loadModel();
    }//GEN-LAST:event_loadBtnActionPerformed
    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        saveModel();
    }//GEN-LAST:event_saveBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionBox;
    private javax.swing.JPanel body;
    private javax.swing.JPanel cardsRow;
    private motorcyclepartsstore.ui.LineAreaChart chart;
    private javax.swing.JLabel chartCaption;
    private motorcyclepartsstore.ui.RoundedPanel chartCard;
    private javax.swing.JLabel chartTitle;
    private javax.swing.JPanel headerPanel;
    private motorcyclepartsstore.ui.SecondaryButton loadBtn;
    private motorcyclepartsstore.ui.RoundedPanel metricsCard;
    private motorcyclepartsstore.screens.MetricsPanel metricsPanel;
    private javax.swing.JLabel metricsTitle;
    private javax.swing.JPanel midRow;
    private javax.swing.JLabel pageSubtitle;
    private javax.swing.JLabel pageTitle;
    private motorcyclepartsstore.ui.RoundButton saveBtn;
    private motorcyclepartsstore.ui.SummaryCard split;
    private motorcyclepartsstore.ui.SummaryCard testing;
    private javax.swing.JPanel titleBox;
    private motorcyclepartsstore.ui.SummaryCard training;
    // End of variables declaration//GEN-END:variables
}
