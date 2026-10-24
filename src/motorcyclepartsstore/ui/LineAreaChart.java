package motorcyclepartsstore.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import motorcyclepartsstore.service.PredictionService.Series;

/** Actual (solid, filled) vs predicted (dashed) line chart drawn with Java2D. */
public class LineAreaChart extends JComponent {

    private Series series;
    private String message = "Training demo models...";

    public LineAreaChart() {
        setOpaque(false);
    }

    public void setSeries(Series s) {
        this.series = s;
        repaint();
    }

    public void setMessage(String m) {
        this.message = m;
        this.series = null;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 260);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        if (series == null || series.actual.length < 2) {
            g2.setFont(Theme.body(14));
            g2.setColor(Theme.MUTED);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(message, (w - fm.stringWidth(message)) / 2, h / 2);
            g2.dispose();
            return;
        }
        int left = 24, right = 24, top = 30, bottom = 30;
        double max = 1;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < series.actual.length; i++) {
            max = Math.max(max, Math.max(series.actual[i], series.predicted[i]));
            min = Math.min(min, Math.min(series.actual[i], series.predicted[i]));
        }
        max *= 1.08;
        min = Math.max(0, min * 0.75); // baseline below the lowest point so the trend is visible
        double span = Math.max(1, max - min);
        int n = series.actual.length;
        int cw = Math.max(10, w - left - right);
        int ch = Math.max(10, h - top - bottom);
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(Theme.GRID);
        for (int i = 0; i <= 3; i++) {
            int y = top + ch * i / 3;
            g2.drawLine(left, y, left + cw, y);
        }
        double[] xs = new double[n];
        double[] ya = new double[n];
        double[] yp = new double[n];
        for (int i = 0; i < n; i++) {
            xs[i] = left + cw * (double) i / (n - 1);
            ya[i] = top + ch - ch * (series.actual[i] - min) / span;
            yp[i] = top + ch - ch * (series.predicted[i] - min) / span;
        }
        Path2D area = new Path2D.Double();
        area.moveTo(xs[0], top + ch);
        for (int i = 0; i < n; i++) {
            area.lineTo(xs[i], ya[i]);
        }
        area.lineTo(xs[n - 1], top + ch);
        area.closePath();
        g2.setColor(new Color(Theme.ACCENT.getRed(), Theme.ACCENT.getGreen(), Theme.ACCENT.getBlue(), 32));
        g2.fill(area);
        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(Theme.ACCENT);
        g2.draw(line(xs, ya));
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10f, new float[]{6f, 5f}, 0f));
        g2.setColor(Theme.NAVY);
        g2.draw(line(xs, yp));
        // x labels
        g2.setFont(Theme.body(11));
        g2.setColor(Theme.MUTED);
        FontMetrics fm = g2.getFontMetrics();
        int every = Math.max(1, (int) Math.ceil(n * (fm.stringWidth("Mmm 00") + 14.0) / cw));
        for (int i = 0; i < n; i += every) {
            String s = series.labels[i];
            g2.drawString(s, (int) (xs[i] - fm.stringWidth(s) / 2.0), top + ch + 18);
        }
        // legend
        int lx = left;
        g2.setStroke(new BasicStroke(3f));
        g2.setColor(Theme.ACCENT);
        g2.drawLine(lx, 12, lx + 16, 12);
        g2.setColor(Theme.MUTED);
        g2.drawString("Actual", lx + 22, 16);
        int lx2 = lx + 22 + fm.stringWidth("Actual") + 18;
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10f, new float[]{5f, 4f}, 0f));
        g2.setColor(Theme.NAVY);
        g2.drawLine(lx2, 12, lx2 + 16, 12);
        g2.setColor(Theme.MUTED);
        g2.drawString("Predicted", lx2 + 22, 16);
        g2.dispose();
    }

    private static Path2D line(double[] xs, double[] ys) {
        Path2D p = new Path2D.Double();
        p.moveTo(xs[0], ys[0]);
        for (int i = 1; i < xs.length; i++) {
            p.lineTo(xs[i], ys[i]);
        }
        return p;
    }
}
