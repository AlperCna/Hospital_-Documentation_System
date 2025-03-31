package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection conn;

    // Constructor
    public UserDAO(Connection connection) {
        this.conn = connection;
    }

    public UserDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
        if (this.conn == null) {
            throw new SQLException("Database connection failed!");
        }
    }

    // Fetch all users
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("UserID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Password"),
                        rs.getString("UserType"),
                        rs.getTimestamp("RegistrationDate")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
            throw e;
        }
        return users;
    }

    // Add a new user
    public void addUser(User user) throws SQLException {
        // Eğer UserID otomatik atanıyorsa IDENTITY_INSERT açmaya gerek yok
        String query = "INSERT INTO Users (FirstName, LastName, Email, Password, UserType, RegistrationDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getUserType());
            ps.setTimestamp(6, user.getRegistrationDate());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            throw e;
        }
        conn.commit(); // Kullanıcıyı ekledikten sonra commit yap

    }

    // Update a user
    public void updateUser(User user) throws SQLException {
        String query = "UPDATE Users SET FirstName = ?, LastName = ?, Email = ?, Password = ?, UserType = ? WHERE UserID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getUserType());
            ps.setInt(6, user.getUserID());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw e;
        }
    }

    // Delete a user
    public void deleteUser(int userID) throws SQLException {
        String query = "DELETE FROM Users WHERE UserID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            throw e;
        }
    }

    // Search users by keyword
    public List<User> searchUsers(String keyword) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users WHERE FirstName LIKE ? OR LastName LIKE ? OR Email LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                            rs.getInt("UserID"),
                            rs.getString("FirstName"),
                            rs.getString("LastName"),
                            rs.getString("Email"),
                            rs.getString("Password"),
                            rs.getString("UserType"),
                            rs.getTimestamp("RegistrationDate")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching users: " + e.getMessage());
            throw e;
        }
        return users;
    }

    // Check if a user exists
    public boolean isUserExists(int userID) throws SQLException {
        String query = "SELECT COUNT(*) FROM Users WHERE UserID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if user exists: " + e.getMessage());
            throw e;
        }
        return false;
    }
    public int addUserAndReturnID(User user) throws SQLException {
    String sql = "INSERT INTO Users (FirstName, LastName, Email, Password, UserType, RegistrationDate) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setString(1, user.getFirstName());
        stmt.setString(2, user.getLastName());
        stmt.setString(3, user.getEmail());
        stmt.setString(4, user.getPassword());
        stmt.setString(5, user.getUserType());
        stmt.setTimestamp(6, user.getRegistrationDate());
        stmt.executeUpdate();

        // Otomatik oluşturulan ID'yi al
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    throw new SQLException("User ID could not be generated.");

}
public boolean isEmailExists(String email) throws SQLException {
    String query = "SELECT COUNT(*) FROM Users WHERE Email = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, email);
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}

   

}
