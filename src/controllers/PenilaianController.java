package controllers;

import config.Database;
import models.Penilaian;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PenilaianController {

    public List<Penilaian> getAllWithDetails() {
        List<Penilaian> penilaianList = new ArrayList<>();
        String sql = "SELECT p.*, a.kode_aset, a.nama_aset "
                + "FROM penilaian p "
                + "JOIN master_aset a ON p.id_aset = a.id_aset "
                + "ORDER BY p.tanggal_penilaian DESC";

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Penilaian penilaian = new Penilaian();
                penilaian.setIdPenilaian(rs.getInt("id_penilaian"));
                penilaian.setIdAset(rs.getInt("id_aset"));
                penilaian.setTanggalPenilaian(rs.getTimestamp("tanggal_penilaian"));
                penilaian.setHasil(rs.getString("hasil"));
                penilaian.setKeterangan(rs.getString("keterangan"));

                // Join fields
                penilaian.setKodeAset(rs.getString("kode_aset"));
                penilaian.setNamaAset(rs.getString("nama_aset"));

                penilaianList.add(penilaian);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return penilaianList;
    }
}
