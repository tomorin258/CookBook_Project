package model;

import java.io.Serializable;

public class Recipes implements Serializable {
    private int id;
    private int userId;
    private String title;
    private String description;
    private String instructions;
    private int cookTime;
    private int servings;
    private int likeCount;

    public Recipes() {}

    public Recipes(int id, int userId, String title, String description, String instructions, int cookTime, int servings, int likeCount) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.cookTime = cookTime;
        this.servings = servings;
        this.likeCount = likeCount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public int getCookTime() { return cookTime; }
    public void setCookTime(int cookTime) { this.cookTime = cookTime; }

    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = servings; }

    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    @Override
    public String toString() {
        return "Recipes{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", instructions='" + instructions + '\'' +
                ", cookTime=" + cookTime +
                ", servings=" + servings +
                ", likeCount=" + likeCount +
                '}';
    }
}