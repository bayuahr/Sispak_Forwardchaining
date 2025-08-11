package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private static Connection connection;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sispak_forward";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                System.out.println("Koneksi database berhasil");
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Koneksi database gagal: " + ex.getMessage());
            }
        }
        return connection;
    }
}
