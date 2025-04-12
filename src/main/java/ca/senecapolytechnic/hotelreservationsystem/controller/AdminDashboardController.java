package ca.senecapolytechnic.hotelreservationsystem.controller;

import ca.senecapolytechnic.hotelreservationsystem.dao.ReservationDAO;
import ca.senecapolytechnic.hotelreservationsystem.models.Reservation;
import ca.senecapolytechnic.hotelreservationsystem.utility.AdminActivityLogger;
import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class AdminDashboardController {

    @FXML private Button backButton;
    @FXML private Button newBookingButton;
    @FXML private Button modifyBookingButton;
    @FXML private Button checkoutButton;
    @FXML private Button cancelBookingButton;
    @FXML private TextField searchTextField;

    @FXML private TableView<Reservation> allReservationsTable;
    @FXML private TableColumn<Reservation, Integer> reservationIDCol;
    @FXML private TableColumn<Reservation, String> nameCol;
    @FXML private TableColumn<Reservation, String> phoneCol;

    @FXML
    public void initialize() {
        reservationIDCol.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("guestPhone"));

        loadReservations();

    }
    private void loadReservations(){
        ReservationDAO reservationDAO = new ReservationDAO();
        List<Reservation> reservationList = reservationDAO.getAllReservations();
        allReservationsTable.setItems(FXCollections.observableList(reservationList));
    }
    @FXML
    private void handleNewBooking() {
        try {
            Reservation reservation = new Reservation();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/booking-details.fxml"));
            Parent root = loader.load();

            BookingDetailsController bookingController = loader.getController();
            bookingController.setReservation(reservation);
            bookingController.setLoggedIn(true);

            Stage stage = (Stage) newBookingButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Admin Dashboard error: ", e);
        }
    }

    @FXML
    private void handleModifyBooking() {
        try {
            Reservation selectedReservation = allReservationsTable.getSelectionModel().getSelectedItem();
            if (selectedReservation != null) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/booking-details.fxml"));
            Parent root = loader.load();

            BookingDetailsController bookingController = loader.getController();
            bookingController.setReservation(selectedReservation);
            bookingController.setLoggedIn(true);

            Stage stage = (Stage) modifyBookingButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            } else {
                showErrorDialog("No reservation selected.");
            }
        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Admin Dashboard error: ", e);
        }
    }
    @FXML
    private void handleCheckout() {
          try {
            Reservation selectedReservation = allReservationsTable.getSelectionModel().getSelectedItem();
            if (selectedReservation != null) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/billing.fxml"));
                Parent root = loader.load();

                BillingController billingController = loader.getController();
                billingController.setReservation(selectedReservation);

                Stage stage = (Stage) modifyBookingButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                showErrorDialog("No reservation selected.");
            }
        } catch (IOException e) {
              ExceptionLogger.log(Level.SEVERE, "Admin Dashboard error: ", e);
        }
    }
    @FXML
    private void handleCancelBooking() {
        Reservation selectedReservation = allReservationsTable.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this reservation?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    ReservationDAO dao = new ReservationDAO();
                    dao.deleteReservation(selectedReservation.getReservationID());
                    AdminActivityLogger.log("Deleted reservation ID: " + selectedReservation.getReservationID());
                    allReservationsTable.getItems().remove(selectedReservation);
                }
                else {
                    showErrorDialog("No reservation selected.");
                }
            });
        }
    }
    @FXML
    private void handleSearch() {
        String searchQuery = searchTextField.getText().toLowerCase();

        if (searchQuery.isEmpty()) {
            // If no search query, show all reservations
            loadReservations();
        } else {
            AdminActivityLogger.log("Searched for guest with query: " + searchQuery);
            ReservationDAO reservationDAO = new ReservationDAO();
            List<Reservation> searchResults = reservationDAO.searchReservations(searchQuery);
            allReservationsTable.getItems().setAll(searchResults);
        }
    }
    @FXML
    private void handleBack() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/welcome.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Admin Dashboard error: ", e);
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
