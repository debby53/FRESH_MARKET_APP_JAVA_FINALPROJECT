package FRESHMARKETAPP;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterFrame extends JFrame {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP";
    private static final String DB_USERNAME = "deborah";
    private static final String DB_PASSWORD = "12345";

    public RegisterFrame() {
        // Set up the JFrame for registration
        setTitle("Register Form");
        setSize(700, 700);  // Increased size to fit all components
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(30, 30, 100, 30);
        add(lblUsername);

        JTextField txtUsername = new JTextField();
        txtUsername.setBounds(150, 30, 150, 30);
        add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(30, 70, 100, 30);
        add(lblPassword);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 70, 150, 30);
        add(txtPassword);

        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        lblConfirmPassword.setBounds(30, 110, 150, 30);
        add(lblConfirmPassword);

        JPasswordField txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(150, 110, 150, 30);
        add(txtConfirmPassword);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(30, 150, 100, 30);
        add(lblEmail);

        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(150, 150, 150, 30);
        add(txtEmail);

        JLabel lblTelNumber = new JLabel("Telephone:");
        lblTelNumber.setBounds(30, 190, 100, 30);
        add(lblTelNumber);
        JTextField txtTelNumber = new JTextField();
        txtTelNumber.setBounds(150, 190, 150, 30);
        add(txtTelNumber);

        JLabel lblLocation = new JLabel("Location:");
        lblLocation.setBounds(30, 230, 100, 30);
        add(lblLocation);

        JTextField txtLocation = new JTextField();
        txtLocation.setBounds(150, 230, 150, 30);
        add(txtLocation);

        JLabel lblDob = new JLabel("Date of Birth:");
        lblDob.setBounds(30, 270, 100, 30);
        add(lblDob);

        JTextField txtDob = new JTextField();
        txtDob.setBounds(150, 270, 150, 30);
        add(txtDob);

        JLabel lblFirstName = new JLabel("First Name:");
        lblFirstName.setBounds(30, 310, 100, 30);
        add(lblFirstName);

        JTextField txtFirstName = new JTextField();
        txtFirstName.setBounds(150, 310, 150, 30);
        add(txtFirstName);

        JLabel lblMiddleName = new JLabel("Middle Name:");
        lblMiddleName.setBounds(30, 350, 100, 30);
        add(lblMiddleName);

        JTextField txtMiddleName = new JTextField();
        txtMiddleName.setBounds(150, 350, 150, 30);
        add(txtMiddleName);

        JLabel lblLastName = new JLabel("Last Name:");
        lblLastName.setBounds(30, 390, 100, 30);
        add(lblLastName);

        JTextField txtLastName = new JTextField();
        txtLastName.setBounds(150, 390, 150, 30);
        add(txtLastName);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(30, 430, 100, 30);
        add(lblGender);

        String[] genders = {"Male", "Female", "Other"};
        JComboBox<String> genderComboBox = new JComboBox<>(genders);
        genderComboBox.setBounds(150, 430, 150, 30);
        add(genderComboBox);

        JLabel lblNationality = new JLabel("Nationality:");
        lblNationality.setBounds(30, 470, 100, 30);
        add(lblNationality);

        JTextField txtNationality = new JTextField();
        txtNationality.setBounds(150, 470, 150, 30);
        add(txtNationality);

        JLabel lblIdOrPassport = new JLabel("National ID/Passport:");
        lblIdOrPassport.setBounds(30, 510, 150, 30);
        add(lblIdOrPassport);

        JTextField txtIdOrPassport = new JTextField();
        txtIdOrPassport.setBounds(150, 510, 150, 30);
        add(txtIdOrPassport);

        JLabel lblRole = new JLabel("Role:");
        lblRole.setBounds(30, 550, 100, 30);
        add(lblRole);

        String[] roles = {"Buyer", "Farmer"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(150, 550, 150, 30);
        add(roleComboBox);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(100, 600, 120, 30);
        add(btnRegister);

        // Action listener for the register button
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String confirmPassword = new String(txtConfirmPassword.getPassword());
                String email = txtEmail.getText();
                String selectedRole = (String) roleComboBox.getSelectedItem();

                String telNumber = txtTelNumber.getText();
                String location = txtLocation.getText();
                String dob = txtDob.getText();
                String firstName = txtFirstName.getText();
                String middleName = txtMiddleName.getText();
                String lastName = txtLastName.getText();
                String gender = (String) genderComboBox.getSelectedItem();
                String nationality = txtNationality.getText();
                String idOrPassport = txtIdOrPassport.getText();
                registerUser(RegisterFrame.this, username, password, confirmPassword, email, telNumber, location, dob, firstName, middleName, lastName, gender, nationality, idOrPassport, selectedRole);
            }
        });
    }

    private static void registerUser(JFrame frame, String username, String password, String confirmPassword, String email, String telNumber, String location, String dob, String firstName, String middleName, String lastName, String gender, String nationality, String idOrPassport, String selectedRole) {
        String dobString = JOptionPane.showInputDialog(null, "Enter date of birth (YYYY-MM-DD):");
        if (dobString == null || dobString.isEmpty()) return;

        // Convert the string to java.sql.Date




        // Validations

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || telNumber.isEmpty() || location.isEmpty() || dob.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || nationality.isEmpty() || idOrPassport.isEmpty()) {
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
             PreparedStatement insertUser = conn.prepareStatement("INSERT INTO USERS(id, username, password, email, tel_number, location, dob, first_name, middle_name, last_name, gender, nationality, id_or_passport, role) VALUES(users_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {



            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = dateFormat.parse(dobString);
//            dob = String.valueOf(new Date(parsedDate.getTime()));
            java.sql.Date sqlDob = new java.sql.Date(parsedDate.getTime());




            checkUser.setString(1, username);
            ResultSet rs = checkUser.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(frame, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            insertUser.setString(1, username);
            insertUser.setString(2, password);
            insertUser.setString(3, email);
            insertUser.setString(4, telNumber);
            insertUser.setString(5, location);
            insertUser.setDate(6, sqlDob);
            insertUser.setString(7, firstName);
            insertUser.setString(8, middleName);
            insertUser.setString(9, lastName);
            insertUser.setString(10, gender);
            insertUser.setString(11, nationality);
            insertUser.setString(12, idOrPassport);
            insertUser.setString(13, selectedRole);
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
