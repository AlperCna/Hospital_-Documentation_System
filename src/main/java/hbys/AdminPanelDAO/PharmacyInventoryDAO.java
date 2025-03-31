package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.PharmacyItem;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PharmacyInventoryDAO {
    private Connection conn;

    public PharmacyInventoryDAO(Connection connection) {
        this.conn = connection;
    }

    public PharmacyInventoryDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // Fetch all items
    public List<PharmacyItem> getAllItems() throws SQLException {
        List<PharmacyItem> items = new ArrayList<>();
        String query = "SELECT * FROM PharmacyInventory";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                LocalDate expiryDate = rs.getDate("ExpiryDate").toLocalDate();
                items.add(new PharmacyItem(
                        rs.getInt("ItemID"),
                        rs.getString("ItemName"),
                        rs.getInt("Quantity"),
                        rs.getDouble("PricePerUnit"),
                        expiryDate,
                        rs.getString("SupplierName")
                ));
            }
        }
        return items;
    }

    // Add a new item
    public void addItem(PharmacyItem item) throws SQLException {
        String query = "INSERT INTO PharmacyInventory (ItemName, Quantity, PricePerUnit, ExpiryDate, SupplierName) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, item.getItemName());
            ps.setInt(2, item.getQuantity());
            ps.setDouble(3, item.getPricePerUnit());
            ps.setDate(4, Date.valueOf(item.getExpiryDate()));
            ps.setString(5, item.getSupplierName());
            ps.executeUpdate();
        }
    }

    // Update an existing item
    public void updateItem(PharmacyItem item) throws SQLException {
        String query = "UPDATE PharmacyInventory SET ItemName = ?, Quantity = ?, PricePerUnit = ?, ExpiryDate = ?, SupplierName = ? WHERE ItemID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, item.getItemName());
            ps.setInt(2, item.getQuantity());
            ps.setDouble(3, item.getPricePerUnit());
            ps.setDate(4, Date.valueOf(item.getExpiryDate()));
            ps.setString(5, item.getSupplierName());
            ps.setInt(6, item.getItemID());
            ps.executeUpdate();
        }
    }

    // Delete an item
    public void deleteItem(int itemID) throws SQLException {
        String query = "DELETE FROM PharmacyInventory WHERE ItemID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, itemID);
            ps.executeUpdate();
        }
    }

    // Search items by name or supplier
    public List<PharmacyItem> searchItems(String keyword) throws SQLException {
        List<PharmacyItem> items = new ArrayList<>();
        String query = "SELECT * FROM PharmacyInventory WHERE ItemName LIKE ? OR SupplierName LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate expiryDate = rs.getDate("ExpiryDate").toLocalDate();
                    items.add(new PharmacyItem(
                            rs.getInt("ItemID"),
                            rs.getString("ItemName"),
                            rs.getInt("Quantity"),
                            rs.getDouble("PricePerUnit"),
                            expiryDate,
                            rs.getString("SupplierName")
                    ));
                }
            }
        }
        return items;
    }
}
