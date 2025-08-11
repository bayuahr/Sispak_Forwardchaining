package controllers;

import config.Database;
import models.Aset;
import models.Penilaian;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AsetController {

    public List<Aset> getAllWithDetails() {
        List<Aset> asetList = new ArrayList<>();
        String sql = "SELECT a.*, j.nama_jenis, k.nama_kondisi "
                + "FROM master_aset a "
                + "LEFT JOIN master_jenis_aset j ON a.kode_jenis = j.kode_jenis "
                + "LEFT JOIN master_kondisi k ON a.kode_kondisi = k.kode_kondisi "
                + "ORDER BY a.kode_aset";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Aset aset = new Aset();
                aset.setIdAset(rs.getInt("id_aset"));
                aset.setKodeAset(rs.getString("kode_aset"));
                aset.setNamaAset(rs.getString("nama_aset"));
                aset.setKodeJenis(rs.getString("kode_jenis"));
                aset.setKodeKondisi(rs.getString("kode_kondisi"));
                aset.setLokasi(rs.getString("lokasi"));
                aset.setTahunPerolehan(rs.getInt("tahun_perolehan"));
                aset.setNilaiPerolehan(rs.getDouble("nilai_perolehan"));
                aset.setStatusKelayakan(rs.getString("status_kelayakan"));
                aset.setCreatedAt(rs.getTimestamp("created_at"));
                aset.setUpdatedAt(rs.getTimestamp("updated_at"));

                // Join fields
                aset.setNamaJenis(rs.getString("nama_jenis"));
                aset.setNamaKondisi(rs.getString("nama_kondisi"));

                asetList.add(aset);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return asetList;
    }

    public Aset getByKode(String kode) {
        String sql = "SELECT a.*, j.nama_jenis, k.nama_kondisi "
                + "FROM master_aset a "
                + "LEFT JOIN master_jenis_aset j ON a.kode_jenis = j.kode_jenis "
                + "LEFT JOIN master_kondisi k ON a.kode_kondisi = k.kode_kondisi "
                + "WHERE a.kode_aset = ?";
        Aset aset = null;

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                aset = new Aset();
                aset.setIdAset(rs.getInt("id_aset"));
                aset.setKodeAset(rs.getString("kode_aset"));
                aset.setNamaAset(rs.getString("nama_aset"));
                aset.setKodeJenis(rs.getString("kode_jenis"));
                aset.setKodeKondisi(rs.getString("kode_kondisi"));
                aset.setLokasi(rs.getString("lokasi"));
                aset.setTahunPerolehan(rs.getInt("tahun_perolehan"));
                aset.setNilaiPerolehan(rs.getDouble("nilai_perolehan"));
                aset.setStatusKelayakan(rs.getString("status_kelayakan"));
                aset.setCreatedAt(rs.getTimestamp("created_at"));
                aset.setUpdatedAt(rs.getTimestamp("updated_at"));

                // Join fields
                aset.setNamaJenis(rs.getString("nama_jenis"));
                aset.setNamaKondisi(rs.getString("nama_kondisi"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return aset;
    }

    public boolean create(Aset aset) {
        String sql = "INSERT INTO master_aset "
                + "(kode_aset, nama_aset, kode_jenis, kode_kondisi, lokasi, "
                + "tahun_perolehan, nilai_perolehan, status_kelayakan) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, aset.getKodeAset());
            stmt.setString(2, aset.getNamaAset());
            stmt.setString(3, aset.getKodeJenis());
            stmt.setString(4, aset.getKodeKondisi());
            stmt.setString(5, aset.getLokasi());
            stmt.setInt(6, aset.getTahunPerolehan());
            stmt.setDouble(7, aset.getNilaiPerolehan());
            stmt.setString(8, aset.getStatusKelayakan() != null
                    ? aset.getStatusKelayakan() : "Belum Dinilai");

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(Aset aset) {
        String sql = "UPDATE master_aset SET "
                + "nama_aset = ?, kode_jenis = ?, kode_kondisi = ?, "
                + "lokasi = ?, tahun_perolehan = ?, nilai_perolehan = ? "
                + "WHERE kode_aset = ?";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, aset.getNamaAset());
            stmt.setString(2, aset.getKodeJenis());
            stmt.setString(3, aset.getKodeKondisi());
            stmt.setString(4, aset.getLokasi());
            stmt.setInt(5, aset.getTahunPerolehan());
            stmt.setDouble(6, aset.getNilaiPerolehan());
            stmt.setString(7, aset.getKodeAset());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(String kode) {
        String sql = "DELETE FROM master_aset WHERE kode_aset = ?";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kode);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateStatusKelayakan(String kodeAset, String status) {
        String sql = "UPDATE master_aset SET status_kelayakan = ? WHERE kode_aset = ?";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setString(2, kodeAset);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean tambahPenilaian(int idAset, String hasil, String keterangan) {
        String sql = "INSERT INTO penilaian (id_aset, hasil, keterangan) VALUES (?, ?, ?)";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idAset);
            stmt.setString(2, hasil);
            stmt.setString(3, keterangan);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public String nilaiKelayakan(Aset aset) {
        // Implementasi forward chaining untuk menentukan kelayakan aset
        // 1. Ambil semua aturan yang aktif dari database, urutkan berdasarkan prioritas
        String sql = "SELECT * FROM master_aturan WHERE aktif = 1 ORDER BY prioritas";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String kondisi = rs.getString("kondisi");
                String kodeJenis = rs.getString("kode_jenis");
                String kesimpulan = rs.getString("kesimpulan");

                // Cek apakah aturan ini berlaku untuk jenis aset tertentu atau semua jenis
                if (kodeJenis == null || kodeJenis.equals(aset.getKodeJenis())) {
                    // Cek apakah kondisi aset memenuhi aturan ini
                    if (cekKondisi(aset.getKodeKondisi(), kondisi)) {
                        return kesimpulan;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private boolean cekKondisi(String kondisiAset, String kondisiAturan) {
        if (kondisiAset == null) {
            return false;
        }

        // Aturan bisa berisi beberapa kondisi yang dipisah koma (OR condition)
        String[] kondisiArr = kondisiAturan.split(",");
        for (String kondisi : kondisiArr) {
            if (kondisi.trim().equals(kondisiAset)) {
                return true;
            }
        }

        return false;
    }
}
