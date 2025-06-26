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
    @FXML private javafx.scene.image.ImageView recipeImageView;

    private Recipes currentRecipe;
    private int baseServings = 1;

    private final RecipeService recipeService = new RecipeService();
    private final CommentService commentService = new CommentService();
    private final RecipeIngredientsService recipeIngredientsService = new RecipeIngredientsService();

    @FXML
    public void initialize() {
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
        alert.setTitle("Tipps");
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
            java.util.List<RecipeIngredients> ingredientList = recipeIngredientsService.getByRecipeId(recipe.getId());
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
    private void onEdit(javafx.event.ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/recipe_edit_add.fxml"));
            javafx.scene.Parent root = loader.load();
            RecipeEditAddController editController = loader.getController();
            editController.loadRecipe(currentRecipe);
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Edit Recipe");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
            setRecipe(recipeService.getRecipesById(currentRecipe.getId()));
        } catch (Exception e) {
            showAlert("Cannot open the edit page" + e.getMessage());
            e.printStackTrace();
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
                // 关闭当前窗口
                ((javafx.stage.Stage)(((javafx.scene.Node)event.getSource()).getScene().getWindow())).close();
            } else {
                showAlert("Failed to delete the recipe, please try again!");
            }
        }
    }
}