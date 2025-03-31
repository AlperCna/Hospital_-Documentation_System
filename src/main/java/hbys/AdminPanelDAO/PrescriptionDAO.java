package hbys.AdminPanelDAO;

import hbys.AdminModels.Prescription;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {
    private Connection connection;

    // Constructor
    public PrescriptionDAO(Connection connection) {
        this.connection = connection;
    }

    // Get all prescriptions
    public List<Prescription> getAllPrescriptions() throws SQLException {
        String query = "SELECT * FROM Prescriptions";
        List<Prescription> prescriptions = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                prescriptions.add(new Prescription(
                    rs.getInt("PrescriptionID"),
                    rs.getInt("PatientID"),
                    rs.getInt("DoctorID"),
                    rs.getTimestamp("DateIssued").toLocalDateTime(),
                    rs.getString("Notes")
                ));
            }
        }
        return prescriptions;
    }

    // Add a prescription
    public void addPrescription(Prescription prescription) throws SQLException {
        String query = "INSERT INTO Prescriptions (PatientID, DoctorID, DateIssued, Notes) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, prescription.getPatientID());
            ps.setInt(2, prescription.getDoctorID());
            ps.setTimestamp(3, Timestamp.valueOf(prescription.getDateIssued()));
            ps.setString(4, prescription.getNotes());
            ps.executeUpdate();
        }
    }

    // Update a prescription
    public void updatePrescription(Prescription prescription) throws SQLException {
        String query = "UPDATE Prescriptions SET PatientID = ?, DoctorID = ?, DateIssued = ?, Notes = ? WHERE PrescriptionID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, prescription.getPatientID());
            ps.setInt(2, prescription.getDoctorID());
            ps.setTimestamp(3, Timestamp.valueOf(prescription.getDateIssued()));
            ps.setString(4, prescription.getNotes());
            ps.setInt(5, prescription.getPrescriptionID());
            ps.executeUpdate();
        }
    }

    // Delete a prescription
    public void deletePrescription(int prescriptionID) throws SQLException {
        String query = "DELETE FROM Prescriptions WHERE PrescriptionID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, prescriptionID);
            ps.executeUpdate();
        }
    }
    public List<Prescription> searchPrescriptions(String keyword) throws SQLException {
    String query = "SELECT * FROM Prescriptions WHERE " +
                   "CAST(PrescriptionID AS VARCHAR) LIKE ? OR " +
                   "CAST(PatientID AS VARCHAR) LIKE ? OR " +
                   "CAST(DoctorID AS VARCHAR) LIKE ? OR " +
                   "Notes LIKE ?";
    List<Prescription> prescriptions = new ArrayList<>();

    try (PreparedStatement ps = connection.prepareStatement(query)) {
        String searchKeyword = "%" + keyword + "%";
        ps.setString(1, searchKeyword);
        ps.setString(2, searchKeyword);
        ps.setString(3, searchKeyword);
        ps.setString(4, searchKeyword);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                prescriptions.add(new Prescription(
                        rs.getInt("PrescriptionID"),
                        rs.getInt("PatientID"),
                        rs.getInt("DoctorID"),
                        rs.getTimestamp("DateIssued").toLocalDateTime(),
                        rs.getString("Notes")
                ));
            }
        }
    }

    return prescriptions;
}
public boolean isPatientExists(int patientID) throws SQLException {
    String query = "SELECT COUNT(*) FROM Patients WHERE PatientID = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, patientID);
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
public boolean isDoctorExists(int doctorID) throws SQLException {
    String query = "SELECT COUNT(*) FROM Doctors WHERE DoctorID = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, doctorID);
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}

}
