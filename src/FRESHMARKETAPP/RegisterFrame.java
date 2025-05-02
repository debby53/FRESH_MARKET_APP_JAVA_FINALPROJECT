package FRESHMARKETAPP;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
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

    // UI Components
    private JTextField txtUsername, txtEmail, txtTelNumber, txtLocation;
    private JTextField txtDob, txtFirstName, txtMiddleName, txtLastName;
    private JTextField txtNationality, txtIdOrPassport;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<String> genderComboBox, roleComboBox;
    private JButton btnRegister, btnReset;

    // Colors
    private final Color PRIMARY_COLOR = new Color(46, 139, 87); // Forest Green
    private final Color SECONDARY_COLOR = new Color(240, 255, 240); // Honeydew
    private final Color ACCENT_COLOR = new Color(60, 179, 113); // Medium Sea Green
    private final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);
    private final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    public RegisterFrame() {
        // Set up the JFrame
        setTitle("Fresh Market - User Registration");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(SECONDARY_COLOR);

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form panels
        JPanel formPanel = new JPanel(new BorderLayout(10, 15));
        formPanel.setBackground(SECONDARY_COLOR);

        // Left panel for account details
        JPanel accountPanel = createAccountPanel();

        // Right panel for personal details
        JPanel personalPanel = createPersonalPanel();

        // Add the two panels side by side
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setBackground(SECONDARY_COLOR);
        centerPanel.add(accountPanel);
        centerPanel.add(personalPanel);
        formPanel.add(centerPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = createButtonsPanel();
        formPanel.add(buttonsPanel, BorderLayout.SOUTH);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        headerPanel.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Fresh Market Registration");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JLabel lblSubtitle = new JLabel("Join our community today!");
        lblSubtitle.setFont(new Font("Arial", Font.ITALIC, 14));
        lblSubtitle.setForeground(Color.WHITE);
        headerPanel.add(lblSubtitle, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createAccountPanel() {
        JPanel accountPanel = new JPanel(new GridBagLayout());
        accountPanel.setBackground(Color.WHITE);
        accountPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "Account Information",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                HEADER_FONT,
                PRIMARY_COLOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.weightx = 1.0;

        // Username
        addFormField(accountPanel, "Username:", gbc, 0);
        txtUsername = new JTextField();
        styleTextField(txtUsername);
        addComponent(accountPanel, txtUsername, gbc, 1);

        // Password
        addFormField(accountPanel, "Password:", gbc, 2);
        txtPassword = new JPasswordField();
        styleTextField(txtPassword);
        addComponent(accountPanel, txtPassword, gbc, 3);

        // Confirm Password
        addFormField(accountPanel, "Confirm Password:", gbc, 4);
        txtConfirmPassword = new JPasswordField();
        styleTextField(txtConfirmPassword);
        addComponent(accountPanel, txtConfirmPassword, gbc, 5);

        // Email
        addFormField(accountPanel, "Email:", gbc, 6);
        txtEmail = new JTextField();
        styleTextField(txtEmail);
        addComponent(accountPanel, txtEmail, gbc, 7);

        // Phone
        addFormField(accountPanel, "Telephone:", gbc, 8);
        txtTelNumber = new JTextField();
        styleTextField(txtTelNumber);
        addComponent(accountPanel, txtTelNumber, gbc, 9);

        // Location
        addFormField(accountPanel, "Location:", gbc, 10);
        txtLocation = new JTextField();
        styleTextField(txtLocation);
        addComponent(accountPanel, txtLocation, gbc, 11);

        // Role
        addFormField(accountPanel, "Role:", gbc, 12);
        String[] roles = {"Buyer", "Farmer"};
        roleComboBox = new JComboBox<>(roles);
        styleComboBox(roleComboBox);
        addComponent(accountPanel, roleComboBox, gbc, 13);

        // Add some vertical glue to push everything up
        gbc.weighty = 1.0;
        gbc.gridy = 14;
        accountPanel.add(Box.createVerticalGlue(), gbc);

        return accountPanel;
    }

    private JPanel createPersonalPanel() {
        JPanel personalPanel = new JPanel(new GridBagLayout());
        personalPanel.setBackground(Color.WHITE);
        personalPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "Personal Information",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                HEADER_FONT,
                PRIMARY_COLOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.weightx = 1.0;

        // First Name
        addFormField(personalPanel, "First Name:", gbc, 0);
        txtFirstName = new JTextField();
        styleTextField(txtFirstName);
        addComponent(personalPanel, txtFirstName, gbc, 1);

        // Middle Name
        addFormField(personalPanel, "Middle Name:", gbc, 2);
        txtMiddleName = new JTextField();
        styleTextField(txtMiddleName);
        addComponent(personalPanel, txtMiddleName, gbc, 3);

        // Last Name
        addFormField(personalPanel, "Last Name:", gbc, 4);
        txtLastName = new JTextField();
        styleTextField(txtLastName);
        addComponent(personalPanel, txtLastName, gbc, 5);

        // Date of Birth
        addFormField(personalPanel, "Date of Birth (YYYY-MM-DD):", gbc, 6);
        txtDob = new JTextField();
        styleTextField(txtDob);
        addComponent(personalPanel, txtDob, gbc, 7);

        // Gender
        addFormField(personalPanel, "Gender:", gbc, 8);
        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        styleComboBox(genderComboBox);
        addComponent(personalPanel, genderComboBox, gbc, 9);

        // Nationality
        addFormField(personalPanel, "Nationality:", gbc, 10);
        txtNationality = new JTextField();
        styleTextField(txtNationality);
        addComponent(personalPanel, txtNationality, gbc, 11);

        // ID/Passport
        addFormField(personalPanel, "National ID/Passport:", gbc, 12);
        txtIdOrPassport = new JTextField();
        styleTextField(txtIdOrPassport);
        addComponent(personalPanel, txtIdOrPassport, gbc, 13);

        // Add some vertical glue to push everything up
        gbc.weighty = 1.0;
        gbc.gridy = 14;
        personalPanel.add(Box.createVerticalGlue(), gbc);

        return personalPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.setBackground(SECONDARY_COLOR);

        btnRegister = new JButton("Register");
        styleButton(btnRegister, PRIMARY_COLOR, Color.WHITE);
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        btnReset = new JButton("Reset");
        styleButton(btnReset, Color.GRAY, Color.WHITE);
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });

        buttonsPanel.add(btnRegister);
        buttonsPanel.add(btnReset);

        return buttonsPanel;
    }

    private void addFormField(JPanel panel, String labelText, GridBagConstraints gbc, int gridy) {
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.weightx = 0.3;

        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(PRIMARY_COLOR);
        panel.add(label, gbc);
    }

    private void addComponent(JPanel panel, JComponent component, GridBagConstraints gbc, int gridy) {
        gbc.gridx = 1;
        gbc.gridy = gridy;
        gbc.weightx = 0.7;

        panel.add(component, gbc);
    }

    private void styleTextField(JTextField textField) {
        textField.setPreferredSize(new Dimension(150, 30));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setPreferredSize(new Dimension(150, 30));
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void resetForm() {
        // Clear all text fields
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtEmail.setText("");
        txtTelNumber.setText("");
        txtLocation.setText("");
        txtDob.setText("");
        txtFirstName.setText("");
        txtMiddleName.setText("");
        txtLastName.setText("");
        txtNationality.setText("");
        txtIdOrPassport.setText("");

        // Reset combo boxes
        genderComboBox.setSelectedIndex(0);
        roleComboBox.setSelectedIndex(0);
    }

    private void registerUser() {
        try {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());
            String email = txtEmail.getText();
            String telNumber = txtTelNumber.getText();
            String location = txtLocation.getText();
            String dob = txtDob.getText();
            String firstName = txtFirstName.getText();
            String middleName = txtMiddleName.getText();
            String lastName = txtLastName.getText();
            String gender = (String) genderComboBox.getSelectedItem();
            String nationality = txtNationality.getText();
            String idOrPassport = txtIdOrPassport.getText();
            String selectedRole = (String) roleComboBox.getSelectedItem();

            // Validation
            if (validateInputs(username, password, confirmPassword, email, telNumber, location,
                    dob, firstName, middleName, lastName, gender, nationality, idOrPassport, selectedRole)) {
                // Save to database
                saveUserToDatabase(username, password, email, telNumber, location, dob,
                        firstName, middleName, lastName, gender, nationality, idOrPassport, selectedRole);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unexpected error! Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInputs(String username, String password, String confirmPassword,
                                   String email, String telNumber, String location, String dob,
                                   String firstName, String middleName, String lastName,
                                   String gender, String nationality, String idOrPassport, String selectedRole) {

        // Validation patterns
        String usernamePattern = "^[a-zA-Z0-9_]{4,}$";
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        String telPattern = "^07\\d{8}$";
        String namePattern = "^[A-Za-z]+$";
        String optionalNamePattern = "^[A-Za-z]*$";
        String nationalityPattern = "^[A-Za-z ]+$";

        if (username.isEmpty() || !username.matches(usernamePattern)) {
            showError("Invalid username. Must be at least 4 characters.");
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            showError("Password must be at least 6 characters long.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match! Try again.");
            return false;
        }

        if (email.isEmpty() || !email.matches(emailPattern)) {
            showError("Invalid email format. Try again.");
            return false;
        }

        if (telNumber.isEmpty() || !telNumber.matches(telPattern)) {
            showError("Telephone must start with 07 and be exactly 10 digits.");
            return false;
        }

        if (location.isEmpty()) {
            showError("Location cannot be empty. Try again.");
            return false;
        }

        if (dob.isEmpty()) {
            showError("Date of Birth is required. Try again.");
            return false;
        }

        // Validate DOB format
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false); // Strict parsing
            dateFormat.parse(dob);
        } catch (Exception e) {
            showError("Invalid Date of Birth format. Use YYYY-MM-DD.");
            return false;
        }

        if (firstName.isEmpty() || !firstName.matches(namePattern)) {
            showError("First Name must only contain letters.");
            return false;
        }

        if (!middleName.matches(optionalNamePattern)) {
            showError("Middle Name must only contain letters (or leave it empty).");
            return false;
        }

        if (lastName.isEmpty() || !lastName.matches(namePattern)) {
            showError("Last Name must only contain letters.");
            return false;
        }

        if (gender == null || gender.isEmpty()) {
            showError("Please select a gender.");
            return false;
        }

        if (nationality.isEmpty() || !nationality.matches(nationalityPattern)) {
            showError("Nationality must only contain letters.");
            return false;
        }

        if (idOrPassport.isEmpty()) {
            showError("National ID/Passport cannot be empty.");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void saveUserToDatabase(String username, String password, String email,
                                    String telNumber, String location, String dob,
                                    String firstName, String middleName, String lastName,
                                    String gender, String nationality, String idOrPassport, String selectedRole) {
        try {
            // Parse the date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = dateFormat.parse(dob);
            java.sql.Date sqlDob = new java.sql.Date(parsedDate.getTime());

            // Database interaction
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                 PreparedStatement checkUser = conn.prepareStatement("SELECT * FROM USERS WHERE username = ?");
                 PreparedStatement insertUser = conn.prepareStatement(
                         "INSERT INTO USERS(id, username, password, email, tel_number, location, dob, " +
                                 "first_name, middle_name, last_name, gender, nationality, id_or_passport, role) " +
                                 "VALUES(users_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                checkUser.setString(1, username);
                ResultSet rs = checkUser.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Username already exists! Try another.",
                            "Error", JOptionPane.ERROR_MESSAGE);
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

                JOptionPane.showMessageDialog(this,
                        "Registration successful! You can now log in.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // Reset form after successful registration
                resetForm();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Database error occurred: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error processing date: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RegisterFrame registerFrame = new RegisterFrame();
                registerFrame.setVisible(true);
            }
        });
    }
}