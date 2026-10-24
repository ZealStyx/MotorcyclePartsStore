package motorcyclepartsstore.screens;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.awt.print.PrinterException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import motorcyclepartsstore.ui.Dialogs;
import motorcyclepartsstore.ui.ModalDialog;
import motorcyclepartsstore.ui.RoundButton;
import motorcyclepartsstore.ui.Theme;

/** Receipt preview with text export and system print (printing itself is not claimed as successful). */
public class ReceiptDialog extends ModalDialog {

    private final JTextArea area = new JTextArea();

    public ReceiptDialog(Window owner, String title, String receiptText, String suggestedName) {
        super(owner, title, 440, true);
        area.setText(receiptText);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setBackground(Theme.WORKSPACE_BG);
        area.setBorder(motorcyclepartsstore.ui.Forms.pad(12, 14, 12, 14));
        area.setCaretPosition(0);
        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(javax.swing.BorderFactory.createLineBorder(Theme.BORDER));
        sp.setPreferredSize(new java.awt.Dimension(400, 330));
        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.add(sp, BorderLayout.CENTER);
        setBody(body);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        foot.setOpaque(false);
        RoundButton save = RoundButton.secondary("Save as text");
        save.addActionListener(e -> saveText(owner, suggestedName));
        RoundButton print = RoundButton.secondary("Print");
        print.addActionListener(e -> print(owner));
        RoundButton close = RoundButton.primary("Close");
        close.addActionListener(e -> dispose());
        foot.add(save);
        foot.add(print);
        foot.add(close);
        setFooter(foot);
    }

    private void saveText(Window owner, String name) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(name + ".txt"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            Files.write(fc.getSelectedFile().toPath(), area.getText().getBytes(StandardCharsets.UTF_8));
            Dialogs.message(owner, "Receipt saved", "Saved to " + fc.getSelectedFile().getAbsolutePath());
        } catch (IOException ex) {
            Dialogs.message(owner, "Save failed", ex.getMessage());
        }
    }

    private void print(Window owner) {
        try {
            if (area.print()) {
                Dialogs.message(owner, "Print", "The print job was sent to the system printer.");
            }
        } catch (PrinterException | java.awt.HeadlessException ex) {
            Dialogs.message(owner, "Print failed", String.valueOf(ex.getMessage()));
        }
    }
}
