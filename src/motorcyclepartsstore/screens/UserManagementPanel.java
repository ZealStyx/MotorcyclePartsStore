package motorcyclepartsstore.screens;

import java.util.ArrayList;
import java.util.List;
import motorcyclepartsstore.model.User;
import motorcyclepartsstore.service.UserService;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Dialogs;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.RoundButton;
import motorcyclepartsstore.ui.RoundedPanel;
import motorcyclepartsstore.ui.ScreenPanel;

/** Admin: accounts and user levels (reference pages 4, 6, 7). */
public class UserManagementPanel extends ScreenPanel {

    private final DataTable.ListModel model = new DataTable.ListModel("Name", "Username", "User level", "Status", "");
    private List<User> shown = new ArrayList<>();

    public UserManagementPanel(Navigator nav) {
        super(nav, "User management", "Accounts and user levels", false, addButton(nav));
        DataTable table = new DataTable(model).weights(24, 20, 20, 14, 18).chipColumn(3)
                .actionColumn(4, new String[]{"Edit", "Delete"}, (row, link) -> {
                    User u = shown.get(row);
                    if (link == 0) {
                        edit(u);
                    } else {
                        delete(u);
                    }
                });
        RoundedPanel card = RoundedPanel.card(new java.awt.BorderLayout());
        card.setBorder(motorcyclepartsstore.ui.Forms.pad(18, 20, 18, 20));
        card.add(top(table.wrapper()), java.awt.BorderLayout.CENTER);
        setBody(card);
    }

    private static RoundButton addButton(Navigator nav) {
        RoundButton b = RoundButton.primary("Add user");
        b.addActionListener(e -> {
            if (new UserDialog(nav.window(), null).showDialog()) {
                // the screen refreshes itself when it regains focus through onShow of the shell
                nav.navigate(motorcyclepartsstore.ui.Page.ADMIN_USERS);
            }
        });
        return b;
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
}
