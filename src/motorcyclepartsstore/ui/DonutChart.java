package motorcyclepartsstore.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;

/** Donut chart with a legend underneath. */
public class DonutChart extends JComponent {

    private String[] labels = new String[0];
    private double[] values = new double[0];

    public DonutChart() {
        setOpaque(false);
    }

    public void setData(String[] labels, double[] values) {
        this.labels = labels;
        this.values = values;
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(240, 300 + (labels.length / 2) * 22);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        double total = 0;
        for (double v : values) {
            total += v;
        }
        int legendRows = (labels.length + 1) / 2;
        int legendH = legendRows * 22 + 8;
        int d = Math.max(60, Math.min(getWidth() - 24, getHeight() - legendH - 16));
        d = Math.min(d, 190);
        int cx = (getWidth() - d) / 2;
        int cy = 6;
        if (total <= 0) {
            g2.setColor(Theme.MUTED);
            g2.drawString("No data yet", 8, 24);
            g2.dispose();
            return;
        }
        double start = 90;
        for (int i = 0; i < values.length; i++) {
            double extent = -360.0 * values[i] / total;
            g2.setColor(Theme.PALETTE[i % Theme.PALETTE.length]);
            g2.fill(new Arc2D.Double(cx, cy, d, d, start, extent, Arc2D.PIE));
            start += extent;
        }
        int hole = (int) (d * 0.56);
        Area ring = new Area(new Ellipse2D.Double(cx, cy, d, d));
        ring.subtract(new Area(new Ellipse2D.Double(cx + (d - hole) / 2.0, cy + (d - hole) / 2.0, hole, hole)));
        // punch the hole using the card background so the donut looks hollow
        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Double(cx + (d - hole) / 2.0, cy + (d - hole) / 2.0, hole, hole));
        g2.setFont(Theme.body(12));
        FontMetrics fm = g2.getFontMetrics();
        int colW = Math.max(100, getWidth() / 2);
        int ly = cy + d + 22;
        for (int i = 0; i < labels.length; i++) {
            int x = 8 + (i % 2) * colW;
            int y = ly + (i / 2) * 22;
            g2.setColor(Theme.PALETTE[i % Theme.PALETTE.length]);
            g2.fillRoundRect(x, y - 9, 10, 10, 4, 4);
            g2.setColor(Theme.MUTED);
            String s = labels[i] + " " + Math.round(100 * values[i] / total) + "%";
            g2.drawString(s, x + 16, y);
        }
        g2.dispose();
    }
}
