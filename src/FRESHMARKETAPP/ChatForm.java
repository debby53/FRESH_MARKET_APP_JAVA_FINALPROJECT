

package FRESHMARKETAPP;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

// First, create a class for the Chat Form
class ChatForm extends JFrame {
    private int farmerId;
    private int buyerId;
    private String buyerName;
    private String farmerName;
    private JTextArea chatHistory;
    private JTextField messageField;
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private Timer refreshTimer;

    public ChatForm(String dbUrl, String dbUsername, String dbPassword, int farmerId, String farmerName, String buyerName) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.buyerName = buyerName;

        // Try to get or create buyer ID
        this.buyerId = getBuyerId(buyerName);

        setTitle("Chat with " + farmerName);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Chat history area
        chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        chatHistory.setLineWrap(true);
        chatHistory.setWrapStyleWord(true);
        chatHistory.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(chatHistory);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Message input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));

        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        messageField.addActionListener(e -> sendMessage());

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Add components to main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Load chat history
        loadChatHistory();

        // Set up auto-refresh timer (refresh every 5 seconds)
        refreshTimer = new Timer(5000, e -> loadChatHistory());
        refreshTimer.start();

        // Stop timer when window closes
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                refreshTimer.stop();
            }
        });

        setVisible(true);
    }

    private int getBuyerId(String buyerName) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            // Check if buyer exists
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT buyer_id FROM BUYERS WHERE buyer_name = ?");
            checkStmt.setString(1, buyerName);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("buyer_id");
            } else {
                // Create new buyer
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO BUYERS (buyer_name, registration_date) VALUES (?, SYSDATE)",
                        new String[] {"buyer_id"});
                insertStmt.setString(1, buyerName);
                insertStmt.executeUpdate();

                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error accessing buyer data: " + ex.getMessage());
        }
        return -1; // Error case
    }

    private void loadChatHistory() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT c.message_text, c.sender_type, c.timestamp " +
                            "FROM CHAT_MESSAGES c " +
                            "WHERE (c.farmer_id = ? AND c.buyer_id = ?) " +
                            "ORDER BY c.timestamp ASC");

            stmt.setInt(1, farmerId);
            stmt.setInt(2, buyerId);

            ResultSet rs = stmt.executeQuery();

            StringBuilder history = new StringBuilder();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                String message = rs.getString("message_text");
                String senderType = rs.getString("sender_type");
                Timestamp timestamp = rs.getTimestamp("timestamp");

                String sender = senderType.equals("BUYER") ? buyerName : farmerName;
                String formattedTime = dateFormat.format(timestamp);

                history.append(formattedTime).append(" - ").append(sender).append(": ").append(message).append("\n\n");
            }

            chatHistory.setText(history.toString());

            // Scroll to bottom
            chatHistory.setCaretPosition(chatHistory.getDocument().getLength());

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading chat history: " + ex.getMessage());
        }
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (message.isEmpty()) return;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO CHAT_MESSAGES (farmer_id, buyer_id, message_text, sender_type, timestamp) " +
                            "VALUES (?, ?, ?, ?, SYSDATE)");

            stmt.setInt(1, farmerId);
            stmt.setInt(2, buyerId);
            stmt.setString(3, message);
            stmt.setString(4, "BUYER"); // Message is from buyer

            stmt.executeUpdate();

            // Clear message field
            messageField.setText("");

            // Reload chat history
            loadChatHistory();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error sending message: " + ex.getMessage());
        }
    }
}

