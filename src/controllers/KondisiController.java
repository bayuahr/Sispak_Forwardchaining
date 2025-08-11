package controllers;

import config.Database;
import models.Kondisi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KondisiController {

    public List<Kondisi> getAll() {
        List<Kondisi> kondisiList = new ArrayList<>();
        String sql = "SELECT * FROM master_kondisi ORDER BY kode_kondisi";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Kondisi kondisi = new Kondisi(
                        rs.getString("kode_kondisi"),
                        rs.getString("nama_kondisi")
                );
                kondisiList.add(kondisi);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return kondisiList;
    }

    public Kondisi getByKode(String kode) {
        String sql = "SELECT * FROM master_kondisi WHERE kode_kondisi = ?";
        Kondisi kondisi = null;

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, kode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                kondisi = new Kondisi(
                        rs.getString("kode_kondisi"),
                        rs.getString("nama_kondisi")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return kondisi;
    }

    public boolean create(Kondisi kondisi) {
        String sql = "INSERT INTO master_kondisi (kode_kondisi, nama_kondisi) VALUES (?, ?)";

        try {

            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kondisi.getKodeKondisi());
            stmt.setString(2, kondisi.getNamaKondisi());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(Kondisi kondisi) {
        String sql = "UPDATE master_kondisi SET nama_kondisi = ? WHERE kode_kondisi = ?";

        try {

            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kondisi.getNamaKondisi());
            stmt.setString(2, kondisi.getKodeKondisi());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(String kode) {
        String sql = "DELETE FROM master_kondisi WHERE kode_kondisi = ?";

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
