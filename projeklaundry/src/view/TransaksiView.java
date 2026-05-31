package view;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import controller.TransaksiController;
import controller.PelangganController;
import controller.LayananController;
import model.Transaksi;
import model.Pelanggan;
import model.Layanan;

public class TransaksiView extends JFrame {

    private final TransaksiController txController = new TransaksiController();
    private final PelangganController pxController = new PelangganController();
    private final LayananController lxController = new LayananController();

    private final JTextField txtId = Apptheme.textField();
    private final JComboBox<String> cmbPelanggan = new JComboBox<>();
    private final JComboBox<String> cmbLayanan = new JComboBox<>();
    private final JTextField txtTanggal = Apptheme.textField();
    private final JTextField txtBerat = Apptheme.textField();
    private final JTextField txtTotal = Apptheme.textField();
    private final JComboBox<String> cmbStatus = new JComboBox<>(
            new String[]{"Diproses", "Selesai", "Diambil"}
    );

    private final JLabel lblJumlah = Apptheme.fieldLabel("Berat (Kg) *");

    private List<Pelanggan> listPelanggan;
    private List<Layanan> listLayanan;
    private List<Transaksi> listTransaksi;

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Tanggal", "Pelanggan", "Layanan", "Jumlah/Berat", "Total", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);

    public TransaksiView() {
        setTitle("Transaksi Laundry — LaundryPro");
        setSize(980, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        txtId.setEditable(false);
        txtId.setBackground(Apptheme.BG_PAGE);
        txtTanggal.setText(LocalDate.now().toString());
        txtTotal.setEditable(false);
        txtTotal.setBackground(Apptheme.BG_PAGE);



        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Apptheme.BG_PAGE);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildContent(), BorderLayout.CENTER);
        root.add(buildToolbar(), BorderLayout.SOUTH);

        setContentPane(root);

        Apptheme.styleTable(table);
        table.getSelectionModel().addListSelectionListener(e -> pilihData());

        styleCombo(cmbPelanggan);
        styleCombo(cmbLayanan);
        styleCombo(cmbStatus);

        muatComboBoxes();

