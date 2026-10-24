/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.ui;

import java.util.function.Consumer;
import javax.swing.JRootPane;
import motorcyclepartsstore.model.Role;
import motorcyclepartsstore.model.User;
import motorcyclepartsstore.service.UserService;

/**
 * Sign in / Create account (reference pages 1-2). Layout lives in AuthPanel.form.
 */
public class AuthPanel extends javax.swing.JPanel {

    private Consumer<User> onLogin = u -> { };
    private boolean signInMode = true;

    /** Creates new form AuthPanel */
    public AuthPanel() {
        initComponents();
        setupLook();
        showMode(true);
    }

    public AuthPanel(Consumer<User> onLogin) {
        this();
        this.onLogin = onLogin;
    }

    /** Theme, placeholders and link actions that are not form properties. */
    private void setupLook() {
        Styler.apply(this);
        formsHost.setOpaque(true);
        formsHost.setBackground(Theme.WORKSPACE_BG);
        brandChip.set("One Prompt Away", Chip.Kind.ACCENT);
        Styler.placeholder(inUser, "username");
        Styler.placeholder(upName, "full name");
        Styler.placeholder(upUser, "username");
        Styler.placeholder(upEmail, "email");
        toSignUp.setOnClick(() -> showMode(false));
        toSignIn.setOnClick(() -> showMode(true));
    }

    private void showMode(boolean signIn) {
        signInMode = signIn;
        signInPanel.setVisible(signIn);
        signUpPanel.setVisible(!signIn);
        inMessage.setText(" ");
        upMessage.setText(" ");
        updateDefaultButton();
        revalidate();
        repaint();
    }

