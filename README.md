 Inventory Management System Documentation

Table of Contents
1. Introduction
2. Project Overview
3. Class Diagram
4. UI Design
5. Use Case Diagram
6. DB schema
7. Functional Requirements
8. Non-functional Requirements
9. Conclusion

1. Introduction
The Inventory Management System is a software application designed to help businesses track, manage, and organize their inventory efficiently. This documentation provides an overview of the system, including its functionality, design, and requirements.

2. Project Overview
The Inventory Management System consists of several components, including a user interface for interaction, a backend database for data storage, and business logic for processing inventory-related tasks. Users can perform actions such as adding categories, adding items to categories, managing user accounts, and more.

3. Class Diagram
 ![image](https://github.com/yeabtsegataye/inventory_MGT_sys_java/assets/127824421/bba44ac1-0527-40e4-bc43-2f5f0f990886)

4. UI Design
Login /register 
 ![image](https://github.com/yeabtsegataye/inventory_MGT_sys_java/assets/127824421/a05c8b58-dda3-4f6e-b793-ca2f6cc58ea4)

Category and Items page
![image](https://github.com/yeabtsegataye/inventory_MGT_sys_java/assets/127824421/86fe618e-78ab-4164-975e-91527a576023)

 Add Category page
 ![image](https://github.com/yeabtsegataye/inventory_MGT_sys_java/assets/127824421/591519f4-65af-421a-87b4-a8f903f33430)

Add Item page
 ![image](https://github.com/yeabtsegataye/inventory_MGT_sys_java/assets/127824421/86714303-c493-4c12-aecc-78823d069a1f)

5. Use Case Diagram
 ![image](https://github.com/yeabtsegataye/inventory_MGT_sys_java/assets/127824421/f07a6b6a-077b-46ce-86af-d9fab097de46)
6. CREATE DATABASE InventoryDB;

USE InventoryDB;

-- Table for storing user information
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for storing item information
CREATE TABLE Items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES Categories(category_id)
);

-- Table for storing category information
CREATE TABLE Categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Insert sample data into the Users table
INSERT INTO Users (username, password, phone) VALUES
('admin', 'admin_password', '1234567890');

-- Insert sample data into the Categories table
INSERT INTO Categories (name, description, user_id) VALUES
('Electronics', 'Electronic gadgets and devices', 1),
('Furniture', 'Home and office furniture', 1),
('Clothing', 'Apparel and accessories', 1);

-- Insert sample data into the Items table
INSERT INTO Items (name, description, quantity, price, category_id) VALUES
('Laptop', '15-inch display, 8GB RAM, 256GB SSD', 10, 799.99, 1),
('Office Chair', 'Ergonomic chair with lumbar support', 20, 149.99, 2),
('T-Shirt', '100% cotton, various sizes', 50, 19.99, 3);
this is the db we have
7. Functional Requirements
The functional requirements of the Inventory Management System include:
- User authentication: Users should be able to log in and access the system securely.
- Category management: Users can create, view categories for organizing inventory items.
- Item management: Users can add, view, update, and delete items within categories.
- Account management: Users can update their account information, including changing passwords.

8. Non-functional Requirements
The non-functional requirements of the Inventory Management System include:
- Performance: The system should respond to user actions promptly, even with a large amount of data.
- Security: User authentication and data access should be secure to prevent unauthorized access.
- User interface: The UI should be intuitive and user-friendly, allowing users to perform tasks efficiently.
- Reliability: The system should be robust and reliable, with minimal downtime or errors.

9. Conclusion
The Inventory Management System is a comprehensive solution for businesses to manage their inventory effectively. By providing features for category and item management, user authentication, and account management, the system helps streamline inventory-related tasks and improve operational efficiency.

