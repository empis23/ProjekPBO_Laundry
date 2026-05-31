package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import controller.AuthController;
import model.Admin;

/**
 * LoginView — Halaman login dengan tampilan dua kolom:
 * - Kiri: Ilustrasi mesin cuci + gelembung sabun (gradient panel)
 * - Kanan: Form login modern
 */
public class LoginView extends JFrame {

    private final JTextField     txtUsername = Apptheme.textField();
    private final JPasswordField txtPassword = Apptheme.passwordField();
    private final AuthController authController = new AuthController();

    public LoginView() {
        setTitle("Login — Projek Laundry");
        setSize(820, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildLeftPanel(),  BorderLayout.WEST);
        root.add(buildRightPanel(), BorderLayout.CENTER);
        setContentPane(root);

        // Enter key → login
        getRootPane().setDefaultButton(null);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enter, "login");
        getRootPane().getActionMap().put("login", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { prosesLogin(); }
        });
    }

    // ── Panel Kiri: Ilustrasi ──────────────────────────────────
    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient diagonal
                GradientPaint gp = new GradientPaint(
                        0, 0, Apptheme.GRAD_START,
                        getWidth(), getHeight(), Apptheme.GRAD_END
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(360, 520));
        panel.setOpaque(true);

        // Ilustrasi mesin cuci
        WashingMachinePanel illustration = new WashingMachinePanel();
        illustration.setOpaque(false);
        illustration.setPreferredSize(new Dimension(360, 300));

        // Teks bawah ilustrasi
        JPanel textBox = new JPanel();
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        textBox.setOpaque(false);
        textBox.setBorder(new EmptyBorder(0, 30, 40, 30));

        JLabel title = new JLabel("LaundryPro", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Manajemen Laundry Modern", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(255, 255, 255, 200));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        textBox.add(title);
        textBox.add(Box.createVerticalStrut(6));
        textBox.add(subtitle);

        panel.add(illustration, BorderLayout.CENTER);
        panel.add(textBox, BorderLayout.SOUTH);
        return panel;
    }

    // ── Panel Kanan: Form ─────────────────────────────────────
    private JPanel buildRightPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(Apptheme.BG_PAGE);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Apptheme.BG_CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
                new Apptheme.RoundBorder(Apptheme.BORDER, 1, 16),
                new EmptyBorder(40, 40, 40, 40)
        ));
        form.setPreferredSize(new Dimension(340, 420));

        // Heading
        JLabel greeting = new JLabel("Selamat Datang!");
        greeting.setFont(new Font("Segoe UI", Font.BOLD, 22));
        greeting.setForeground(Apptheme.PRIMARY_DARK);
        greeting.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Masukkan akun Anda untuk melanjutkan");
        sub.setFont(Apptheme.FONT_SMALL);
        sub.setForeground(Apptheme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Username field
        JLabel lblUser = Apptheme.fieldLabel("Username");
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, Apptheme.FIELD_HEIGHT));

        // Password field
        JLabel lblPass = Apptheme.fieldLabel("Password");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, Apptheme.FIELD_HEIGHT));

        // Login button
        JButton btnLogin = Apptheme.primaryButton("Masuk →");
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, Apptheme.BTN_HEIGHT));
        btnLogin.addActionListener(e -> prosesLogin());

        form.add(greeting);
        form.add(Box.createVerticalStrut(4));
        form.add(sub);
        form.add(Box.createVerticalStrut(28));
        form.add(lblUser);
        form.add(Box.createVerticalStrut(6));
        form.add(txtUsername);
        form.add(Box.createVerticalStrut(16));
        form.add(lblPass);
        form.add(Box.createVerticalStrut(6));
        form.add(txtPassword);
        form.add(Box.createVerticalStrut(28));
        form.add(btnLogin);
        form.add(Box.createVerticalStrut(20));

        // Footer note
        JLabel note = new JLabel("© 2026 LaundryPro — Aplikasi Manajemen Laundry");
        note.setFont(Apptheme.FONT_SMALL);
        note.setForeground(Apptheme.TEXT_HINT);
        note.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(note);

        outer.add(form);
        return outer;
    }

    // ── Logic ─────────────────────────────────────────────────
    private void prosesLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username dan password wajib diisi.");
            return;
        }

        // Visual feedback: disable tombol sementara proses login
        Component[] forms = ((JPanel) ((JPanel) getContentPane()
                .getComponent(1)).getComponent(0)).getComponents();

        Admin admin = authController.login(username, password);
        if (admin != null) {
            JOptionPane.showMessageDialog(this,
                    "Login berhasil. Selamat datang, " + admin.getUsername() + "!",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            new DashboardView(admin).setVisible(true);
            dispose();
        } else {
            showError("Username atau password salah. Coba lagi.");
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Perhatian", JOptionPane.WARNING_MESSAGE);
    }

    // ════════════════════════════════════════════════════════════
    // INNER CLASS — Ilustrasi Mesin Cuci
    // ════════════════════════════════════════════════════════════
    static class WashingMachinePanel extends JPanel {

        private float bubbleOffset = 0;
        private Timer animTimer;

        public WashingMachinePanel() {
            setOpaque(false);
            // Animasi gelembung naik perlahan
            animTimer = new Timer(50, e -> {
                bubbleOffset = (bubbleOffset + 0.5f) % 20;
                repaint();
            });
            animTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            int w = getWidth();
            int h = getHeight();
            int cx = w / 2;
            int cy = h / 2 + 10;

            // ── Badan mesin (body) ────────────────────────────
            g2.setColor(new Color(255, 255, 255, 210));
            g2.fillRoundRect(cx - 75, cy - 95, 150, 175, 22, 22);
            g2.setColor(new Color(255, 255, 255, 100));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawRoundRect(cx - 75, cy - 95, 150, 175, 22, 22);

            // ── Panel kontrol atas ────────────────────────────
            g2.setColor(new Color(255, 255, 255, 150));
            g2.fillRoundRect(cx - 65, cy - 90, 130, 28, 6, 6);

            // Tombol-tombol kecil di panel kontrol
            int[] btnX = {cx - 50, cx - 25, cx, cx + 25, cx + 45};
            for (int i = 0; i < btnX.length; i++) {
                if (i == 2) {
                    g2.setColor(new Color(236, 72, 153, 200)); // tombol aktif (pink)
                    g2.fillOval(btnX[i] - 7, cy - 82, 14, 14);
                } else {
                    g2.setColor(new Color(200, 200, 255, 150));
                    g2.fillOval(btnX[i] - 5, cy - 80, 10, 10);
                }
            }

            // ── Jendela drum ──────────────────────────────────
            // Bingkai luar
            g2.setColor(new Color(180, 160, 220, 180));
            g2.fillOval(cx - 56, cy - 54, 112, 112);
            // Kaca
            g2.setColor(new Color(160, 210, 255, 180));
            g2.fillOval(cx - 50, cy - 48, 100, 100);
            // Isi drum (gradient biru-putih simulasi air)
            GradientPaint waterGrad = new GradientPaint(
                    cx - 50, cy - 48, new Color(100, 180, 255, 200),
                    cx + 50, cy + 52, new Color(200, 230, 255, 150)
            );
            g2.setPaint(waterGrad);
            g2.fillOval(cx - 50, cy - 48, 100, 100);
            // Ring dalam drum
            g2.setColor(new Color(255, 255, 255, 100));
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(cx - 40, cy - 38, 80, 80);
            g2.setStroke(new BasicStroke(3f));
            g2.setColor(new Color(255, 255, 255, 150));
            g2.drawOval(cx - 50, cy - 48, 100, 100);

            // Baju dalam drum (abstraksi sederhana)
            g2.setColor(new Color(255, 200, 100, 120));
            g2.fillRoundRect(cx - 18, cy - 10, 40, 25, 8, 8);
            g2.setColor(new Color(255, 150, 200, 100));
            g2.fillRoundRect(cx - 25, cy + 10, 35, 20, 8, 8);

            // Kilap kaca
            g2.setColor(new Color(255, 255, 255, 130));
            g2.fillOval(cx - 35, cy - 42, 30, 20);

            // ── Gagang pintu ──────────────────────────────────
            g2.setColor(new Color(255, 255, 255, 200));
            g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(cx + 50, cy - 10, cx + 50, cy + 10);
            g2.fillRoundRect(cx + 46, cy - 12, 8, 24, 6, 6);

            // ── Kaki mesin ────────────────────────────────────
            g2.setColor(new Color(255, 255, 255, 160));
            g2.fillRoundRect(cx - 60, cy + 78, 25, 10, 6, 6);
            g2.fillRoundRect(cx + 35, cy + 78, 25, 10, 6, 6);

            // ── Gelembung sabun (animasi) ──────────────────────
            float[][] bubbles = {
                    {cx - 90f, cy - 20f, 14f, 0f},
                    {cx + 85f, cy - 50f, 10f, 0.3f},
                    {cx - 100f, cy + 30f, 8f,  0.6f},
                    {cx + 95f, cy + 10f, 12f, 0.1f},
                    {cx - 70f, cy - 70f, 16f, 0.5f},
                    {cx + 70f, cy + 50f, 9f,  0.8f},
                    {cx - 30f, cy - 110f, 11f, 0.2f},
                    {cx + 40f, cy - 100f, 7f,  0.7f},
                    {cx - 110f, cy - 50f, 6f,  0.9f},
            };

            for (float[] b : bubbles) {
                float bx  = b[0];
                float by  = b[1] - (bubbleOffset * (1 + b[3])) % 30;
                float rad = b[2];
                float phase = b[3];
                float alpha = 0.5f + 0.3f * (float) Math.sin(bubbleOffset * 0.2f + phase * Math.PI * 2);

                // Gelembung utama
                g2.setColor(new Color(1f, 1f, 1f, Math.min(alpha, 0.8f)));
                Ellipse2D bubble = new Ellipse2D.Float(bx - rad, by - rad, rad * 2, rad * 2);
                g2.fill(bubble);

                // Garis tepi gelembung
                g2.setColor(new Color(1f, 1f, 1f, 0.9f));
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(bubble);

                // Kilap gelembung
                g2.setColor(new Color(1f, 1f, 1f, 0.95f));
                g2.fillOval((int)(bx - rad * 0.45f), (int)(by - rad * 0.7f),
                        (int)(rad * 0.4f), (int)(rad * 0.3f));
            }

            g2.dispose();
        }
    }
}