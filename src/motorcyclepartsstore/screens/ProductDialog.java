package motorcyclepartsstore.screens;

import java.awt.Window;
import motorcyclepartsstore.model.Product;
import motorcyclepartsstore.ui.ModalDialog;

/** Modal window around {@link ProductForm}. */
public class ProductDialog extends ModalDialog {

    private boolean saved;

    public ProductDialog(Window owner, Product existing) {
        super(owner, existing == null ? "Add product" : "Edit product", 560, true);
        ProductForm form = new ProductForm(existing, ok -> {
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
