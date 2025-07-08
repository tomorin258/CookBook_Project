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
    @FXML private VBox sortedListVBox;
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

    private void showSortedPageByLikes(int page) {
        sortedListVBox.getChildren().clear();
        if (sortedRecipes == null || sortedRecipes.isEmpty()) {
            pageInfoLabel.setText("No recipes found.");
            return;
        }
        int from = (page - 1) * pageSize;
        int to   = Math.min(from + pageSize, sortedRecipes.size());

        for (Recipes rec : sortedRecipes.subList(from, to)) {
            HBox card = new HBox(15);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new Insets(10));
            card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 8; -fx-background-radius: 8;");

            ImageView imageView = new ImageView(loadImageSafe(rec.getImageUrl()));
            imageView.setFitHeight(80);
            imageView.setFitWidth(80);

            Label titleLabel = new Label(rec.getTitle());
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            Label likesLabel = new Label("👍 " + rec.getLikeCount());
            likesLabel.setStyle("-fx-font-size: 14px;");

            VBox titleAndLikes = new VBox(5, titleLabel, likesLabel);
            card.getChildren().addAll(imageView, titleAndLikes);

            card.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    switchScene(event.getSource(), "/fxml/recipe_detail.fxml", rec);
                }
            });

            sortedListVBox.getChildren().add(card);
        }

        pageInfoLabel.setText("Page " + page + " / " + totalPage);
        prevPageBtn.setDisable(page == 1);
        nextPageBtn.setDisable(page == totalPage);
    }

    @FXML private void onPrevPage() { 
        if (currentPage > 1) {
            --currentPage;
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
        switchScene(event.getSource(), "/fxml/recipe_list.fxml", null);
    }

    @FXML private void onAddRecipe(ActionEvent event) {
        switchScene(event.getSource(), "/fxml/recipe_edit_add.fxml", null);
    }

    @FXML public void onSearch() {
        String keyword = keywordField.getText();
        if (keyword == null || keyword.trim().isEmpty()) {
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING, "Keyword cannot be empty!").showAndWait();
            return;
        }
        sortedRecipes = searchRecipes(keyword);
        totalPage = (int) Math.ceil((double) sortedRecipes.size() / pageSize);
        currentPage = 1;
        showSortedPage(currentPage);
    }

    @FXML
    private void onSortByLikes(ActionEvent event) {
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
                if (data != null) {
                    controller.loadRecipe(data);
                }
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
}