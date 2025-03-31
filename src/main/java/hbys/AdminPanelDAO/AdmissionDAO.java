package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.Admission;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdmissionDAO {
    private Connection conn;

    public AdmissionDAO(Connection connection) {
        this.conn = connection;
    }

    public AdmissionDAO() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    public List<Admission> getAllAdmissions() throws SQLException {
        List<Admission> admissions = new ArrayList<>();
        String query = "SELECT * FROM Admissions";
        
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                admissions.add(new Admission(
                    rs.getInt("AdmissionID"),
                    rs.getInt("PatientID"),
                    rs.getInt("RoomID"),
                    rs.getTimestamp("AdmissionDate").toLocalDateTime(),
                    rs.getTimestamp("DischargeDate") != null ? rs.getTimestamp("DischargeDate").toLocalDateTime() : null,
                    rs.getString("Notes"),
                    rs.getInt("ResponsibleDoctorID"),
                    rs.getInt("ResponsibleNurseID")
                ));
            }
        }
        return admissions;
    }

    public void addAdmission(Admission admission) throws SQLException {
        String query = "INSERT INTO Admissions (PatientID, RoomID, AdmissionDate, DischargeDate, Notes, ResponsibleDoctorID, ResponsibleNurseID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, admission.getPatientID());
            ps.setInt(2, admission.getRoomID());
            ps.setTimestamp(3, Timestamp.valueOf(admission.getAdmissionDate()));
            ps.setTimestamp(4, admission.getDischargeDate() != null ? Timestamp.valueOf(admission.getDischargeDate()) : null);
            ps.setString(5, admission.getNotes());
            ps.setInt(6, admission.getResponsibleDoctorID());
            ps.setInt(7, admission.getResponsibleNurseID());
            ps.executeUpdate();
        }
    }

    public void updateAdmission(Admission admission) throws SQLException {
        String query = "UPDATE Admissions SET PatientID = ?, RoomID = ?, AdmissionDate = ?, DischargeDate = ?, Notes = ?, ResponsibleDoctorID = ?, ResponsibleNurseID = ? WHERE AdmissionID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, admission.getPatientID());
            ps.setInt(2, admission.getRoomID());
            ps.setTimestamp(3, Timestamp.valueOf(admission.getAdmissionDate()));
            ps.setTimestamp(4, admission.getDischargeDate() != null ? Timestamp.valueOf(admission.getDischargeDate()) : null);
            ps.setString(5, admission.getNotes());
            ps.setInt(6, admission.getResponsibleDoctorID());
            ps.setInt(7, admission.getResponsibleNurseID());
            ps.setInt(8, admission.getAdmissionID());
            ps.executeUpdate();
        }
    }

    public void deleteAdmission(int admissionID) throws SQLException {
        String query = "DELETE FROM Admissions WHERE AdmissionID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, admissionID);
            ps.executeUpdate();
        }
    }

    public List<Admission> searchAdmissions(String keyword) throws SQLException {
        List<Admission> admissions = new ArrayList<>();
        String query = "SELECT * FROM Admissions WHERE Notes LIKE ? OR CAST(PatientID AS CHAR) LIKE ? OR CAST(RoomID AS CHAR) LIKE ?";
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    admissions.add(new Admission(
                        rs.getInt("AdmissionID"),
                        rs.getInt("PatientID"),
                        rs.getInt("RoomID"),
                        rs.getTimestamp("AdmissionDate").toLocalDateTime(),
                        rs.getTimestamp("DischargeDate") != null ? rs.getTimestamp("DischargeDate").toLocalDateTime() : null,
                        rs.getString("Notes"),
                        rs.getInt("ResponsibleDoctorID"),
                        rs.getInt("ResponsibleNurseID")
                    ));
                }
            }
        }
        return admissions;
    }
}
