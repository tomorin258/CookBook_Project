package service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import dao.mappers.IngredientsMapper;
import model.Ingredients;

public class IngredientService {
    private final SqlSessionFactory sqlSessionFactory;

    public IngredientService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * 根据配料名称查找id，找不到返回null
     */
    public Integer getIngredientIdByName(String name) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            IngredientsMapper mapper = session.getMapper(IngredientsMapper.class);
            ArrayList<Ingredients> list = mapper.getIngredientsByName(name);
            if (list != null && !list.isEmpty()) {
                return list.get(0).getId();
            }
            return null;
        }
    }

    /**
     * 新增配料，返回新id
     */
    public Integer addIngredient(String name) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            IngredientsMapper mapper = session.getMapper(IngredientsMapper.class);
            Ingredients ingredient = new Ingredients();
            ingredient.setName(name);
            boolean success = mapper.addIngredients(ingredient);
            session.commit();
            if (success) {
                Ingredients newest = mapper.getNewestIngredients();
                return newest != null ? newest.getId() : null;
            }
            return null;
        }
    }
}
