package motorcyclepartsstore.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sale {

    private final String number;
    private final String customer;
    private final String payment;
    private final LocalDateTime time;
    private final List<SaleLine> lines;
    private final String status = "Paid";

    public Sale(String number, String customer, String payment, LocalDateTime time, List<SaleLine> lines) {
        this.number = number;
        this.customer = customer;
        this.payment = payment;
        this.time = time;
        this.lines = new ArrayList<>(lines);
    }

    public String getNumber() { return number; }
    public String getCustomer() { return customer; }
    public String getPayment() { return payment; }
    public LocalDateTime getTime() { return time; }
    public List<SaleLine> getLines() { return lines; }
    public String getStatus() { return status; }

    public double total() {
        double t = 0;
        for (SaleLine l : lines) {
            t += l.subtotal();
        }
        return Math.round(t * 100.0) / 100.0;
    }

    public int itemCount() {
        int n = 0;
        for (SaleLine l : lines) {
            n += l.getQty();
        }
        return n;
    }
}
