package inventorymanagementsystem.gui;

import inventorymanagementsystem.models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardForm extends JFrame {

    private final JButton categoryButton;
    private JButton addCategoryButton;
    private JButton addItemButton;
    private JButton accountButton;
    private JButton logoutButton;
    private JPanel itemsPanel;
    private JButton backButton;  // Add backButton

    public DashboardForm(User loggedInUser) {
        // Set up the frame
        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame

        // Create components
        categoryButton = new JButton("Category");
        addCategoryButton = new JButton("Add Category");
        addItemButton = new JButton("Add Items");
        accountButton = new JButton("Account");
        logoutButton = new JButton("Log Out");
        backButton = new JButton("Back");  // Initialize backButton

        // Set up layout for the main frame
        setLayout(new BorderLayout());

        // Create sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.add(categoryButton);
        sidebar.add(addCategoryButton);
        sidebar.add(addItemButton);
        sidebar.add(accountButton);
        sidebar.add(logoutButton);

        add(sidebar, BorderLayout.WEST);

        // Create items panel
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new GridLayout(0, 3, 10, 10)); // 3 columns, variable rows
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        categoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle category action
                handleViewCategories(loggedInUser);
            }
        });

        addCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle add category action
                handleAddCategory(loggedInUser);
            }
        });
        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle add item action
                handleAddItem(loggedInUser);
            }
        });

        accountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle account action
                handleAccount(loggedInUser);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle logout action
                handleLogout();
            }
        });

        backButton.addActionListener(new ActionListener() {  // Add action listener for backButton
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchAndDisplayCategories(loggedInUser.getId());
            }
        });

        // Fetch and display categories
        fetchAndDisplayCategories(loggedInUser.getId());
    }
  
    private void fetchAndDisplayCategories(int userId) {
        itemsPanel.removeAll();  // Clear the items panel
        String url = "jdbc:mysql://localhost:3306/InventoryDB";
        String dbUsername = "tati";
        String dbPassword = "123";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT * FROM Categories WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int categoryId = resultSet.getInt("category_id");
                String categoryName = resultSet.getString("name");
                String img = resultSet.getString("img");
                String description = resultSet.getString("description");

                addCategoryToPanel(categoryId, categoryName, description,img);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        itemsPanel.revalidate();
        itemsPanel.repaint();
    }
 public void reloadCategories(User loggedInUser) {
        fetchAndDisplayCategories(loggedInUser.getId());
    }
private void addCategoryToPanel(int categoryId, String categoryName, String description, String imgPath) {
    JPanel categoryPanel = new JPanel();
    categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
    categoryPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    // Load image from file path and resize it to fit the container
    ImageIcon icon = new ImageIcon(imgPath);
    Image image = icon.getImage().getScaledInstance(215, 200, Image.SCALE_SMOOTH);
    ImageIcon resizedIcon = new ImageIcon(image);
    JLabel imgLabel = new JLabel(resizedIcon);
    imgLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    JLabel nameLabel = new JLabel(categoryName, SwingConstants.CENTER);
    JLabel detailsLabel = new JLabel(description, SwingConstants.CENTER);
    JButton viewButton = new JButton("View");

    viewButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadItemsByCategory(categoryId);
        }
    });

    categoryPanel.add(imgLabel);
    categoryPanel.add(nameLabel);
    categoryPanel.add(detailsLabel);
    categoryPanel.add(viewButton);
    itemsPanel.add(categoryPanel);
    itemsPanel.revalidate();
    itemsPanel.repaint();
}

private void loadItemsByCategory(int categoryId) {
        itemsPanel.removeAll();  // Clear the items panel

        String url = "jdbc:mysql://localhost:3306/InventoryDB";
        String dbUsername = "tati";
        String dbPassword = "123";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT * FROM Items WHERE category_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String price = resultSet.getString("price");
                String quantity = resultSet.getString("quantity");
                String itemName = resultSet.getString("name");
                String itemDescription = resultSet.getString("description");
                String itemImagePath = resultSet.getString("img"); // Assuming there's a column for image path

                addItemToPanel(itemName, itemDescription, itemImagePath, price, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        itemsPanel.add(backButton);  // Add backButton when viewing items
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

private void addItemToPanel(String name, String description, String imagePath, String price, String quantity) {
    JPanel itemPanel = new JPanel();
    itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
    itemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300)); // Maximize width

    JLabel imgLabel = new JLabel(); // Placeholder for image
    imgLabel.setPreferredSize(new Dimension(150, 150));
    imgLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
    // Load image from file path and set it to the label
    ImageIcon icon = new ImageIcon(imagePath);
    Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
    imgLabel.setIcon(new ImageIcon(image));

    JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
    JLabel priceLabel = new JLabel("Price: " + price, SwingConstants.CENTER);
    JLabel quantityLabel = new JLabel("Quantity: " + quantity, SwingConstants.CENTER);
    JLabel descLabel = new JLabel(description, SwingConstants.CENTER);

    // Create buttons and make them look like the 'View' button
    JButton deleteButton = new JButton("Delete");
    JButton updateButton = new JButton("Update");

    // Add action listeners for delete and update buttons
    deleteButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleDeleteItem(name);
        }
    });

    updateButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleUpdateItem(name, description, imagePath, price, quantity);
        }
    });

    // Create a panel for the buttons and add them
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center alignment
    buttonPanel.add(deleteButton);
    buttonPanel.add(updateButton);
    buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, deleteButton.getPreferredSize().height));

    // Align components to fill the width
    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    quantityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Add components to the item panel
    itemPanel.add(imgLabel);
    itemPanel.add(nameLabel);
    itemPanel.add(descLabel);
    itemPanel.add(priceLabel);
    itemPanel.add(quantityLabel);
    itemPanel.add(buttonPanel);

    // Add to itemsPanel
    itemsPanel.add(itemPanel);
    itemsPanel.revalidate();
    itemsPanel.repaint();
}

