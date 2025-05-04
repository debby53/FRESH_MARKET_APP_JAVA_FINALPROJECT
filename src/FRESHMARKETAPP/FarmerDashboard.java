package FRESHMARKETAPP;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.net.URL;

public class FarmerDashboard {

    // Color theme
    private static final Color PRIMARY_COLOR = new Color(76, 175, 80);      // Green
    private static final Color SECONDARY_COLOR = new Color(139, 195, 74);   // Light Green
    private static final Color ACCENT_COLOR = new Color(255, 152, 0);       // Orange
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light Gray
    private static final Color TEXT_COLOR = new Color(33, 33, 33);          // Dark Gray
    private static final Color WHITE = new Color(255, 255, 255);            // White
    private static final Color BLACK = new Color(0, 0, 0);                  // Black

    // Replace with your actual DB credentials
    static final String dbUrl = "jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP";
    static final String dbUsername = "deborah";
    static final String dbPassword = "12345";

    // Font definitions
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public static void main(String[] args) {
        try {
            // Set the look and feel to the system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Fresh Market - Farmer Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Create main panels
        JPanel headerPanel = createHeaderPanel();
        JPanel sidebarPanel = createSidebarPanel(frame);
        JPanel contentPanel = createContentPanel();

        // Main content split between sidebar and content
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel, contentPanel);
        splitPane.setDividerLocation(220);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);

        // Add components to frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);

        // Initialize table model and table
        String[] columnNames = {"Product ID", "Name", "Category", "Price", "Quantity"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        JTable productsTable = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(productsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(WHITE);

        // Set content panel's components
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel tableTitle = new JLabel("My Products");
        tableTitle.setFont(SUBTITLE_FONT);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        tableTitle.setForeground(BLACK); // Setting table title to black color

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(WHITE);

        JTextField searchField = new JTextField(20);
        searchField.setFont(REGULAR_FONT);
        styleTextField(searchField);

        JButton btnSearch = createStyledButton("Search", SECONDARY_COLOR);

        searchPanel.add(searchField);
        searchPanel.add(btnSearch);

        // Add action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(WHITE);

        JButton btnAddProduct = createStyledButton("Add Product", PRIMARY_COLOR);
        btnAddProduct.setIcon(loadIcon("/images/add.png", 16, 16));

        JButton btnRefresh = createStyledButton("Refresh", SECONDARY_COLOR);
        btnRefresh.setIcon(loadIcon("/images/refresh.png", 16, 16));

        actionPanel.add(btnRefresh);
        actionPanel.add(btnAddProduct);

        // Add components to table panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(WHITE);
        topPanel.add(tableTitle, BorderLayout.WEST);
        topPanel.add(actionPanel, BorderLayout.EAST);

        tablePanel.add(topPanel, BorderLayout.NORTH);
        tablePanel.add(searchPanel, BorderLayout.CENTER);
        tablePanel.add(scrollPane, BorderLayout.SOUTH);

        // Add table panel to content panel
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // Add Product Button Logic
        btnAddProduct.addActionListener(e -> addProductForm(frame, model));

        // Refresh Button Logic
        btnRefresh.addActionListener(e -> loadProducts(model));

        // Search Logic
        btnSearch.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            if (keyword.isEmpty()) {
                loadProducts(model);
            } else {
                searchProducts(model, keyword);
            }
        });

        // Create menu items
        JPanel menuProductsPanel = createMenuPanel("Products", "/images/products.png");
        JPanel menuOrdersPanel = createMenuPanel("Orders", "/images/orders.png");
        JPanel menuCartPanel = createMenuPanel("Cart", "/images/cart.png");
        JPanel menuLogoutPanel = createMenuPanel("Logout", "/images/logout.png");

        // Add menu items to sidebar
        sidebarPanel.add(menuProductsPanel);
        sidebarPanel.add(menuOrdersPanel);
        sidebarPanel.add(menuCartPanel);
        sidebarPanel.add(Box.createVerticalGlue()); // Push logout to bottom
        sidebarPanel.add(menuLogoutPanel);

