package motorcyclepartsstore.screens;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import motorcyclepartsstore.model.DatasetRow;
import motorcyclepartsstore.service.Money;
import motorcyclepartsstore.ui.DataTable;

/** Formatting helpers shared by the Instructor pages. */
final class PredictionPanelSupport {

    private PredictionPanelSupport() {
    }

    static void fillPreview(DataTable.ListModel model, List<DatasetRow> data) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        List<Object[]> rows = new ArrayList<>();
        for (DatasetRow r : data) {
            rows.add(new Object[]{r.date.format(f), r.product, r.category, r.qtySold,
                Math.round(r.unitPrice), r.stock});
        }
        model.setRows(rows);
    }

    static String units(long n) {
        return Money.number(n) + " units";
    }
}
