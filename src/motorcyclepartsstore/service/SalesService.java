package motorcyclepartsstore.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import motorcyclepartsstore.model.Product;
import motorcyclepartsstore.model.Role;
import motorcyclepartsstore.model.Sale;
import motorcyclepartsstore.model.SaleLine;

/** Sales transactions (in-memory demo data) and the numbers shown on the dashboards. */
public final class SalesService {

    public static final String[] PAYMENT_METHODS = {"Cash", "GCash", "Credit / debit card"};

    private static final SalesService INSTANCE = new SalesService();
    private final List<Sale> sales = new ArrayList<>();
    private int counter;

    private SalesService() {
        seed();
    }

    public static SalesService get() {
        return INSTANCE;
    }

    private void seed() {
        InventoryService inv = InventoryService.get();
        Random rnd = new Random(42);
        String[] customers = {"Walk-in", "R. Mendoza", "J. Bautista", "K. Villanueva", "M. Aquino", "D. Cruz"};
        LocalDate today = LocalDate.now();
        List<Sale> temp = new ArrayList<>();
        // Older history: 60 days, engine oil deliberately excluded so it shows as slow moving.
        for (int day = 60; day >= 1; day--) {
            int n = 1 + rnd.nextInt(2);
            for (int k = 0; k < n; k++) {
                temp.add(randomSale(inv, rnd, customers, today.minusDays(day).atTime(9 + rnd.nextInt(8), rnd.nextInt(60))));
            }
        }
        // Today's sales.
        int[] hours = {8, 9, 11, 13};
        for (int h : hours) {
            temp.add(randomSale(inv, rnd, customers, today.atTime(h, rnd.nextInt(60))));
        }
        // One old engine oil sale.
        List<SaleLine> oil = new ArrayList<>();
        Product op = inv.findBySku("FLD-001");
        oil.add(new SaleLine(op.getSku(), op.getName(), 2, op.getPrice()));
        temp.add(new Sale("", "Walk-in", "Cash", today.minusDays(75).atTime(10, 30), oil));
        temp.sort((a, b) -> a.getTime().compareTo(b.getTime()));
        for (Sale s : temp) {
            counter++;
            sales.add(new Sale(String.format("S-%04d", counter), s.getCustomer(), s.getPayment(), s.getTime(), s.getLines()));
        }
    }

    private Sale randomSale(InventoryService inv, Random rnd, String[] customers, LocalDateTime time) {
        List<SaleLine> lines = new ArrayList<>();
        int items = 1 + rnd.nextInt(3);
        for (int i = 0; i < items; i++) {
            int idx;
            do {
                idx = weightedIndex(rnd);
            } while (idx == 7); // Engine oil 1L is not part of the random history
            Product p = inv.findBySku((String) DemoCatalog.ITEMS[idx][0]);
            SaleLine existing = null;
            for (SaleLine l : lines) {
                if (l.getSku().equals(p.getSku())) {
                    existing = l;
                }
            }
            int qty = 1 + rnd.nextInt(2);
            if (existing != null) {
                existing.setQty(existing.getQty() + qty);
            } else {
                lines.add(new SaleLine(p.getSku(), p.getName(), qty, p.getPrice()));
            }
        }
        String[] pay = PAYMENT_METHODS;
        return new Sale("", customers[rnd.nextInt(customers.length)], pay[rnd.nextInt(pay.length)], time, lines);
    }

    private int weightedIndex(Random rnd) {
        int total = 0;
        for (int i = 0; i < DemoCatalog.size(); i++) {
            total += DemoCatalog.baseDemand(i);
        }
        int r = rnd.nextInt(total);
        for (int i = 0; i < DemoCatalog.size(); i++) {
            r -= DemoCatalog.baseDemand(i);
            if (r < 0) {
                return i;
            }
        }
        return 0;
    }

    public List<Sale> all() {
        return new ArrayList<>(sales);
    }

    public List<Sale> recent(int n) {
        List<Sale> out = new ArrayList<>();
        for (int i = sales.size() - 1; i >= 0 && out.size() < n; i--) {
            out.add(sales.get(i));
        }
        return out;
    }

    public List<Sale> today() {
        List<Sale> out = new ArrayList<>();
        LocalDate d = LocalDate.now();
        for (Sale s : sales) {
            if (s.getTime().toLocalDate().equals(d)) {
                out.add(s);
            }
        }
        return out;
    }

    public double totalSales() {
        double t = 0;
        for (Sale s : sales) {
            t += s.total();
        }
        return t;
    }

    public int transactionCount() {
        return sales.size();
    }

    public double todayTotal() {
        double t = 0;
        for (Sale s : today()) {
            t += s.total();
        }
        return t;
    }

    public int todayItems() {
        int n = 0;
        for (Sale s : today()) {
            n += s.itemCount();
        }
        return n;
    }

