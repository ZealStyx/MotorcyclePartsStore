/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package motorcyclepartsstore.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.JComponent;
import javax.swing.JPanel;
import motorcyclepartsstore.model.Role;

/**
 * Dark persistent sidebar: brand, role-specific navigation, current role and Log out.
 * Layout lives in Sidebar.form; the navigation items depend on the role, so they are added here.
 */
public class Sidebar extends javax.swing.JPanel {

    private final Map<Page, NavItem> items = new HashMap<>();

    /** Creates new form Sidebar */
    public Sidebar(Role role, String fullName, List<Page> pages, Consumer<Page> onSelect, Runnable onLogout) {
        initComponents();
        Styler.apply(this);
        sepLine.setBackground(Theme.SIDEBAR_LINE);
        avatar.setInitials(role.initials());
        sideRole.setText(role.getLabel());
        sideRole.setToolTipText(fullName);
        logoutLink.setColor(Theme.SIDEBAR_MUTED);
        logoutLink.setOnClick(onLogout);
        int row = 0;
        for (Page p : pages) {
            NavItem item = new NavItem(p, () -> onSelect.accept(p));
            items.put(p, item);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = row++;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            c.insets = new Insets(0, 0, 6, 0);
            navPanel.add(item, c);
        }
        GridBagConstraints filler = new GridBagConstraints();
        filler.gridy = row;
        filler.weighty = 1;
        JPanel f = new JPanel();
        f.setOpaque(false);
        navPanel.add(f, filler);
    }

    public void select(Page page) {
        for (Map.Entry<Page, NavItem> e : items.entrySet()) {
            e.getValue().setSelected(e.getKey() == page);
        }
    }

    /** One navigation entry, painted by hand (icon + label, accent when selected). */
    private static final class NavItem extends JComponent {

        private final Page page;
        private boolean selected;
        private boolean hover;

        NavItem(Page page, Runnable action) {
            this.page = page;
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(100, 44));
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        java.awt.GridBagConstraints gridBagConstraints;

        brandPanel = new javax.swing.JPanel();
        logoMark = new motorcyclepartsstore.ui.LogoMark();
        brandName = new javax.swing.JLabel();
        navPanel = new javax.swing.JPanel();
        profilePanel = new javax.swing.JPanel();
        sepLine = new javax.swing.JPanel();
        avatar = new motorcyclepartsstore.ui.Avatar();
        sideRole = new javax.swing.JLabel();
        logoutLink = new motorcyclepartsstore.ui.LinkLabel();

        logoMark.setPreferredSize(new java.awt.Dimension(34, 34));
        brandName.setName("brandName");
        brandName.setText("ONE PROMPT AWAY");
        brandPanel.setOpaque(false);
        brandPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 23, 20, 16));
        brandPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        brandPanel.add(logoMark, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        brandPanel.add(brandName, gridBagConstraints);
        navPanel.setOpaque(false);
        navPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 15, 0, 15));
        navPanel.setLayout(new java.awt.GridBagLayout());
        sepLine.setPreferredSize(new java.awt.Dimension(10, 1));
        avatar.setPreferredSize(new java.awt.Dimension(36, 36));
        sideRole.setName("sideRole");
        sideRole.setText("Role");
        logoutLink.setText("Log out");
        profilePanel.setOpaque(false);
        profilePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 18, 15));
        profilePanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        profilePanel.add(sepLine, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(14, 8, 0, 12);
        profilePanel.add(avatar, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 0);
        profilePanel.add(sideRole, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        profilePanel.add(logoutLink, gridBagConstraints);
        setName("dark");
        setPreferredSize(new java.awt.Dimension(228, 100));
        setLayout(new java.awt.BorderLayout());
        add(brandPanel, java.awt.BorderLayout.NORTH);
        add(navPanel, java.awt.BorderLayout.CENTER);
        add(profilePanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private motorcyclepartsstore.ui.Avatar avatar;
    private javax.swing.JLabel brandName;
    private javax.swing.JPanel brandPanel;
    private motorcyclepartsstore.ui.LogoMark logoMark;
    private motorcyclepartsstore.ui.LinkLabel logoutLink;
    private javax.swing.JPanel navPanel;
    private javax.swing.JPanel profilePanel;
    private javax.swing.JPanel sepLine;
    private javax.swing.JLabel sideRole;
    // End of variables declaration//GEN-END:variables
}
