package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.LabTechnician;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LabTechnicianDAO {
    private Connection conn;

    public LabTechnicianDAO(Connection connection) {
        this.conn = connection;
    }

    public LabTechnicianDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // Fetch all lab technicians
    public List<LabTechnician> getAllLabTechnicians() throws SQLException {
        List<LabTechnician> technicians = new ArrayList<>();
        String query = "SELECT * FROM LabTechnicians";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                LocalDateTime hireDate = rs.getTimestamp("HireDate").toLocalDateTime();

                technicians.add(new LabTechnician(
                    rs.getInt("TechnicianID"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("ContactNumber"),
                    rs.getString("Email"),
                    hireDate
                ));
            }
        }

        return technicians;
    }

    // Add a new lab technician
    public void addLabTechnician(LabTechnician technician) throws SQLException {
    String query = "INSERT INTO LabTechnicians (FirstName, LastName, ContactNumber, Email, HireDate) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setString(1, technician.getFirstName());
        ps.setString(2, technician.getLastName());
        ps.setString(3, technician.getContactNumber());
        ps.setString(4, technician.getEmail());
        ps.setTimestamp(5, Timestamp.valueOf(technician.getHireDate())); // Use hireDate from the technician object
        ps.executeUpdate();
    }
}


    // Update an existing lab technician
    public void updateLabTechnician(LabTechnician technician) throws SQLException {
        String query = "UPDATE LabTechnicians SET FirstName = ?, LastName = ?, ContactNumber = ?, Email = ?, HireDate = ? WHERE TechnicianID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, technician.getFirstName());
            ps.setString(2, technician.getLastName());
            ps.setString(3, technician.getContactNumber());
            ps.setString(4, technician.getEmail());
            ps.setTimestamp(5, Timestamp.valueOf(technician.getHireDate()));
            ps.setInt(6, technician.getTechnicianID());
            ps.executeUpdate();
        }
    }

    // Delete a lab technician by ID
    public void deleteLabTechnician(int technicianID) throws SQLException {
        String query = "DELETE FROM LabTechnicians WHERE TechnicianID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, technicianID);
            ps.executeUpdate();
        }
    }

    // Search lab technicians by keyword
    public List<LabTechnician> searchLabTechnicians(String keyword) throws SQLException {
        List<LabTechnician> technicians = new ArrayList<>();
        String query = "SELECT * FROM LabTechnicians WHERE FirstName LIKE ? OR LastName LIKE ? OR ContactNumber LIKE ? OR Email LIKE ? OR CAST(TechnicianID AS CHAR) LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);
            ps.setString(4, searchKeyword);
            ps.setString(5, searchKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime hireDate = rs.getTimestamp("HireDate").toLocalDateTime();

                    technicians.add(new LabTechnician(
                        rs.getInt("TechnicianID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("ContactNumber"),
                        rs.getString("Email"),
                        hireDate
                    ));
                }
            }
        }

        return technicians;
    }
    public int addLabTechnicianAndReturnID(LabTechnician technician) throws SQLException {
    String query = "INSERT INTO LabTechnicians (FirstName, LastName, ContactNumber, Email, HireDate) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setString(1, technician.getFirstName());
        stmt.setString(2, technician.getLastName());
        stmt.setString(3, technician.getContactNumber());
        stmt.setString(4, technician.getEmail());
        stmt.setTimestamp(5, Timestamp.valueOf(technician.getHireDate()));
        stmt.executeUpdate();

        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1); // Return the generated technician ID
            }
        }
    }
    throw new SQLException("Failed to retrieve Lab Technician ID.");
}
    public boolean isTechnicianExists(int technicianID) throws SQLException {
    String query = "SELECT COUNT(*) FROM Staff WHERE StaffID = ? AND Role = 'Lab Technician'";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, technicianID);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}

    

}
