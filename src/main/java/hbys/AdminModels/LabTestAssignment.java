package hbys.AdminModels;

import java.time.LocalDateTime;

public class LabTestAssignment {
    private int assignmentID;
    private int testID;
    private int technicianID;
    private LocalDateTime assignmentDate;

    public LabTestAssignment(int assignmentID, int testID, int technicianID, LocalDateTime assignmentDate) {
        this.assignmentID = assignmentID;
        this.testID = testID;
        this.technicianID = technicianID;
        this.assignmentDate = assignmentDate;
    }

    public int getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public int getTechnicianID() {
        return technicianID;
    }

    public void setTechnicianID(int technicianID) {
        this.technicianID = technicianID;
    }

    public LocalDateTime getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(LocalDateTime assignmentDate) {
        this.assignmentDate = assignmentDate;
    }
}
