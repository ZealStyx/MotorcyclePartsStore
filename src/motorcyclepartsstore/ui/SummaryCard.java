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

/**
 * White stat card: optional icon tile, caption, large value and optional chip.
 * A bean: set {@code title} in the form designer, the rest in user code.
 */
public class SummaryCard extends RoundedPanel {

    private final JLabel caption = new JLabel("Title");
    private final JLabel value = new JLabel("-");
    private final Chip chip = new Chip("", Chip.Kind.BLUE);
    private final JPanel tileHolder = new JPanel(new BorderLayout());
    private Icons.Kind iconKind;

    public SummaryCard() {
        super(new BorderLayout(14, 0), Theme.CARD_ARC, Theme.CARD_BG, Theme.BORDER);
        setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        JComponent tile = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                if (iconKind == null) {
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.ACCENT_TINT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.translate((getWidth() - 22) / 2, (getHeight() - 22) / 2);
                Icons.paint(g2, iconKind, 22, Theme.ACCENT);
                g2.dispose();
            }
        };
        tileHolder.setOpaque(false);
        tileHolder.setPreferredSize(new Dimension(48, 48));
        tileHolder.add(tile, BorderLayout.CENTER);
        tileHolder.setVisible(false);
        JPanel holderWrap = new JPanel(new BorderLayout());
        holderWrap.setOpaque(false);
        holderWrap.add(tileHolder, BorderLayout.NORTH);
        add(holderWrap, BorderLayout.WEST);

        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        caption.setFont(Theme.body(13));
        caption.setForeground(Theme.MUTED);
        caption.setAlignmentX(0f);
        value.setFont(Theme.heading(32));
        value.setForeground(Theme.INK);
        value.setAlignmentX(0f);
        chip.setAlignmentX(0f);
        chip.setVisible(false);
        box.add(caption);
        box.add(value);
        box.add(Box.createVerticalStrut(2));
        box.add(chip);
        add(box, BorderLayout.CENTER);
    }

    public String getTitle() {
        return caption.getText();
    }

    public void setTitle(String title) {
        caption.setText(title);
    }

    public void setValue(String text) {
        value.setText(text);
    }

    public String getValue() {
        return value.getText();
    }

    /** Shows an icon tile on the left (null hides it). */
    public void setIconKind(Icons.Kind kind) {
        this.iconKind = kind;
        tileHolder.setVisible(kind != null);
        repaint();
    }

    /** Text of the chip under the value; empty hides the chip. */
    public void setChipText(String text) {
        chip.setText(text == null ? "" : text);
        chip.setVisible(text != null && !text.isEmpty());
    }

    public void setChipKind(Chip.Kind kind) {
        chip.setKind(kind);
    }
}
