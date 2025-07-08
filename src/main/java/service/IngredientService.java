package service;

/**
 * Service class for handling ingredient-related business logic.
 * Provides methods to get ingredient ID by name and to add new ingredients.
 */

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import config.MyBatisUtil;
import dao.mappers.IngredientsMapper;
import model.Ingredients;

public class IngredientService {


    /**
     * Retrieves the ID of an ingredient by its name.
     *
     * @param name the name of the ingredient
     * @return the ID of the ingredient if found, otherwise null
     */
    public Integer getIngredientIdByName(String name) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            IngredientsMapper mapper = session.getMapper(IngredientsMapper.class);
            
            System.out.println("==> [IngredientService] Searching for ingredient by name: '" + name + "'");
            ArrayList<Ingredients> list = mapper.getIngredientsByName(name);
            System.out.println("==> [IngredientService] MyBatis returned a list of size: " + (list == null ? "null" : list.size()));

            if (list != null && !list.isEmpty()) {
                System.out.println("==> [IngredientService] Found ingredient with ID: " + list.get(0).getId());
                return list.get(0).getId();
            }
            
            System.out.println("==> [IngredientService] Ingredient not found, returning null.");
            return null;
        }
    }


    /**
     * Adds a new ingredient with the specified name.
     *
     * @param name the name of the new ingredient
     * @return the generated ID of the new ingredient
     */
    public Integer addIngredient(String name) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            IngredientsMapper mapper = session.getMapper(IngredientsMapper.class);
            Ingredients ingredient = new Ingredients();
            ingredient.setName(name);
            
            mapper.addIngredients(ingredient);
            
            return ingredient.getId();
        }
    }
}
