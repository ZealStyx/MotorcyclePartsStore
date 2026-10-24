package motorcyclepartsstore.model;

/** User levels of the system. */
public enum Role {
    ADMIN("Admin"),
    INVENTORY_CLERK("Inventory clerk"),
    SALES("Sales personnel"),
    INSTRUCTOR("Instructor");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    /** Two-letter initials used for the sidebar avatar. */
    public String initials() {
        switch (this) {
            case ADMIN: return "AD";
            case INVENTORY_CLERK: return "IC";
            case SALES: return "SP";
            default: return "IN";
        }
    }
}
