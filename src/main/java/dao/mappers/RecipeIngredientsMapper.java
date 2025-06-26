package dao.mappers;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import model.RecipeIngredients;

/**
 * The interface RecipeIngredientsMapper.
 * Provides CRUD operations for the recipe_ingredients table.
 * Maps to the 'recipe_ingredients' table in the database.
 *  
 *@author: Pan Zitao
 */
public interface RecipeIngredientsMapper {
    /**
     * Add a recipe ingredient entry.
     * @param recipeIngredients the recipe ingredients object
     * @return true if successful, false otherwise  
     * */
    boolean addRecipeIngredients(RecipeIngredients recipeIngredients);
    
    /**
     * Delete a recipe ingredient by its recipe ID and ingredient ID.
     * @param recipeId the recipe ID
     * @param ingredientId the ingredient ID
     * @return true if successful, false otherwise
     * */
    boolean deleteRecipeIngredients(Map<String, Object> params);

    /**
     * Update a recipe ingredient entry.
     * @param recipeIngredients the recipe ingredients object
     * @return true if successful, false otherwise
     * */
    boolean updateRecipeIngredients(RecipeIngredients recipeIngredients);

    /**
     * Get a recipe ingredient by its recipe ID and ingredient ID.
     * @param recipeId the recipe ID
     * @param ingredientId the ingredient ID
     * @return the recipe ingredients object
     * */
    RecipeIngredients getRecipeIngredientsById(@Param("recipeId") int recipeId, @Param("ingredientId") int ingredientId);

    /**
     * Get all ingredients for a specific recipe.
     * @param recipeId the recipe ID
     * @return a list of recipe ingredients
     * */
    List<RecipeIngredients> getRecipeIngredientsByRecipeId(@Param("recipeId") int recipeId);

    /**
     * Get all recipes that use a specific ingredient.
     * @param ingredientId the ingredient ID
     * @return a list of recipe ingredients
     * */
    List<RecipeIngredients> getRecipeIngredientsByIngredientId(@Param("ingredientId") int ingredientId);
}

