package ca.senecapolytechnic.hotelreservationsystem.dao;

import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;

import java.sql.*;
import java.util.logging.Level;

public class UserDAO {

    String url = "jdbc:sqlite:C:\\Users\\Marcus\\IdeaProjects\\HotelReservationSystem\\src\\main\\java\\ca\\senecapolytechnic\\hotelreservationsystem\\database\\hotel.db";
    // Check if the username and hashed password match an entry in the database
    public boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM User WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            // If a matching user is found, return true (valid login)
            return rs.next();

        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
            return false;
        }
    }
}
