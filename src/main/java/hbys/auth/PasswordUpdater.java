package hbys.auth;

import hbys.database.DatabaseConnection;
import hbys.utils.PasswordHasher;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PasswordUpdater {
    public static boolean updatePassword(String email, String oldPassword, String newPassword) {
        String hashedOldPassword = PasswordHasher.hashPassword(oldPassword);
        String hashedNewPassword = PasswordHasher.hashPassword(newPassword);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE Patients SET Password = ? WHERE Email = ? AND Password = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, hashedNewPassword);
            ps.setString(2, email);
            ps.setString(3, hashedOldPassword);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // Şifre başarılı bir şekilde güncellendiğinde true döner
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
