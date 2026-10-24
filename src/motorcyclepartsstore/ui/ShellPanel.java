package motorcyclepartsstore.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Window;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JPanel;
import motorcyclepartsstore.model.User;
import motorcyclepartsstore.screens.AdminDashboardPanel;
import motorcyclepartsstore.screens.EvaluationPanel;
import motorcyclepartsstore.screens.InstructorDashboardPanel;
import motorcyclepartsstore.screens.InventoryDashboardPanel;
import motorcyclepartsstore.screens.InventoryPanel;
import motorcyclepartsstore.screens.PredictionPanel;
import motorcyclepartsstore.screens.SalesDashboardPanel;
import motorcyclepartsstore.screens.SalesTransactionPanel;
import motorcyclepartsstore.screens.UserManagementPanel;
import motorcyclepartsstore.service.Session;

/** Signed-in shell: sidebar + one screen at a time, with role enforcement on every navigation. */
public class ShellPanel extends JPanel implements Navigator {

    private final User user;
    private final Window window;
    private final Runnable onLogout;
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final Map<Page, ScreenPanel> screens = new EnumMap<>(Page.class);
    private final Sidebar sidebar;
    private Page current;

    public ShellPanel(User user, Window window, Runnable onLogout) {
        super(new BorderLayout());
        this.user = user;
        this.window = window;
        this.onLogout = onLogout;
        content.setBackground(Theme.WORKSPACE_BG);
        sidebar = new Sidebar(user.getRole(), user.getFullName(), Page.forRole(user.getRole()), this::navigate, onLogout);
        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }

    @Override
    public void navigate(Page page) {
        if (Session.current() != user || page.role != user.getRole()) {
            Dialogs.message(window, "Access denied", "Your user level cannot open that page.");
            return;
        }
        ScreenPanel s = screens.get(page);
        if (s == null) {
            s = create(page);
            screens.put(page, s);
            content.add(s, page.name());
        }
        current = page;
        cards.show(content, page.name());
        sidebar.select(page);
        s.onShow();
    }

    /** Re-reads the visible page (used when background model training finishes). */
    public void refreshCurrent() {
        if (current != null) {
            screens.get(current).onShow();
        }
    }

    private ScreenPanel create(Page page) {
        switch (page) {
            case ADMIN_DASHBOARD: return new AdminDashboardPanel(this);
            case ADMIN_PREDICTION: return new PredictionPanel(this, false);
            case ADMIN_USERS: return new UserManagementPanel(this);
            case INSTRUCTOR_DASHBOARD: return new InstructorDashboardPanel(this);
            case INSTRUCTOR_PREDICTION: return new PredictionPanel(this, true);
            case INSTRUCTOR_EVALUATION: return new EvaluationPanel(this);
            case INVENTORY_DASHBOARD: return new InventoryDashboardPanel(this);
            case INVENTORY_LIST: return new InventoryPanel(this);
            case SALES_DASHBOARD: return new SalesDashboardPanel(this);
            default: return new SalesTransactionPanel(this);
        }
    }

    @Override
    public void logout() {
        onLogout.run();
    }

    @Override
    public Window window() {
        return window;
    }
}
