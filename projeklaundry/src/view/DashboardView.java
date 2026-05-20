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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import model.Admin;

/**
 *
 * @author pf344
 */
public class DashboardView extends JFrame {
    private final Admin admin;

    public DashboardView(Admin admin) {
        this.admin = admin;

        setTitle("Dashboard - Projek Laundry");
        setSize(500, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel judul = new JLabel("Dashboard Projek Laundry", SwingConstants.CENTER);
        judul.setBorder(new EmptyBorder(15, 10, 5, 10));
        add(judul, BorderLayout.NORTH);

        JPanel panelMenu = new JPanel(new GridLayout(3, 2, 10, 10));
        panelMenu.setBorder(new EmptyBorder(20, 30, 20, 30));

        JButton btnPelanggan = new JButton("Data Pelanggan");
        JButton btnLayanan = new JButton("Data Layanan");
        JButton btnTransaksi = new JButton("Transaksi Laundry");
        JButton btnLaporan = new JButton("Laporan Transaksi");
        JButton btnTentang = new JButton("Tentang Aplikasi");
        JButton btnLogout = new JButton("Logout");

        btnPelanggan.addActionListener(e -> new PelangganView().setVisible(true));
        btnLayanan.addActionListener(e -> new LayananView().setVisible(true));
        btnTransaksi.addActionListener(e -> new TransaksiView().setVisible(true));
        btnLaporan.addActionListener(e -> new LaporanView().setVisible(true));
        btnTentang.addActionListener(e -> tampilTentang());
        btnLogout.addActionListener(e -> logout());

        panelMenu.add(btnPelanggan);
        panelMenu.add(btnLayanan);
        panelMenu.add(btnTransaksi);
        panelMenu.add(btnLaporan);
        panelMenu.add(btnTentang);
        panelMenu.add(btnLogout);

        add(panelMenu, BorderLayout.CENTER);

        JLabel footer = new JLabel("Login sebagai: " + admin.getUsername() + " (" + admin.getRole() + ")", SwingConstants.CENTER);
        footer.setBorder(new EmptyBorder(5, 10, 15, 10));
        add(footer, BorderLayout.SOUTH);
    }

    private void tampilTentang() {
        JOptionPane.showMessageDialog(this,
                "Aplikasi Manajemen Laundry\n"
                + "Dibuat menggunakan Java Swing, MySQL, dan konsep MVC.\n"
                + "Fitur: login, CRUD pelanggan, CRUD layanan, transaksi, dan laporan.");
    }

    private void logout() {
        int pilih = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (pilih == JOptionPane.YES_OPTION) {
            new LoginView().setVisible(true);
            dispose();
        }
    }
}
