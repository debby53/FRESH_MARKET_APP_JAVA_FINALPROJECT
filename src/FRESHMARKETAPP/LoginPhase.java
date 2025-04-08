package FRESHMARKETAPP;

import FRESHMARKETAPP.BuyerDashboard;
import FRESHMARKETAPP.FarmerDashboard;
import FRESHMARKETAPP.RegisterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginPhase extends JFrame {
    private static String dbUrl = "jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP";
    private static String dbUsername = "deborah";
    private static String dbPassword = "12345";

    public LoginPhase() {
        setTitle("Fresh Market Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(null);

        JLabel lblName = new JLabel("Username:");
        lblName.setBounds(30, 30, 100, 25);
        add(lblName);

        JTextField txtName = new JTextField();
        txtName.setBounds(150, 30, 200, 25);
        add(txtName);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(30, 70, 100, 25);
        add(lblPassword);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 70, 200, 25);
        add(txtPassword);

        JCheckBox chkShowPassword = new JCheckBox("Show Password");
        chkShowPassword.setBounds(150, 100, 150, 25);
        add(chkShowPassword);

        JLabel lblRole = new JLabel("Login as:");
        lblRole.setBounds(30, 130, 100, 25);
        add(lblRole);

        String[] roles = {"Farmer", "Buyer"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(150, 130, 200, 25);
        add(roleComboBox);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(50, 180, 100, 30);
        add(btnLogin);

        JButton btnForgotPassword = new JButton("Forgot Password?");
        btnForgotPassword.setBounds(180, 180, 150, 30);
        add(btnForgotPassword);

        JLabel lblRegister = new JLabel("<html><a href='#'>Don't have an account? Register</a></html>");
        lblRegister.setBounds(100, 230, 250, 25);
        lblRegister.setForeground(Color.BLUE);
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(lblRegister);

        // Event listeners...
        chkShowPassword.addActionListener(e -> {
            txtPassword.setEchoChar(chkShowPassword.isSelected() ? (char) 0 : '•');
        });

        btnForgotPassword.addActionListener(e -> forgotPassword());

        lblRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new RegisterFrame().setVisible(true);
                dispose();
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtName.getText();
                String password = new String(txtPassword.getPassword());
                String role = roleComboBox.getSelectedItem().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both username and password!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM USERS WHERE username=? AND password=? AND role=?")) {

                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    stmt.setString(3, role);

                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        if (role.equals("Farmer")) {
                            FarmerDashboard.main(null);
                        } else {
                            BuyerDashboard.main(null);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void forgotPassword() {
        String username = JOptionPane.showInputDialog(null, "Enter your username:");
        if (username == null || username.isEmpty()) return;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement stmt = conn.prepareStatement("SELECT password FROM USERS WHERE username=?")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String password = rs.getString("password");
                JOptionPane.showMessageDialog(null, "Your password is: " + password, "Password Recovery", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Username not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error!", "Error" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
