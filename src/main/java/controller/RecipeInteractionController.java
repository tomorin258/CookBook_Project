package controller;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    @FXML private javafx.scene.image.ImageView recipeImageView;

    @FXML
    private TableColumn<RecipeIngredients, String> ingredientNameCol;
    @FXML
    private TableColumn<RecipeIngredients, String> quantityCol;
    @FXML
    private TableColumn<RecipeIngredients, String> unitsCol;
    @FXML
    private TableColumn<RecipeIngredients, String> ingredientDescCol;

    private Recipes currentRecipe;
    private int baseServings = 1;

    private final RecipeService recipeService = new RecipeService();
    private final CommentService commentService = new CommentService();
    private final RecipeIngredientsService recipeIngredientsService = new RecipeIngredientsService();

    @FXML
    public void initialize() {
        System.out.println("111");
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

        // **** 在这里加入或替换为下面的绑定代码 ****
        // 这会告诉每一列如何从 RecipeIngredients 对象中获取数据
        ingredientNameCol.setCellValueFactory(cellData -> cellData.getValue().ingredientNameProperty());
        quantityCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        unitsCol.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        ingredientDescCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
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
        // 不做任何UI操作，如果需要可以留空或仅做日志
        // List<Comments> comments = commentService.getCommentsByRecipeId(currentRecipe.getId());
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Achtung");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private int getCurrentUserId() {
        return 1;
    }

    public void setRecipe(Recipes recipe) {
        this.currentRecipe = recipe;
        if (recipe != null) {
            baseServings = recipe.getServings();
            likeCountLabel.setText(String.valueOf(recipe.getLikeCount()));
            serveSpinner.getValueFactory().setValue(baseServings);
            cookTimeLabel.setText("Cooking Time: " + recipe.getCookTime() + " min");
            instructionsArea.setText(recipe.getInstructions());
            
            // **** 修改你的调试代码 ****
            System.out.println("--- 开始调试Detail界面配料 ---");
            System.out.println("正在为 Recipe ID: " + recipe.getId() + " 查询配料..."); // <-- 增加这一行
            java.util.List<RecipeIngredients> ingredientList = recipeIngredientsService.getByRecipeId(recipe.getId());
            System.out.println("获取到的配料数量: " + ingredientList.size());
            for (RecipeIngredients ri : ingredientList) {
                System.out.println("配料ID: " + ri.getIngredientId() + ", 配料名: '" + ri.getIngredientName() + "'");
            }
            System.out.println("--- 调试结束 ---");
            // **** 调试代码结束 ****

            ingredientsTable.setItems(javafx.collections.FXCollections.observableArrayList(ingredientList));
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
        ((javafx.stage.Stage)(((javafx.scene.Node)event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void onEdit() {
        try {
            // 1. 创建 FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/recipe_edit_add.fxml"));
            
            // 2. 加载 FXML 并获取根节点
            Parent root = loader.load();
            
            // 3. 【关键】获取新窗口的控制器实例
            RecipeEditAddController controller = loader.getController();
            
            // 4. 【关键】调用 loadRecipe 方法，将当前菜谱数据传递过去
            controller.loadRecipe(this.currentRecipe);
            
            // 5. 创建并显示新窗口
            Stage stage = new Stage();
            stage.setTitle("Edit Recipe");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // 阻塞父窗口
            stage.showAndWait(); // 等待编辑窗口关闭

            // 可选：编辑完成后刷新详情页数据
            // refreshRecipeDetails(); 

        } catch (IOException e) {
            e.printStackTrace();
            // 显示一个错误弹窗
            new Alert(Alert.AlertType.ERROR, "Could not open the edit window.").showAndWait();
        }
    }

    @FXML
    private void onDelete(javafx.event.ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to DELETE the recipe?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Delete Confirm");
        alert.setHeaderText(null);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            boolean success = recipeService.deleteRecipe(currentRecipe.getId());
            if (success) {
                showAlert("Recipe deleted successfully!");
                ((javafx.stage.Stage)(((javafx.scene.Node)event.getSource()).getScene().getWindow())).close();
            } else {
                showAlert("Failed to delete the recipe, please try again!");
            }
        }
    }
}