package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.LabTestAssignment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LabTestAssignmentDAO {
    private Connection conn;

    public LabTestAssignmentDAO(Connection connection) {
        this.conn = connection;
    }

    public LabTestAssignmentDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // Fetch all assignments
    public List<LabTestAssignment> getAllAssignments() throws SQLException {
        List<LabTestAssignment> assignments = new ArrayList<>();
        String query = "SELECT * FROM LabTestAssignments";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                LocalDateTime assignmentDate = rs.getTimestamp("AssignmentDate").toLocalDateTime();
                assignments.add(new LabTestAssignment(
                        rs.getInt("AssignmentID"),
                        rs.getInt("TestID"),
                        rs.getInt("TechnicianID"),
                        assignmentDate
                ));
            }
        }
        return assignments;
    }

    // Add a new assignment
    public void addAssignment(LabTestAssignment assignment) throws SQLException {
        String query = "INSERT INTO LabTestAssignments (TestID, TechnicianID, AssignmentDate) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, assignment.getTestID());
            ps.setInt(2, assignment.getTechnicianID());
            ps.setTimestamp(3, Timestamp.valueOf(assignment.getAssignmentDate()));
            ps.executeUpdate();
        }
    }

    // Update an existing assignment
    public void updateAssignment(LabTestAssignment assignment) throws SQLException {
        String query = "UPDATE LabTestAssignments SET TestID = ?, TechnicianID = ?, AssignmentDate = ? WHERE AssignmentID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, assignment.getTestID());
            ps.setInt(2, assignment.getTechnicianID());
            ps.setTimestamp(3, Timestamp.valueOf(assignment.getAssignmentDate()));
            ps.setInt(4, assignment.getAssignmentID());
            ps.executeUpdate();
        }
    }

    // Delete an assignment
    public void deleteAssignment(int assignmentID) throws SQLException {
        String query = "DELETE FROM LabTestAssignments WHERE AssignmentID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, assignmentID);
            ps.executeUpdate();
        }
    }

    // Search assignments by TestID or TechnicianID
    public List<LabTestAssignment> searchAssignments(String keyword) throws SQLException {
        List<LabTestAssignment> assignments = new ArrayList<>();
        String query = "SELECT * FROM LabTestAssignments WHERE CAST(TestID AS CHAR) LIKE ? OR CAST(TechnicianID AS CHAR) LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime assignmentDate = rs.getTimestamp("AssignmentDate").toLocalDateTime();
                    assignments.add(new LabTestAssignment(
                            rs.getInt("AssignmentID"),
                            rs.getInt("TestID"),
                            rs.getInt("TechnicianID"),
                            assignmentDate
                    ));
                }
            }
        }
        return assignments;
    }
}
