package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.DocumentAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentAccessDAO {
    private Connection conn;

    public DocumentAccessDAO(Connection connection) {
        this.conn = connection;
    }

    // Constructor to establish a connection
    public DocumentAccessDAO() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    // Fetch all document access records
    public List<DocumentAccess> getAllAccess() throws SQLException {
        List<DocumentAccess> accesses = new ArrayList<>();
        String query = "SELECT * FROM DocumentAccess";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                accesses.add(new DocumentAccess(
                    rs.getInt("AccessID"),
                    rs.getInt("DocumentID"),
                    rs.getInt("UserID"),
                    rs.getString("AccessLevel"),
                    rs.getTimestamp("AccessDate")
                ));
            }
        }
        return accesses;
    }

    // Add a new document access record
    public void addAccess(DocumentAccess access) throws SQLException {
        String query = "INSERT INTO DocumentAccess (DocumentID, UserID, AccessLevel, AccessDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, access.getDocumentID());
            ps.setInt(2, access.getUserID());
            ps.setString(3, access.getAccessLevel());
            ps.setTimestamp(4, access.getAccessDate());
            ps.executeUpdate();
        }
    }

    // Update an existing document access record
    public void updateAccess(DocumentAccess access) throws SQLException {
        String query = "UPDATE DocumentAccess SET DocumentID = ?, UserID = ?, AccessLevel = ?, AccessDate = ? WHERE AccessID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, access.getDocumentID());
            ps.setInt(2, access.getUserID());
            ps.setString(3, access.getAccessLevel());
            ps.setTimestamp(4, access.getAccessDate());
            ps.setInt(5, access.getAccessID());
            ps.executeUpdate();
        }
    }

    // Delete a document access record by ID
    public void deleteAccess(int accessID) throws SQLException {
        String query = "DELETE FROM DocumentAccess WHERE AccessID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, accessID);
            ps.executeUpdate();
        }
    }

    // Search document access records by keyword
    public List<DocumentAccess> searchAccess(String keyword) throws SQLException {
        String query = "SELECT * FROM DocumentAccess WHERE AccessLevel LIKE ? OR CAST(DocumentID AS CHAR) LIKE ? OR CAST(UserID AS CHAR) LIKE ? OR CAST(AccessDate AS CHAR) LIKE ?";
        List<DocumentAccess> accesses = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);
            ps.setString(4, searchKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accesses.add(new DocumentAccess(
                        rs.getInt("AccessID"),
                        rs.getInt("DocumentID"),
                        rs.getInt("UserID"),
                        rs.getString("AccessLevel"),
                        rs.getTimestamp("AccessDate")
                    ));
                }
            }
        }
        return accesses;
    }
     public boolean isUserExists(int userID) throws SQLException {
        String query = "SELECT COUNT(*) FROM Users WHERE UserID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
      // Check if a document exists
    public boolean isDocumentExists(int documentID) throws SQLException {
        String query = "SELECT COUNT(*) FROM Documents WHERE DocumentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, documentID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
