package model;

import java.io.Serializable;

public class Comments implements Serializable {
    private int id;
    private int recipeId;
    private int userId;
    private String content;
    private String createdAt;

    public Comments() {}

    public Comments(int id, int recipeId, int userId, String content, String createdAt) {
        this.id = id;
        this.recipeId = recipeId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", recipeId=" + recipeId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}