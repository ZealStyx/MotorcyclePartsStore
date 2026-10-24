package motorcyclepartsstore.screens;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JComponent;
import javax.swing.JLabel;
import motorcyclepartsstore.service.InventoryService;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.service.PredictionService;
import motorcyclepartsstore.service.SalesService;
import motorcyclepartsstore.ui.DataTable;
import motorcyclepartsstore.ui.Forms;
import motorcyclepartsstore.ui.HBarChart;
import motorcyclepartsstore.ui.Icons;
import motorcyclepartsstore.ui.LineAreaChart;
import motorcyclepartsstore.ui.Navigator;
import motorcyclepartsstore.ui.RoundedPanel;
import motorcyclepartsstore.ui.ScreenPanel;
import motorcyclepartsstore.ui.SummaryCard;
import motorcyclepartsstore.ui.Theme;

/** Admin dashboard (reference page 3). */
public class AdminDashboardPanel extends ScreenPanel {

    private final SummaryCard totalSales = new SummaryCard("Total sales", Icons.Kind.CART);
    private final SummaryCard products = new SummaryCard("Products", Icons.Kind.BOX);
    private final SummaryCard lowStock = new SummaryCard("Low stock", Icons.Kind.ALERT);
    private final SummaryCard transactions = new SummaryCard("Transactions", Icons.Kind.TREND);
    private final LineAreaChart trend = new LineAreaChart();
    private final HBarChart best = new HBarChart();
    private final DataTable.ListModel slowModel = new DataTable.ListModel("Part", "Last sold", "Stock");

    public AdminDashboardPanel(Navigator nav) {
        super(nav, "Dashboard", "Admin overview", true);
        DataTable slow = new DataTable(slowModel).weights(40, 30, 30);

        RoundedPanel trendCard = titledCard("Sales trend and prediction", chartWithCaption(trend,
                "Demo dataset: units per quarter, actual vs combined model."));
        RoundedPanel bestCard = titledCard("Best selling", best);
        JComponent middle = split(trendCard, bestCard, 0.68);
        RoundedPanel slowCard = titledCard("Slow moving products", slow.wrapper());

        javax.swing.JPanel body = new javax.swing.JPanel(new BorderLayout(0, Theme.GAP));
        body.setOpaque(false);
        body.add(row(Theme.GAP, totalSales, products, lowStock, transactions), BorderLayout.NORTH);
        javax.swing.JPanel lower = new javax.swing.JPanel(new BorderLayout(0, Theme.GAP));
        lower.setOpaque(false);
        lower.add(middle, BorderLayout.CENTER);
        lower.add(slowCard, BorderLayout.SOUTH);
        body.add(lower, BorderLayout.CENTER);
        setBody(body);
    }

    static JComponent chartWithCaption(JComponent chart, String caption) {
        javax.swing.JPanel p = new javax.swing.JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(chart, BorderLayout.CENTER);
        JLabel c = Forms.muted(caption, 12);
        p.add(c, BorderLayout.SOUTH);
        return p;
    }

    @Override
    public void onShow() {
        SalesService sales = SalesService.get();
        InventoryService inv = InventoryService.get();
        totalSales.setValue(Money.peso(sales.totalSales()));
        products.setValue(String.valueOf(inv.all().size()));
        lowStock.setValue(String.valueOf(inv.lowStockCount()));
        transactions.setValue(String.valueOf(sales.transactionCount()));
        best.setData(sales.bestSelling(4), "pcs");
        DateTimeFormatter f = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
        List<Object[]> rows = new ArrayList<>();
        for (Object[] r : sales.slowMoving(4)) {
            rows.add(new Object[]{r[0], r[1] == null ? "Never" : ((LocalDate) r[1]).format(f), r[2]});
        }
        slowModel.setRows(rows);
        PredictionService ps = PredictionService.get();
        if (ps.isReady()) {
            trend.setSeries(ps.getTrend());
        } else if (ps.getError() != null) {
            trend.setMessage(ps.getError());
        } else {
            trend.setMessage("Training demo models...");
        }
    }
}
