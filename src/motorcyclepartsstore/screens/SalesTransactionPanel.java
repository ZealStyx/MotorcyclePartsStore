/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import motorcyclepartsstore.model.Product;
import motorcyclepartsstore.model.Sale;
import motorcyclepartsstore.model.SaleLine;
import motorcyclepartsstore.service.InventoryService;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.service.SalesService;
import motorcyclepartsstore.ui.Dialogs;
import motorcyclepartsstore.ui.Forms;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.Screen;
import motorcyclepartsstore.ui.Styler;

/**
 * Sales transaction: cart, order summary, complete sale and receipt (reference page 14).
 * Layout lives in SalesTransactionPanel.form.
 */
public class SalesTransactionPanel extends javax.swing.JPanel implements Screen {

    private final Navigator nav;
    private final List<SaleLine> cart = new ArrayList<>();
    private final CartModel model = new CartModel();
    private final JPopupMenu popup = new JPopupMenu();

    /** Creates new form SalesTransactionPanel */
    public SalesTransactionPanel(Navigator nav) {
        this.nav = nav;
        initComponents();
        Styler.apply(this);
        Styler.placeholder(search, "Search part or scan SKU");
        Styler.placeholder(customer, "customer name");
        popup.setFocusable(false);
        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                suggest();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                suggest();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        search.addActionListener(e -> addFromSearch());
        clearLink.setOnClick(() -> {
            cart.clear();
            customer.setText("");
            refresh();
        });
        cartTable.setModel(model);
        cartHost.add(cartTable.getTableHeader(), java.awt.BorderLayout.NORTH);
        cartTable.weights(38, 12, 18, 18, 14);
        cartTable.actionColumn(4, new String[]{"\u2212", "+", "Remove"}, (row, link) -> {
            SaleLine l = cart.get(row);
            if (link == 0) {
                setQty(l, l.getQty() - 1);
            } else if (link == 1) {
                setQty(l, l.getQty() + 1);
            } else {
                cart.remove(row);
            }
            refresh();
        });
        model.addTableModelListener(e -> updateTotal());
    }

    @Override
    public void onShow() {
        // Stock may have changed elsewhere: drop lines that can no longer be fulfilled.
        InventoryService inv = InventoryService.get();
        cart.removeIf(l -> inv.findBySku(l.getSku()) == null || inv.findBySku(l.getSku()).getStock() <= 0);
        for (SaleLine l : cart) {
            Product p = inv.findBySku(l.getSku());
            if (l.getQty() > p.getStock()) {
                l.setQty(p.getStock());
            }
        }
        refresh();
        info.setText(" ");
    }

    private void refresh() {
        model.fireTableDataChanged();
        updateTotal();
    }

    private double cartTotal() {
        double t = 0;
        for (SaleLine l : cart) {
            t += l.subtotal();
        }
        return Math.round(t * 100.0) / 100.0;
    }

    private void updateTotal() {
        totalLabel.setText(Money.peso(cartTotal()));
    }

    private void setQty(SaleLine l, int qty) {
        Product p = InventoryService.get().findBySku(l.getSku());
        int max = p == null ? 0 : p.getStock();
        if (qty > max) {
            Forms.error(info, "Only " + max + " unit(s) of " + l.getName() + " are available.");
            qty = max;
        } else {
            info.setText(" ");
        }
        l.setQty(Math.max(1, qty));
    }

    private void suggest() {
        popup.setVisible(false);
        popup.removeAll();
        String q = search.getText().trim();
        if (q.isEmpty()) {
            return;
        }
        List<Product> found = InventoryService.get().search(q, null);
        for (int i = 0; i < found.size() && i < 8; i++) {
            Product p = found.get(i);
            JMenuItem mi = new JMenuItem(p.getSku() + "   " + p.getName() + "   " + Money.peso(p.getPrice())
                    + (p.getStock() <= 0 ? "   (out of stock)" : "   stock " + p.getStock()));
            mi.setEnabled(p.getStock() > 0);
            mi.addActionListener(e -> add(p));
            popup.add(mi);
        }
        if (popup.getComponentCount() > 0 && search.isShowing()) {
            popup.show(search, 0, search.getHeight());
        }
    }

