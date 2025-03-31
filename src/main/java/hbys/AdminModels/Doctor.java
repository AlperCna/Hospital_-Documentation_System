package hbys.AdminModels;

import java.time.LocalDateTime;

public class Doctor {
    private int doctorID;
    private int userID;
    private String specialization;
    private String contactNumber;
    private String availabilityStatus;
    private LocalDateTime joinDate;

    public Doctor(int doctorID, int userID, String specialization, String contactNumber, String availabilityStatus, LocalDateTime joinDate) {
        this.doctorID = doctorID;
        this.userID = userID;
        this.specialization = specialization;
        this.contactNumber = contactNumber;
        this.availabilityStatus = availabilityStatus;
        this.joinDate = joinDate;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }
}
