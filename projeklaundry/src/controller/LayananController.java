/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import config.Koneksi;
import model.Layanan;

/**
 *
 * @author pf344
 */
public class LayananController {
    public List<Layanan> getSemuaLayanan() {
        List<Layanan> data = new ArrayList<>();
        String sql = "SELECT * FROM layanan ORDER BY id_layanan DESC";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                data.add(new Layanan(
                        rs.getInt("id_layanan"),
                        rs.getString("nama_layanan"),
                        rs.getDouble("harga_per_kg"),
                        rs.getString("estimasi")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error ambil layanan: " + e.getMessage());
        }

        return data;
    }

    public boolean tambahLayanan(Layanan layanan) {
        String sql = "INSERT INTO layanan (nama_layanan, harga_per_kg, estimasi) VALUES (?, ?, ?)";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, layanan.getNamaLayanan());
            ps.setDouble(2, layanan.getHargaPerKg());
            ps.setString(3, layanan.getEstimasi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error tambah layanan: " + e.getMessage());
            return false;
        }
    }

    public boolean ubahLayanan(Layanan layanan) {
        String sql = "UPDATE layanan SET nama_layanan = ?, harga_per_kg = ?, estimasi = ? WHERE id_layanan = ?";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, layanan.getNamaLayanan());
            ps.setDouble(2, layanan.getHargaPerKg());
            ps.setString(3, layanan.getEstimasi());
            ps.setInt(4, layanan.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error ubah layanan: " + e.getMessage());
            return false;
        }
    }

    public boolean hapusLayanan(int id) {
        String sql = "DELETE FROM layanan WHERE id_layanan = ?";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error hapus layanan: " + e.getMessage());
            return false;
        }
    }

    public List<Layanan> cariLayanan(String keyword) {
        List<Layanan> data = new ArrayList<>();
        String sql = "SELECT * FROM layanan WHERE nama_layanan LIKE ? ORDER BY id_layanan DESC";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.add(new Layanan(
                            rs.getInt("id_layanan"),
                            rs.getString("nama_layanan"),
                            rs.getDouble("harga_per_kg"),
                            rs.getString("estimasi")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error cari layanan: " + e.getMessage());
        }

        return data;
    }
}