    private void addFromSearch() {
        String q = search.getText().trim();
        if (q.isEmpty()) {
            return;
        }
        Product p = InventoryService.get().findBySku(q);
        if (p == null) {
            List<Product> found = InventoryService.get().search(q, null);
            p = found.isEmpty() ? null : found.get(0);
        }
        if (p == null) {
            Forms.error(info, "No product matches \"" + q + "\".");
            return;
        }
        add(p);
    }

    private void add(Product p) {
        popup.setVisible(false);
        if (p.getStock() <= 0) {
            Forms.error(info, p.getName() + " is out of stock.");
            return;
        }
        SaleLine existing = null;
        for (SaleLine l : cart) {
            if (l.getSku().equals(p.getSku())) {
                existing = l;
            }
        }
        if (existing == null) {
            cart.add(new SaleLine(p.getSku(), p.getName(), 1, p.getPrice()));
            info.setText(" ");
        } else {
            setQty(existing, existing.getQty() + 1);
        }
        search.setText("");
        popup.setVisible(false);
        refresh();
    }

    private String customerName() {
        String c = customer.getText().trim();
        return c.isEmpty() ? "Walk-in" : c;
    }

    private void previewReceipt() {
        if (cart.isEmpty()) {
            Dialogs.message(nav.window(), "Receipt preview", "Add at least one product first.");
            return;
        }
        String text = SalesService.receiptText("PREVIEW (not recorded)", customerName(),
                (String) payment.getSelectedItem(), LocalDateTime.now(), cart);
        new ReceiptDialog(nav.window(), "Receipt preview", text, "receipt-preview").open();
    }

    private void completeSale() {
        try {
            Sale s = SalesService.get().complete(customer.getText(), (String) payment.getSelectedItem(), cart);
            cart.clear();
            customer.setText("");
            info.setText(" ");
            refresh();
            new ReceiptDialog(nav.window(), "Sale " + s.getNumber() + " completed",
                    SalesService.receiptText(s), "receipt-" + s.getNumber()).open();
        } catch (IllegalArgumentException ex) {
            Dialogs.message(nav.window(), "Cannot complete sale", ex.getMessage());
        }
    }

    /** Table model over the cart; quantity cells are editable. */
    private final class CartModel extends AbstractTableModel {

        private final String[] cols = {"Part", "Qty", "Unit price", "Subtotal", ""};

