/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author pf344
 */
public class Transaksi extends TransaksiLaundry {
    private Pelanggan pelanggan;
    private Layanan layanan;
    private LocalDate tanggalMasuk;

    public Transaksi() {
    }

    public Transaksi(int id, Pelanggan pelanggan, Layanan layanan, LocalDate tanggalMasuk, double berat, double totalHarga, String status) {
        setId(id);
        this.pelanggan = pelanggan;
        this.layanan = layanan;
        this.tanggalMasuk = tanggalMasuk;
        setBerat(berat);
        setTotalHarga(totalHarga);
        setStatus(status);
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(Pelanggan pelanggan) {
        this.pelanggan = pelanggan;
    }

    public Layanan getLayanan() {
        return layanan;
    }

    public void setLayanan(Layanan layanan) {
        this.layanan = layanan;
    }

    public LocalDate getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(LocalDate tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }

    @Override
    public double hitungTotal() {
        if (layanan == null) {
            return 0;
        }
        return getBerat() * layanan.getHargaPerKg();
    }

    @Override
    public String getInfo() {
        String namaPelanggan = pelanggan == null ? "-" : pelanggan.getNama();
        String namaLayanan = layanan == null ? "-" : layanan.getNamaLayanan();
        return namaPelanggan + " - " + namaLayanan + " - Rp" + getTotalHarga();
    }
}
