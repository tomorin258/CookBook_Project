
/**
 * Service class for handling recipe-ingredient relationship business logic.
 * Provides methods for adding, updating, retrieving, and deleting recipe-ingredient associations.
 */
package service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import config.MyBatisUtil;
import dao.mappers.RecipeIngredientsMapper;
import model.RecipeIngredients;

public class RecipeIngredientsService {
    /**
     * Adds a new recipe-ingredient association.
     *
     * @param ri the recipe-ingredient association to add
     * @return true if added successfully, false otherwise
     */
    public boolean addRecipeIngredients(RecipeIngredients ri) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipeIngredientsMapper mapper = session.getMapper(RecipeIngredientsMapper.class);
            return mapper.addRecipeIngredients(ri);
        }
    }

    /**
     * Updates a recipe-ingredient association.
     *
     * @param ri the recipe-ingredient association to update
     * @return true if updated successfully, false otherwise
     */
    public boolean updateRecipeIngredients(RecipeIngredients ri) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipeIngredientsMapper mapper = session.getMapper(RecipeIngredientsMapper.class);
            return mapper.updateRecipeIngredients(ri);
        }
    }

    /**
     * Retrieves all recipe-ingredient associations for a given recipe ID.
     *
     * @param recipeId the recipe ID
     * @return a list of recipe-ingredient associations
     */
    public List<RecipeIngredients> getByRecipeId(int recipeId) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RecipeIngredientsMapper mapper = session.getMapper(RecipeIngredientsMapper.class);
            return mapper.getByRecipeId(recipeId);
        }
    }

    /**
     * Deletes all recipe-ingredient associations for a given recipe ID.
     *
     * @param recipeId the recipe ID
     */
    public void deleteByRecipeId(Integer recipeId) {
        try (org.apache.ibatis.session.SqlSession session = config.MyBatisUtil.getSqlSession(true)) {
            dao.mappers.RecipeIngredientsMapper mapper = session.getMapper(dao.mappers.RecipeIngredientsMapper.class);
            mapper.deleteByRecipeId(recipeId);
        }
    }
}