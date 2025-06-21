package model;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * Represents a RecipeIngredients entity.
 * Each RecipeIngredients entry contains information such as recipe ID, ingredient ID, amount, unit.
 * Maps to the 'recipe_ingredients' table in the database.
 * 
 * @author: Pan Zitao
 */

public class RecipeIngredients implements Serializable {
    private int recipeId;
    private int ingredientId;
    private BigDecimal amount;
    private String unit;

    /**
     * Default constructor for the RecipeIngredients class.
    */
    public RecipeIngredients() {

    }

    /**
     * Constructs a RecipeIngredients object with all fields.
     * @param recipeId The ID of the recipe this ingredient belongs to.
     * @param ingredientId The ID of the ingredient.
     * @param amount The amount of the ingredient.
     * @param unit The unit of measurement for the ingredient.
     * */
    public RecipeIngredients(int recipeId, int ingredientId, BigDecimal amount, String unit) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.amount = amount;
        this.unit = unit;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) { 
        this.recipeId = recipeId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "RecipeIngredients{" +
                "recipeId=" + recipeId +
                ", ingredientId=" + ingredientId +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                '}';
    }   
}