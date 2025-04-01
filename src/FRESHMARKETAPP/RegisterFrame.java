package FRESHMARKETAPP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterFrame extends JFrame {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/orcl";
    private static final String DB_USERNAME = "deborah";
    private static final String DB_PASSWORD = "12345";

    public RegisterFrame() {
        // Set up the JFrame for registration
        setTitle("Register Form");
        setSize(500, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(30, 30, 100, 30);
        add(lblUsername);

        JTextField txtUsername = new JTextField();
        txtUsername.setBounds(150, 30, 150, 30);
        add(txtUsername);

        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        lblConfirmPassword.setBounds(30, 110, 150, 30);
        add(lblConfirmPassword);

        JPasswordField txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(150, 110, 150, 30);
        add(txtConfirmPassword);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(30, 70, 100, 30);
        add(lblPassword);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 70, 150, 30);
        add(txtPassword);

        String[] roles = {"Buyer", "Farmer"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(150, 150, 150, 30);
        add(roleComboBox);

        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(30, 150, 100, 30);
        add(lblRole);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(100, 200, 120, 30);
        add(btnRegister);

        // Action listener for the register button
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String confirmPassword = new String(txtConfirmPassword.getPassword());
                String selectedRole = (String) roleComboBox.getSelectedItem();

                registerUser(RegisterFrame.this, username, password, confirmPassword,selectedRole);  // Use RegisterFrame.this here
            }
        });

    }

    private static void registerUser(JFrame frame, String username, String password, String confirmPassword,String role) {
        // Validations
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(frame, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Database interaction to check for existing users and insert new user
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement checkUser = conn.prepareStatement("SELECT * FROM USERS WHERE username = ?");
             PreparedStatement insertUser = conn.prepareStatement("INSERT INTO USERS(id, username, password,role) VALUES(users_seq.NEXTVAL, ?, ?,?)")) {

            checkUser.setString(1, username);
            ResultSet rs = checkUser.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(frame, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            insertUser.setString(1, username);
            insertUser.setString(2, password);
            insertUser.setString(3, role);
            insertUser.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Registration successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Create an instance of RegisterFrame and show it
        RegisterFrame registerFrame = new RegisterFrame();  // Create instance of RegisterFrame
        registerFrame.setVisible(true);  // Show the registration form
    }
}
