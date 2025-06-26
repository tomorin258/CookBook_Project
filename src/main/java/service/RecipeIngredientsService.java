package service;

import dao.mappers.RecipeIngredientsMapper;
import model.RecipeIngredients;
import config.MyBatisUtil;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class RecipeIngredientsService {
    public List<RecipeIngredients> getByRecipeId(int recipeId) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            RecipeIngredientsMapper mapper = session.getMapper(RecipeIngredientsMapper.class);
            return mapper.getRecipeIngredientsByRecipeId(recipeId);
        }
    }
}