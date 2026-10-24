package motorcyclepartsstore.screens;

import java.awt.Window;
import motorcyclepartsstore.ui.ModalDialog;

/** Modal window around {@link StockInForm}. */
public class StockInDialog extends ModalDialog {

    private boolean saved;

    public StockInDialog(Window owner) {
        super(owner, "Stock in", 460, true);
        StockInForm form = new StockInForm(ok -> {
            saved = ok;
            dispose();
        });
        setBody(form);
        getRootPane().setDefaultButton(form.getSaveButton());
    }

    public boolean showDialog() {
        open();
        return saved;
    }
}
