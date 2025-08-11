package models;

import java.sql.Timestamp;

public class Aset {

    private int idAset;
    private String kodeAset;
    private String namaAset;
    private String kodeJenis;
    private String kodeKondisi;
    private String lokasi;
    private int tahunPerolehan;
    private double nilaiPerolehan;
    private String statusKelayakan;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Tambahan untuk join
    private String namaJenis;
    private String namaKondisi;

    public Aset() {
    }

    // Getters and Setters
    public int getIdAset() {
        return idAset;
    }

    public void setIdAset(int idAset) {
        this.idAset = idAset;
    }

    public String getKodeAset() {
        return kodeAset;
    }

    public void setKodeAset(String kodeAset) {
        this.kodeAset = kodeAset;
    }

    public String getNamaAset() {
        return namaAset;
    }

    public void setNamaAset(String namaAset) {
        this.namaAset = namaAset;
    }

    public String getKodeJenis() {
        return kodeJenis;
    }

    public void setKodeJenis(String kodeJenis) {
        this.kodeJenis = kodeJenis;
    }

    public String getKodeKondisi() {
        return kodeKondisi;
    }

    public void setKodeKondisi(String kodeKondisi) {
        this.kodeKondisi = kodeKondisi;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public int getTahunPerolehan() {
        return tahunPerolehan;
    }

    public void setTahunPerolehan(int tahunPerolehan) {
        this.tahunPerolehan = tahunPerolehan;
    }

    public double getNilaiPerolehan() {
        return nilaiPerolehan;
    }

    public void setNilaiPerolehan(double nilaiPerolehan) {
        this.nilaiPerolehan = nilaiPerolehan;
    }

    public String getStatusKelayakan() {
        return statusKelayakan;
    }

    public void setStatusKelayakan(String statusKelayakan) {
        this.statusKelayakan = statusKelayakan;
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

    public String getNamaKondisi() {
        return namaKondisi;
    }

    public void setNamaKondisi(String namaKondisi) {
        this.namaKondisi = namaKondisi;
    }
}
