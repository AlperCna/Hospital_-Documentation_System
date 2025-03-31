package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.PrescriptionItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionItemsDAO {
    private Connection conn;

    public PrescriptionItemsDAO(Connection connection) {
        this.conn = connection;
    }

    public PrescriptionItemsDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // Fetch all prescription items
    public List<PrescriptionItem> getAllPrescriptionItems() throws SQLException {
        List<PrescriptionItem> items = new ArrayList<>();
        String query = "SELECT * FROM PrescriptionItems";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                items.add(new PrescriptionItem(
                        rs.getInt("PrescriptionItemID"),
                        rs.getInt("PrescriptionID"),
                        rs.getInt("ItemID"),
                        rs.getInt("Quantity")
                ));
            }
        }
        return items;
    }

    // Add a new prescription item
    public void addPrescriptionItem(PrescriptionItem item) throws SQLException {
        String query = "INSERT INTO PrescriptionItems (PrescriptionID, ItemID, Quantity) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, item.getPrescriptionID());
            ps.setInt(2, item.getItemID());
            ps.setInt(3, item.getQuantity());
            ps.executeUpdate();
        }
    }

    // Update a prescription item
    public void updatePrescriptionItem(PrescriptionItem item) throws SQLException {
        String query = "UPDATE PrescriptionItems SET PrescriptionID = ?, ItemID = ?, Quantity = ? WHERE PrescriptionItemID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, item.getPrescriptionID());
            ps.setInt(2, item.getItemID());
            ps.setInt(3, item.getQuantity());
            ps.setInt(4, item.getPrescriptionItemID());
            ps.executeUpdate();
        }
    }

    // Delete a prescription item
    public void deletePrescriptionItem(int prescriptionItemID) throws SQLException {
        String query = "DELETE FROM PrescriptionItems WHERE PrescriptionItemID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, prescriptionItemID);
            ps.executeUpdate();
        }
    }

    // Search prescription items by Prescription ID
    public List<PrescriptionItem> searchPrescriptionItems(String keyword) throws SQLException {
        List<PrescriptionItem> items = new ArrayList<>();
        String query = "SELECT * FROM PrescriptionItems WHERE CAST(PrescriptionID AS CHAR) LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new PrescriptionItem(
                            rs.getInt("PrescriptionItemID"),
                            rs.getInt("PrescriptionID"),
                            rs.getInt("ItemID"),
                            rs.getInt("Quantity")
                    ));
                }
            }
        }
        return items;
    }
}
