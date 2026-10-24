package motorcyclepartsstore.ui;

import java.awt.FlowLayout;
import java.awt.Window;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Small message and confirmation dialogs built on {@link ModalDialog}. */
public final class Dialogs {

    private Dialogs() {
    }

    private static JLabel text(String s, int width) {
        String esc = s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>");
        JLabel l = new JLabel("<html><body style='width:" + width + "px'>" + esc + "</body></html>");
        l.setFont(Theme.body(14));
        l.setForeground(Theme.MUTED);
        return l;
    }

    public static void message(Window owner, String title, String message) {
        ModalDialog d = new ModalDialog(owner, title, 420, false);
        d.setBody(text(message, 360));
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        foot.setOpaque(false);
        RoundButton ok = RoundButton.primary("OK");
        ok.addActionListener(e -> d.dispose());
        foot.add(ok);
        d.setFooter(foot);
        d.getRootPane().setDefaultButton(ok);
        d.open();
    }

    /** Returns true when the confirm button was chosen. */
    public static boolean confirm(Window owner, String title, String message, String cancelLabel,
            String confirmLabel, boolean danger) {
        boolean[] result = {false};
        ModalDialog d = new ModalDialog(owner, title, 440, false);
        d.setBody(text(message, 380));
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        foot.setOpaque(false);
        RoundButton cancel = RoundButton.secondary(cancelLabel);
        cancel.addActionListener(e -> d.dispose());
        RoundButton ok = new RoundButton(confirmLabel, danger ? RoundButton.Kind.DANGER : RoundButton.Kind.PRIMARY);
        ok.addActionListener(e -> {
            result[0] = true;
            d.dispose();
        });
        foot.add(cancel);
        foot.add(ok);
        d.setFooter(foot);
        d.open();
        return result[0];
    }
}
