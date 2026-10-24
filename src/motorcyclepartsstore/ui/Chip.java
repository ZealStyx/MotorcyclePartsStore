package motorcyclepartsstore.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import motorcyclepartsstore.model.Role;

/** Small pill used for status, class and role badges. */
public class Chip extends JComponent {

    public enum Kind { GREEN, BLUE, RED, GREY, ACCENT }

    private String text;
    private Kind kind;

    public Chip(String text, Kind kind) {
        this.text = text;
        this.kind = kind;
        setFont(Theme.body(12));
        setOpaque(false);
    }

    public void set(String text, Kind kind) {
        this.text = text;
        this.kind = kind;
        revalidate();
        repaint();
    }

    public static Kind forStatus(String s) {
        if (s == null) {
            return Kind.GREY;
        }
        switch (s) {
            case "Active": case "In stock": case "Paid": case "Best selling": case "Combined":
                return Kind.GREEN;
            case "Low stock": case "Out of stock": case "Slow moving": case "Inactive":
                return Kind.RED;
            case "Average": case "Linear": case "Forest":
                return Kind.BLUE;
            default:
                return Kind.GREY;
        }
    }

    /** Role badges: Admin is green, all other levels blue (as in the reference design). */
    public static Chip forRole(Role role) {
        return new Chip(role.getLabel(), role == Role.ADMIN ? Kind.GREEN : Kind.BLUE);
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        return new Dimension(fm.stringWidth(text) + 22, fm.getHeight() + 8);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Color bg;
        Color fg;
        switch (kind) {
            case GREEN: bg = Theme.GREEN_BG; fg = Theme.GREEN_FG; break;
            case BLUE: bg = Theme.BLUE_BG; fg = Theme.BLUE_FG; break;
            case RED: bg = Theme.RED_BG; fg = Theme.RED_FG; break;
            case ACCENT: bg = Theme.ACCENT; fg = Color.WHITE; break;
            default: bg = Theme.GREY_BG; fg = Theme.GREY_FG;
        }
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
        g2.setColor(fg);
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, (getWidth() - fm.stringWidth(text)) / 2,
                (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
        g2.dispose();
    }
}
