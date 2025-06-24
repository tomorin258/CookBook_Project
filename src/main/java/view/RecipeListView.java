package view;

import controller.RecipeManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RecipeListView extends Stage {

    private final RecipeManagementController controller;

    public RecipeListView() {
        this(null);
    }

    public RecipeListView(String initialKeyword) {
        try {
            // 1. Load FXML and build the scene
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/recipe_list.fxml"));
            Scene scene = new Scene(loader.load());

            // 2. Obtain controller
            controller = loader.getController();

            // 3. Configure stage
            setTitle("Recipe List");
            setMinWidth(768);
            setMinHeight(850);
            setScene(scene);

            // 4. If a keyword was provided, trigger an initial search
            if (initialKeyword != null && !initialKeyword.isBlank()) {
                controller.setKeyword(initialKeyword);
                controller.onSearch();                 // simulate Search button click
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load recipe_list.fxml", e);
        }
    }

    public RecipeManagementController getController() {
        return controller;
    }
}
