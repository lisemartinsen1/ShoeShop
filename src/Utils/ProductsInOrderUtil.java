package Utils;

import Configuration.LoadProperties;
import Tables.Product;
import Tables.ShoeOrder;
import Tables.ProductsInOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProductsInOrderUtil {

    private final Properties p = LoadProperties.getInstance();
    private final ShoeOrderUtil shoeOrderUtil = new ShoeOrderUtil();
    private final ProductUtil productUtil = new ProductUtil();


    public List<ProductsInOrder> getAllProductsInOrder() {
        List<ProductsInOrder> productsInOrders = new ArrayList<>();
        List<ShoeOrder> allOrders = shoeOrderUtil.getAllOrders();
        List<Product> allProducts = productUtil.getAllProducts();

        String query = "SELECT * FROM ProductsInOrder";

        try (Connection con = DriverManager.getConnection(
                p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int shoeOrderid = rs.getInt("ShoeOrderId");
                int productId = rs.getInt("ProductId");

                ShoeOrder shoeOrder = shoeOrderUtil.getShoeOrderById(shoeOrderid);
                Product product = productUtil.getProductById(productId);

                ProductsInOrder productsInOrder = new ProductsInOrder(shoeOrder, product);
                productsInOrders.add(productsInOrder);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return productsInOrders;
    }


}
