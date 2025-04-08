package FRESHMARKETAPP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartupPage extends JFrame {

    public StartupPage() {
        // Frame settings
        setTitle("Fresh Market App - Welcome");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load and set background image
        ImageIcon backgroundImage = new ImageIcon("src/resource/img.png"); // Change path if needed
        JLabel background = new JLabel(backgroundImage);
        background.setLayout(new BorderLayout());
        setContentPane(background);

        // Transparent panel to hold welcome text and button
        JPanel overlayPanel = new JPanel();
        overlayPanel.setOpaque(false);
        overlayPanel.setLayout(new BoxLayout(overlayPanel, BoxLayout.Y_AXIS));
        overlayPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to Fresh Market App");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 32));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button
        JButton getStartedButton = new JButton("Let's Get Started");
        getStartedButton.setFont(new Font("Arial", Font.BOLD, 20));
        getStartedButton.setBackground(new Color(255, 222, 89)); // Nice warm color
        getStartedButton.setFocusPainted(false);
        getStartedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        getStartedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Button action
        getStartedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPhase(); // Launch login
                dispose();        // Close this window
            }
        });

        // Add components to panel
        overlayPanel.add(Box.createVerticalGlue());
        overlayPanel.add(welcomeLabel);
        overlayPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        overlayPanel.add(getStartedButton);
        overlayPanel.add(Box.createVerticalGlue());

        // Add overlay panel to background
        background.add(overlayPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartupPage startup = new StartupPage();
            startup.setVisible(true);
        });
    }
}
