/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import config.Koneksi;
import model.Layanan;
import model.Pelanggan;
import model.Transaksi;

/**
 *
 * @author pf344
 */
public class TransaksiController {
    public List<Transaksi> getSemuaTransaksi() {
        List<Transaksi> data = new ArrayList<>();
        String sql = "SELECT t.*, p.nama, p.no_hp, p.alamat, l.nama_layanan, l.harga_per_kg, l.estimasi "
                + "FROM transaksi t "
                + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                + "JOIN layanan l ON t.id_layanan = l.id_layanan "
                + "ORDER BY t.id_transaksi DESC";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                data.add(mapTransaksi(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error ambil transaksi: " + e.getMessage());
        }

        return data;
    }

    public boolean tambahTransaksi(Transaksi transaksi) {
        String sql = "INSERT INTO transaksi (id_pelanggan, id_layanan, tanggal_masuk, berat, total_harga, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, transaksi.getPelanggan().getId());
            ps.setInt(2, transaksi.getLayanan().getId());
            ps.setDate(3, Date.valueOf(transaksi.getTanggalMasuk()));
            ps.setDouble(4, transaksi.getBerat());
            ps.setDouble(5, transaksi.getTotalHarga());
            ps.setString(6, transaksi.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error tambah transaksi: " + e.getMessage());
            return false;
        }
    }

    public boolean ubahTransaksi(Transaksi transaksi) {
        String sql = "UPDATE transaksi SET id_pelanggan = ?, id_layanan = ?, tanggal_masuk = ?, berat = ?, total_harga = ?, status = ? WHERE id_transaksi = ?";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, transaksi.getPelanggan().getId());
            ps.setInt(2, transaksi.getLayanan().getId());
            ps.setDate(3, Date.valueOf(transaksi.getTanggalMasuk()));
            ps.setDouble(4, transaksi.getBerat());
            ps.setDouble(5, transaksi.getTotalHarga());
            ps.setString(6, transaksi.getStatus());
            ps.setInt(7, transaksi.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error ubah transaksi: " + e.getMessage());
            return false;
        }
    }

    public boolean hapusTransaksi(int id) {
        String sql = "DELETE FROM transaksi WHERE id_transaksi = ?";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error hapus transaksi: " + e.getMessage());
            return false;
        }
    }

    public List<Transaksi> getLaporan(LocalDate tanggalAwal, LocalDate tanggalAkhir) {
        List<Transaksi> data = new ArrayList<>();
        String sql = "SELECT t.*, p.nama, p.no_hp, p.alamat, l.nama_layanan, l.harga_per_kg, l.estimasi "
                + "FROM transaksi t "
                + "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                + "JOIN layanan l ON t.id_layanan = l.id_layanan "
                + "WHERE t.tanggal_masuk BETWEEN ? AND ? "
                + "ORDER BY t.tanggal_masuk DESC";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(tanggalAwal));
            ps.setDate(2, Date.valueOf(tanggalAkhir));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.add(mapTransaksi(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error laporan transaksi: " + e.getMessage());
        }

        return data;
    }

    private Transaksi mapTransaksi(ResultSet rs) throws SQLException {
        Pelanggan pelanggan = new Pelanggan(
                rs.getInt("id_pelanggan"),
                rs.getString("nama"),
                rs.getString("no_hp"),
                rs.getString("alamat")
        );

        Layanan layanan = new Layanan(
                rs.getInt("id_layanan"),
                rs.getString("nama_layanan"),
                rs.getDouble("harga_per_kg"),
                rs.getString("estimasi")
        );

        return new Transaksi(
                rs.getInt("id_transaksi"),
                pelanggan,
                layanan,
                rs.getDate("tanggal_masuk").toLocalDate(),
                rs.getDouble("berat"),
                rs.getDouble("total_harga"),
                rs.getString("status")
        );
    }
}
