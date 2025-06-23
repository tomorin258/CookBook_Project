package view;

import controller.RecipeManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Recipes;

public class RecipeCreateView extends Stage {

    private final RecipeManagementController controller;

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
        initModality(Modality.APPLICATION_MODAL); // Modal — blocks interaction with the parent window
        setScene(scene);
    }

    public RecipeManagementController getController() {
        return controller;
    }
}
