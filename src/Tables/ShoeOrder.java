package Tables;
import java.time.LocalDate;

public class ShoeOrder {
    private int id;
    private LocalDate dateOfOrder;
    private Customer customer;

    public ShoeOrder(int id, LocalDate dateOfOrder, Customer customer) {
        this.id = id;
        this.dateOfOrder = dateOfOrder;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(LocalDate dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
