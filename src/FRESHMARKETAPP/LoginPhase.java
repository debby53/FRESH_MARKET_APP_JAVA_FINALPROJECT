package FRESHMARKETAPP;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

public class LoginPhase extends JFrame {
    private static final String dbUrl = "jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP";
    private static final String dbUsername = "deborah";
    private static final String dbPassword = "12345";

    // Custom colors
    private final Color GREEN_600 = new Color(22, 163, 74);
    private final Color GREEN_700 = new Color(21, 128, 61);
    private final Color WHITE = new Color(255, 255, 255);
    private final Color GREEN_50 = new Color(240, 253, 244);
    private final Color BLUE_50 = new Color(239, 246, 255);
    private final Color GRAY_600 = new Color(75, 85, 99);
    private final Color GRAY_700 = new Color(55, 65, 81);
    private final Color GRAY_300 = new Color(209, 213, 219);
    private final Color GRAY_800 = new Color(31, 41, 55);
    private final Color BLACK = new Color(0, 0, 0); // Added black color

    // Components
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkShowPassword;
    private JComboBox<String> roleComboBox;
    private JButton btnLogin;
    private JButton btnForgotPassword;
    private JLabel lblRegister;

    public LoginPhase() {
        setTitle("Fresh Market Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, GREEN_50, w, h, BLUE_50);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Content panel with split pane effect
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        contentPanel.setMaximumSize(new Dimension(850, 500));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(new RoundedBorder(15));

        // Left panel - Branding section
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(GREEN_600);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Branding components
        JLabel lblTitle = new JLabel("Fresh Market");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitle.setForeground(WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTagline = new JLabel("Farm to Table, Fresh Every Day");
        lblTagline.setFont(new Font("Arial", Font.PLAIN, 18));
        lblTagline.setForeground(WHITE);
        lblTagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Value proposition panel
        JPanel propositionPanel = new JPanel();
        propositionPanel.setLayout(new BoxLayout(propositionPanel, BoxLayout.Y_AXIS));
        propositionPanel.setBackground(new Color(255, 255, 255, 50)); // semi-transparent white
        propositionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        propositionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        propositionPanel.setMaximumSize(new Dimension(400, 200));

        JLabel lblProposition = new JLabel("Connect directly with local farmers or find the freshest produce as a buyer.");
        lblProposition.setForeground(WHITE);
        lblProposition.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel bulletPoints = new JPanel();
        bulletPoints.setLayout(new BoxLayout(bulletPoints, BoxLayout.Y_AXIS));
        bulletPoints.setOpaque(false);
        bulletPoints.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        String[] points = {
                "Support local agriculture",
                "Discover seasonal offers",
                "Guaranteed freshness"
        };

        for (String point : points) {
            JPanel bulletPoint = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            bulletPoint.setOpaque(false);

            JPanel bullet = new JPanel();
            bullet.setPreferredSize(new Dimension(8, 8));
            bullet.setBackground(WHITE);
            bullet.setBorder(new RoundedBorder(4));

            JLabel lblPoint = new JLabel(point);
            lblPoint.setForeground(WHITE);
            lblPoint.setFont(new Font("Arial", Font.PLAIN, 14));

            bulletPoint.add(bullet);
            bulletPoint.add(lblPoint);
            bulletPoints.add(bulletPoint);
        }

        propositionPanel.add(lblProposition);
        propositionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        propositionPanel.add(bulletPoints);

        // Add components to left panel
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(lblTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(lblTagline);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftPanel.add(propositionPanel);
        leftPanel.add(Box.createVerticalGlue());

        // Right panel - Login form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Form title
        JLabel lblFormTitle = new JLabel("Login to your account");
        lblFormTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblFormTitle.setForeground(GRAY_800);

        // Username field with icon
        JPanel usernamePanel = new JPanel(new BorderLayout(10, 0));
        usernamePanel.setOpaque(false);
        usernamePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsername.setForeground(GRAY_700);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(5, GRAY_300),
                BorderFactory.createEmptyBorder(8, 40, 8, 10)
        ));

        // Username icon panel
        JPanel usernameIconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        usernameIconPanel.setOpaque(false);
        JLabel lblUsernameIcon = new JLabel("\uD83D\uDC64"); // User emoji
        lblUsernameIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        lblUsernameIcon.setForeground(GRAY_600);
        usernameIconPanel.add(lblUsernameIcon);

        JLayeredPane usernameLayeredPane = new JLayeredPane();
        usernameLayeredPane.setPreferredSize(new Dimension(400, 40));

        txtUsername.setBounds(0, 0, 400, 40);
        usernameIconPanel.setBounds(0, 0, 400, 40);

        usernameLayeredPane.add(txtUsername, JLayeredPane.DEFAULT_LAYER);
        usernameLayeredPane.add(usernameIconPanel, JLayeredPane.PALETTE_LAYER);

        JPanel usernameFieldPanel = new JPanel(new BorderLayout());
        usernameFieldPanel.setOpaque(false);
        usernameFieldPanel.add(usernameLayeredPane, BorderLayout.CENTER);

        usernamePanel.add(lblUsername, BorderLayout.NORTH);
        usernamePanel.add(usernameFieldPanel, BorderLayout.CENTER);

        // Password field with icon and show/hide option
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
        passwordPanel.setOpaque(false);
        passwordPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPassword.setForeground(GRAY_700);

        txtPassword = new JPasswordField();
        txtPassword.setEchoChar('•');
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(5, GRAY_300),
                BorderFactory.createEmptyBorder(8, 40, 8, 10)
        ));

