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
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddItemForm extends JFrame {

    private JTextField itemNameField;
    private JTextArea itemDescriptionField;
    private JTextField quantityField;
    private JTextField priceField;
    private JComboBox<String> categoryComboBox;
    private JTextField imgPathField;
    private JButton chooseImgButton;
    private JButton submitButton;
    private JButton backButton;
    private User loggedInUser;

    public AddItemForm(DashboardForm dashboard, User loggedInUser) {
        this.loggedInUser = loggedInUser;

        // Set up the frame
        setTitle("Add Item");
        setSize(500, 400);
        setLocationRelativeTo(null); // Center the frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this form

        // Create components
        itemNameField = new JTextField(20);
        itemDescriptionField = new JTextArea(5, 20);
        quantityField = new JTextField(10);
        priceField = new JTextField(10);
        categoryComboBox = new JComboBox<>();
        imgPathField = new JTextField(20);
        chooseImgButton = new JButton("Choose Image");
        submitButton = new JButton("Submit");
        backButton = new JButton("Back");

        // Fetch categories from the database
        fetchCategories();

        // Set up layout
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Item Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(itemNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(itemDescriptionField), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        formPanel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        formPanel.add(categoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Image Path:"), gbc);
        gbc.gridx = 1;
        formPanel.add(imgPathField, gbc);
        gbc.gridx = 2;
        formPanel.add(chooseImgButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(submitButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
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
                dispose(); // Close the add item form
            }
        });
    }

    private void fetchCategories() {
        String url = "jdbc:mysql://localhost:3306/InventoryDB";
        String dbUsername = "tati";
        String dbPassword = "123";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT name FROM Categories WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, loggedInUser.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                categoryComboBox.addItem(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        String itemName = itemNameField.getText();
        String itemDescription = itemDescriptionField.getText();
        String quantityText = quantityField.getText();
        String priceText = priceField.getText();
        String imgPath = imgPathField.getText();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();

        if (itemName.isEmpty() || itemDescription.isEmpty() || quantityText.isEmpty() || priceText.isEmpty() || selectedCategory == null || imgPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity;
        double price;
        try {
            quantity = Integer.parseInt(quantityText);
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid quantity and price.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int categoryId = getCategoryIdByName(selectedCategory);
        if (categoryId == -1) {
            JOptionPane.showMessageDialog(this, "Invalid category selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = "jdbc:mysql://localhost:3306/InventoryDB";
        String dbUsername = "tati";
        String dbPassword = "123";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "INSERT INTO Items (name, description, quantity, price, category_id, img) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, itemName);
            preparedStatement.setString(2, itemDescription);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setDouble(4, price);
            preparedStatement.setInt(5, categoryId);
            preparedStatement.setString(6, imgPath);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                itemNameField.setText("");
                itemDescriptionField.setText("");
                quantityField.setText("");
                priceField.setText("");
                imgPathField.setText("");
                categoryComboBox.setSelectedIndex(-1);
            } else {
                JOptionPane.showMessageDialog(this, "Error adding item.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getCategoryIdByName(String categoryName) {
        String url = "jdbc:mysql://localhost:3306/InventoryDB";
        String dbUsername = "tati";
        String dbPassword = "123";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT category_id FROM Categories WHERE name = ? AND user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, categoryName);
            preparedStatement.setInt(2, loggedInUser.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("category_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // If category is not found
    }
}
