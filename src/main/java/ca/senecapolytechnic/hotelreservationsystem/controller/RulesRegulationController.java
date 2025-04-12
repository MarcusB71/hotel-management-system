package ca.senecapolytechnic.hotelreservationsystem.controller;

import ca.senecapolytechnic.hotelreservationsystem.models.Reservation;
import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;

public class RulesRegulationController {
    @FXML private Button backButton;

    private Reservation reservation;
    private boolean admin;
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    public void setLoggedIn(boolean loggedIn){
        admin = loggedIn;
    }
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/room-select.fxml"));
            Parent root = loader.load();

            RoomSelectController roomSelectController = loader.getController();
            roomSelectController.setReservation(reservation);
            roomSelectController.setLoggedIn(admin);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Rules Regulation error: ", e);
        }
    }


}
