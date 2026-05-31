/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import config.Koneksi;
import model.Admin;

/**
 *
 * @author pf344
 */
public class AuthController {
    public Admin login(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                            rs.getInt("id_admin"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error login: " + e.getMessage());
        }

        return null;
    }
}

