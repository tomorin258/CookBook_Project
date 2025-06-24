package view;

import controller.RecipeInteractionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Recipes;

public class RecipeDetailView extends Stage {

    private final RecipeInteractionController controller;

    public RecipeDetailView(Recipes recipe) throws Exception {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe must not be null");
        }

        // 1. Load FXML and build the scene
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/recipe_detail.fxml"));
        Scene scene = new Scene(loader.load());

        // 2. Obtain controller and pass the recipe object to it
        controller = loader.getController();

        controller.setRecipe(recipe);

        // 3. Configure the stage
        setTitle("Recipe — " + recipe.getTitle());
        setResizable(false);                       // Fixed size 768 × 850
        initModality(Modality.APPLICATION_MODAL);  // Blocks interaction with parent
        setScene(scene);
    }

    public RecipeInteractionController getController() {
        return controller;
    }
}
