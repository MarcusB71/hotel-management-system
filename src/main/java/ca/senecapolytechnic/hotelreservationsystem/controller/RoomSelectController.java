package ca.senecapolytechnic.hotelreservationsystem.controller;

import ca.senecapolytechnic.hotelreservationsystem.dao.GuestDAO;
import ca.senecapolytechnic.hotelreservationsystem.dao.ReservationDAO;
import ca.senecapolytechnic.hotelreservationsystem.dao.RoomDAO;
import ca.senecapolytechnic.hotelreservationsystem.models.Guest;
import ca.senecapolytechnic.hotelreservationsystem.models.Reservation;
import ca.senecapolytechnic.hotelreservationsystem.models.Room;
import ca.senecapolytechnic.hotelreservationsystem.models.RoomType;
import ca.senecapolytechnic.hotelreservationsystem.utility.AdminActivityLogger;
import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;


public class RoomSelectController {
    @FXML private Spinner<Integer> singleSpinner;
    @FXML private Spinner<Integer> doubleSpinner;
    @FXML private Spinner<Integer> deluxSpinner;
    @FXML private Spinner<Integer> penthouseSpinner;
    @FXML private Button confirmSelectionButton;
    @FXML private Button backButton;

    private Reservation reservation;
    private Boolean admin;
    private int singleCount;
    private int doubleCount;
    private int deluxeCount;
    private int penthouseCount;

    public void setLoggedIn(boolean loggedIn){
        admin = loggedIn;
    }
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        Map<RoomType, Long> roomTypeCounts = reservation.getRooms().stream()
                .collect(Collectors.groupingBy(Room::getRoomType, Collectors.counting()));

        // Default values to 0 if not present
        singleCount = roomTypeCounts.getOrDefault(RoomType.SINGLE, 0L).intValue();
        doubleCount = roomTypeCounts.getOrDefault(RoomType.DOUBLE, 0L).intValue();
        deluxeCount = roomTypeCounts.getOrDefault(RoomType.DELUX, 0L).intValue();
        penthouseCount = roomTypeCounts.getOrDefault(RoomType.PENT_HOUSE, 0L).intValue();


        singleSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, singleCount));
        doubleSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, doubleCount));
        deluxSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, deluxeCount));
        penthouseSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, penthouseCount));
    }
    @FXML
    private void initialize() {
    }

    @FXML
    private void handleRulesRegulations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/rules-regulations.fxml"));
            Parent root = loader.load();

            RulesRegulationController rulesRegulationController = loader.getController();
            rulesRegulationController.setReservation(reservation);
            rulesRegulationController.setLoggedIn(admin);

            Stage stage = (Stage) confirmSelectionButton.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Room Select error: ", e);
        }
    }
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/guest-details.fxml"));
            Parent root = loader.load();

            int singleCount = singleSpinner.getValue();
            int doubleCount = doubleSpinner.getValue();
            int deluxeCount = deluxSpinner.getValue();
            int penthouseCount = penthouseSpinner.getValue();

            // Add rooms
            addRooms(RoomType.SINGLE, singleCount);
            addRooms(RoomType.DOUBLE, doubleCount);
            addRooms(RoomType.DELUX, deluxeCount);
            addRooms(RoomType.PENT_HOUSE, penthouseCount);

            GuestDetailsController guestDetailController = loader.getController();
            guestDetailController.setReservation(reservation);
            guestDetailController.setLoggedIn(admin);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Room Select error: ", e);
        }
    }
    @FXML
    private void handleConfirmSelection() {
        try {
            reservation.getRooms().clear();
            int singleCount = singleSpinner.getValue();
            int doubleCount = doubleSpinner.getValue();
            int deluxeCount = deluxSpinner.getValue();
            int penthouseCount = penthouseSpinner.getValue();

            int totalRooms = singleCount + doubleCount + deluxeCount + penthouseCount;
            if ((totalRooms * 2) < reservation.getNumberOfGuests()){
                showError("Too many guests per room!", "Please Review rules and regulations");
                return;
            }

            // Add rooms
            addRooms(RoomType.SINGLE, singleCount);
            addRooms(RoomType.DOUBLE, doubleCount);
            addRooms(RoomType.DELUX, deluxeCount);
            addRooms(RoomType.PENT_HOUSE, penthouseCount);

            if (reservation.getRooms().isEmpty()) {
                System.out.println("Please select at least one room.");
                showError("Invalid room selection", "Please select at least one room");
                return;
            }

            GuestDAO guestDAO = new GuestDAO();
            ReservationDAO reservationDAO = new ReservationDAO();
            RoomDAO roomDAO = new RoomDAO();

            //if ID exists, we update the reservation otherwise we are making a new reservation
            if (reservation.getReservationID() > 0){
                boolean reservationUpdated = reservationDAO.updateReservation(reservation, reservation.getGuest(), reservation.getRooms());
                boolean guestUpdated = guestDAO.updateGuest(reservation.getGuest());
                boolean roomsUpdated = roomDAO.updateRoomsByReservationID(reservation.getRooms(), reservation.getReservationID());
                if (reservationUpdated && guestUpdated && roomsUpdated) {
                    AdminActivityLogger.log("Reservation Modified: " + reservation.getReservationID());
                    showConfirmation("Update Successful", "Reservation details were updated.");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/admin-dashboard.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) confirmSelectionButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                } else {
                    showError("Update Failed", "There was an error updating the reservation.");
                }
            } else {
                int guestId = guestDAO.insertGuest(reservation.getGuest());
                int reservationId = reservationDAO.insertReservation(reservation, guestId);
                roomDAO.insertRooms(reservation.getRooms(), reservationId);

                reservation.setReservationID(reservationId);
                reservation.getGuest().setGuestID(guestId);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/booking-confirmed.fxml"));
                Parent root = loader.load();

                BookingConfirmedController bookingConfirmedController = loader.getController();
                bookingConfirmedController.setReservation(reservation);
                bookingConfirmedController.setLoggedIn(admin);

                Stage stage = (Stage) confirmSelectionButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            }
        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Room Select error: ", e);
        }
    }
    private void addRooms(RoomType type, int count) {
        for (int i = 0; i < count; i++) {
            Room room = new Room();
            room.setRoomType(type);
            room.setPrice(calculatePrice(type));
            reservation.addRoom(room);
        }
    }
    private double calculatePrice(RoomType type) {
        return switch (type) {
            case SINGLE -> 100.00;
            case DOUBLE -> 150.00;
            case DELUX -> 220.00;
            case PENT_HOUSE -> 400.00;
        };
    }
    private void showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
