package controllers;

import config.Database;
import models.JenisAset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JenisAsetController {

    public List<JenisAset> getAll() {
        List<JenisAset> jenisAsetList = new ArrayList<>();
        String sql = "SELECT * FROM master_jenis_aset ORDER BY kode_jenis";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JenisAset jenisAset = new JenisAset(
                        rs.getString("kode_jenis"),
                        rs.getString("nama_jenis")
                );
                jenisAsetList.add(jenisAset);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return jenisAsetList;
    }

    public JenisAset getByKode(String kode) {
        String sql = "SELECT * FROM master_jenis_aset WHERE kode_jenis = ?";
        JenisAset jenisAset = null;

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                jenisAset = new JenisAset(
                        rs.getString("kode_jenis"),
                        rs.getString("nama_jenis")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return jenisAset;
    }

    public boolean create(JenisAset jenisAset) {
        String sql = "INSERT INTO master_jenis_aset (kode_jenis, nama_jenis) VALUES (?, ?)";

        try {

            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, jenisAset.getKodeJenis());
            stmt.setString(2, jenisAset.getNamaJenis());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(JenisAset jenisAset) {
        String sql = "UPDATE master_jenis_aset SET nama_jenis = ? WHERE kode_jenis = ?";

        try {

            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, jenisAset.getNamaJenis());
            stmt.setString(2, jenisAset.getKodeJenis());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(String kode) {
        String sql = "DELETE FROM master_jenis_aset WHERE kode_jenis = ?";

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
}
