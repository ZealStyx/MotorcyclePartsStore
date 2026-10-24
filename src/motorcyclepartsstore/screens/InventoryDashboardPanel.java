package motorcyclepartsstore.screens;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import motorcyclepartsstore.model.Product;
import motorcyclepartsstore.service.InventoryService;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.DonutChart;
import motorcyclepartsstore.ui.Icons;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.RoundButton;
import motorcyclepartsstore.ui.ScreenPanel;
import motorcyclepartsstore.ui.SummaryCard;
import motorcyclepartsstore.ui.Theme;

/** Inventory clerk dashboard (reference page 11). */
public class InventoryDashboardPanel extends ScreenPanel {

    private final SummaryCard products = new SummaryCard("Products", Icons.Kind.BOX);
    private final SummaryCard low = new SummaryCard("Low stock", Icons.Kind.ALERT);
    private final SummaryCard out = new SummaryCard("Out of stock", Icons.Kind.ALERT);
    private final SummaryCard value = new SummaryCard("Stock value", Icons.Kind.CART);
    private final DataTable.ListModel model = new DataTable.ListModel("SKU", "Part", "Category", "Stock", "Price", "Status", "");
    private final DonutChart donut = new DonutChart();
    private List<Product> shown = new ArrayList<>();

    public InventoryDashboardPanel(Navigator nav) {
        this(nav, RoundButton.primary("Add product"));
    }

    private InventoryDashboardPanel(Navigator nav, RoundButton add) {
        super(nav, "Dashboard", "Stock overview", true, add);
        add.addActionListener(e -> {
            if (new ProductDialog(nav.window(), null).showDialog()) {
                onShow();
            }
        });
        DataTable table = new DataTable(model).weights(13, 28, 16, 10, 14, 13, 8).chipColumn(5)
                .actionColumn(6, new String[]{"Edit"}, (row, link) -> {
                    if (new ProductDialog(nav.window(), shown.get(row)).showDialog()) {
                        onShow();
                    }
                });
        JPanel body = new JPanel(new BorderLayout(0, Theme.GAP));
        body.setOpaque(false);
        body.add(row(Theme.GAP, products, low, out, value), BorderLayout.NORTH);
        body.add(split(titledCard("Low stock alerts", top(table.wrapper())),
                titledCard("Stock by category", donut), 0.68), BorderLayout.CENTER);
        setBody(body);
    }

    @Override
    public void onShow() {
        InventoryService inv = InventoryService.get();
        products.setValue(String.valueOf(inv.all().size()));
        low.setValue(String.valueOf(inv.lowStockCount()));
        out.setValue(String.valueOf(inv.outOfStockCount()));
        value.setValue(Money.peso(inv.stockValue()));
        shown = inv.lowStock();
        List<Object[]> rows = new ArrayList<>();
        for (Product p : shown) {
            rows.add(new Object[]{p.getSku(), p.getName(), p.getCategory(), p.getStock(), Money.peso(p.getPrice()), p.status(), ""});
        }
        model.setRows(rows);
        Map<String, Integer> byCat = inv.stockByCategory();
        double[] v = new double[byCat.size()];
        int i = 0;
        for (int n : byCat.values()) {
            v[i++] = n;
        }
        donut.setData(byCat.keySet().toArray(new String[0]), v);
    }
}
