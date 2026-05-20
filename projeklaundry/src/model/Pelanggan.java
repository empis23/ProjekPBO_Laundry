/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author pf344
 */
public class Pelanggan extends BaseEntity {
    private String nama;
    private String noHp;
    private String alamat;

    public Pelanggan() {
    }

    public Pelanggan(int id, String nama, String noHp, String alamat) {
        super(id);
        this.nama = nama;
        this.noHp = noHp;
        this.alamat = alamat;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    @Override
    public String getInfo() {
        return getId() + " - " + nama;
    }

    @Override
    public String toString() {
        return getId() + " - " + nama;
    }
}
