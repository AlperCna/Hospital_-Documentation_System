package hbys.AdminPanelDAO;

import hbys.AdminModels.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    private Connection connection;

    // Constructor
    public PatientDAO(Connection connection) {
        this.connection = connection;
    }

    // Get all patients
    public List<Patient> getAllPatients() throws SQLException {
        String query = "SELECT * FROM Patients";
        List<Patient> patients = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String dateOfBirth = rs.getDate("DateOfBirth") != null
                        ? rs.getDate("DateOfBirth").toString()
                        : "";

                patients.add(new Patient(
                        rs.getInt("PatientID"),
                        rs.getInt("UserID"),
                        dateOfBirth,
                        rs.getString("Gender"),
                        rs.getString("ContactNumber"),
                        rs.getString("Address"),
                        rs.getString("RegistrationDate")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching patients: " + e.getMessage());
            throw e;
        }

        return patients;
    }

    // Add a patient
    public void addPatient(Patient patient) throws SQLException {
        String query = "INSERT INTO Patients (UserID, DateOfBirth, Gender, ContactNumber, Address, RegistrationDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, patient.getUserID());
            ps.setString(2, patient.getDateOfBirth());
            ps.setString(3, patient.getGender());
            ps.setString(4, patient.getContactNumber());
            ps.setString(5, patient.getAddress());
            ps.setTimestamp(6, Timestamp.valueOf(patient.getRegistrationDate()));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding patient: " + e.getMessage());
            throw e;
        }
    }

    // Update a patient
    public void updatePatient(Patient patient) throws SQLException {
        String query = "UPDATE Patients SET UserID = ?, DateOfBirth = ?, Gender = ?, ContactNumber = ?, Address = ?, RegistrationDate = ? WHERE PatientID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, patient.getUserID());
            ps.setString(2, patient.getDateOfBirth());
            ps.setString(3, patient.getGender());
            ps.setString(4, patient.getContactNumber());
            ps.setString(5, patient.getAddress());
            ps.setTimestamp(6, Timestamp.valueOf(patient.getRegistrationDate()));
            ps.setInt(7, patient.getPatientID());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating patient: " + e.getMessage());
            throw e;
        }
    }

    // Delete a patient
    public void deletePatient(int patientID) throws SQLException {
        String query = "DELETE FROM Patients WHERE PatientID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, patientID);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting patient: " + e.getMessage());
            throw e;
        }
    }

    // Search for patients
    public List<Patient> searchPatients(String keyword) throws SQLException {
        String query = "SELECT * FROM Patients WHERE ContactNumber LIKE ? OR Address LIKE ?";
        List<Patient> patients = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String dateOfBirth = rs.getDate("DateOfBirth") != null
                            ? rs.getDate("DateOfBirth").toString()
                            : "";

                    patients.add(new Patient(
                            rs.getInt("PatientID"),
                            rs.getInt("UserID"),
                            dateOfBirth,
                            rs.getString("Gender"),
                            rs.getString("ContactNumber"),
                            rs.getString("Address"),
                            rs.getString("RegistrationDate")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching patients: " + e.getMessage());
            throw e;
        }

        return patients;
    }

    // Check if a patient exists by UserID
    public boolean isPatientExists(int userID) throws SQLException {
        String query = "SELECT COUNT(*) FROM Patients WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if patient exists: " + e.getMessage());
            throw e;
        }
        return false;
    }
     public void deletePatientByUserID(int userID) throws SQLException {
    String sql = "DELETE FROM Patients WHERE UserID = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, userID);
        stmt.executeUpdate();
    }
}

}
