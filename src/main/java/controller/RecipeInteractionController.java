package controller;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Comments;
import model.RecipeIngredients;
import model.Recipes;
import service.CommentService;
import service.RecipeIngredientsService;
import service.RecipeService;

public class RecipeInteractionController {

    @FXML private Spinner<Integer> serveSpinner;
    @FXML private Label likeCountLabel;
    @FXML private Button likeBtn;
    @FXML private TableView<RecipeIngredients> ingredientsTable;
    @FXML private TextArea commentArea;
    @FXML private Button commentBtn;
    @FXML private TextField commentField;
    @FXML private Button addCommentBtn;
    @FXML private ListView<String> commentListView;

    private Recipes currentRecipe;
    private int baseServings = 1;

    private final RecipeService recipeService = new RecipeService();
    private final CommentService commentService = new CommentService();
    private final RecipeIngredientsService recipeIngredientsService = new RecipeIngredientsService();

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
        recipeService.updateRecipe(currentRecipe);
        likeCountLabel.setText(String.valueOf(likes));
    }

    private void updateIngredientsTable(int newServings) {
        ObservableList<RecipeIngredients> items = ingredientsTable.getItems();
        double ratio = (double) newServings / baseServings;
        for (RecipeIngredients ri : items) {
            try {
                java.math.BigDecimal oldAmount = new java.math.BigDecimal(ri.getAmount());
                java.math.BigDecimal newAmount = oldAmount.multiply(java.math.BigDecimal.valueOf(ratio));
                ri.setAmount(newAmount.stripTrailingZeros().toPlainString());
            } catch (Exception e) {

            }
        }
    }

    @FXML
    private void onComment(ActionEvent event) {
        String content = commentArea.getText();
        if (content == null || content.trim().isEmpty()) return;
        Comments comment = new Comments();
        comment.setRecipeId(currentRecipe.getId());
        comment.setContent(content);
        comment.setCreatedAt(java.time.LocalDateTime.now().toString());
        commentService.addComment(comment);
        commentArea.clear();
        loadComments();
    }

    @FXML
    private void onAddComment() {
        String commentText = commentField.getText();
        if (commentText == null || commentText.trim().isEmpty()) {
            showAlert("Comments shound not be empty!");
            return;
        }
        Comments comment = new Comments();
        comment.setRecipeId(currentRecipe.getId());
        comment.setUserId(getCurrentUserId());
        comment.setContent(commentText);

        boolean success = commentService.addComment(comment);
        if (success) {
            commentField.clear();
            loadComments();
        } else {
            showAlert("Fail to load comments, please try again!");
        }
    }

    private void loadComments() {
        if (currentRecipe == null) return;
        List<Comments> comments = commentService.getCommentsByRecipeId(currentRecipe.getId());
        commentListView.getItems().clear();
        for (Comments c : comments) {
            commentListView.getItems().add("User" + c.getUserId() + ": " + c.getContent());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tipps");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private int getCurrentUserId() {
        // TODO: 返回当前登录用户的id
        return 1;
    }

    public void setRecipe(Recipes recipe) {
        this.currentRecipe = recipe;
        if (recipe != null) {
            baseServings = recipe.getServings();
            likeCountLabel.setText(String.valueOf(recipe.getLikeCount()));
            serveSpinner.getValueFactory().setValue(baseServings);
            updateIngredientsTable(baseServings);
            loadComments();
        }
    }
}