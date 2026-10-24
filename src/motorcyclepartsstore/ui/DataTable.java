package motorcyclepartsstore.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Flat, borderless table in the style of the reference design. Meant to be placed in a card via
 * {@link #wrapper()} (header + rows, no inner scroll pane, so the page scrolls as a whole).
 */
public class DataTable extends JTable {

    private static final String SEP = " \u00B7 ";
    private static final int PAD = 8;

    private final Map<Integer, Boolean> chipCols = new HashMap<>();
    private final Map<Integer, String[]> actionCols = new HashMap<>();
    private final Map<Integer, Integer> aligns = new HashMap<>();
    private BiConsumer<Integer, Integer> actionHandler;

    /** Empty table for the form designer; call setModel(...) in user code. */
    public DataTable() {
        this(new ListModel());
    }

    public DataTable(TableModel model) {
        super(model);
        setRowHeight(42);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setFont(Theme.body(14));
        setForeground(Theme.INK);
        setBackground(Theme.CARD_BG);
        setRowSelectionAllowed(false);
        setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
        setFillsViewportHeight(false);
        JTableHeader h = getTableHeader();
        h.setReorderingAllowed(false);
        h.setResizingAllowed(false);
        h.setDefaultRenderer(new HeaderRenderer());
        h.setPreferredSize(new Dimension(100, 38));
        setDefaultRenderer(Object.class, new CellRenderer());
        setDefaultRenderer(Integer.class, new CellRenderer());
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(actionAt(e.getPoint()) != null ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int[] hit = actionAt(e.getPoint());
                if (hit != null && actionHandler != null) {
                    actionHandler.accept(hit[0], hit[1]);
                }
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    /** Uses the given model and attaches the column header above the table inside {@code host}. */
    public DataTable bind(ListModel model, javax.swing.JPanel host) {
        setModel(model);
        host.add(getTableHeader(), java.awt.BorderLayout.NORTH);
        return this;
    }

    /** Relative column widths. */
    public DataTable weights(int... w) {
        for (int i = 0; i < w.length && i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setPreferredWidth(w[i] * 10);
        }
        return this;
    }

    public DataTable chipColumn(int col) {
        chipCols.put(col, true);
        return this;
    }

    public DataTable align(int col, int alignment) {
        aligns.put(col, alignment);
        return this;
    }

    /** Text links in a column, e.g. {"Edit", "Delete"}; handler gets (modelRow, linkIndex). */
    public DataTable actionColumn(int col, String[] labels, BiConsumer<Integer, Integer> handler) {
        actionCols.put(col, labels);
        this.actionHandler = handler;
        return this;
    }

    /** Header on top, rows below, ready to drop into a card. */
    public JPanel wrapper() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(getTableHeader(), BorderLayout.NORTH);
        p.add(this, BorderLayout.CENTER);
        return p;
    }

    /** Returns {row, linkIndex} if the point is over an action link, else null. */
    private int[] actionAt(Point p) {
        int row = rowAtPoint(p);
        int viewCol = columnAtPoint(p);
        if (row < 0 || viewCol < 0) {
            return null;
        }
        String[] labels = actionCols.get(convertColumnIndexToModel(viewCol));
        if (labels == null) {
            return null;
        }
        FontMetrics fm = getFontMetrics(getFont());
        int x = getCellRect(row, viewCol, false).x + PAD;
        for (int i = 0; i < labels.length; i++) {
            int w = fm.stringWidth(labels[i]);
            if (p.x >= x && p.x <= x + w) {
                return new int[]{row, i};
            }
            x += w + fm.stringWidth(SEP);
        }
        return null;
    }

    private static String actionText(String[] labels) {
        return String.join(SEP, labels);
    }

    private class CellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
            int mc = convertColumnIndexToModel(col);
            if (chipCols.containsKey(mc)) {
                JPanel p = new JPanel(new java.awt.GridBagLayout());
                p.setBackground(Theme.CARD_BG);
                p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));
                String s = String.valueOf(v);
                java.awt.GridBagConstraints gc = new java.awt.GridBagConstraints();
                gc.anchor = java.awt.GridBagConstraints.WEST;
                gc.weightx = 1;
                gc.insets = new java.awt.Insets(0, PAD, 0, 0);
                p.add(new Chip(s, Chip.forStatus(s)), gc);
                return p;
            }
            String[] actions = actionCols.get(mc);
            super.getTableCellRendererComponent(t, actions != null ? actionText(actions) : v, false, false, row, col);
            setFont(getFont());
            setForeground(Theme.INK);
            setBackground(Theme.CARD_BG);
            setOpaque(true);
            setHorizontalAlignment(aligns.getOrDefault(mc, SwingConstants.LEFT));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                    BorderFactory.createEmptyBorder(0, PAD, 0, PAD)));
            return this;
        }
    }

    private class HeaderRenderer implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
            JLabel l = new JLabel(v == null ? "" : v.toString());
            l.setFont(Theme.bold(13));
            l.setForeground(Theme.HEADER_TEXT);
            l.setOpaque(true);
            l.setBackground(Theme.CARD_BG);
            l.setHorizontalAlignment(aligns.getOrDefault(convertColumnIndexToModel(col), SwingConstants.LEFT));
            l.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                    BorderFactory.createEmptyBorder(0, PAD, 0, PAD)));
            return l;
        }
    }

    /** Simple read-only list-backed model; replace contents with {@link #setRows}. */
    public static class ListModel extends AbstractTableModel {

        private final String[] columns;
        private java.util.List<Object[]> rows = new java.util.ArrayList<>();

        public ListModel(String... columns) {
            this.columns = columns;
        }

        public void setRows(java.util.List<Object[]> rows) {
            this.rows = rows;
            fireTableDataChanged();
        }

        public void setColumnName(int c, String name) {
            columns[c] = name;
            fireTableStructureChanged();
        }

        public Object[] row(int i) {
            return rows.get(i);
        }

        @Override
        public int getRowCount() {
            return rows.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int c) {
            return columns[c];
        }

        @Override
        public Object getValueAt(int r, int c) {
            return rows.get(r)[c];
        }
    }
}
