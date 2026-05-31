package view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import controller.PelangganController;
import model.Pelanggan;

public class PelangganView extends JFrame {

    private final PelangganController controller = new PelangganController();

    private final JTextField txtId     = Apptheme.textField();
    private final JTextField txtNama   = Apptheme.textField();
    private final JTextField txtNoHp   = Apptheme.textField();
    private final JTextArea  txtAlamat = Apptheme.textArea(3, 20);
    private final JTextField txtCari   = Apptheme.textField(20);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Nama", "No HP", "Alamat"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);

    public PelangganView() {
        setTitle("Data Pelanggan — LaundryPro");
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        txtId.setEditable(false);
        txtId.setBackground(Apptheme.BG_PAGE);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Apptheme.BG_PAGE);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildContent(), BorderLayout.CENTER);
        root.add(buildToolbar(), BorderLayout.SOUTH);

        setContentPane(root);

        Apptheme.styleTable(table);
        table.getSelectionModel().addListSelectionListener(e -> pilihData());

        tampilData(controller.getSemuaPelanggan());
    }

    private JPanel buildHeader() {
        JPanel header = Apptheme.gradientPanel();
        header.setPreferredSize(new Dimension(0, Apptheme.HEADER_HEIGHT));
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(10, 24, 10, 24));

        JLabel title = new JLabel("Data Pelanggan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JPanel searchBox = new JPanel(new BorderLayout(8, 0));
        searchBox.setOpaque(false);
        searchBox.setPreferredSize(new Dimension(330, 40));

        txtCari.setPreferredSize(new Dimension(230, 38));

        JButton btnCari = Apptheme.secondaryButton("Cari");
        btnCari.setPreferredSize(new Dimension(85, 38));
        btnCari.addActionListener(e ->
                tampilData(controller.cariPelanggan(txtCari.getText().trim()))
        );

        searchBox.add(txtCari, BorderLayout.CENTER);
        searchBox.add(btnCari, BorderLayout.EAST);

        header.add(title, BorderLayout.WEST);
        header.add(searchBox, BorderLayout.EAST);

        return header;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(16, 0));
        content.setBackground(Apptheme.BG_PAGE);
        content.setBorder(new EmptyBorder(18, 18, 0, 18));

        content.add(buildForm(), BorderLayout.WEST);
        content.add(buildTable(), BorderLayout.CENTER);

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

        card.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel heading = new JLabel("Form Pelanggan");
        heading.setFont(Apptheme.FONT_SUBHEAD);
        heading.setForeground(Apptheme.PRIMARY);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(heading, gbc);

        JSeparator sep = new JSeparator();

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        card.add(sep, gbc);

        addFormRow(card, gbc, "ID Pelanggan", txtId);
        addFormRow(card, gbc, "Nama *", txtNama);
        addFormRow(card, gbc, "No HP", txtNoHp);

        JLabel lblAlamat = Apptheme.fieldLabel("Alamat");

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(lblAlamat, gbc);

        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);
        scrollAlamat.setPreferredSize(new Dimension(240, 110));
        scrollAlamat.setMinimumSize(new Dimension(240, 110));
        scrollAlamat.setBorder(BorderFactory.createCompoundBorder(
                new Apptheme.ShadowBorder(6, 8),
                BorderFactory.createCompoundBorder(
                        new Apptheme.RoundBorder(new Color(205, 210, 220), 1, 8),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)
                )
        ));

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(scrollAlamat, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        card.add(Box.createVerticalGlue(), gbc);

        return card;
    }

    private void addFormRow(JPanel parent, GridBagConstraints gbc, String label, JComponent field) {
        JLabel lbl = Apptheme.fieldLabel(label);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        parent.add(lbl, gbc);

        field.setPreferredSize(new Dimension(240, Apptheme.FIELD_HEIGHT));
        field.setMinimumSize(new Dimension(240, Apptheme.FIELD_HEIGHT));

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
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
        JButton btnHapus  = Apptheme.dangerButton("Hapus");
        JButton btnUbah   = Apptheme.secondaryButton("Ubah");
        JButton btnSimpan = Apptheme.primaryButton("+ Simpan");

        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnBersih.addActionListener(e -> bersih());
        btnReload.addActionListener(e -> tampilData(controller.getSemuaPelanggan()));

        bar.add(btnBersih);
        bar.add(btnReload);
        bar.add(btnHapus);
        bar.add(btnUbah);
        bar.add(btnSimpan);

        return bar;
    }

    private void tampilData(List<Pelanggan> data) {
        tableModel.setRowCount(0);

        for (Pelanggan p : data) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getNama(),
                    p.getNoHp(),
                    p.getAlamat()
            });
        }
    }

    private void simpan() {
        if (txtNama.getText().trim().isEmpty()) {
            showWarn("Nama pelanggan wajib diisi.");
            return;
        }

        Pelanggan p = new Pelanggan(
                0,
                txtNama.getText().trim(),
                txtNoHp.getText().trim(),
                txtAlamat.getText().trim()
        );

        if (controller.tambahPelanggan(p)) {
            showInfo("Data pelanggan berhasil disimpan.");
            bersih();
            tampilData(controller.getSemuaPelanggan());
        } else {
            showWarn("Gagal menyimpan data pelanggan.");
        }
    }

    private void ubah() {
        if (txtId.getText().isEmpty()) {
            showWarn("Pilih data pelanggan yang ingin diubah.");
            return;
        }

        Pelanggan p = new Pelanggan(
                Integer.parseInt(txtId.getText()),
                txtNama.getText().trim(),
                txtNoHp.getText().trim(),
                txtAlamat.getText().trim()
        );

        if (controller.ubahPelanggan(p)) {
            showInfo("Data pelanggan berhasil diubah.");
            bersih();
            tampilData(controller.getSemuaPelanggan());
        } else {
            showWarn("Gagal mengubah data pelanggan.");
        }
    }

    private void hapus() {
        if (txtId.getText().isEmpty()) {
            showWarn("Pilih data pelanggan yang ingin dihapus.");
            return;
        }

        int pilih = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus pelanggan ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (pilih == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());

            if (controller.hapusPelanggan(id)) {
                showInfo("Data pelanggan berhasil dihapus.");
                bersih();
                tampilData(controller.getSemuaPelanggan());
            } else {
                showWarn("Gagal menghapus. Pelanggan mungkin sudah digunakan dalam transaksi.");
            }
        }
    }

    private void pilihData() {
        int row = table.getSelectedRow();

        if (row >= 0) {
            txtId.setText(safeGet(row, 0));
            txtNama.setText(safeGet(row, 1));
            txtNoHp.setText(safeGet(row, 2));
            txtAlamat.setText(safeGet(row, 3));
        }
    }

    private void bersih() {
        txtId.setText("");
        txtNama.setText("");
        txtNoHp.setText("");
        txtAlamat.setText("");
        table.clearSelection();
        txtNama.requestFocus();
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