package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {
    private Connection conn;

    public DocumentDAO(Connection connection) {
        this.conn = connection;
    }

    // Constructor to establish a connection
    public DocumentDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // Fetch all documents
    public List<Document> getAllDocuments() throws SQLException {
        List<Document> documents = new ArrayList<>();
        String query = "SELECT * FROM Documents";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                documents.add(mapResultSetToDocument(rs));
            }
        }
        return documents;
    }

    // Add a new document
    public void addDocument(Document document) throws SQLException {
        String query = "INSERT INTO Documents (DocumentType, RelatedID, CreatedBy, CreatedFor, CreationDate, Status, Description) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, document.getDocumentType());
            ps.setInt(2, document.getRelatedID());
            ps.setInt(3, document.getCreatedBy());
            ps.setInt(4, document.getCreatedFor());
            ps.setTimestamp(5, document.getCreationDate());
            ps.setString(6, document.getStatus());
            ps.setString(7, document.getDescription());
            ps.executeUpdate();
        }
    }

    // Update an existing document
    public void updateDocument(Document document) throws SQLException {
        String query = "UPDATE Documents SET DocumentType = ?, RelatedID = ?, CreatedBy = ?, CreatedFor = ?, Status = ?, Description = ? WHERE DocumentID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, document.getDocumentType());
            ps.setInt(2, document.getRelatedID());
            ps.setInt(3, document.getCreatedBy());
            ps.setInt(4, document.getCreatedFor());
            ps.setString(5, document.getStatus());
            ps.setString(6, document.getDescription());
            ps.setInt(7, document.getDocumentID());
            ps.executeUpdate();
        }
    }

    // Delete a document by ID
    public void deleteDocument(int documentID) throws SQLException {
        String query = "DELETE FROM Documents WHERE DocumentID = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, documentID);
            ps.executeUpdate();
        }
    }

    // Search documents by keyword
    public List<Document> searchDocuments(String keyword) throws SQLException {
        List<Document> documents = new ArrayList<>();
        String query = "SELECT * FROM Documents WHERE DocumentType LIKE ? OR Status LIKE ? OR Description LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    documents.add(mapResultSetToDocument(rs));
                }
            }
        }
        return documents;
    }

    // Helper method to map a ResultSet row to a Document object
    private Document mapResultSetToDocument(ResultSet rs) throws SQLException {
        return new Document(
            rs.getInt("DocumentID"),
            rs.getString("DocumentType"),
            rs.getInt("RelatedID"),
            rs.getInt("CreatedBy"),
            rs.getInt("CreatedFor"),
            rs.getTimestamp("CreationDate"),
            rs.getString("Status"),
            rs.getString("Description")
        );
    }
    public boolean isDocumentExists(int documentID) throws SQLException {
    String query = "SELECT COUNT(*) FROM Documents WHERE DocumentID = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, documentID);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}

}
