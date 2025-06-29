package controller;

import java.io.IOException;
import java.util.List; 

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory; 
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea; 
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import model.Comments;
import model.RecipeIngredients;
import model.Recipes;
import service.CommentService;
import service.RecipeIngredientsService;
import service.RecipeService;

public class RecipeInteractionController {

    /* ---------- FXML nodes ---------- */
    @FXML private Spinner<Integer> serveSpinner;
    @FXML private Label likeCountLabel;
    @FXML private Button likeBtn;
    @FXML private ListView<Comments> commentsListView; // <-- 添加 FXML 注入

    @FXML private TableView<RecipeIngredients> ingredientsTable;
    @FXML private TableColumn<RecipeIngredients, String> ingredientNameCol;
    @FXML private TableColumn<RecipeIngredients, String> quantityCol;
    @FXML private TableColumn<RecipeIngredients, String> unitsCol;
    @FXML private TableColumn<RecipeIngredients, String> ingredientDescCol;

    @FXML private Label cookTimeLabel;
    @FXML private TextArea instructionsArea;
    @FXML private ImageView recipeImageView;

    /* ---------- Comment area ---------- */
    @FXML private TextField commentField;           // 输入框
    @FXML private Button addCommentBtn;             // “Add” 按钮

    /* ---------- Services ---------- */
    private final RecipeService recipeService = new RecipeService();
    private final RecipeIngredientsService recipeIngredientsService = new RecipeIngredientsService();
    private final CommentService commentService = new CommentService(); // <-- 添加 Service 实例

    /* ---------- Runtime state ---------- */
    private Recipes currentRecipe;
    private int baseServings = 1;

