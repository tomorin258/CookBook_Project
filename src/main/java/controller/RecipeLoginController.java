package controller;

<<<<<<< HEAD
=======
import dao.mappers.UsersMapper;
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
<<<<<<< HEAD
import service.UserService;

public class RecipeLoginController {

=======
import model.Users;

public class RecipeLoginController {

    // to modify login.fxml
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Button signupBtn;

<<<<<<< HEAD
=======
    // sign up related fields
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
    @FXML private PasswordField confirmField;
    @FXML private Button registerBtn;
    @FXML private Button backBtn;
    @FXML private Label messageLabel;

<<<<<<< HEAD
    private final UserService userService = new UserService();

=======
    private UsersMapper usersMapper;

    // set UsersMapper
    public void setUsersMapper(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    // login logic
    private boolean login(String userName, String password) {
        if (usersMapper == null) {
            messageLabel.setText("system error: UsersMapper is not set.");
            return false;
        }
        Users user = usersMapper.getUsersByName(userName).stream().findFirst().orElse(null);
        return user != null && user.getPassword().equals(password);
    }

    // loginBtn event handler
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
    @FXML
    private void onLogin(ActionEvent event) {
        String userName = usernameField.getText();
        String password = passwordField.getText();
<<<<<<< HEAD
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
=======
        boolean success = login(userName, password);
        if (success) {
            messageLabel.setText("Login successfully.");
            // TODO: jump to the main application window
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
        } else {
            messageLabel.setText("Incorrect account or password.");
        }
    }

<<<<<<< HEAD
=======
    // signupBtn event handler
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
    @FXML
    private void onSignup(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/signup.fxml"));
            javafx.scene.Parent root = loader.load();
<<<<<<< HEAD
=======
            
            // Set the UsersMapper in the controller
            if (usersMapper == null) {
                messageLabel.setText("system error: UsersMapper is not set.");
                return;
            }
            RecipeLoginController signupController = loader.getController();
            signupController.setUsersMapper(this.usersMapper);

>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
            javafx.stage.Stage stage = (javafx.stage.Stage) signupBtn.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            messageLabel.setText("cannot open signup" + e.getMessage());
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
=======
    // sign up logic
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
    @FXML
    private void onRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirm = confirmField.getText();
<<<<<<< HEAD
        String result = userService.register(username, password, confirm);
        messageLabel.setText(result);
    }

=======

        if (username == null || username.isEmpty()) {
            messageLabel.setText("account name cannot be empty!");
            return;
        }
        if (password == null || password.isEmpty()) {
            messageLabel.setText("password cannot be empty!");
            return;
        }
        if (!password.equals(confirm)) {
            messageLabel.setText("the two passwords do not match!");
            return;
        }
        if (usersMapper == null) {
            messageLabel.setText("system error: UsersMapper is not set.");
            return;
        }
        // search for existing user
        Users existUser = usersMapper.getUsersByName(username).stream().findFirst().orElse(null);
        if (existUser != null) {
            messageLabel.setText("account already exists, please choose another one!");
            return;
        }
        // create new user
        Users newUser = new Users();
        newUser.setUserName(username);
        newUser.setPassword(password);
        usersMapper.addUsers(newUser);
        messageLabel.setText("sign up successfully, please login now!");
    }

    // jump back to login feature
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
    @FXML
    private void onBackToLogin(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            javafx.scene.Parent root = loader.load();
<<<<<<< HEAD
=======

            // Set the UsersMapper in the controller
            if (usersMapper == null) {
                messageLabel.setText("system error: UsersMapper is not set.");
                return;
            }
            RecipeLoginController loginController = loader.getController();
            loginController.setUsersMapper(this.usersMapper);

>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
            javafx.stage.Stage stage = (javafx.stage.Stage) backBtn.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            messageLabel.setText("cannot open login" + e.getMessage());
            e.printStackTrace();
        }
    }
}