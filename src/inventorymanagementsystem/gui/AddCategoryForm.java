package inventorymanagementsystem.gui;

import inventorymanagementsystem.models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCategoryForm extends JFrame {

    private JTextField categoryNameField;
    private JTextArea categoryDescriptionField;
    private JTextField imgPathField;
    private JButton chooseImgButton;
    private JButton submitButton;
    private JButton backButton;
    private User loggedInUser;

    public AddCategoryForm(DashboardForm dashboard, User loggedInUser) {
        this.loggedInUser = loggedInUser;

        // Set up the frame
        setTitle("Add Category");
        setSize(500, 400);
        setLocationRelativeTo(null); // Center the frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this form

        // Create components
        categoryNameField = new JTextField(20);
        categoryDescriptionField = new JTextArea(5, 20);
        imgPathField = new JTextField(20);
        chooseImgButton = new JButton("Choose Image");
        submitButton = new JButton("Submit");
        backButton = new JButton("Back");

        // Set up layout
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Category Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(categoryNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(categoryDescriptionField), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Image Path:"), gbc);
        gbc.gridx = 1;
        formPanel.add(imgPathField, gbc);
        gbc.gridx = 2;
        formPanel.add(chooseImgButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(submitButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(backButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Add action listeners
        chooseImgButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleChooseImage();
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashboard.setVisible(true); // Show the dashboard
                dispose(); // Close the add category form
            }
        });
    }

    private void handleChooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imgPathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void handleSubmit() {
        String categoryName = categoryNameField.getText();
        String categoryDescription = categoryDescriptionField.getText();
        String imgPath = imgPathField.getText();

        if (categoryName.isEmpty() || categoryDescription.isEmpty() || imgPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/InventoryDB";
        String dbUsername = "tati";
        String dbPassword = "123";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "INSERT INTO Categories (name, description, user_id, img) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, categoryName);
            preparedStatement.setString(2, categoryDescription);
            preparedStatement.setInt(3, loggedInUser.getId());
            preparedStatement.setString(4, imgPath);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Category added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                categoryNameField.setText("");
                categoryDescriptionField.setText("");
                imgPathField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error adding category.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
