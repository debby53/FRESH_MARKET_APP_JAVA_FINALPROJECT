package FRESHMARKETAPP;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginPhase {
    String dbUrl = "jdbc:oracle:thin:@localhost:1522/orcl";
    String dbUsername = "deborah";
    String dbPassword = "12345";
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

        JButton btnRegister=new JButton("Register");
        btnRegister.setBounds(250,150,100,30);
        frame.add(btnRegister);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username=txtName.getText();
                String password=new String(txtPassword.getPassword());
                loginUser(username, password);

            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtName.getText();
                String password = new String(txtPassword.getPassword());
                registerUser(username, password);

            }
        });


        frame.setVisible(true);


    }
//    METHOD TO HANDLE USER LOGIN
   private static  void loginUser(String username,String password){
        try(Connection conn= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP","deborah","12345");
            PreparedStatement pst=conn.prepareStatement("SELECT * FROM USERS WHERE username=? AND password=?")){

            pst.setString(1,username);
            pst.setString(2,password);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(null,"Login successful");
            }else {
                JOptionPane.showMessageDialog(null,"Invalid username or password");
            }


        }catch (Exception e){
            e.printStackTrace();
        }
   }
    //   METHOD TO HANDLE USER REGISTRATION
    private static void registerUser(String username,String password){
        try(Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522/FRESH_FRUITS_APP","deborah","12345");
            PreparedStatement pst=conn.prepareStatement("INSERT INTO USERS(id,username,password) VALUES(users_seq.NEXTVAL,?,?)")){

            pst.setString(1,username);
            pst.setString(2,password);
           pst.executeUpdate();
            JOptionPane.showMessageDialog(null,"Registration successful");

        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Registration failed");



        }

   }

}
