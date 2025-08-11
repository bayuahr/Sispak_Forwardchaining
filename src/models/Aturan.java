package models;

import java.sql.Timestamp;

public class Aturan {

    private int idAturan;
    private String kodeAturan;
    private String kondisi;
    private String kodeJenis;
    private String kesimpulan;
    private int prioritas;
    private boolean aktif;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Tambahan untuk join
    private String namaJenis;

    public Aturan() {
    }

    // Getters and Setters
    public int getIdAturan() {
        return idAturan;
    }

    public void setIdAturan(int idAturan) {
        this.idAturan = idAturan;
    }

    public String getKodeAturan() {
        return kodeAturan;
    }

    public void setKodeAturan(String kodeAturan) {
        this.kodeAturan = kodeAturan;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public String getKodeJenis() {
        return kodeJenis;
    }

    public void setKodeJenis(String kodeJenis) {
        this.kodeJenis = kodeJenis;
    }

    public String getKesimpulan() {
        return kesimpulan;
    }

    public void setKesimpulan(String kesimpulan) {
        this.kesimpulan = kesimpulan;
    }

    public int getPrioritas() {
        return prioritas;
    }

    public void setPrioritas(int prioritas) {
        this.prioritas = prioritas;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNamaJenis() {
        return namaJenis;
    }

    public void setNamaJenis(String namaJenis) {
        this.namaJenis = namaJenis;
    }
}
