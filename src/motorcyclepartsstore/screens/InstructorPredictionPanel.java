/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import motorcyclepartsstore.ui.Chip;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.Screen;
import motorcyclepartsstore.ui.Styler;
import motorcyclepartsstore.service.PredictionService;
import motorcyclepartsstore.ui.DataTable;

/**
 * Instructor knowledge analysis and prediction (reference page 9). Layout lives in InstructorPredictionPanel.form.
 */
public class InstructorPredictionPanel extends javax.swing.JPanel implements Screen {

    private final DataTable.ListModel resultModel = new DataTable.ListModel("Part", "Predicted units", "Class");
    private final DataTable.ListModel previewModel =
            new DataTable.ListModel("date", "product", "category", "qty_sold", "unit_price", "stock");
    private final PredictionController controller;

    /** Creates new form InstructorPredictionPanel */
    public InstructorPredictionPanel(Navigator nav) {
        initComponents();
        Styler.apply(this);
        roleBadge.set(motorcyclepartsstore.service.Session.current().getRole().getLabel(),
                motorcyclepartsstore.service.Session.current().getRole() == motorcyclepartsstore.model.Role.ADMIN
                        ? motorcyclepartsstore.ui.Chip.Kind.GREEN : motorcyclepartsstore.ui.Chip.Kind.BLUE);
        resultsTable.bind(resultModel, resultsHost).weights(36, 28, 36).chipColumn(2);
        previewTable.bind(previewModel, previewHost).weights(19, 20, 17, 12, 17, 12);
        linearCard.setChipKind(Chip.Kind.BLUE);
        forestCard.setChipKind(Chip.Kind.BLUE);
        combinedCard.setChipKind(Chip.Kind.GREEN);
        controller = new PredictionController(nav, typeBox, modelBox, productBox, periodBox, status,
                linearCard, forestCard, combinedCard, resultModel, resultsTable);
    }

