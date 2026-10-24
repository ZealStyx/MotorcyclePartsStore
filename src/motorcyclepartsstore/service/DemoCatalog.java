package motorcyclepartsstore.service;

/** Fixed demo catalog shared by the inventory seed data and the demo prediction dataset. */
public final class DemoCatalog {

    /** sku, name, category, stock, price, base weekly demand. */
    public static final Object[][] ITEMS = {
        {"BRK-001", "Brake pad set, front", "Brakes", 48, 650.0, 14},
        {"BRK-002", "Brake pad set, rear", "Brakes", 36, 580.0, 9},
        {"BRK-003", "Brake disc rotor", "Brakes", 14, 1450.0, 3},
        {"DRV-001", "Drive chain 428H", "Drivetrain", 25, 890.0, 11},
        {"DRV-002", "Sprocket set", "Drivetrain", 18, 1250.0, 5},
        {"ENG-001", "Spark plug", "Engine", 6, 185.0, 16},
        {"ENG-002", "Air filter", "Engine", 30, 320.0, 7},
        {"FLD-001", "Engine oil 1L", "Fluids", 60, 420.0, 2},
        {"FLD-002", "Brake fluid DOT3", "Fluids", 22, 210.0, 4},
        {"ELC-001", "Battery 12V 5Ah", "Electrical", 9, 1350.0, 4},
        {"ELC-002", "Headlight bulb H4", "Electrical", 0, 240.0, 8},
        {"TIR-001", "Tire 80/90-14", "Tires", 12, 1900.0, 3},
    };

    private DemoCatalog() {
    }

    public static String name(int i) { return (String) ITEMS[i][1]; }
    public static String category(int i) { return (String) ITEMS[i][2]; }
    public static double price(int i) { return (Double) ITEMS[i][4]; }
    public static int baseDemand(int i) { return (Integer) ITEMS[i][5]; }
    public static int size() { return ITEMS.length; }
}
