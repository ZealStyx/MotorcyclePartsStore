package motorcyclepartsstore.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

/** Self-painted rounded button with hover / pressed states. */
public class RoundButton extends JButton {

    public enum Kind { PRIMARY, SECONDARY, DANGER }

    private final Kind kind;

    /** Primary (accent) button; also used by the form designer. */
    public RoundButton() {
        this("Button", Kind.PRIMARY);
    }

    public RoundButton(String text, Kind kind) {
        super(text);
        this.kind = kind;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setFont(Theme.bold(14));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setForeground(kind == Kind.SECONDARY ? Theme.INK : Color.WHITE);
    }

    public static RoundButton primary(String text) {
        return new RoundButton(text, Kind.PRIMARY);
    }

    public static RoundButton secondary(String text) {
        return new RoundButton(text, Kind.SECONDARY);
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        FontMetrics fm = getFontMetrics(getFont());
        return new Dimension(fm.stringWidth(getText()) + 40, 42);
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension p = getPreferredSize();
        return new Dimension(Math.min(p.width, 60), p.height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        boolean pressed = getModel().isPressed();
        boolean hover = getModel().isRollover();
        Color bg;
        Color border = null;
        switch (kind) {
            case PRIMARY:
                bg = pressed ? Theme.ACCENT_PRESSED : hover ? Theme.ACCENT_HOVER : Theme.ACCENT;
                break;
            case DANGER:
                bg = pressed ? new Color(0x70, 0x20, 0x08) : hover ? new Color(0x7A, 0x23, 0x0A) : Theme.DANGER;
                break;
            default:
                bg = pressed ? new Color(0xEF, 0xEB, 0xE4) : hover ? new Color(0xF7, 0xF4, 0xEF) : Color.WHITE;
                border = Theme.FIELD_BORDER;
        }
        if (!isEnabled()) {
            bg = new Color(0xD8, 0xD4, 0xCD);
        }
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
        if (border != null) {
            g2.setColor(border);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
        }
        if (isFocusOwner()) {
            g2.setColor(new Color(Theme.ACCENT.getRed(), Theme.ACCENT.getGreen(), Theme.ACCENT.getBlue(), 120));
            g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 11, 11);
        }
        g2.setFont(getFont());
        g2.setColor(isEnabled() ? getForeground() : Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(getText(), x, y);
        g2.dispose();
    }
}