// Method to create a button with the specified text and style
private JButton createButton(String text) {
    JButton button = new JButton(text);
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setMaximumSize(new Dimension(100, 30));
    button.setBackground(Color.WHITE);
    button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    return button;
}

// Handle item deletion
private void handleDeleteItem(String name) {
    int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this item?", "Delete Item", JOptionPane.YES_NO_OPTION);
    if (confirmation == JOptionPane.YES_OPTION) {
        String url = "jdbc:mysql://localhost:3306/InventoryDB";
        String dbUsername = "tati";
        String dbPassword = "123";
        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "DELETE FROM Items WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
           // fetchAndDisplayCategories(loggedInUser.getId()); // Refresh the list after deletion
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// Handle item update with a popup
private void handleUpdateItem(String name, String description, String imagePath, String price, String quantity) {
    // Create the input fields
    JTextField nameField = new JTextField(name);
    JTextField descField = new JTextField(description);
    JTextField imgField = new JTextField(imagePath);
    JTextField priceField = new JTextField(price);
    JTextField quantityField = new JTextField(quantity);

    // Create a panel to hold the input fields
    JPanel panel = new JPanel(new GridLayout(5, 2));
    panel.add(new JLabel("Name:"));
    panel.add(nameField);
    panel.add(new JLabel("Description:"));
    panel.add(descField);
    panel.add(new JLabel("Image Path:"));
    panel.add(imgField);
    panel.add(new JLabel("Price:"));
    panel.add(priceField);
    panel.add(new JLabel("Quantity:"));
    panel.add(quantityField);

    // Show the dialog
    int result = JOptionPane.showConfirmDialog(this, panel, "Update Item", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
        String newName = nameField.getText();
        String newDescription = descField.getText();
        String newImagePath = imgField.getText();
        String newPrice = priceField.getText();
        String newQuantity = quantityField.getText();

        String url = "jdbc:mysql://localhost:3306/InventoryDB";
        String dbUsername = "tati";
        String dbPassword = "123";
        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "UPDATE Items SET name = ?, description = ?, img = ?, price = ?, quantity = ? WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newDescription);
            preparedStatement.setString(3, newImagePath);
            preparedStatement.setString(4, newPrice);
            preparedStatement.setString(5, newQuantity);
            preparedStatement.setString(6, name);
            preparedStatement.executeUpdate();
            //fetchAndDisplayCategories(loggedInUser.getId()); // Refresh the list after update
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

    private void handleViewCategories(User loggedInUser) {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchAndDisplayCategories(loggedInUser.getId());
            }
        });
    }

    private void handleAddCategory(User loggedInUser) {
        // Open the AddCategoryForm
        AddCategoryForm addCategoryForm = new AddCategoryForm(this, loggedInUser);
        addCategoryForm.setVisible(true);
        this.setVisible(false);
    }

    private void handleAddItem(User loggedInUser) {
        AddItemForm addItemForm = new AddItemForm(this, loggedInUser);
        addItemForm.setVisible(true);
        this.setVisible(false);
    }

    private void handleAccount(User loggedInUser) {
        AccountForm accountForm = new AccountForm(this, loggedInUser);
        accountForm.setVisible(true);
        this.setVisible(false);
    }

    private void handleLogout() {
        // Go back to the LoginForm
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
        this.dispose(); // Close the dashboard window
    }

    public static void main(String[] args) {
        // Run the application on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Create and show the dashboard form
            // DashboardForm dashboardForm = new DashboardForm();
            // dashboardForm.setVisible(true);
        });
    }
}
