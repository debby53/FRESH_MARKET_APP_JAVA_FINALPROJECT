package FRESHMARKETAPP;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class StartupPage extends JFrame {

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(42, 157, 63);    // Green
    private static final Color ACCENT_COLOR = new Color(255, 193, 7);     // Amber/Gold
    private static final Color TEXT_LIGHT = new Color(255, 255, 255);     // White
    private static final Color TEXT_DARK = new Color(33, 33, 33);         // Dark Gray

    public StartupPage() {
        // Frame settings with modern design
        setTitle("Fresh Market");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(800, 500));
        // Set undecorated for opacity effects
        setUndecorated(true);

        // Custom main panel with gradient background as fallback for image
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // Create gradient background (in case image fails to load)
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(34, 139, 34),  // Forest Green
                        getWidth(), getHeight(), new Color(20, 80, 20) // Darker Green
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Create a layered pane that will contain all our components
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null); // Use null layout for precise positioning

        // Try to load and set background image
        try {
            Image backgroundImg = ImageIO.read(new File("src/resource/img.png"))
                    .getScaledInstance(900, 600, Image.SCALE_SMOOTH);
            JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImg));
            backgroundLabel.setBounds(0, 0, 900, 600);

            // Add background to the bottom layer
            layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

            // Create semi-transparent overlay
            JPanel overlay = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(new Color(0, 0, 0, 80)); // Semi-transparent black
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.dispose();
                }
            };
            overlay.setOpaque(false);
            overlay.setBounds(0, 0, 900, 600);
            layeredPane.add(overlay, JLayeredPane.PALETTE_LAYER);

            // Create content panel for the top layer
            JPanel contentPanel = createContentPanel();
            contentPanel.setBounds(0, 0, 900, 600);
            layeredPane.add(contentPanel, JLayeredPane.DRAG_LAYER);

            // Set the layered pane as content pane
            mainPanel.add(layeredPane, BorderLayout.CENTER);
            setContentPane(mainPanel);
        } catch (IOException e) {
            // If image loading fails, use the gradient background
            System.out.println("Background image not found. Using gradient background.");

            // Create semi-transparent overlay
            JPanel overlay = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(new Color(0, 0, 0, 80)); // Semi-transparent black
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.dispose();
                }
            };
            overlay.setOpaque(false);
            overlay.setBounds(0, 0, 900, 600);
            layeredPane.add(overlay, JLayeredPane.PALETTE_LAYER);

            // Create content panel for the top layer
            JPanel contentPanel = createContentPanel();
            contentPanel.setBounds(0, 0, 900, 600);
            layeredPane.add(contentPanel, JLayeredPane.DRAG_LAYER);

            // Set the layered pane as content pane
            mainPanel.add(layeredPane, BorderLayout.CENTER);
            setContentPane(mainPanel);
        }
    }

    // This method is no longer needed as we've restructured the UI with layered panes
    private void addCloseButton(JLayeredPane layeredPane) {
        // Add a close button since we're using an undecorated frame
        JButton closeButton = new JButton("X");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setForeground(TEXT_LIGHT);
        closeButton.setBackground(new Color(255, 0, 0, 150));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setOpaque(true);
        closeButton.setBounds(getWidth() - 50, 10, 30, 30);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        closeButton.addActionListener(e -> System.exit(0));
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(new Color(220, 0, 0, 200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(new Color(255, 0, 0, 150));
            }
        });

        layeredPane.add(closeButton, JLayeredPane.DRAG_LAYER + 1);
    }

    // New method to create content panel
    private JPanel createContentPanel() {
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));

        // Logo and headline section
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Logo (create text logo as fallback)
        JLabel logoLabel = new JLabel("🍎 🥕 🥦");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 48));
        logoLabel.setForeground(TEXT_LIGHT);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Welcome message
        JLabel titleLabel = new JLabel("FRESH MARKET");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 42));
        titleLabel.setForeground(TEXT_LIGHT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Farm Fresh Produce Delivered to Your Door");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitleLabel.setForeground(TEXT_LIGHT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to header panel
        headerPanel.add(logoLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(subtitleLabel);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        // Custom styled button
        JButton getStartedButton = createStyledButton("Let's Get Started", ACCENT_COLOR);
        getStartedButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add hover effect
        getStartedButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                getStartedButton.setBackground(new Color(
                        Math.min(ACCENT_COLOR.getRed() + 20, 255),
                        Math.min(ACCENT_COLOR.getGreen() + 20, 255),
                        Math.min(ACCENT_COLOR.getBlue() + 20, 255)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                getStartedButton.setBackground(ACCENT_COLOR);
            }
        });

        // Button action
        getStartedButton.addActionListener(e -> {
            showButtonClickEffect(getStartedButton);

            // Perform transition with fade effect
            Timer timer = new Timer(20, new ActionListener() {
                float opacity = 1.0f;

                @Override
                public void actionPerformed(ActionEvent e) {
                    opacity -= 0.05f;
                    if (opacity <= 0) {
                        ((Timer)e.getSource()).stop();
                        new LoginPhase();
                        dispose();
                    } else {
                        try {
                            setOpacity(opacity);
                        } catch (Exception ex) {
                            // If opacity setting fails, just close immediately
                            ((Timer)e.getSource()).stop();
                            new LoginPhase();
                            dispose();
                        }
                    }
                }
            });
            timer.start();
        });

        // Additional info text
        JLabel infoLabel = new JLabel("Connect with local farmers and enjoy fresh produce");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        infoLabel.setForeground(TEXT_LIGHT);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to button panel
        buttonPanel.add(getStartedButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonPanel.add(infoLabel);

        // Add panels to main content panel
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());

        // Return the content panel
        return contentPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Create rounded rectangle button shape
                RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(getBackground());
                g2.fill(roundedRectangle);

                // Add subtle gradient
                GradientPaint gradient = new GradientPaint(
                        0, 0, getBackground().brighter(),
                        0, getHeight(), getBackground().darker()
                );
                g2.setPaint(gradient);
                g2.fill(roundedRectangle);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        // Button styling
        button.setBackground(bgColor);
        button.setForeground(TEXT_DARK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 60));
        button.setMaximumSize(new Dimension(250, 60));

        // Add subtle border
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 0, 0, 50), 1, true),
                new EmptyBorder(10, 25, 10, 25)
        ));

        return button;
    }

    private void showButtonClickEffect(JButton button) {
        // Create a temporary color for click effect
        Color originalColor = button.getBackground();
        button.setBackground(originalColor.darker());

        // Reset after short delay
        Timer timer = new Timer(100, e -> button.setBackground(originalColor));
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        try {
            // Use system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start the application
        SwingUtilities.invokeLater(() -> {
            StartupPage startupPage = new StartupPage();

            try {
                // Show window with fade-in effect
                startupPage.setOpacity(0.0f);
                startupPage.setVisible(true);

                // Fade in animation
                Timer timer = new Timer(20, new ActionListener() {
                    float opacity = 0.0f;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        opacity += 0.05f;
                        if (opacity >= 1.0f) {
                            opacity = 1.0f;
                            ((Timer)e.getSource()).stop();
                        }
                        try {
                            startupPage.setOpacity(opacity);
                        } catch (Exception ex) {
                            ((Timer)e.getSource()).stop();
                            // Just show the window without fade if opacity fails
                            startupPage.setOpacity(1.0f);
                        }
                    }
                });
                timer.start();
            } catch (Exception e) {
                // If opacity is not supported, just show the window normally
                startupPage.setOpacity(1.0f);
                startupPage.setVisible(true);
                System.out.println("Window transparency not supported: " + e.getMessage());
            }
        });
    }
}