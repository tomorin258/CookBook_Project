package controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import service.RecipeService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Recipes;

public class RecipeManagementController {

    @FXML private TextField keywordField;
    @FXML private ListView<Recipes> recipeListView;
    @FXML private VBox sortedListVBox;
    @FXML private Button prevPageBtn;
    @FXML private Button nextPageBtn;
    @FXML private Label pageInfoLabel;
    @FXML private Button backBtn;

    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPage = 1;
    private List<Recipes> sortedRecipes;

    // 只依赖 Service
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

    // 搜索按钮事件
    @FXML
    public void onSearch() {
        String keyword = keywordField.getText();
        List<Recipes> result = searchRecipes(keyword);
        recipeListView.getItems().setAll(result);
    }

    // 按点赞数排序
    public List<Recipes> getRecipesSortedByLikes() {
        List<Recipes> all = recipeService.searchRecipes(null);
        return all.stream()
                .sorted(Comparator.comparingInt(Recipes::getLikeCount).reversed())
                .collect(Collectors.toList());
    }

    @FXML
    public void initialize() {
        if (sortedListVBox != null) {
            sortedRecipes = getRecipesSortedByLikes();
            totalPage = (int) Math.ceil((double) sortedRecipes.size() / pageSize);
            showSortedPage(currentPage);
        }
    }

    @FXML
    private void showSortedPage(int page) {
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
            label.setStyle("-fx-font-size:18;-fx-padding:8 0;");
            sortedListVBox.getChildren().add(label);
        }
        pageInfoLabel.setText("Page " + page + " / " + totalPage);
        prevPageBtn.setDisable(page == 1);
        nextPageBtn.setDisable(page == totalPage);
    }
}