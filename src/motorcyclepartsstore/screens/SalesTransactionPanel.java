package motorcyclepartsstore.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import motorcyclepartsstore.model.Product;
import motorcyclepartsstore.model.Sale;
import motorcyclepartsstore.model.SaleLine;
import motorcyclepartsstore.service.InventoryService;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.service.SalesService;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Dialogs;
import motorcyclepartsstore.ui.Forms;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.RoundButton;
import motorcyclepartsstore.ui.RoundedPanel;
import motorcyclepartsstore.ui.ScreenPanel;
import motorcyclepartsstore.ui.Theme;

/** Sales transaction: cart, order summary, complete sale and receipt (reference page 14). */
public class SalesTransactionPanel extends ScreenPanel {

    private final List<SaleLine> cart = new ArrayList<>();
    private final CartModel model = new CartModel();
    private final JTextField search = Forms.text("Search part or scan SKU");
    private final JPopupMenu popup = new JPopupMenu();
    private final JTextField customer = Forms.text("customer name");
    private final JComboBox<String> payment = Forms.combo(SalesService.PAYMENT_METHODS);
    private final JLabel total = Forms.title(Money.peso(0), 34);
    private final JLabel info = Forms.message();

    public SalesTransactionPanel(Navigator nav) {
        this(nav, RoundButton.secondary("Receipt preview"), RoundButton.primary("Complete sale"));
    }

    private SalesTransactionPanel(Navigator nav, RoundButton preview, RoundButton complete) {
        super(nav, "Sales transaction", "Create a sale and print the receipt", false, preview, complete);
        preview.addActionListener(e -> previewReceipt());
        complete.addActionListener(e -> completeSale());

        popup.setFocusable(false);
        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                suggest();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                suggest();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        search.addActionListener(e -> addFromSearch());

        DataTable table = new DataTable(model).weights(38, 12, 18, 18, 14);
        table.actionColumn(4, new String[]{"\u2212", "+", "Remove"}, (row, link) -> {
            SaleLine l = cart.get(row);
            if (link == 0) {
                setQty(l, l.getQty() - 1);
            } else if (link == 1) {
                setQty(l, l.getQty() + 1);
            } else {
                cart.remove(row);
            }
            refresh();
        });
        model.addTableModelListener(e -> updateTotal());

        RoundedPanel left = RoundedPanel.card(new BorderLayout(0, 12));
        left.setBorder(Forms.pad(18, 20, 18, 20));
        JPanel head = new JPanel(new BorderLayout(0, 4));
        head.setOpaque(false);
        head.add(search, BorderLayout.NORTH);
        head.add(info, BorderLayout.SOUTH);
        left.add(head, BorderLayout.NORTH);
        left.add(top(table.wrapper()), BorderLayout.CENTER);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        JPanel c1 = Forms.labeled("Customer", customer);
        JPanel c2 = Forms.labeled("Payment", payment);
        JLabel totalCap = Forms.muted("Total", 14);
        c1.setAlignmentX(0f);
        c2.setAlignmentX(0f);
        totalCap.setAlignmentX(0f);
        total.setAlignmentX(0f);
        form.add(c1);
        form.add(javax.swing.Box.createVerticalStrut(14));
        form.add(c2);
        form.add(javax.swing.Box.createVerticalStrut(18));
        form.add(totalCap);
        form.add(total);
        form.add(javax.swing.Box.createVerticalStrut(16));
        JLabel clear = Forms.link("Clear sale", Theme.MUTED, () -> {
            cart.clear();
            customer.setText("");
            refresh();
        });
        clear.setAlignmentX(0f);
        form.add(clear);
        RoundedPanel right = titledCard("Order summary", form);
        right.setPreferredSize(new Dimension(340, 100));

        JPanel body = new JPanel(new BorderLayout(Theme.GAP, 0));
        body.setOpaque(false);
        body.add(left, BorderLayout.CENTER);
        body.add(right, BorderLayout.EAST);
        setBody(body);
    }

    @Override
    public void onShow() {
        // Stock may have changed elsewhere: drop lines that can no longer be fulfilled.
        InventoryService inv = InventoryService.get();
        cart.removeIf(l -> inv.findBySku(l.getSku()) == null || inv.findBySku(l.getSku()).getStock() <= 0);
        for (SaleLine l : cart) {
            Product p = inv.findBySku(l.getSku());
            if (l.getQty() > p.getStock()) {
                l.setQty(p.getStock());
            }
        }
        refresh();
        info.setText(" ");
    }

    private void refresh() {
        model.fireTableDataChanged();
        updateTotal();
    }

