

package FRESHMARKETAPP;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class BuyerDashboard {

    static final String dbUrl = "jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP";
    static final String dbUsername = "deborah";
    static final String dbPassword = "12345";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Buyer Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 850);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        JLabel lblTitle = new JLabel("Welcome to Fresh Market - Buyer Dashboard", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBounds(100, 20, 800, 40);
        lblTitle.setBorder(new LineBorder(Color.GRAY));
        frame.add(lblTitle);

        String[] columnNames = {"Product ID", "Name", "Category", "Price", "Quantity", "Farmer ID"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 150, 880, 300);
        frame.add(scrollPane);

        JComboBox<String> categoryFilter = new JComboBox<>(new String[]{"All", "Fruits", "Vegetables", "Grains"});
        categoryFilter.setBounds(50, 100, 200, 30);
        frame.add(categoryFilter);

        JTextField searchField = new JTextField();
        searchField.setBounds(270, 100, 200, 30);
        frame.add(searchField);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(480, 100, 100, 30);
        frame.add(btnSearch);

        JButton btnAddToCart = new JButton("Add to Cart");
        btnAddToCart.setBounds(50, 470, 150, 40);
        frame.add(btnAddToCart);

        JButton btnOrder = new JButton("Place Order");
        btnOrder.setBounds(220, 470, 150, 40);
        frame.add(btnOrder);

        JButton btnChat = new JButton("Chat with Farmer");
        btnChat.setBounds(390, 470, 170, 40);
        frame.add(btnChat);

        JButton btnRate = new JButton("Rate Farmer");
        btnRate.setBounds(580, 470, 150, 40);
        frame.add(btnRate);

        JButton btnHistory = new JButton("Order History");
        btnHistory.setBounds(750, 470, 150, 40);
        frame.add(btnHistory);

        loadProducts(model, "All", "");

        categoryFilter.addActionListener(e -> loadProducts(model, (String) categoryFilter.getSelectedItem(), searchField.getText()));

        btnSearch.addActionListener(e -> loadProducts(model, (String) categoryFilter.getSelectedItem(), searchField.getText()));

        btnAddToCart.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Product added to cart (simulation)."));

        btnOrder.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Order placed (simulation)."));

        btnChat.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Chat window simulation - direct messaging feature coming soon!"));

        btnRate.addActionListener(e -> {
            String rating = JOptionPane.showInputDialog(frame, "Rate the farmer (1-5):");
            if (rating != null && !rating.trim().isEmpty()) {
                try {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int farmerId = (int) table.getValueAt(selectedRow, 5);
                        saveFarmerRating(farmerId, Integer.parseInt(rating));
                        JOptionPane.showMessageDialog(frame, "Rating submitted successfully.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid rating.");
                }
            }
        });

        btnHistory.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Order history simulation - display of past orders coming soon!"));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int farmerId = (int) table.getValueAt(table.getSelectedRow(), 5);
                JOptionPane.showMessageDialog(frame, "Farmer Profile Info (ID: " + farmerId + ")\nName, contact, ratings, and more (simulation). More info coming soon.");
            }
        });

        frame.setVisible(true);
    }

    private static void loadProducts(DefaultTableModel model, String category, String keyword) {
        model.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String query = "SELECT product_id, name, category, price, quantity, farmer_id FROM PRODUCTS WHERE quantity > 0";
            if (!category.equals("All")) {
                query += " AND category = ?";
            }
            if (!keyword.isEmpty()) {
                query += " AND LOWER(name) LIKE ?";
            }
            PreparedStatement stmt = conn.prepareStatement(query);
            int index = 1;
            if (!category.equals("All")) {
                stmt.setString(index++, category);
            }
            if (!keyword.isEmpty()) {
                stmt.setString(index, "%" + keyword.toLowerCase() + "%");
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("farmer_id")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
        }
    }

    private static void saveFarmerRating(int farmerId, int rating) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String insert = "INSERT INTO FARMER_RATINGS (farmer_id, rating, rating_date) VALUES (?, ?, SYSDATE)";
            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.setInt(1, farmerId);
            stmt.setInt(2, rating);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save rating: " + ex.getMessage());
        }
    }
}

