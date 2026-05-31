package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import model.Layanan;

public class LayananView extends JFrame {
    private final LayananController controller = new LayananController();

    private final JTextField txtId = new JTextField();

    private final JComboBox<String> cmbNamaLayanan = new JComboBox<>(new String[]{
            "Cuci Kering",
            "Cuci Setrika",
            "Setrika Saja",
            "Laundry Express",
            "Cuci Lipat",
            "Cuci Sepatu",
            "Cuci Boneka",
            "Cuci Bed Cover",
            "Cuci Selimut",
            "Cuci Karpet",
            "Cuci Gorden"
    });

    private final JComboBox<String> cmbSatuan = new JComboBox<>(new String[]{
            "per kg",
            "per pasang",
            "per item",
            "per lembar"
    });

    private final JTextField txtHarga = new JTextField();
    private final JTextField txtEstimasi = new JTextField();
    private final JTextField txtCari = new JTextField();

    private final DefaultTableModel tableModel = new DefaultTableModel();
    private final JTable table = new JTable(tableModel);

    public LayananView() {
        setTitle("Data Layanan Laundry");
        setSize(820, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        txtId.setEditable(false);
        txtHarga.setEditable(false);
        txtEstimasi.setEditable(false);
        cmbSatuan.setEnabled(false);

        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 10));
        panelForm.setBorder(new EmptyBorder(15, 15, 5, 15));

        panelForm.add(new JLabel("ID Layanan"));
        panelForm.add(txtId);

        panelForm.add(new JLabel("Jenis Layanan"));
        panelForm.add(cmbNamaLayanan);

        panelForm.add(new JLabel("Satuan Hitung"));
        panelForm.add(cmbSatuan);

        panelForm.add(new JLabel("Harga"));
        panelForm.add(txtHarga);

        panelForm.add(new JLabel("Estimasi"));
        panelForm.add(txtEstimasi);

        add(panelForm, BorderLayout.NORTH);

        tableModel.addColumn("ID");
        tableModel.addColumn("Nama Layanan");
        tableModel.addColumn("Harga");
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

        cmbNamaLayanan.addActionListener(e -> aturDetailLayanan());

        table.getSelectionModel().addListSelectionListener(e -> pilihData());

        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnBersih.addActionListener(e -> bersih());
        btnReload.addActionListener(e -> tampilData(controller.getSemuaLayanan()));
        btnCari.addActionListener(e -> tampilData(controller.cariLayanan(txtCari.getText().trim())));

        aturDetailLayanan();
        tampilData(controller.getSemuaLayanan());
    }

    private void aturDetailLayanan() {
        String layanan = cmbNamaLayanan.getSelectedItem().toString();

        switch (layanan) {
            case "Cuci Kering":
                setDetail("per kg", 7000, "2 hari");
                break;

            case "Cuci Setrika":
                setDetail("per kg", 10000, "2 hari");
                break;

            case "Setrika Saja":
                setDetail("per kg", 6000, "1 hari");
                break;

            case "Laundry Express":
                setDetail("per kg", 15000, "1 hari");
                break;

            case "Cuci Lipat":
                setDetail("per kg", 8000, "2 hari");
                break;

            case "Cuci Sepatu":
                setDetail("per pasang", 30000, "3 hari");
                break;

            case "Cuci Boneka":
                setDetail("per item", 25000, "3 hari");
                break;

            case "Cuci Bed Cover":
                setDetail("per item", 35000, "3 hari");
                break;

            case "Cuci Selimut":
                setDetail("per item", 30000, "3 hari");
                break;

            case "Cuci Karpet":
                setDetail("per item", 50000, "4 hari");
                break;

            case "Cuci Gorden":
                setDetail("per lembar", 20000, "3 hari");
                break;

            default:
                setDetail("per kg", 0, "-");
                break;
        }
    }

    private void setDetail(String satuan, double harga, String estimasi) {
        cmbSatuan.setSelectedItem(satuan);
        txtHarga.setText(String.valueOf(harga));
        txtEstimasi.setText(estimasi);
    }

    private String getNamaLayananLengkap() {
        String nama = cmbNamaLayanan.getSelectedItem().toString();
        String satuan = cmbSatuan.getSelectedItem().toString();

        return nama + " - " + satuan;
    }

    private void tampilData(List<Layanan> data) {
        tableModel.setRowCount(0);

        for (Layanan l : data) {
            tableModel.addRow(new Object[]{
                    l.getId(),
                    l.getNamaLayanan(),
                    l.getHargaPerKg(),
                    l.getEstimasi()
            });
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data layanan belum lengkap.");
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data layanan belum lengkap.");
        }
    }

    private void hapus() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data layanan yang ingin dihapus.");
            return;
        }

        int pilih = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus data ini?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

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
        return new Layanan(
                id,
                getNamaLayananLengkap(),
                Double.parseDouble(txtHarga.getText().trim()),
                txtEstimasi.getText().trim()
        );
    }

    private void pilihData() {
        int row = table.getSelectedRow();

        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());

            String namaLengkap = tableModel.getValueAt(row, 1).toString();
            pilihComboDariNama(namaLengkap);

            txtHarga.setText(tableModel.getValueAt(row, 2).toString());
            txtEstimasi.setText(tableModel.getValueAt(row, 3) == null ? "" : tableModel.getValueAt(row, 3).toString());
        }
    }

    private void pilihComboDariNama(String namaLengkap) {
        String nama = namaLengkap;
        String satuan = "per kg";

        if (namaLengkap.contains(" - ")) {
            String[] bagian = namaLengkap.split(" - ");
            nama = bagian[0];
            satuan = bagian[1];
        }

        cmbNamaLayanan.setSelectedItem(nama);
        cmbSatuan.setSelectedItem(satuan);
    }

    private void bersih() {
        txtId.setText("");
        cmbNamaLayanan.setSelectedIndex(0);
        aturDetailLayanan();
        table.clearSelection();
    }
}