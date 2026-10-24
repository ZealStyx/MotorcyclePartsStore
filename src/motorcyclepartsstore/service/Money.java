package motorcyclepartsstore.service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/** Philippine peso and number formatting shared by the UI and receipts. */
public final class Money {

    public static final String PESO = "\u20B1";

    private Money() {
    }

    private static DecimalFormat fmt(String pattern) {
        return new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(Locale.US));
    }

    /** e.g. "₱1,234.50" */
    public static String peso(double v) {
        return PESO + fmt("#,##0.00").format(v);
    }

    /** e.g. "1,234.50" */
    public static String plain(double v) {
        return fmt("#,##0.00").format(v);
    }

    public static String number(long v) {
        return fmt("#,##0").format(v);
    }
}
