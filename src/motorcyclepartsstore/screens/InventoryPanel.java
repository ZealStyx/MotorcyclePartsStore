/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import motorcyclepartsstore.model.Product;
import motorcyclepartsstore.service.InventoryService;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.Screen;
import motorcyclepartsstore.ui.Styler;

/**
 * Inventory list with search, category filter, stock in and add/edit (reference page 12).
 * Layout lives in InventoryPanel.form.
 */
public class InventoryPanel extends javax.swing.JPanel implements Screen {

    private final Navigator nav;
    private final DataTable.ListModel model =
            new DataTable.ListModel("SKU", "Part", "Category", "Stock", "Price", "Status", "");
    private List<Product> shown = new ArrayList<>();
    private boolean updating;

    /** Creates new form InventoryPanel */
    public InventoryPanel(Navigator nav) {
        this.nav = nav;
        initComponents();
        Styler.apply(this);
        Styler.placeholder(search, "Search parts");
        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                reload();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                reload();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                reload();
            }
        });
        category.addActionListener(e -> reload());
        listTable.bind(model, listHost).weights(13, 28, 16, 10, 14, 13, 8).chipColumn(5)
                .actionColumn(6, new String[]{"Edit"}, (row, link) -> {
                    if (new ProductDialog(nav.window(), shown.get(row)).showDialog()) {
                        onShow();
                    }
                });
    }

    @Override
    public void onShow() {
        updating = true;
        Object sel = category.getSelectedItem();
        category.removeAllItems();
        category.addItem("All categories");
        for (String c : InventoryService.get().categories()) {
            category.addItem(c);
        }
        category.setSelectedItem(sel == null ? "All categories" : sel);
        if (category.getSelectedItem() == null) {
            category.setSelectedIndex(0);
        }
        updating = false;
        reload();
    }

    private void reload() {
        if (updating) {
            return;
        }
        shown = InventoryService.get().search(search.getText(), (String) category.getSelectedItem());
        List<Object[]> rows = new ArrayList<>();
        for (Product p : shown) {
            rows.add(new Object[]{p.getSku(), p.getName(), p.getCategory(), p.getStock(), Money.peso(p.getPrice()), p.status(), ""});
        }
        model.setRows(rows);
    }

    private void stockIn() {
        if (new StockInDialog(nav.window()).showDialog()) {
            onShow();
        }
    }

    private void addProduct() {
        if (new ProductDialog(nav.window(), null).showDialog()) {
            onShow();
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
        stockInBtn = new motorcyclepartsstore.ui.SecondaryButton();
        addBtn = new motorcyclepartsstore.ui.RoundButton();
        body = new javax.swing.JPanel();
        listCard = new motorcyclepartsstore.ui.RoundedPanel();
        filters = new javax.swing.JPanel();
        search = new javax.swing.JTextField();
        category = new javax.swing.JComboBox<>();
        filterFiller = new javax.swing.JPanel();
        listHost = new javax.swing.JPanel();
        listTable = new motorcyclepartsstore.ui.DataTable();

        pageTitle.setName("pageTitle");
        pageTitle.setText("Inventory");
        pageSubtitle.setName("pageSubtitle");
        pageSubtitle.setText("Add, edit and track stock");
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
        stockInBtn.setText("Stock in");
        stockInBtn.addActionListener(this::stockInBtnActionPerformed);
        addBtn.setText("Add product");
        addBtn.addActionListener(this::addBtnActionPerformed);
        actionBox.setOpaque(false);
        actionBox.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        actionBox.add(stockInBtn, gridBagConstraints);
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
        search.setName("field");
        search.setPreferredSize(new java.awt.Dimension(340, 40));
        category.setName("field");
        category.setPreferredSize(new java.awt.Dimension(170, 40));
        category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All categories" }));
        filterFiller.setOpaque(false);
        filters.setOpaque(false);
        filters.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 14, 0));
        filters.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
        filters.add(search, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        filters.add(category, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        filters.add(filterFiller, gridBagConstraints);
        listHost.setOpaque(false);
        listHost.setLayout(new java.awt.BorderLayout());
        listHost.add(listTable, java.awt.BorderLayout.CENTER);
        listCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        listCard.setLayout(new java.awt.BorderLayout());
        listCard.add(filters, java.awt.BorderLayout.NORTH);
        listCard.add(listHost, java.awt.BorderLayout.CENTER);
        body.setOpaque(false);
        body.setLayout(new java.awt.BorderLayout());
        body.add(listCard, java.awt.BorderLayout.CENTER);
        setOpaque(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(28, 34, 28, 34));
        setLayout(new java.awt.BorderLayout());
        add(headerPanel, java.awt.BorderLayout.NORTH);
        add(body, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void stockInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockInBtnActionPerformed
        stockIn();
    }//GEN-LAST:event_stockInBtnActionPerformed
    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        addProduct();
    }//GEN-LAST:event_addBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionBox;
    private motorcyclepartsstore.ui.RoundButton addBtn;
    private javax.swing.JPanel body;
    private javax.swing.JComboBox<String> category;
    private javax.swing.JPanel filterFiller;
    private javax.swing.JPanel filters;
    private javax.swing.JPanel headerPanel;
    private motorcyclepartsstore.ui.RoundedPanel listCard;
    private javax.swing.JPanel listHost;
    private motorcyclepartsstore.ui.DataTable listTable;
    private javax.swing.JLabel pageSubtitle;
    private javax.swing.JLabel pageTitle;
    private javax.swing.JTextField search;
    private motorcyclepartsstore.ui.SecondaryButton stockInBtn;
    private javax.swing.JPanel titleBox;
    // End of variables declaration//GEN-END:variables
}
