package ca.senecapolytechnic.hotelreservationsystem.dao;

import ca.senecapolytechnic.hotelreservationsystem.models.Room;
import ca.senecapolytechnic.hotelreservationsystem.models.RoomType;
import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class RoomDAO {
    String url = "jdbc:sqlite:C:\\Users\\Marcus\\IdeaProjects\\HotelReservationSystem\\src\\main\\java\\ca\\senecapolytechnic\\hotelreservationsystem\\database\\hotel.db";

    public void insertRooms(List<Room> rooms, int reservationId) {
        String sql = "INSERT INTO Room(reservation_id, room_type, price_per_night) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Room room : rooms) {
                pstmt.setInt(1, reservationId);
                pstmt.setString(2, room.getRoomType().name());
                pstmt.setDouble(3, room.getPrice());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
        }
    }
    public List<Room> getRoomsByReservationId(int reservationId) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE reservation_id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Room room = new Room();
                room.setRoomID(rs.getInt("id"));
                room.setRoomType(RoomType.valueOf(rs.getString("type")));
                rooms.add(room);
            }

        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
        }

        return rooms;
    }
    public boolean updateRoomsByReservationID(List<Room> rooms, int reservationId) {
        String deleteSql = "DELETE FROM Room WHERE reservation_id = ?";
        String insertSql = "INSERT INTO Room (reservation_id, room_type, price_per_night) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url)) {
            // Delete old rooms
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, reservationId);
                deleteStmt.executeUpdate();
            }

            // Insert new rooms
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                for (Room room : rooms) {
                    insertStmt.setInt(1, reservationId);
                    insertStmt.setString(2, room.getRoomType().name());
                    insertStmt.setDouble(3, room.getPrice());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
            return false;
        }
        return true;
    }


}
