package view;

import entity.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Login extends javax.swing.JFrame {
    private JTextField fieldUser;
    private JPasswordField fieldPass;
    private JButton btnLogin;
    private JPanel panelLogin;

    public Login() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application when the window is closed
        setContentPane(panelLogin);
        pack();
        setSize(500, 500);

        // Action listener for the login button
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Admin admin = new Admin();
                String username = admin.setUsername(fieldUser.getText());
                String password = new String(fieldPass.getPassword());

                try {
                    Connection conn = Connect.getConnection();
                    String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
                    PreparedStatement stm = conn.prepareStatement(query);
                    stm.setString(1, username);
                    stm.setString(2, password);
                    ResultSet res = stm.executeQuery();

                    if (res.next()) {
                        JOptionPane.showMessageDialog(null, "Login Berhasil");
                        Home home = new Home();
                        JFrame frameHome = new JFrame("Stock Barang");
                        frameHome.setSize(1200, 500);
                        frameHome.add(home.getPanelHome());
                        frameHome.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Username atau Password yang dimasukkan Salah!");
                        fieldPass.requestFocus();
                    }

                    stm.close();
                    res.close();
                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    public JPanel getPanelLogin() {
        return panelLogin;
    }
}