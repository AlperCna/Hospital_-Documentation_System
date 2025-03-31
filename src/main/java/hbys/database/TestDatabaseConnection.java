package hbys.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDatabaseConnection {

    public static void main(String[] args) {
        final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS01:1433;databaseName=Hospital_Document_System;encrypt=true;trustServerCertificate=true;";
        final String USERNAME = "sql_user_talha02";
        final String PASSWORD = "melike82";

        Connection connection = null;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            if (connection != null) {
                System.out.println("Bağlantı başarılı! Veritabanına erişildi.");
            }

        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver bulunamadı: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Veritabanı bağlantı hatası: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Bağlantı kapatıldı.");
                } catch (SQLException e) {
                    System.err.println("Bağlantı kapatma hatası: " + e.getMessage());
                }
            }
        }
    }
}
