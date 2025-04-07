package FRESHMARKETAPP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginPhase  extends JFrame{
    private static String dbUrl = "jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP";
    private static String dbUsername = "deborah";
    private static String dbPassword = "12345";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fresh Market Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(null);

        JLabel lblName = new JLabel("Username:");
        lblName.setBounds(30, 30, 100, 25);
        frame.add(lblName);

        JTextField txtName = new JTextField();
        txtName.setBounds(150, 30, 200, 25);
        frame.add(txtName);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(30, 70, 100, 25);
        frame.add(lblPassword);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 70, 200, 25);
        frame.add(txtPassword);

        JCheckBox chkShowPassword = new JCheckBox("Show Password");
        chkShowPassword.setBounds(150, 100, 150, 25);
        frame.add(chkShowPassword);

        JLabel lblRole = new JLabel("Login as:");
        lblRole.setBounds(30, 130, 100, 25);
        frame.add(lblRole);

        String[] roles = {"Farmer", "Buyer"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(150, 130, 200, 25);
        frame.add(roleComboBox);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(50, 180, 100, 30);
        frame.add(btnLogin);

        JButton btnForgotPassword = new JButton("Forgot Password?");
        btnForgotPassword.setBounds(180, 180, 150, 30);
        frame.add(btnForgotPassword);

        JLabel lblRegister = new JLabel("<html><a href='#'>Don't have an account? Register</a></html>");
        lblRegister.setBounds(100, 230, 250, 25);
        lblRegister.setForeground(Color.BLUE);
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.add(lblRegister);
        lblRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new RegisterFrame().setVisible(true);
                frame.dispose();
                // Open registration form here
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtName.getText();
                String password = new String(txtPassword.getPassword()); // Convert char[] to String
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
                        frame.dispose();
                        if(role.equals("Farmer")){
                            FarmerDashboard.main(null);
                        }
                        else if(role.equals("Buyer")){
                            BuyerDashboard.main(null);
                        }
                        // Open dashboard or next screen here
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid username, password, or role!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database Error!" +ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


//        JButton btnGmailLogin = new JButton("Continue with Gmail");
//        btnGmailLogin.setBounds(100, 300, 200, 40);
//        frame.add(btnGmailLogin);
//
//        btnGmailLogin.addActionListener(e -> openGmail());


        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        btnForgotPassword.addActionListener(e -> new LoginPhase().forgotPassword());

        chkShowPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chkShowPassword.isSelected()) {
                    txtPassword.setEchoChar((char) 0);
                } else {
                    txtPassword.setEchoChar('•');
                }
            }
        });
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
            JOptionPane.showMessageDialog(null, "Database Error!", "Error" +ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
//    private static void openGmail() {
//        try {
//            Desktop.getDesktop().browse(new URI("https://mail.google.com/"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Failed to open Gmail!", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }

    private  static void registerUser() {
        String username = JOptionPane.showInputDialog(null, "Enter username:");
        if (username == null || username.isEmpty()) return;

        String password = JOptionPane.showInputDialog(null, "Enter password:");
        if (password == null || password.isEmpty()) return;

        String[] roles = {"Farmer", "Buyer"};
        String role = (String) JOptionPane.showInputDialog(null, "Select Role:", "Role", JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO USERS (username, password, role) VALUES (?, ?, ?)")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Registration Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error!"+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



