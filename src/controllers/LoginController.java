package controllers;

import config.Database;
import models.User;
import utils.HashUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;

        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String inputHash = HashUtil.sha256(password);

                if (storedHash.equals(inputHash)) {
                    user = new User(
                            rs.getInt("id_user"),
                            rs.getString("username"),
                            storedHash,
                            rs.getString("nama_lengkap"),
                            rs.getString("role"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return user;
    }
}