        @Override
        public int getRowCount() {
            return cart.size();
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public String getColumnName(int c) {
            return cols[c];
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return c == 1 ? Integer.class : String.class;
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return c == 1;
        }

        @Override
        public Object getValueAt(int r, int c) {
            SaleLine l = cart.get(r);
            switch (c) {
                case 0: return l.getName();
                case 1: return l.getQty();
                case 2: return Money.peso(l.getUnitPrice());
                case 3: return Money.peso(l.subtotal());
                default: return "";
            }
        }

        @Override
        public void setValueAt(Object v, int r, int c) {
            if (c != 1) {
                return;
            }
            try {
                setQty(cart.get(r), Integer.parseInt(String.valueOf(v).trim()));
            } catch (NumberFormatException ex) {
                Forms.error(info, "Quantity must be a whole number.");
            }
            fireTableDataChanged();
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
        previewBtn = new motorcyclepartsstore.ui.SecondaryButton();
        completeBtn = new motorcyclepartsstore.ui.RoundButton();
        body = new javax.swing.JPanel();
        leftCard = new motorcyclepartsstore.ui.RoundedPanel();
        searchBox = new javax.swing.JPanel();
        search = new javax.swing.JTextField();
        info = new javax.swing.JLabel();
        cartHost = new javax.swing.JPanel();
        cartTable = new motorcyclepartsstore.ui.DataTable();
        summaryCard = new motorcyclepartsstore.ui.RoundedPanel();
        summaryTitle = new javax.swing.JLabel();
        customerCaption = new javax.swing.JLabel();
        customer = new javax.swing.JTextField();
        paymentCaption = new javax.swing.JLabel();
        payment = new javax.swing.JComboBox<>();
        totalCaption = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        clearLink = new motorcyclepartsstore.ui.LinkLabel();

        pageTitle.setName("pageTitle");
        pageTitle.setText("Sales transaction");
        pageSubtitle.setName("pageSubtitle");
        pageSubtitle.setText("Create a sale and print the receipt");
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
        previewBtn.setText("Receipt preview");
        previewBtn.addActionListener(this::previewBtnActionPerformed);
        completeBtn.setText("Complete sale");
        completeBtn.addActionListener(this::completeBtnActionPerformed);
        actionBox.setOpaque(false);
        actionBox.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        actionBox.add(previewBtn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        actionBox.add(completeBtn, gridBagConstraints);
        headerPanel.setOpaque(false);
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 14, 0));
        headerPanel.setLayout(new java.awt.BorderLayout());
        headerPanel.add(titleBox, java.awt.BorderLayout.WEST);
        headerPanel.add(actionBox, java.awt.BorderLayout.EAST);
        search.setName("field");
        info.setName("message");
        info.setText(" ");
        searchBox.setOpaque(false);
        searchBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 12, 0));
        searchBox.setLayout(new java.awt.BorderLayout());
        searchBox.add(search, java.awt.BorderLayout.NORTH);
        searchBox.add(info, java.awt.BorderLayout.SOUTH);
        cartHost.setOpaque(false);
        cartHost.setLayout(new java.awt.BorderLayout());
        cartHost.add(cartTable, java.awt.BorderLayout.CENTER);
        leftCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        leftCard.setLayout(new java.awt.BorderLayout());
        leftCard.add(searchBox, java.awt.BorderLayout.NORTH);
        leftCard.add(cartHost, java.awt.BorderLayout.CENTER);
        summaryTitle.setName("cardTitle");
        summaryTitle.setText("Order summary");
        customerCaption.setName("caption");
        customerCaption.setText("Customer");
        customer.setName("field");
        paymentCaption.setName("caption");
        paymentCaption.setText("Payment");
        payment.setName("field");
        payment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "GCash", "Credit / debit card" }));
        totalCaption.setName("muted");
        totalCaption.setText("Total");
        totalLabel.setName("bigValue");
        totalLabel.setText("₱0.00");
        clearLink.setText("Clear sale");
        summaryCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        summaryCard.setPreferredSize(new java.awt.Dimension(340, 100));
        summaryCard.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        summaryCard.add(summaryTitle, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        summaryCard.add(customerCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        summaryCard.add(customer, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        summaryCard.add(paymentCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        summaryCard.add(payment, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        summaryCard.add(totalCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 16, 0);
        summaryCard.add(totalLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        summaryCard.add(clearLink, gridBagConstraints);
        body.setOpaque(false);
        body.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 16);
        body.add(leftCard, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        body.add(summaryCard, gridBagConstraints);
        setOpaque(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(28, 34, 28, 34));
        setLayout(new java.awt.BorderLayout());
        add(headerPanel, java.awt.BorderLayout.NORTH);
        add(body, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void previewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewBtnActionPerformed
        previewReceipt();
    }//GEN-LAST:event_previewBtnActionPerformed
    private void completeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completeBtnActionPerformed
        completeSale();
    }//GEN-LAST:event_completeBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionBox;
    private javax.swing.JPanel body;
    private javax.swing.JPanel cartHost;
    private motorcyclepartsstore.ui.DataTable cartTable;
    private motorcyclepartsstore.ui.LinkLabel clearLink;
    private motorcyclepartsstore.ui.RoundButton completeBtn;
    private javax.swing.JTextField customer;
    private javax.swing.JLabel customerCaption;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel info;
    private motorcyclepartsstore.ui.RoundedPanel leftCard;
    private javax.swing.JLabel pageSubtitle;
    private javax.swing.JLabel pageTitle;
    private javax.swing.JComboBox<String> payment;
    private javax.swing.JLabel paymentCaption;
    private motorcyclepartsstore.ui.SecondaryButton previewBtn;
    private javax.swing.JTextField search;
    private javax.swing.JPanel searchBox;
    private motorcyclepartsstore.ui.RoundedPanel summaryCard;
    private javax.swing.JLabel summaryTitle;
    private javax.swing.JPanel titleBox;
    private javax.swing.JLabel totalCaption;
    private javax.swing.JLabel totalLabel;
    // End of variables declaration//GEN-END:variables
}