        txtBerat.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                hitungTotal();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                hitungTotal();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                hitungTotal();
            }
        });

        cmbLayanan.addActionListener(e -> {
            hitungTotal();
            updateBeratLabel();
        });

        tampilData();
    }

    private JPanel buildHeader() {
        JPanel header = Apptheme.gradientPanel();
        header.setPreferredSize(new Dimension(0, Apptheme.HEADER_HEIGHT));
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(10, 24, 10, 24));

        JLabel title = new JLabel("Transaksi Laundry");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        header.add(title, BorderLayout.WEST);

        return header;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(16, 0));
        content.setBackground(Apptheme.BG_PAGE);
        content.setBorder(new EmptyBorder(18, 18, 0, 18));

        JPanel formPanel = buildForm();
        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        formScroll.setPreferredSize(new Dimension(350, 0)); 
        content.add(formScroll, BorderLayout.WEST);

        JScrollPane tableScroll = buildTable();
        content.add(tableScroll, BorderLayout.CENTER);
        
        return content;
    }

    private JPanel buildForm() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Apptheme.BG_CARD);

        card.setBorder(BorderFactory.createCompoundBorder(
                new Apptheme.ShadowBorder(8, 12),
                BorderFactory.createCompoundBorder(
                        new Apptheme.RoundBorder(Apptheme.BORDER, 1, 12),
                        new EmptyBorder(18, 18, 18, 18)
                )
        ));

        card.setPreferredSize(new Dimension(315, 800));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel heading = new JLabel("Form Transaksi");
        heading.setFont(Apptheme.FONT_SUBHEAD);
        heading.setForeground(Apptheme.PRIMARY);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(heading, gbc);

        JSeparator sep = new JSeparator();

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        card.add(sep, gbc);

        addFormRow(card, gbc, "ID Transaksi", txtId);
        addFormRow(card, gbc, "Pelanggan *", cmbPelanggan);
        addFormRow(card, gbc, "Layanan *", cmbLayanan);
        addFormRow(card, gbc, "Tanggal (yyyy-mm-dd)", txtTanggal);
        addFormRow(card, gbc, lblJumlah, txtBerat);
        addFormRow(card, gbc, "Total Harga", txtTotal);
        addFormRow(card, gbc, "Status", cmbStatus);

        JLabel hint = new JLabel("Total otomatis dihitung dari layanan dan berat.");
        hint.setFont(Apptheme.FONT_SMALL);
        hint.setForeground(Apptheme.TEXT_HINT);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(hint, gbc);

        // Add vertical glue to push components up and allow scrolling if needed
        gbc.gridy++;
        gbc.weighty = 2.0;
        gbc.fill = GridBagConstraints.BOTH;
        card.add(Box.createVerticalGlue(), gbc);

        return card;
    }

    private void addFormRow(JPanel parent, GridBagConstraints gbc, String label, JComponent field) {
        addFormRow(parent, gbc, Apptheme.fieldLabel(label), field);
    }

    private void addFormRow(JPanel parent, GridBagConstraints gbc, JLabel lbl, JComponent field) {
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        parent.add(lbl, gbc);

        field.setPreferredSize(new Dimension(255, Apptheme.FIELD_HEIGHT));
        field.setMinimumSize(new Dimension(255, Apptheme.FIELD_HEIGHT));

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 13, 0);
        parent.add(field, gbc);
    }

    private JScrollPane buildTable() {
        JScrollPane scroll = new JScrollPane(table);

        scroll.setBorder(BorderFactory.createCompoundBorder(
                new Apptheme.ShadowBorder(8, 12),
                new Apptheme.RoundBorder(Apptheme.BORDER, 1, 12)
        ));

        scroll.getViewport().setBackground(Apptheme.BG_CARD);

        return scroll;
    }

    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        bar.setBackground(Apptheme.BG_PAGE);
        bar.setBorder(new MatteBorder(1, 0, 0, 0, Apptheme.BORDER));

        JButton btnBersih = Apptheme.neutralButton("Bersih");
        JButton btnReload = Apptheme.neutralButton("Reload");
        JButton btnHapus = Apptheme.dangerButton("Hapus");
        JButton btnUbah = Apptheme.secondaryButton("Ubah");
        JButton btnSimpan = Apptheme.primaryButton("+ Simpan");

        btnBersih.addActionListener(e -> bersih());
        btnReload.addActionListener(e -> {
            muatComboBoxes();
            tampilData();
        });
        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());

        bar.add(btnBersih);
        bar.add(btnReload);
        bar.add(btnHapus);
        bar.add(btnUbah);
        bar.add(btnSimpan);

        return bar;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(Apptheme.FONT_BODY);
        cb.setForeground(Apptheme.TEXT_PRIMARY);
        cb.setBackground(Color.WHITE);
        cb.setBorder(BorderFactory.createCompoundBorder(
                new Apptheme.ShadowBorder(6, Apptheme.RADIUS),
                BorderFactory.createCompoundBorder(
                        new Apptheme.RoundBorder(new Color(205, 210, 220), 1, Apptheme.RADIUS),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)
                )
        ));
    }

    private void muatComboBoxes() {
        cmbPelanggan.removeAllItems();
        cmbPelanggan.addItem("-- Pilih Pelanggan --");

        listPelanggan = pxController.getSemuaPelanggan();

        for (Pelanggan p : listPelanggan) {
            cmbPelanggan.addItem(p.getNama() + " (" + p.getNoHp() + ")");
        }

        cmbLayanan.removeAllItems();
        cmbLayanan.addItem("-- Pilih Layanan --");

        listLayanan = lxController.getSemuaLayanan();

        for (Layanan l : listLayanan) {
            String suffix = "";
            String lower = l.getNamaLayanan().toLowerCase();
            if (lower.contains("per kg") || lower.contains("kg")) {
                suffix = " per kg";
            } else if (lower.contains("per lembar") || lower.contains("lembar")) {
                suffix = " per lembar";
            } else if (lower.contains("per item") || lower.contains("item")) {
                suffix = " per item";
            } else if (lower.contains("per pasang") || lower.contains("pasang")) {
                suffix = " per pasang";
            } else {
                suffix = " per kg";
            }
            String displayName = l.getNamaLayanan();
            if (displayName.contains(" - ")) {
                displayName = displayName.split(" - ")[0];
            }
            cmbLayanan.addItem(displayName + " - " + Apptheme.formatRupiah(l.getHargaPerKg()) + suffix);
        }
    }

    private void hitungTotal() {
        try {
            int idxLayanan = cmbLayanan.getSelectedIndex();

            if (idxLayanan <= 0 || txtBerat.getText().trim().isEmpty()) {
                txtTotal.setText("");
                return;
            }

            double berat = Double.parseDouble(txtBerat.getText().trim());
            double harga = listLayanan.get(idxLayanan - 1).getHargaPerKg();
            double total = berat * harga;

            txtTotal.setText(Apptheme.formatRupiah(total));

        } catch (Exception e) {
            txtTotal.setText("");
        }
    }

    private void tampilData() {
        tableModel.setRowCount(0);
        listTransaksi = txController.getSemuaTransaksi();
        
        for (Transaksi t : listTransaksi) {
            String status = t.getStatus();
            if (status != null) {
                status = status.trim();
            }
            if ("Diambil".equalsIgnoreCase(status)) {
            continue;
            }
            tableModel.addRow(new Object[]{
                t.getId(),
                t.getTanggalMasuk(),
                t.getPelanggan().getNama(),
                t.getLayanan().getNamaLayanan(),
                t.getBerat(),
                Apptheme.formatRupiah(t.getTotalHarga()),
                status
            });
        }
    }

    private void simpan() {
        int idxPelanggan = cmbPelanggan.getSelectedIndex();
        int idxLayanan = cmbLayanan.getSelectedIndex();

        if (idxPelanggan <= 0) {
            showWarn("Pilih pelanggan terlebih dahulu.");
            return;
        }

        if (idxLayanan <= 0) {
            showWarn("Pilih layanan terlebih dahulu.");
            return;
        }

        if (txtBerat.getText().trim().isEmpty()) {
            showWarn("Berat wajib diisi.");
            return;
        }

        try {
            LocalDate tanggal = LocalDate.parse(txtTanggal.getText().trim());
            double berat = Double.parseDouble(txtBerat.getText().trim());

            if (berat <= 0) {
                showWarn("Berat harus lebih dari 0.");
                return;
            }

            Pelanggan pelanggan = listPelanggan.get(idxPelanggan - 1);
            Layanan layanan = listLayanan.get(idxLayanan - 1);
            double total = berat * layanan.getHargaPerKg();
            String status = (String) cmbStatus.getSelectedItem();

            Transaksi tx = new Transaksi(
                    0,
                    pelanggan,
                    layanan,
                    tanggal,
                    berat,
                    total,
                    status
            );

            if (txController.tambahTransaksi(tx)) {
                showInfo("Transaksi berhasil disimpan.\nTotal: " + Apptheme.formatRupiah(total));
                bersih();
                tampilData();
            } else {
                showWarn("Gagal menyimpan transaksi.");
            }

        } catch (java.time.format.DateTimeParseException e) {
            showWarn("Format tanggal salah. Gunakan yyyy-mm-dd. Contoh: 2026-05-21");
        } catch (NumberFormatException e) {
            showWarn("Berat harus berupa angka. Contoh: 2.5");
        }
    }

    private void pilihData() {
        int row = table.getSelectedRow();

        if (row >= 0) {
            try {
                int id = Integer.parseInt(safeGet(row, 0));
                Transaksi selectedTx = null;
                if (listTransaksi != null) {
                    for (Transaksi t : listTransaksi) {
                        if (t.getId() == id) {
                            selectedTx = t;
                            break;
                        }
                    }
                }

                if (selectedTx != null) {
                    txtId.setText(String.valueOf(selectedTx.getId()));
                    txtTanggal.setText(selectedTx.getTanggalMasuk().toString());
                    txtBerat.setText(String.valueOf(selectedTx.getBerat()));
                    txtTotal.setText(Apptheme.formatRupiah(selectedTx.getTotalHarga()));
                    cmbStatus.setSelectedItem(selectedTx.getStatus());

                    // Select Pelanggan
                    int pelangganIdx = -1;
                    if (listPelanggan != null) {
                        for (int i = 0; i < listPelanggan.size(); i++) {
                            if (listPelanggan.get(i).getId() == selectedTx.getPelanggan().getId()) {
                                pelangganIdx = i + 1;
                                break;
                            }
                        }
                    }
                    cmbPelanggan.setSelectedIndex(pelangganIdx != -1 ? pelangganIdx : 0);

                    // Select Layanan
                    int layananIdx = -1;
                    if (listLayanan != null) {
                        for (int i = 0; i < listLayanan.size(); i++) {
                            if (listLayanan.get(i).getId() == selectedTx.getLayanan().getId()) {
                                layananIdx = i + 1;
                                break;
                            }
                        }
                    }
                    cmbLayanan.setSelectedIndex(layananIdx != -1 ? layananIdx : 0);

                    updateBeratLabel();
                }
            } catch (Exception e) {
                System.out.println("Error pilihData: " + e.getMessage());
            }
        }
    }

    private void bersih() {
        txtId.setText("");
        cmbPelanggan.setSelectedIndex(0);
        cmbLayanan.setSelectedIndex(0);
        txtTanggal.setText(LocalDate.now().toString());
        txtBerat.setText("");
        txtTotal.setText("");
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
        updateBeratLabel();
        cmbPelanggan.requestFocus();
    }

    private void ubah() {
        if (txtId.getText().isEmpty()) {
            showWarn("Pilih data transaksi yang ingin diubah.");
            return;
        }

        int idxPelanggan = cmbPelanggan.getSelectedIndex();
        int idxLayanan = cmbLayanan.getSelectedIndex();

        if (idxPelanggan <= 0) {
            showWarn("Pilih pelanggan terlebih dahulu.");
            return;
        }

        if (idxLayanan <= 0) {
            showWarn("Pilih layanan terlebih dahulu.");
            return;
        }

        if (txtBerat.getText().trim().isEmpty()) {
            showWarn("Jumlah / berat wajib diisi.");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText());
            LocalDate tanggal = LocalDate.parse(txtTanggal.getText().trim());
            double berat = Double.parseDouble(txtBerat.getText().trim());

            if (berat <= 0) {
                showWarn("Jumlah / berat harus lebih dari 0.");
                return;
            }

            Pelanggan pelanggan = listPelanggan.get(idxPelanggan - 1);
            Layanan layanan = listLayanan.get(idxLayanan - 1);
            double total = berat * layanan.getHargaPerKg();
            String status = (String) cmbStatus.getSelectedItem();

            Transaksi tx = new Transaksi(
                    id,
                    pelanggan,
                    layanan,
                    tanggal,
                    berat,
                    total,
                    status
            );

            if (txController.ubahTransaksi(tx)) {
                showInfo("Transaksi berhasil diubah.");
                bersih();
                tampilData();
            } else {
                showWarn("Gagal mengubah transaksi.");
            }

        } catch (java.time.format.DateTimeParseException e) {
            showWarn("Format tanggal salah. Gunakan yyyy-mm-dd. Contoh: 2026-05-21");
        } catch (NumberFormatException e) {
            showWarn("Jumlah / berat harus berupa angka. Contoh: 2.5");
        }
    }

    private void hapus() {
        if (txtId.getText().isEmpty()) {
            showWarn("Pilih data transaksi yang ingin dihapus.");
            return;
        }

        int pilih = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus transaksi ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (pilih == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());

            if (txController.hapusTransaksi(id)) {
                showInfo("Transaksi berhasil dihapus.");
                bersih();
                tampilData();
            } else {
                showWarn("Gagal menghapus transaksi.");
            }
        }
    }

    private void updateBeratLabel() {
        int idxLayanan = cmbLayanan.getSelectedIndex();
        if (idxLayanan <= 0) {
            lblJumlah.setText("Berat (Kg) *");
            return;
        }
        Layanan l = listLayanan.get(idxLayanan - 1);
        String name = l.getNamaLayanan().toLowerCase();
        if (name.contains("per kg") || name.contains("kg")) {
            lblJumlah.setText("Berat (Kg) *");
        } else if (name.contains("per lembar") || name.contains("lembar")) {
            lblJumlah.setText("Jumlah (Lembar) *");
        } else if (name.contains("per item") || name.contains("item")) {
            lblJumlah.setText("Jumlah (Item) *");
        } else if (name.contains("per pasang") || name.contains("pasang")) {
            lblJumlah.setText("Jumlah (Pasang) *");
        } else {
            lblJumlah.setText("Jumlah / Berat *");
        }
    }

    private String safeGet(int row, int col) {
        Object v = tableModel.getValueAt(row, col);
        return v == null ? "" : v.toString();
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWarn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Perhatian", JOptionPane.WARNING_MESSAGE);
    }
}