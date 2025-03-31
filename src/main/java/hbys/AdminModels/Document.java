package hbys.AdminModels;

import java.sql.Timestamp;

public class Document {
    private int documentID;
    private String documentType;
    private int relatedID;
    private int createdBy;
    private int createdFor;
    private Timestamp creationDate;
    private String status;
    private String description;

    // Constructor
    public Document(int documentID, String documentType, int relatedID, int createdBy, int createdFor, Timestamp creationDate, String status, String description) {
        this.documentID = documentID;
        this.documentType = documentType;
        this.relatedID = relatedID;
        this.createdBy = createdBy;
        this.createdFor = createdFor;
        this.creationDate = creationDate;
        this.status = status;
        this.description = description;
    }

    // Getters and Setters
    public int getDocumentID() {
        return documentID;
    }

    public void setDocumentID(int documentID) {
        this.documentID = documentID;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public int getRelatedID() {
        return relatedID;
    }

    public void setRelatedID(int relatedID) {
        this.relatedID = relatedID;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getCreatedFor() {
        return createdFor;
    }

    public void setCreatedFor(int createdFor) {
        this.createdFor = createdFor;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Document{" +
                "documentID=" + documentID +
                ", documentType='" + documentType + '\'' +
                ", relatedID=" + relatedID +
                ", createdBy=" + createdBy +
                ", createdFor=" + createdFor +
                ", creationDate=" + creationDate +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
