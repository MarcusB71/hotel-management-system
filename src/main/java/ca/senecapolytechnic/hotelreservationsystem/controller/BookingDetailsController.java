package ca.senecapolytechnic.hotelreservationsystem.controller;

import ca.senecapolytechnic.hotelreservationsystem.models.Guest;
import ca.senecapolytechnic.hotelreservationsystem.models.Reservation;
import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;
import ca.senecapolytechnic.hotelreservationsystem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;

public class BookingDetailsController {

    @FXML private TextField numGuestsField;
    @FXML private DatePicker checkInDatePicker;
    @FXML private DatePicker checkOutDatePicker;
    @FXML private Label statusLabel;
    @FXML private Button nextButton;
    @FXML private Button backButton;

    private Reservation reservation;
    private Boolean admin;

    public void setLoggedIn(boolean loggedIn){
        admin = loggedIn;
    }
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        if (reservation.getNumberOfGuests() != 0){
            numGuestsField.setText(String.valueOf(reservation.getNumberOfGuests()));
        }
        if (reservation.getCheckInDate() != null){
            checkInDatePicker.setValue(reservation.getCheckInDate());
        }
        if (reservation.getCheckOutDate() != null){
            checkOutDatePicker.setValue(reservation.getCheckOutDate());
        }
    }

    @FXML
    private void handleBack() {
        try {
            if (admin) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/admin-dashboard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/welcome.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            }
        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Booking Detail error: ", e);
        }
    }
    @FXML
    private void handleNext() {
        try {
            LocalDate checkIn = checkInDatePicker.getValue();
            LocalDate checkOut = checkOutDatePicker.getValue();
            if (numGuestsField.getText().isEmpty()) {
                System.out.println("Invalid number of guests.");
                showError("Invalid number of guests", "Please include number of guests");
                return;
            }
            if (checkIn == null || checkOut == null || checkOut.isBefore(checkIn)) {
                System.out.println("Invalid date selection.");
                showError("Invalid date selection", "Please select a valid check in and check out");
                return;
            }
            int numGuests = Integer.parseInt(numGuestsField.getText());

            reservation.setCheckInDate(checkIn);
            reservation.setCheckOutDate(checkOut);
            reservation.setNumberOfGuests(numGuests);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/guest-details.fxml"));
            Parent root = loader.load();

            GuestDetailsController guestDetailsController = loader.getController();
            guestDetailsController.setReservation(reservation);
            guestDetailsController.setLoggedIn(admin);

            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Booking Detail error: ", e);
        }
    }
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

