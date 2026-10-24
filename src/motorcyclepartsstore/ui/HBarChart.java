package motorcyclepartsstore.ui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;

/** Horizontal bar chart (label above each rounded bar, value at the end). */
public class HBarChart extends JComponent {

    private Map<String, Integer> data = new LinkedHashMap<>();
    private String unit = "";
    private String emptyText = "No data yet";

    public HBarChart() {
        setOpaque(false);
    }

    public void setData(Map<String, Integer> data, String unit) {
        this.data = data;
        this.unit = unit;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(240, 220);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int w = getWidth();
        g2.setFont(Theme.body(12));
        FontMetrics fm = g2.getFontMetrics();
        if (data.isEmpty()) {
            g2.setColor(Theme.MUTED);
            g2.drawString(emptyText, 4, 20);
            g2.dispose();
            return;
        }
        int max = 1;
        for (int v : data.values()) {
            max = Math.max(max, v);
        }
        int rowH = 52;
        int y = 4;
        for (Map.Entry<String, Integer> e : data.entrySet()) {
            g2.setColor(Theme.MUTED);
            g2.drawString(e.getKey(), 4, y + fm.getAscent());
            String val = e.getValue() + " " + unit;
            g2.setColor(Theme.INK);
            g2.drawString(val, w - fm.stringWidth(val) - 4, y + fm.getAscent());
            int barMax = w - 8;
            int bw = Math.max(14, (int) (barMax * e.getValue() / (double) max));
            g2.setColor(Theme.ACCENT);
            g2.fillRoundRect(4, y + fm.getHeight() + 2, bw, 20, 8, 8);
            y += rowH;
        }
        g2.dispose();
    }
}