    private void updateDefaultButton() {
        JRootPane rp = getRootPane();
        if (rp != null) {
            rp.setDefaultButton(signInMode ? signInBtn : signUpBtn);
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        updateDefaultButton();
    }

    /** Clears sensitive fields; called after logout. */
    public void reset() {
        inPass.setText("");
        upPass.setText("");
        upConfirm.setText("");
        showMode(true);
        inUser.requestFocusInWindow();
    }

    private void doSignIn() {
        Role level = Role.values()[inLevel.getSelectedIndex()];
        UserService.AuthResult r = UserService.get().authenticate(inUser.getText(),
                new String(inPass.getPassword()), level);
        if (r.user == null) {
            Forms.error(inMessage, r.error);
            return;
        }
        inMessage.setText(" ");
        inPass.setText("");
        onLogin.accept(r.user);
    }

    private void doSignUp() {
        String err = UserService.get().register(upName.getText(), upUser.getText(), upEmail.getText(),
                new String(upPass.getPassword()), new String(upConfirm.getPassword()));
        if (err != null) {
            Forms.error(upMessage, err);
            return;
        }
        String user = upUser.getText().trim();
        upName.setText("");
        upUser.setText("");
        upEmail.setText("");
        upPass.setText("");
        upConfirm.setText("");
        showMode(true);
        inUser.setText(user);
        Forms.success(inMessage, "Request sent. An admin must assign your user level before you can sign in.");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        java.awt.GridBagConstraints gridBagConstraints;

        brandPanel = new javax.swing.JPanel();
        brandTitle1 = new javax.swing.JLabel();
        brandTitle2 = new javax.swing.JLabel();
        brandSub = new javax.swing.JLabel();
        brandChip = new motorcyclepartsstore.ui.Chip();
        formsHost = new javax.swing.JPanel();
        signInPanel = new javax.swing.JPanel();
        signInTitle = new javax.swing.JLabel();
        signInSub = new javax.swing.JLabel();
        userCaption = new javax.swing.JLabel();
        inUser = new javax.swing.JTextField();
        passCaption = new javax.swing.JLabel();
        inPass = new javax.swing.JPasswordField();
        levelCaption = new javax.swing.JLabel();
        inLevel = new javax.swing.JComboBox<>();
        signInBtn = new motorcyclepartsstore.ui.RoundButton();
        inMessage = new javax.swing.JLabel();
        toSignUp = new motorcyclepartsstore.ui.LinkLabel();
        signUpPanel = new javax.swing.JPanel();
        signUpTitle = new javax.swing.JLabel();
        signUpSub = new javax.swing.JLabel();
        nameCaption = new javax.swing.JLabel();
        upName = new javax.swing.JTextField();
        upUserCaption = new javax.swing.JLabel();
        upUser = new javax.swing.JTextField();
        emailCaption = new javax.swing.JLabel();
        upEmail = new javax.swing.JTextField();
        upPassCaption = new javax.swing.JLabel();
        upPass = new javax.swing.JPasswordField();
        confirmCaption = new javax.swing.JLabel();
        upConfirm = new javax.swing.JPasswordField();
        signUpBtn = new motorcyclepartsstore.ui.RoundButton();
        upMessage = new javax.swing.JLabel();
        toSignIn = new motorcyclepartsstore.ui.LinkLabel();

        brandTitle1.setName("brandTitle");
        brandTitle1.setText("MOTORCYCLE PARTS");
        brandTitle2.setName("brandTitle");
        brandTitle2.setText("STORE SYSTEM");
        brandSub.setName("brandSub");
        brandSub.setText("<html><body style='width:430px'>Inventory, sales transactions and demand prediction, in one place.</body></html>");
        brandPanel.setName("dark");
        brandPanel.setPreferredSize(new java.awt.Dimension(10, 10));
        brandPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 77, 0, 40);
        brandPanel.add(brandTitle1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 77, 18, 40);
        brandPanel.add(brandTitle2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 77, 16, 40);
        brandPanel.add(brandSub, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 77, 0, 40);
        brandPanel.add(brandChip, gridBagConstraints);
        signInTitle.setName("formTitle");
        signInTitle.setText("Welcome back");
        signInSub.setName("pageSubtitle");
        signInSub.setText("Sign in with your account.");
        userCaption.setName("caption");
        userCaption.setText("Username");
        inUser.setName("field");
        passCaption.setName("caption");
        passCaption.setText("Password");
        inPass.setName("field");
        levelCaption.setName("caption");
        levelCaption.setText("User level");
        inLevel.setName("field");
        inLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Inventory clerk", "Sales personnel", "Instructor" }));
        signInBtn.setText("Sign in");
        signInBtn.setPreferredSize(new java.awt.Dimension(100, 44));
        signInBtn.addActionListener(this::signInBtnActionPerformed);
        inMessage.setName("message");
        inMessage.setText(" ");
        toSignUp.setText("No account yet? Sign up");
        signInPanel.setOpaque(false);
        signInPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        signInPanel.add(signInTitle, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        signInPanel.add(signInSub, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        signInPanel.add(userCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        signInPanel.add(inUser, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        signInPanel.add(passCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        signInPanel.add(inPass, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        signInPanel.add(levelCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        signInPanel.add(inLevel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 8, 0);
        signInPanel.add(signInBtn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        signInPanel.add(inMessage, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        signInPanel.add(toSignUp, gridBagConstraints);
        signUpTitle.setName("formTitle");
        signUpTitle.setText("Create account");
        signUpSub.setName("pageSubtitle");
        signUpSub.setText("Register to request access. An admin assigns your user level.");
        nameCaption.setName("caption");
        nameCaption.setText("Full name");
        upName.setName("field");
        upUserCaption.setName("caption");
        upUserCaption.setText("Username");
        upUser.setName("field");
        emailCaption.setName("caption");
        emailCaption.setText("Email");
        upEmail.setName("field");
        upPassCaption.setName("caption");
        upPassCaption.setText("Password");
        upPass.setName("field");
        confirmCaption.setName("caption");
        confirmCaption.setText("Confirm password");
        upConfirm.setName("field");
        signUpBtn.setText("Sign up");
        signUpBtn.setPreferredSize(new java.awt.Dimension(100, 44));
        signUpBtn.addActionListener(this::signUpBtnActionPerformed);
        upMessage.setName("message");
        upMessage.setText(" ");
        toSignIn.setText("Already registered? Sign in");
        signUpPanel.setOpaque(false);
        signUpPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        signUpPanel.add(signUpTitle, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        signUpPanel.add(signUpSub, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        signUpPanel.add(nameCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        signUpPanel.add(upName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        signUpPanel.add(upUserCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        signUpPanel.add(upUser, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        signUpPanel.add(emailCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        signUpPanel.add(upEmail, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        signUpPanel.add(upPassCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        signUpPanel.add(upPass, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        signUpPanel.add(confirmCaption, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 14, 0);
        signUpPanel.add(upConfirm, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 8, 0);
        signUpPanel.add(signUpBtn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        signUpPanel.add(upMessage, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        signUpPanel.add(toSignIn, gridBagConstraints);
        formsHost.setName("workspace");
        formsHost.setPreferredSize(new java.awt.Dimension(10, 10));
        formsHost.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 60, 20, 60);
        formsHost.add(signInPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 60, 20, 60);
        formsHost.add(signUpPanel, gridBagConstraints);
        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.52;
        gridBagConstraints.weighty = 1.0;
        add(brandPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.48;
        gridBagConstraints.weighty = 1.0;
        add(formsHost, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void signInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signInBtnActionPerformed
        doSignIn();
    }//GEN-LAST:event_signInBtnActionPerformed
    private void signUpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signUpBtnActionPerformed
        doSignUp();
    }//GEN-LAST:event_signUpBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private motorcyclepartsstore.ui.Chip brandChip;
    private javax.swing.JPanel brandPanel;
    private javax.swing.JLabel brandSub;
    private javax.swing.JLabel brandTitle1;
    private javax.swing.JLabel brandTitle2;
    private javax.swing.JLabel confirmCaption;
    private javax.swing.JLabel emailCaption;
    private javax.swing.JPanel formsHost;
    private javax.swing.JComboBox<String> inLevel;
    private javax.swing.JLabel inMessage;
    private javax.swing.JPasswordField inPass;
    private javax.swing.JTextField inUser;
    private javax.swing.JLabel levelCaption;
    private javax.swing.JLabel nameCaption;
    private javax.swing.JLabel passCaption;
    private motorcyclepartsstore.ui.RoundButton signInBtn;
    private javax.swing.JPanel signInPanel;
    private javax.swing.JLabel signInSub;
    private javax.swing.JLabel signInTitle;
    private motorcyclepartsstore.ui.RoundButton signUpBtn;
    private javax.swing.JPanel signUpPanel;
    private javax.swing.JLabel signUpSub;
    private javax.swing.JLabel signUpTitle;
    private motorcyclepartsstore.ui.LinkLabel toSignIn;
    private motorcyclepartsstore.ui.LinkLabel toSignUp;
    private javax.swing.JPasswordField upConfirm;
    private javax.swing.JTextField upEmail;
    private javax.swing.JLabel upMessage;
    private javax.swing.JTextField upName;
    private javax.swing.JPasswordField upPass;
    private javax.swing.JLabel upPassCaption;
    private javax.swing.JTextField upUser;
    private javax.swing.JLabel upUserCaption;
    private javax.swing.JLabel userCaption;
    // End of variables declaration//GEN-END:variables
}
