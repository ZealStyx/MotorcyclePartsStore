package motorcyclepartsstore.screens;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import motorcyclepartsstore.model.Role;
import motorcyclepartsstore.model.User;
import motorcyclepartsstore.service.UserService;
import motorcyclepartsstore.ui.Forms;
import motorcyclepartsstore.ui.ModalDialog;
import motorcyclepartsstore.ui.RoundButton;

/** Add / edit user dialog (reference page 6). */
public class UserDialog extends ModalDialog {

    private final User existing;
    private final JTextField fullName = Forms.text("full name");
    private final JTextField username = Forms.text("username");
    private final JTextField email = Forms.text("email");
    private final JTextField phone = Forms.text("phone");
    private final JComboBox<Role> level = Forms.combo(new Role[]{null, Role.ADMIN, Role.INVENTORY_CLERK, Role.SALES, Role.INSTRUCTOR});
    private final JComboBox<String> status = Forms.combo(new String[]{User.ACTIVE, User.INACTIVE, User.PENDING});
    private final JPasswordField password = Forms.password();
    private final JPasswordField confirm = Forms.password();
    private final JLabel message = Forms.message();
    private boolean saved;

    public UserDialog(Window owner, User existing) {
        super(owner, existing == null ? "Add user" : "Edit user", 620, true);
        this.existing = existing;
        level.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean sel, boolean focus) {
                return super.getListCellRendererComponent(list, value == null ? "Select user level" : value, index, sel, focus);
            }
        });
        if (existing != null) {
            fullName.setText(existing.getFullName());
            username.setText(existing.getUsername());
            email.setText(existing.getEmail());
            phone.setText(existing.getPhone());
            level.setSelectedItem(existing.getRole());
            // Approving a pending sign-up: assigning a level activates it by default.
            status.setSelectedItem(User.PENDING.equals(existing.getStatus()) ? User.ACTIVE : existing.getStatus());
        }
        if (existing != null) {
            password.putClientProperty("JTextField.placeholderText", "leave empty to keep");
        }

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 12, 8);
        place(grid, c, 0, 0, "Full name", fullName);
        place(grid, c, 1, 0, "Username", username);
        place(grid, c, 0, 1, "Email", email);
        place(grid, c, 1, 1, "Phone", phone);
        place(grid, c, 0, 2, "User level", level);
        place(grid, c, 1, 2, "Status", status);
        place(grid, c, 0, 3, "New password", password);
        place(grid, c, 1, 3, "Confirm password", confirm);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 0, 0);
        grid.add(message, c);
        setBody(grid);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        foot.setOpaque(false);
        RoundButton cancel = RoundButton.secondary("Cancel");
        cancel.addActionListener(e -> dispose());
        RoundButton save = RoundButton.primary("Save user");
        save.addActionListener(e -> save());
        foot.add(cancel);
        foot.add(save);
        setFooter(foot);
        getRootPane().setDefaultButton(save);
    }

    private void place(JPanel grid, GridBagConstraints c, int x, int y, String caption, javax.swing.JComponent field) {
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = 1;
        c.insets = new Insets(0, x == 0 ? 0 : 8, 12, x == 0 ? 8 : 0);
        grid.add(Forms.labeled(caption, field), c);
    }

    private void save() {
        String err = UserService.get().save(existing, fullName.getText(), username.getText(), email.getText(),
                phone.getText(), (Role) level.getSelectedItem(), (String) status.getSelectedItem(),
                new String(password.getPassword()), new String(confirm.getPassword()));
        if (err != null) {
            Forms.error(message, err);
            return;
        }
        saved = true;
        dispose();
    }

    /** Shows the dialog; returns true when a user was saved. */
    public boolean showDialog() {
        open();
        return saved;
    }
}
