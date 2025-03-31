package hbys.AdminModels;

import java.time.LocalDateTime;

public class Admission {
    private int admissionID;
    private int patientID;
    private int roomID;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;
    private String notes;
    private int responsibleDoctorID;
    private int responsibleNurseID;

    public Admission(int admissionID, int patientID, int roomID, LocalDateTime admissionDate, 
                     LocalDateTime dischargeDate, String notes, int responsibleDoctorID, int responsibleNurseID) {
        this.admissionID = admissionID;
        this.patientID = patientID;
        this.roomID = roomID;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.notes = notes;
        this.responsibleDoctorID = responsibleDoctorID;
        this.responsibleNurseID = responsibleNurseID;
    }

    public int getAdmissionID() {
        return admissionID;
    }

    public void setAdmissionID(int admissionID) {
        this.admissionID = admissionID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDateTime getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDateTime dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getResponsibleDoctorID() {
        return responsibleDoctorID;
    }

    public void setResponsibleDoctorID(int responsibleDoctorID) {
        this.responsibleDoctorID = responsibleDoctorID;
    }

    public int getResponsibleNurseID() {
        return responsibleNurseID;
    }

    public void setResponsibleNurseID(int responsibleNurseID) {
        this.responsibleNurseID = responsibleNurseID;
    }
}
