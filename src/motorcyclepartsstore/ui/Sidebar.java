package motorcyclepartsstore.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import motorcyclepartsstore.model.Role;

/** Dark persistent sidebar: brand, role-specific navigation, current role and Log out. */
public class Sidebar extends JPanel {

    private final Map<Page, NavItem> items = new HashMap<>();

    public Sidebar(Role role, String fullName, List<Page> pages, Consumer<Page> onSelect, Runnable onLogout) {
        super(new BorderLayout());
        setBackground(Theme.SIDEBAR_BG);
        setPreferredSize(new Dimension(Theme.SIDEBAR_WIDTH, 100));

        add(brand(), BorderLayout.NORTH);

        JPanel nav = new JPanel();
        nav.setOpaque(false);
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(Forms.pad(6, 15, 0, 15));
        for (Page p : pages) {
            NavItem item = new NavItem(p, () -> onSelect.accept(p));
            items.put(p, item);
            nav.add(item);
            nav.add(Box.createVerticalStrut(6));
        }
        add(nav, BorderLayout.CENTER);
        add(profile(role, fullName, onLogout), BorderLayout.SOUTH);
    }

    private JComponent brand() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);
        p.setBorder(Forms.pad(24, 23, 20, 16));
        p.add(logoMark(34), BorderLayout.WEST);
        JLabel name = new JLabel("ONE PROMPT AWAY");
        name.setFont(Theme.heading(20));
        name.setForeground(Color.WHITE);
        p.add(name, BorderLayout.CENTER);
        return p;
    }

    /** Rounded accent square with the logo icon. */
    public static JComponent logoMark(int size) {
        return new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.ACCENT);
                g2.fillRoundRect(0, 0, size, size, size / 3, size / 3);
                int ic = size * 6 / 10;
                g2.translate((size - ic) / 2, (size - ic) / 2);
                Icons.paint(g2, Icons.Kind.LOGO, ic, Color.WHITE);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(size, size);
            }
        };
    }

    private JComponent profile(Role role, String fullName, Runnable onLogout) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(Forms.pad(0, 15, 18, 15));
        JPanel line = new JPanel();
        line.setBackground(Theme.SIDEBAR_LINE);
        line.setPreferredSize(new Dimension(10, 1));
        wrap.add(line, BorderLayout.NORTH);

        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(Forms.pad(14, 8, 0, 0));
        JComponent avatar = new JComponent() {
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
                String s = role.initials();
                g2.drawString(s, (getWidth() - fm.stringWidth(s)) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(36, 36);
            }
        };
        JPanel av = new JPanel(new BorderLayout());
        av.setOpaque(false);
        av.add(avatar, BorderLayout.NORTH);
        row.add(av, BorderLayout.WEST);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(role.getLabel());
        name.setFont(Theme.body(14));
        name.setForeground(Theme.SIDEBAR_TEXT);
        name.setToolTipText(fullName);
        JLabel out = Forms.link("Log out", Theme.SIDEBAR_MUTED, onLogout);
        name.setAlignmentX(0f);
        out.setAlignmentX(0f);
        text.add(name);
        text.add(out);
        row.add(text, BorderLayout.CENTER);
        wrap.add(row, BorderLayout.CENTER);
        return wrap;
    }

    public void select(Page page) {
        for (Map.Entry<Page, NavItem> e : items.entrySet()) {
            e.getValue().setSelected(e.getKey() == page);
        }
    }

    private static final class NavItem extends JComponent {

        private final Page page;
        private boolean selected;
        private boolean hover;

        NavItem(Page page, Runnable action) {
            this.page = page;
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            setPreferredSize(new Dimension(100, 44));
            setAlignmentX(0f);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    action.run();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        void setSelected(boolean s) {
            selected = s;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            if (selected) {
                g2.setColor(Theme.ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            } else if (hover) {
                g2.setColor(new Color(0x23, 0x24, 0x2B));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }
            Color fg = selected ? Color.WHITE : Theme.SIDEBAR_TEXT;
            g2.translate(14, (getHeight() - 20) / 2);
            Icons.paint(g2, page.icon, 20, fg);
            g2.translate(-14, -(getHeight() - 20) / 2);
            g2.setFont(Theme.body(14));
            g2.setColor(fg);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(page.label, 46, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }
}
