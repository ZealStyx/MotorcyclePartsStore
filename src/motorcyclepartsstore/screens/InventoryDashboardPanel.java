/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import motorcyclepartsstore.model.Product;
import motorcyclepartsstore.service.InventoryService;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Icons;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.Screen;
import motorcyclepartsstore.ui.Styler;

/**
 * Inventory clerk dashboard (reference page 11). Layout lives in InventoryDashboardPanel.form.
 */
public class InventoryDashboardPanel extends javax.swing.JPanel implements Screen {

    private final Navigator nav;
    private final DataTable.ListModel model =
            new DataTable.ListModel("SKU", "Part", "Category", "Stock", "Price", "Status", "");
    private List<Product> shown = new ArrayList<>();

    /** Creates new form InventoryDashboardPanel */
    public InventoryDashboardPanel(Navigator nav) {
        this.nav = nav;
        initComponents();
        Styler.apply(this);
        roleBadge.set(motorcyclepartsstore.service.Session.current().getRole().getLabel(),
                motorcyclepartsstore.service.Session.current().getRole() == motorcyclepartsstore.model.Role.ADMIN
                        ? motorcyclepartsstore.ui.Chip.Kind.GREEN : motorcyclepartsstore.ui.Chip.Kind.BLUE);
        products.setIconKind(Icons.Kind.BOX);
        low.setIconKind(Icons.Kind.ALERT);
        out.setIconKind(Icons.Kind.ALERT);
        value.setIconKind(Icons.Kind.CART);
        lowTable.bind(model, lowHost).weights(13, 28, 16, 10, 14, 13, 8).chipColumn(5)
                .actionColumn(6, new String[]{"Edit"}, (row, link) -> {
                    if (new ProductDialog(nav.window(), shown.get(row)).showDialog()) {
                        onShow();
                    }
                });
    }

    private void addProduct() {
        if (new ProductDialog(nav.window(), null).showDialog()) {
            onShow();
        }
    }

    @Override
    public void onShow() {
        InventoryService inv = InventoryService.get();
        products.setValue(String.valueOf(inv.all().size()));
        low.setValue(String.valueOf(inv.lowStockCount()));
        out.setValue(String.valueOf(inv.outOfStockCount()));
        value.setValue(Money.peso(inv.stockValue()));
        shown = inv.lowStock();
        List<Object[]> rows = new ArrayList<>();
        for (Product p : shown) {
            rows.add(new Object[]{p.getSku(), p.getName(), p.getCategory(), p.getStock(), Money.peso(p.getPrice()), p.status(), ""});
        }
        model.setRows(rows);
        Map<String, Integer> byCat = inv.stockByCategory();
        double[] v = new double[byCat.size()];
        int i = 0;
        for (int n : byCat.values()) {
            v[i++] = n;
        }
        donut.setData(byCat.keySet().toArray(new String[0]), v);
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
        addBtn = new motorcyclepartsstore.ui.RoundButton();
        body = new javax.swing.JPanel();
        cardsRow = new javax.swing.JPanel();
        products = new motorcyclepartsstore.ui.SummaryCard();
        low = new motorcyclepartsstore.ui.SummaryCard();
        out = new motorcyclepartsstore.ui.SummaryCard();
        value = new motorcyclepartsstore.ui.SummaryCard();
        midRow = new javax.swing.JPanel();
        lowCard = new motorcyclepartsstore.ui.RoundedPanel();
        lowTitle = new javax.swing.JLabel();
        lowHost = new javax.swing.JPanel();
        lowTable = new motorcyclepartsstore.ui.DataTable();
        categoryCard = new motorcyclepartsstore.ui.RoundedPanel();
        categoryTitle = new javax.swing.JLabel();
        donut = new motorcyclepartsstore.ui.DonutChart();

        pageTitle.setName("pageTitle");
        pageTitle.setText("Dashboard");
        pageSubtitle.setName("pageSubtitle");
        pageSubtitle.setText("Stock overview");
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
        addBtn.setText("Add product");
        addBtn.addActionListener(this::addBtnActionPerformed);
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
        actionBox.add(addBtn, gridBagConstraints);
        headerPanel.setOpaque(false);
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 14, 0));
        headerPanel.setLayout(new java.awt.BorderLayout());
        headerPanel.add(titleBox, java.awt.BorderLayout.WEST);
        headerPanel.add(actionBox, java.awt.BorderLayout.EAST);
        products.setTitle("Products");
        low.setTitle("Low stock");
        out.setTitle("Out of stock");
        value.setTitle("Stock value");
        cardsRow.setOpaque(false);
        cardsRow.setLayout(new java.awt.GridLayout(1, 4, 16, 0));
        cardsRow.add(products);
        cardsRow.add(low);
        cardsRow.add(out);
        cardsRow.add(value);
        lowTitle.setName("cardTitle");
        lowTitle.setText("Low stock alerts");
        lowHost.setOpaque(false);
        lowHost.setLayout(new java.awt.BorderLayout());
        lowHost.add(lowTable, java.awt.BorderLayout.CENTER);
        lowCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        lowCard.setLayout(new java.awt.BorderLayout());
        lowCard.add(lowTitle, java.awt.BorderLayout.NORTH);
        lowCard.add(lowHost, java.awt.BorderLayout.CENTER);
        categoryTitle.setName("cardTitle");
        categoryTitle.setText("Stock by category");
        categoryCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        categoryCard.setLayout(new java.awt.BorderLayout());
        categoryCard.add(categoryTitle, java.awt.BorderLayout.NORTH);
        categoryCard.add(donut, java.awt.BorderLayout.CENTER);
        midRow.setOpaque(false);
        midRow.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.68;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        midRow.add(lowCard, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.31999999999999995;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        midRow.add(categoryCard, gridBagConstraints);
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

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        addProduct();
    }//GEN-LAST:event_addBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionBox;
    private motorcyclepartsstore.ui.RoundButton addBtn;
    private javax.swing.JPanel body;
    private javax.swing.JPanel cardsRow;
    private motorcyclepartsstore.ui.RoundedPanel categoryCard;
    private javax.swing.JLabel categoryTitle;
    private motorcyclepartsstore.ui.DonutChart donut;
    private javax.swing.JPanel headerPanel;
    private motorcyclepartsstore.ui.SummaryCard low;
    private motorcyclepartsstore.ui.RoundedPanel lowCard;
    private javax.swing.JPanel lowHost;
    private motorcyclepartsstore.ui.DataTable lowTable;
    private javax.swing.JLabel lowTitle;
    private javax.swing.JPanel midRow;
    private motorcyclepartsstore.ui.SummaryCard out;
    private javax.swing.JLabel pageSubtitle;
    private javax.swing.JLabel pageTitle;
    private motorcyclepartsstore.ui.SummaryCard products;
    private motorcyclepartsstore.ui.Chip roleBadge;
    private javax.swing.JPanel titleBox;
    private motorcyclepartsstore.ui.SummaryCard value;
    // End of variables declaration//GEN-END:variables
}
