package Utils;

import Configuration.LoadProperties;
import Tables.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CategoryUtil {

    private final Properties p = LoadProperties.getInstance();

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Category";

        try (Connection con = DriverManager.getConnection(
                p.getProperty("connectionString"),
                p.getProperty("name"),
                p.getProperty("password"));
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");

                Category category = new Category(id, name);
                categories.add(category);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return categories;
    }

    public Category getCategoryById(int id) {
        List<Category> categories = getAllCategories();
        return categories.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

}
