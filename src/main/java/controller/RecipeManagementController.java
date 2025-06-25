package controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Recipes;
import service.RecipeService;

public class RecipeManagementController {

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

    // 初始化方法，加载所有配方
    @FXML
    public void initialize() {
        if (sortedListVBox != null) { // 只在按赞排序界面执行
            sortedRecipes = recipeService.getRecipesSortedByLikes();
            totalPage = (int) Math.ceil((double) sortedRecipes.size() / pageSize);
            currentPage = 1;
            showSortedPageByLikes(currentPage);
        } else {
            loadAllRecipes(); // 其它界面
        }
    }

    // 按赞排序分页显示
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

    // 上一页
    @FXML
    private void onPrevPage() {
        if (currentPage > 1) {
            currentPage--;
            showSortedPageByLikes(currentPage);
        }
    }

    // 下一页
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
        // TODO: 跳转到新增配方界面
    }

    // 搜索按钮事件
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

    // 按点赞排序按钮事件
    @FXML
    private void onSortByLikes() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/recipe_sortbylikes.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Recipes Sorted by Likes");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
            // 可选：关闭当前窗口
            // backBtn.getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}