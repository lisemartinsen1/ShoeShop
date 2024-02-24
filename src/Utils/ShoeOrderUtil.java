package Utils;

import Configuration.LoadProperties;
import Tables.Customer;
import Tables.ShoeOrder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ShoeOrderUtil {

    private final Properties p = LoadProperties.getInstance();

    public List<ShoeOrder> getAllOrders() {
        List<ShoeOrder> orders = new ArrayList<>();
        String query = "SELECT * from ShoeOrder";
        CustomerUtil customerUtil = new CustomerUtil();

        try (Connection con = DriverManager.getConnection(
                p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Id");
                LocalDate date = rs.getDate("DateOfOrder").toLocalDate();
                int customerId = rs.getInt("CustomerId");

                Customer customer = customerUtil.getCustomerById(customerId);
                ShoeOrder shoeOrder = new ShoeOrder(id, date, customer);
                orders.add(shoeOrder);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }

    public int getOrderNrForNewOrder(List<ShoeOrder> shoeOrders) {
        return shoeOrders.stream().mapToInt(ShoeOrder::getId).max().orElseThrow() + 1;
    }

    public ShoeOrder getShoeOrderById(int shoeOrderId) {
        List<ShoeOrder> shoeOrders = getAllOrders();
        return shoeOrders.stream().filter(s -> s.getId() == shoeOrderId).findFirst().orElse(null);
    }


}
