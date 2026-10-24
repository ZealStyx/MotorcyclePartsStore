package motorcyclepartsstore.model;

public class Product {

    public static final int LOW_STOCK_LEVEL = 10;

    private String sku;
    private String name;
    private String category;
    private int stock;
    private double price;

    public Product(String sku, String name, String category, int stock, double price) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.price = price;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String status() {
        if (stock <= 0) {
            return "Out of stock";
        }
        return stock <= LOW_STOCK_LEVEL ? "Low stock" : "In stock";
    }

    public boolean needsAttention() {
        return stock <= LOW_STOCK_LEVEL;
    }
}
