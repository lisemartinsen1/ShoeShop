package Utils;

import Configuration.LoadProperties;
import Tables.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CustomerUtil {
    private final Properties p = LoadProperties.getInstance();

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM Customer";

        try (Connection con = DriverManager.getConnection(
                p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                String mail = rs.getString("Mail");
                String city = rs.getString("City");
                String password = rs.getString("Password");

                Customer customer = new Customer(id, name, mail, city, password);
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customers;
    }

    public Customer getCustomerById(int custId) {
        List<Customer> customers = getAllCustomers();
        return customers.stream().filter(customer -> customer.getId() == custId).findFirst().orElse(null);
    }



}
