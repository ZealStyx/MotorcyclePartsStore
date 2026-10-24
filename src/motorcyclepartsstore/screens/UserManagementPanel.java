/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.screens;

import java.util.ArrayList;
import java.util.List;
import motorcyclepartsstore.model.User;
import motorcyclepartsstore.service.UserService;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Dialogs;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.Screen;
import motorcyclepartsstore.ui.Styler;

/**
 * Admin: accounts and user levels (reference pages 4, 6, 7). Layout lives in UserManagementPanel.form.
 */
public class UserManagementPanel extends javax.swing.JPanel implements Screen {

    private final Navigator nav;
    private final DataTable.ListModel model = new DataTable.ListModel("Name", "Username", "User level", "Status", "");
    private List<User> shown = new ArrayList<>();

    /** Creates new form UserManagementPanel */
    public UserManagementPanel(Navigator nav) {
        this.nav = nav;
        initComponents();
        Styler.apply(this);
        usersTable.bind(model, usersHost).weights(24, 20, 20, 14, 18).chipColumn(3)
                .actionColumn(4, new String[]{"Edit", "Delete"}, (row, link) -> {
                    User u = shown.get(row);
                    if (link == 0) {
                        edit(u);
                    } else {
                        delete(u);
                    }
                });
    }

    @Override
    public void onShow() {
        shown = UserService.get().all();
        List<Object[]> rows = new ArrayList<>();
        for (User u : shown) {
            rows.add(new Object[]{u.getFullName(), u.getUsername(), u.roleLabel(), u.getStatus(), ""});
        }
        model.setRows(rows);
    }

    private void addUser() {
        if (new UserDialog(nav.window(), null).showDialog()) {
            onShow();
        }
    }

    private void edit(User u) {
        if (new UserDialog(nav.window(), u).showDialog()) {
            onShow();
        }
    }

    private void delete(User u) {
        boolean yes = Dialogs.confirm(nav.window(), "Delete this user?",
                u.getFullName() + " will lose access to the system. This cannot be undone.",
                "Keep user", "Yes, delete", true);
        if (!yes) {
            return;
        }
        String err = UserService.get().delete(u);
        if (err != null) {
            Dialogs.message(nav.window(), "Cannot delete user", err);
        }
        onShow();
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
        addUserBtn = new motorcyclepartsstore.ui.RoundButton();
        body = new javax.swing.JPanel();
        usersCard = new motorcyclepartsstore.ui.RoundedPanel();
        usersHost = new javax.swing.JPanel();
        usersTable = new motorcyclepartsstore.ui.DataTable();

        pageTitle.setName("pageTitle");
        pageTitle.setText("User management");
        pageSubtitle.setName("pageSubtitle");
        pageSubtitle.setText("Accounts and user levels");
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
        addUserBtn.setText("Add user");
        addUserBtn.addActionListener(this::addUserBtnActionPerformed);
        actionBox.setOpaque(false);
        actionBox.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        actionBox.add(addUserBtn, gridBagConstraints);
        headerPanel.setOpaque(false);
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 14, 0));
        headerPanel.setLayout(new java.awt.BorderLayout());
        headerPanel.add(titleBox, java.awt.BorderLayout.WEST);
        headerPanel.add(actionBox, java.awt.BorderLayout.EAST);
        usersHost.setOpaque(false);
        usersHost.setLayout(new java.awt.BorderLayout());
        usersHost.add(usersTable, java.awt.BorderLayout.CENTER);
        usersCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 20, 18, 20));
        usersCard.setLayout(new java.awt.BorderLayout());
        usersCard.add(usersHost, java.awt.BorderLayout.CENTER);
        body.setOpaque(false);
        body.setLayout(new java.awt.BorderLayout());
        body.add(usersCard, java.awt.BorderLayout.CENTER);
        setOpaque(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(28, 34, 28, 34));
        setLayout(new java.awt.BorderLayout());
        add(headerPanel, java.awt.BorderLayout.NORTH);
        add(body, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void addUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserBtnActionPerformed
        addUser();
    }//GEN-LAST:event_addUserBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionBox;
    private motorcyclepartsstore.ui.RoundButton addUserBtn;
    private javax.swing.JPanel body;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel pageSubtitle;
    private javax.swing.JLabel pageTitle;
    private javax.swing.JPanel titleBox;
    private motorcyclepartsstore.ui.RoundedPanel usersCard;
    private javax.swing.JPanel usersHost;
    private motorcyclepartsstore.ui.DataTable usersTable;
    // End of variables declaration//GEN-END:variables
}
