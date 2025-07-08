
/**
 * Service class for handling comment-related business logic.
 * Provides methods for adding comments and retrieving comments by recipe ID.
 */
package service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import config.MyBatisUtil;
import dao.mappers.CommentsMapper;
import model.Comments;

public class CommentService {
    /**
     * Adds a new comment.
     *
     * @param comment the comment to add
     * @return true if added successfully, false otherwise
     */
    public boolean addComment(Comments comment) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            CommentsMapper mapper = session.getMapper(CommentsMapper.class);
            return mapper.addComments(comment);
        }
    }

    /**
     * Retrieves all comments for a given recipe ID.
     *
     * @param recipeId the recipe ID
     * @return a list of comments for the recipe
     */
    public List<Comments> getCommentsByRecipeId(int recipeId) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            CommentsMapper mapper = session.getMapper(CommentsMapper.class);
            return mapper.getCommentsByRecipeId(recipeId);
        }
    }
}