        // Menu item action listeners
        menuProductsPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tableTitle.setText("My Products");
                model.setColumnIdentifiers(new String[]{"Product ID", "Name", "Category", "Price", "Quantity"});
                loadProducts(model);
            }
        });

        menuOrdersPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tableTitle.setText("Customer Orders");
                model.setColumnIdentifiers(new String[]{"Order ID", "Product ID", "Quantity", "Buyer Name", "Delivery Address", "Contact Number", "Order Date"});
                loadOrders(model);
            }
        });

        menuCartPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tableTitle.setText("Items in Cart");
                model.setColumnIdentifiers(new String[]{"Cart ID", "Product ID", "Quantity", "Buyer Name", "Added Date"});
                loadCart(model);
            }
        });

        menuLogoutPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                JOptionPane.showMessageDialog(null, "Logged out successfully!", "Logout", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Load initial data
        loadProducts(model);

        frame.setVisible(true);
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel logoLabel = new JLabel("Fresh Market");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoLabel.setForeground(WHITE);
        logoLabel.setIcon(loadIcon("/images/logo.png", 32, 32));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        JLabel farmerName = new JLabel("Welcome, Farmer John");
        farmerName.setFont(REGULAR_FONT);
        farmerName.setForeground(WHITE);
        farmerName.setIcon(loadIcon("/images/user.png", 24, 24));

        userPanel.add(farmerName);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private static JPanel createSidebarPanel(JFrame parentFrame) {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(51, 51, 51));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Add dashboard title
        JLabel dashboardLabel = new JLabel("Farmer Dashboard", JLabel.CENTER);
        dashboardLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        dashboardLabel.setForeground(WHITE);
        dashboardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dashboardLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        dashboardLabel.setIcon(loadIcon("/images/dashboard.png", 24, 24));

        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
        dashboardPanel.setOpaque(false);
        dashboardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dashboardPanel.add(dashboardLabel);

        sidebarPanel.add(dashboardPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        return sidebarPanel;
    }

    private static JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return contentPanel;
    }

    private static JPanel createMenuPanel(String title, String iconPath) {
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(new Color(51, 51, 51));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        menuPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel menuLabel = new JLabel(title);
        menuLabel.setFont(REGULAR_FONT);
        menuLabel.setForeground(WHITE);
        menuLabel.setIcon(loadIcon(iconPath, 20, 20));

        menuPanel.add(menuLabel);

        // Add hover effect
        menuPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuPanel.setBackground(new Color(75, 75, 75));
                menuPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuPanel.setBackground(new Color(51, 51, 51));
            }
        });

        return menuPanel;
    }

    private static JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(REGULAR_FONT);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(232, 245, 233));
        table.setSelectionForeground(TEXT_COLOR);

        // Style the header - Changed text color to BLACK
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(SECONDARY_COLOR);
        header.setForeground(BLACK); // Changed from WHITE to BLACK
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));

        // Add zebra striping
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? WHITE : new Color(245, 245, 245));
                }

                // Center align the text
                setHorizontalAlignment(JLabel.CENTER);

                return comp;
            }
        });

        return table;
    }

    private static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private static void styleTextField(JTextField textField) {
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    // Helper method to load icons
    private static ImageIcon loadIcon(String path, int width, int height) {
        // This is a placeholder method since we don't have actual icons
        // In a real application, you would load images from resources
        // For now, we'll return a colored square icon as a placeholder
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Fill with a color based on the path to create different icons
        if (path.contains("logo")) {
            g2d.setColor(new Color(255, 140, 0));
        } else if (path.contains("user")) {
            g2d.setColor(new Color(100, 181, 246));
        } else if (path.contains("dashboard")) {
            g2d.setColor(new Color(255, 215, 64));
        } else if (path.contains("products")) {
            g2d.setColor(new Color(129, 199, 132));
        } else if (path.contains("orders")) {
            g2d.setColor(new Color(159, 168, 218));
        } else if (path.contains("cart")) {
            g2d.setColor(new Color(255, 138, 128));
        } else if (path.contains("logout")) {
            g2d.setColor(new Color(239, 83, 80));
        } else if (path.contains("add")) {
            g2d.setColor(new Color(66, 165, 245));
        } else if (path.contains("refresh")) {
            g2d.setColor(new Color(156, 204, 101));
        } else {
            g2d.setColor(ACCENT_COLOR);
        }

        g2d.fillRoundRect(0, 0, width, height, 5, 5);
        g2d.dispose();

        return new ImageIcon(img);
    }

    // Search products from DB
    private static void searchProducts(DefaultTableModel model, String keyword) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            // Modified query: removed the CAST operation that was causing the error
            String sql = "SELECT product_id, name, category, price, quantity FROM PRODUCTS WHERE " +
                    "LOWER(name) LIKE ? OR LOWER(category) LIKE ? OR product_id = ?";

            // First try exact match for product_id if the keyword is numeric
            int productId = -1;
            try {
                productId = Integer.parseInt(keyword);
            } catch (NumberFormatException e) {
                // Not a number, that's fine
            }

            PreparedStatement stmt;
            if (productId > 0) {
                // If keyword is a valid number, use the productId directly
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, "%" + keyword + "%");
                stmt.setString(2, "%" + keyword + "%");
                stmt.setInt(3, productId);
            } else {
                // If not a valid number, use a modified query without the product_id check
                sql = "SELECT product_id, name, category, price, quantity FROM PRODUCTS WHERE " +
                        "LOWER(name) LIKE ? OR LOWER(category) LIKE ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, "%" + keyword + "%");
                stmt.setString(2, "%" + keyword + "%");
            }

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
            JOptionPane.showMessageDialog(null, "Error searching products:\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load Orders placed from DB
    private static void loadOrders(DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ORDER_ID, PRODUCT_ID, QUANTITY, BUYER_NAME, DELIVERY_ADDRESS, CONTACT_NUMBER, ORDER_DATE FROM ORDERED")) {

            model.setRowCount(0); // Clear table

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("ORDER_ID"),
                        rs.getInt("PRODUCT_ID"),
                        rs.getInt("QUANTITY"),
                        rs.getString("BUYER_NAME"),
                        rs.getString("DELIVERY_ADDRESS"),
                        rs.getString("CONTACT_NUMBER"),
                        rs.getDate("ORDER_DATE")
                };
                model.addRow(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading orders:\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load Cart items from DB
    private static void loadCart(DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT CART_ID, PRODUCT_ID, QUANTITY, BUYER_NAME, ADDED_DATE FROM CART")) {

            model.setRowCount(0); // Clear table

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("CART_ID"),
                        rs.getInt("PRODUCT_ID"),
                        rs.getInt("QUANTITY"),
                        rs.getString("BUYER_NAME"),
                        rs.getDate("ADDED_DATE")
                };
                model.addRow(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading cart:\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
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
            JOptionPane.showMessageDialog(null, "Error loading products:\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add product form with improved UI
    private static void addProductForm(JFrame parentFrame, DefaultTableModel model) {
        JDialog dialog = new JDialog(parentFrame, "Add New Product", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(WHITE);

        // Form title
        JLabel titleLabel = new JLabel("Add New Product");
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Form fields
        JLabel nameLabel = new JLabel("Product Name");
        nameLabel.setFont(REGULAR_FONT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField nameField = new JTextField();
        nameField.setFont(REGULAR_FONT);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleTextField(nameField);

        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setFont(REGULAR_FONT);
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] categories = {"Fruits", "Vegetables", "Dairy", "Grains", "Others"};
        JComboBox<String> categoryField = new JComboBox<>(categories);
        categoryField.setFont(REGULAR_FONT);
        categoryField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        categoryField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel priceLabel = new JLabel("Price ($)");
        priceLabel.setFont(REGULAR_FONT);
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField priceField = new JTextField();
        priceField.setFont(REGULAR_FONT);
        priceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        priceField.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleTextField(priceField);

        JLabel quantityLabel = new JLabel("Quantity");
        quantityLabel.setFont(REGULAR_FONT);
        quantityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField quantityField = new JTextField();
        quantityField.setFont(REGULAR_FONT);
        quantityField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        quantityField.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleTextField(quantityField);

        // Add components to form panel
        formPanel.add(titleLabel);

        formPanel.add(nameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(nameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(categoryLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(categoryField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(priceLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(priceField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        formPanel.add(quantityLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(quantityField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(WHITE);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(BUTTON_FONT);
        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setForeground(TEXT_COLOR);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JButton saveButton = createStyledButton("Save Product", PRIMARY_COLOR);

        buttonsPanel.add(cancelButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.add(saveButton);

        // Add action listeners
        cancelButton.addActionListener(e -> dialog.dispose());

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String category = (String) categoryField.getSelectedItem();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a product name", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                double price = Double.parseDouble(priceField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());

                if (price <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Price must be greater than zero", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Quantity must be greater than zero", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

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
                        dialog.dispose();
                        JOptionPane.showMessageDialog(parentFrame, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadProducts(model); // Refresh table
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "Database error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numeric values for Price and Quantity", "Validation Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}