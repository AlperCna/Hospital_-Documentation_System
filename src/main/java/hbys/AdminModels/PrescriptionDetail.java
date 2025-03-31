package hbys.AdminModels;

public class PrescriptionDetail {
    private int prescriptionDetailID;
    private int documentID;
    private int itemID;
    private int quantity;

    public PrescriptionDetail(int prescriptionDetailID, int documentID, int itemID, int quantity) {
        this.prescriptionDetailID = prescriptionDetailID;
        this.documentID = documentID;
        this.itemID = itemID;
        this.quantity = quantity;
    }

    public int getPrescriptionDetailID() {
        return prescriptionDetailID;
    }

    public void setPrescriptionDetailID(int prescriptionDetailID) {
        this.prescriptionDetailID = prescriptionDetailID;
    }

    public int getDocumentID() {
        return documentID;
    }

    public void setDocumentID(int documentID) {
        this.documentID = documentID;
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
