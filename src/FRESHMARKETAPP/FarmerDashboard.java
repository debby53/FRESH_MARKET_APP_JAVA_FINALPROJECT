package FRESHMARKETAPP;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FarmerDashboard {
    private static String dbUrl = "jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP";
    private static String dbUsername = "deborah";
    private static String dbPassword = "12345";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Farmer Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        // Welcome Label
        JLabel lblWelcome = new JLabel("Welcome to Fresh Market, Farmer!", JLabel.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        lblWelcome.setBounds(150, 20, 400, 30);
        lblWelcome.setBorder(new LineBorder(Color.GRAY));
        frame.add(lblWelcome);

        // Title Label
        JLabel lblTitle = new JLabel("My Products", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBounds(200, 60, 300, 30);
        lblTitle.setBorder(new LineBorder(Color.GRAY));
        frame.add(lblTitle);

        // Add Product Button
        JButton btnAddProduct = new JButton("Add Product");
        btnAddProduct.setBounds(100, 120, 100, 50);
        btnAddProduct.setBorder(new LineBorder(Color.BLACK));
        frame.add(btnAddProduct);

        // View Products Button
        JButton btnViewProducts = new JButton("View Products");
        btnViewProducts.setBounds(250, 120, 100, 50);
        btnViewProducts.setBorder(new LineBorder(Color.BLACK));
        frame.add(btnViewProducts);

        // Logout Button
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(400, 120, 100, 50);
        btnLogout.setBorder(new LineBorder(Color.BLACK));
        frame.add(btnLogout);

        // Table to display products
        String[] columnNames = {"Product ID", "Name", "Category", "Price", "Quantity"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable productsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(productsTable);
        scrollPane.setBounds(50, 200, 600, 300);
        frame.add(scrollPane);
        loadProducts(model);

        // Add Product Action
        btnAddProduct.addActionListener(e -> addProductForm(frame));

        // View Products Action
        btnViewProducts.addActionListener(e -> viewProducts(frame));

        // Logout Action
        btnLogout.addActionListener(e -> {
            frame.dispose();
            new LoginPhase().main(null);
        });

        frame.setVisible(true);
    }

    private static void addProductForm(JFrame frame) {
        JFrame addProductFrame = new JFrame("Add Product");
        addProductFrame.setSize(400, 300);
        addProductFrame.setLayout(new FlowLayout());

        JLabel lblProductName = new JLabel("Product Name:");
        JTextField txtProductName = new JTextField(20);
        JLabel lblDescription = new JLabel("Description:");
        JTextArea txtDescription = new JTextArea(3, 20);
        JLabel lblPrice = new JLabel("Price:");
        JTextField txtPrice = new JTextField(10);
        JLabel lblImage = new JLabel("Product Image:");

        JButton btnUploadImage = new JButton("Upload Image");
        JLabel lblImagePath = new JLabel("No image selected");

        btnUploadImage.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Product Image");
            int result = fileChooser.showOpenDialog(addProductFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                lblImagePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        JButton btnSubmit = new JButton("Add Product");
        btnSubmit.addActionListener(e -> {
            String productName = txtProductName.getText();
            String description = txtDescription.getText();
            String price = txtPrice.getText();
            String imagePath = lblImagePath.getText();

            if (productName.isEmpty() || description.isEmpty() || price.isEmpty() || imagePath.equals("No image selected")) {
                JOptionPane.showMessageDialog(addProductFrame, "Please fill in all fields and upload an image!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO PRODUCTS (product_name, description, price, image_path) VALUES (?, ?, ?, ?)")
            ) {
                stmt.setString(1, productName);
                stmt.setString(2, description);
                stmt.setString(3, price);
                stmt.setString(4, imagePath);

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(addProductFrame, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(addProductFrame, "Failed to add product.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(addProductFrame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addProductFrame.add(lblProductName);
        addProductFrame.add(txtProductName);
        addProductFrame.add(lblDescription);
        addProductFrame.add(txtDescription);
        addProductFrame.add(lblPrice);
        addProductFrame.add(txtPrice);
        addProductFrame.add(lblImage);
        addProductFrame.add(btnUploadImage);
        addProductFrame.add(lblImagePath);
        addProductFrame.add(btnSubmit);

        addProductFrame.setLocationRelativeTo(frame);
        addProductFrame.setVisible(true);
    }

    private static void viewProducts(JFrame frame) {
        JFrame productsFrame = new JFrame("View Products");
        productsFrame.setSize(600, 400);
        productsFrame.setLayout(new BorderLayout());

        JTable productsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(productsTable);
        productsFrame.add(scrollPane, BorderLayout.CENTER);

        productsFrame.setLocationRelativeTo(frame);
        productsFrame.setVisible(true);
    }

    private static void loadProducts(DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT product_id, name, category, price, quantity FROM PRODUCTS")
        ) {
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
}
