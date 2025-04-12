package ca.senecapolytechnic.hotelreservationsystem.controller;

import ca.senecapolytechnic.hotelreservationsystem.models.Reservation;
import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;
import ca.senecapolytechnic.hotelreservationsystem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;

public class WelcomeController {
    @FXML
    private Button bookNow;

    @FXML private Label welcomeLabel;
    @FXML private MediaView mediaView;

    public void initialize() {
        String videoPath = getClass().getResource("/videos/SenecaHotelWalkthrough.mp4").toExternalForm(); // Ensure it's in resources

        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

    }
    @FXML
    private void handlePause(){
        mediaView.getMediaPlayer().pause();
    }
    @FXML
    private void handlePlay(){
        mediaView.getMediaPlayer().play();
    }
    @FXML
    private void handleBookNow(ActionEvent event) {
        try {
            Reservation reservation = new Reservation();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/booking-details.fxml"));
            Parent root = loader.load();

            BookingDetailsController bookingController = loader.getController();
            bookingController.setReservation(reservation);
            bookingController.setLoggedIn(false);

            Stage stage = (Stage) bookNow.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Welcome screen error: ", e);
        }
    }
    @FXML
    private void handleAdminLogin(ActionEvent event) {
//        update to include format of above I think
        SceneLoader.loadScene((Stage) bookNow.getScene().getWindow(), "admin-login.fxml");
    }
    @FXML
    private void handleLeaveReview(ActionEvent event) {
        SceneLoader.loadScene((Stage) bookNow.getScene().getWindow(), "review.fxml");
    }
}

