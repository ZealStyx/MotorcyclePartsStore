package motorcyclepartsstore.ui;

import java.awt.Window;

/** Small message and confirmation dialogs built on {@link ModalDialog} and {@link ConfirmForm}. */
public final class Dialogs {

    private Dialogs() {
    }

    public static void message(Window owner, String title, String message) {
        ModalDialog d = new ModalDialog(owner, title, 420, false);
        ConfirmForm form = new ConfirmForm(message, null, "OK", false, ok -> d.dispose());
        d.setBody(form);
        d.getRootPane().setDefaultButton(form.defaultButton());
        d.open();
    }

    /** Returns true when the confirm button was chosen. */
    public static boolean confirm(Window owner, String title, String message, String cancelLabel,
            String confirmLabel, boolean danger) {
        boolean[] result = {false};
        ModalDialog d = new ModalDialog(owner, title, 440, false);
        ConfirmForm form = new ConfirmForm(message, cancelLabel, confirmLabel, danger, ok -> {
            result[0] = ok;
            d.dispose();
        });
        d.setBody(form);
        d.open();
        return result[0];
    }
}
