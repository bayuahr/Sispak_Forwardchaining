package models;

public class JenisAset {

    private String kodeJenis;
    private String namaJenis;

    public JenisAset() {
    }

    public JenisAset(String kodeJenis, String namaJenis) {
        this.kodeJenis = kodeJenis;
        this.namaJenis = namaJenis;
    }

    // Getters and Setters
    public String getKodeJenis() {
        return kodeJenis;
    }

    public void setKodeJenis(String kodeJenis) {
        this.kodeJenis = kodeJenis;
    }

    public String getNamaJenis() {
        return namaJenis;
    }

    public void setNamaJenis(String namaJenis) {
        this.namaJenis = namaJenis;
    }

    @Override
    public String toString() {
        return namaJenis;
    }
}
