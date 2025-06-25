package service;

<<<<<<< HEAD
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.session.SqlSession;

import config.MyBatisUtil;
import dao.mappers.RecipesMapper;
import model.Recipes;
=======
import dao.mappers.RecipesMapper;
import model.Recipes;
import resources.MyBatisUtil;

import org.apache.ibatis.session.SqlSession;

import java.util.List;
>>>>>>> 9505fc0761a20a92127299d307ecc7014c97c7ca

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

<<<<<<< HEAD
    public boolean updateRecipe(Recipes recipe) {
=======
        public boolean updateRecipe(Recipes recipe) {
>>>>>>> 9505fc0761a20a92127299d307ecc7014c97c7ca
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipesMapper mapper = session.getMapper(RecipesMapper.class);
            return mapper.updateRecipes(recipe);
        }
    }
<<<<<<< HEAD

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
=======
>>>>>>> 9505fc0761a20a92127299d307ecc7014c97c7ca
}
