package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.InvoiceDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    private Connection conn;

    // Constructor to establish a connection
    public InvoiceDAO(Connection connection) {
        this.conn = connection;
    }

    public InvoiceDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // Fetch all invoice details
    public List<InvoiceDetail> getAllInvoiceDetails() throws SQLException {
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        String query = "SELECT * FROM InvoiceDetails";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                invoiceDetails.add(new InvoiceDetail(
                    rs.getInt("InvoiceDetailID"),
                    rs.getInt("DocumentID"),
                    rs.getString("ItemDescription"),
                    rs.getInt("Quantity"),
                    rs.getDouble("UnitPrice"),
                    rs.getDouble("TotalPrice") // Fetch TotalPrice but don't modify it
                ));
            }
        }

        return invoiceDetails;
    }

    // Add a new invoice detail (TotalPrice is auto-calculated in the database)
    public void addInvoiceDetail(InvoiceDetail detail) throws SQLException {
        String query = "INSERT INTO InvoiceDetails (DocumentID, ItemDescription, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, detail.getDocumentID());
            ps.setString(2, detail.getItemDescription());
            ps.setInt(3, detail.getQuantity());
            ps.setDouble(4, detail.getUnitPrice());
            ps.executeUpdate();
        }
    }

    // Update an existing invoice detail (TotalPrice is not updated here)
    public void updateInvoiceDetail(InvoiceDetail detail) throws SQLException {
        String query = "UPDATE InvoiceDetails SET DocumentID = ?, ItemDescription = ?, Quantity = ?, UnitPrice = ? WHERE InvoiceDetailID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, detail.getDocumentID());
            ps.setString(2, detail.getItemDescription());
            ps.setInt(3, detail.getQuantity());
            ps.setDouble(4, detail.getUnitPrice());
            ps.setInt(5, detail.getInvoiceDetailID());
            ps.executeUpdate();
        }
    }

    // Delete an invoice detail by ID
    public void deleteInvoiceDetail(int invoiceDetailID) throws SQLException {
        String query = "DELETE FROM InvoiceDetails WHERE InvoiceDetailID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, invoiceDetailID);
            ps.executeUpdate();
        }
    }

    // Search invoice details by keyword
    public List<InvoiceDetail> searchInvoiceDetails(String keyword) throws SQLException {
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        String query = "SELECT * FROM InvoiceDetails WHERE ItemDescription LIKE ? OR CAST(DocumentID AS CHAR) LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoiceDetails.add(new InvoiceDetail(
                        rs.getInt("InvoiceDetailID"),
                        rs.getInt("DocumentID"),
                        rs.getString("ItemDescription"),
                        rs.getInt("Quantity"),
                        rs.getDouble("UnitPrice"),
                        rs.getDouble("TotalPrice") // Fetch TotalPrice but don't modify it
                    ));
                }
            }
        }
        return invoiceDetails;
    }
}
