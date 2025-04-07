package FRESHMARKETAPP;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FarmerDashboard {

    // Replace with your actual DB credentials
    static final String dbUrl = "jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP";
    static final String dbUsername = "deborah";
    static final String dbPassword = "12345";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Farmer Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 750);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        JLabel lblWelcome = new JLabel("Welcome to Fresh Market, Farmer!", JLabel.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        lblWelcome.setBounds(150, 20, 500, 30);
        lblWelcome.setBorder(new LineBorder(Color.GRAY));
        frame.add(lblWelcome);

        JLabel lblTitle = new JLabel("My Products", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBounds(200, 60, 400, 30);
        lblTitle.setBorder(new LineBorder(Color.GRAY));
        frame.add(lblTitle);

        JButton btnAddProduct = new JButton("Add Product");
        btnAddProduct.setBounds(50, 120, 130, 50);
        frame.add(btnAddProduct);

        JButton btnViewProducts = new JButton("View Products");
        btnViewProducts.setBounds(200, 120, 130, 50);
        frame.add(btnViewProducts);

        JButton btnInventory = new JButton("Manage Inventory");
        btnInventory.setBounds(350, 120, 150, 50);
        frame.add(btnInventory);

        JButton btnCustomerChat = new JButton("Customer Chat");
        btnCustomerChat.setBounds(520, 120, 130, 50);
        frame.add(btnCustomerChat);

        JButton btnLikedProduce = new JButton("Liked Produce");
        btnLikedProduce.setBounds(50, 190, 130, 50);
        frame.add(btnLikedProduce);

        JButton btnTips = new JButton("Farming Tips");
        btnTips.setBounds(200, 190, 130, 50);
        frame.add(btnTips);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(520, 190, 130, 50);
        frame.add(btnLogout);

        // Search bar
        JTextField searchField = new JTextField();
        searchField.setBounds(50, 250, 200, 30);
        searchField.setVisible(false);
        frame.add(searchField);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(270, 250, 100, 30);
        btnSearch.setVisible(false);
        frame.add(btnSearch);

        // Product Table
        String[] columnNames = {"Product ID", "Name", "Category", "Price", "Quantity"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable productsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(productsTable);
        scrollPane.setBounds(50, 300, 680, 350);
        scrollPane.setVisible(false);
        frame.add(scrollPane);

        // Add Product
        btnAddProduct.addActionListener(e -> addProductForm(frame, model));

        // View Products
        btnViewProducts.addActionListener(e -> {
            searchField.setVisible(true);
            btnSearch.setVisible(true);
            scrollPane.setVisible(true);
            model.setRowCount(0);
            loadProducts(model);
        });

        // Search Logic
        btnSearch.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            if (keyword.isEmpty()) {
                loadProducts(model);
            } else {
                try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                     PreparedStatement stmt = conn.prepareStatement(
                             "SELECT product_id, name, category, price, quantity FROM PRODUCTS WHERE LOWER(name) LIKE ? OR CAST(product_id AS CHAR) LIKE ?")
                ) {
                    stmt.setString(1, "%" + keyword + "%");
                    stmt.setString(2, "%" + keyword + "%");
                    ResultSet rs = stmt.executeQuery();
                    model.setRowCount(0);
                    while (rs.next()) {
                        model.addRow(new Object[]{
                                rs.getInt("product_id"),
                                rs.getString("name"),
                                rs.getString("category"),
                                rs.getDouble("price"),
                                rs.getInt("quantity")
                        });
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error searching products:\n" + ex.getMessage());
                }
            }
        });

        // Placeholder actions
        btnInventory.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Inventory management coming soon!"));
        btnCustomerChat.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Chat feature coming soon!"));
        btnLikedProduce.addActionListener(e -> JOptionPane.showMessageDialog(frame, "List of liked produce coming soon!"));
        btnTips.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Useful farming tips coming soon!"));
        btnLogout.addActionListener(e -> {
            frame.dispose();
            // You can replace this with your login form logic
            JOptionPane.showMessageDialog(null, "Logged out!");
        });

        frame.setVisible(true);
    }

    // Load products from DB
    private static void loadProducts(DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT product_id, name, category, price, quantity FROM PRODUCTS")) {

            model.setRowCount(0); // Clear table
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                };
                model.addRow(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading products:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Placeholder method for adding a product
    private static void addProductForm(JFrame frame, DefaultTableModel model) {
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Product Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add New Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            double price;
            int quantity;

            // Validation
            try {
                price = Double.parseDouble(priceField.getText().trim());
                quantity = Integer.parseInt(quantityField.getText().trim());

                try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                     PreparedStatement stmt = conn.prepareStatement(
                             "INSERT INTO PRODUCTS (name, category, price, quantity) VALUES (?, ?, ?, ?)")
                ) {
                    stmt.setString(1, name);
                    stmt.setString(2, category);
                    stmt.setDouble(3, price);
                    stmt.setInt(4, quantity);
                    int rowsInserted = stmt.executeUpdate();

                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(frame, "Product added successfully!");
                        loadProducts(model); // Refresh table if visible
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Database error:\n" + ex.getMessage());
                }

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numeric values for Price and Quantity.");
            }
        }
    }

}

