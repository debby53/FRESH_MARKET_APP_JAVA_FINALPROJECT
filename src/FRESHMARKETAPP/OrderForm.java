package FRESHMARKETAPP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class OrderForm extends JDialog {

    static final String dbUrl = "jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP";
    static final String dbUsername = "deborah";
    static final String dbPassword = "12345";

    public OrderForm(JFrame parent) {
        super(parent, "Place Your Order", true);
        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JLabel lblAddress = new JLabel("Delivery Address:");
        JTextField txtAddress = new JTextField();

        JLabel lblPhone = new JLabel("Phone Number:");
        JTextField txtPhone = new JTextField();

        JLabel lblNotes = new JLabel("Notes:");
        JTextField txtNotes = new JTextField();

        JButton btnSubmit = new JButton("Submit Order");
        JButton btnCancel = new JButton("Cancel");

        add(lblAddress); add(txtAddress);
        add(lblPhone); add(txtPhone);
        add(lblNotes); add(txtNotes);
        add(btnSubmit); add(btnCancel);

        btnSubmit.addActionListener(e -> {
            String address = txtAddress.getText().trim();
            String phone = txtPhone.getText().trim();
            String notes = txtNotes.getText().trim();

            if (address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Address and phone are required.");
                return;
            }

            if (!phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Phone must be exactly 10 digits.");
                return;
            }

            saveOrder(address, phone, notes);
            Cart.clear(); // Empty cart after order
            JOptionPane.showMessageDialog(this, "Order placed successfully!");
            dispose();
        });

        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void saveOrder(String address, String phone, String notes) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            for (CartItem item : Cart.getItems()) {
                String insert = "INSERT INTO ORDERS (product_id, farmer_id, quantity, delivery_address, phone_number, notes, order_date) VALUES (?, ?, ?, ?, ?, ?, SYSDATE)";
                PreparedStatement stmt = conn.prepareStatement(insert);
                stmt.setInt(1, item.productId);
                stmt.setInt(2, item.farmerId);
                stmt.setInt(3, item.quantity);
                stmt.setString(4, address);
                stmt.setString(5, phone);
                stmt.setString(6, notes);
                stmt.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }
}
