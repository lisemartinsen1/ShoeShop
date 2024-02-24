package Utils;

import Configuration.LoadProperties;
import Tables.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProductUtil {
    private final Properties p = LoadProperties.getInstance();

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Product";

        try (Connection con = DriverManager.getConnection(
                p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Id");
                int amountInStock = rs.getInt("AmountInStock");
                double sizeOfShoe = rs.getDouble("SizeOfShoe");
                String color = rs.getString("Color");
                int price = rs.getInt("Price");
                String brand = rs.getString("Brand");

                Product product = new Product(id, amountInStock, sizeOfShoe, color, price, brand);
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }

    public void printAllProducts(List<Product> products) {
        BelongsUtil belongsUtil = new BelongsUtil();

        products.forEach(product -> {
            System.out.println("\n" + product.getId());

            List<String> categories =  belongsUtil.getCategoriesForProduct(product);
            System.out.println("Category: " + String.join(", ", categories));
            System.out.println("Brand: " + product.getBrand());
            System.out.println("Color: " + product.getColor());
            System.out.println("Size: " + product.getSizeOfShoe());
            System.out.println("Price: " + product.getPrice() + "kr");
        });
    }

    public Product getProductById(int productId) {
        List<Product> products = getAllProducts();
        return products.stream().filter(p -> p.getId() == productId).findFirst().orElse(null);
    }

    public boolean findMatch(int id) {
        boolean foundMatch = false;
        List<Product> products = getAllProducts();

        for (Product p : products) {
            if (p.getId() == id) {
                foundMatch = true;
                break;
            }
        }
        return foundMatch;
    }

}
