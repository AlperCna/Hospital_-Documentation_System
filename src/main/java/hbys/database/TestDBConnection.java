package hbys.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDBConnection {

    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost\\SQLEXPRESS01:1433;databaseName=Hospital_Document_System;encrypt=true;trustServerCertificate=true;";
        String username = "sql_user_talha02";
        String password = "melike82";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Veritabanı bağlantısı başarılı!");

            String sql = "SELECT TOP 10 * FROM Patients";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                System.out.println("PatientID: " + resultSet.getInt("PatientID")
                        + ", FirstName: " + resultSet.getString("FirstName")
                        + ", LastName: " + resultSet.getString("LastName"));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.err.println("Bağlantı başarısız: " + e.getMessage());
        }
    }
}
