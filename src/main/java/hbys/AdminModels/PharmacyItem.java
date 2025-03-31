package hbys.AdminModels;

import java.time.LocalDate;

public class PharmacyItem {
    private int itemID;
    private String itemName;
    private int quantity;
    private double pricePerUnit;
    private LocalDate expiryDate;
    private String supplierName;

    public PharmacyItem(int itemID, String itemName, int quantity, double pricePerUnit, LocalDate expiryDate, String supplierName) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.expiryDate = expiryDate;
        this.supplierName = supplierName;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
