package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
    @FXML private Label cookTimeLabel;
    @FXML private TextArea instructionsArea;
    @FXML private javafx.scene.image.ImageView recipeImageView; // 新增的 ImageView

    private Recipes currentRecipe;
    private int baseServings = 1;

    private final RecipeService recipeService = new RecipeService();
    private final CommentService commentService = new CommentService();
    private final RecipeIngredientsService recipeIngredientsService = new RecipeIngredientsService();

    @FXML
    public void initialize() {
        // 确保 Spinner 有 ValueFactory
        if (serveSpinner.getValueFactory() == null) {
            serveSpinner.setValueFactory(new javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        }

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
        // 不做任何UI操作，如果需要可以留空或仅做日志
        // List<Comments> comments = commentService.getCommentsByRecipeId(currentRecipe.getId());
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
            cookTimeLabel.setText("Cooking Time: " + recipe.getCookTime() + " min");
            instructionsArea.setText(recipe.getInstructions());
            // 配料
            java.util.List<RecipeIngredients> ingredientList = recipeIngredientsService.getByRecipeId(recipe.getId());
            ingredientsTable.setItems(javafx.collections.FXCollections.observableArrayList(ingredientList));
            // 图片
            if (recipeImageView != null && recipe.getImageUrl() != null) {
                try {
                    java.net.URL imgUrl = getClass().getResource("/" + recipe.getImageUrl());
                    if (imgUrl != null) {
                        recipeImageView.setImage(new javafx.scene.image.Image(imgUrl.toString()));
                    } else {
                        recipeImageView.setImage(null);
                    }
                } catch (Exception e) {
                    recipeImageView.setImage(null);
                }
            }
            updateIngredientsTable(baseServings);
            loadComments();
        }
    }

    @FXML
    private void onBack(javafx.event.ActionEvent event) {
        // 关闭窗口或返回上一页
        ((javafx.stage.Stage)(((javafx.scene.Node)event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void onEdit(javafx.event.ActionEvent event) {
        // 打开编辑窗口，传递当前菜谱对象
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/recipe_edit_add.fxml"));
            javafx.scene.Parent root = loader.load();
            // 获取编辑窗口的控制器
            RecipeEditAddController editController = loader.getController();
            // 传递当前菜谱数据
            editController.loadRecipe(currentRecipe);
            // 显示编辑窗口（模态）
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("编辑菜谱");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
            // 编辑完成后可刷新本页面内容
            setRecipe(recipeService.getRecipesById(currentRecipe.getId()));
        } catch (Exception e) {
            showAlert("无法打开编辑窗口: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onDelete(javafx.event.ActionEvent event) {
        // 弹窗确认
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除该菜谱吗？", ButtonType.YES, ButtonType.NO);
        alert.setTitle("确认删除");
        alert.setHeaderText(null);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            boolean success = recipeService.deleteRecipe(currentRecipe.getId());
            if (success) {
                showAlert("删除成功！");
                // 关闭当前窗口
                ((javafx.stage.Stage)(((javafx.scene.Node)event.getSource()).getScene().getWindow())).close();
            } else {
                showAlert("删除失败，请重试！");
            }
        }
    }
}