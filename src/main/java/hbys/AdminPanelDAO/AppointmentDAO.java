package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private Connection conn;

    public AppointmentDAO(Connection connection) {
        this.conn = connection;
    }

    public AppointmentDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // Fetch all appointments using a stored procedure
    public List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String procedureCall = "{CALL GetAllAppointments()}";

        try (CallableStatement cs = conn.prepareCall(procedureCall);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                appointments.add(new Appointment(
                        rs.getInt("AppointmentID"),
                        rs.getInt("PatientID"),
                        rs.getInt("DoctorID"),
                        rs.getTimestamp("AppointmentDate").toLocalDateTime(),
                        rs.getString("Status"),
                        rs.getString("Notes")
                ));
            }
        }
        return appointments;
    }

    // Add a new appointment using a stored procedure
    public void addAppointment(Appointment appointment) throws SQLException {
        String procedureCall = "{CALL AddAppointment(?, ?, ?, ?, ?)}";

        try (CallableStatement cs = conn.prepareCall(procedureCall)) {
            cs.setInt(1, appointment.getPatientID());
            cs.setInt(2, appointment.getDoctorID());
            cs.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentDate()));
            cs.setString(4, appointment.getStatus());
            cs.setString(5, appointment.getNotes());
            cs.execute();
        }
    }

    // Update an appointment using a stored procedure
    public void updateAppointment(Appointment appointment) throws SQLException {
        String procedureCall = "{CALL UpdateAppointment(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement cs = conn.prepareCall(procedureCall)) {
            cs.setInt(1, appointment.getAppointmentID());
            cs.setInt(2, appointment.getPatientID());
            cs.setInt(3, appointment.getDoctorID());
            cs.setTimestamp(4, Timestamp.valueOf(appointment.getAppointmentDate()));
            cs.setString(5, appointment.getStatus());
            cs.setString(6, appointment.getNotes());
            cs.execute();
        }
    }

    // Delete an appointment using a stored procedure
    public void deleteAppointment(int appointmentID) throws SQLException {
        String procedureCall = "{CALL DeleteAppointment(?)}";

        try (CallableStatement cs = conn.prepareCall(procedureCall)) {
            cs.setInt(1, appointmentID);
            cs.execute();
        }
    }

    // Search appointments using a stored procedure
    public List<Appointment> searchAppointments(String keyword) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String procedureCall = "{CALL SearchAppointments(?)}";

        try (CallableStatement cs = conn.prepareCall(procedureCall)) {
            cs.setString(1, "%" + keyword + "%");

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    appointments.add(new Appointment(
                            rs.getInt("AppointmentID"),
                            rs.getInt("PatientID"),
                            rs.getInt("DoctorID"),
                            rs.getTimestamp("AppointmentDate").toLocalDateTime(),
                            rs.getString("Status"),
                            rs.getString("Notes")
                    ));
                }
            }
        }
        return appointments;
    }
        // Method to check if a patient exists
    public boolean isPatientExists(int patientID) throws SQLException {
        String query = "SELECT COUNT(*) FROM Patients WHERE PatientID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, patientID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Returns true if patient exists
                }
            }
        }
        return false;
    }

    // Method to check if a doctor exists
    public boolean isDoctorExists(int doctorID) throws SQLException {
        String query = "SELECT COUNT(*) FROM Doctors WHERE DoctorID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, doctorID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Returns true if doctor exists
                }
            }
        }
        return false;
    }

    
    // Existing method to add an appointment
  
}

