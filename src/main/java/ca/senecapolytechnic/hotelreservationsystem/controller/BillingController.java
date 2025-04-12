package ca.senecapolytechnic.hotelreservationsystem.controller;

import ca.senecapolytechnic.hotelreservationsystem.dao.ReservationDAO;
import ca.senecapolytechnic.hotelreservationsystem.models.Reservation;
import ca.senecapolytechnic.hotelreservationsystem.models.Room;
import ca.senecapolytechnic.hotelreservationsystem.utility.AdminActivityLogger;
import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

public class BillingController {

    @FXML private Text nameText;
    @FXML private Text phoneText;
    @FXML private Text emailText;
    @FXML private Text guestsText;
    @FXML private Text checkInText;
    @FXML private Text checkOutText;
    @FXML private Text priceText;
    @FXML private Text taxText;
    @FXML private Text totalText;
    @FXML private VBox roomListVBox;
    @FXML private TextField dicountTextField;
    @FXML private Button exitButton;

    private Reservation reservation;

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        displayBookingInfo();
    }

    public void displayBookingInfo(){

        nameText.setText(reservation.getGuest().getName());
        phoneText.setText(reservation.getGuest().getPhoneNumber());
        emailText.setText(reservation.getGuest().getEmail());
        guestsText.setText(String.valueOf(reservation.getNumberOfGuests())+ " Guest(s)");

        for (Room room : reservation.getRooms()) {
            Label roomLabel = new Label("  " + room.getRoomType() + " x1");
            roomListVBox.getChildren().add(roomLabel);
        }

        checkInText.setText("Check in: " + formatDate(reservation.getCheckInDate()));
        checkOutText.setText("Check out: " + formatDate(reservation.getCheckOutDate()));

        Double price = reservation.calculatePrice();
        Double tax = price * 0.13;
        Double discount = 0.0;
        Double total = price * (1 - (discount * 0.01)) + tax;

        priceText.setText("Price: $" + String.valueOf(price));
        taxText.setText("Tax: $" + String.valueOf(tax));
        totalText.setText("Total: $" + String.valueOf(total));
    }
    @FXML
    public void calcTotal(){
        Double price = reservation.calculatePrice();
        Double tax = price * 0.3;
        Double discount = Double.valueOf(dicountTextField.getText()) ;
        Double total = price * (1 - (discount * 0.01)) + tax;

        totalText.setText("Total: $" + String.valueOf(total));
    }

    public String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return date.format(formatter);
    }

    public void handlePay(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Payment Complete?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                ReservationDAO dao = new ReservationDAO();
                dao.deleteReservation(reservation.getReservationID());
                AdminActivityLogger.log("Checkout and delete reservation ID: " + reservation.getReservationID());
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/admin-dashboard.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) exitButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                } catch (
                        IOException e) {
                    ExceptionLogger.log(Level.SEVERE, "Billing error: ", e);
                }
            }
            else {
                showErrorDialog("Payment not complete.");
            }
        });
    }
    public void handleExit(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/admin-dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (
                IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Billing error: ", e);
        }
    }
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

