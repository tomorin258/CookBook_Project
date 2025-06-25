package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import model.Recipes;
import model.Comments;
import model.RecipeIngredients;
<<<<<<< HEAD
import service.RecipeService;
import service.CommentService;
import service.RecipeIngredientsService;
=======
import dao.mappers.RecipesMapper;
import dao.mappers.CommentsMapper;
import dao.mappers.RecipeIngredientsMapper;
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f

import java.util.List;

public class RecipeInteractionController {

    @FXML private Spinner<Integer> serveSpinner;
    @FXML private Label likeCountLabel;
    @FXML private Button likeBtn;
    @FXML private TableView<RecipeIngredients> ingredientsTable;
    @FXML private TextArea commentArea;
    @FXML private Button commentBtn;
    @FXML private ListView<String> commentListView;

    private Recipes currentRecipe;
<<<<<<< HEAD
    private int baseServings = 1;

    // 只依赖 Service
    private final RecipeService recipeService = new RecipeService();
    private final CommentService commentService = new CommentService();
    private final RecipeIngredientsService recipeIngredientsService = new RecipeIngredientsService();
=======
    private RecipesMapper recipesMapper;
    private CommentsMapper commentsMapper;
    private RecipeIngredientsMapper recipeIngredientsMapper;

    private int baseServings = 1;
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f

    @FXML
    public void initialize() {
        if (currentRecipe != null) {
            baseServings = currentRecipe.getServings();
            likeCountLabel.setText(String.valueOf(currentRecipe.getLikeCount()));
            serveSpinner.getValueFactory().setValue(baseServings);
            updateIngredientsTable(baseServings);
            loadComments();
        }

        serveSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateIngredientsTable(newVal);
        });
    }

    @FXML
    private void onLike(ActionEvent event) {
        int likes = currentRecipe.getLikeCount() + 1;
        currentRecipe.setLikeCount(likes);
<<<<<<< HEAD
        recipeService.updateRecipe(currentRecipe);
=======
        recipesMapper.updateRecipes(currentRecipe);
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
        likeCountLabel.setText(String.valueOf(likes));
    }

    private void updateIngredientsTable(int newServings) {
<<<<<<< HEAD
        List<RecipeIngredients> ingredients = recipeIngredientsService.getByRecipeId(currentRecipe.getId());
=======
        List<RecipeIngredients> ingredients = recipeIngredientsMapper.getRecipeIngredientsByRecipeId(currentRecipe.getId());
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
        double ratio = (double) newServings / baseServings;
        for (RecipeIngredients ri : ingredients) {
            ri.setAmount(ri.getAmount().multiply(java.math.BigDecimal.valueOf(ratio)));
        }
        ingredientsTable.getItems().setAll(ingredients);
    }

    @FXML
    private void onComment(ActionEvent event) {
        String content = commentArea.getText();
        if (content == null || content.trim().isEmpty()) return;
        Comments comment = new Comments();
        comment.setRecipeId(currentRecipe.getId());
        comment.setContent(content);
        comment.setCreatedAt(java.time.LocalDateTime.now().toString());
<<<<<<< HEAD
        commentService.addComment(comment);
=======
        commentsMapper.addComments(comment);
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
        commentArea.clear();
        loadComments();
    }

    private void loadComments() {
<<<<<<< HEAD
        List<Comments> comments = commentService.getCommentsByRecipeId(currentRecipe.getId());
=======
        List<Comments> comments = commentsMapper.getCommentsByRecipeId(currentRecipe.getId());
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
        commentListView.getItems().clear();
        for (Comments c : comments) {
            commentListView.getItems().add(c.getContent());
        }
    }
}