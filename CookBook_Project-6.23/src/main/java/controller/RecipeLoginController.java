package controller;

import dao.mappers.UsersMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Users;

public class RecipeLoginController {

    // to modify login.fxml
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Button signupBtn;
    @FXML private Label errorLabel;

    private UsersMapper usersMapper;

    // set UsersMapper
    public void setUsersMapper(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    // login logic
    private boolean login(String userName, String password) {
        if (usersMapper == null) {
            errorLabel.setText("system error: UsersMapper is not set.");
            return false;
        }
        Users user = usersMapper.getUsersByName(userName).stream().findFirst().orElse(null);
        return user != null && user.getPassword().equals(password);
    }

    // loginBtn event handler
    @FXML
    private void onLogin(ActionEvent event) {
        String userName = usernameField.getText();
        String password = passwordField.getText();
        boolean success = login(userName, password);
        if (success) {
            errorLabel.setText("Login successfully.");
            // TODO: jump to the main application window
        } else {
            errorLabel.setText("Incorrect account or password.");
        }
    }

    // registerBtn event handler
    @FXML
    private void onSignup(ActionEvent event) {
        errorLabel.setText("Sign up feature not implemented yet.");
        // TODO: enable sign up feature
    }
}