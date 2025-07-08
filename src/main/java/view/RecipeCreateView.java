package view;

import controller.RecipeEditAddController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Recipes;

/**
 * A modal {@link javafx.stage.Stage Stage} that lets the user create a new
 * {@link model.Recipes recipe} or edit an existing one.
 *
 * <p>The view is defined in {@code /fxml/recipe_edit_add.fxml}. Once the FXML
 * has been loaded, its {@link controller.RecipeEditAddController} is obtained
 * so callers can interact with it via {@link #getController()}. If the
 * {@linkplain #RecipeCreateView(model.Recipes) constructor} receives a non-null
 * {@code Recipes} instance, the controller is pre-populated with that data;
 * otherwise the dialog opens in “Add Recipe” mode.</p>
 *
 * <p>The window is fixed-size, non-resizable, and shown with
 * {@link javafx.stage.Modality#APPLICATION_MODAL APPLICATION_MODAL}, blocking
 * its owner until the user closes it.</p>
 *
 * @author Hao He
 */

public class RecipeCreateView extends Stage {

    private final RecipeEditAddController controller;

    public RecipeCreateView() throws Exception {
        this(null);
    }

    public RecipeCreateView(Recipes editingRecipe) throws Exception {
        // 1. Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/recipe_edit_add.fxml"));
        Scene scene = new Scene(loader.load());

        // 2. Get controller instance
        controller = loader.getController();

        if (editingRecipe != null) {
            controller.loadRecipe(editingRecipe);
        }

        // 3. Configure stage
        setTitle(editingRecipe == null ? "Add Recipe" : "Edit Recipe");
        setResizable(false);                    // Fixed size 768 × 850
        initModality(Modality.APPLICATION_MODAL); // Modal blocks interaction with the parent window
        setScene(scene);
    }

    public RecipeEditAddController getController() {
        return controller;
    }
}
