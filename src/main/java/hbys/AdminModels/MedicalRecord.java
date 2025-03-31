package hbys.AdminModels;

import java.time.LocalDateTime;

public class MedicalRecord {
    private int recordID;
    private int patientID;
    private int doctorID;
    private LocalDateTime recordDate;
    private String description;

    public MedicalRecord(int recordID, int patientID, int doctorID, LocalDateTime recordDate, String description) {
        this.recordID = recordID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.recordDate = recordDate;
        this.description = description;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public LocalDateTime getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDateTime recordDate) {
        this.recordDate = recordDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
