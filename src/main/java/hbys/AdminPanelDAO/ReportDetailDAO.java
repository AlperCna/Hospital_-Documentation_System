package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.ReportDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDetailDAO {
    private Connection conn;

    public ReportDetailDAO(Connection connection) {
        this.conn = connection;
    }

    public ReportDetailDAO() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    // Fetch all reports
    public List<ReportDetail> getAllReports() throws SQLException {
        List<ReportDetail> reports = new ArrayList<>();
        String query = "SELECT * FROM ReportDetails";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reports.add(new ReportDetail(
                    rs.getInt("ReportID"),
                    rs.getInt("DocumentID"),
                    rs.getString("ReportTitle"),
                    rs.getString("ReportContent")
                ));
            }
        }
        return reports;
    }

    // Add a new report
    public void addReport(ReportDetail report) throws SQLException {
        String query = "INSERT INTO ReportDetails (DocumentID, ReportTitle, ReportContent) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, report.getDocumentID());
            ps.setString(2, report.getReportTitle());
            ps.setString(3, report.getReportContent());
            ps.executeUpdate();
        }
    }

    // Update an existing report
    public void updateReport(ReportDetail report) throws SQLException {
        String query = "UPDATE ReportDetails SET DocumentID = ?, ReportTitle = ?, ReportContent = ? WHERE ReportID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, report.getDocumentID());
            ps.setString(2, report.getReportTitle());
            ps.setString(3, report.getReportContent());
            ps.setInt(4, report.getReportID());
            ps.executeUpdate();
        }
    }

    // Delete a report by ID
    public void deleteReport(int reportID) throws SQLException {
        String query = "DELETE FROM ReportDetails WHERE ReportID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reportID);
            ps.executeUpdate();
        }
    }

    // Search reports by keyword (Title or Content)
    public List<ReportDetail> searchReports(String keyword) throws SQLException {
        String query = "SELECT * FROM ReportDetails WHERE ReportTitle LIKE ? OR ReportContent LIKE ?";
        List<ReportDetail> reports = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(new ReportDetail(
                        rs.getInt("ReportID"),
                        rs.getInt("DocumentID"),
                        rs.getString("ReportTitle"),
                        rs.getString("ReportContent")
                    ));
                }
            }
        }
        return reports;
    }
}
