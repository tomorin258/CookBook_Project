package service;

import dao.mappers.CommentsMapper;
import model.Comments;
import config.MyBatisUtil;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

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