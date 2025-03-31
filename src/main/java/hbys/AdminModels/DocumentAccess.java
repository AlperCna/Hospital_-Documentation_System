package hbys.AdminModels;

import java.sql.Timestamp;

public class DocumentAccess {
    private int accessID;
    private int documentID;
    private int userID;
    private String accessLevel;
    private Timestamp accessDate;

    public DocumentAccess(int accessID, int documentID, int userID, String accessLevel, Timestamp accessDate) {
        this.accessID = accessID;
        this.documentID = documentID;
        this.userID = userID;
        this.accessLevel = accessLevel;
        this.accessDate = accessDate;
    }

    public int getAccessID() {
        return accessID;
    }

    public void setAccessID(int accessID) {
        this.accessID = accessID;
    }

    public int getDocumentID() {
        return documentID;
    }

    public void setDocumentID(int documentID) {
        this.documentID = documentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Timestamp getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(Timestamp accessDate) {
        this.accessDate = accessDate;
    }
}
