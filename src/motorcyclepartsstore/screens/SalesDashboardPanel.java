/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import motorcyclepartsstore.model.Sale;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.service.SalesService;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Icons;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.Page;
import motorcyclepartsstore.ui.Screen;
import motorcyclepartsstore.ui.Styler;

/**
 * Sales personnel dashboard (reference page 13). Layout lives in SalesDashboardPanel.form.
 */
public class SalesDashboardPanel extends javax.swing.JPanel implements Screen {

    private final Navigator nav;
    private final DataTable.ListModel model = new DataTable.ListModel("No.", "Customer", "Total", "Status");

    /** Creates new form SalesDashboardPanel */
    public SalesDashboardPanel(Navigator nav) {
        this.nav = nav;
        initComponents();
        Styler.apply(this);
        roleBadge.set(motorcyclepartsstore.service.Session.current().getRole().getLabel(),
                motorcyclepartsstore.service.Session.current().getRole() == motorcyclepartsstore.model.Role.ADMIN
                        ? motorcyclepartsstore.ui.Chip.Kind.GREEN : motorcyclepartsstore.ui.Chip.Kind.BLUE);
        today.setIconKind(Icons.Kind.CART);
        count.setIconKind(Icons.Kind.TREND);
        items.setIconKind(Icons.Kind.BOX);
        avg.setIconKind(Icons.Kind.PLUS);
        recentTable.bind(model, recentHost).weights(18, 32, 28, 22).chipColumn(3);
    }

    @Override
    public void onShow() {
        SalesService s = SalesService.get();
        today.setValue(Money.peso(s.todayTotal()));
        count.setValue(String.valueOf(s.today().size()));
        items.setValue(String.valueOf(s.todayItems()));
        avg.setValue(Money.peso(s.todayAverage()));
        List<Object[]> rows = new ArrayList<>();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("MMM d");
        for (Sale x : s.recent(8)) {
            rows.add(new Object[]{x.getNumber(), x.getCustomer() + "  (" + x.getTime().format(f) + ")",
                Money.peso(x.total()), x.getStatus()});
        }
        model.setRows(rows);
        bars.setData(s.todayByProduct(4), "pcs");
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
        newSaleBtn = new motorcyclepartsstore.ui.RoundButton();
        body = new javax.swing.JPanel();
        cardsRow = new javax.swing.JPanel();
        today = new motorcyclepartsstore.ui.SummaryCard();
        count = new motorcyclepartsstore.ui.SummaryCard();
        items = new motorcyclepartsstore.ui.SummaryCard();
        avg = new motorcyclepartsstore.ui.SummaryCard();
        midRow = new javax.swing.JPanel();
        recentCard = new motorcyclepartsstore.ui.RoundedPanel();
        recentTitle = new javax.swing.JLabel();
        recentHost = new javax.swing.JPanel();
        recentTable = new motorcyclepartsstore.ui.DataTable();
        todayCard = new motorcyclepartsstore.ui.RoundedPanel();
        todayTitle = new javax.swing.JLabel();
        bars = new motorcyclepartsstore.ui.HBarChart();

        pageTitle.setName("pageTitle");
        pageTitle.setText("Dashboard");
        pageSubtitle.setName("pageSubtitle");
        pageSubtitle.setText("Today at the counter");
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
        newSaleBtn.setText("New sale");
        newSaleBtn.addActionListener(this::newSaleBtnActionPerformed);
        actionBox.setOpaque(false);
        actionBox.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        actionBox.add(roleBadge, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        actionBox.add(newSaleBtn, gridBagConstraints);
        headerPanel.setOpaque(false);
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 14, 0));
        headerPanel.setLayout(new java.awt.BorderLayout());
        headerPanel.add(titleBox, java.awt.BorderLayout.WEST);
        headerPanel.add(actionBox, java.awt.BorderLayout.EAST);
        today.setTitle("Sales today");
        count.setTitle("Transactions");
        items.setTitle("Items sold");
        avg.setTitle("Avg. sale");
        cardsRow.setOpaque(false);
        cardsRow.setLayout(new java.awt.GridLayout(1, 4, 16, 0));
        cardsRow.add(today);
        cardsRow.add(count);
        cardsRow.add(items);
        cardsRow.add(avg);
        recentTitle.setName("cardTitle");
        recentTitle.setText("Recent transactions");
        recentHost.setOpaque(false);
        recentHost.setLayout(new java.awt.BorderLayout());
        recentHost.add(recentTable, java.awt.BorderLayout.CENTER);
        recentCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        recentCard.setLayout(new java.awt.BorderLayout());
        recentCard.add(recentTitle, java.awt.BorderLayout.NORTH);
        recentCard.add(recentHost, java.awt.BorderLayout.CENTER);
        todayTitle.setName("cardTitle");
        todayTitle.setText("Sales today");
        todayCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        todayCard.setLayout(new java.awt.BorderLayout());
        todayCard.add(todayTitle, java.awt.BorderLayout.NORTH);
        todayCard.add(bars, java.awt.BorderLayout.CENTER);
        midRow.setOpaque(false);
        midRow.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.68;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        midRow.add(recentCard, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.31999999999999995;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        midRow.add(todayCard, gridBagConstraints);
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

    private void newSaleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newSaleBtnActionPerformed
        nav.navigate(Page.SALES_NEW);
    }//GEN-LAST:event_newSaleBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionBox;
    private motorcyclepartsstore.ui.SummaryCard avg;
    private motorcyclepartsstore.ui.HBarChart bars;
    private javax.swing.JPanel body;
    private javax.swing.JPanel cardsRow;
    private motorcyclepartsstore.ui.SummaryCard count;
    private javax.swing.JPanel headerPanel;
    private motorcyclepartsstore.ui.SummaryCard items;
    private javax.swing.JPanel midRow;
    private motorcyclepartsstore.ui.RoundButton newSaleBtn;
    private javax.swing.JLabel pageSubtitle;
    private javax.swing.JLabel pageTitle;
    private motorcyclepartsstore.ui.RoundedPanel recentCard;
    private javax.swing.JPanel recentHost;
    private motorcyclepartsstore.ui.DataTable recentTable;
    private javax.swing.JLabel recentTitle;
    private motorcyclepartsstore.ui.Chip roleBadge;
    private javax.swing.JPanel titleBox;
    private motorcyclepartsstore.ui.SummaryCard today;
    private motorcyclepartsstore.ui.RoundedPanel todayCard;
    private javax.swing.JLabel todayTitle;
    // End of variables declaration//GEN-END:variables
}
