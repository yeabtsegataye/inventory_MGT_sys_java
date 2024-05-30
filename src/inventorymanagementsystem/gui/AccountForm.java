package inventorymanagementsystem.gui;

import inventorymanagementsystem.dao.UserDAO;
import inventorymanagementsystem.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountForm extends JFrame {

    private JTextField usernameField;
    private JTextField phoneField;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JButton saveButton;
    private JButton backButton;
    private User loggedInUser;

    public AccountForm(DashboardForm dashboard, User loggedInUser) {
        this.loggedInUser = loggedInUser;

        // Set up the frame
        setTitle("Account Information");
        setSize(400, 400);
        setLocationRelativeTo(null); // Center the frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this form

        // Create components
        usernameField = new JTextField(20);
        phoneField = new JTextField(20);
        oldPasswordField = new JPasswordField(20);
        newPasswordField = new JPasswordField(20);
        saveButton = new JButton("Save");
        backButton = new JButton("Back");

        // Pre-fill user information
        prefillUserInfo();

        // Set up layout
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Old Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(oldPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(newPasswordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(saveButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(backButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSave();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashboard.setVisible(true); // Show the dashboard
                dispose(); // Close the account form
            }
        });
    }

    private void prefillUserInfo() {
        usernameField.setText(loggedInUser.getUsername());
        phoneField.setText(loggedInUser.getPhone());
    }

    private void handleSave() {
        String username = usernameField.getText();
        String phone = phoneField.getText();
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());

        if (username.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.isEmpty() && oldPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the old password to change to a new password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/InventoryDB";
        String dbUsername = "tati";
        String dbPassword = "123";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            if (!newPassword.isEmpty()) {
                // Verify the old password
                UserDAO userDAO = new UserDAO();
                User userFromDB = userDAO.getUserByUsername(loggedInUser.getUsername());

                if (userFromDB == null || !userFromDB.getPassword().equals(oldPassword)) {
                    JOptionPane.showMessageDialog(this, "Old password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String query = "UPDATE Users SET username = ?, phone = ?" + (newPassword.isEmpty() ? "" : ", password = ?") + " WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, phone);
            if (!newPassword.isEmpty()) {
                preparedStatement.setString(3, newPassword);
                preparedStatement.setInt(4, loggedInUser.getId());
            } else {
                preparedStatement.setInt(3, loggedInUser.getId());
            }

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Account information updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loggedInUser.setUsername(username);
                loggedInUser.setPhone(phone);
                if (!newPassword.isEmpty()) {
                    loggedInUser.setPassword(newPassword);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error updating account information.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
