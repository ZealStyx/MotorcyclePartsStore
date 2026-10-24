package motorcyclepartsstore;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import motorcyclepartsstore.model.User;
import motorcyclepartsstore.service.Session;
import motorcyclepartsstore.ui.AuthPanel;
import motorcyclepartsstore.ui.Icons;
import motorcyclepartsstore.ui.Page;
import motorcyclepartsstore.ui.ShellPanel;
import motorcyclepartsstore.ui.Theme;

/** The single main window: switches between the sign-in screen and the signed-in shell. */
public class AppFrame extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private final AuthPanel auth = new AuthPanel(this::login);
    private ShellPanel shell;

    public AppFrame() {
        super("One Prompt Away - Motorcycle Parts Store System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 680));
        setSize(1360, 840);
        setLocationRelativeTo(null);
        setIconImage(appIcon());
        root.add(auth, "auth");
        setContentPane(root);
    }

    private static BufferedImage appIcon() {
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Theme.ACCENT);
        g.fillRoundRect(0, 0, 64, 64, 20, 20);
        g.translate(10, 10);
        Icons.paint(g, Icons.Kind.LOGO, 44, java.awt.Color.WHITE);
        g.dispose();
        return img;
    }

    private void login(User user) {
        Session.set(user);
        if (shell != null) {
            root.remove(shell);
        }
        shell = new ShellPanel(user, this, this::logout);
        root.add(shell, "shell");
        cards.show(root, "shell");
        shell.navigate(Page.forRole(user.getRole()).get(0));
        root.revalidate();
    }

    private void logout() {
        Session.clear();
        if (shell != null) {
            root.remove(shell);
            shell = null;
        }
        auth.reset();
        cards.show(root, "auth");
        root.revalidate();
        root.repaint();
    }

    /** Called on the EDT when background model training has finished. */
    public void modelsTrained() {
        if (shell != null) {
            shell.refreshCurrent();
        }
    }
}
