package FRESHMARKETAPP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginPhase {
    private String dbUrl = "jdbc:oracle:thin:@localhost:1522/orcl";
    private String dbUsername = "deborah";
    private String dbPassword = "12345";
    public static void main(String[] args) {
        JFrame frame=new JFrame("Fresh Market login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        frame.setLayout(null);

        JLabel lblName=new JLabel("Username");
        lblName.setBounds(30,30,100,30);
        frame.add(lblName);

        JTextField txtName=new JTextField();
        txtName.setBounds(150,30,100,30);
        frame.add(txtName);

        JLabel lblPassword=new JLabel("Password");
        lblPassword.setBounds(30,80,100,30);
        frame.add(lblPassword);

        JPasswordField txtPassword=new JPasswordField();
        txtPassword.setBounds(150,80,100,30);
        frame.add(txtPassword);

        JButton btnLogin=new JButton("Login");
        btnLogin.setBounds(100,150,100,30);
        frame.add(btnLogin);

        JLabel lblRegister=new JLabel("<html><a href='#'>Don't an account? Register</a><html>");
        lblRegister.setBounds(100,180,200,30);
        lblRegister.setForeground(Color.BLUE);
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.add(lblRegister);

//        JButton btnRegister=new JButton("Register");
//        btnRegister.setBounds(250,150,100,30);


//        JButton btnRegister=new JButton("Register");
//        btnRegister.setBounds(250,150,100,30);
//        frame.add(btnRegister);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username=txtName.getText();
                String password=new String(txtPassword.getPassword());
                loginUser(username, password);

            }
        });
        lblRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RegisterFrame registerFrame=new RegisterFrame();
                registerFrame.setVisible(true);// Open Register Form
            }
        });

//        btnRegister.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String username = txtName.getText();
//                String password = new String(txtPassword.getPassword());
//                registerUser(username, password);
//
//            }
//        });
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);


    }
//    METHOD TO HANDLE USER LOGIN
   private static  void loginUser(String username,String password){

        if(username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(null,"Please enter both username and password!", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try(Connection conn= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP","deborah","12345");
            PreparedStatement pst=conn.prepareStatement("SELECT * FROM USERS WHERE username=? AND password=?")){

            pst.setString(1,username);
            pst.setString(2,password);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(null,"Login successful","welcome",JOptionPane.INFORMATION_MESSAGE);
//                new DashboardFrame().setVisible(true);
            }else {
                JOptionPane.showMessageDialog(null,"Invalid username or password","Login failed",JOptionPane.ERROR_MESSAGE);
            }


        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
   }
    //   METHOD TO HANDLE USER REGISTRATION
//    private static void registerUser(String username,String password){
//        try(Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP","deborah","12345");
//            PreparedStatement pst=conn.prepareStatement("INSERT INTO USERS(id,username,password) VALUES(users_seq.NEXTVAL,?,?)")){
//
//            pst.setString(1,username);
//            pst.setString(2,password);
//           pst.executeUpdate();
//            JOptionPane.showMessageDialog(null,"Registration successful");
//
//        }catch (Exception e){
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null,"Registration failed");
//
//
//
//        }
//
//   }

}
