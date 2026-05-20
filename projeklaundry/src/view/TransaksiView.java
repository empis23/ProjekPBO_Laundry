/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import controller.LayananController;
import controller.PelangganController;
import controller.TransaksiController;
import model.Layanan;
import model.Pelanggan;
import model.Transaksi;

/**
 *
 * @author pf344
 */
public class TransaksiView extends JFrame {
    private final TransaksiController transaksiController = new TransaksiController();
    private final PelangganController pelangganController = new PelangganController();
    private final LayananController layananController = new LayananController();

    private final JTextField txtId = new JTextField();
    private final JComboBox<Pelanggan> cmbPelanggan = new JComboBox<>();
    private final JComboBox<Layanan> cmbLayanan = new JComboBox<>();
    private final JTextField txtTanggal = new JTextField(LocalDate.now().toString());
    private final JTextField txtBerat = new JTextField();
    private final JTextField txtTotal = new JTextField();
    private final JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Diproses", "Selesai", "Diambil"});

    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable table = new JTable(tableModel);

    public TransaksiView() {
        setTitle("Transaksi Laundry");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        txtId.setEditable(false);
        txtTotal.setEditable(false);

        JPanel panelForm = new JPanel(new GridLayout(7, 2, 10, 10));
        panelForm.setBorder(new EmptyBorder(15, 15, 5, 15));
        panelForm.add(new JLabel("ID Transaksi"));
        panelForm.add(txtId);
        panelForm.add(new JLabel("Pelanggan"));
        panelForm.add(cmbPelanggan);
        panelForm.add(new JLabel("Layanan"));
        panelForm.add(cmbLayanan);
        panelForm.add(new JLabel("Tanggal Masuk yyyy-mm-dd"));
        panelForm.add(txtTanggal);
        panelForm.add(new JLabel("Berat kg"));
        panelForm.add(txtBerat);
        panelForm.add(new JLabel("Total Harga"));
        panelForm.add(txtTotal);
        panelForm.add(new JLabel("Status"));
        panelForm.add(cmbStatus);
        add(panelForm, BorderLayout.NORTH);

        tableModel.addColumn("ID");
        tableModel.addColumn("Tanggal");
        tableModel.addColumn("Pelanggan");
        tableModel.addColumn("Layanan");
        tableModel.addColumn("Berat");
        tableModel.addColumn("Total");
        tableModel.addColumn("Status");
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelButton = new JPanel();
        panelButton.setBorder(new EmptyBorder(5, 15, 15, 15));
        JButton btnHitung = new JButton("Hitung");
        JButton btnSimpan = new JButton("Simpan");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnBersih = new JButton("Bersih");
        JButton btnReload = new JButton("Reload");

        panelButton.add(btnHitung);
        panelButton.add(btnSimpan);
        panelButton.add(btnUbah);
        panelButton.add(btnHapus);
        panelButton.add(btnBersih);
        panelButton.add(btnReload);
        add(panelButton, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> pilihData());
        btnHitung.addActionListener(e -> hitungTotal());
        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnBersih.addActionListener(e -> bersih());
        btnReload.addActionListener(e -> reloadSemua());

        reloadSemua();
    }

    private void reloadSemua() {
        isiComboPelanggan();
        isiComboLayanan();
        tampilData(transaksiController.getSemuaTransaksi());
    }

    private void isiComboPelanggan() {
        cmbPelanggan.removeAllItems();
        for (Pelanggan p : pelangganController.getSemuaPelanggan()) {
            cmbPelanggan.addItem(p);
        }
    }

    private void isiComboLayanan() {
        cmbLayanan.removeAllItems();
        for (Layanan l : layananController.getSemuaLayanan()) {
            cmbLayanan.addItem(l);
        }
    }