    @Override
    public void onShow() {
        controller.onShow(() -> {
            PredictionPanelSupport.fillPreview(previewModel, PredictionService.get().preview(3));
            metricsPanel.refresh();
        });
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
        inputCard = new motorcyclepartsstore.ui.RoundedPanel();
        inputTitle = new javax.swing.JLabel();
        typeCaption = new javax.swing.JLabel();
        typeBox = new javax.swing.JComboBox<>();
        modelCaption = new javax.swing.JLabel();
        modelBox = new javax.swing.JComboBox<>();
        productCaption = new javax.swing.JLabel();
        productBox = new javax.swing.JComboBox<>();
        periodCaption = new javax.swing.JLabel();
        periodBox = new javax.swing.JComboBox<>();
        runBtn = new motorcyclepartsstore.ui.RoundButton();
        status = new javax.swing.JLabel();
        demoNote = new javax.swing.JLabel();
        rightPanel = new javax.swing.JPanel();
        cardsRow = new javax.swing.JPanel();
        linearCard = new motorcyclepartsstore.ui.SummaryCard();
        forestCard = new motorcyclepartsstore.ui.SummaryCard();
        combinedCard = new motorcyclepartsstore.ui.SummaryCard();
        midRow = new javax.swing.JPanel();
        resultCard = new motorcyclepartsstore.ui.RoundedPanel();
        resultTitle = new javax.swing.JLabel();
        resultsHost = new javax.swing.JPanel();
        resultsTable = new motorcyclepartsstore.ui.DataTable();
        previewCard = new motorcyclepartsstore.ui.RoundedPanel();
        previewTitle = new javax.swing.JLabel();
        previewHost = new javax.swing.JPanel();
        previewTable = new motorcyclepartsstore.ui.DataTable();
        metricsCard = new motorcyclepartsstore.ui.RoundedPanel();
        metricsTitle = new javax.swing.JLabel();
        metricsPanel = new motorcyclepartsstore.screens.MetricsPanel();

        pageTitle.setName("pageTitle");
        pageTitle.setText("Knowledge analysis and prediction");
        pageSubtitle.setName("pageSubtitle");
        pageSubtitle.setText("Instructor: inputs, outputs, dataset, metrics and description");
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
        inputTitle.setName("cardTitle");
        inputTitle.setText("Input");
        typeCaption.setName("caption");
        typeCaption.setText("Prediction type");
        typeBox.setName("field");
        typeBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Product demand", "Category demand" }));
        modelCaption.setName("caption");
        modelCaption.setText("Model");
        modelBox.setName("field");
        modelBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Simple Linear Regression", "Random Forest Regression", "Combined (Linear + Random Forest)" }));
        productCaption.setName("caption");
        productCaption.setText("Product");
        productBox.setName("field");
        productBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All products" }));
        periodCaption.setName("caption");
        periodCaption.setText("Period");
        periodBox.setName("field");
        periodBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Next week", "Next month", "Next quarter" }));
        runBtn.setText("Run prediction");
        runBtn.setPreferredSize(new java.awt.Dimension(100, 44));
        runBtn.addActionListener(this::runBtnActionPerformed);
        status.setName("message");
        status.setText(" ");
        demoNote.setName("muted");
        demoNote.setText("<html><body style='width:230px'>Demo data: Weka models trained on a generated weekly dataset, not real shop sales.</body></html>");
        inputCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        inputCard.setPreferredSize(new java.awt.Dimension(300, 100));
        inputCard.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        inputCard.add(inputTitle, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        inputCard.add(typeCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        inputCard.add(typeBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        inputCard.add(modelCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        inputCard.add(modelBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        inputCard.add(productCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        inputCard.add(productBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        inputCard.add(periodCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        inputCard.add(periodBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        inputCard.add(runBtn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        inputCard.add(status, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        inputCard.add(demoNote, gridBagConstraints);
        linearCard.setTitle("Simple Linear Regression");
        linearCard.setChipText("Linear");
        forestCard.setTitle("Random Forest Regression");
        forestCard.setChipText("Forest");
        combinedCard.setTitle("Combined result");
        combinedCard.setChipText("Combined");
        cardsRow.setOpaque(false);
        cardsRow.setLayout(new java.awt.GridLayout(1, 3, 16, 0));
        cardsRow.add(linearCard);
        cardsRow.add(forestCard);
        cardsRow.add(combinedCard);
        resultTitle.setName("cardTitle");
        resultTitle.setText("Prediction results");
        resultsHost.setOpaque(false);
        resultsHost.setLayout(new java.awt.BorderLayout());
        resultsHost.add(resultsTable, java.awt.BorderLayout.CENTER);
        resultCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        resultCard.setLayout(new java.awt.BorderLayout());
        resultCard.add(resultTitle, java.awt.BorderLayout.NORTH);
        resultCard.add(resultsHost, java.awt.BorderLayout.CENTER);
        previewTitle.setName("cardTitle");
        previewTitle.setText("Dataset preview");
        previewHost.setOpaque(false);
        previewHost.setLayout(new java.awt.BorderLayout());
        previewHost.add(previewTable, java.awt.BorderLayout.CENTER);
        previewCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        previewCard.setLayout(new java.awt.BorderLayout());
        previewCard.add(previewTitle, java.awt.BorderLayout.NORTH);
        previewCard.add(previewHost, java.awt.BorderLayout.CENTER);
        midRow.setOpaque(false);
        midRow.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.46;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        midRow.add(resultCard, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.54;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        midRow.add(previewCard, gridBagConstraints);
        metricsTitle.setName("cardTitle");
        metricsTitle.setText("Evaluation metrics");
        metricsCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        metricsCard.setLayout(new java.awt.BorderLayout());
        metricsCard.add(metricsTitle, java.awt.BorderLayout.NORTH);
        metricsCard.add(metricsPanel, java.awt.BorderLayout.CENTER);
        rightPanel.setOpaque(false);
        rightPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 0, 0));
        rightPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 16, 0);
        rightPanel.add(cardsRow, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 16, 0);
        rightPanel.add(midRow, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rightPanel.add(metricsCard, gridBagConstraints);
        body.setOpaque(false);
        body.setLayout(new java.awt.BorderLayout());
        body.add(inputCard, java.awt.BorderLayout.WEST);
        body.add(rightPanel, java.awt.BorderLayout.CENTER);
        setOpaque(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(28, 34, 28, 34));
        setLayout(new java.awt.BorderLayout());
        add(headerPanel, java.awt.BorderLayout.NORTH);
        add(body, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void runBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runBtnActionPerformed
        controller.run();
    }//GEN-LAST:event_runBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionBox;
    private javax.swing.JPanel body;
    private javax.swing.JPanel cardsRow;
    private motorcyclepartsstore.ui.SummaryCard combinedCard;
    private javax.swing.JLabel demoNote;
    private motorcyclepartsstore.ui.SummaryCard forestCard;
    private javax.swing.JPanel headerPanel;
    private motorcyclepartsstore.ui.RoundedPanel inputCard;
    private javax.swing.JLabel inputTitle;
    private motorcyclepartsstore.ui.SummaryCard linearCard;
    private motorcyclepartsstore.ui.RoundedPanel metricsCard;
    private motorcyclepartsstore.screens.MetricsPanel metricsPanel;
    private javax.swing.JLabel metricsTitle;
    private javax.swing.JPanel midRow;
    private javax.swing.JComboBox<String> modelBox;
    private javax.swing.JLabel modelCaption;
    private javax.swing.JLabel pageSubtitle;
    private javax.swing.JLabel pageTitle;
    private javax.swing.JComboBox<String> periodBox;
    private javax.swing.JLabel periodCaption;
    private motorcyclepartsstore.ui.RoundedPanel previewCard;
    private javax.swing.JPanel previewHost;
    private motorcyclepartsstore.ui.DataTable previewTable;
    private javax.swing.JLabel previewTitle;
    private javax.swing.JComboBox<String> productBox;
    private javax.swing.JLabel productCaption;
    private motorcyclepartsstore.ui.RoundedPanel resultCard;
    private javax.swing.JLabel resultTitle;
    private javax.swing.JPanel resultsHost;
    private motorcyclepartsstore.ui.DataTable resultsTable;
    private javax.swing.JPanel rightPanel;
    private motorcyclepartsstore.ui.Chip roleBadge;
    private motorcyclepartsstore.ui.RoundButton runBtn;
    private javax.swing.JLabel status;
    private javax.swing.JPanel titleBox;
    private javax.swing.JComboBox<String> typeBox;
    private javax.swing.JLabel typeCaption;
    // End of variables declaration//GEN-END:variables
}
