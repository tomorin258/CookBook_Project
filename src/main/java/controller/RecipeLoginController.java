
/**
 * Controller for handling user login, registration, and navigation between login and signup views.
 * Provides methods for login, signup, and registration actions.
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Users;
import service.UserService;
import util.CurrentUser;

public class RecipeLoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Button signupBtn;

    @FXML private PasswordField confirmField;
    @FXML private Button registerBtn;
    @FXML private Button backBtn;
    @FXML private Label messageLabel;

    private final UserService userService = new UserService();

    /**
     * Handles the login button action. Authenticates the user and navigates to the recipe list view on success.
     * @param event the action event
     */
    @FXML
    private void onLogin(ActionEvent event) {
        String userName = usernameField.getText();
        String password = passwordField.getText();
        Users user = userService.login(userName, password); 
        if (user != null) {
            CurrentUser.setId(user.getUserId());

            messageLabel.setText("Login successfully.");
            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/recipe_list.fxml"));
                javafx.scene.Parent root = loader.load();
                javafx.stage.Stage stage = (javafx.stage.Stage) loginBtn.getScene().getWindow();
                stage.setScene(new javafx.scene.Scene(root));
            } catch (Exception e) {
                messageLabel.setText("Cannot open page: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Incorrect account or password.");
        }
    }

    /**
     * Handles the signup button action. Navigates to the signup view.
     * @param event the action event
     */
    @FXML
    private void onSignup(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/signup.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) signupBtn.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            messageLabel.setText("cannot open signup" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the register button action. Registers a new user and shows validation messages.
     * @param event the action event
     */
    @FXML
    private void onRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirm = confirmField.getText();
        String result = userService.register(username, password, confirm);
        if (username != null && username.length() > 8) {
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
                "Username cannot exceed 8 characters!").showAndWait();
            return;
        }
        if (password != null && password.length() > 12) {
        new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
            "Password cannot exceed 12 characters!").showAndWait();
        return;
        }
        if (password != null && password.matches(".*\\p{Punct}.*")) {
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
            "Password cannot contain punctuation symbols!").showAndWait();
        return;
        }
        messageLabel.setText(result);
    }

    /**
     * Handles the back button action. Navigates back to the login view.
     * @param event the action event
     */
    @FXML
    private void onBackToLogin(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) backBtn.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            messageLabel.setText("cannot open login" + e.getMessage());
            e.printStackTrace();
        }
    }
}