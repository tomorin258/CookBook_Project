package dao.mappers;

import java.io.Serializable;
/**
 * Represents a comments entity.
 * Each comments entry contains information such as id, recipe_id, user_id, content and created_at.
 * Maps to the 'comments' table in the database.
 * 
 * @author: Pan Zitao
 */
public class Comments implements Serializable {

    private int id;
    private int recipeId;
    private int userId;
    private String content;
    private String createdAt;

    /**
     * Default constructor for the Comments class.
     */
    public Comments() {

    }

    /**
     * Constructs a Comments object with all fields.
     * @param id The ID of the comment (primary key).
     * @param recipeId The ID of the recipe this comment belongs to.
     * @param userId The ID of the user who made the comment.
     * @param content The content of the comment.
     * @param createdAt The timestamp when the comment was created.
     * */
    public Comments(int id, int recipeId, int userId, String content, String createdAt) {
        this.id = id;
        this.recipeId = recipeId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;    
    }

    public void setRecipeId(int recipeId) { 
        this.recipeId = recipeId;
    }
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;   
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "id=" + id +
                ", recipeId=" + recipeId +
                ", userId=" + userId +  
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
