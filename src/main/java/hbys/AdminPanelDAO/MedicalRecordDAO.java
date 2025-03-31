package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.MedicalRecord;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO {
    private Connection conn;

    public MedicalRecordDAO(Connection connection) {
        this.conn = connection;
    }

    public MedicalRecordDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // Fetch all records
    public List<MedicalRecord> getAllRecords() throws SQLException {
        List<MedicalRecord> records = new ArrayList<>();
        String query = "SELECT * FROM MedicalRecords";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                LocalDateTime recordDate = rs.getTimestamp("RecordDate").toLocalDateTime();
                records.add(new MedicalRecord(
                        rs.getInt("RecordID"),
                        rs.getInt("PatientID"),
                        rs.getInt("DoctorID"),
                        recordDate,
                        rs.getString("Description")
                ));
            }
        }
        return records;
    }

    // Add a new record
    public void addRecord(MedicalRecord record) throws SQLException {
        String query = "INSERT INTO MedicalRecords (PatientID, DoctorID, RecordDate, Description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, record.getPatientID());
            ps.setInt(2, record.getDoctorID());
            ps.setTimestamp(3, Timestamp.valueOf(record.getRecordDate()));
            ps.setString(4, record.getDescription());
            ps.executeUpdate();
        }
    }

    // Update an existing record
    public void updateRecord(MedicalRecord record) throws SQLException {
        String query = "UPDATE MedicalRecords SET PatientID = ?, DoctorID = ?, RecordDate = ?, Description = ? WHERE RecordID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, record.getPatientID());
            ps.setInt(2, record.getDoctorID());
            ps.setTimestamp(3, Timestamp.valueOf(record.getRecordDate()));
            ps.setString(4, record.getDescription());
            ps.setInt(5, record.getRecordID());
            ps.executeUpdate();
        }
    }

    // Delete a record
    public void deleteRecord(int recordID) throws SQLException {
        String query = "DELETE FROM MedicalRecords WHERE RecordID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, recordID);
            ps.executeUpdate();
        }
    }

    // Search records by PatientID, DoctorID, or Description
    public List<MedicalRecord> searchRecords(String keyword) throws SQLException {
        List<MedicalRecord> records = new ArrayList<>();
        String query = "SELECT * FROM MedicalRecords WHERE CAST(PatientID AS CHAR) LIKE ? OR CAST(DoctorID AS CHAR) LIKE ? OR Description LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime recordDate = rs.getTimestamp("RecordDate").toLocalDateTime();
                    records.add(new MedicalRecord(
                            rs.getInt("RecordID"),
                            rs.getInt("PatientID"),
                            rs.getInt("DoctorID"),
                            recordDate,
                            rs.getString("Description")
                    ));
                }
            }
        }
        return records;
    }
}
