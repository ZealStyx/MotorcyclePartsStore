package motorcyclepartsstore.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/** Rounded accent square with the logo icon (bean for the form designer). */
public class LogoMark extends JComponent {

    public LogoMark() {
        setPreferredSize(new Dimension(34, 34));
        setMinimumSize(new Dimension(34, 34));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int size = Math.min(getWidth(), getHeight());
        g2.setColor(Theme.ACCENT);
        g2.fillRoundRect(0, 0, size, size, size / 3, size / 3);
        int ic = size * 6 / 10;
        g2.translate((size - ic) / 2, (size - ic) / 2);
        Icons.paint(g2, Icons.Kind.LOGO, ic, Color.WHITE);
        g2.dispose();
    }
}
