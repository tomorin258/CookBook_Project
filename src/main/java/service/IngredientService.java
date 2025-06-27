package service;

import java.util.ArrayList; // 确保引入了 ArrayList

import org.apache.ibatis.session.SqlSession;

import config.MyBatisUtil;
import dao.mappers.IngredientsMapper;
import model.Ingredients;

public class IngredientService {

    /**
     * 根据配料名称查找id，找不到返回null
     */
    public Integer getIngredientIdByName(String name) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            IngredientsMapper mapper = session.getMapper(IngredientsMapper.class);
            
            // **** 在这里加入调试代码 ****
            System.out.println("==> [IngredientService] Searching for ingredient by name: '" + name + "'");
            ArrayList<Ingredients> list = mapper.getIngredientsByName(name);
            System.out.println("==> [IngredientService] MyBatis returned a list of size: " + (list == null ? "null" : list.size()));
            // **** 调试代码结束 ****

            if (list != null && !list.isEmpty()) {
                System.out.println("==> [IngredientService] Found ingredient with ID: " + list.get(0).getId());
                return list.get(0).getId();
            }
            
            System.out.println("==> [IngredientService] Ingredient not found, returning null.");
            return null;
        }
    }

    /**
     * 新增配料，返回新id (优化后)
     */
    public Integer addIngredient(String name) {
        // 改为使用 MyBatisUtil 并开启自动提交
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            IngredientsMapper mapper = session.getMapper(IngredientsMapper.class);
            Ingredients ingredient = new Ingredients();
            ingredient.setName(name);
            
            // 执行插入
            mapper.addIngredients(ingredient);
            
            // MyBatis 在配置正确的情况下，会自动将生成的主键ID回填到传入的 ingredient 对象中
            // 直接返回回填的ID即可，这比 getNewestIngredients 更安全、更准确
            return ingredient.getId();
        }
    }
}
