package motorcyclepartsstore.screens;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import motorcyclepartsstore.model.Product;
import motorcyclepartsstore.service.InventoryService;
import motorcyclepartsstore.ui.Forms;
import motorcyclepartsstore.ui.ModalDialog;
import motorcyclepartsstore.ui.RoundButton;

/** Add / edit product dialog for the inventory clerk. */
public class ProductDialog extends ModalDialog {

    private final Product existing;
    private final JTextField sku = Forms.text("e.g. BRK-004");
    private final JTextField name = Forms.text("part name");
    private final JComboBox<String> category;
    private final JTextField stock = Forms.text("0");
    private final JTextField price = Forms.text("0.00");
    private final JLabel message = Forms.message();
    private boolean saved;

    public ProductDialog(Window owner, Product existing) {
        super(owner, existing == null ? "Add product" : "Edit product", 560, true);
        this.existing = existing;
        category = Forms.combo(InventoryService.get().categories().toArray(new String[0]));
        category.setEditable(true);
        if (existing != null) {
            sku.setText(existing.getSku());
            sku.setEditable(false); // SKU stays fixed so past sales keep pointing at the product
            name.setText(existing.getName());
            category.setSelectedItem(existing.getCategory());
            stock.setText(String.valueOf(existing.getStock()));
            price.setText(String.format("%.2f", existing.getPrice()));
        } else {
            category.setSelectedItem("");
        }
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        place(grid, c, 0, 0, "SKU", sku);
        place(grid, c, 1, 0, "Part name", name);
        place(grid, c, 0, 1, "Category", category);
        place(grid, c, 1, 1, "Stock (units)", stock);
        place(grid, c, 0, 2, "Price (\u20B1)", price);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 0, 0);
        grid.add(message, c);
        setBody(grid);
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        foot.setOpaque(false);
        RoundButton cancel = RoundButton.secondary("Cancel");
        cancel.addActionListener(e -> dispose());
        RoundButton save = RoundButton.primary("Save product");
        save.addActionListener(e -> save());
        foot.add(cancel);
        foot.add(save);
        setFooter(foot);
        getRootPane().setDefaultButton(save);
    }

    private void place(JPanel grid, GridBagConstraints c, int x, int y, String caption, javax.swing.JComponent f) {
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = 1;
        c.insets = new Insets(0, x == 0 ? 0 : 8, 12, x == 0 ? 8 : 0);
        grid.add(Forms.labeled(caption, f), c);
    }

    private void save() {
        Object cat = category.getEditor().getItem();
        String err = InventoryService.get().save(existing, sku.getText(), name.getText(),
                cat == null ? "" : cat.toString(), stock.getText(), price.getText());
        if (err != null) {
            Forms.error(message, err);
            return;
        }
        saved = true;
        dispose();
    }

    public boolean showDialog() {
        open();
        return saved;
    }
}
