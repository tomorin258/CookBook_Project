package view;

import controller.RecipeInteractionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Recipes;

import java.io.IOException;

/**
 * A modal dialog window that presents the details of one {@link Recipes} item.
 * <p>
 * The UI is loaded from {@code /fxml/recipe_detail.fxml}. After loading, the
 * selected recipe is injected into the {@link RecipeInteractionController}
 * so that all labels / buttons within the view reflect its data.
 * <p>
 * <strong>Key characteristics</strong>
 * <ul>
 *   <li>Fixed size: 640 × 420 px (non-resizable)</li>
 *   <li>Owned &amp; blocking: {@link Modality#WINDOW_MODAL}</li>
 *   <li>Typically shown via {@link #showAndWait()} to pause the caller until the
 *       user closes the dialog.</li>
 * </ul>
 *
 * @author Hao He
 */

public class RecipeDetailView extends Stage {

    private final RecipeInteractionController controller;

    public RecipeDetailView(Recipes recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe must not be null");
        }

        try {
            // 1. Load FXML and build the scene
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/recipe_detail.fxml"));
            Scene scene = new Scene(loader.load());

            // 2. Obtain controller and pass the recipe object to it
            controller = loader.getController();
            controller.setRecipe(recipe);

            // 3. Configure the stage
            setTitle("Recipe — " + recipe.getTitle());
            setResizable(false);               // fixed 768 × 850
            setMinWidth(768);
            setMinHeight(850);
            initModality(Modality.APPLICATION_MODAL); // block parent window
            setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load recipe_detail.fxml", e);
        }
    }

    public RecipeInteractionController getController() {
        return controller;
    }
}
