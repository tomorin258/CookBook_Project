package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX {@link javafx.application.Application Application} that launches the
 * “Most-Liked Recipes” main window.
 *
 * <p>The UI is defined in {@code /fxml/recipe_sortbylikes.fxml}. After loading,
 * it is wrapped in a {@link javafx.scene.Scene Scene} and shown in the primary
 * {@link javafx.stage.Stage Stage} with the title “Most-Liked Recipes”.
 * The stage is set to <em>non-resizable</em>; its size comes
 * from the root layout specified in the FXML file.</p>
 *
 * <p>This class merely bootstraps the JavaFX runtime and displays the window.
 * All data binding and the actual “order-by-likes” logic live in the controller
 * declared inside the FXML.</p>
 *
 * @author Hao He
 */

public class CategorybyLikeView extends Application {

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
