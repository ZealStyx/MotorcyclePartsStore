/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import motorcyclepartsstore.service.InventoryService;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.service.PredictionService;
import motorcyclepartsstore.service.SalesService;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Icons;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.Screen;
import motorcyclepartsstore.ui.Styler;

/**
 * Admin dashboard (reference page 3). Layout lives in AdminDashboardPanel.form.
 */
public class AdminDashboardPanel extends javax.swing.JPanel implements Screen {

    private final DataTable.ListModel slowModel = new DataTable.ListModel("Part", "Last sold", "Stock");

    /** Creates new form AdminDashboardPanel */
    public AdminDashboardPanel(Navigator nav) {
        initComponents();
        Styler.apply(this);
        roleBadge.set(motorcyclepartsstore.service.Session.current().getRole().getLabel(),
                motorcyclepartsstore.service.Session.current().getRole() == motorcyclepartsstore.model.Role.ADMIN
                        ? motorcyclepartsstore.ui.Chip.Kind.GREEN : motorcyclepartsstore.ui.Chip.Kind.BLUE);
        totalSales.setIconKind(Icons.Kind.CART);
        products.setIconKind(Icons.Kind.BOX);
        lowStock.setIconKind(Icons.Kind.ALERT);
        transactions.setIconKind(Icons.Kind.TREND);
        slowTable.bind(slowModel, slowHost).weights(40, 30, 30);
    }

    @Override
    public void onShow() {
        SalesService sales = SalesService.get();
        InventoryService inv = InventoryService.get();
        totalSales.setValue(Money.peso(sales.totalSales()));
        products.setValue(String.valueOf(inv.all().size()));
        lowStock.setValue(String.valueOf(inv.lowStockCount()));
        transactions.setValue(String.valueOf(sales.transactionCount()));
        bestChart.setData(sales.bestSelling(4), "pcs");
        DateTimeFormatter f = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
        List<Object[]> rows = new ArrayList<>();
        for (Object[] r : sales.slowMoving(4)) {
            rows.add(new Object[]{r[0], r[1] == null ? "Never" : ((LocalDate) r[1]).format(f), r[2]});
        }
        slowModel.setRows(rows);
        PredictionService ps = PredictionService.get();
        if (ps.isReady()) {
            trendChart.setSeries(ps.getTrend());
        } else if (ps.getError() != null) {
            trendChart.setMessage(ps.getError());
        } else {
            trendChart.setMessage("Training demo models...");
        }
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
        totalSales = new motorcyclepartsstore.ui.SummaryCard();
        products = new motorcyclepartsstore.ui.SummaryCard();
        lowStock = new motorcyclepartsstore.ui.SummaryCard();
        transactions = new motorcyclepartsstore.ui.SummaryCard();
        midRow = new javax.swing.JPanel();
        trendCard = new motorcyclepartsstore.ui.RoundedPanel();
        trendTitle = new javax.swing.JLabel();
        trendChart = new motorcyclepartsstore.ui.LineAreaChart();
        trendCaption = new javax.swing.JLabel();
        bestCard = new motorcyclepartsstore.ui.RoundedPanel();
        bestTitle = new javax.swing.JLabel();
        bestChart = new motorcyclepartsstore.ui.HBarChart();
        slowCard = new motorcyclepartsstore.ui.RoundedPanel();
        slowTitle = new javax.swing.JLabel();
        slowHost = new javax.swing.JPanel();
        slowTable = new motorcyclepartsstore.ui.DataTable();

        pageTitle.setName("pageTitle");
        pageTitle.setText("Dashboard");
        pageSubtitle.setName("pageSubtitle");
        pageSubtitle.setText("Admin overview");
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
        totalSales.setTitle("Total sales");
        products.setTitle("Products");
        lowStock.setTitle("Low stock");
        transactions.setTitle("Transactions");
        cardsRow.setOpaque(false);
        cardsRow.setLayout(new java.awt.GridLayout(1, 4, 16, 0));
        cardsRow.add(totalSales);
        cardsRow.add(products);
        cardsRow.add(lowStock);
        cardsRow.add(transactions);
        trendTitle.setName("cardTitle");
        trendTitle.setText("Sales trend and prediction");
        trendCaption.setName("muted");
        trendCaption.setText("Demo dataset: units per quarter, actual vs combined model.");
        trendCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        trendCard.setLayout(new java.awt.BorderLayout());
        trendCard.add(trendTitle, java.awt.BorderLayout.NORTH);
        trendCard.add(trendChart, java.awt.BorderLayout.CENTER);
        trendCard.add(trendCaption, java.awt.BorderLayout.SOUTH);
        bestTitle.setName("cardTitle");
        bestTitle.setText("Best selling");
        bestCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        bestCard.setLayout(new java.awt.BorderLayout());
        bestCard.add(bestTitle, java.awt.BorderLayout.NORTH);
        bestCard.add(bestChart, java.awt.BorderLayout.CENTER);
        midRow.setOpaque(false);
        midRow.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.68;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        midRow.add(trendCard, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.31999999999999995;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        midRow.add(bestCard, gridBagConstraints);
        slowTitle.setName("cardTitle");
        slowTitle.setText("Slow moving products");
        slowHost.setOpaque(false);
        slowHost.setLayout(new java.awt.BorderLayout());
        slowHost.add(slowTable, java.awt.BorderLayout.CENTER);
        slowCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        slowCard.setLayout(new java.awt.BorderLayout());
        slowCard.add(slowTitle, java.awt.BorderLayout.NORTH);
        slowCard.add(slowHost, java.awt.BorderLayout.CENTER);
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
        body.add(slowCard, gridBagConstraints);
        setOpaque(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(28, 34, 28, 34));
        setLayout(new java.awt.BorderLayout());
        add(headerPanel, java.awt.BorderLayout.NORTH);
        add(body, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionBox;
    private motorcyclepartsstore.ui.RoundedPanel bestCard;
    private motorcyclepartsstore.ui.HBarChart bestChart;
    private javax.swing.JLabel bestTitle;
    private javax.swing.JPanel body;
    private javax.swing.JPanel cardsRow;
    private javax.swing.JPanel headerPanel;
    private motorcyclepartsstore.ui.SummaryCard lowStock;
    private javax.swing.JPanel midRow;
    private javax.swing.JLabel pageSubtitle;
    private javax.swing.JLabel pageTitle;
    private motorcyclepartsstore.ui.SummaryCard products;
    private motorcyclepartsstore.ui.Chip roleBadge;
    private motorcyclepartsstore.ui.RoundedPanel slowCard;
    private javax.swing.JPanel slowHost;
    private motorcyclepartsstore.ui.DataTable slowTable;
    private javax.swing.JLabel slowTitle;
    private javax.swing.JPanel titleBox;
    private motorcyclepartsstore.ui.SummaryCard totalSales;
    private motorcyclepartsstore.ui.SummaryCard transactions;
    private javax.swing.JLabel trendCaption;
    private motorcyclepartsstore.ui.RoundedPanel trendCard;
    private motorcyclepartsstore.ui.LineAreaChart trendChart;
    private javax.swing.JLabel trendTitle;
    // End of variables declaration//GEN-END:variables
}
