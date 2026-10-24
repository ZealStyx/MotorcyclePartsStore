package motorcyclepartsstore.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** White stat card: icon tile + caption + large value, or caption + value + chip. */
public class SummaryCard extends RoundedPanel {

    private final JLabel value = new JLabel("-");

    /** Card with an icon tile (dashboards). */
    public SummaryCard(String title, Icons.Kind icon) {
        super(new BorderLayout(14, 0), Theme.CARD_ARC, Theme.CARD_BG, Theme.BORDER);
        setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        JComponent tile = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.ACCENT_TINT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.translate((getWidth() - 22) / 2, (getHeight() - 22) / 2);
                Icons.paint(g2, icon, 22, Theme.ACCENT);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(48, 48);
            }
        };
        JPanel tileHolder = new JPanel(new BorderLayout());
        tileHolder.setOpaque(false);
        tileHolder.add(tile, BorderLayout.CENTER);
        tileHolder.setPreferredSize(new Dimension(48, 48));
        JPanel holderWrap = new JPanel(new BorderLayout());
        holderWrap.setOpaque(false);
        holderWrap.add(tileHolder, BorderLayout.CENTER);
        add(holderWrap, BorderLayout.WEST);
        add(textBlock(title, null), BorderLayout.CENTER);
    }

    /** Card with a caption, value and a chip underneath (prediction output). */
    public SummaryCard(String title, Chip chip) {
        super(new BorderLayout(), Theme.CARD_ARC, Theme.CARD_BG, Theme.BORDER);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        add(textBlock(title, chip), BorderLayout.CENTER);
    }

    private JComponent textBlock(String title, Chip chip) {
        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        JLabel cap = new JLabel(title);
        cap.setFont(Theme.body(13));
        cap.setForeground(Theme.MUTED);
        cap.setAlignmentX(0f);
        value.setFont(Theme.heading(32));
        value.setForeground(Theme.INK);
        value.setAlignmentX(0f);
        box.add(cap);
        box.add(value);
        if (chip != null) {
            chip.setAlignmentX(0f);
            box.add(Box.createVerticalStrut(2));
            box.add(chip);
        }
        return box;
    }

    public void setValue(String text) {
        value.setText(text);
    }
}
