package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginView extends Stage {
    public LoginView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load());
        this.setTitle("CookBook — Login");
        this.setResizable(false);
        this.setScene(scene);
    }
}
