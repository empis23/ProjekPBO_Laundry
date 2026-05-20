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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import controller.PelangganController;
import model.Pelanggan;

/**
 *
 * @author pf344
 */
public class PelangganView extends JFrame {
    private final PelangganController controller = new PelangganController();
    private final JTextField txtId = new JTextField();
    private final JTextField txtNama = new JTextField();
    private final JTextField txtNoHp = new JTextField();
    private final JTextArea txtAlamat = new JTextArea(3, 20);
    private final JTextField txtCari = new JTextField();
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable table = new JTable(tableModel);

    public PelangganView() {
        setTitle("Data Pelanggan");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        txtId.setEditable(false);

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.setBorder(new EmptyBorder(15, 15, 5, 15));
        panelForm.add(new JLabel("ID Pelanggan"));
        panelForm.add(txtId);
        panelForm.add(new JLabel("Nama"));
        panelForm.add(txtNama);
        panelForm.add(new JLabel("No HP"));
        panelForm.add(txtNoHp);
        panelForm.add(new JLabel("Alamat"));
        panelForm.add(new JScrollPane(txtAlamat));
        add(panelForm, BorderLayout.NORTH);

        tableModel.addColumn("ID");
        tableModel.addColumn("Nama");
        tableModel.addColumn("No HP");
        tableModel.addColumn("Alamat");
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new BorderLayout(10, 10));
        panelBawah.setBorder(new EmptyBorder(5, 15, 15, 15));

        JPanel panelCari = new JPanel(new BorderLayout(5, 5));
        JButton btnCari = new JButton("Cari");
        panelCari.add(new JLabel("Cari nama/no HP: "), BorderLayout.WEST);
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
        btnReload.addActionListener(e -> tampilData(controller.getSemuaPelanggan()));
        btnCari.addActionListener(e -> tampilData(controller.cariPelanggan(txtCari.getText().trim())));

        tampilData(controller.getSemuaPelanggan());
    }

    private void tampilData(List<Pelanggan> data) {
        tableModel.setRowCount(0);
        for (Pelanggan p : data) {
            tableModel.addRow(new Object[]{p.getId(), p.getNama(), p.getNoHp(), p.getAlamat()});
        }
    }

    private void simpan() {
        if (txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama pelanggan wajib diisi.");
            return;
        }

        Pelanggan p = new Pelanggan(0, txtNama.getText().trim(), txtNoHp.getText().trim(), txtAlamat.getText().trim());
        if (controller.tambahPelanggan(p)) {
            JOptionPane.showMessageDialog(this, "Data pelanggan berhasil disimpan.");
            bersih();
            tampilData(controller.getSemuaPelanggan());
        } else {
            JOptionPane.showMessageDialog(this, "Data pelanggan gagal disimpan.");
        }
    }

    private void ubah() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data pelanggan yang ingin diubah.");
            return;
        }

        Pelanggan p = new Pelanggan(
                Integer.parseInt(txtId.getText()),
                txtNama.getText().trim(),
                txtNoHp.getText().trim(),
                txtAlamat.getText().trim()
        );

        if (controller.ubahPelanggan(p)) {
            JOptionPane.showMessageDialog(this, "Data pelanggan berhasil diubah.");
            bersih();
            tampilData(controller.getSemuaPelanggan());
        } else {
            JOptionPane.showMessageDialog(this, "Data pelanggan gagal diubah.");
        }
    }

    private void hapus() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data pelanggan yang ingin dihapus.");
            return;
        }

        int pilih = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (pilih == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            if (controller.hapusPelanggan(id)) {
                JOptionPane.showMessageDialog(this, "Data pelanggan berhasil dihapus.");
                bersih();
                tampilData(controller.getSemuaPelanggan());
            } else {
                JOptionPane.showMessageDialog(this, "Data pelanggan gagal dihapus. Mungkin pelanggan sudah dipakai dalam transaksi.");
            }
        }
    }

    private void pilihData() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtNama.setText(tableModel.getValueAt(row, 1).toString());
            txtNoHp.setText(tableModel.getValueAt(row, 2) == null ? "" : tableModel.getValueAt(row, 2).toString());
            txtAlamat.setText(tableModel.getValueAt(row, 3) == null ? "" : tableModel.getValueAt(row, 3).toString());
        }
    }

    private void bersih() {
        txtId.setText("");
        txtNama.setText("");
        txtNoHp.setText("");
        txtAlamat.setText("");
        table.clearSelection();
    }
}
