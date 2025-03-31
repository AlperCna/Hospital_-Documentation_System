package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.PrescriptionDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDetailsDAO {
    private Connection conn;

    public PrescriptionDetailsDAO(Connection connection) {
        this.conn = connection;
    }

    public PrescriptionDetailsDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // Fetch all prescription details
    public List<PrescriptionDetail> getAllPrescriptionDetails() throws SQLException {
        List<PrescriptionDetail> details = new ArrayList<>();
        String query = "SELECT * FROM PrescriptionDetails";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                details.add(new PrescriptionDetail(
                        rs.getInt("PrescriptionDetailID"),
                        rs.getInt("DocumentID"),
                        rs.getInt("ItemID"),
                        rs.getInt("Quantity")
                ));
            }
        }
        return details;
    }

    // Add a new prescription detail
    public void addPrescriptionDetail(PrescriptionDetail detail) throws SQLException {
        String query = "INSERT INTO PrescriptionDetails (DocumentID, ItemID, Quantity) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, detail.getDocumentID());
            ps.setInt(2, detail.getItemID());
            ps.setInt(3, detail.getQuantity());
            ps.executeUpdate();
        }
    }

    // Update a prescription detail
    public void updatePrescriptionDetail(PrescriptionDetail detail) throws SQLException {
        String query = "UPDATE PrescriptionDetails SET DocumentID = ?, ItemID = ?, Quantity = ? WHERE PrescriptionDetailID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, detail.getDocumentID());
            ps.setInt(2, detail.getItemID());
            ps.setInt(3, detail.getQuantity());
            ps.setInt(4, detail.getPrescriptionDetailID());
            ps.executeUpdate();
        }
    }

    // Delete a prescription detail
    public void deletePrescriptionDetail(int prescriptionDetailID) throws SQLException {
        String query = "DELETE FROM PrescriptionDetails WHERE PrescriptionDetailID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, prescriptionDetailID);
            ps.executeUpdate();
        }
    }

    // Search prescription details by document ID
    public List<PrescriptionDetail> searchPrescriptionDetails(String keyword) throws SQLException {
        List<PrescriptionDetail> details = new ArrayList<>();
        String query = "SELECT * FROM PrescriptionDetails WHERE CAST(DocumentID AS CHAR) LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    details.add(new PrescriptionDetail(
                            rs.getInt("PrescriptionDetailID"),
                            rs.getInt("DocumentID"),
                            rs.getInt("ItemID"),
                            rs.getInt("Quantity")
                    ));
                }
            }
        }
        return details;
    }
}
