package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));

        // Build scene
        Scene scene = new Scene(loader.load());

        // Stage settings
        primaryStage.setTitle("CookBook — Login");
        primaryStage.setResizable(false); // Disable resizing
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
