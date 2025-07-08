
/**
 * Service class for handling recipe-related business logic.
 * Provides methods for adding, deleting, searching, editing, and retrieving recipes.
 */
package service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import config.MyBatisUtil;
import dao.mappers.RecipesMapper;
import model.Recipes;

public class RecipeService {
    /**
     * Adds a new recipe.
     *
     * @param recipe the recipe to add
     * @return true if added successfully, false otherwise
     */
    public boolean addRecipe(Recipes recipe) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            return mapper.addRecipes(recipe);
        }
    }

    /**
     * Deletes a recipe by its ID.
     *
     * @param recipeId the ID of the recipe to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteRecipe(int recipeId) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            return mapper.deleteRecipes(recipeId);
        }
    }

    /**
     * Searches for recipes by keyword. If keyword is null or empty, returns all recipes.
     *
     * @param keyword the search keyword
     * @return a list of matching recipes
     */
    public List<Recipes> searchRecipes(String keyword) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            if (keyword == null || keyword.trim().isEmpty()) {
                return mapper.getAllRecipes();
            }
            return mapper.getRecipesByTitle(keyword);
        }
    }

    /**
     * Edits an existing recipe.
     *
     * @param recipe the recipe to edit
     * @return true if updated successfully, false otherwise
     */
    public boolean editRecipe(Recipes recipe) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            return mapper.updateRecipes(recipe);
        }
    }

    /**
     * Updates an existing recipe (alias for editRecipe).
     *
     * @param recipe the recipe to update
     * @return true if updated successfully, false otherwise
     */
    public boolean updateRecipe(Recipes recipe) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            return mapper.updateRecipes(recipe);
        }
    }

    /**
     * Retrieves all recipes sorted by like count.
     *
     * @return a list of recipes sorted by likes
     */
    public List<Recipes> getRecipesSortedByLikes() {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            return mapper.getRecipesSortedByLikes();
        }
    }

    /**
     * Retrieves a recipe by its ID.
     *
     * @param recipeId the ID of the recipe
     * @return the recipe object, or null if not found
     */
    public Recipes getRecipeById(int recipeId) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            return mapper.getRecipeById(recipeId);
        }
    }
}
