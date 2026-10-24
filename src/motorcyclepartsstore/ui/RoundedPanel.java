package motorcyclepartsstore.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/** Panel with a rounded, softly bordered background (cards, dialogs, tiles). */
public class RoundedPanel extends JPanel {

    private final int arc;
    private Color fill;
    private Color line;

    /** Standard white card (usable as a bean in the NetBeans form designer). */
    public RoundedPanel() {
        this(Theme.CARD_ARC, Theme.CARD_BG, Theme.BORDER);
    }

    public RoundedPanel(int arc, Color fill, Color line) {
        this.arc = arc;
        this.fill = fill;
        this.line = line;
        setOpaque(false);
    }

    public RoundedPanel(LayoutManager lm, int arc, Color fill, Color line) {
        this(arc, fill, line);
        setLayout(lm);
    }

    /** White card with the standard radius and border. */
    public static RoundedPanel card(LayoutManager lm) {
        return new RoundedPanel(lm, Theme.CARD_ARC, Theme.CARD_BG, Theme.BORDER);
    }

    public void setFill(Color c) {
        this.fill = c;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth() - 1;
        int h = getHeight() - 1;
        if (fill != null) {
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, w, h, arc, arc);
        }
        if (line != null) {
            g2.setColor(line);
            g2.drawRoundRect(0, 0, w, h, arc, arc);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