        // Password icon panel
        JPanel passwordIconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        passwordIconPanel.setOpaque(false);
        JLabel lblPasswordIcon = new JLabel("\uD83D\uDD12"); // Lock emoji
        lblPasswordIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        lblPasswordIcon.setForeground(GRAY_600);
        passwordIconPanel.add(lblPasswordIcon);

        // Password visibility toggle
        JPanel passwordVisibilityPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        passwordVisibilityPanel.setOpaque(false);
        JLabel lblEyeIcon = new JLabel("\uD83D\uDC41"); // Eye emoji
        lblEyeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        lblEyeIcon.setForeground(GRAY_600);
        lblEyeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        passwordVisibilityPanel.add(lblEyeIcon);

        JLayeredPane passwordLayeredPane = new JLayeredPane();
        passwordLayeredPane.setPreferredSize(new Dimension(400, 40));

        txtPassword.setBounds(0, 0, 400, 40);
        passwordIconPanel.setBounds(0, 0, 400, 40);
        passwordVisibilityPanel.setBounds(0, 0, 400, 40);

        passwordLayeredPane.add(txtPassword, JLayeredPane.DEFAULT_LAYER);
        passwordLayeredPane.add(passwordIconPanel, JLayeredPane.PALETTE_LAYER);
        passwordLayeredPane.add(passwordVisibilityPanel, JLayeredPane.MODAL_LAYER);

        JPanel passwordFieldPanel = new JPanel(new BorderLayout());
        passwordFieldPanel.setOpaque(false);
        passwordFieldPanel.add(passwordLayeredPane, BorderLayout.CENTER);

