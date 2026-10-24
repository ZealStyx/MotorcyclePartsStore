/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import java.awt.Font;
import java.awt.Window;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import motorcyclepartsstore.ui.Dialogs;
import motorcyclepartsstore.ui.Styler;
import motorcyclepartsstore.ui.Theme;

/**
 * Receipt preview with text export and system print (printing itself is never reported as successful
 * unless the print job completed). Layout lives in ReceiptForm.form; ReceiptDialog hosts it.
 */
public class ReceiptForm extends javax.swing.JPanel {

    private final Window owner;
    private final String suggestedName;
    private final Runnable onClose;
    private final JTextArea area = new JTextArea();

    /** Creates new form ReceiptForm */
    public ReceiptForm(Window owner, String receiptText, String suggestedName, Runnable onClose) {
        this.owner = owner;
        this.suggestedName = suggestedName;
        this.onClose = onClose;
        initComponents();
        Styler.apply(this);
        area.setText(receiptText);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setBackground(Theme.WORKSPACE_BG);
        area.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 14, 12, 14));
        area.setCaretPosition(0);
        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(javax.swing.BorderFactory.createLineBorder(Theme.BORDER));
        receiptHolder.add(sp, java.awt.BorderLayout.CENTER);
    }

    private void saveText() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(suggestedName + ".txt"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            Files.write(fc.getSelectedFile().toPath(), area.getText().getBytes(StandardCharsets.UTF_8));
            Dialogs.message(owner, "Receipt saved", "Saved to " + fc.getSelectedFile().getAbsolutePath());
        } catch (IOException ex) {
            Dialogs.message(owner, "Save failed", ex.getMessage());
        }
    }

    private void printReceipt() {
        try {
            if (area.print()) {
                Dialogs.message(owner, "Print", "The print job was sent to the system printer.");
            }
        } catch (PrinterException | java.awt.HeadlessException ex) {
            Dialogs.message(owner, "Print failed", String.valueOf(ex.getMessage()));
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        java.awt.GridBagConstraints gridBagConstraints;

        receiptHolder = new javax.swing.JPanel();
        buttonRow = new javax.swing.JPanel();
        saveTextBtn = new motorcyclepartsstore.ui.SecondaryButton();
        printBtn = new motorcyclepartsstore.ui.SecondaryButton();
        closeBtn = new motorcyclepartsstore.ui.RoundButton();

        receiptHolder.setOpaque(false);
        receiptHolder.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 14, 0));
        receiptHolder.setLayout(new java.awt.BorderLayout());
        saveTextBtn.setText("Save as text");
        saveTextBtn.addActionListener(this::saveTextBtnActionPerformed);
        printBtn.setText("Print");
        printBtn.addActionListener(this::printBtnActionPerformed);
        closeBtn.setText("Close");
        closeBtn.addActionListener(this::closeBtnActionPerformed);
        buttonRow.setOpaque(false);
        buttonRow.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        buttonRow.add(saveTextBtn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        buttonRow.add(printBtn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        buttonRow.add(closeBtn, gridBagConstraints);
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(400, 380));
        setLayout(new java.awt.BorderLayout());
        add(receiptHolder, java.awt.BorderLayout.CENTER);
        add(buttonRow, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void saveTextBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveTextBtnActionPerformed
        saveText();
    }//GEN-LAST:event_saveTextBtnActionPerformed
    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed
        printReceipt();
    }//GEN-LAST:event_printBtnActionPerformed
    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        onClose.run();
    }//GEN-LAST:event_closeBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonRow;
    private motorcyclepartsstore.ui.RoundButton closeBtn;
    private motorcyclepartsstore.ui.SecondaryButton printBtn;
    private javax.swing.JPanel receiptHolder;
    private motorcyclepartsstore.ui.SecondaryButton saveTextBtn;
    // End of variables declaration//GEN-END:variables
}
