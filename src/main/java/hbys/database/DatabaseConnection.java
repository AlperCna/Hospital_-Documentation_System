package hbys.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS02:1433;databaseName=Hospital_Document_System1;encrypt=true;trustServerCertificate=true";
    private static final String USERNAME = "alparjan";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() {
        try {
            // JDBC sürücüsünü yüklemek gerekli değil, DriverManager bunu otomatik halledecek

            // Veritabanına bağlanın
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Veritabanı bağlantısı başarısız oldu. Bağlantı detaylarını kontrol edin.");
            e.printStackTrace();
            return null;
        }
    }
}