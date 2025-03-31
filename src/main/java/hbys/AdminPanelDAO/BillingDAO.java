package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.Billing;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {
    private Connection conn;

    public BillingDAO(Connection connection) throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    public List<Billing> getAllInvoices() throws SQLException {
    String query = "SELECT * FROM Billing"; // Use 'Billing'
    List<Billing> invoices = new ArrayList<>();
    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
            invoices.add(new Billing(
                rs.getInt("BillID"),
                rs.getInt("AppointmentID"),
                rs.getDouble("TotalAmount"),
                rs.getString("PaymentStatus"),
                rs.getDate("IssueDate").toLocalDate()
            ));
        }
    }
    return invoices;
}


    public void addInvoice(Billing invoice) throws SQLException {
        String query = "INSERT INTO Billing (AppointmentID, TotalAmount, PaymentStatus, IssueDate) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, invoice.getAppointmentID());
        ps.setDouble(2, invoice.getTotalAmount());
        ps.setString(3, invoice.getPaymentStatus());
        ps.setDate(4, Date.valueOf(invoice.getIssueDate()));
        ps.executeUpdate();
    }

    public void updateInvoice(Billing invoice) throws SQLException {
        String query = "UPDATE Billing SET AppointmentID = ?, TotalAmount = ?, PaymentStatus = ?, IssueDate = ? WHERE BillID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, invoice.getAppointmentID());
        ps.setDouble(2, invoice.getTotalAmount());
        ps.setString(3, invoice.getPaymentStatus());
        ps.setDate(4, Date.valueOf(invoice.getIssueDate()));
        ps.setInt(5, invoice.getBillID());
        ps.executeUpdate();
    }

    public void deleteInvoice(int billID) throws SQLException {
        String query = "DELETE FROM Billing WHERE BillID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, billID);
        ps.executeUpdate();
    }
   public List<Billing> searchInvoices(String keyword) throws SQLException {
    String query = "SELECT * FROM Billing WHERE " +
                   "CAST(BillID AS VARCHAR) LIKE ? OR " +
                   "CAST(AppointmentID AS VARCHAR) LIKE ? OR " +
                   "CAST(TotalAmount AS VARCHAR) LIKE ? OR " +
                   "PaymentStatus LIKE ? OR " +
                   "CAST(IssueDate AS VARCHAR) LIKE ?";
    List<Billing> invoices = new ArrayList<>();

    try (PreparedStatement ps = conn.prepareStatement(query)) {
        String searchKeyword = "%" + keyword + "%";
        ps.setString(1, searchKeyword);
        ps.setString(2, searchKeyword);
        ps.setString(3, searchKeyword);
        ps.setString(4, searchKeyword);
        ps.setString(5, searchKeyword);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                invoices.add(new Billing(
                        rs.getInt("BillID"),
                        rs.getInt("AppointmentID"),
                        rs.getDouble("TotalAmount"),
                        rs.getString("PaymentStatus"),
                        rs.getDate("IssueDate").toLocalDate()
                ));
            }
        }
    }

    return invoices;
}
 // Method to check if an appointment exists
    public boolean isAppointmentExists(int appointmentID) throws SQLException {
        String query = "SELECT COUNT(*) FROM Appointments WHERE AppointmentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, appointmentID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

}
