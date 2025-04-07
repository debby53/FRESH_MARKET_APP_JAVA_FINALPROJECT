package FRESHMARKETAPP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JFrame {

    private JButton btnGetStarted;

    public WelcomeScreen() {
        // Set the frame properties
        setTitle("Fresh Market App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Add a background image (optional)
        setContentPane(new JLabel(new ImageIcon("src/resources/farm-background.png")));
        setLayout(new BorderLayout());

        // Create a JPanel for the title and button
        JPanel panel = new JPanel();
        panel.setOpaque(false); // Make the panel transparent
        panel.setLayout(new BorderLayout());

        // Add a title label
        JLabel label = new JLabel("Welcome to Fresh Market App", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 36));
        label.setForeground(Color.WHITE);
        panel.add(label, BorderLayout.CENTER);

        // Create a button for "Get Started"
        btnGetStarted = new JButton("Get Started");
        btnGetStarted.setFont(new Font("Arial", Font.PLAIN, 18));
        btnGetStarted.setForeground(Color.BLACK);
        btnGetStarted.setBackground(new Color(255, 223, 186));  // Soft color for the button

        // Add action listener to handle button click
        btnGetStarted.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // When button is clicked, navigate to the login screen
                new LoginPhase(); // Create an instance of LoginPhase (no need to use setVisible(true) here)
                dispose();  // Close the welcome screen
            }
        });

        panel.add(btnGetStarted, BorderLayout.SOUTH);

        // Add the panel to the frame
        add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WelcomeScreen().setVisible(true);  // Show the welcome screen
            }
        });
    }
}
