package view;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import controller.TransaksiController;
import model.Transaksi;

/**
 * LaporanView — Laporan transaksi dengan filter tanggal + export CSV.
 */
public class LaporanView extends JFrame {

    private final TransaksiController controller = new TransaksiController();

    private final JTextField txtTanggalAwal  = Apptheme.textField(12);
    private final JTextField txtTanggalAkhir = Apptheme.textField(12);
    private final JLabel lblTotal = new JLabel("Total Pendapatan: —");

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Tanggal", "Pelanggan", "Layanan", "Berat (Kg)", "Total", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    public LaporanView() {
        setTitle("Laporan Transaksi — LaundryPro");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        txtTanggalAwal.setText(LocalDate.now().withDayOfMonth(1).toString());
        txtTanggalAkhir.setText(LocalDate.now().toString());

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Apptheme.BG_PAGE);
        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildContent(), BorderLayout.CENTER);
        root.add(buildFooter(),  BorderLayout.SOUTH);
        setContentPane(root);

        Apptheme.styleTable(table);
        tampilkanLaporan();
    }

    // ── Header dengan filter tanggal ─────────────────────────────
    private JPanel buildHeader() {
        // Gradient bar atas
        JPanel gradBar = Apptheme.gradientPanel();
        gradBar.setPreferredSize(new Dimension(0, Apptheme.HEADER_HEIGHT));
        gradBar.setLayout(new BorderLayout());
        gradBar.setBorder(new EmptyBorder(12, 24, 12, 24));
        JLabel title = new JLabel("📊  Laporan Transaksi");
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        gradBar.add(title, BorderLayout.WEST);

        // Panel filter di bawah gradient bar
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filterPanel.setBackground(Apptheme.BG_CARD);
        filterPanel.setBorder(new MatteBorder(0, 0, 1, 0, Apptheme.BORDER));

        filterPanel.add(Apptheme.bodyLabel("Dari:"));
        txtTanggalAwal.setPreferredSize(new Dimension(130, Apptheme.FIELD_HEIGHT));
        filterPanel.add(txtTanggalAwal);

        filterPanel.add(Apptheme.bodyLabel("Sampai:"));
        txtTanggalAkhir.setPreferredSize(new Dimension(130, Apptheme.FIELD_HEIGHT));
        filterPanel.add(txtTanggalAkhir);

       JButton btnTampilkan = Apptheme.primaryButton("Tampilkan");
JButton btnCetak     = Apptheme.secondaryButton("Cetak");
JButton btnExport    = Apptheme.secondaryButton("Export CSV");

        btnTampilkan.addActionListener(e -> tampilkanLaporan());
        btnCetak.addActionListener(e -> cetakLaporan());
        btnExport.addActionListener(e -> exportCsv());

        filterPanel.add(btnTampilkan);
        filterPanel.add(btnCetak);
        filterPanel.add(btnExport);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Apptheme.BG_PAGE);
        wrapper.add(gradBar, BorderLayout.NORTH);
        wrapper.add(filterPanel, BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel buildContent() {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Apptheme.BG_CARD);

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Apptheme.BG_PAGE);
        p.setBorder(new EmptyBorder(12, 16, 0, 16));
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(245, 243, 255));
        footer.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, Apptheme.PRIMARY_LIGHT),
                new EmptyBorder(10, 20, 10, 20)
        ));

        lblTotal.setFont(Apptheme.FONT_SUBHEAD);
        lblTotal.setForeground(Apptheme.PRIMARY_DARK);

        JLabel hint = new JLabel("Format tanggal: yyyy-mm-dd  •  Contoh: 2026-01-01");
        hint.setFont(Apptheme.FONT_SMALL);
        hint.setForeground(Apptheme.TEXT_HINT);

        footer.add(lblTotal, BorderLayout.WEST);
        footer.add(hint,     BorderLayout.EAST);
        return footer;
    }

    // ── Logic ─────────────────────────────────────────────────────
    private void tampilkanLaporan() {
        try {
            LocalDate awal  = LocalDate.parse(txtTanggalAwal.getText().trim());
            LocalDate akhir = LocalDate.parse(txtTanggalAkhir.getText().trim());

            if (awal.isAfter(akhir)) {
                showWarn("Tanggal awal tidak boleh lebih besar dari tanggal akhir.");
                return;
            }

            List<Transaksi> data = controller.getLaporan(awal, akhir);
            tableModel.setRowCount(0);
            double totalPendapatan = 0;
            int transaksiDihitung = 0;

            for (Transaksi t : data) {
                tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getTanggalMasuk(),
                    t.getPelanggan().getNama(),
                    t.getLayanan().getNamaLayanan(),
                    t.getBerat(),
                    Apptheme.formatRupiah(t.getTotalHarga()),
                    t.getStatus()
                });
                
                // Sinkronkan: Hanya hitung pendapatan dari transaksi yang sudah "Diambil"
                if ("Diambil".equalsIgnoreCase(t.getStatus())) {
                    totalPendapatan += t.getTotalHarga();
                    transaksiDihitung++;
                }
            }

            lblTotal.setText("Total Pendapatan (Diambil): " + Apptheme.formatRupiah(totalPendapatan)
                    + "   (" + transaksiDihitung + " dari " + data.size() + " transaksi)");

        } catch (java.time.format.DateTimeParseException e) {
            showWarn("Format tanggal salah. Gunakan yyyy-mm-dd. Contoh: 2026-05-01");
        }
    }

    private void cetakLaporan() {
        if (tableModel.getRowCount() == 0) {
            showWarn("Tidak ada data untuk dicetak.");
            return;
        }
        try {
            boolean selesai = table.print(
                    JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat("Laporan Transaksi LaundryPro"),
                    new java.text.MessageFormat("Halaman {0}")
            );
            if (selesai) showInfo("Laporan berhasil dicetak.");
        } catch (java.awt.print.PrinterException e) {
            showWarn("Gagal mencetak: " + e.getMessage());
        }
    }

    private void exportCsv() {
        if (tableModel.getRowCount() == 0) {
            showWarn("Tidak ada data untuk diexport.");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("laporan_laundry_"
                + txtTanggalAwal.getText() + "_" + txtTanggalAkhir.getText() + ".csv"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            // Tambahkan ekstensi .csv jika belum ada
            if (!file.getName().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            try (PrintWriter w = new PrintWriter(new FileWriter(file, java.nio.charset.StandardCharsets.UTF_8))) {
                // BOM untuk Excel (opsional, membantu buka di Excel Windows)
                w.print('\uFEFF');
                w.println("ID,Tanggal,Pelanggan,Layanan,Berat,Total,Status");
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    w.println(
                        tableModel.getValueAt(i, 0) + "," +
                        tableModel.getValueAt(i, 1) + "," +
                        csvEscape(tableModel.getValueAt(i, 2)) + "," +
                        csvEscape(tableModel.getValueAt(i, 3)) + "," +
                        tableModel.getValueAt(i, 4) + "," +
                        tableModel.getValueAt(i, 5) + "," +
                        tableModel.getValueAt(i, 6)
                    );
                }
                showInfo("Laporan berhasil diexport ke:\n" + file.getAbsolutePath());
            } catch (Exception e) {
                showWarn("Gagal export CSV: " + e.getMessage());
            }
        }
    }

    /** Bungkus nilai dengan tanda kutip jika mengandung koma */
    private String csvEscape(Object val) {
        if (val == null) return "";
        String s = val.toString();
        return s.contains(",") ? "\"" + s.replace("\"", "\"\"") + "\"" : s;
    }

    private void showInfo(String m) { JOptionPane.showMessageDialog(this, m, "Berhasil",  JOptionPane.INFORMATION_MESSAGE); }
    private void showWarn(String m) { JOptionPane.showMessageDialog(this, m, "Perhatian", JOptionPane.WARNING_MESSAGE); }
}