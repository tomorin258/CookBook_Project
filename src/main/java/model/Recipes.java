package model;

import java.io.Serializable;
/**
 * Represents a recipes entity.
 * Each recipes entry contains information such as user ID, title, description, instructions, cook time, servings, and like count.
 * Maps to the 'recipe' table in the database.
 * 
 * @author: Pan Zitao
 */
public class Recipes implements Serializable {

    private int id;
    private int userId;
    private String title;
    private String instructions;
    private int cookTime;
    private int servings;
    private int likeCount;
    private String imageUrl;
    private Integer likesCount; // 假设你有点赞数字段

    /**
     * Default constructor for the Recipes class.
     */
    public Recipes() {

    }

    /**
     * Constructs a Recipes object with all fields.
     *
     * @param id            The ID of the recipe (primary key).
     * @param userId        The ID of the user who created the recipe.
     * @param title         The title of the recipe.
     * @param instructions  The cooking instructions.
     * @param cookTime      The cooking time in minutes.
     * @param servings      The number of servings.
     * @param likeCount     The number of likes this recipe has received.
     * @param imageUrl      The URL of the recipe image (optional).
     */
    public Recipes(int id, int userId, String title, String instructions, int cookTime, int servings, int likeCount) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.instructions = instructions;
        this.cookTime = cookTime;
        this.servings = servings;
        this.likeCount = likeCount;
        this.imageUrl = null; // Default to null if no image URL is provided
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // 在这里添加 getLikesCount() 方法
    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    /**
     * Returns a string representation of the Recipes object.
     *
     * @return A string representation including all recipes fields.
     */
    @Override
    public String toString() {
        return "Recipes{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", instructions='" + instructions + '\'' +
                ", cookTime=" + cookTime +
                ", servings=" + servings +
                ", likeCount=" + likeCount +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
