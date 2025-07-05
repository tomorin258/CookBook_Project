package service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import config.MyBatisUtil;
import dao.mappers.RecipeIngredientsMapper;
import model.RecipeIngredients;

public class RecipeIngredientsService {
    public boolean addRecipeIngredients(RecipeIngredients ri) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipeIngredientsMapper mapper = session.getMapper(RecipeIngredientsMapper.class);
            return mapper.addRecipeIngredients(ri);
        }
    }

    public boolean updateRecipeIngredients(RecipeIngredients ri) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            RecipeIngredientsMapper mapper = session.getMapper(RecipeIngredientsMapper.class);
            return mapper.updateRecipeIngredients(ri);
        }
    }

    public List<RecipeIngredients> getByRecipeId(int recipeId) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RecipeIngredientsMapper mapper = session.getMapper(RecipeIngredientsMapper.class);
            return mapper.getByRecipeId(recipeId);
        }
    }

    public void deleteByRecipeId(Integer recipeId) {
        try (org.apache.ibatis.session.SqlSession session = config.MyBatisUtil.getSqlSession(true)) {
            dao.mappers.RecipeIngredientsMapper mapper = session.getMapper(dao.mappers.RecipeIngredientsMapper.class);
            mapper.deleteByRecipeId(recipeId);
        }
    }
}