package motorcyclepartsstore.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import motorcyclepartsstore.model.Product;
import motorcyclepartsstore.model.Role;

/** In-memory product catalog and stock operations. */
public final class InventoryService {

    private static final InventoryService INSTANCE = new InventoryService();
    private final List<Product> products = new ArrayList<>();

    private InventoryService() {
        for (Object[] i : DemoCatalog.ITEMS) {
            products.add(new Product((String) i[0], (String) i[1], (String) i[2], (Integer) i[3], (Double) i[4]));
        }
    }

    public static InventoryService get() {
        return INSTANCE;
    }

    public List<Product> all() {
        return new ArrayList<>(products);
    }

    public Product findBySku(String sku) {
        if (sku == null) {
            return null;
        }
        for (Product p : products) {
            if (p.getSku().equalsIgnoreCase(sku.trim())) {
                return p;
            }
        }
        return null;
    }

    public List<String> categories() {
        Set<String> set = new LinkedHashSet<>();
        for (Product p : products) {
            set.add(p.getCategory());
        }
        return new ArrayList<>(set);
    }

    /** Search by part name or SKU; category null or "All categories" means any. */
    public List<Product> search(String query, String category) {
        String q = query == null ? "" : query.trim().toLowerCase();
        List<Product> out = new ArrayList<>();
        for (Product p : products) {
            boolean catOk = category == null || category.startsWith("All") || p.getCategory().equalsIgnoreCase(category);
            boolean textOk = q.isEmpty() || p.getName().toLowerCase().contains(q) || p.getSku().toLowerCase().contains(q);
            if (catOk && textOk) {
                out.add(p);
            }
        }
        return out;
    }

    public List<Product> lowStock() {
        List<Product> out = new ArrayList<>();
        for (Product p : products) {
            if (p.needsAttention()) {
                out.add(p);
            }
        }
        out.sort((a, b) -> Integer.compare(a.getStock(), b.getStock()));
        return out;
    }

    public int lowStockCount() {
        int n = 0;
        for (Product p : products) {
            if (p.getStock() > 0 && p.getStock() <= Product.LOW_STOCK_LEVEL) {
                n++;
            }
        }
        return n;
    }

    public int outOfStockCount() {
        int n = 0;
        for (Product p : products) {
            if (p.getStock() <= 0) {
                n++;
            }
        }
        return n;
    }

    public double stockValue() {
        double v = 0;
        for (Product p : products) {
            v += p.getStock() * p.getPrice();
        }
        return v;
    }

    /** Stock units per category, in category order. */
    public java.util.Map<String, Integer> stockByCategory() {
        java.util.Map<String, Integer> m = new java.util.LinkedHashMap<>();
        for (Product p : products) {
            m.merge(p.getCategory(), p.getStock(), Integer::sum);
        }
        return m;
    }

    /**
     * Adds (existing == null) or updates a product. Only the Inventory clerk may do this.
     * Returns an error message or null on success.
     */
    public String save(Product existing, String sku, String name, String category, String stockText, String priceText) {
        Session.require(Role.INVENTORY_CLERK);
        if (sku == null || sku.trim().isEmpty()) {
            return "SKU is required.";
        }
        if (name == null || name.trim().isEmpty()) {
            return "Part name is required.";
        }
        if (category == null || category.trim().isEmpty()) {
            return "Category is required.";
        }
        Product dup = findBySku(sku);
        if (dup != null && dup != existing) {
            return "That SKU already exists.";
        }
        int stock;
        double price;
        try {
            stock = Integer.parseInt(stockText.trim());
        } catch (NumberFormatException ex) {
            return "Stock must be a whole number.";
        }
        try {
            price = Double.parseDouble(priceText.trim().replace(",", ""));
        } catch (NumberFormatException ex) {
            return "Price must be a number.";
        }
        if (stock < 0) {
            return "Stock cannot be negative.";
        }
        if (price < 0 || Double.isNaN(price) || Double.isInfinite(price)) {
            return "Price cannot be negative.";
        }
        price = Math.round(price * 100.0) / 100.0;
        if (existing == null) {
            products.add(new Product(sku.trim().toUpperCase(), name.trim(), category.trim(), stock, price));
        } else {
            existing.setSku(sku.trim().toUpperCase());
            existing.setName(name.trim());
            existing.setCategory(category.trim());
            existing.setStock(stock);
            existing.setPrice(price);
        }
        return null;
    }

    /** Adds received units to a product's stock. Returns an error message or null on success. */
    public String stockIn(Product product, String qtyText) {
        Session.require(Role.INVENTORY_CLERK);
        if (product == null) {
            return "Choose a product.";
        }
        int qty;
        try {
            qty = Integer.parseInt(qtyText.trim());
        } catch (NumberFormatException ex) {
            return "Quantity must be a whole number.";
        }
        if (qty <= 0) {
            return "Quantity must be greater than zero.";
        }
        product.setStock(product.getStock() + qty);
        return null;
    }

    /** Used by SalesService after validation; not role-gated because the sale itself is. */
    void reduceStock(Product product, int qty) {
        product.setStock(product.getStock() - qty);
    }
}
