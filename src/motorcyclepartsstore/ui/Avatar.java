package motorcyclepartsstore.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/** Round avatar with initials (bean for the form designer). */
public class Avatar extends JComponent {

    private String initials = "AD";

    public Avatar() {
        setPreferredSize(new Dimension(36, 36));
        setMinimumSize(new Dimension(36, 36));
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Theme.AVATAR_BG);
        g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
        g2.setColor(Color.WHITE);
        g2.setFont(Theme.bold(13));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(initials, (getWidth() - fm.stringWidth(initials)) / 2,
                (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
        g2.dispose();
    }
}
