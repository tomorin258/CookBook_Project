package service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import config.MyBatisUtil;
import dao.mappers.CommentsMapper;
import model.Comments;

public class CommentService {
    public boolean addComment(Comments comment) {
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            CommentsMapper mapper = session.getMapper(CommentsMapper.class);
            return mapper.addComments(comment);
        }
    }

    public List<Comments> getCommentsByRecipeId(int recipeId) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            CommentsMapper mapper = session.getMapper(CommentsMapper.class);
            return mapper.getCommentsByRecipeId(recipeId);
        }
    }
}