package models;

import java.sql.Timestamp;

public class Penilaian {

    private int idPenilaian;
    private int idAset;
    private Timestamp tanggalPenilaian;
    private String hasil;
    private String keterangan;

    // Tambahan untuk join
    private String kodeAset;
    private String namaAset;

    public Penilaian() {
    }

    // Getters and Setters
    public int getIdPenilaian() {
        return idPenilaian;
    }

    public void setIdPenilaian(int idPenilaian) {
        this.idPenilaian = idPenilaian;
    }

    public int getIdAset() {
        return idAset;
    }

    public void setIdAset(int idAset) {
        this.idAset = idAset;
    }

    public Timestamp getTanggalPenilaian() {
        return tanggalPenilaian;
    }

    public void setTanggalPenilaian(Timestamp tanggalPenilaian) {
        this.tanggalPenilaian = tanggalPenilaian;
    }

    public String getHasil() {
        return hasil;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
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
}
