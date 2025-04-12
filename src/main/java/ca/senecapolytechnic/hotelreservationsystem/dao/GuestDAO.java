package ca.senecapolytechnic.hotelreservationsystem.dao;

import ca.senecapolytechnic.hotelreservationsystem.models.Guest;
import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;

import java.sql.*;
import java.util.logging.Level;

public class GuestDAO {
    String url = "jdbc:sqlite:C:\\Users\\Marcus\\IdeaProjects\\HotelReservationSystem\\src\\main\\java\\ca\\senecapolytechnic\\hotelreservationsystem\\database\\hotel.db";

    public int insertGuest(Guest guest) {
        String sql = "INSERT INTO Guest(name, phone_number, email, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, guest.getName());
            pstmt.setString(2, guest.getPhoneNumber());
            pstmt.setString(3, guest.getEmail());
            pstmt.setString(4, guest.getAddress());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // guest_id
            }

        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
        }
        return -1;
    }
    public Guest getGuestById(int id) {
        String sql = "SELECT * FROM Guest WHERE id = ?";
        Guest guest = null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                guest = new Guest();
                guest.setGuestID(rs.getInt("id"));
                guest.setName(rs.getString("name"));
                guest.setEmail(rs.getString("email"));
                guest.setPhoneNumber(rs.getString("phone"));
            }

        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
        }

        return guest;
    }

    public boolean updateGuest(Guest guest) {
        String sql = "UPDATE Guest SET name = ?, phone_number = ?, email = ?, address = ? WHERE guest_id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, guest.getName());
            pstmt.setString(2, guest.getPhoneNumber());
            pstmt.setString(3, guest.getEmail());
            pstmt.setString(4, guest.getAddress());
            pstmt.setInt(5, guest.getGuestID());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
            e.printStackTrace();
            return false;
        }
            return true;
    }

}
