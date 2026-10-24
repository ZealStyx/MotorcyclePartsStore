/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import java.util.function.Consumer;
import motorcyclepartsstore.model.Role;
import motorcyclepartsstore.model.User;
import motorcyclepartsstore.service.UserService;
import motorcyclepartsstore.ui.Forms;
import motorcyclepartsstore.ui.RoundButton;
import motorcyclepartsstore.ui.Styler;

/**
 * Add / edit user form (reference page 6). Layout lives in UserForm.form; UserDialog hosts it.
 */
public class UserForm extends javax.swing.JPanel {

    private final User existing;
    private final Consumer<Boolean> onClose;

    /** Creates new form UserForm */
    public UserForm(User existing, Consumer<Boolean> onClose) {
        this.existing = existing;
        this.onClose = onClose;
        initComponents();
        Styler.apply(this);
        Styler.placeholder(fullName, "full name");
        Styler.placeholder(username, "username");
        Styler.placeholder(email, "email");
        Styler.placeholder(phone, "phone");
        if (existing != null) {
            fullName.setText(existing.getFullName());
            username.setText(existing.getUsername());
            email.setText(existing.getEmail());
            phone.setText(existing.getPhone());
            level.setSelectedIndex(existing.getRole() == null ? 0 : existing.getRole().ordinal() + 1);
            // Approving a pending sign-up: assigning a level activates it by default.
            status.setSelectedItem(User.PENDING.equals(existing.getStatus()) ? User.ACTIVE : existing.getStatus());
            Styler.placeholder(password, "leave empty to keep");
        }
    }

    public RoundButton getSaveButton() {
        return saveBtn;
    }

    private void close(boolean saved) {
        onClose.accept(saved);
    }

    private void save() {
        Role role = level.getSelectedIndex() == 0 ? null : Role.values()[level.getSelectedIndex() - 1];
        String err = UserService.get().save(existing, fullName.getText(), username.getText(), email.getText(),
                phone.getText(), role, (String) status.getSelectedItem(),
                new String(password.getPassword()), new String(confirm.getPassword()));
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

        fullNameCaption = new javax.swing.JLabel();
        fullName = new javax.swing.JTextField();
        usernameCaption = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        emailCaption = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        phoneCaption = new javax.swing.JLabel();
        phone = new javax.swing.JTextField();
        levelCaption = new javax.swing.JLabel();
        level = new javax.swing.JComboBox<>();
        statusCaption = new javax.swing.JLabel();
        status = new javax.swing.JComboBox<>();
        passwordCaption = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        confirmCaption = new javax.swing.JLabel();
        confirm = new javax.swing.JPasswordField();
        message = new javax.swing.JLabel();
        buttonRow = new javax.swing.JPanel();
        cancelBtn = new motorcyclepartsstore.ui.SecondaryButton();
        saveBtn = new motorcyclepartsstore.ui.RoundButton();

        fullNameCaption.setName("caption");
        fullNameCaption.setText("Full name");
        fullName.setName("field");
        usernameCaption.setName("caption");
        usernameCaption.setText("Username");
        username.setName("field");
        emailCaption.setName("caption");
        emailCaption.setText("Email");
        email.setName("field");
        phoneCaption.setName("caption");
        phoneCaption.setText("Phone");
        phone.setName("field");
        levelCaption.setName("caption");
        levelCaption.setText("User level");
        level.setName("field");
        level.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select user level", "Admin", "Inventory clerk", "Sales personnel", "Instructor" }));
        statusCaption.setName("caption");
        statusCaption.setText("Status");
        status.setName("field");
        status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Inactive", "Pending" }));
        passwordCaption.setName("caption");
        passwordCaption.setText("New password");
        password.setName("field");
        confirmCaption.setName("caption");
        confirmCaption.setText("Confirm password");
        confirm.setName("field");
        message.setName("message");
        message.setText(" ");
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(this::cancelBtnActionPerformed);
        saveBtn.setText("Save user");
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
        add(fullNameCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 8);
        add(fullName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 6, 0);
        add(usernameCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 12, 0);
        add(username, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 8);
        add(emailCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 8);
        add(email, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 6, 0);
        add(phoneCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 12, 0);
        add(phone, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 8);
        add(levelCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 8);
        add(level, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 6, 0);
        add(statusCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 12, 0);
        add(status, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 8);
        add(passwordCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 8);
        add(password, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 6, 0);
        add(confirmCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 12, 0);
        add(confirm, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        add(message, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
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
    private javax.swing.JPasswordField confirm;
    private javax.swing.JLabel confirmCaption;
    private javax.swing.JTextField email;
    private javax.swing.JLabel emailCaption;
    private javax.swing.JTextField fullName;
    private javax.swing.JLabel fullNameCaption;
    private javax.swing.JComboBox<String> level;
    private javax.swing.JLabel levelCaption;
    private javax.swing.JLabel message;
    private javax.swing.JPasswordField password;
    private javax.swing.JLabel passwordCaption;
    private javax.swing.JTextField phone;
    private javax.swing.JLabel phoneCaption;
    private motorcyclepartsstore.ui.RoundButton saveBtn;
    private javax.swing.JComboBox<String> status;
    private javax.swing.JLabel statusCaption;
    private javax.swing.JTextField username;
    private javax.swing.JLabel usernameCaption;
    // End of variables declaration//GEN-END:variables
}
