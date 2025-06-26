package service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.session.SqlSession;

import config.MyBatisUtil;
import dao.mappers.RecipesMapper;
import model.Recipes;

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

    public List<Recipes> getRecipesSortedByLikes() {
        List<Recipes> all = getAllRecipes(); // 或从数据库查
        return all.stream()
            .sorted(Comparator.comparingInt(Recipes::getLikeCount).reversed())
            .collect(Collectors.toList());
    }

    public List<Recipes> getAllRecipes() {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            return mapper.getAllRecipes();
        }
    }

    public Integer addRecipeAndReturnId(Recipes recipe) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            boolean success = mapper.addRecipes(recipe);
            if (success) {
                return recipe.getId(); // MyBatis会自动回填id
            }
            return null;
        }
    }

    public Recipes getRecipesById(int id) {
        try (org.apache.ibatis.session.SqlSession session = MyBatisUtil.getSqlSession()) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            return mapper.getRecipesById(id);
        }
    }
}
