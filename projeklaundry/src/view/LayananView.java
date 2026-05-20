/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
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
import model.Layanan;

/**
 *
 * @author pf344
 */
public class LayananView extends JFrame {
    private final LayananController controller = new LayananController();
    private final JTextField txtId = new JTextField();
    private final JTextField txtNamaLayanan = new JTextField();
    private final JTextField txtHarga = new JTextField();
    private final JTextField txtEstimasi = new JTextField();
    private final JTextField txtCari = new JTextField();
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable table = new JTable(tableModel);

    public LayananView() {
        setTitle("Data Layanan Laundry");
        setSize(750, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        txtId.setEditable(false);

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.setBorder(new EmptyBorder(15, 15, 5, 15));
        panelForm.add(new JLabel("ID Layanan"));
        panelForm.add(txtId);
        panelForm.add(new JLabel("Nama Layanan"));
        panelForm.add(txtNamaLayanan);
        panelForm.add(new JLabel("Harga per Kg"));
        panelForm.add(txtHarga);
        panelForm.add(new JLabel("Estimasi"));
        panelForm.add(txtEstimasi);
        add(panelForm, BorderLayout.NORTH);

        tableModel.addColumn("ID");
        tableModel.addColumn("Nama Layanan");
        tableModel.addColumn("Harga per Kg");
        tableModel.addColumn("Estimasi");
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new BorderLayout(10, 10));
        panelBawah.setBorder(new EmptyBorder(5, 15, 15, 15));

        JPanel panelCari = new JPanel(new BorderLayout(5, 5));
        JButton btnCari = new JButton("Cari");
        panelCari.add(new JLabel("Cari layanan: "), BorderLayout.WEST);
        panelCari.add(txtCari, BorderLayout.CENTER);
        panelCari.add(btnCari, BorderLayout.EAST);

        JPanel panelButton = new JPanel();
        JButton btnSimpan = new JButton("Simpan");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnBersih = new JButton("Bersih");
        JButton btnReload = new JButton("Reload");

        panelButton.add(btnSimpan);
        panelButton.add(btnUbah);
        panelButton.add(btnHapus);
        panelButton.add(btnBersih);
        panelButton.add(btnReload);

        panelBawah.add(panelCari, BorderLayout.NORTH);
        panelBawah.add(panelButton, BorderLayout.SOUTH);
        add(panelBawah, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> pilihData());
        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnBersih.addActionListener(e -> bersih());
        btnReload.addActionListener(e -> tampilData(controller.getSemuaLayanan()));
        btnCari.addActionListener(e -> tampilData(controller.cariLayanan(txtCari.getText().trim())));

        tampilData(controller.getSemuaLayanan());
    }

    private void tampilData(List<Layanan> data) {
        tableModel.setRowCount(0);
        for (Layanan l : data) {
            tableModel.addRow(new Object[]{l.getId(), l.getNamaLayanan(), l.getHargaPerKg(), l.getEstimasi()});
        }
    }

    private void simpan() {
        try {
            Layanan layanan = ambilDataForm(0);
            if (controller.tambahLayanan(layanan)) {
                JOptionPane.showMessageDialog(this, "Data layanan berhasil disimpan.");
                bersih();
                tampilData(controller.getSemuaLayanan());
            } else {
                JOptionPane.showMessageDialog(this, "Data layanan gagal disimpan.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka.");
        }
    }

    private void ubah() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data layanan yang ingin diubah.");
            return;
        }

        try {
            Layanan layanan = ambilDataForm(Integer.parseInt(txtId.getText()));
            if (controller.ubahLayanan(layanan)) {
                JOptionPane.showMessageDialog(this, "Data layanan berhasil diubah.");
                bersih();
                tampilData(controller.getSemuaLayanan());
            } else {
                JOptionPane.showMessageDialog(this, "Data layanan gagal diubah.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka.");
        }
    }

    private void hapus() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data layanan yang ingin dihapus.");
            return;
        }

        int pilih = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (pilih == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            if (controller.hapusLayanan(id)) {
                JOptionPane.showMessageDialog(this, "Data layanan berhasil dihapus.");
                bersih();
                tampilData(controller.getSemuaLayanan());
            } else {
                JOptionPane.showMessageDialog(this, "Data layanan gagal dihapus. Mungkin layanan sudah dipakai dalam transaksi.");
            }
        }
    }

    private Layanan ambilDataForm(int id) {
        if (txtNamaLayanan.getText().trim().isEmpty() || txtHarga.getText().trim().isEmpty()) {
            throw new NumberFormatException("Nama dan harga wajib diisi.");
        }

        return new Layanan(
                id,
                txtNamaLayanan.getText().trim(),
                Double.parseDouble(txtHarga.getText().trim()),
                txtEstimasi.getText().trim()
        );
    }

    private void pilihData() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtNamaLayanan.setText(tableModel.getValueAt(row, 1).toString());
            txtHarga.setText(tableModel.getValueAt(row, 2).toString());
            txtEstimasi.setText(tableModel.getValueAt(row, 3) == null ? "" : tableModel.getValueAt(row, 3).toString());
        }
    }

    private void bersih() {
        txtId.setText("");
        txtNamaLayanan.setText("");
        txtHarga.setText("");
        txtEstimasi.setText("");
        table.clearSelection();
    }
}