    private void tampilData(List<Transaksi> data) {
        tableModel.setRowCount(0);
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
        }
    }

    private void hitungTotal() {
        try {
            Layanan layanan = (Layanan) cmbLayanan.getSelectedItem();
            double berat = Double.parseDouble(txtBerat.getText().trim());

            if (layanan == null) {
                JOptionPane.showMessageDialog(this, "Data layanan masih kosong.");
                return;
            }

            double total = berat * layanan.getHargaPerKg();
            txtTotal.setText(String.valueOf(total));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Berat harus diisi dengan angka, contoh: 3.5");
        }
    }

    private void simpan() {
        try {
            Transaksi transaksi = ambilDataForm(0);
            transaksi.setTotalHarga(transaksi.hitungTotal());

            if (transaksiController.tambahTransaksi(transaksi)) {
                JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan.");
                bersih();
                tampilData(transaksiController.getSemuaTransaksi());
            } else {
                JOptionPane.showMessageDialog(this, "Transaksi gagal disimpan.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Periksa lagi data transaksi. " + e.getMessage());
        }
    }

    private void ubah() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin diubah.");
            return;
        }

        try {
            Transaksi transaksi = ambilDataForm(Integer.parseInt(txtId.getText()));
            transaksi.setTotalHarga(transaksi.hitungTotal());

            if (transaksiController.ubahTransaksi(transaksi)) {
                JOptionPane.showMessageDialog(this, "Transaksi berhasil diubah.");
                bersih();
                tampilData(transaksiController.getSemuaTransaksi());
            } else {
                JOptionPane.showMessageDialog(this, "Transaksi gagal diubah.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Periksa lagi data transaksi. " + e.getMessage());
        }
    }

    private void hapus() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin dihapus.");
            return;
        }

        int pilih = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus transaksi ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (pilih == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            if (transaksiController.hapusTransaksi(id)) {
                JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus.");
                bersih();
                tampilData(transaksiController.getSemuaTransaksi());
            } else {
                JOptionPane.showMessageDialog(this, "Transaksi gagal dihapus.");
            }
        }
    }

    private Transaksi ambilDataForm(int id) {
        Pelanggan pelanggan = (Pelanggan) cmbPelanggan.getSelectedItem();
        Layanan layanan = (Layanan) cmbLayanan.getSelectedItem();

        if (pelanggan == null || layanan == null) {
            throw new IllegalArgumentException("Pelanggan dan layanan wajib dipilih.");
        }

        Transaksi transaksi = new Transaksi();
        transaksi.setId(id);
        transaksi.setPelanggan(pelanggan);
        transaksi.setLayanan(layanan);
        transaksi.setTanggalMasuk(LocalDate.parse(txtTanggal.getText().trim()));
        transaksi.setBerat(Double.parseDouble(txtBerat.getText().trim()));
        transaksi.setStatus(cmbStatus.getSelectedItem().toString());
        transaksi.setTotalHarga(transaksi.hitungTotal());
        txtTotal.setText(String.valueOf(transaksi.getTotalHarga()));
        return transaksi;
    }

    private void pilihData() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtTanggal.setText(tableModel.getValueAt(row, 1).toString());
            pilihComboPelanggan(tableModel.getValueAt(row, 2).toString());
            pilihComboLayanan(tableModel.getValueAt(row, 3).toString());
            txtBerat.setText(tableModel.getValueAt(row, 4).toString());
            txtTotal.setText(tableModel.getValueAt(row, 5).toString());
            cmbStatus.setSelectedItem(tableModel.getValueAt(row, 6).toString());
        }
    }

    private void pilihComboPelanggan(String nama) {
        for (int i = 0; i < cmbPelanggan.getItemCount(); i++) {
            if (cmbPelanggan.getItemAt(i).getNama().equals(nama)) {
                cmbPelanggan.setSelectedIndex(i);
                return;
            }
        }
    }

    private void pilihComboLayanan(String namaLayanan) {
        for (int i = 0; i < cmbLayanan.getItemCount(); i++) {
            if (cmbLayanan.getItemAt(i).getNamaLayanan().equals(namaLayanan)) {
                cmbLayanan.setSelectedIndex(i);
                return;
            }
        }
    }

    private void bersih() {
        txtId.setText("");
        txtTanggal.setText(LocalDate.now().toString());
        txtBerat.setText("");
        txtTotal.setText("");
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
    }
}
