package ca.senecapolytechnic.hotelreservationsystem.controller;

import ca.senecapolytechnic.hotelreservationsystem.dao.UserDAO;
import ca.senecapolytechnic.hotelreservationsystem.models.Reservation;
import ca.senecapolytechnic.hotelreservationsystem.utility.AdminActivityLogger;
import ca.senecapolytechnic.hotelreservationsystem.utility.ExceptionLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;

public class AdminLoginController {

    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private Button backButton;
    @FXML private Button loginButton;
    @FXML private Label loginMsgLabel;


    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/welcome.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            ExceptionLogger.log(Level.SEVERE, "Admin Login error: ", e);
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        UserDAO userDAO = new UserDAO();
        boolean user = userDAO.validateLogin(username, password);
        if (user) {
            loginMsgLabel.setText("Login successful!");
            AdminActivityLogger.log("Admin logged in at " + LocalDateTime.now());
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/senecapolytechnic/hotelreservationsystem/admin-dashboard.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) backButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                } catch (IOException e) {
                    ExceptionLogger.log(Level.SEVERE, "Admin Login error: ", e);
                }
        } else {
            loginMsgLabel.setText("Invalid credentials!");
        }


    }
}
