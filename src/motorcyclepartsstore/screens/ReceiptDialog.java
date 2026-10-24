package motorcyclepartsstore.screens;

import java.awt.Window;
import motorcyclepartsstore.ui.ModalDialog;

/** Modal window around {@link ReceiptForm}. */
public class ReceiptDialog extends ModalDialog {

    public ReceiptDialog(Window owner, String title, String receiptText, String suggestedName) {
        super(owner, title, 440, true);
        setBody(new ReceiptForm(owner, receiptText, suggestedName, this::dispose));
    }
}
