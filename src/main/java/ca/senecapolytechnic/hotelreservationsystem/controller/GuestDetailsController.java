package ca.senecapolytechnic.hotelreservationsystem.controller;

import ca.senecapolytechnic.hotelreservationsystem.models.Guest;
import ca.senecapolytechnic.hotelreservationsystem.models.Reservation;
import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;

public class GuestDetailsController {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;;
    @FXML private Button nextButton;
    @FXML private Button backButton;

    private Reservation reservation;
    private Boolean admin;

    public void setLoggedIn(boolean loggedIn){
        admin = loggedIn;
    }
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        if (reservation.getGuest().getName()!= null){
            nameField.setText(reservation.getGuest().getName());
        }
        if (reservation.getGuest().getPhoneNumber()!= null){
            phoneField.setText(reservation.getGuest().getPhoneNumber());
        }
        if (reservation.getGuest().getEmail()!= null){
            emailField.setText(reservation.getGuest().getEmail());
        }
        if (reservation.getGuest().getAddress()!= null){
            addressField.setText(reservation.getGuest().getAddress());
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/booking-details.fxml"));
            Parent root = loader.load();

            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String address = addressField.getText();

            Guest guest = new Guest();
            guest.setName(name);
            guest.setPhoneNumber(phone);
            guest.setEmail(email);
            guest.setAddress(address);

            reservation.setGuest(guest);

            BookingDetailsController bookingDetailsController = loader.getController();
            bookingDetailsController.setReservation(reservation);
            bookingDetailsController.setLoggedIn(admin);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Guest Detail error: ", e);
        }
    }
    @FXML
    private void handleNext() {
        try {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String address = addressField.getText();
            if(name.isEmpty()){
                System.out.println("Please include name");
                showError("Invalid input", "Please include name");
                return;
            }
            if(phone.isEmpty()){
                showError("Invalid input", "Please include phone");
                System.out.println("Please include phone");
                return;
            }
            if(email.isEmpty()){
                System.out.println("Please include email");
                showError("Invalid input", "Please include email");
                return;
            }
            if(address.isEmpty()){
                System.out.println("Please include address");
                showError("Invalid input", "Please include address");
                return;
            }

            reservation.getGuest().setName(name);
            reservation.getGuest().setPhoneNumber(phone);
            reservation.getGuest().setEmail(email);
            reservation.getGuest().setAddress(address);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/room-select.fxml"));
            Parent root = loader.load();

            RoomSelectController roomSelectController = loader.getController();
            roomSelectController.setReservation(reservation);
            roomSelectController.setLoggedIn(admin);

            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Guest Detail error: ", e);
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
