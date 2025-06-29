package controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;              
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;    
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;        
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Recipes;
import service.RecipeService;

public class RecipeManagementController {

    @FXML private Button saveBtn;
    @FXML private TextField keywordField;
    @FXML private ListView<Recipes> recipeListView;
    @FXML private VBox sortedListVBox; // 这是 recipe_sortbylikes.fxml 中的 VBox
    @FXML private Button prevPageBtn, nextPageBtn;
    @FXML private Label pageInfoLabel;
    @FXML private Button backBtn, addRecipeBtn, searchBtn, sortLikeBtn;
    @FXML private TextField titleField, cookTimeField;
    @FXML private Spinner<Integer> serveSpinner;
    @FXML private TextArea instructionsArea;
    @FXML private ImageView recipeImageView;
    private int currentPage = 1;
    private final int pageSize = 5;
    private int totalPage = 1;
    private List<Recipes> sortedRecipes;
    private final RecipeService recipeService = new RecipeService();
    public boolean addRecipe(Recipes recipe)   { return recipeService.addRecipe(recipe);   }
    public boolean deleteRecipe(int id)        { return recipeService.deleteRecipe(id);    }
    public boolean editRecipe(Recipes recipe)   { return recipeService.editRecipe(recipe);  }
    public List<Recipes> searchRecipes(String k){ return recipeService.searchRecipes(k);    }
    @FXML
    public void initialize() {
        if (recipeListView != null) {
            recipeListView.setCellFactory(list -> new ListCell<>() {

                private final ImageView img = new ImageView();
                private final Label      title = new Label();
                private final HBox       box = new HBox(12, img, title);

                {
                    img.setFitWidth(64);
                    img.setFitHeight(64);
                    img.setPreserveRatio(true);
                    title.setStyle("-fx-font-size:16; -fx-font-weight:bold;");
                    box.setAlignment(Pos.CENTER_LEFT);
                    box.setPadding(new Insets(6, 12, 6, 12));
                }

                @Override
                protected void updateItem(Recipes rec, boolean empty) {
                    super.updateItem(rec, empty);
                    if (empty || rec == null) {
                        setGraphic(null);
                    } else {
                        img.setImage(loadImageSafe(rec.getImageUrl()));
                        title.setText(rec.getTitle());
                        setGraphic(box);
                    }
                }
            });
            recipeListView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Recipes selected = recipeListView.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        switchScene(event.getSource(), "/fxml/recipe_detail.fxml", selected);
                    }
                }
            });
            loadAllRecipes();
        }
        if (sortedListVBox != null) {
            sortedRecipes = recipeService.getRecipesSortedByLikes();
            totalPage = (int) Math.ceil((double) sortedRecipes.size() / pageSize);
            currentPage = 1;
            showSortedPageByLikes(currentPage);
        }
    }
    private void loadAllRecipes() {
        List<Recipes> all = recipeService.searchRecipes(null);
        sortedRecipes = all; 
        totalPage = (int) Math.ceil((double) sortedRecipes.size() / pageSize);
        currentPage = 1;
        showSortedPage(currentPage);
    }

    private void showSortedPage(int page) {
        recipeListView.getItems().clear();
        if (sortedRecipes == null || sortedRecipes.isEmpty()) {
            pageInfoLabel.setText("No recipes found.");
            return;
        }
        int from = (page - 1) * pageSize;
        int to   = Math.min(from + pageSize, sortedRecipes.size());
        recipeListView.getItems().addAll(sortedRecipes.subList(from, to));

        pageInfoLabel.setText("Page " + page + " / " + totalPage);
        prevPageBtn.setDisable(page == 1);
        nextPageBtn.setDisable(page == totalPage);
    }

    // 用这个版本替换你现有的 showSortedPageByLikes 方法
    private void showSortedPageByLikes(int page) {
        sortedListVBox.getChildren().clear();
        if (sortedRecipes == null || sortedRecipes.isEmpty()) {
            pageInfoLabel.setText("No recipes found.");
            return;
        }
        int from = (page - 1) * pageSize;
        int to   = Math.min(from + pageSize, sortedRecipes.size());

        // 遍历当前页的菜谱
        for (Recipes rec : sortedRecipes.subList(from, to)) {
            // 1. 创建卡片UI (这里我们直接创建，不需要单独的方法)
            HBox card = new HBox(15);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 8; -fx-background-radius: 8;");

            ImageView imageView = new ImageView(loadImageSafe(rec.getImageUrl()));
            imageView.setFitHeight(80);
            imageView.setFitWidth(80);

            Label titleLabel = new Label(rec.getTitle());
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            // 使用正确的 getLikeCount() 方法
            Label likesLabel = new Label("👍 " + rec.getLikeCount());
            likesLabel.setStyle("-fx-font-size: 14px;");

            VBox titleAndLikes = new VBox(5, titleLabel, likesLabel);
            card.getChildren().addAll(imageView, titleAndLikes);

            // 2. 【核心】为卡片添加点击事件
            card.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    switchScene(event.getSource(), "/fxml/recipe_detail.fxml", rec);
                }
            });

            // 3. 将卡片添加到VBox
            sortedListVBox.getChildren().add(card);
        }

        pageInfoLabel.setText("Page " + page + " / " + totalPage);
        prevPageBtn.setDisable(page == 1);
        nextPageBtn.setDisable(page == totalPage);
    }

    @FXML private void onPrevPage() { 
        if (currentPage > 1) {
            --currentPage;
            // 根据当前在哪个VBox上操作，调用对应的方法
            if (sortedListVBox != null && sortedListVBox.isVisible()) {
                showSortedPageByLikes(currentPage);
            } else {
                showSortedPage(currentPage);
            }
        }
    }
    @FXML private void onNextPage() { 
        if (currentPage < totalPage) {
            ++currentPage;
            if (sortedListVBox != null && sortedListVBox.isVisible()) {
                showSortedPageByLikes(currentPage);
            } else {
                showSortedPage(currentPage);
            }
        }
    }
    @FXML private void onBack(ActionEvent event)     { 
        // 从排序页面返回列表页
        switchScene(event.getSource(), "/fxml/recipe_list.fxml", null);
    }

    @FXML private void onAddRecipe(ActionEvent event) {
        // 跳转到添加页面，并设置返回目标为列表页
        switchScene(event.getSource(), "/fxml/recipe_edit_add.fxml", null);
    }

    @FXML public void onSearch() {
        String keyword = keywordField.getText();
        sortedRecipes = searchRecipes(keyword);
        totalPage = (int) Math.ceil((double) sortedRecipes.size() / pageSize);
        currentPage = 1;
        showSortedPage(currentPage);
    }

    @FXML
    private void onSortByLikes(ActionEvent event) {
        // 跳转到排序页面
        switchScene(event.getSource(), "/fxml/recipe_sortbylikes.fxml", null);
    }

    public void setKeyword(String keyword) {
        keywordField.setText(keyword);
    }

    private void switchScene(Object eventSource, String fxml, Recipes data) {
        try {
            Node source = (Node) eventSource;
            Stage stage = (Stage) source.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            if (fxml.equals("/fxml/recipe_detail.fxml")) {
                RecipeInteractionController controller = loader.getController();
                controller.setRecipe(data);
            } else if (fxml.equals("/fxml/recipe_edit_add.fxml")) {
                RecipeEditAddController controller = loader.getController();
                if (data != null) { // Editing existing recipe
                    controller.loadRecipe(data);
                }
                // When adding a new recipe or editing, set the return path to the list view
                controller.setReturnTarget("/fxml/recipe_list.fxml", null);
            }

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image loadImageSafe(String url) {
        try {
            return new Image(Objects.requireNonNull(
                    getClass().getResource("/" + url)).toExternalForm());
        } catch (Exception e) {
            return new Image(getClass().getResource("/images/placeholder.png").toExternalForm());
        }
    }

    /**
     * 这是你已经有的、用来打开详情页的方法
     * @param recipe 要在详情页中显示的菜谱
     */
    private void openDetail(Recipes recipe) {
        // 此方法现在可以被 switchScene 替代，但为保持兼容性，我们让它也调用 switchScene
        Node node = recipeListView; // 获取一个有效的Node
        try {
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/recipe_detail.fxml"));
            Parent root = loader.load();
            RecipeInteractionController controller = loader.getController();
            controller.setRecipe(recipe);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}