package motorcyclepartsstore.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import motorcyclepartsstore.model.Product;
import motorcyclepartsstore.service.InventoryService;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Forms;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.RoundButton;
import motorcyclepartsstore.ui.RoundedPanel;
import motorcyclepartsstore.ui.ScreenPanel;

/** Inventory list with search, category filter, stock in and add/edit (reference page 12). */
public class InventoryPanel extends ScreenPanel {

    private final JTextField search = Forms.text("Search parts");
    private final JComboBox<String> category = Forms.combo(new String[]{"All categories"});
    private final DataTable.ListModel model = new DataTable.ListModel("SKU", "Part", "Category", "Stock", "Price", "Status", "");
    private List<Product> shown = new ArrayList<>();
    private boolean updating;

    public InventoryPanel(Navigator nav) {
        this(nav, RoundButton.secondary("Stock in"), RoundButton.primary("Add product"));
    }

    private InventoryPanel(Navigator nav, RoundButton stockIn, RoundButton add) {
        super(nav, "Inventory", "Add, edit and track stock", false, stockIn, add);
        stockIn.addActionListener(e -> {
            if (new StockInDialog(nav.window()).showDialog()) {
                onShow();
            }
        });
        add.addActionListener(e -> {
            if (new ProductDialog(nav.window(), null).showDialog()) {
                onShow();
            }
        });
        search.setPreferredSize(new Dimension(340, 40));
        category.setPreferredSize(new Dimension(170, 40));
        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                reload();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                reload();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                reload();
            }
        });
        category.addActionListener(e -> reload());
        DataTable table = new DataTable(model).weights(13, 28, 16, 10, 14, 13, 8).chipColumn(5)
                .actionColumn(6, new String[]{"Edit"}, (row, link) -> {
                    if (new ProductDialog(nav.window(), shown.get(row)).showDialog()) {
                        onShow();
                    }
                });
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        filters.setOpaque(false);
        filters.add(search);
        filters.add(category);
        RoundedPanel card = RoundedPanel.card(new BorderLayout(0, 14));
        card.setBorder(Forms.pad(18, 20, 18, 20));
        card.add(filters, BorderLayout.NORTH);
        card.add(top(table.wrapper()), BorderLayout.CENTER);
        setBody(card);
    }

    @Override
    public void onShow() {
        updating = true;
        Object sel = category.getSelectedItem();
        category.removeAllItems();
        category.addItem("All categories");
        for (String c : InventoryService.get().categories()) {
            category.addItem(c);
        }
        category.setSelectedItem(sel == null ? "All categories" : sel);
        if (category.getSelectedItem() == null) {
            category.setSelectedIndex(0);
        }
        updating = false;
        reload();
    }

    private void reload() {
        if (updating) {
            return;
        }
        shown = InventoryService.get().search(search.getText(), (String) category.getSelectedItem());
        List<Object[]> rows = new ArrayList<>();
        for (Product p : shown) {
            rows.add(new Object[]{p.getSku(), p.getName(), p.getCategory(), p.getStock(), Money.peso(p.getPrice()), p.status(), ""});
        }
        model.setRows(rows);
    }
}
