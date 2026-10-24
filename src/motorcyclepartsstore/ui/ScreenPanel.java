package motorcyclepartsstore.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import motorcyclepartsstore.model.Role;
import motorcyclepartsstore.service.Session;

/** Base class of every signed-in page: title/subtitle header, role badge, actions and a scrolling body. */
public abstract class ScreenPanel extends JPanel {

    protected final Navigator nav;
    private final JPanel scrollBody = new ScrollBody();
    private final JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));

    protected ScreenPanel(Navigator nav, String title, String subtitle, boolean roleBadge, JComponent... actions) {
        super(new BorderLayout());
        this.nav = nav;
        setBackground(Theme.WORKSPACE_BG);

        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);
        head.setBorder(Forms.pad(28, Theme.PAGE_PAD, 14, Theme.PAGE_PAD));
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel t = Forms.title(title, 36);
        t.setAlignmentX(0f);
        JLabel s = Forms.muted(subtitle, 14);
        s.setAlignmentX(0f);
        text.add(t);
        text.add(s);
        head.add(text, BorderLayout.WEST);

        actionBar.setOpaque(false);
        if (roleBadge && Session.current() != null && Session.current().getRole() != null) {
            Role r = Session.current().getRole();
            actionBar.add(Chip.forRole(r));
        }
        for (JComponent a : actions) {
            actionBar.add(a);
        }
        JPanel east = new JPanel(new BorderLayout());
        east.setOpaque(false);
        east.add(actionBar, BorderLayout.NORTH);
        head.add(east, BorderLayout.EAST);
        add(head, BorderLayout.NORTH);

        scrollBody.setLayout(new BorderLayout());
        scrollBody.setOpaque(false);
        scrollBody.setBorder(Forms.pad(0, Theme.PAGE_PAD, 28, Theme.PAGE_PAD));
        JScrollPane sp = new JScrollPane(scrollBody, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(24);
        add(sp, BorderLayout.CENTER);
    }

    protected void setBody(JComponent body) {
        scrollBody.removeAll();
        scrollBody.add(body, BorderLayout.CENTER);
    }

    /** Called every time the page becomes visible; refresh data here. */
    public void onShow() {
    }

    /** Lays out {@code cards} in one row of equal width. */
    protected static JPanel row(int gap, JComponent... cards) {
        JPanel p = new JPanel(new java.awt.GridLayout(1, cards.length, gap, 0));
        p.setOpaque(false);
        for (JComponent c : cards) {
            p.add(c);
        }
        return p;
    }

    /** Two cards side by side with the given width ratio (left : right). */
    protected static JPanel split(JComponent left, JComponent right, double leftWeight) {
        JPanel p = new JPanel(new java.awt.GridBagLayout());
        p.setOpaque(false);
        java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
        c.fill = java.awt.GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = leftWeight;
        c.insets = new java.awt.Insets(0, 0, 0, Theme.GAP / 2);
        p.add(left, c);
        c.gridx = 1;
        c.weightx = 1 - leftWeight;
        c.insets = new java.awt.Insets(0, Theme.GAP / 2, 0, 0);
        p.add(right, c);
        return p;
    }

    /** Card with a heading and content. */
    protected static RoundedPanel titledCard(String title, JComponent content) {
        RoundedPanel card = RoundedPanel.card(new BorderLayout(0, 10));
        card.setBorder(Forms.pad(18, 20, 18, 20));
        card.add(Forms.title(title, 20), BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    /** Wraps content so it sits at the top of a taller card instead of stretching. */
    protected static JPanel top(JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(c, BorderLayout.NORTH);
        return p;
    }

    /** Vertical stack with a fixed gap; the last component takes remaining height. */
    protected static JPanel stack(int gap, JComponent first, JComponent second) {
        JPanel p = new JPanel(new BorderLayout(0, gap));
        p.setOpaque(false);
        p.add(first, BorderLayout.NORTH);
        p.add(second, BorderLayout.CENTER);
        return p;
    }

    /** Body that tracks the viewport width, and the height when the viewport is taller than needed. */
    private static final class ScrollBody extends JPanel implements Scrollable {

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle r, int orientation, int direction) {
            return 24;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle r, int orientation, int direction) {
            return Math.max(40, r.height - 60);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return getParent() instanceof JViewport && getParent().getHeight() > getPreferredSize().height;
        }
    }

    protected static int alignRight() {
        return SwingConstants.RIGHT;
    }
}
