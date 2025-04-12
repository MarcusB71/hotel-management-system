package ca.senecapolytechnic.hotelreservationsystem.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class SceneController {

    private Stage primaryStage;
    private final HashMap<String, Scene> sceneCache = new HashMap<>();

    public SceneController(Stage stage) {
        this.primaryStage = stage;
    }

    public void switchTo(String fxmlName) {
        try {
            Scene scene;
            if (sceneCache.containsKey(fxmlName)) {
                scene = sceneCache.get(fxmlName);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/" + fxmlName + ".fxml"));
                Parent root = loader.load();
                scene = new Scene(root);
                sceneCache.put(fxmlName, scene);
            }
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to load FXML: " + fxmlName);
        }
    }

    public void clearCache() {
        sceneCache.clear();
    }

    public void removeFromCache(String fxmlName) {
        sceneCache.remove(fxmlName);
    }
}

