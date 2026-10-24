package motorcyclepartsstore.model;

public class SaleLine {

    private final String sku;
    private final String name;
    private int qty;
    private final double unitPrice;

    public SaleLine(String sku, String name, int qty, double unitPrice) {
        this.sku = sku;
        this.name = name;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public double getUnitPrice() { return unitPrice; }

    public double subtotal() {
        return Math.round(qty * unitPrice * 100.0) / 100.0;
    }
}
