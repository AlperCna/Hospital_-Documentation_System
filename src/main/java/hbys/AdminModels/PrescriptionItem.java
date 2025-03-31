package hbys.AdminModels;

public class PrescriptionItem {
    private int prescriptionItemID;
    private int prescriptionID;
    private int itemID;
    private int quantity;

    public PrescriptionItem(int prescriptionItemID, int prescriptionID, int itemID, int quantity) {
        this.prescriptionItemID = prescriptionItemID;
        this.prescriptionID = prescriptionID;
        this.itemID = itemID;
        this.quantity = quantity;
    }

    public int getPrescriptionItemID() {
        return prescriptionItemID;
    }

    public void setPrescriptionItemID(int prescriptionItemID) {
        this.prescriptionItemID = prescriptionItemID;
    }

    public int getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(int prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
