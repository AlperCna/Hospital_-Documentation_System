package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.Doctor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {
    private Connection conn;

    public DoctorDAO(Connection connection) {
        this.conn = connection;
    }

    public DoctorDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // Fetch all doctors
    public List<Doctor> getAllDoctors() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM Doctors";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                doctors.add(new Doctor(
                        rs.getInt("DoctorID"),
                        rs.getInt("UserID"),
                        rs.getString("Specialization"),
                        rs.getString("ContactNumber"),
                        rs.getString("AvailabilityStatus"),
                        rs.getTimestamp("JoinDate").toLocalDateTime()
                ));
            }
        }
        return doctors;
    }

    // Add a new doctor
    public void addDoctor(Doctor doctor) throws SQLException {
        String query = "INSERT INTO Doctors (UserID, Specialization, ContactNumber, AvailabilityStatus, JoinDate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, doctor.getUserID());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getContactNumber());
            ps.setString(4, doctor.getAvailabilityStatus());
            ps.setTimestamp(5, Timestamp.valueOf(doctor.getJoinDate()));
            ps.executeUpdate();
        }
    }

    // Update a doctor
    public void updateDoctor(Doctor doctor) throws SQLException {
        String query = "UPDATE Doctors SET UserID = ?, Specialization = ?, ContactNumber = ?, AvailabilityStatus = ?, JoinDate = ? WHERE DoctorID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, doctor.getUserID());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getContactNumber());
            ps.setString(4, doctor.getAvailabilityStatus());
            ps.setTimestamp(5, Timestamp.valueOf(doctor.getJoinDate()));
            ps.setInt(6, doctor.getDoctorID());
            ps.executeUpdate();
        }
    }

    // Delete a doctor
    public void deleteDoctor(int doctorID) throws SQLException {
        String query = "DELETE FROM Doctors WHERE DoctorID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, doctorID);
            ps.executeUpdate();
        }
    }

    // Search doctors
    public List<Doctor> searchDoctors(String keyword) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM Doctors WHERE Specialization LIKE ? OR ContactNumber LIKE ? OR AvailabilityStatus LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doctors.add(new Doctor(
                            rs.getInt("DoctorID"),
                            rs.getInt("UserID"),
                            rs.getString("Specialization"),
                            rs.getString("ContactNumber"),
                            rs.getString("AvailabilityStatus"),
                            rs.getTimestamp("JoinDate").toLocalDateTime()
                    ));
                }
            }
        }
        return doctors;
    }
    public boolean isUserExists(int userID) throws SQLException {
    String query = "SELECT COUNT(*) FROM Users WHERE UserID = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, userID);
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
    public boolean isDoctor(int userID) throws SQLException {
    String query = "SELECT COUNT(*) FROM Doctors WHERE UserID = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, userID);
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
public void deleteDoctorByUserID(int userID) throws SQLException {
    String query = "DELETE FROM Doctors WHERE UserID = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, userID);
        stmt.executeUpdate();
    }
}
public boolean isDoctorExists(int doctorID) throws SQLException {
    String query = "SELECT COUNT(*) FROM Doctors WHERE DoctorID = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, doctorID);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}


}
