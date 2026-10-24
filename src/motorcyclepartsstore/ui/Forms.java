package motorcyclepartsstore.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/** Factory methods that give inputs a consistent look. */
public final class Forms {

    private Forms() {
    }

    public static JTextField text(String placeholder) {
        JTextField f = new JTextField();
        f.putClientProperty("JTextField.placeholderText", placeholder);
        size(f);
        return f;
    }

    public static JPasswordField password() {
        JPasswordField f = new JPasswordField();
        f.putClientProperty("JTextField.showRevealButton", true);
        size(f);
        return f;
    }

    public static <T> JComboBox<T> combo(T[] items) {
        JComboBox<T> c = new JComboBox<>(items);
        size(c);
        return c;
    }

    public static void size(JComponent c) {
        c.setFont(Theme.body(14));
        c.setPreferredSize(new Dimension(180, Theme.FIELD_HEIGHT));
        c.setMinimumSize(new Dimension(60, Theme.FIELD_HEIGHT));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, Theme.FIELD_HEIGHT));
    }

    /** Bold caption above a field. */
    public static JPanel labeled(String caption, JComponent field) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel l = new JLabel(caption);
        l.setFont(Theme.bold(13));
        l.setForeground(Theme.INK);
        l.setAlignmentX(0f);
        field.setAlignmentX(0f);
        p.add(l);
        p.add(Box.createVerticalStrut(6));
        p.add(field);
        return p;
    }

    public static JLabel message() {
        JLabel l = new JLabel(" ");
        l.setFont(Theme.body(13));
        l.setForeground(Theme.ERROR);
        return l;
    }

    private static String wrap(String text) {
        String esc = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        return "<html><body style='width:340px'>" + esc + "</body></html>";
    }

    public static void error(JLabel l, String text) {
        l.setForeground(Theme.ERROR);
        l.setText(wrap(text));
    }

    public static void success(JLabel l, String text) {
        l.setForeground(Theme.SUCCESS);
        l.setText(wrap(text));
    }

    /** Text that behaves like a link (hand cursor, underline on hover). */
    public static JLabel link(String text, Color color, Runnable action) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.body(13));
        l.setForeground(color);
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        l.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                l.setText("<html><u>" + text + "</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                l.setText(text);
            }
        });
        return l;
    }

    public static JLabel title(String text, float size) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.heading(size));
        l.setForeground(Theme.INK);
        return l;
    }

    public static JLabel muted(String text, float size) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.body(size));
        l.setForeground(Theme.MUTED);
        return l;
    }

    public static javax.swing.border.Border pad(int t, int l, int b, int r) {
        return BorderFactory.createEmptyBorder(t, l, b, r);
    }
}
