package hbys.auth;

import hbys.database.DatabaseConnection;
import hbys.utils.PasswordHasher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginValidator {
    public static boolean validateLogin(String email, String password) {
        String hashedPassword = PasswordHasher.hashPassword(password);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Patients WHERE Email = ? AND Password = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Kullanıcı bulunursa true döner
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
