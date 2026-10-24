package motorcyclepartsstore.screens;

import java.awt.BorderLayout;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import motorcyclepartsstore.model.Sale;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.service.SalesService;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.HBarChart;
import motorcyclepartsstore.ui.Icons;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.Page;
import motorcyclepartsstore.ui.RoundButton;
import motorcyclepartsstore.ui.ScreenPanel;
import motorcyclepartsstore.ui.SummaryCard;
import motorcyclepartsstore.ui.Theme;

/** Sales personnel dashboard (reference page 13). */
public class SalesDashboardPanel extends ScreenPanel {

    private final SummaryCard today = new SummaryCard("Sales today", Icons.Kind.CART);
    private final SummaryCard count = new SummaryCard("Transactions", Icons.Kind.TREND);
    private final SummaryCard items = new SummaryCard("Items sold", Icons.Kind.BOX);
    private final SummaryCard avg = new SummaryCard("Avg. sale", Icons.Kind.PLUS);
    private final DataTable.ListModel model = new DataTable.ListModel("No.", "Customer", "Total", "Status");
    private final HBarChart bars = new HBarChart();

    public SalesDashboardPanel(Navigator nav) {
        this(nav, RoundButton.primary("New sale"));
    }

    private SalesDashboardPanel(Navigator nav, RoundButton newSale) {
        super(nav, "Dashboard", "Today at the counter", true, newSale);
        newSale.addActionListener(e -> nav.navigate(Page.SALES_NEW));
        DataTable t = new DataTable(model).weights(18, 32, 28, 22).chipColumn(3);
        JPanel body = new JPanel(new BorderLayout(0, Theme.GAP));
        body.setOpaque(false);
        body.add(row(Theme.GAP, today, count, items, avg), BorderLayout.NORTH);
        body.add(split(titledCard("Recent transactions", top(t.wrapper())),
                titledCard("Sales today", bars), 0.68), BorderLayout.CENTER);
        setBody(body);
    }

    @Override
    public void onShow() {
        SalesService s = SalesService.get();
        today.setValue(Money.peso(s.todayTotal()));
        count.setValue(String.valueOf(s.today().size()));
        items.setValue(String.valueOf(s.todayItems()));
        avg.setValue(Money.peso(s.todayAverage()));
        List<Object[]> rows = new ArrayList<>();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("MMM d");
        for (Sale x : s.recent(8)) {
            rows.add(new Object[]{x.getNumber(), x.getCustomer() + "  (" + x.getTime().format(f) + ")",
                Money.peso(x.total()), x.getStatus()});
        }
        model.setRows(rows);
        bars.setData(s.todayByProduct(4), "pcs");
    }
}
