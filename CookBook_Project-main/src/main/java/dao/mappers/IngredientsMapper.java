package dao.mappers;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import model.Ingredients;

/**
 * The interface IngredientsMapper.
 * Provides CRUD operations for ingredients table.
 * 
 * @author: Pan Zitao
 */

public interface IngredientsMapper {

    /**
     * Add an ingredient entry.
     *
     * @param ingredients the ingredients object
     * @return the boolean
     */
    boolean addIngredients(@Param("ingredients") Ingredients ingredients);

    /**
     * Delete an ingredient entry.
     * @param ingredientsId the ingredients id
     * @return the boolean
     */
    boolean deleteIngredients(@Param("ingredientsId") Integer ingredientsId);

    /**
     * Update an ingredient entry.
     * @param ingredients the ingredients object
     * @return the boolean
     */
    boolean updateIngredients(@Param("ingredients") Ingredients ingredients);

    /**
     * Get ingredient by id.
     * @param ingredientsId the ingredients id
     * @return the ingredients
     * */
    Ingredients getIngredientsById(@Param("ingredientsId") int ingredientsId);

    /**
     * Get ingredient by name.
     * @param name the ingredients name
     * @return the list of ingredients
     */
    ArrayList<Ingredients> getIngredientsByName(@Param("name") String name);

    /**
     * Get the newest ingredient.
     * @return the newest ingredient
     * */
    Ingredients getNewestIngredients();

    /**
     * Get all ingredients.
     * @return the list of all ingredients
     * */
    ArrayList<Ingredients> getAllIngredients();
}
