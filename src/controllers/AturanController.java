package controllers;

import config.Database;
import models.Aturan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AturanController {

    public List<Aturan> getAllWithDetails() {
        List<Aturan> aturanList = new ArrayList<>();
        String sql = "SELECT a.*, j.nama_jenis "
                + "FROM master_aturan a "
                + "LEFT JOIN master_jenis_aset j ON a.kode_jenis = j.kode_jenis "
                + "ORDER BY a.prioritas, a.kode_aturan";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Aturan aturan = new Aturan();
                aturan.setIdAturan(rs.getInt("id_aturan"));
                aturan.setKodeAturan(rs.getString("kode_aturan"));
                aturan.setKondisi(rs.getString("kondisi"));
                aturan.setKodeJenis(rs.getString("kode_jenis"));
                aturan.setKesimpulan(rs.getString("kesimpulan"));
                aturan.setPrioritas(rs.getInt("prioritas"));
                aturan.setAktif(rs.getBoolean("aktif"));
                aturan.setCreatedAt(rs.getTimestamp("created_at"));
                aturan.setUpdatedAt(rs.getTimestamp("updated_at"));

                // Join field
                aturan.setNamaJenis(rs.getString("nama_jenis"));

                aturanList.add(aturan);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return aturanList;
    }

    public Aturan getByKode(String kode) {
        String sql = "SELECT * FROM master_aturan WHERE kode_aturan = ?";
        Aturan aturan = null;

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                aturan = new Aturan();
                aturan.setIdAturan(rs.getInt("id_aturan"));
                aturan.setKodeAturan(rs.getString("kode_aturan"));
                aturan.setKondisi(rs.getString("kondisi"));
                aturan.setKodeJenis(rs.getString("kode_jenis"));
                aturan.setKesimpulan(rs.getString("kesimpulan"));
                aturan.setPrioritas(rs.getInt("prioritas"));
                aturan.setAktif(rs.getBoolean("aktif"));
                aturan.setCreatedAt(rs.getTimestamp("created_at"));
                aturan.setUpdatedAt(rs.getTimestamp("updated_at"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return aturan;
    }

    public boolean create(Aturan aturan) {
        String sql = "INSERT INTO master_aturan "
                + "(kode_aturan, kondisi, kode_jenis, kesimpulan, prioritas, aktif) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, aturan.getKodeAturan());
            stmt.setString(2, aturan.getKondisi());
            stmt.setString(3, aturan.getKodeJenis());
            stmt.setString(4, aturan.getKesimpulan());
            stmt.setInt(5, aturan.getPrioritas());
            stmt.setBoolean(6, aturan.isAktif());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(Aturan aturan) {
        String sql = "UPDATE master_aturan SET "
                + "kondisi = ?, kode_jenis = ?, kesimpulan = ?, "
                + "prioritas = ?, aktif = ? "
                + "WHERE kode_aturan = ?";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, aturan.getKondisi());
            stmt.setString(2, aturan.getKodeJenis());
            stmt.setString(3, aturan.getKesimpulan());
            stmt.setInt(4, aturan.getPrioritas());
            stmt.setBoolean(5, aturan.isAktif());
            stmt.setString(6, aturan.getKodeAturan());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(String kode) {
        String sql = "DELETE FROM master_aturan WHERE kode_aturan = ?";

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

    public boolean toggleAktif(String kode, boolean newStatus) {
        String sql = "UPDATE master_aturan SET aktif = ? WHERE kode_aturan = ?";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, newStatus);
            stmt.setString(2, kode);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