    public double todayAverage() {
        List<Sale> t = today();
        return t.isEmpty() ? 0 : todayTotal() / t.size();
    }

    /** Units sold per product over all recorded sales, highest first. */
    public Map<String, Integer> bestSelling(int limit) {
        return topUnits(sales, limit);
    }

    public Map<String, Integer> todayByProduct(int limit) {
        return topUnits(today(), limit);
    }

    private Map<String, Integer> topUnits(List<Sale> source, int limit) {
        Map<String, Integer> units = new LinkedHashMap<>();
        for (Sale s : source) {
            for (SaleLine l : s.getLines()) {
                units.merge(l.getName(), l.getQty(), Integer::sum);
            }
        }
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(units.entrySet());
        entries.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        Map<String, Integer> out = new LinkedHashMap<>();
        for (int i = 0; i < entries.size() && i < limit; i++) {
            out.put(entries.get(i).getKey(), entries.get(i).getValue());
        }
        return out;
    }

    /** Rows of {part name, last sold date or null, stock}, longest unsold first. */
    public List<Object[]> slowMoving(int limit) {
        Map<String, LocalDate> last = new LinkedHashMap<>();
        for (Sale s : sales) {
            for (SaleLine l : s.getLines()) {
                LocalDate d = s.getTime().toLocalDate();
                LocalDate prev = last.get(l.getSku());
                if (prev == null || d.isAfter(prev)) {
                    last.put(l.getSku(), d);
                }
            }
        }
        List<Object[]> rows = new ArrayList<>();
        for (Product p : InventoryService.get().all()) {
            rows.add(new Object[]{p.getName(), last.get(p.getSku()), p.getStock()});
        }
        rows.sort((a, b) -> {
            LocalDate da = (LocalDate) a[1];
            LocalDate db = (LocalDate) b[1];
            if (da == null) {
                return db == null ? 0 : -1;
            }
            return db == null ? 1 : da.compareTo(db);
        });
        return rows.subList(0, Math.min(limit, rows.size()));
    }

    /**
     * Records a sale and reduces stock. Throws IllegalArgumentException with a user-readable message
     * when the cart is invalid. Only Sales personnel may complete sales.
     */
    public Sale complete(String customer, String payment, List<SaleLine> cart) {
        Session.require(Role.SALES);
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Add at least one product to the sale.");
        }
        InventoryService inv = InventoryService.get();
        for (SaleLine l : cart) {
            Product p = inv.findBySku(l.getSku());
            if (p == null) {
                throw new IllegalArgumentException(l.getName() + " no longer exists in the inventory.");
            }
            if (l.getQty() <= 0) {
                throw new IllegalArgumentException("Quantity for " + l.getName() + " must be at least 1.");
            }
            if (l.getQty() > p.getStock()) {
                throw new IllegalArgumentException("Only " + p.getStock() + " unit(s) of " + l.getName() + " are available.");
            }
        }
        List<SaleLine> copy = new ArrayList<>();
        for (SaleLine l : cart) {
            inv.reduceStock(inv.findBySku(l.getSku()), l.getQty());
            copy.add(new SaleLine(l.getSku(), l.getName(), l.getQty(), l.getUnitPrice()));
        }
        counter++;
        String name = customer == null || customer.trim().isEmpty() ? "Walk-in" : customer.trim();
        Sale sale = new Sale(String.format("S-%04d", counter), name, payment, LocalDateTime.now(), copy);
        sales.add(sale);
        return sale;
    }

    /** Plain-text receipt used for the preview, text export and printing. */
    public static String receiptText(String number, String customer, String payment, LocalDateTime time,
            List<SaleLine> lines) {
        StringBuilder sb = new StringBuilder();
        sb.append("ONE PROMPT AWAY\nMotorcycle Parts Store System\n");
        sb.append("--------------------------------------\n");
        sb.append("Receipt: ").append(number).append('\n');
        sb.append("Date:    ").append(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append('\n');
        sb.append("Customer: ").append(customer).append('\n');
        sb.append("Payment:  ").append(payment).append('\n');
        sb.append("--------------------------------------\n");
        double total = 0;
        for (SaleLine l : lines) {
            String name = l.getName().length() > 22 ? l.getName().substring(0, 22) : l.getName();
            sb.append(String.format("%-22s%3d x %9s%n", name, l.getQty(), motorcyclepartsstore.service.Money.plain(l.getUnitPrice())));
            sb.append(String.format("%38s%n", motorcyclepartsstore.service.Money.peso(l.subtotal())));
            total += l.subtotal();
        }
        sb.append("--------------------------------------\n");
        sb.append(String.format("%-20s%18s%n", "TOTAL", Money.peso(total)));
        sb.append("\nThank you for riding with us!\n");
        return sb.toString();
    }

    public static String receiptText(Sale s) {
        return receiptText(s.getNumber(), s.getCustomer(), s.getPayment(), s.getTime(), s.getLines());
    }
}
