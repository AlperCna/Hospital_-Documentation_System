package hbys.AdminModels;

import java.time.LocalDateTime;

public class Prescription {
    private int prescriptionID;
    private int patientID;
    private int doctorID;
    private LocalDateTime dateIssued;
    private String notes;

    // Constructor
    public Prescription(int prescriptionID, int patientID, int doctorID, LocalDateTime dateIssued, String notes) {
        this.prescriptionID = prescriptionID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.dateIssued = dateIssued;
        this.notes = notes;
    }

    // Getters and Setters
    public int getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(int prescriptionID) {
        this.prescriptionID = prescriptionID;
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

    public LocalDateTime getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(LocalDateTime dateIssued) {
        this.dateIssued = dateIssued;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

