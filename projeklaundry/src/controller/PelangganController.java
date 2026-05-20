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
import model.Pelanggan;


/**
 *
 * @author pf344
 */
public class PelangganController {
    public List<Pelanggan> getSemuaPelanggan() {
        List<Pelanggan> data = new ArrayList<>();
        String sql = "SELECT * FROM pelanggan ORDER BY id_pelanggan DESC";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pelanggan pelanggan = new Pelanggan(
                        rs.getInt("id_pelanggan"),
                        rs.getString("nama"),
                        rs.getString("no_hp"),
                        rs.getString("alamat")
                );
                data.add(pelanggan);
            }
        } catch (SQLException e) {
            System.out.println("Error ambil pelanggan: " + e.getMessage());
        }

        return data;
    }

    public boolean tambahPelanggan(Pelanggan pelanggan) {
        String sql = "INSERT INTO pelanggan (nama, no_hp, alamat) VALUES (?, ?, ?)";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pelanggan.getNama());
            ps.setString(2, pelanggan.getNoHp());
            ps.setString(3, pelanggan.getAlamat());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error tambah pelanggan: " + e.getMessage());
            return false;
        }
    }

    public boolean ubahPelanggan(Pelanggan pelanggan) {
        String sql = "UPDATE pelanggan SET nama = ?, no_hp = ?, alamat = ? WHERE id_pelanggan = ?";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pelanggan.getNama());
            ps.setString(2, pelanggan.getNoHp());
            ps.setString(3, pelanggan.getAlamat());
            ps.setInt(4, pelanggan.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error ubah pelanggan: " + e.getMessage());
            return false;
        }
    }

    public boolean hapusPelanggan(int id) {
        String sql = "DELETE FROM pelanggan WHERE id_pelanggan = ?";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error hapus pelanggan: " + e.getMessage());
            return false;
        }
    }

    public List<Pelanggan> cariPelanggan(String keyword) {
        List<Pelanggan> data = new ArrayList<>();
        String sql = "SELECT * FROM pelanggan WHERE nama LIKE ? OR no_hp LIKE ? ORDER BY id_pelanggan DESC";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String cari = "%" + keyword + "%";
            ps.setString(1, cari);
            ps.setString(2, cari);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.add(new Pelanggan(
                            rs.getInt("id_pelanggan"),
                            rs.getString("nama"),
                            rs.getString("no_hp"),
                            rs.getString("alamat")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error cari pelanggan: " + e.getMessage());
        }

        return data;
    }
}
