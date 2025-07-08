package view;

import controller.RecipeLoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A modal {@link javafx.stage.Stage Stage} that hosts the “CookBook&nbsp;—&nbsp;Sign Up” form.
 *
 * <p>The UI is loaded from {@code /fxml/signup.fxml}. Once the FXML has been
 * parsed, its {@link controller.RecipeLoginController} is stored and can be
 * accessed via {@link #getController()}.</p>
 *
 * <p>The window is fixed-size (<strong>640 × 420&nbsp;px</strong>), non-resizable,
 * and displayed with {@link javafx.stage.Modality#APPLICATION_MODAL
 * APPLICATION_MODAL}, blocking its owner until the user closes it.</p>
 *
 * @author Hao He
 */

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
        initModality(Modality.APPLICATION_MODAL); // Modal blocks the login window
        setScene(scene);
    }

    public RecipeLoginController getController() {
        return controller;
    }
}
