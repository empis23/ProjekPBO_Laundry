/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import controller.TransaksiController;
import model.Transaksi;

/**
 *
 * @author pf344
 */
public class LaporanView extends JFrame {
    private final TransaksiController controller = new TransaksiController();
    private final JTextField txtTanggalAwal = new JTextField(LocalDate.now().withDayOfMonth(1).toString());
    private final JTextField txtTanggalAkhir = new JTextField(LocalDate.now().toString());
    private final JLabel lblTotalPendapatan = new JLabel("Total Pendapatan: Rp0");
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable table = new JTable(tableModel);

    public LaporanView() {
        setTitle("Laporan Transaksi");
        setSize(900, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelAtas = new JPanel(new GridLayout(2, 3, 10, 10));
        panelAtas.setBorder(new EmptyBorder(15, 15, 5, 15));

        JButton btnTampilkan = new JButton("Tampilkan");
        JButton btnCetak = new JButton("Cetak");
        JButton btnExport = new JButton("Export CSV");

        panelAtas.add(new JLabel("Tanggal Awal yyyy-mm-dd"));
        panelAtas.add(new JLabel("Tanggal Akhir yyyy-mm-dd"));
        panelAtas.add(new JLabel("Aksi"));
        panelAtas.add(txtTanggalAwal);
        panelAtas.add(txtTanggalAkhir);

        JPanel panelAksi = new JPanel();
        panelAksi.add(btnTampilkan);
        panelAksi.add(btnCetak);
        panelAksi.add(btnExport);
        panelAtas.add(panelAksi);

        add(panelAtas, BorderLayout.NORTH);

        tableModel.addColumn("ID");
        tableModel.addColumn("Tanggal");
        tableModel.addColumn("Pelanggan");
        tableModel.addColumn("Layanan");
        tableModel.addColumn("Berat");
        tableModel.addColumn("Total");
        tableModel.addColumn("Status");
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setBorder(new EmptyBorder(5, 15, 15, 15));
        panelBawah.add(lblTotalPendapatan, BorderLayout.WEST);
        add(panelBawah, BorderLayout.SOUTH);

        btnTampilkan.addActionListener(e -> tampilkanLaporan());
        btnCetak.addActionListener(e -> cetakLaporan());
        btnExport.addActionListener(e -> exportCsv());

        tampilkanLaporan();
    }

    private void tampilkanLaporan() {
        try {
            LocalDate awal = LocalDate.parse(txtTanggalAwal.getText().trim());
            LocalDate akhir = LocalDate.parse(txtTanggalAkhir.getText().trim());
            List<Transaksi> data = controller.getLaporan(awal, akhir);

            tableModel.setRowCount(0);
            double totalPendapatan = 0;

            for (Transaksi t : data) {
                tableModel.addRow(new Object[]{
                        t.getId(),
                        t.getTanggalMasuk(),
                        t.getPelanggan().getNama(),
                        t.getLayanan().getNamaLayanan(),
                        t.getBerat(),
                        t.getTotalHarga(),
                        t.getStatus()
                });
                totalPendapatan += t.getTotalHarga();
            }

            lblTotalPendapatan.setText("Total Pendapatan: Rp" + totalPendapatan);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus yyyy-mm-dd. Contoh: 2026-05-02");
        }
    }

    private void cetakLaporan() {
        try {
            boolean selesai = table.print();
            if (selesai) {
                JOptionPane.showMessageDialog(this, "Laporan selesai dicetak.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mencetak laporan: " + e.getMessage());
        }
    }

    private void exportCsv() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk diexport.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("laporan_laundry.csv"));
        int pilihan = chooser.showSaveDialog(this);

        if (pilihan == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("ID,Tanggal,Pelanggan,Layanan,Berat,Total,Status");
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    writer.println(
                            tableModel.getValueAt(i, 0) + "," +
                            tableModel.getValueAt(i, 1) + "," +
                            tableModel.getValueAt(i, 2) + "," +
                            tableModel.getValueAt(i, 3) + "," +
                            tableModel.getValueAt(i, 4) + "," +
                            tableModel.getValueAt(i, 5) + "," +
                            tableModel.getValueAt(i, 6)
                    );
                }
                JOptionPane.showMessageDialog(this, "Laporan berhasil diexport ke CSV.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal export CSV: " + e.getMessage());
            }
        }
    }
}
