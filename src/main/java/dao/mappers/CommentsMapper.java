package dao.mappers;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import model.Comments;

/**
 * The interface CommentsMapper.
 * Provides CRUD operations for the comments table.
 * Maps to the 'comments' table in the database.
 * 
 * @author: Pan Zitao
 */
public interface CommentsMapper {

    /**
     * Add a comment entry.
     * @param comments the comments object
     * @return true if successful, false otherwise
     */
    boolean addComments(@Param("comments") Comments comments);

    /**
     * Delete a comment by its ID.
     * @param commentId the comment ID
     * @return true if successful, false otherwise
     */
    boolean deleteComments(@Param("commentId") Integer commentId);

    /**
     * Update a comment entry.
     * @param comments the comments object
     * @return true if successful, false otherwise
     */
    boolean updateComments(@Param("comments") Comments comments);

    /**
     * Get a comment by its ID.
     * @param commentId the comment ID
     * @return the comments object
     */
    Comments getCommentsById(@Param("commentId") int commentId);

    /**
     * Get all comments for a specific recipe.
     * @param recipeId the recipe ID
     * @return a list of comments
     */
    ArrayList<Comments> getCommentsByRecipeId(@Param("recipeId") int recipeId);

    /**
     * Get all comments made by a specific user.
     * @param userId the user ID
     * @return a list of comments
     */
    ArrayList<Comments> getCommentsByUserId(@Param("userId") int userId);

    /**
     * Get the newest comment (based on createdAt).
     * @return the newest comment
     */
    Comments getNewestComments();

    /**
     * Get all comments.
     * @return a list of all comments
     */
    ArrayList<Comments> getAllComments();
}
