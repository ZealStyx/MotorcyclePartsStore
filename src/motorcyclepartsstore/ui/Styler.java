package motorcyclepartsstore.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.text.JTextComponent;

/**
 * Applies the application theme to a form by component name, so the .form files only describe
 * structure and text while fonts and colors come from one place ({@link Theme}).
 *
 * Names understood: pageTitle, pageSubtitle, cardTitle, formTitle, muted, caption, message,
 * bigValue, brandTitle, brandSub, dark, field.
 */
public final class Styler {

    private Styler() {
    }

    public static void apply(Component c) {
        style(c);
        if (c instanceof Container) {
            for (Component k : ((Container) c).getComponents()) {
                apply(k);
            }
        }
    }

    private static void style(Component c) {
        String n = c.getName();
        if (n == null) {
            return;
        }
        if (c instanceof JLabel) {
            JLabel l = (JLabel) c;
            switch (n) {
                case "pageTitle": l.setFont(Theme.heading(36)); l.setForeground(Theme.INK); break;
                case "pageSubtitle": l.setFont(Theme.body(14)); l.setForeground(Theme.MUTED); break;
                case "cardTitle": l.setFont(Theme.heading(20)); l.setForeground(Theme.INK); break;
                case "formTitle": l.setFont(Theme.heading(40)); l.setForeground(Theme.INK); break;
                case "muted": l.setFont(Theme.body(13)); l.setForeground(Theme.MUTED); break;
                case "caption": l.setFont(Theme.bold(13)); l.setForeground(Theme.INK); break;
                case "message": l.setFont(Theme.body(13)); l.setForeground(Theme.ERROR); break;
                case "bigValue": l.setFont(Theme.heading(34)); l.setForeground(Theme.INK); break;
                case "brandTitle": l.setFont(Theme.heading(62)); l.setForeground(Color.WHITE); break;
                case "brandSub": l.setFont(Theme.body(18)); l.setForeground(new Color(0xCF, 0xCF, 0xD6)); break;
                case "brandName": l.setFont(Theme.heading(Theme.condensed() ? 20 : 17)); l.setForeground(Color.WHITE); break;
                case "sideRole": l.setFont(Theme.body(14)); l.setForeground(Theme.SIDEBAR_TEXT); break;
                case "matrixHead": l.setFont(Theme.bold(12)); l.setForeground(Theme.HEADER_TEXT); break;
                case "matrixCell": l.setFont(Theme.body(14)); l.setForeground(Theme.INK); break;
                default: break;
            }
        } else if (n.equals("dark") && c instanceof JPanel) {
            JPanel p = (JPanel) c;
            p.setOpaque(true);
            p.setBackground(Theme.SIDEBAR_BG);
        } else if (n.equals("field") && c instanceof JComponent) {
            Forms.size((JComponent) c);
            if (c instanceof JPasswordField) {
                ((JPasswordField) c).putClientProperty("JTextField.showRevealButton", true);
            }
        }
    }

    /** Placeholder text inside an empty text field. */
    public static void placeholder(JTextComponent field, String text) {
        field.putClientProperty("JTextField.placeholderText", text);
    }

    public static void size(JComboBox<?> box) {
        Forms.size(box);
    }
}
