package ca.senecapolytechnic.hotelreservationsystem.dao;

import ca.senecapolytechnic.hotelreservationsystem.models.Guest;
import ca.senecapolytechnic.hotelreservationsystem.models.Reservation;
import ca.senecapolytechnic.hotelreservationsystem.models.Room;
import ca.senecapolytechnic.hotelreservationsystem.models.RoomType;
import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ReservationDAO {
    String url = "jdbc:sqlite:C:\\Users\\Marcus\\IdeaProjects\\HotelReservationSystem\\src\\main\\java\\ca\\senecapolytechnic\\hotelreservationsystem\\database\\hotel.db";

    public int insertReservation(Reservation reservation, int guestId) {
        String sql = "INSERT INTO Reservation(guest_id, check_in_date, check_out_date, number_of_guests) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, guestId);
            pstmt.setString(2, reservation.getCheckInDate().toString());
            pstmt.setString(3, reservation.getCheckOutDate().toString());
            pstmt.setInt(4, reservation.getNumberOfGuests());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // reservation_id
            }

        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
        }
        return -1;
    }
    public Reservation getReservationById(int reservationId) {
        Reservation reservation = null;

        String sql = "SELECT * FROM Reservation WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                reservation = new Reservation();
                reservation.setReservationID(rs.getInt("id"));
                reservation.setCheckInDate(LocalDate.parse(rs.getString("check_in_date")));
                reservation.setCheckOutDate(LocalDate.parse(rs.getString("check_out_date")));
                reservation.setNumberOfGuests(rs.getInt("number_of_guests"));

                int guestId = rs.getInt("guest_id");

                // Load Guest
                GuestDAO guestDAO = new GuestDAO();
                Guest guest = guestDAO.getGuestById(guestId);
                reservation.setGuest(guest);

                // Load Rooms
                RoomDAO roomDAO = new RoomDAO();
                List<Room> rooms = roomDAO.getRoomsByReservationId(reservationId);
                reservation.setRooms(rooms);
            }
        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
        }
        return reservation;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT r.reservation_id, r.check_in_date, r.check_out_date, r.number_of_guests, " +
                "g.guest_id, g.name, g.phone_number, g.email, g.address " +
                "FROM Reservation r JOIN Guest g ON r.guest_id = g.guest_id";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Guest guest = new Guest(
                        rs.getInt("guest_id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("address")
                );

                Reservation reservation = new Reservation();
                reservation.setReservationID(rs.getInt("reservation_id"));
                reservation.setGuest(guest);
                reservation.setCheckInDate(LocalDate.parse(rs.getString("check_in_date")));
                reservation.setCheckOutDate(LocalDate.parse(rs.getString("check_out_date")));
                reservation.setNumberOfGuests(rs.getInt("number_of_guests"));

                // Attach rooms
                reservation.setRooms(getRoomsForReservation(reservation.getReservationID()));

                reservations.add(reservation);
            }

        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
        }

        return reservations;
    }
    private List<Room> getRoomsForReservation(int reservationId) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT room_id, room_type, price_per_night FROM Room WHERE reservation_id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservationId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Room room = new Room();
                room.setRoomID(rs.getInt("room_id"));
                room.setRoomType(RoomType.valueOf(rs.getString("room_type")));
                room.setPrice(rs.getDouble("price_per_night"));
                rooms.add(room);
            }
        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
        }
        return rooms;
    }
    public void deleteReservation(int reservationId) {
        String sql = "DELETE FROM Reservation WHERE reservation_id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
        }
    }
    public List<Reservation> searchReservations(String query) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.reservation_id, g.name, g.phone_number, r.check_in_date, r.check_out_date, r.number_of_guests " +
                "FROM Reservation r " +
                "JOIN Guest g ON r.guest_id = g.guest_id " +
                "WHERE LOWER(g.name) LIKE ? OR LOWER(g.phone_number) LIKE ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + query + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservationID(rs.getInt("reservation_id"));
                reservation.setGuestName(rs.getString("name"));
                reservation.setGuestPhone(rs.getString("phone_number"));
                reservation.setCheckInDate(LocalDate.parse(rs.getString("check_in_date")));
                reservation.setCheckOutDate(LocalDate.parse(rs.getString("check_out_date")));
                reservation.setNumberOfGuests(rs.getInt("number_of_guests"));

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
        }

        return reservations;
    }

    public boolean updateReservation(Reservation reservation, Guest guest, List<Room> rooms) {
        String reservationSql = "UPDATE Reservation SET check_in_date = ?, check_out_date = ?, number_of_guests = ? WHERE reservation_id = ?";

        try (Connection conn = DriverManager.getConnection(url)) {
            // Update reservation
            try (PreparedStatement pstmt = conn.prepareStatement(reservationSql)) {
                pstmt.setString(1, reservation.getCheckInDate().toString());
                pstmt.setString(2, reservation.getCheckOutDate().toString());
                pstmt.setInt(3, reservation.getNumberOfGuests());
                pstmt.setInt(4, reservation.getReservationID());
                pstmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            ExceptionLogger.log(Level.SEVERE, "Database error occurred", e);
            return false;
        }
    }
}
