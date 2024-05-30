package inventorymanagementsystem.gui;

import inventorymanagementsystem.models.User; // Import User class from models package
import inventorymanagementsystem.services.AuthService;
import inventorymanagementsystem.gui.DashboardForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel registerLabel;

    public LoginForm() {
        // Set up the frame
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame

        // Create components
        JLabel headerLabel = new JLabel("Login", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        loginButton = new JButton("Login");
        registerLabel = new JLabel("Don't have an account? Register now", SwingConstants.CENTER);

        // Set up layout and add components
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(headerLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(registerLabel, gbc);

        add(panel, BorderLayout.CENTER);

        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        registerLabel.setForeground(Color.BLUE.darker());
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handleRegister();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Perform login logic here, verify against database
        AuthService authService = new AuthService();
        User loggedInUser = authService.login(username, password);
        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            // Proceed to the dashboard or main application window
            DashboardForm dashboard = new DashboardForm(loggedInUser);
            dashboard.setVisible(true);
            this.dispose(); // Close the login window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        // Open the registration form
        RegisterForm registerForm = new RegisterForm();
        registerForm.setVisible(true);
        this.dispose(); // Close the login window
    }

    private boolean authenticateUser(String username, String password) {
        boolean isAuthenticated = false;
        String url = "jdbc:mysql://localhost:3306/InventoryDB";
        String dbUsername = "tati";
        String dbPassword = "123";

        try {
            // Establish the database connection
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            // Prepare SQL statement
            String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if a matching user is found
            if (resultSet.next()) {
                isAuthenticated = true;
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAuthenticated;
    }

    public static void main(String[] args) {
        // Run the application on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Create and show the login form
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}