//public class BuyerDashboard {
//
//    static final String dbUrl = "jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP";
//    static final String dbUsername = "deborah";
//    static final String dbPassword = "12345";
//
//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Buyer Dashboard");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1000, 850);
//        frame.setLayout(null);
//        frame.setLocationRelativeTo(null);
//
//        JLabel lblTitle = new JLabel("Welcome to Fresh Market - Buyer Dashboard", JLabel.CENTER);
//        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
//        lblTitle.setBounds(100, 20, 800, 40);
//        lblTitle.setBorder(new LineBorder(Color.GRAY));
//        frame.add(lblTitle);
//
//        String[] columnNames = {"Product ID", "Name", "Category", "Price", "Quantity", "Farmer ID", "Farmer Name"};
//        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
//        JTable table = new JTable(model);
//        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setBounds(50, 150, 880, 300);
//        frame.add(scrollPane);
//
//        JComboBox<String> categoryFilter = new JComboBox<>(new String[]{"All", "Fruits", "Vegetables", "Grains"});
//        categoryFilter.setBounds(50, 100, 200, 30);
//        frame.add(categoryFilter);
//
//        JTextField searchField = new JTextField();
//        searchField.setBounds(270, 100, 200, 30);
//        frame.add(searchField);
//
//        JButton btnSearch = new JButton("Search");
//        btnSearch.setBounds(480, 100, 100, 30);
//        frame.add(btnSearch);
//
//        JButton btnAddToCart = new JButton("Add to Cart");
//        btnAddToCart.setBounds(50, 470, 150, 40);
//        frame.add(btnAddToCart);
//
//        JButton btnOrder = new JButton("Place Order");
//        btnOrder.setBounds(220, 470, 150, 40);
//        frame.add(btnOrder);
//
//        JButton btnChat = new JButton("Chat with Farmer");
//        btnChat.setBounds(390, 470, 170, 40);
//        frame.add(btnChat);
//
//        JButton btnRate = new JButton("Rate Farmer");
//        btnRate.setBounds(580, 470, 150, 40);
//        frame.add(btnRate);
//
//        JButton btnHistory = new JButton("Order History");
//        btnHistory.setBounds(750, 470, 150, 40);
//        frame.add(btnHistory);
//
//        loadProducts(model, "All", "");
//
//        categoryFilter.addActionListener(e -> loadProducts(model, (String) categoryFilter.getSelectedItem(), searchField.getText()));
//
//        btnSearch.addActionListener(e -> loadProducts(model, (String) categoryFilter.getSelectedItem(), searchField.getText()));
//
//        btnAddToCart.addActionListener(e -> {
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow != -1) {
//                int productId = (int) table.getValueAt(selectedRow, 0);
//                String quantityStr = JOptionPane.showInputDialog(frame, "Enter quantity to add to cart:");
//                String buyerName = JOptionPane.showInputDialog(frame, "Enter your name:");
//
//                if (quantityStr != null && buyerName != null) {
//                    try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
//                        String insert = "INSERT INTO CART (product_id, quantity, buyer_name) VALUES (?, ?, ?)";
//                        PreparedStatement stmt = conn.prepareStatement(insert);
//                        stmt.setInt(1, productId);
//                        stmt.setInt(2, Integer.parseInt(quantityStr));
//                        stmt.setString(3, buyerName);
//                        stmt.executeUpdate();
//                        JOptionPane.showMessageDialog(frame, "Product added to cart!");
//                    } catch (Exception ex) {
//                        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
//                    }
//                }
//            } else {
//                JOptionPane.showMessageDialog(frame, "Please select a product first.");
//            }
//        });
//
//        btnOrder.addActionListener(e -> {
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow != -1) {
//                int productId = (int) table.getValueAt(selectedRow, 0);
//
//                JTextField buyerNameField = new JTextField();
//                JTextField contactField = new JTextField();
//                JTextField addressField = new JTextField();
//                JTextField quantityField = new JTextField();
//
//                JPanel panel = new JPanel(new GridLayout(0, 1));
//                panel.add(new JLabel("Your Name:"));
//                panel.add(buyerNameField);
//                panel.add(new JLabel("Contact Number:"));
//                panel.add(contactField);
//                panel.add(new JLabel("Delivery Address:"));
//                panel.add(addressField);
//                panel.add(new JLabel("Quantity:"));
//                panel.add(quantityField);
//
//                int result = JOptionPane.showConfirmDialog(frame, panel, "Place Your Order", JOptionPane.OK_CANCEL_OPTION);
//                if (result == JOptionPane.OK_OPTION) {
//                    try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
//                        String insert = "INSERT INTO ORDERED (product_id, quantity, buyer_name, delivery_address, contact_number) VALUES (?, ?, ?, ?, ?)";
//                        PreparedStatement stmt = conn.prepareStatement(insert);
//                        stmt.setInt(1, productId);
//                        stmt.setInt(2, Integer.parseInt(quantityField.getText()));
//                        stmt.setString(3, buyerNameField.getText());
//                        stmt.setString(4, addressField.getText());
//                        stmt.setString(5, contactField.getText());
//                        stmt.executeUpdate();
//                        JOptionPane.showMessageDialog(frame, "Order placed successfully!");
//                    } catch (Exception ex) {
//                        JOptionPane.showMessageDialog(frame, "Error placing order: " + ex.getMessage());
//                    }
//                }
//            } else {
//                JOptionPane.showMessageDialog(frame, "Please select a product to order.");
//            }
//        });
//
//        // Updated chat button functionality
//        btnChat.addActionListener(e -> {
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow == -1) {
//                JOptionPane.showMessageDialog(frame, "Please select a product to chat with the farmer.");
//                return;
//            }
//
//            int farmerId = (int) table.getValueAt(selectedRow, 5);
//            String farmerName = (String) table.getValueAt(selectedRow, 6);
//
//            if (farmerName == null || farmerName.isEmpty()) {
//                farmerName = "Farmer #" + farmerId;
//            }
//
//            String buyerName = JOptionPane.showInputDialog(frame, "Enter your name:");
//            if (buyerName != null && !buyerName.trim().isEmpty()) {
//                createChatTablesIfNeeded(); // Create chat tables if they don't exist
//                new ChatForm(dbUrl, dbUsername, dbPassword, farmerId, farmerName, buyerName);
//            }
//        });
//
//        btnRate.addActionListener(e -> {
//            String rating = JOptionPane.showInputDialog(frame, "Rate the farmer (1-5):");
//            if (rating != null && !rating.trim().isEmpty()) {
//                try {
//                    int selectedRow = table.getSelectedRow();
//                    if (selectedRow != -1) {
//                        int farmerId = (int) table.getValueAt(selectedRow, 5);
//                        saveFarmerRating(farmerId, Integer.parseInt(rating));
//                        JOptionPane.showMessageDialog(frame, "Rating submitted successfully.");
//                    }
//                } catch (NumberFormatException ex) {
//                    JOptionPane.showMessageDialog(frame, "Invalid rating.");
//                }
//            }
//        });
//
//        btnHistory.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Order history simulation - display of past orders coming soon!"));
//
//        table.getSelectionModel().addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
//                int farmerId = (int) table.getValueAt(table.getSelectedRow(), 5);
//                JOptionPane.showMessageDialog(frame, "Farmer Profile Info (ID: " + farmerId + ")\nName, contact, ratings, and more (simulation). More info coming soon.");
//            }
//        });
//
//        frame.setVisible(true);
//    }
//
//    private static void createChatTablesIfNeeded() {
//        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
//            DatabaseMetaData meta = conn.getMetaData();
//
//            // Check if BUYERS table exists
//            ResultSet buyersTable = meta.getTables(null, "DEBORAH", "BUYERS", null);
//            if (!buyersTable.next()) {
//                // Create BUYERS table
//                Statement stmt = conn.createStatement();
//                stmt.execute(
//                        "CREATE TABLE BUYERS (" +
//                                "buyer_id NUMBER PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
//                                "buyer_name VARCHAR2(100) NOT NULL," +
//                                "registration_date DATE DEFAULT SYSDATE," +
//                                "CONSTRAINT buyer_name_unique UNIQUE (buyer_name)" +
//                                ")"
//                );
//            }
//
//            // Check if CHAT_MESSAGES table exists
//            ResultSet chatTable = meta.getTables(null, "DEBORAH", "CHAT_MESSAGES", null);
//            if (!chatTable.next()) {
//                // Create CHAT_MESSAGES table
//                Statement stmt = conn.createStatement();
//                stmt.execute(
//                        "CREATE TABLE CHAT_MESSAGES (" +
//                                "message_id NUMBER PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
//                                "farmer_id NUMBER NOT NULL," +
//                                "buyer_id NUMBER NOT NULL," +
//                                "message_text VARCHAR2(500) NOT NULL," +
//                                "sender_type VARCHAR2(10) NOT NULL," + // BUYER or FARMER
//                                "timestamp DATE DEFAULT SYSDATE," +
//                                "is_read NUMBER(1) DEFAULT 0" +
//                                ")"
//                );
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error creating chat tables: " + ex.getMessage());
//        }
//    }
//
//    private static void loadProducts(DefaultTableModel model, String category, String keyword) {
//        model.setRowCount(0);
//        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
//            String query = "SELECT p.product_id, p.name, p.category, p.price, p.quantity, p.farmer_id, f.farmer_name " +
//                    "FROM PRODUCTS p " +
//                    "LEFT JOIN FARMERS f ON p.farmer_id = f.farmer_id " +
//                    "WHERE p.quantity > 0";
//            if (!category.equals("All")) {
//                query += " AND p.category = ?";
//            }
//            if (!keyword.isEmpty()) {
//                query += " AND LOWER(p.name) LIKE ?";
//            }
//            PreparedStatement stmt = conn.prepareStatement(query);
//            int index = 1;
//            if (!category.equals("All")) {
//                stmt.setString(index++, category);
//            }
//            if (!keyword.isEmpty()) {
//                stmt.setString(index, "%" + keyword.toLowerCase() + "%");
//            }
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                model.addRow(new Object[]{
//                        rs.getInt("product_id"),
//                        rs.getString("name"),
//                        rs.getString("category"),
//                        rs.getDouble("price"),
//                        rs.getInt("quantity"),
//                        rs.getInt("farmer_id"),
//                        rs.getString("farmer_name") // Added farmer name column
//                });
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
//        }
//    }
//
//    private static void saveFarmerRating(int farmerId, int rating) {
//        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
//            String insert = "INSERT INTO FARMER_RATINGS (farmer_id, rating, rating_date) VALUES (?, ?, SYSDATE)";
//            PreparedStatement stmt = conn.prepareStatement(insert);
//            stmt.setInt(1, farmerId);
//            stmt.setInt(2, rating);
//            stmt.executeUpdate();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Failed to save rating: " + ex.getMessage());
//        }
//    }
