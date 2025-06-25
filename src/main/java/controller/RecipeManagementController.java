package controller;

<<<<<<< HEAD
=======
import java.util.ArrayList;
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

<<<<<<< HEAD
import service.RecipeService;
=======
import dao.mappers.RecipesMapper;
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
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
<<<<<<< HEAD

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
=======
    private RecipesMapper recipesMapper;

    //add recipes
    /**
     * Adds a new recipe to the database.
     * @param recipe The recipe to add.
     * @return true if the addition was successful, false otherwise.
     */
    public boolean addRecipe(Recipes recipe) {
        return recipesMapper.addRecipes(recipe);
    }
    
    //delete recipes
    /**
     * Deletes a recipe by its ID.
     * @param recipeId The ID of the recipe to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteRecipe(int recipeId) {
        return recipesMapper.deleteRecipes(recipeId);
    }

    //edit recipes
    /**
     * Edits an existing recipe.
     * @param recipe The recipe with updated information.
     * @return true if the edit was successful, false otherwise.
     */
    public boolean editRecipe(Recipes recipe) {
        return recipesMapper.updateRecipes(recipe);
    }

    //get recipes by keyword
    /**
     * Searches for recipes by a keyword in their title.
     * If the keyword is null or empty, returns all recipes.
     * @param keyword The keyword to search for.
     * @return A list of recipes that match the keyword.
     */
    public List<Recipes> searchRecipes(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return recipesMapper.getAllRecipes();
        }
        return recipesMapper.getRecipesByTitle(keyword);
    }

    //get keyword for search
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
    @FXML
    public void onSearch() {
        String keyword = keywordField.getText();
        List<Recipes> result = searchRecipes(keyword);
        recipeListView.getItems().setAll(result);
    }

<<<<<<< HEAD
    // 按点赞数排序
    public List<Recipes> getRecipesSortedByLikes() {
        List<Recipes> all = recipeService.searchRecipes(null);
=======
    //order recipes by likes
    /**
     * Retrieves all recipes sorted by the number of likes in descending order.
     * @return A list of recipes sorted by likes.
     */
    public List<Recipes> getRecipesSortedByLikes() {
        ArrayList<Recipes> all = recipesMapper.getAllRecipes();
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
        return all.stream()
                .sorted(Comparator.comparingInt(Recipes::getLikeCount).reversed())
                .collect(Collectors.toList());
    }

<<<<<<< HEAD
    @FXML
    public void initialize() {
=======
    //initialize pagination controls
    /**
     * Initializes the pagination controls and displays the first page of sorted recipes.
     * This method should be called after the sortedListVBox is set up.
     */
    @FXML
    public void initialize() {
        // if sortedListVBox exists， initialize pagination controls
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
        if (sortedListVBox != null) {
            sortedRecipes = getRecipesSortedByLikes();
            totalPage = (int) Math.ceil((double) sortedRecipes.size() / pageSize);
            showSortedPage(currentPage);
        }
    }

<<<<<<< HEAD
=======

    //show sorted recipes
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
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
<<<<<<< HEAD
=======

    

    
>>>>>>> 158e4cf06541bbd7a1d92d52fa6af4bc116aff8f
}