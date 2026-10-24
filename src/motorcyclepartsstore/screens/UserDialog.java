package motorcyclepartsstore.screens;

import java.awt.Window;
import motorcyclepartsstore.model.User;
import motorcyclepartsstore.ui.ModalDialog;

/** Modal window around {@link UserForm} (reference page 6). */
public class UserDialog extends ModalDialog {

    private boolean saved;

    public UserDialog(Window owner, User existing) {
        super(owner, existing == null ? "Add user" : "Edit user", 620, true);
        UserForm form = new UserForm(existing, ok -> {
            saved = ok;
            dispose();
        });
        setBody(form);
        getRootPane().setDefaultButton(form.getSaveButton());
    }

    /** Shows the dialog; returns true when a user was saved. */
    public boolean showDialog() {
        open();
        return saved;
    }
}