    /* =====================================================================
     *  Initialization
     * =================================================================== */
    @FXML
    public void initialize() {
        if (serveSpinner.getValueFactory() == null) {
            serveSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        }

        /* 配料表列绑定（保持原逻辑） */
        ingredientNameCol.setCellValueFactory(c -> c.getValue().ingredientNameProperty());
        ingredientNameCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        ingredientNameCol.setOnEditCommit(e -> e.getRowValue().setIngredientName(e.getNewValue()));

        quantityCol.setCellValueFactory(c -> c.getValue().amountProperty());
        quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        quantityCol.setOnEditCommit(e -> e.getRowValue().setAmount(e.getNewValue()));

        unitsCol.setCellValueFactory(c -> c.getValue().unitProperty());
        unitsCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        unitsCol.setOnEditCommit(e -> e.getRowValue().setUnit(e.getNewValue()));

        ingredientDescCol.setCellValueFactory(c -> c.getValue().descriptionProperty());
        ingredientDescCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        ingredientDescCol.setOnEditCommit(e -> e.getRowValue().setDescription(e.getNewValue()));

        /* 份量调整监听 */
        serveSpinner.valueProperty().addListener((obs, o, n) -> updateIngredientsTable(n));

        // 2. 简化 CellFactory，只显示评论内容
        commentsListView.setCellFactory(param -> new ListCell<Comments>() {
            @Override
            protected void updateItem(Comments item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getContent());
                }
            }
        });
    }

    /* =====================================================================
     *  Like
     * =================================================================== */
    @FXML
    private void onLike(ActionEvent e) {
        int likes = currentRecipe.getLikeCount() + 1;
        currentRecipe.setLikeCount(likes);
        recipeService.updateRecipe(currentRecipe);
        likeCountLabel.setText(String.valueOf(likes));
    }

    /* =====================================================================
     *  Add Comment  (整合新 ListView)
     * =================================================================== */
    @FXML
    private void onAddComment() {
        String txt = commentField.getText() == null ? "" : commentField.getText().trim();
        if (txt.isEmpty()) {
            showAlert("Comments should not be empty!");
            return;
        }

        /* 1. 写入数据库 */
        Comments cmt = new Comments();
        cmt.setRecipeId(currentRecipe.getId());
        cmt.setUserId(getCurrentUserId());
        cmt.setContent(txt);
        // 在保存前设置当前时间，确保UI不会因为null而崩溃
        cmt.setCreatedAt(new java.util.Date()); 

        boolean ok = commentService.addComment(cmt);

        /* 2. 更新 UI */
        if (ok) {
            commentsListView.getItems().add(cmt); // 立即刷新
            commentField.clear();
        } else {
            showAlert("Fail to save comment, please try again!");
        }
    }

    /* =====================================================================
     *  Ingredient table helper
     * =================================================================== */
    private void updateIngredientsTable(int newServings) {
        double ratio = (double) newServings / baseServings;
        for (RecipeIngredients ri : ingredientsTable.getItems()) {
            try {
                var oldAmt = new java.math.BigDecimal(ri.getAmount());
                var newAmt = oldAmt.multiply(java.math.BigDecimal.valueOf(ratio));
                ri.setAmount(newAmt.stripTrailingZeros().toPlainString());
            } catch (Exception ignore) {}
        }
    }

    /* =====================================================================
     *  Set recipe & populate UI
     * =================================================================== */
    public void setRecipe(Recipes recipe) {
        this.currentRecipe = recipe;
        if (recipe == null) return;

        baseServings = recipe.getServings();
        likeCountLabel.setText(String.valueOf(recipe.getLikeCount()));
        serveSpinner.getValueFactory().setValue(baseServings);
        cookTimeLabel.setText("Cooking Time: " + recipe.getCookTime() + " min");
        instructionsArea.setText(recipe.getInstructions());

        /* 配料列表 */
        List<RecipeIngredients> list = recipeIngredientsService.getByRecipeId(recipe.getId());
        ingredientsTable.setItems(FXCollections.observableArrayList(list));
        updateIngredientsTable(baseServings);

        /* 加载评论列表 */
        List<Comments> comments = commentService.getCommentsByRecipeId(recipe.getId());
        commentsListView.setItems(FXCollections.observableArrayList(comments));

        /* 图片 */
        if (recipeImageView != null && recipe.getImageUrl() != null) {
            try {
                var url = getClass().getResource("/" + recipe.getImageUrl());
                recipeImageView.setImage(url != null ? new javafx.scene.image.Image(url.toString()) : null);
            } catch (Exception ex) { recipeImageView.setImage(null); }
        }
    }

    /* =====================================================================
     *  Navigation buttons (back / edit / delete) — unchanged
     * =================================================================== */
    @FXML private void onBack(ActionEvent e) { 
        switchScene(e, "/fxml/recipe_list.fxml", null);
    }

    @FXML
    private void onEdit(ActionEvent event) {
        switchScene(event, "/fxml/recipe_edit_add.fxml", currentRecipe);
    }

    @FXML
    private void onDelete(ActionEvent event) {
        new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this recipe?", ButtonType.YES, ButtonType.NO)
            .showAndWait()
            .ifPresent(response -> {
                if (response == ButtonType.YES) {
                    recipeService.deleteRecipe(currentRecipe.getId());
                    showAlert("Recipe deleted successfully!");
                    switchScene(event, "/fxml/recipe_list.fxml", null);
                }
            });
    }

    private void switchScene(ActionEvent e, String fxml, Recipes data) {
        try {
            Node source = (Node) e.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            // 根据目标 FXML 文件，获取控制器并传递数据
            if (fxml.contains("recipe_edit_add.fxml")) {
                RecipeEditAddController controller = loader.getController();
                controller.loadRecipe(data);
                // Set the return target to the detail view
                controller.setReturnTarget("/fxml/recipe_detail.fxml", data);
            } else if (fxml.contains("recipe_detail.fxml")) {
                RecipeInteractionController controller = loader.getController();
                controller.setRecipe(data);
            }
            // 对于 recipe_list.fxml，通常不需要传递特定数据

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /* =====================================================================
     *  Helpers
     * =================================================================== */
    private void showAlert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    private int getCurrentUserId() { return 1; }  // TODO: replace with real login userId
}
