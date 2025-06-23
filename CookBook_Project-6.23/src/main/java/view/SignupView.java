package view;

import controller.RecipeLoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SignupView extends Stage {

    private final RecipeLoginController controller;

    public SignupView() throws Exception {
        // 1. Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signup.fxml"));
        Scene scene = new Scene(loader.load());

        // 2. Obtain controller
        controller = loader.getController();

        // 3. Configure stage
        setTitle("CookBook — Sign Up");
        setResizable(false);                    // Fixed size 640 × 420
        initModality(Modality.APPLICATION_MODAL); // Modal — blocks the login window
        setScene(scene);
    }

    public RecipeLoginController getController() {
        return controller;
    }
}
