package hbys.AdminPanelDAO;

import hbys.database.DatabaseConnection;
import hbys.AdminModels.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private Connection conn;

    public RoomDAO(Connection connection) {
        this.conn = connection;
    }

    public RoomDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // Fetch all rooms
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM Rooms";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("RoomID"),
                        rs.getString("RoomType"),
                        rs.getString("RoomNumber"),
                        rs.getString("AvailabilityStatus")
                ));
            }
        }
        return rooms;
    }

    // Add a new room
    public void addRoom(Room room) throws SQLException {
        String query = "INSERT INTO Rooms (RoomType, RoomNumber, AvailabilityStatus) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, room.getRoomType());
            ps.setString(2, room.getRoomNumber());
            ps.setString(3, room.getAvailabilityStatus());
            ps.executeUpdate();
        }
    }

    // Update a room
    public void updateRoom(Room room) throws SQLException {
        String query = "UPDATE Rooms SET RoomType = ?, RoomNumber = ?, AvailabilityStatus = ? WHERE RoomID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, room.getRoomType());
            ps.setString(2, room.getRoomNumber());
            ps.setString(3, room.getAvailabilityStatus());
            ps.setInt(4, room.getRoomID());
            ps.executeUpdate();
        }
    }

    // Delete a room
    public void deleteRoom(int roomID) throws SQLException {
        String query = "DELETE FROM Rooms WHERE RoomID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, roomID);
            ps.executeUpdate();
        }
    }

    // Search rooms
    public List<Room> searchRooms(String keyword) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM Rooms WHERE RoomType LIKE ? OR RoomNumber LIKE ? OR AvailabilityStatus LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(new Room(
                            rs.getInt("RoomID"),
                            rs.getString("RoomType"),
                            rs.getString("RoomNumber"),
                            rs.getString("AvailabilityStatus")
                    ));
                }
            }
        }
        return rooms;
    }

    // Check if a room is available using the IsRoomAvailable function
    public boolean isRoomAvailable(int roomID) throws SQLException {
        String query = "SELECT dbo.IsRoomAvailable(?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, roomID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 1; // Function returns 1 for true
                }
            }
        }
        return false; // Default if no result
    }
    public boolean isRoomExists(int roomID) throws SQLException {
    String query = "SELECT COUNT(*) FROM Rooms WHERE RoomID = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, roomID);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}

}
