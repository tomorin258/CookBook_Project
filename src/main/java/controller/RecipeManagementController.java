package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Recipes;
import dao.mappers.RecipesMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeManagementController {

    @FXML private TextField keywordField;
    @FXML private ListView<Recipes> recipeListView;

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

    //get keyword for search
    @FXML
    private void onSearch() {
        String keyword = keywordField.getText();
        List<Recipes> result = searchRecipes(keyword);
        recipeListView.getItems().setAll(result);
    }
}