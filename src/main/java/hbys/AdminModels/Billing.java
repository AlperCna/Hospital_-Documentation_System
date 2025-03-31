package hbys.AdminModels;

import java.time.LocalDate;

public class Billing {
    private int billID;
    private int appointmentID;
    private double totalAmount;
    private String paymentStatus;
    private LocalDate issueDate;

    public Billing(int billID, int appointmentID, double totalAmount, String paymentStatus, LocalDate issueDate) {
        this.billID = billID;
        this.appointmentID = appointmentID;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.issueDate = issueDate;
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
