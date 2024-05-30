package inventorymanagementsystem.dao;

import inventorymanagementsystem.models.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/InventoryDB";
    private static final String USERNAME = "tati";
    private static final String PASSWORD = "123";

    public User getUserByUsername(String username) {
        User user = null;
        String query = "SELECT * FROM Users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getInt("user_id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setPhone(resultSet.getString("phone"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}
