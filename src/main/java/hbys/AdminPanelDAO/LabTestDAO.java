package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.LabTest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LabTestDAO {
    private Connection conn;

    public LabTestDAO(Connection connection) throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    public List<LabTest> getAllLabTests() throws SQLException {
        List<LabTest> labTests = new ArrayList<>();
        String query = "SELECT * FROM LabTests";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            LabTest labTest = new LabTest(
                rs.getInt("TestID"),
                rs.getInt("PatientID"),
                rs.getInt("TechnicianID"),
                rs.getString("TestName"),
                rs.getTimestamp("TestDate").toLocalDateTime(),
                rs.getString("TestResult")
            );
            labTests.add(labTest);
        }
        return labTests;
    }

    public void addLabTest(LabTest labTest) throws SQLException {
        String query = "INSERT INTO LabTests (PatientID, TechnicianID, TestName, TestDate, TestResult) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, labTest.getPatientID());
        ps.setInt(2, labTest.getTechnicianID());
        ps.setString(3, labTest.getTestName());
        ps.setTimestamp(4, Timestamp.valueOf(labTest.getTestDate()));
        ps.setString(5, labTest.getTestResult());
        ps.executeUpdate();
    }

    public void deleteLabTest(int testID) throws SQLException {
        String query = "DELETE FROM LabTests WHERE TestID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, testID);
        ps.executeUpdate();
    }

    public void updateLabTest(LabTest labTest) throws SQLException {
        String query = "UPDATE LabTests SET PatientID = ?, TechnicianID = ?, TestName = ?, TestDate = ?, TestResult = ? WHERE TestID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, labTest.getPatientID());
        ps.setInt(2, labTest.getTechnicianID());
        ps.setString(3, labTest.getTestName());
        ps.setTimestamp(4, Timestamp.valueOf(labTest.getTestDate()));
        ps.setString(5, labTest.getTestResult());
        ps.setInt(6, labTest.getTestID());
        ps.executeUpdate();
    }
    public List<LabTest> searchLabTests(String keyword) throws SQLException {
    String query = "SELECT * FROM LabTests WHERE " +
                   "CAST(TestID AS VARCHAR) LIKE ? OR " +
                   "CAST(PatientID AS VARCHAR) LIKE ? OR " +
                   "CAST(TechnicianID AS VARCHAR) LIKE ? OR " +
                   "TestName LIKE ? OR " +
                   "TestResult LIKE ?";
    List<LabTest> labTests = new ArrayList<>();

    try (PreparedStatement ps = conn.prepareStatement(query)) {
        String searchKeyword = "%" + keyword + "%";
        ps.setString(1, searchKeyword);
        ps.setString(2, searchKeyword);
        ps.setString(3, searchKeyword);
        ps.setString(4, searchKeyword);
        ps.setString(5, searchKeyword);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                labTests.add(new LabTest(
                        rs.getInt("TestID"),
                        rs.getInt("PatientID"),
                        rs.getInt("TechnicianID"),
                        rs.getString("TestName"),
                        rs.getTimestamp("TestDate").toLocalDateTime(),
                        rs.getString("TestResult")
                ));
            }
        }
    }

    return labTests;
}
    public boolean isPatientExists(int patientID) throws SQLException {
    String query = "SELECT COUNT(*) FROM Patients WHERE PatientID = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, patientID);
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}

public boolean isTechnicianExists(int technicianID) throws SQLException {
    String query = "SELECT COUNT(*) FROM LabTechnicians WHERE TechnicianID = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, technicianID);
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
public boolean isTestExists(int testID) throws SQLException {
    String query = "SELECT COUNT(*) FROM LabTests WHERE TestID = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, testID);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}




}
