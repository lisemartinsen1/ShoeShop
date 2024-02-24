package Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import Configuration.LoadProperties;
import Tables.Belongs;
import Tables.Category;
import Tables.Product;

public class BelongsUtil {

    private final Properties p = LoadProperties.getInstance();

    public List<Belongs> getBelongs() {
        List<Belongs> belongsList = new ArrayList<>();
        String query = "SELECT * FROM Belongs";
        ProductUtil productUtil = new ProductUtil();
        CategoryUtil categoryUtil = new CategoryUtil();

        try (Connection con = DriverManager.getConnection(
                p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int productId = rs.getInt("ProductId");
                int categoryId = rs.getInt("CategoryId");

                Product product = productUtil.getProductById(productId);
                Category category = categoryUtil.getCategoryById(categoryId);

                Belongs b = new Belongs(product, category);
                belongsList.add(b);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return belongsList;
    }

    public List<String> getCategoriesForProduct(Product product) {
        List<Belongs> belongsList = getBelongs();
        return belongsList.stream().filter(b -> b.getProduct().getId() == product.getId())
                .map(b -> b.getCategory().getName()).collect(Collectors.toList());
    }

}
