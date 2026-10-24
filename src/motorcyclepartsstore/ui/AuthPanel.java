package motorcyclepartsstore.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import motorcyclepartsstore.model.Role;
import motorcyclepartsstore.model.User;
import motorcyclepartsstore.service.UserService;

/** Sign in / Create account: dark brand panel on the left, light form on the right. */
public class AuthPanel extends JPanel {

    private static final String SIGN_IN = "signin";
    private static final String SIGN_UP = "signup";

    private final Consumer<User> onLogin;
    private final CardLayout cards = new CardLayout();
    private final JPanel forms = new JPanel(cards);

    private final JTextField inUser = Forms.text("username");
    private final JPasswordField inPass = Forms.password();
    private final JComboBox<Role> inLevel = Forms.combo(Role.values());
    private final JLabel inMessage = Forms.message();
    private final RoundButton signInBtn = RoundButton.primary("Sign in");

    private final JTextField upName = Forms.text("full name");
    private final JTextField upUser = Forms.text("username");
    private final JTextField upEmail = Forms.text("email");
    private final JPasswordField upPass = Forms.password();
    private final JPasswordField upConfirm = Forms.password();
    private final JLabel upMessage = Forms.message();
    private final RoundButton signUpBtn = RoundButton.primary("Sign up");

    private String current = SIGN_IN;

    public AuthPanel(Consumer<User> onLogin) {
        super(new GridBagLayout());
        this.onLogin = onLogin;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 0.52;
        add(brandPanel(), c);
        c.gridx = 1;
        c.weightx = 0.48;
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Theme.WORKSPACE_BG);
        forms.setOpaque(false);
        forms.add(signInForm(), SIGN_IN);
        forms.add(signUpForm(), SIGN_UP);
        GridBagConstraints rc = new GridBagConstraints();
        rc.fill = GridBagConstraints.HORIZONTAL;
        rc.weightx = 1;
        rc.insets = new Insets(20, 60, 20, 60);
        right.add(forms, rc);
        add(right, c);

        signInBtn.addActionListener(e -> doSignIn());
        signUpBtn.addActionListener(e -> doSignUp());
    }

    private JComponent brandPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Theme.SIDEBAR_BG);
        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        float size = 62;
        JLabel l1 = new JLabel("MOTORCYCLE PARTS");
        JLabel l2 = new JLabel("STORE SYSTEM");
        for (JLabel l : new JLabel[]{l1, l2}) {
            l.setFont(Theme.heading(size));
            l.setForeground(Color.WHITE);
            l.setAlignmentX(0f);
            box.add(l);
        }
        box.add(Box.createVerticalStrut(18));
        JLabel sub = new JLabel("<html><body style='width:430px'>Inventory, sales transactions and demand prediction, in one place.</body></html>");
        sub.setFont(Theme.body(18));
        sub.setForeground(new Color(0xCF, 0xCF, 0xD6));
        sub.setAlignmentX(0f);
        box.add(sub);
        box.add(Box.createVerticalStrut(16));
        Chip chip = new Chip("One Prompt Away", Chip.Kind.ACCENT);
        chip.setAlignmentX(0f);
        box.add(chip);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(0, 77, 0, 40);
        gc.anchor = GridBagConstraints.WEST;
        gc.weightx = 1;
        p.add(box, gc);
        return p;
    }

    private JPanel formShell() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    private void add(JPanel p, JComponent c, int gapAfter) {
        c.setAlignmentX(0f);
        p.add(c);
        p.add(Box.createVerticalStrut(gapAfter));
    }

    private JPanel signInForm() {
        JPanel p = formShell();
        add(p, Forms.title("Welcome back", 40), 14);
        add(p, Forms.muted("Sign in with your account.", 14), 14);
        add(p, Forms.labeled("Username", inUser), 14);
        add(p, Forms.labeled("Password", inPass), 14);
        add(p, Forms.labeled("User level", inLevel), 18);
        signInBtn.setPreferredSize(new java.awt.Dimension(100, 44));
        signInBtn.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 44));
        add(p, signInBtn, 8);
        add(p, inMessage, 6);
        add(p, Forms.link("No account yet? Sign up", Theme.MUTED, () -> show(SIGN_UP)), 0);
        return p;
    }

    private JPanel signUpForm() {
        JPanel p = formShell();
        add(p, Forms.title("Create account", 40), 10);
        add(p, Forms.muted("Register to request access. An admin assigns your user level.", 14), 12);
        add(p, Forms.labeled("Full name", upName), 12);
        add(p, Forms.labeled("Username", upUser), 12);
        add(p, Forms.labeled("Email", upEmail), 12);
        add(p, Forms.labeled("Password", upPass), 12);
        add(p, Forms.labeled("Confirm password", upConfirm), 16);
        signUpBtn.setPreferredSize(new java.awt.Dimension(100, 44));
        signUpBtn.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 44));
        add(p, signUpBtn, 8);
        add(p, upMessage, 6);
        add(p, Forms.link("Already registered? Sign in", Theme.MUTED, () -> show(SIGN_IN)), 0);
        return p;
    }

    private void show(String name) {
        current = name;
        cards.show(forms, name);
        inMessage.setText(" ");
        upMessage.setText(" ");
        updateDefaultButton();
    }

    private void updateDefaultButton() {
        JRootPane rp = getRootPane();
        if (rp != null) {
            rp.setDefaultButton(current.equals(SIGN_IN) ? signInBtn : signUpBtn);
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
        show(SIGN_IN);
        inUser.requestFocusInWindow();
    }

    private void doSignIn() {
        UserService.AuthResult r = UserService.get().authenticate(inUser.getText(),
                new String(inPass.getPassword()), (Role) inLevel.getSelectedItem());
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
        show(SIGN_IN);
        inUser.setText(user);
        Forms.success(inMessage, "Request sent. An admin must assign your user level before you can sign in.");
    }
}
