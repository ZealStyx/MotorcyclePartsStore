package motorcyclepartsstore.ui;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.UIManager;

/** Colors, fonts and spacing used across the application, plus Look & Feel setup. */
public final class Theme {

    public static final Color SIDEBAR_BG = new Color(0x17, 0x18, 0x1D);
    public static final Color SIDEBAR_TEXT = new Color(0xCF, 0xCF, 0xD6);
    public static final Color SIDEBAR_MUTED = new Color(0x8D, 0x8E, 0x97);
    public static final Color SIDEBAR_LINE = new Color(0x2A, 0x2B, 0x32);
    public static final Color AVATAR_BG = new Color(0x2C, 0x2D, 0x34);

    public static final Color WORKSPACE_BG = new Color(0xF6, 0xF3, 0xEE);
    public static final Color CARD_BG = Color.WHITE;
    public static final Color BORDER = new Color(0xEC, 0xE8, 0xE1);
    public static final Color FIELD_BORDER = new Color(0xD9, 0xD4, 0xCC);
    public static final Color GRID = new Color(0xEE, 0xEA, 0xE3);

    public static final Color ACCENT = new Color(0xC8, 0x40, 0x1B);
    public static final Color ACCENT_HOVER = new Color(0xB3, 0x37, 0x15);
    public static final Color ACCENT_PRESSED = new Color(0x9A, 0x2F, 0x10);
    public static final Color ACCENT_TINT = new Color(0xFB, 0xE3, 0xDA);
    public static final Color DANGER = new Color(0x8C, 0x2A, 0x0E);
    public static final Color NAVY = new Color(0x1F, 0x4A, 0x7C);
    public static final Color BEIGE = new Color(0xE7, 0xE1, 0xD8);

    public static final Color INK = new Color(0x1B, 0x1B, 0x1F);
    public static final Color MUTED = new Color(0x6B, 0x6B, 0x72);
    public static final Color HEADER_TEXT = new Color(0x55, 0x55, 0x5D);
    public static final Color ERROR = new Color(0xB0, 0x2A, 0x0E);
    public static final Color SUCCESS = new Color(0x2E, 0x6B, 0x3A);

    public static final Color GREEN_BG = new Color(0xDD, 0xEE, 0xDD);
    public static final Color GREEN_FG = new Color(0x2E, 0x6B, 0x3A);
    public static final Color BLUE_BG = new Color(0xDC, 0xE7, 0xF5);
    public static final Color BLUE_FG = new Color(0x2F, 0x5C, 0x93);
    public static final Color RED_BG = new Color(0xFB, 0xE0, 0xD6);
    public static final Color RED_FG = new Color(0xA6, 0x3A, 0x1C);
    public static final Color GREY_BG = new Color(0xEC, 0xE9, 0xE3);
    public static final Color GREY_FG = new Color(0x55, 0x55, 0x5D);

    /** Colors for donut charts. */
    public static final Color[] PALETTE = {ACCENT, NAVY, BEIGE, new Color(0xE0, 0x9F, 0x3E),
        new Color(0x4F, 0x8A, 0x8B), new Color(0x8A, 0x8D, 0x91)};

    public static final int SIDEBAR_WIDTH = 228;
    public static final int PAGE_PAD = 34;
    public static final int GAP = 16;
    public static final int CARD_ARC = 22;
    public static final int FIELD_HEIGHT = 40;

    private static final String BODY_FAMILY;
    private static final String HEADING_FAMILY;
    private static final boolean CONDENSED;

    static {
        Set<String> fams = new HashSet<>(Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        BODY_FAMILY = pick(fams, new String[]{"Barlow", "Segoe UI", "Helvetica Neue", "Inter", "Roboto", "Arial"},
                Font.SANS_SERIF);
        HEADING_FAMILY = pick(fams, new String[]{"Barlow Condensed", "Roboto Condensed", "Bahnschrift",
            "Arial Narrow"}, BODY_FAMILY);
        CONDENSED = !HEADING_FAMILY.equals(BODY_FAMILY);
    }

    private Theme() {
    }

    private static String pick(Set<String> installed, String[] candidates, String fallback) {
        for (String c : candidates) {
            if (installed.contains(c)) {
                return c;
            }
        }
        return fallback;
    }

    /** True when a condensed heading face is installed (headings can then be set larger). */
    public static boolean condensed() {
        return CONDENSED;
    }

    public static Font body(float size) {
        return new Font(BODY_FAMILY, Font.PLAIN, 12).deriveFont(size);
    }

    public static Font bold(float size) {
        return new Font(BODY_FAMILY, Font.BOLD, 12).deriveFont(size);
    }

    /** Condensed bold face used for titles and big numbers (falls back to the body font). */
    public static Font heading(float size) {
        return new Font(HEADING_FAMILY, Font.BOLD, 12).deriveFont(CONDENSED ? size : size * 0.78f);
    }

    /** Installs FlatLaf and the application-wide component defaults. Call before creating any UI. */
    public static void install() {
        FlatLightLaf.setup();
        UIManager.put("defaultFont", body(14));
        UIManager.put("Component.arc", 12);
        UIManager.put("Button.arc", 12);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("Component.borderColor", FIELD_BORDER);
        UIManager.put("Component.focusColor", ACCENT);
        UIManager.put("Component.focusedBorderColor", ACCENT);
        UIManager.put("Component.hoverBorderColor", new Color(0xBDB6AB));
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("PasswordField.background", Color.WHITE);
        UIManager.put("TextField.margin", new Insets(6, 12, 6, 12));
        UIManager.put("PasswordField.margin", new Insets(6, 12, 6, 12));
        UIManager.put("ComboBox.padding", new Insets(6, 10, 6, 6));
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.selectionBackground", ACCENT_TINT);
        UIManager.put("ComboBox.selectionForeground", INK);
        UIManager.put("List.selectionBackground", ACCENT_TINT);
        UIManager.put("List.selectionForeground", INK);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        UIManager.put("ScrollBar.width", 12);
        UIManager.put("ScrollBar.track", WORKSPACE_BG);
        UIManager.put("ScrollBar.thumb", new Color(0xCF, 0xC9, 0xBF));
        UIManager.put("Panel.background", WORKSPACE_BG);
    }
}
