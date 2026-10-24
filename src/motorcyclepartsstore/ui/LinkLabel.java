package motorcyclepartsstore.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

/** Label that behaves like a link: hand cursor, underline on hover, optional click action. */
public class LinkLabel extends JLabel {

    private String plain = "Link";
    private Runnable onClick;

    public LinkLabel() {
        super("Link");
        setFont(Theme.body(13));
        setForeground(Theme.MUTED);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                LinkLabel.super.setText("<html><u>" + plain + "</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                LinkLabel.super.setText(plain);
            }
        });
    }

    @Override
    public void setText(String text) {
        this.plain = text == null ? "" : text;
        super.setText(plain);
    }

    public void setOnClick(Runnable r) {
        this.onClick = r;
    }

    public void setColor(Color c) {
        setForeground(c);
    }
}
