package service;

import dao.mappers.RecipesMapper;
import model.Recipes;
import resources.MyBatisUtil;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class RecipeService {
    public boolean addRecipe(Recipes recipe) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            return mapper.addRecipes(recipe);
        }
    }

    public boolean deleteRecipe(int recipeId) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            return mapper.deleteRecipes(recipeId);
        }
    }

    public List<Recipes> searchRecipes(String keyword) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            if (keyword == null || keyword.trim().isEmpty()) {
                return mapper.getAllRecipes();
            }
            return mapper.getRecipesByTitle(keyword);
        }
    }

    public boolean editRecipe(Recipes recipe) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            return mapper.updateRecipes(recipe);
        }
    }

        public boolean updateRecipe(Recipes recipe) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            return mapper.updateRecipes(recipe);
        }
    }
}
