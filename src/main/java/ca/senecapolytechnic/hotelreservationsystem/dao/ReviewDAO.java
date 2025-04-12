package ca.senecapolytechnic.hotelreservationsystem.dao;

import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;

import java.sql.*;
import java.util.logging.Level;

public class ReviewDAO {
    String url = "jdbc:sqlite:C:\\Users\\Marcus\\IdeaProjects\\HotelReservationSystem\\src\\main\\java\\ca\\senecapolytechnic\\hotelreservationsystem\\database\\hotel.db";

    public boolean insertReview(int reservationId, String reviewText) {
        String checkSql = "SELECT 1 FROM Reservation WHERE reservation_id = ?";
        String insertSql = "INSERT INTO Review (reservation_id, review_text) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            // Check if reservation ID exists
            checkStmt.setInt(1, reservationId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                // Reservation ID not found
                return false;
            }

            // If it exists, insert the review
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, reservationId);
                insertStmt.setString(2, reviewText);
                insertStmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
            return false;
        }
    }
}

