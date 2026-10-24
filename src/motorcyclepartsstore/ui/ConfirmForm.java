/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.ui;

import java.util.function.Consumer;

/**
 * Message / confirmation body used by {@link Dialogs}. Layout lives in ConfirmForm.form.
 */
public class ConfirmForm extends javax.swing.JPanel {

    private final Consumer<Boolean> onResult;

    /** Creates new form ConfirmForm */
    public ConfirmForm(String text, String cancelLabel, String confirmLabel, boolean danger, Consumer<Boolean> onResult) {
        this.onResult = onResult;
        initComponents();
        Styler.apply(this);
        String esc = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>");
        messageText.setText("<html><body style='width:360px'>" + esc + "</body></html>");
        messageText.setFont(Theme.body(14));
        cancelBtn.setVisible(cancelLabel != null);
        if (cancelLabel != null) {
            cancelBtn.setText(cancelLabel);
        }
        okBtn.setText(confirmLabel);
        dangerBtn.setText(confirmLabel);
        okBtn.setVisible(!danger);
        dangerBtn.setVisible(danger);
    }

    /** The button that should react to the Enter key. */
    public RoundButton defaultButton() {
        return dangerBtn.isVisible() ? dangerBtn : okBtn;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        java.awt.GridBagConstraints gridBagConstraints;

        messageText = new javax.swing.JLabel();
        cancelBtn = new motorcyclepartsstore.ui.SecondaryButton();
        okBtn = new motorcyclepartsstore.ui.RoundButton();
        dangerBtn = new motorcyclepartsstore.ui.DangerButton();

        messageText.setName("muted");
        messageText.setText(" ");
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(this::cancelBtnActionPerformed);
        okBtn.setText("OK");
        okBtn.addActionListener(this::okBtnActionPerformed);
        dangerBtn.setText("Delete");
        dangerBtn.addActionListener(this::dangerBtnActionPerformed);
        setOpaque(false);
        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 18, 0);
        add(messageText, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        add(cancelBtn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(okBtn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(dangerBtn, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        onResult.accept(false);
    }//GEN-LAST:event_cancelBtnActionPerformed
    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
        onResult.accept(true);
    }//GEN-LAST:event_okBtnActionPerformed
    private void dangerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dangerBtnActionPerformed
        onResult.accept(true);
    }//GEN-LAST:event_dangerBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private motorcyclepartsstore.ui.SecondaryButton cancelBtn;
    private motorcyclepartsstore.ui.DangerButton dangerBtn;
    private javax.swing.JLabel messageText;
    private motorcyclepartsstore.ui.RoundButton okBtn;
    // End of variables declaration//GEN-END:variables
}
