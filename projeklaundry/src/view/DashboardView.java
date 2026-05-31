package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import model.Admin;

/**
 * DashboardView — Halaman utama setelah login.
 * Layout: header gradient + grid menu card + footer status.
 */
public class DashboardView extends JFrame {

    private final Admin admin;

    // Menu: [label, ikon unicode, warna aksen, deskripsi singkat]
    private static final String[][] MENU_DATA = {
        {"Data Pelanggan",   "👤", "#7C3AED", "Kelola data pelanggan"},
        {"Data Layanan",     "🧺", "#9333EA", "Harga & estimasi layanan"},
        {"Transaksi",        "🧾", "#A21CAF", "Catat transaksi masuk"},
        {"Laporan",          "📊", "#DB2777", "Laporan & export CSV"},
        {"Tentang Aplikasi", "ℹ",  "#6B7280", "Info versi & pengembang"},
        {"Logout",           "🚪", "#DC2626", "Keluar dari aplikasi"},
    };

    public DashboardView(Admin admin) {
        this.admin = admin;
        setTitle("Dashboard — LaundryPro");
        setSize(720, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Apptheme.BG_PAGE);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildMenuGrid(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ── Header ──────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = Apptheme.gradientPanel();
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel title = new JLabel("LaundryPro");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JLabel tagline = new JLabel("Sistem Manajemen Laundry Terintegrasi");
        tagline.setFont(Apptheme.FONT_SMALL);
        tagline.setForeground(new Color(255, 255, 255, 180));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(tagline);

        header.add(textPanel, BorderLayout.CENTER);
        return header;
    }

    // ── Grid Menu ───────────────────────────────────────────────
    private JPanel buildMenuGrid() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Apptheme.BG_PAGE);
        wrapper.setBorder(new EmptyBorder(24, 30, 24, 30));

        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setBackground(Apptheme.BG_PAGE);

        for (String[] menu : MENU_DATA) {
            grid.add(createMenuCard(menu[0], menu[1], menu[2], menu[3]));
        }

        wrapper.add(grid);
        return wrapper;
    }

    private JPanel createMenuCard(String label, String icon, String colorHex, String desc) {
        Color accentColor = Color.decode(colorHex);

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // garis kiri berwarna
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, 5, getHeight(), 3, 3);
                g2.dispose();
            }
        };
        card.setBackground(Apptheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new Apptheme.RoundBorder(Apptheme.BORDER, 1, 12),
                new EmptyBorder(14, 18, 14, 14)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Ikon
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));

        // Teks
        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setOpaque(false);
        texts.setBorder(new EmptyBorder(0, 8, 0, 0));

        JLabel lblName = new JLabel(label);
        lblName.setFont(Apptheme.FONT_SUBHEAD);
        lblName.setForeground(Apptheme.TEXT_PRIMARY);

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(Apptheme.FONT_SMALL);
        lblDesc.setForeground(Apptheme.TEXT_SECONDARY);

        texts.add(lblName);
        texts.add(Box.createVerticalStrut(3));
        texts.add(lblDesc);

        card.add(lblIcon, BorderLayout.WEST);
        card.add(texts, BorderLayout.CENTER);

        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(250, 245, 255));
                card.repaint();
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Apptheme.BG_CARD);
                card.repaint();
            }
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                handleMenuClick(label);
            }
        });

        return card;
    }

    // ── Footer ──────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(245, 243, 255));
        footer.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, Apptheme.PRIMARY_LIGHT),
                new EmptyBorder(10, 20, 10, 20)
        ));

        JLabel lblUser = new JLabel("Login sebagai: " + admin.getUsername()
                + "   •   Role: " + admin.getRole());
        lblUser.setFont(Apptheme.FONT_SMALL);
        lblUser.setForeground(Apptheme.PRIMARY);

        JLabel lblVersion = new JLabel("LaundryPro v1.0");
        lblVersion.setFont(Apptheme.FONT_SMALL);
        lblVersion.setForeground(Apptheme.TEXT_HINT);

        footer.add(lblUser, BorderLayout.WEST);
        footer.add(lblVersion, BorderLayout.EAST);
        return footer;
    }

    // ── Event Handler ───────────────────────────────────────────
    private void handleMenuClick(String label) {
        switch (label) {
            case "Data Pelanggan"   -> new PelangganView().setVisible(true);
            case "Data Layanan"     -> new LayananView().setVisible(true);
            case "Transaksi"        -> new TransaksiView().setVisible(true);
            case "Laporan"          -> new LaporanView().setVisible(true);
            case "Tentang Aplikasi" -> tampilTentang();
            case "Logout"           -> logout();
        }
    }

    private void tampilTentang() {
        JOptionPane.showMessageDialog(this,
                "<html><b>LaundryPro — Aplikasi Manajemen Laundry</b><br><br>"
                + "Dibuat dengan Java Swing & MySQL, pola MVC.<br>"
                + "Fitur: Login · CRUD Pelanggan · CRUD Layanan<br>"
                + "       Transaksi · Laporan · Export CSV<br><br>"
                + "<i>© 2026 LaundryPro</i></html>",
                "Tentang Aplikasi", JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        int pilih = JOptionPane.showConfirmDialog(this,
                "Yakin ingin logout?", "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (pilih == JOptionPane.YES_OPTION) {
            new LoginView().setVisible(true);
            dispose();
        }
    }
}