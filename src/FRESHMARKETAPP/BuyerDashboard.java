package FRESHMARKETAPP;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BuyerDashboard {
    // Database connection parameters
    static final String dbUrl = "jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP";
    static final String dbUsername = "deborah";
    static final String dbPassword = "12345";

    // Color scheme
    static final Color PRIMARY_COLOR = new Color(180, 230, 180);     // Lighter green
    static final Color SECONDARY_COLOR = new Color(255, 200, 150);   // Lighter orange
    static final Color ACCENT_COLOR = new Color(180, 210, 255);      // Lighter blue
    static final Color BACKGROUND_COLOR = new Color(245, 245, 245);  // Light gray
    static final Color TEXT_COLOR = new Color(33, 33, 33);           // Dark gray
    static final Color SELECTION_COLOR = new Color(200, 255, 200);   // Highlight color for selection

    private JFrame frame;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryFilter;
    private JTextField searchField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new BuyerDashboard().initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initialize() {
        // Create and setup the main frame
        frame = new JFrame("Fresh Market");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Use a modern layout manager
        frame.setLayout(new BorderLayout());

        // Add components to the frame
        createHeaderPanel();
        createMainPanel();
        createFooterPanel();

        frame.setVisible(true);
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(20, 120, 60));  // Darker green for header
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Logo and Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);

        try {
            ImageIcon logoIcon = new ImageIcon(ImageIO.read(new File("resources/fresh_market_logo.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            JLabel logoLabel = new JLabel(logoIcon);
            titlePanel.add(logoLabel);
        } catch (IOException e) {
            // If logo not found, use a text placeholder
            JLabel logoText = new JLabel("🍎");
            logoText.setFont(new Font("Arial", Font.BOLD, 30));
            logoText.setForeground(Color.WHITE);
            titlePanel.add(logoText);
        }

        JLabel titleLabel = new JLabel("Fresh Market");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // User info and logout button
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Buyer Dashboard");
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setForeground(Color.WHITE);
        userPanel.add(userLabel);

        // Add logout button with more prominent styling
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(200, 50, 50));
        logoutButton.setForeground(Color.BLACK);  // Changed text color to black
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.addActionListener(e -> logout());
        userPanel.add(logoutButton);

        headerPanel.add(userPanel, BorderLayout.EAST);

        frame.add(headerPanel, BorderLayout.NORTH);
    }

    private void createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(BACKGROUND_COLOR);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        searchPanel.add(categoryLabel);

        categoryFilter = new JComboBox<>(new String[]{"All", "Fruits", "Vegetables", "Grains"});
        categoryFilter.setPreferredSize(new Dimension(150, 30));
        categoryFilter.setFont(new Font("Arial", Font.PLAIN, 14));
        searchPanel.add(categoryFilter);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setForeground(new Color(0, 0, 0));         // Black text
        searchField.setBackground(new Color(255, 255, 255));   // White background
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(ACCENT_COLOR);
        searchButton.setForeground(Color.BLACK);
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setFocusPainted(false);
        searchButton.setBorder(new EmptyBorder(5, 15, 5, 15));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchPanel.add(searchButton);

        // Add refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(100, 180, 220));
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(new EmptyBorder(5, 15, 5, 15));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> refreshProducts());
        searchPanel.add(refreshButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Products table
        String[] columnNames = {"ID", "Product", "Category", "Price ($)", "Available", "Farmer ID"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);
        productTable.setRowHeight(30);
        productTable.setFont(new Font("Arial", Font.PLAIN, 14));
        productTable.setSelectionBackground(SELECTION_COLOR);

        // Set cell renderer to maintain selection visibility
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(SELECTION_COLOR);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                }
                return c;
            }
        };

        // Apply the renderer to all columns
        for (int i = 0; i < productTable.getColumnCount(); i++) {
            productTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Change table header font to black color
        JTableHeader header = productTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.BLACK);

        JScrollPane tableScrollPane = new JScrollPane(productTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(BACKGROUND_COLOR);

        JButton addToCartButton = createStyledButton("Add to Cart", PRIMARY_COLOR);
        JButton orderButton = createStyledButton("Place Order", SECONDARY_COLOR);

        // Make buttons larger and more noticeable
        Dimension buttonSize = new Dimension(150, 40);
        addToCartButton.setPreferredSize(buttonSize);
        orderButton.setPreferredSize(buttonSize);

        actionPanel.add(addToCartButton);
        actionPanel.add(orderButton);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.CENTER);

        // Load initial data
        loadProducts("All", "");

        // Event handling
        categoryFilter.addActionListener(e ->
                loadProducts((String)categoryFilter.getSelectedItem(), searchField.getText()));

        searchButton.addActionListener(e ->
                loadProducts((String)categoryFilter.getSelectedItem(), searchField.getText()));

        addToCartButton.addActionListener(e -> addToCart());
        orderButton.addActionListener(e -> placeOrder());
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(50, 50, 50), 1),
                new EmptyBorder(8, 20, 8, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(
                        Math.min(bgColor.getRed() + 20, 255),
                        Math.min(bgColor.getGreen() + 20, 255),
                        Math.min(bgColor.getBlue() + 20, 255)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel footerLabel = new JLabel("© 2025 Fresh Market - Buy Fresh, Stay Healthy");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(new Color(100, 100, 100));
        footerPanel.add(footerLabel);

        frame.add(footerPanel, BorderLayout.SOUTH);
    }

    private void loadProducts(String category, String keyword) {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String query = "SELECT product_id, name, category, price, quantity, farmer_id FROM PRODUCTS WHERE quantity > 0";
            if (!category.equals("All")) {
                query += " AND UPPER(category) = UPPER(?)";
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
                tableModel.addRow(new Object[]{
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        String.format("%.2f", rs.getDouble("price")),
                        rs.getInt("quantity"),
                        rs.getInt("farmer_id")
                });
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frame,
                        "No products found matching your criteria",
                        "Search Results",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            showErrorDialog("Database Error", ex.getMessage());
        }
    }

    private void refreshProducts() {
        // Clear search field
        searchField.setText("");
        // Reset category filter to "All"
        categoryFilter.setSelectedItem("All");
        // Reload products
        loadProducts("All", "");
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            frame.dispose();
            // Here you would typically redirect to login screen
            JOptionPane.showMessageDialog(null,
                    "You have been logged out successfully!",
                    "Logout Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            // Redirect to login screen (uncomment if you have a login class)
            // new LoginScreen().initialize();
            System.exit(0); // For now, just exit the application
        }
    }

    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            showErrorDialog("Selection Required", "Please select a product first.");
            return;
        }

        int productId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        String productName = tableModel.getValueAt(selectedRow, 1).toString();

        JPanel panel = createFormPanel();
        JTextField nameField = addFormField(panel, "Your Name:");
        JTextField quantityField = addFormField(panel, "Quantity:");
        quantityField.setName("quantity"); // Set name for validation

        // Add product name label for clarity
        JLabel productLabel = new JLabel("Selected Product: " + productName);
        productLabel.setFont(new Font("Arial", Font.BOLD, 14));
        productLabel.setForeground(new Color(20, 120, 60));
        panel.add(productLabel, 0);
        panel.add(Box.createRigidArea(new Dimension(0, 10)), 1);

        // Use standard JOptionPane with custom buttons
        int result = JOptionPane.showConfirmDialog(
                frame, panel, "Add to Cart",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (validateFields(nameField, quantityField)) {
                try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
                    String insert = "INSERT INTO CART (product_id, quantity, buyer_name) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(insert);
                    stmt.setInt(1, productId);
                    stmt.setInt(2, Integer.parseInt(quantityField.getText().trim()));
                    stmt.setString(3, nameField.getText().trim());
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(frame,
                            "Product added to your cart!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Maintain selection after operation
                    productTable.setRowSelectionInterval(selectedRow, selectedRow);
                } catch (Exception ex) {
                    showErrorDialog("Database Error", ex.getMessage());
                }
            }
        }

        // Maintain selection even if canceled
        productTable.setRowSelectionInterval(selectedRow, selectedRow);
    }

    private void placeOrder() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            showErrorDialog("Selection Required", "Please select a product to order.");
            return;
        }

        int productId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        String productName = tableModel.getValueAt(selectedRow, 1).toString();

        JPanel panel = createFormPanel();
        JTextField nameField = addFormField(panel, "Your Name:");
        JTextField contactField = addFormField(panel, "Contact (must start with 07, 10 digits):");
        JTextField addressField = addFormField(panel, "Delivery Address:");
        JTextField quantityField = addFormField(panel, "Quantity:");
        quantityField.setName("quantity"); // Set name for validation

        // Add product name label for clarity
        JLabel productLabel = new JLabel("Selected Product: " + productName);
        productLabel.setFont(new Font("Arial", Font.BOLD, 14));
        productLabel.setForeground(new Color(20, 120, 60));
        panel.add(productLabel, 0);
        panel.add(Box.createRigidArea(new Dimension(0, 10)), 1);

        // Use standard JOptionPane with custom buttons
        int result = JOptionPane.showConfirmDialog(
                frame, panel, "Place Order",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Validate phone number
            String contact = contactField.getText().trim();
            if (!contact.startsWith("07") || contact.length() != 10 || !contact.matches("\\d+")) {
                showErrorDialog("Invalid Input", "Contact number must start with 07 and be exactly 10 digits.");
                // Maintain selection
                productTable.setRowSelectionInterval(selectedRow, selectedRow);
                return;
            }

            if (validateFields(nameField, addressField, quantityField)) {
                try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
                    String insert = "INSERT INTO ORDERED (product_id, quantity, buyer_name, delivery_address, contact_number) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(insert);
                    stmt.setInt(1, productId);
                    stmt.setInt(2, Integer.parseInt(quantityField.getText().trim()));
                    stmt.setString(3, nameField.getText().trim());
                    stmt.setString(4, addressField.getText().trim());
                    stmt.setString(5, contact);
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(frame,
                            "Your order has been placed successfully!",
                            "Order Confirmed",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Maintain selection after operation
                    productTable.setRowSelectionInterval(selectedRow, selectedRow);
                } catch (Exception ex) {
                    showErrorDialog("Database Error", ex.getMessage());
                }
            }
        }

        // Maintain selection even if canceled
        productTable.setRowSelectionInterval(selectedRow, selectedRow);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 5, 10));
        panel.setBackground(new Color(245, 245, 245));  // Light background
        return panel;
    }

    private JTextField addFormField(JPanel panel, String labelText) {
        JPanel fieldPanel = new JPanel(new BorderLayout(5, 5));
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        fieldPanel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);

        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(new Color(0, 0, 0));         // Black text
        field.setBackground(new Color(255, 255, 255));   // White background
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));

        fieldPanel.add(label, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);

        panel.add(fieldPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        return field;
    }

    private boolean validateFields(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                showErrorDialog("Missing Information", "Please fill in all required fields.");
                return false;
            }
        }

        // Validate quantity is a positive number
        try {
            for (JTextField field : fields) {
                if (field.getName() != null && field.getName().equals("quantity")) {
                    int quantity = Integer.parseInt(field.getText().trim());
                    if (quantity <= 0) {
                        showErrorDialog("Invalid Input", "Quantity must be greater than zero.");
                        return false;
                    }
                }
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid Input", "Please enter a valid number for quantity.");
            return false;
        }

        return true;
    }

    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(
                frame, message, title,
                JOptionPane.ERROR_MESSAGE);
    }
}