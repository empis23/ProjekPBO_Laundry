/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author pf344
 */
public class Layanan extends BaseEntity {
    private String namaLayanan;
    private double hargaPerKg;
    private String estimasi;

    public Layanan() {
    }

    public Layanan(int id, String namaLayanan, double hargaPerKg, String estimasi) {
        super(id);
        this.namaLayanan = namaLayanan;
        this.hargaPerKg = hargaPerKg;
        this.estimasi = estimasi;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public void setNamaLayanan(String namaLayanan) {
        this.namaLayanan = namaLayanan;
    }

    public double getHargaPerKg() {
        return hargaPerKg;
    }

    public void setHargaPerKg(double hargaPerKg) {
        this.hargaPerKg = hargaPerKg;
    }

    public String getEstimasi() {
        return estimasi;
    }

    public void setEstimasi(String estimasi) {
        this.estimasi = estimasi;
    }

    @Override
    public String getInfo() {
        return namaLayanan + " - Rp" + hargaPerKg + "/kg";
    }

    @Override
    public String toString() {
        return getId() + " - " + namaLayanan;
    }
}
