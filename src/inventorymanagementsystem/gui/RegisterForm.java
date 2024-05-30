package inventorymanagementsystem.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class RegisterForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JButton registerButton;
    private JLabel loginLabel;

    public RegisterForm() {
        // Set up the frame
        setTitle("Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame

        // Create components
        JLabel headerLabel = new JLabel("Register", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField(20);

        registerButton = new JButton("Register");
        loginLabel = new JLabel("Already have an account? Login now", SwingConstants.CENTER);

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
        panel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(loginLabel, gbc);

        add(panel, BorderLayout.CENTER);

        // Add action listeners
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });

        loginLabel.setForeground(Color.BLUE.darker());
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handleLogin();
            }
        });
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String phone = phoneField.getText();

        if (registerUser(username, password, phone)) {
            JOptionPane.showMessageDialog(this, "Registration successful!");
            // Open the login form
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
            this.dispose(); // Close the registration window
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Try again.", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogin() {
        // Open the login form
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
        this.dispose(); // Close the registration window
    }

    private boolean registerUser(String username, String password, String phone) {
        boolean isRegistered = false;
        String url = "jdbc:mysql://localhost:3306/InventoryDB";
        String dbUsername = "tati";
        String dbPassword = "123";

        try {
            // Establish the database connection
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            
            // Prepare SQL statement
            String query = "INSERT INTO Users (username, password, phone) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, phone);

            // Execute the query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                isRegistered = true;
            }

            // Close resources
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isRegistered;
    }

    public static void main(String[] args) {
        // Run the application on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Create and show the register form
            RegisterForm registerForm = new RegisterForm();
            registerForm.setVisible(true);
        });
    }
}
