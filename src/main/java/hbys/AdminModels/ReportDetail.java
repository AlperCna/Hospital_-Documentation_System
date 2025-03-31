package hbys.AdminModels;

public class ReportDetail {
    private int reportID;
    private int documentID;
    private String reportTitle;
    private String reportContent;

    public ReportDetail(int reportID, int documentID, String reportTitle, String reportContent) {
        this.reportID = reportID;
        this.documentID = documentID;
        this.reportTitle = reportTitle;
        this.reportContent = reportContent;
    }

    // Getters and Setters
    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public int getDocumentID() {
        return documentID;
    }

    public void setDocumentID(int documentID) {
        this.documentID = documentID;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }
}
