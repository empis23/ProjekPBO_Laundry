/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import controller.AuthController;
import model.Admin;

/**
 *
 * @author pf344
 */
public class LoginView extends JFrame {
    private final JTextField txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final AuthController authController = new AuthController();

    public LoginView() {
        setTitle("Login - Projek Laundry");
        setSize(400, 230);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel judul = new JLabel("PROJEK LAUNDRY", SwingConstants.CENTER);
        judul.setBorder(new EmptyBorder(15, 10, 5, 10));
        add(judul, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        panelForm.setBorder(new EmptyBorder(10, 30, 10, 30));
        panelForm.add(new JLabel("Username"));
        panelForm.add(txtUsername);
        panelForm.add(new JLabel("Password"));
        panelForm.add(txtPassword);
        add(panelForm, BorderLayout.CENTER);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> prosesLogin());

        JPanel panelButton = new JPanel();
        panelButton.add(btnLogin);
        add(panelButton, BorderLayout.SOUTH);
    }

    private void prosesLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password wajib diisi.");
            return;
        }

        Admin admin = authController.login(username, password);
        if (admin != null) {
            JOptionPane.showMessageDialog(this, "Login berhasil. Selamat datang, " + admin.getUsername());
            new DashboardView(admin).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username atau password salah.");
        }
    }
}
