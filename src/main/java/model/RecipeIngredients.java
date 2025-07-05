package model;

import java.io.Serializable;
import java.math.BigDecimal;

import javafx.beans.property.SimpleStringProperty;
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
    private final SimpleStringProperty amount = new SimpleStringProperty(""); // 用字符串存储数量，便于绑定
    private final SimpleStringProperty unit = new SimpleStringProperty("");
    private final SimpleStringProperty ingredientName = new SimpleStringProperty("");
    private final SimpleStringProperty description = new SimpleStringProperty(""); // 新增字段：描述

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
        this.amount.set(String.valueOf(amount));
        this.unit.set(unit);
    }

    /**
     * Copy constructor for creating a deep copy of a RecipeIngredients object.
     * @param other The object to copy.
     */
    public RecipeIngredients(RecipeIngredients other) {
        this.recipeId = other.getRecipeId();
        this.ingredientId = other.getIngredientId();
        this.setAmount(other.getAmount());
        this.setUnit(other.getUnit());
        this.setIngredientName(other.getIngredientName());
        this.setDescription(other.getDescription());
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

    public String getAmount() {
        return amount.get();
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public String getUnit() {
        return unit.get();
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public SimpleStringProperty unitProperty() {
        return unit;
    }

    public String getIngredientName() {
        return ingredientName.get();
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName.set(ingredientName);
    }

    public SimpleStringProperty ingredientNameProperty() {
        return ingredientName;
    }

    public String getDescription() { return description.get(); } 

    public void setDescription(String desc) { description.set(desc); } 

    public SimpleStringProperty descriptionProperty() { return description; } 

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