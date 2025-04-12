package ca.senecapolytechnic.hotelreservationsystem.controller;

import ca.senecapolytechnic.hotelreservationsystem.models.Reservation;
import ca.senecapolytechnic.hotelreservationsystem.models.Room;
import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;

public class BookingConfirmedController {
    @FXML private Text nameText;
    @FXML private Text phoneText;
    @FXML private Text emailText;
    @FXML private Text idText;
    @FXML private Text guestsText;
    @FXML private Text checkInText;
    @FXML private Text checkOutText;
    @FXML private Text priceText;
    @FXML private Text taxText;
    @FXML private Text totalText;
    @FXML private VBox roomListVBox;
    @FXML private Button closeButton;

    private Reservation reservation;
    private Boolean admin;

    public void setLoggedIn(boolean loggedIn){
        admin = loggedIn;
    }
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        displayBookingInfo();
    }

    public void displayBookingInfo(){

    nameText.setText(reservation.getGuest().getName());
    phoneText.setText(reservation.getGuest().getPhoneNumber());
    emailText.setText(reservation.getGuest().getEmail());
    guestsText.setText(String.valueOf(reservation.getNumberOfGuests())+ " Guest(s)");
    idText.setText("ID: " + reservation.getReservationID());
        for (Room room : reservation.getRooms()) {
            Label roomLabel = new Label("  " + room.getRoomType() + " x1");
            roomListVBox.getChildren().add(roomLabel);
        }

        checkInText.setText("Check in: " + formatDate(reservation.getCheckInDate()));
        checkOutText.setText("Check out: " + formatDate(reservation.getCheckOutDate()));

        Double price = reservation.calculatePrice();
        Double tax = price * 0.3;
        Double total = price + tax;

        priceText.setText("Price: $" + String.valueOf(price));
        taxText.setText("Tax: $" + String.valueOf(tax));
        totalText.setText("Total: $" + String.valueOf(total));
    }

    public String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return date.format(formatter);
    }

    public void handleClose(){
        try{
            if (admin) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/admin-dashboard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/welcome.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            }
    } catch (
    IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Booking Confirmed error: ", e);
        }
    }
}
