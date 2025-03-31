package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.Staff;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {
    private Connection conn;

    public StaffDAO(Connection connection) {
        this.conn = connection;
    }

    public StaffDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // Get all staff members
    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT * FROM Staff";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                staffList.add(new Staff(
                    rs.getInt("StaffID"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Role"),
                    rs.getString("ContactNumber"),
                    rs.getString("Email"),
                    rs.getTimestamp("JoinDate").toLocalDateTime()
                ));
            }
        }
        return staffList;
    }

    // Add staff member
    public void addStaff(Staff staff) throws SQLException {
        String query = "INSERT INTO Staff (FirstName, LastName, Role, ContactNumber, Email, JoinDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, staff.getFirstName());
            ps.setString(2, staff.getLastName());
            ps.setString(3, staff.getRole());
            ps.setString(4, staff.getContactNumber());
            ps.setString(5, staff.getEmail());
            ps.setTimestamp(6, Timestamp.valueOf(staff.getJoinDate()));
            ps.executeUpdate();
        }
    }

    // Update staff member
    public void updateStaff(Staff staff) throws SQLException {
        String query = "UPDATE Staff SET FirstName = ?, LastName = ?, Role = ?, ContactNumber = ?, Email = ? WHERE StaffID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, staff.getFirstName());
            ps.setString(2, staff.getLastName());
            ps.setString(3, staff.getRole());
            ps.setString(4, staff.getContactNumber());
            ps.setString(5, staff.getEmail());
            ps.setInt(6, staff.getStaffID());
            ps.executeUpdate();
        }
    }

    // Delete staff member
    public void deleteStaff(int staffID) throws SQLException {
        String query = "DELETE FROM Staff WHERE StaffID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, staffID);
            ps.executeUpdate();
        }
    }

    // Search staff by keyword
    public List<Staff> searchStaff(String keyword) throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT * FROM Staff WHERE FirstName LIKE ? OR LastName LIKE ? OR Role LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    staffList.add(new Staff(
                        rs.getInt("StaffID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Role"),
                        rs.getString("ContactNumber"),
                        rs.getString("Email"),
                        rs.getTimestamp("JoinDate").toLocalDateTime()
                    ));
                }
            }
        }
        return staffList;
    }
    public int addStaffAndReturnID(Staff staff) throws SQLException {
    String query = "INSERT INTO Staff (FirstName, LastName, Role, ContactNumber, Email, JoinDate) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setString(1, staff.getFirstName());
        stmt.setString(2, staff.getLastName());
        stmt.setString(3, staff.getRole());
        stmt.setString(4, staff.getContactNumber());
        stmt.setString(5, staff.getEmail());
        stmt.setTimestamp(6, Timestamp.valueOf(staff.getJoinDate()));
        stmt.executeUpdate();

        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1); // Return the generated Staff ID
            }
        }
    }
    throw new SQLException("Failed to retrieve Staff ID.");
}
    public boolean isNurseExists(int nurseID) throws SQLException {
    String query = "SELECT COUNT(*) FROM Staff WHERE StaffID = ? AND Role = 'Nurse'";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, nurseID);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}
public boolean isTechnicianExists(int technicianID) throws SQLException {
    String query = "SELECT COUNT(*) FROM Staff WHERE StaffID = ? AND Role = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, technicianID);
        stmt.setString(2, "Lab Technician"); // Ensure this matches the database value for technicians
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                System.out.println("Technician Query Result: " + rs.getInt(1)); // Debug logging
                return rs.getInt(1) > 0; // Return true if a match is found
            }
        }
    }
    return false; // Return false if no match is found
}




    
}
