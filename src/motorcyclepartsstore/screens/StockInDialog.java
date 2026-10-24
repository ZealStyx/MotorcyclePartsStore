package motorcyclepartsstore.screens;

import java.awt.FlowLayout;
import java.awt.Window;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import motorcyclepartsstore.model.Product;
import motorcyclepartsstore.service.InventoryService;
import motorcyclepartsstore.ui.Forms;
import motorcyclepartsstore.ui.ModalDialog;
import motorcyclepartsstore.ui.RoundButton;

/** Receive stock for an existing product. */
public class StockInDialog extends ModalDialog {

    private final JComboBox<Product> products;
    private final JTextField qty = Forms.text("units received");
    private final JLabel message = Forms.message();
    private boolean saved;

    public StockInDialog(Window owner) {
        super(owner, "Stock in", 460, true);
        products = Forms.combo(InventoryService.get().all().toArray(new Product[0]));
        products.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> l, Object v, int i, boolean s, boolean f) {
                Product p = (Product) v;
                return super.getListCellRendererComponent(l, p == null ? "" : p.getSku() + "  " + p.getName()
                        + "  (stock " + p.getStock() + ")", i, s, f);
            }
        });
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        JPanel a = Forms.labeled("Product", products);
        JPanel b = Forms.labeled("Quantity received", qty);
        a.setAlignmentX(0f);
        b.setAlignmentX(0f);
        message.setAlignmentX(0f);
        body.add(a);
        body.add(javax.swing.Box.createVerticalStrut(12));
        body.add(b);
        body.add(javax.swing.Box.createVerticalStrut(8));
        body.add(message);
        setBody(body);
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        foot.setOpaque(false);
        RoundButton cancel = RoundButton.secondary("Cancel");
        cancel.addActionListener(e -> dispose());
        RoundButton ok = RoundButton.primary("Add stock");
        ok.addActionListener(e -> {
            String err = InventoryService.get().stockIn((Product) products.getSelectedItem(), qty.getText());
            if (err != null) {
                Forms.error(message, err);
                return;
            }
            saved = true;
            dispose();
        });
        foot.add(cancel);
        foot.add(ok);
        setFooter(foot);
        getRootPane().setDefaultButton(ok);
    }

    public boolean showDialog() {
        open();
        return saved;
    }
}
