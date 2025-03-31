package hbys.AdminModels;

public class Room {
    private int roomID;
    private String roomType;
    private String roomNumber;
    private String availabilityStatus;

    public Room(int roomID, String roomType, String roomNumber, String availabilityStatus) {
        this.roomID = roomID;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.availabilityStatus = availabilityStatus;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}
