package inventorymanagementsystem.dao;

import inventorymanagementsystem.models.Category;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/InventoryDB";
    private static final String USERNAME = "tati";
    private static final String PASSWORD = "123";

    public List<Category> getCategoriesByUserId(int userId) {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Categories WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Category category = new Category();
                    category.setId(resultSet.getInt("category_id"));
                    category.setName(resultSet.getString("name"));
                    category.setDescription(resultSet.getString("description"));
                    categories.add(category);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }
}
