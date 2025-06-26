package controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.Recipes;
import service.RecipeService;

public class RecipeManagementController {

    @FXML private Button saveBtn;
    @FXML private TextField keywordField;
    @FXML private ListView<Recipes> recipeListView;
    @FXML private VBox sortedListVBox;
    @FXML private Button prevPageBtn;
    @FXML private Button nextPageBtn;
    @FXML private Label pageInfoLabel;
    @FXML private Button backBtn;
    @FXML private Button addRecipeBtn;
    @FXML private Button searchBtn;
    @FXML private Button sortLikeBtn;

    // 你需要在类字段区添加这些控件的 @FXML 声明（如未声明）：
    @FXML private TextField titleField;
    @FXML private Spinner<Integer> serveSpinner;
    @FXML private TextField cookTimeField;
    @FXML private TextArea instructionsArea;
    @FXML private ImageView recipeImageView;
    // 其它如 descriptionField、prepTimeField、ingredientsTable 等，按实际 FXML 控件补充

    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPage = 1;
    private List<Recipes> sortedRecipes;

    // rely on Service
    private final RecipeService recipeService = new RecipeService();

    // add recipes
    public boolean addRecipe(Recipes recipe) {
        return recipeService.addRecipe(recipe);
    }

    // delete recipes
    public boolean deleteRecipe(int recipeId) {
        return recipeService.deleteRecipe(recipeId);
    }

    // edit recipes
    public boolean editRecipe(Recipes recipe) {
        return recipeService.editRecipe(recipe);
    }

    // get recipes by keyword
    public List<Recipes> searchRecipes(String keyword) {
        return recipeService.searchRecipes(keyword);
    }

    // 加载所有配方（初始化或搜索为空时调用）
    private void loadAllRecipes() {
        List<Recipes> all = recipeService.searchRecipes(null);
        sortedRecipes = all.stream()
                .sorted(Comparator.comparingInt(Recipes::getLikeCount).reversed())
                .collect(Collectors.toList());
        totalPage = (int) Math.ceil((double) sortedRecipes.size() / pageSize);
        currentPage = 1;
        showSortedPage(currentPage);
    }

    // 分页显示（显示到 recipeListView）
    @FXML
    private void showSortedPage(int page) {
        recipeListView.getItems().clear();
        if (sortedRecipes == null || sortedRecipes.isEmpty()) {
            pageInfoLabel.setText("No recipes found.");
            return;
        }
        int from = (page - 1) * pageSize;
        int to = Math.min(from + pageSize, sortedRecipes.size());
        List<Recipes> pageList = sortedRecipes.subList(from, to);
        recipeListView.getItems().addAll(pageList);
        pageInfoLabel.setText("Page " + page + " / " + totalPage);
        prevPageBtn.setDisable(page == 1);
        nextPageBtn.setDisable(page == totalPage);
    }

    // initialize方法
    @FXML
    public void initialize() {
        if (sortedListVBox != null) { //only when sortedListVBox is present
            sortedRecipes = recipeService.getRecipesSortedByLikes();
            totalPage = (int) Math.ceil((double) sortedRecipes.size() / pageSize);
            currentPage = 1;
            showSortedPageByLikes(currentPage);
        } else {
            loadAllRecipes(); // load all recipes if sortedListVBox is not present
        }

        recipeListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // 双击
                Recipes selected = recipeListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    try {
                        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/recipe_detail.fxml"));
                        javafx.scene.Parent root = loader.load();
                        controller.RecipeInteractionController detailController = loader.getController();
                        detailController.setRecipe(selected); // 你需要在RecipeInteractionController中实现setRecipe方法
                        javafx.stage.Stage stage = new javafx.stage.Stage();
                        stage.setTitle("Recipe Detail");
                        stage.setScene(new javafx.scene.Scene(root));
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // showSortedPageByLikes
    @FXML
    private void showSortedPageByLikes(int page) {
        sortedListVBox.getChildren().clear();
        if (sortedRecipes == null || sortedRecipes.isEmpty()) {
            pageInfoLabel.setText("No recipes found.");
            return;
        }
        int from = (page - 1) * pageSize;
        int to = Math.min(from + pageSize, sortedRecipes.size());
        List<Recipes> pageList = sortedRecipes.subList(from, to);
        for (Recipes recipe : pageList) {
            Label label = new Label(recipe.getTitle() + "  👍" + recipe.getLikeCount());
            label.setStyle("-fx-font-size:18;-fx-text-fill:#0d3b66;-fx-background-color:#fff;-fx-padding:8 12 8 12;-fx-background-radius:8;");
            sortedListVBox.getChildren().add(label);
        }
        pageInfoLabel.setText("Page " + page + " / " + totalPage);
        prevPageBtn.setDisable(page == 1);
        nextPageBtn.setDisable(page == totalPage);
    }

    // previous page
    @FXML
    private void onPrevPage() {
        if (currentPage > 1) {
            currentPage--;
            showSortedPageByLikes(currentPage);
        }
    }

    // next page
    @FXML
    private void onNextPage() {
        if (currentPage < totalPage) {
            currentPage++;
            showSortedPageByLikes(currentPage);
        }
    }

    @FXML
    private void onBack() {
        backBtn.getScene().getWindow().hide();
    }

    @FXML
    private void onAddRecipe() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/recipe_edit_add.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Add Recipe");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // onSearch
    @FXML
    public void onSearch() {
        String keyword = keywordField.getText();
        List<Recipes> result = searchRecipes(keyword);
        sortedRecipes = result.stream()
                .sorted(Comparator.comparingInt(Recipes::getLikeCount).reversed())
                .collect(Collectors.toList());
        totalPage = (int) Math.ceil((double) sortedRecipes.size() / pageSize);
        currentPage = 1;
        showSortedPage(currentPage);
    }

    // onSortByLikes
    @FXML
    private void onSortByLikes() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/recipe_sortbylikes.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Recipes Sorted by Likes");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setKeyword(String keyword) {
        if (keywordField != null) {
            keywordField.setText(keyword);
        }
    }
}