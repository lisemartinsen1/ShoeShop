package TimeToShop;

import Configuration.LoadProperties;
import Tables.Customer;
import Utils.CustomerUtil;

import java.sql.*;
import java.util.List;
import java.util.Properties;

public class PlaceOrder {

    private final Properties p = LoadProperties.getInstance();


    public boolean authenticate(String mail, String password) {
        CustomerUtil customerUtil = new CustomerUtil();
        List<Customer> allCustomers = customerUtil.getAllCustomers();
        Customer customer = allCustomers.stream()
                .filter(c -> c.getMail().equalsIgnoreCase(mail) && c.getPassword().equals(password))
                .findFirst().orElse(null);

        return customer != null;
    }

    public int getCustomerId(String mail, String password) {
        String query = "SELECT Id FROM Customer " +
                "Where Mail = ? AND Password = ? ";
        try (Connection con = DriverManager.getConnection(
                p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             PreparedStatement stmt = con.prepareStatement(query);
        ) {
            stmt.setString(1, mail);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Id");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int getOrderId(int customerId) {
        //Hämtar Id för eventuell pågående order.
        String query = "SELECT Id, DateOfOrder FROM ShoeOrder " +
                "Where CustomerId = ? AND DATE(DateOfOrder) = CURDATE()" +
                "ORDER BY DateOfOrder DESC LIMIT 1";
        try (Connection con = DriverManager.getConnection(
                p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             PreparedStatement stmt = con.prepareStatement(query);
        ) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //returnerar 0 om kunden inte har en pågående (skapad idag) beställning.
        return 0;
    }

    public void addToCart(int customerId, Integer orderId, int productId) {
        try (Connection con = DriverManager.getConnection(
                p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             CallableStatement stmt = con.prepareCall("CALL addToCart(?, ?, ?)");
        ) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, orderId);
            stmt.setInt(3, productId);
            stmt.executeQuery();
            System.out.println("Product has been ordered!");

        } catch (SQLException e) {
            System.out.println("Something went wrong.");
            System.out.println(e.getMessage());
        }
    }
}
