package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class Apptheme {
    public static final Color PRIMARY        = new Color(109, 40, 217);
    public static final Color PRIMARY_DARK   = new Color(76, 29, 149);
    public static final Color PRIMARY_LIGHT  = new Color(237, 233, 254);
    public static final Color ACCENT         = new Color(236, 72, 153);
    public static final Color ACCENT_LIGHT   = new Color(253, 224, 242);

    public static final Color GRAD_START = new Color(109, 40, 217);
    public static final Color GRAD_MID   = new Color(167, 64, 185);
    public static final Color GRAD_END   = new Color(236, 72, 153);

    public static final Color BG_PAGE   = new Color(249, 250, 251);
    public static final Color BG_CARD   = Color.WHITE;
    public static final Color BORDER    = new Color(229, 231, 235);
    public static final Color DIVIDER   = new Color(243, 244, 246);

    public static final Color TEXT_PRIMARY   = new Color(17, 24, 39);
    public static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    public static final Color TEXT_WHITE     = Color.WHITE;
    public static final Color TEXT_HINT      = new Color(156, 163, 175);

    public static final Color SUCCESS = new Color(16, 185, 129);
    public static final Color DANGER  = new Color(239, 68, 68);
    public static final Color WARNING = new Color(245, 158, 11);
    public static final Color INFO    = new Color(59, 130, 246);

    public static final Font FONT_TITLE    = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADING  = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_SUBHEAD  = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL    = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BUTTON   = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_MONO     = new Font("Consolas", Font.PLAIN, 13);

    public static final int RADIUS        = 10;
    public static final int PAD           = 16;
    public static final int BTN_HEIGHT    = 38;
    public static final int FIELD_HEIGHT  = 42;
    public static final int ROW_HEIGHT    = 38;
    public static final int HEADER_HEIGHT = 58;

    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, isEnabled() ? GRAD_START : TEXT_HINT,
                        getWidth(), 0, isEnabled() ? GRAD_END : BORDER
                );

                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        styleBaseButton(btn);
        btn.setForeground(TEXT_WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        applyButtonSize(btn);

        return btn;
    }

    public static JButton secondaryButton(String text) {
        JButton btn = new JButton(text);

        styleBaseButton(btn);
        btn.setForeground(PRIMARY);
        btn.setBackground(BG_CARD);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(PRIMARY, 1, true),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        btn.setOpaque(true);
        applyButtonSize(btn);

        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = new JButton(text);

        styleBaseButton(btn);
        btn.setForeground(DANGER);
        btn.setBackground(new Color(254, 242, 242));
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(252, 165, 165), 1, true),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        btn.setOpaque(true);
        applyButtonSize(btn);

        return btn;
    }

    public static JButton neutralButton(String text) {
        JButton btn = new JButton(text);

        styleBaseButton(btn);
        btn.setForeground(TEXT_SECONDARY);
        btn.setBackground(DIVIDER);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        btn.setOpaque(true);
        applyButtonSize(btn);

        return btn;
    }

    private static void styleBaseButton(JButton btn) {
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setMargin(new Insets(8, 16, 8, 16));
    }

    private static void applyButtonSize(JButton btn) {
        Dimension d = btn.getPreferredSize();
        btn.setPreferredSize(new Dimension(Math.max(d.width + 12, 105), BTN_HEIGHT));
    }

    public static JTextField textField() {
        JTextField tf = new JTextField();
        applyFieldStyle(tf);
        return tf;
    }

    public static JTextField textField(int columns) {
        JTextField tf = new JTextField(columns);
        applyFieldStyle(tf);
        return tf;
    }

    public static JPasswordField passwordField() {
        JPasswordField pf = new JPasswordField();
        applyFieldStyle(pf);
        return pf;
    }

    public static JTextArea textArea(int rows, int cols) {
        JTextArea ta = new JTextArea(rows, cols);
        ta.setFont(FONT_BODY);
        ta.setForeground(TEXT_PRIMARY);
        ta.setBackground(Color.WHITE);
        ta.setCaretColor(PRIMARY);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return ta;
    }

    public static JComboBox<String> comboBox(String... items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(FONT_BODY);
        cb.setBackground(BG_CARD);
        cb.setForeground(TEXT_PRIMARY);
        cb.setPreferredSize(new Dimension(0, FIELD_HEIGHT));
        return cb;
    }

    private static void applyFieldStyle(javax.swing.text.JTextComponent tc) {
        tc.setFont(FONT_BODY);
        tc.setForeground(TEXT_PRIMARY);
        tc.setBackground(Color.WHITE);
        tc.setCaretColor(PRIMARY);

        tc.setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(6, RADIUS),
                BorderFactory.createCompoundBorder(
                        new RoundBorder(new Color(205, 210, 220), 1, RADIUS),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                )
        ));

        tc.setPreferredSize(new Dimension(240, FIELD_HEIGHT));
    }

    public static JLabel titleLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_TITLE);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    public static JLabel headingLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADING);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    public static JLabel bodyLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_BODY);
        l.setForeground(TEXT_SECONDARY);
        return l;
    }

    public static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SUBHEAD);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    public static JLabel whiteTitleLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADING);
        l.setForeground(TEXT_WHITE);
        return l;
    }

    public static JPanel gradientPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, GRAD_START,
                        getWidth(), getHeight(), GRAD_END
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
    }

    public static JPanel headerPanel(String title) {
        JPanel header = gradientPanel();
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, HEADER_HEIGHT));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        header.add(whiteTitleLabel(title), BorderLayout.CENTER);
        return header;
    }

    public static JPanel cardPanel() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(8, RADIUS),
                BorderFactory.createCompoundBorder(
                        new RoundBorder(BORDER, 1, RADIUS),
                        BorderFactory.createEmptyBorder(PAD, PAD, PAD, PAD)
                )
        ));
        return p;
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(ROW_HEIGHT);
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_PRIMARY);
        table.setGridColor(BORDER);
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(PRIMARY_DARK);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_SUBHEAD);
        header.setBackground(new Color(245, 243, 255));
        header.setForeground(PRIMARY_DARK);
        header.setBorder(new MatteBorder(0, 0, 2, 0, PRIMARY_LIGHT));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer renderer =
                (DefaultTableCellRenderer) header.getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
    }

    public static class RoundBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;

        public RoundBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness + 2, thickness + 2, thickness + 2, thickness + 2);
        }
    }

    public static class ShadowBorder extends AbstractBorder {
        private final int shadowSize;
        private final int radius;

        public ShadowBorder(int shadowSize, int radius) {
            this.shadowSize = shadowSize;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int i = 0; i < shadowSize; i++) {
                int alpha = 42 - (i * 5);
                if (alpha < 6) {
                    alpha = 6;
                }

                g2.setColor(new Color(0, 0, 0, alpha));

                g2.drawRoundRect(
                        x + i,
                        y + i,
                        width - shadowSize - 1,
                        height - shadowSize - 1,
                        radius,
                        radius
                );
            }

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, shadowSize + 2, shadowSize + 2);
        }
    }

    public static String formatRupiah(double amount) {
        long val = Math.round(amount);
        String s = String.valueOf(val);
        StringBuilder sb = new StringBuilder();

        int count = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (count > 0 && count % 3 == 0) {
                sb.insert(0, '.');
            }

            sb.insert(0, s.charAt(i));
            count++;
        }

        return "Rp " + sb;
    }

    private Apptheme() {
    }
}