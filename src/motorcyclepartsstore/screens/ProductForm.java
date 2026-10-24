/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import java.util.function.Consumer;
import javax.swing.DefaultComboBoxModel;
import motorcyclepartsstore.model.Product;
import motorcyclepartsstore.service.InventoryService;
import motorcyclepartsstore.ui.Forms;
import motorcyclepartsstore.ui.RoundButton;
import motorcyclepartsstore.ui.Styler;

/**
 * Add / edit product form. Layout lives in ProductForm.form; ProductDialog hosts it.
 */
public class ProductForm extends javax.swing.JPanel {

    private final Product existing;
    private final Consumer<Boolean> onClose;

    /** Creates new form ProductForm */
    public ProductForm(Product existing, Consumer<Boolean> onClose) {
        this.existing = existing;
        this.onClose = onClose;
        initComponents();
        Styler.apply(this);
        Styler.placeholder(sku, "e.g. BRK-004");
        Styler.placeholder(partName, "part name");
        Styler.placeholder(stock, "0");
        Styler.placeholder(price, "0.00");
        category.setModel(new DefaultComboBoxModel<>(InventoryService.get().categories().toArray(new String[0])));
        category.setEditable(true);
        if (existing != null) {
            sku.setText(existing.getSku());
            sku.setEditable(false); // SKU stays fixed so past sales keep pointing at the product
            partName.setText(existing.getName());
            category.setSelectedItem(existing.getCategory());
            stock.setText(String.valueOf(existing.getStock()));
            price.setText(String.format("%.2f", existing.getPrice()));
        } else {
            category.setSelectedItem("");
        }
    }

    public RoundButton getSaveButton() {
        return saveBtn;
    }

    private void close(boolean saved) {
        onClose.accept(saved);
    }

    private void save() {
        Object cat = category.getEditor().getItem();
        String err = InventoryService.get().save(existing, sku.getText(), partName.getText(),
                cat == null ? "" : cat.toString(), stock.getText(), price.getText());
        if (err != null) {
            Forms.error(message, err);
            return;
        }
        close(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        java.awt.GridBagConstraints gridBagConstraints;

        skuCaption = new javax.swing.JLabel();
        sku = new javax.swing.JTextField();
        nameCaption = new javax.swing.JLabel();
        partName = new javax.swing.JTextField();
        categoryCaption = new javax.swing.JLabel();
        category = new javax.swing.JComboBox<>();
        stockCaption = new javax.swing.JLabel();
        stock = new javax.swing.JTextField();
        priceCaption = new javax.swing.JLabel();
        price = new javax.swing.JTextField();
        message = new javax.swing.JLabel();
        buttonRow = new javax.swing.JPanel();
        cancelBtn = new motorcyclepartsstore.ui.SecondaryButton();
        saveBtn = new motorcyclepartsstore.ui.RoundButton();

        skuCaption.setName("caption");
        skuCaption.setText("SKU");
        sku.setName("field");
        nameCaption.setName("caption");
        nameCaption.setText("Part name");
        partName.setName("field");
        categoryCaption.setName("caption");
        categoryCaption.setText("Category");
        category.setName("field");
        category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {  }));
        stockCaption.setName("caption");
        stockCaption.setText("Stock (units)");
        stock.setName("field");
        priceCaption.setName("caption");
        priceCaption.setText("Price (₱)");
        price.setName("field");
        message.setName("message");
        message.setText(" ");
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(this::cancelBtnActionPerformed);
        saveBtn.setText("Save product");
        saveBtn.addActionListener(this::saveBtnActionPerformed);
        buttonRow.setOpaque(false);
        buttonRow.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        buttonRow.add(cancelBtn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        buttonRow.add(saveBtn, gridBagConstraints);
        setOpaque(false);
        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 8);
        add(skuCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 8);
        add(sku, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 6, 0);
        add(nameCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 12, 0);
        add(partName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 8);
        add(categoryCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 8);
        add(category, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 6, 0);
        add(stockCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 12, 0);
        add(stock, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        add(priceCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        add(price, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        add(message, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        add(buttonRow, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        close(false);
    }//GEN-LAST:event_cancelBtnActionPerformed
    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        save();
    }//GEN-LAST:event_saveBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonRow;
    private motorcyclepartsstore.ui.SecondaryButton cancelBtn;
    private javax.swing.JComboBox<String> category;
    private javax.swing.JLabel categoryCaption;
    private javax.swing.JLabel message;
    private javax.swing.JLabel nameCaption;
    private javax.swing.JTextField partName;
    private javax.swing.JTextField price;
    private javax.swing.JLabel priceCaption;
    private motorcyclepartsstore.ui.RoundButton saveBtn;
    private javax.swing.JTextField sku;
    private javax.swing.JLabel skuCaption;
    private javax.swing.JTextField stock;
    private javax.swing.JLabel stockCaption;
    // End of variables declaration//GEN-END:variables
}
