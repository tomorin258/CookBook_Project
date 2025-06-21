package dao.mappers;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

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
    boolean addRecipes(@Param("recipes") Recipes recipes);

    /**
     * Delete a recipes entry.
     *
     * @param recipesId the recipes id
     * @return the boolean
     */
    boolean deleteRecipes(@Param("recipesId") Integer recipesId);

    /**
     * Update a recipes entry.
     *
     * @param recipes the recipes object
     * @return the boolean
     */
    boolean updateRecipes(@Param("recipes") Recipes recipes);

    /**
     * Get recipes by id.
     *
     * @param recipesId the recipes id
     * @return the recipes
     */
    Recipes getRecipesById(@Param("recipesId") int recipesId);

    /**
     * Get recipes by title.
     *
     * @param title the recipes title
     * @return the list of recipes
     */
    ArrayList<Recipes> getRecipesByTitle(@Param("title") String title);

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
}