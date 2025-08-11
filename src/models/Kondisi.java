package models;

public class Kondisi {

    private String kodeKondisi;
    private String namaKondisi;

    public Kondisi() {
    }

    public Kondisi(String kodeKondisi, String namaKondisi) {
        this.kodeKondisi = kodeKondisi;
        this.namaKondisi = namaKondisi;
    }

    // Getters and Setters
    public String getKodeKondisi() {
        return kodeKondisi;
    }

    public void setKodeKondisi(String kodeKondisi) {
        this.kodeKondisi = kodeKondisi;
    }

    public String getNamaKondisi() {
        return namaKondisi;
    }

    public void setNamaKondisi(String namaKondisi) {
        this.namaKondisi = namaKondisi;
    }

    @Override
    public String toString() {
        return namaKondisi;
    }
}
