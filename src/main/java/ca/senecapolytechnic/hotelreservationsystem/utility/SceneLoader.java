package ca.senecapolytechnic.hotelreservationsystem.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {
    public static void loadScene(Stage stage, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(SceneLoader.class.getResource("/ca/senecapolytechnic/hotelreservationsystem/" + fxmlFile));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