    private double cartTotal() {
        double t = 0;
        for (SaleLine l : cart) {
            t += l.subtotal();
        }
        return Math.round(t * 100.0) / 100.0;
    }

    private void updateTotal() {
        total.setText(Money.peso(cartTotal()));
    }

    private void setQty(SaleLine l, int qty) {
        Product p = InventoryService.get().findBySku(l.getSku());
        int max = p == null ? 0 : p.getStock();
        if (qty > max) {
            Forms.error(info, "Only " + max + " unit(s) of " + l.getName() + " are available.");
            qty = max;
        } else {
            info.setText(" ");
        }
        l.setQty(Math.max(1, qty));
    }

    private void suggest() {
        popup.setVisible(false);
        popup.removeAll();
        String q = search.getText().trim();
        if (q.isEmpty()) {
            return;
        }
        List<Product> found = InventoryService.get().search(q, null);
        for (int i = 0; i < found.size() && i < 8; i++) {
            Product p = found.get(i);
            JMenuItem mi = new JMenuItem(p.getSku() + "   " + p.getName() + "   " + Money.peso(p.getPrice())
                    + (p.getStock() <= 0 ? "   (out of stock)" : "   stock " + p.getStock()));
            mi.setEnabled(p.getStock() > 0);
            mi.addActionListener(e -> add(p));
            popup.add(mi);
        }
        if (popup.getComponentCount() > 0 && search.isShowing()) {
            popup.show(search, 0, search.getHeight());
        }
    }

    private void addFromSearch() {
        String q = search.getText().trim();
        if (q.isEmpty()) {
            return;
        }
        Product p = InventoryService.get().findBySku(q);
        if (p == null) {
            List<Product> found = InventoryService.get().search(q, null);
            p = found.isEmpty() ? null : found.get(0);
        }
        if (p == null) {
            Forms.error(info, "No product matches \"" + q + "\".");
            return;
        }
        add(p);
    }

    private void add(Product p) {
        popup.setVisible(false);
        if (p.getStock() <= 0) {
            Forms.error(info, p.getName() + " is out of stock.");
            return;
        }
        SaleLine existing = null;
        for (SaleLine l : cart) {
            if (l.getSku().equals(p.getSku())) {
                existing = l;
            }
        }
        if (existing == null) {
            cart.add(new SaleLine(p.getSku(), p.getName(), 1, p.getPrice()));
            info.setText(" ");
        } else {
            setQty(existing, existing.getQty() + 1);
        }
        search.setText("");
        popup.setVisible(false);
        refresh();
    }

    private String customerName() {
        String c = customer.getText().trim();
        return c.isEmpty() ? "Walk-in" : c;
    }

    private void previewReceipt() {
        if (cart.isEmpty()) {
            Dialogs.message(nav.window(), "Receipt preview", "Add at least one product first.");
            return;
        }
        String text = SalesService.receiptText("PREVIEW (not recorded)", customerName(),
                (String) payment.getSelectedItem(), LocalDateTime.now(), cart);
        new ReceiptDialog(nav.window(), "Receipt preview", text, "receipt-preview").open();
    }

    private void completeSale() {
        try {
            Sale s = SalesService.get().complete(customer.getText(), (String) payment.getSelectedItem(), cart);
            cart.clear();
            customer.setText("");
            info.setText(" ");
            refresh();
            new ReceiptDialog(nav.window(), "Sale " + s.getNumber() + " completed",
                    SalesService.receiptText(s), "receipt-" + s.getNumber()).open();
        } catch (IllegalArgumentException ex) {
            Dialogs.message(nav.window(), "Cannot complete sale", ex.getMessage());
        }
    }

    /** Table model over the cart; quantity cells are editable. */
    private final class CartModel extends AbstractTableModel {

        private final String[] cols = {"Part", "Qty", "Unit price", "Subtotal", ""};

        @Override
        public int getRowCount() {
            return cart.size();
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public String getColumnName(int c) {
            return cols[c];
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return c == 1 ? Integer.class : String.class;
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return c == 1;
        }

        @Override
        public Object getValueAt(int r, int c) {
            SaleLine l = cart.get(r);
            switch (c) {
                case 0: return l.getName();
                case 1: return l.getQty();
                case 2: return Money.peso(l.getUnitPrice());
                case 3: return Money.peso(l.subtotal());
                default: return "";
            }
        }

        @Override
        public void setValueAt(Object v, int r, int c) {
            if (c != 1) {
                return;
            }
            try {
                setQty(cart.get(r), Integer.parseInt(String.valueOf(v).trim()));
            } catch (NumberFormatException ex) {
                Forms.error(info, "Quantity must be a whole number.");
            }
            fireTableDataChanged();
        }
    }
}
