package test;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import config.MyBatisUtil;
import dao.mappers.RecipesMapper;
import dao.mappers.IngredientsMapper;
import model.Recipes;
import model.Ingredients;

public class DAOmapperTest {

    @Test
    public void testGetRecipeById() {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RecipesMapper recipesMapper = session.getMapper(RecipesMapper.class);
            // Using a sample recipe id (adjust as needed)
            Recipes recipe = recipesMapper.getRecipeById(1);
            assertNotNull(recipe, "Recipe should not be null for recipeId 1");
            System.out.println("Retrieved Recipe: " + recipe.getTitle());
        }
    }

    @Test
    public void testGetIngredientById() {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            IngredientsMapper ingredientsMapper = session.getMapper(IngredientsMapper.class);
            // Using a sample ingredient id (adjust as needed)
            Ingredients ingredient = ingredientsMapper.getIngredientById(1);
            assertNotNull(ingredient, "Ingredient should not be null for ingredientId 1");
            System.out.println("Retrieved Ingredient: " + ingredient.getName());
        }
    }
}
