package motorcyclepartsstore.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;

/** Rounded, undecorated modal dialog that dims the window behind it, as in the reference design. */
public class ModalDialog extends JDialog {

    protected final RoundedPanel card;
    private final int width;
    private Component previousGlass;
    private JComponent dim;

    public ModalDialog(Window owner, String title, int width, boolean showClose) {
        super(owner, ModalityType.APPLICATION_MODAL);
        this.width = width;
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(false);
        root.setBorder(Forms.pad(12, 12, 12, 12));
        card = new RoundedPanel(new BorderLayout(0, 14), 28, Color.WHITE, Theme.BORDER);
        card.setBorder(Forms.pad(22, 24, 20, 24));
        root.add(card, BorderLayout.CENTER);
        setContentPane(root);

        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);
        head.add(Forms.title(title, 26), BorderLayout.WEST);
        if (showClose) {
            JLabel close = Forms.link("Close", Theme.MUTED, this::dispose);
            head.add(close, BorderLayout.EAST);
        }
        card.add(head, BorderLayout.NORTH);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ESCAPE"), "close");
        getRootPane().getActionMap().put("close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    protected void setBody(JComponent c) {
        card.add(c, BorderLayout.CENTER);
    }

    protected void setFooter(JComponent c) {
        card.add(c, BorderLayout.SOUTH);
    }

    /** Shows the dialog (blocks until it is closed). */
    public void open() {
        pack();
        setSize(new Dimension(width + 24, getHeight()));
        setLocationRelativeTo(getOwner());
        Window o = getOwner();
        if (o instanceof RootPaneContainer) {
            RootPaneContainer rpc = (RootPaneContainer) o;
            previousGlass = rpc.getGlassPane();
            dim = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    g.setColor(new Color(20, 20, 26, 110));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            ((JPanel) dim).setOpaque(false);
            rpc.setGlassPane(dim);
            dim.setVisible(true);
        }
        try {
            setVisible(true);
        } finally {
            if (dim != null) {
                dim.setVisible(false);
                ((RootPaneContainer) o).setGlassPane(previousGlass);
            }
        }
    }
}
