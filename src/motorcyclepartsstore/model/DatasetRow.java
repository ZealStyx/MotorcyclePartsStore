package motorcyclepartsstore.model;

import java.time.LocalDate;

/** One weekly sales record of the demo dataset used by the prediction models. */
public class DatasetRow {

    public final LocalDate date;
    public final int productId;
    public final String product;
    public final String category;
    public final int qtySold;
    public final double unitPrice;
    public final int stock;
    public final int weekIndex;
    public final int weekOfYear;

    public DatasetRow(LocalDate date, int productId, String product, String category, int qtySold,
            double unitPrice, int stock, int weekIndex, int weekOfYear) {
        this.date = date;
        this.productId = productId;
        this.product = product;
        this.category = category;
        this.qtySold = qtySold;
        this.unitPrice = unitPrice;
        this.stock = stock;
        this.weekIndex = weekIndex;
        this.weekOfYear = weekOfYear;
    }
}
