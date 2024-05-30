package inventorymanagementsystem;

import javax.swing.SwingUtilities;
import inventorymanagementsystem.gui.LoginForm;

/**
 * Main class for the Inventory Management System.
 * Initializes the application and shows the login form.
 * 
 * @author user
 */
public class InventoryManagementSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Run the application on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Create and show the login form
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}
