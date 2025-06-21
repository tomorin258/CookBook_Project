package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import model.Recipes;
import model.Comments;
import model.RecipeIngredients;
import dao.mappers.RecipesMapper;
import dao.mappers.CommentsMapper;
import dao.mappers.RecipeIngredientsMapper;

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
    private RecipesMapper recipesMapper;
    private CommentsMapper commentsMapper;
    private RecipeIngredientsMapper recipeIngredientsMapper;

    private int baseServings = 1;

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
        recipesMapper.updateRecipes(currentRecipe);
        likeCountLabel.setText(String.valueOf(likes));
    }

    private void updateIngredientsTable(int newServings) {
        List<RecipeIngredients> ingredients = recipeIngredientsMapper.getRecipeIngredientsByRecipeId(currentRecipe.getId());
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
        commentsMapper.addComments(comment);
        commentArea.clear();
        loadComments();
    }

    private void loadComments() {
        List<Comments> comments = commentsMapper.getCommentsByRecipeId(currentRecipe.getId());
        commentListView.getItems().clear();
        for (Comments c : comments) {
            commentListView.getItems().add(c.getContent());
        }
    }
}