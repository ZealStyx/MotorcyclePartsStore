package motorcyclepartsstore.ui;

import java.util.ArrayList;
import java.util.List;
import motorcyclepartsstore.model.Role;

/** Every navigable page and the single role allowed to open it. */
public enum Page {
    ADMIN_DASHBOARD(Role.ADMIN, "Dashboard", Icons.Kind.GRID),
    ADMIN_PREDICTION(Role.ADMIN, "Prediction", Icons.Kind.TREND),
    ADMIN_USERS(Role.ADMIN, "User management", Icons.Kind.USER),
    INSTRUCTOR_DASHBOARD(Role.INSTRUCTOR, "Dashboard", Icons.Kind.GRID),
    INSTRUCTOR_PREDICTION(Role.INSTRUCTOR, "Prediction", Icons.Kind.TREND),
    INSTRUCTOR_EVALUATION(Role.INSTRUCTOR, "Evaluation", Icons.Kind.BARS),
    INVENTORY_DASHBOARD(Role.INVENTORY_CLERK, "Dashboard", Icons.Kind.GRID),
    INVENTORY_LIST(Role.INVENTORY_CLERK, "Inventory", Icons.Kind.BOX),
    SALES_DASHBOARD(Role.SALES, "Dashboard", Icons.Kind.GRID),
    SALES_NEW(Role.SALES, "New sale", Icons.Kind.CART);

    public final Role role;
    public final String label;
    public final Icons.Kind icon;

    Page(Role role, String label, Icons.Kind icon) {
        this.role = role;
        this.label = label;
        this.icon = icon;
    }

    public static List<Page> forRole(Role role) {
        List<Page> out = new ArrayList<>();
        for (Page p : values()) {
            if (p.role == role) {
                out.add(p);
            }
        }
        return out;
    }
}
