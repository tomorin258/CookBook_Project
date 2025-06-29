package dao.mappers;
import java.util.ArrayList;

import model.Recipes;

/**
 * The interface RecipesMapper.
 * Provides CRUD operations for Recipes table.
 * 
 * @author: Pan Zitao
 */
public interface RecipesMapper {

    /**
     * Add a recipes entry.
     *
     * @param recipes the recipes object
     * @return the boolean
     */
    boolean addRecipes(Recipes recipes);

    /**
     * Delete a recipes entry.
     *
     * @param recipesId the recipes id
     * @return the boolean
     */
    boolean deleteRecipes(Integer id);

    /**
     * Update a recipes entry.
     *
     * @param recipes the recipes object
     * @return the boolean
     */
    boolean updateRecipes(Recipes recipes);

    /**
     * Gets recipe by id.
     *
     * @param recipeId the recipe id
     * @return the recipe by id
     */
    Recipes getRecipeById(int recipeId);

    /**
     * Get recipes by title.
     *
     * @param title the recipes title
     * @return the list of recipes
     */
    ArrayList<Recipes> getRecipesByTitle(String title);

    /**
     * Get the newest recipes.
     *
     * @return the newest recipes
     */
    Recipes getNewestRecipes();

    /**
     * Get all recipes.
     *
     * @return the list of all recipes
     */
    ArrayList<Recipes> getAllRecipes();

    /**
     * Get recipes sorted by likes count in descending order.
     *
     * @return the list of recipes sorted by likes
     */
    ArrayList<Recipes> getRecipesSortedByLikes();
}