package controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import dao.mappers.RecipesMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Recipes;

public class RecipeManagementController {

    @FXML private TextField keywordField;
    @FXML private ListView<Recipes> recipeListView;
    @FXML private VBox sortedListVBox;
    @FXML private Button prevPageBtn;
    @FXML private Button nextPageBtn;
    @FXML private Label pageInfoLabel;
    @FXML private Button backBtn;

    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPage = 1;
    private List<Recipes> sortedRecipes;
    private RecipesMapper recipesMapper;

    //add recipes
    /**
     * Adds a new recipe to the database.
     * @param recipe The recipe to add.
     * @return true if the addition was successful, false otherwise.
     */
    public boolean addRecipe(Recipes recipe) {
        return recipesMapper.addRecipes(recipe);
    }
    
    //delete recipes
    /**
     * Deletes a recipe by its ID.
     * @param recipeId The ID of the recipe to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteRecipe(int recipeId) {
        return recipesMapper.deleteRecipes(recipeId);
    }

    //edit recipes
    /**
     * Edits an existing recipe.
     * @param recipe The recipe with updated information.
     * @return true if the edit was successful, false otherwise.
     */
    public boolean editRecipe(Recipes recipe) {
        return recipesMapper.updateRecipes(recipe);
    }

    //get recipes by keyword
    /**
     * Searches for recipes by a keyword in their title.
     * If the keyword is null or empty, returns all recipes.
     * @param keyword The keyword to search for.
     * @return A list of recipes that match the keyword.
     */
    public List<Recipes> searchRecipes(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return recipesMapper.getAllRecipes();
        }
        return recipesMapper.getRecipesByTitle(keyword);
    }

    //get keyword for search
    @FXML
    private void onSearch() {
        String keyword = keywordField.getText();
        List<Recipes> result = searchRecipes(keyword);
        recipeListView.getItems().setAll(result);
    }

    //order recipes by likes
    /**
     * Retrieves all recipes sorted by the number of likes in descending order.
     * @return A list of recipes sorted by likes.
     */
    public List<Recipes> getRecipesSortedByLikes() {
        ArrayList<Recipes> all = recipesMapper.getAllRecipes();
        return all.stream()
                .sorted(Comparator.comparingInt(Recipes::getLikeCount).reversed())
                .collect(Collectors.toList());
    }

    //initialize pagination controls
    /**
     * Initializes the pagination controls and displays the first page of sorted recipes.
     * This method should be called after the sortedListVBox is set up.
     */
    @FXML
    public void initialize() {
        // if sortedListVBox exists， initialize pagination controls
        if (sortedListVBox != null) {
            sortedRecipes = getRecipesSortedByLikes();
            totalPage = (int) Math.ceil((double) sortedRecipes.size() / pageSize);
            showSortedPage(currentPage);
        }
    }


    //show sorted recipes
    @FXML
    private void showSortedPage(int page) {
        sortedListVBox.getChildren().clear();
        if (sortedRecipes == null || sortedRecipes.isEmpty()) {
            pageInfoLabel.setText("No recipes found.");
            return;
        }
        int from = (page - 1) * pageSize;
        int to = Math.min(from + pageSize, sortedRecipes.size());
        List<Recipes> pageList = sortedRecipes.subList(from, to);
        for (Recipes recipe : pageList) {
            Label label = new Label(recipe.getTitle() + "  👍" + recipe.getLikeCount());
            label.setStyle("-fx-font-size:18;-fx-padding:8 0;");
            sortedListVBox.getChildren().add(label);
        }
        pageInfoLabel.setText("Page " + page + " / " + totalPage);
        prevPageBtn.setDisable(page == 1);
        nextPageBtn.setDisable(page == totalPage);
    }

    

    
}