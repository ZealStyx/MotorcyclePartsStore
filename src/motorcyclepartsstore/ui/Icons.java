package motorcyclepartsstore.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;

/** Simple line icons drawn with Java2D, so no image resources are needed. */
public final class Icons {

    public enum Kind { GRID, TREND, USER, BOX, CART, ALERT, BARS, PLUS, LOGO }

    private Icons() {
    }

    public static Icon of(Kind kind, int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.translate(x, y);
                paint(g2, kind, size, color);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /** Draws the icon inside a size x size box at the origin. */
    public static void paint(Graphics2D g2, Kind kind, int size, Color color) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        double s = size / 24.0;
        g2.scale(s, s);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        switch (kind) {
            case GRID:
                g2.draw(new RoundRectangle2D.Double(3.5, 3.5, 7, 7, 2, 2));
                g2.draw(new RoundRectangle2D.Double(13.5, 3.5, 7, 4, 2, 2));
                g2.draw(new RoundRectangle2D.Double(13.5, 10.5, 7, 10, 2, 2));
                g2.draw(new RoundRectangle2D.Double(3.5, 13.5, 7, 7, 2, 2));
                break;
            case TREND: {
                Path2D p = new Path2D.Double();
                p.moveTo(3.5, 4);
                p.lineTo(3.5, 20.5);
                p.lineTo(21, 20.5);
                g2.draw(p);
                Path2D l = new Path2D.Double();
                l.moveTo(7, 15.5);
                l.lineTo(11, 11);
                l.lineTo(14, 13.5);
                l.lineTo(19.5, 7.5);
                g2.draw(l);
                break;
            }
            case USER:
                g2.draw(new Ellipse2D.Double(8.5, 3.5, 7, 7));
                g2.draw(new java.awt.geom.Arc2D.Double(5, 13, 14, 14, 20, 140, java.awt.geom.Arc2D.OPEN));
                break;
            case BOX: {
                Path2D p = new Path2D.Double();
                p.moveTo(12, 3);
                p.lineTo(20.5, 7.5);
                p.lineTo(20.5, 16.5);
                p.lineTo(12, 21);
                p.lineTo(3.5, 16.5);
                p.lineTo(3.5, 7.5);
                p.closePath();
                g2.draw(p);
                Path2D q = new Path2D.Double();
                q.moveTo(3.5, 7.5);
                q.lineTo(12, 12);
                q.lineTo(20.5, 7.5);
                q.moveTo(12, 12);
                q.lineTo(12, 21);
                g2.draw(q);
                break;
            }
            case CART: {
                Path2D p = new Path2D.Double();
                p.moveTo(2.5, 4.5);
                p.lineTo(5, 4.5);
                p.lineTo(7, 15);
                p.lineTo(18, 15);
                p.lineTo(20, 8);
                p.lineTo(5.6, 8);
                g2.draw(p);
                g2.fill(new Ellipse2D.Double(8, 17.8, 2.6, 2.6));
                g2.fill(new Ellipse2D.Double(15, 17.8, 2.6, 2.6));
                break;
            }
            case ALERT: {
                Path2D p = new Path2D.Double();
                p.moveTo(12, 3.5);
                p.lineTo(21.5, 20);
                p.lineTo(2.5, 20);
                p.closePath();
                g2.draw(p);
                g2.drawLine(12, 9, 12, 14);
                g2.fill(new Ellipse2D.Double(11.1, 15.9, 1.8, 1.8));
                break;
            }
            case BARS:
                g2.draw(new RoundRectangle2D.Double(3.5, 3.5, 17, 17, 3, 3));
                g2.drawLine(8, 17, 8, 12);
                g2.drawLine(12, 17, 12, 8);
                g2.drawLine(16, 17, 16, 14);
                break;
            case PLUS:
                g2.drawLine(12, 6, 12, 18);
                g2.drawLine(6, 12, 18, 12);
                break;
            case LOGO:
                g2.draw(new Ellipse2D.Double(3.5, 12.5, 6, 6));
                g2.draw(new Ellipse2D.Double(14.5, 12.5, 6, 6));
                Path2D p = new Path2D.Double();
                p.moveTo(6.5, 12.5);
                p.lineTo(10, 6);
                p.lineTo(14, 6);
                p.lineTo(17.5, 12.5);
                g2.draw(p);
                break;
            default:
                break;
        }
    }
}
