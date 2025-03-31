package hbys.AdminModels;

import java.time.LocalDateTime;

public class LabTest {
    private int testID;
    private int patientID;
    private int technicianID;
    private String testName;
    private LocalDateTime testDate;
    private String testResult;

    public LabTest(int testID, int patientID, int technicianID, String testName, LocalDateTime testDate, String testResult) {
        this.testID = testID;
        this.patientID = patientID;
        this.technicianID = technicianID;
        this.testName = testName;
        this.testDate = testDate;
        this.testResult = testResult;
    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getTechnicianID() {
        return technicianID;
    }

    public void setTechnicianID(int technicianID) {
        this.technicianID = technicianID;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public LocalDateTime getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDateTime testDate) {
        this.testDate = testDate;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }
}
