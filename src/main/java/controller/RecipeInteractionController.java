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
                // 如果amount不是数字，保持原样
            }
        }
        // 不要 setItems，不要 setAll
        // ingredientsTable.refresh(); // 如需刷新显示
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
            showAlert("评论不能为空！");
            return;
        }
        Comments comment = new Comments();
        comment.setRecipeId(currentRecipe.getId());
        // 你需要获取当前登录用户的 userId
        comment.setUserId(getCurrentUserId()); // 实现此方法或替换为实际用户id
        comment.setContent(commentText);

        boolean success = commentService.addComment(comment);
        if (success) {
            commentField.clear();
            loadComments();
        } else {
            showAlert("评论添加失败，请重试！");
        }
    }

    // 加载评论列表
    private void loadComments() {
        if (currentRecipe == null) return;
        List<Comments> comments = commentService.getCommentsByRecipeId(currentRecipe.getId());
        commentListView.getItems().clear();
        for (Comments c : comments) {
            // 你可以根据 userId 查询用户名，这里假设只显示内容
            commentListView.getItems().add("用户" + c.getUserId() + ": " + c.getContent());
        }
    }

    // 辅助弹窗方法
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // 示例：获取当前登录用户id（你需要根据实际登录逻辑实现）
    private int getCurrentUserId() {
        // TODO: 返回当前登录用户的id
        return 1;
    }

    /**
     * 设置当前要显示的菜谱，并刷新界面内容
     */
    public void setRecipe(Recipes recipe) {
        this.currentRecipe = recipe;
        if (recipe != null) {
            baseServings = recipe.getServings();
            likeCountLabel.setText(String.valueOf(recipe.getLikeCount()));
            serveSpinner.getValueFactory().setValue(baseServings);
            updateIngredientsTable(baseServings);
            loadComments();
            // 你可以根据需要补充其它控件的赋值，如标题、图片等
        }
    }
}