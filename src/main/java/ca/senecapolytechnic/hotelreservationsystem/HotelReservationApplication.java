package ca.senecapolytechnic.hotelreservationsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HotelReservationApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HotelReservationApplication.class.getResource("/ca/senecapolytechnic/hotelreservationsystem/welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 475, 450);
        stage.setTitle("Hotel Reservation System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}