        lblEyeIcon.addMouseListener(new MouseAdapter() {
            boolean visible = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                visible = !visible;
                txtPassword.setEchoChar(visible ? (char) 0 : '•');
                lblEyeIcon.setText(visible ? "\uD83D\uDC41\u200D\uD83D\uDDE8" : "\uD83D\uDC41"); // Eye with slash when hidden
            }
        });

        chkShowPassword = new JCheckBox("Show Password");
        chkShowPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        chkShowPassword.setForeground(GRAY_600);
        chkShowPassword.setBackground(WHITE);
        chkShowPassword.addActionListener(e -> txtPassword.setEchoChar(chkShowPassword.isSelected() ? (char) 0 : '•'));

        passwordPanel.add(lblPassword, BorderLayout.NORTH);
        passwordPanel.add(passwordFieldPanel, BorderLayout.CENTER);
        passwordPanel.add(chkShowPassword, BorderLayout.SOUTH);

        // Role selection
        JPanel rolePanel = new JPanel(new BorderLayout(10, 0));
        rolePanel.setOpaque(false);
        rolePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        JLabel lblRole = new JLabel("Login as");
        lblRole.setFont(new Font("Arial", Font.PLAIN, 14));
        lblRole.setForeground(GRAY_700);

        String[] roles = {"select","Farmer", "Buyer"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        roleComboBox.setBorder(new RoundedBorder(5, GRAY_300));
        roleComboBox.setBackground(WHITE);

        JPanel roleComboBorder = new JPanel(new BorderLayout());
        roleComboBorder.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        roleComboBorder.setOpaque(false);
        roleComboBorder.add(roleComboBox, BorderLayout.CENTER);

        rolePanel.add(lblRole, BorderLayout.NORTH);
        rolePanel.add(roleComboBorder, BorderLayout.CENTER);

        // Login button - CHANGED TEXT COLOR TO BLACK
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setForeground(BLACK); // Changed from WHITE to BLACK for better visibility
        btnLogin.setBackground(GREEN_600);
        btnLogin.setBorder(new RoundedBorder(5));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(400, 40));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(GREEN_700);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(GREEN_600);
            }
        });

        // Forgot password button
        btnForgotPassword = new JButton("Forgot Password?");
        btnForgotPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        btnForgotPassword.setForeground(GREEN_600);
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setFocusPainted(false);
        btnForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgotPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnForgotPassword.setForeground(GREEN_700);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnForgotPassword.setForeground(GREEN_600);
            }
        });

        // Register link
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerPanel.setOpaque(false);

        JLabel lblNoAccount = new JLabel("Don't have an account? ");
        lblNoAccount.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNoAccount.setForeground(GRAY_600);

        lblRegister = new JLabel("Register");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 12));
        lblRegister.setForeground(GREEN_600);
        lblRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblRegister.setForeground(GREEN_700);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblRegister.setForeground(GREEN_600);
            }
        });

        registerPanel.add(lblNoAccount);
        registerPanel.add(lblRegister);

        // Add all components to right panel
        rightPanel.add(lblFormTitle);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(usernamePanel);
        rightPanel.add(passwordPanel);
        rightPanel.add(rolePanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(btnLogin);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(btnForgotPassword);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(registerPanel);
        rightPanel.add(Box.createVerticalGlue());

        // Add panels to content panel
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);

        // Add to main panel with centering
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(contentPanel);
        mainPanel.add(Box.createVerticalGlue());

        // Add to frame
        add(mainPanel);

        // Add action listeners
        btnLogin.addActionListener(e -> login());
        btnForgotPassword.addActionListener(e -> forgotPassword());
        lblRegister.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                new RegisterFrame().setVisible(true);
                dispose();
            }
        });

        setVisible(true);
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();

        // Validation: empty fields
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter both username and password!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Additional validation: check input length
        if (username.length() > 50 || password.length() > 50) {
            JOptionPane.showMessageDialog(null, "Username or password is too long!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM USERS WHERE username=? AND password=? AND role=?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Login Successful!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    if ("Farmer".equals(role)) {
                        FarmerDashboard.main(null);
                    } else {
                        BuyerDashboard.main(null);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username, password, or role!",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + sqlEx.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unexpected Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // In case a user forgot password
    private void forgotPassword() {
        try {
            String username = JOptionPane.showInputDialog(null, "Enter your username:");
            if (username == null || username.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username cannot be empty!",
                        "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                 PreparedStatement stmt = conn.prepareStatement("SELECT password FROM USERS WHERE username=?")) {

                stmt.setString(1, username.trim());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String password = rs.getString("password");
                        JOptionPane.showMessageDialog(null, "Your password is: " + password,
                                "Password Recovery", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Username not found!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + sqlEx.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unexpected Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom rounded border class
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        RoundedBorder(int radius) {
            this(radius, null);
        }

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (color != null) {
                g2.setColor(color);
            } else {
                g2.setColor(c.getBackground().darker());
            }

            g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = radius / 2;
            return insets;
        }
    }

    public static void main(String[] args) {
        try {
            // Set cross-platform look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginPhase());
    }
}