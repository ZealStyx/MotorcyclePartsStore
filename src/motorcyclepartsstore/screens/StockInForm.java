/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import java.util.List;
import java.util.function.Consumer;
import javax.swing.DefaultComboBoxModel;
import motorcyclepartsstore.model.Product;
import motorcyclepartsstore.service.InventoryService;
import motorcyclepartsstore.ui.Forms;
import motorcyclepartsstore.ui.RoundButton;
import motorcyclepartsstore.ui.Styler;

/**
 * Stock in form. Layout lives in StockInForm.form; StockInDialog hosts it.
 */
public class StockInForm extends javax.swing.JPanel {

    private final Consumer<Boolean> onClose;
    private final List<Product> products = InventoryService.get().all();

    /** Creates new form StockInForm */
    public StockInForm(Consumer<Boolean> onClose) {
        this.onClose = onClose;
        initComponents();
        Styler.apply(this);
        Styler.placeholder(qty, "units received");
        String[] items = new String[products.size()];
        for (int i = 0; i < items.length; i++) {
            Product p = products.get(i);
            items[i] = p.getSku() + "  " + p.getName() + "  (stock " + p.getStock() + ")";
        }
        productBox.setModel(new DefaultComboBoxModel<>(items));
    }

    public RoundButton getSaveButton() {
        return saveBtn;
    }

    private void close(boolean saved) {
        onClose.accept(saved);
    }

    private void save() {
        int i = productBox.getSelectedIndex();
        String err = InventoryService.get().stockIn(i < 0 ? null : products.get(i), qty.getText());
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

        productCaption = new javax.swing.JLabel();
        productBox = new javax.swing.JComboBox<>();
        qtyCaption = new javax.swing.JLabel();
        qty = new javax.swing.JTextField();
        message = new javax.swing.JLabel();
        buttonRow = new javax.swing.JPanel();
        cancelBtn = new motorcyclepartsstore.ui.SecondaryButton();
        saveBtn = new motorcyclepartsstore.ui.RoundButton();

        productCaption.setName("caption");
        productCaption.setText("Product");
        productBox.setName("field");
        productBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {  }));
        qtyCaption.setName("caption");
        qtyCaption.setText("Quantity received");
        qty.setName("field");
        message.setName("message");
        message.setText(" ");
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(this::cancelBtnActionPerformed);
        saveBtn.setText("Add stock");
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
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        add(productCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        add(productBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        add(qtyCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        add(qty, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        add(message, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
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
    private javax.swing.JLabel message;
    private javax.swing.JComboBox<String> productBox;
    private javax.swing.JLabel productCaption;
    private javax.swing.JTextField qty;
    private javax.swing.JLabel qtyCaption;
    private motorcyclepartsstore.ui.RoundButton saveBtn;
    // End of variables declaration//GEN-END:variables
}
