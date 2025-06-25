package controller;

import dao.mappers.UsersMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Users;
import service.UserService;

public class RecipeLoginController {

    // to modify login.fxml
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Button signupBtn;

    // sign up related fields
    @FXML private PasswordField confirmField;
    @FXML private Button registerBtn;
    @FXML private Button backBtn;
    @FXML private Label messageLabel;

    private UsersMapper usersMapper;
    private final UserService userService = new UserService();

    // set UsersMapper
    public void setUsersMapper(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    // loginBtn event handler
    @FXML
    private void onLogin(ActionEvent event) {
        String userName = usernameField.getText();
        String password = passwordField.getText();
        boolean success = userService.login(userName, password);
        if (success) {
            messageLabel.setText("Login successfully.");
            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/recipelist.fxml"));
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

    // signupBtn event handler
    @FXML
    private void onSignup(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/signup.fxml"));
            javafx.scene.Parent root = loader.load();
            
            RecipeLoginController signupController = loader.getController();
            signupController.setUsersMapper(this.usersMapper);

            javafx.stage.Stage stage = (javafx.stage.Stage) signupBtn.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            messageLabel.setText("cannot open signup" + e.getMessage());
            e.printStackTrace();
        }
    }

    // sign up logic
    @FXML
    private void onRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirm = confirmField.getText();
        String result = userService.register(username, password, confirm);
        messageLabel.setText(result);
    }

    // jump back to login feature
    @FXML
    private void onBackToLogin(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            javafx.scene.Parent root = loader.load();

            RecipeLoginController loginController = loader.getController();
            loginController.setUsersMapper(this.usersMapper);

            javafx.stage.Stage stage = (javafx.stage.Stage) backBtn.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            messageLabel.setText("cannot open login" + e.getMessage());
            e.printStackTrace();
        }
    }
}