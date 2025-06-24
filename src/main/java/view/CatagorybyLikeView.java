package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CatagorybyLikeView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/recipe_sortbylikes.fxml"));

        // Build scene
        Scene scene = new Scene(loader.load());

        // Stage settings
        primaryStage.setTitle("Most-Liked Recipes");
        primaryStage.setResizable(false); // Fixed size 768 × 850
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
