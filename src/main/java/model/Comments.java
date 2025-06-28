package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a comments entity.
 * Each comments entry contains information such as id, recipe_id, user_id, content and created_at.
 * Maps to the 'comments' table in the database.
 * 
 * @author: Pan Zitao
 */
public class Comments implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer recipeId;
    private Integer userId;
    private String content;
    private Date createdAt;
    private Date updatedAt;

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
    public Comments(int id, int recipeId, int userId, String content, Date createdAt) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "id=" + id +
                ", recipeId=" + recipeId +
                ", userId=" + userId +  
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
