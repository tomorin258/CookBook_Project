package controller;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.converter.DefaultStringConverter;
import model.RecipeIngredients;
import model.Recipes;
import service.IngredientService;
import service.RecipeIngredientsService;
import service.RecipeService;
import util.CurrentUser;

public class RecipeEditAddController {

    /* ---------- FXML nodes ---------- */
    @FXML private Button saveBtn;
    @FXML private Button backBtn;
    @FXML private TextField titleField;
    @FXML private Spinner<Integer> serveSpinner;
    @FXML private TextField cookTimeField;
    @FXML private TextArea instructionsArea;
    @FXML private TextArea commentsArea;                 //  NEW
    @FXML private ImageView recipeImageView;

    @FXML private javafx.scene.control.TableView<RecipeIngredients> ingredientsTable;
    @FXML private TableColumn<RecipeIngredients, String> ingredientNameCol;
    @FXML private TableColumn<RecipeIngredients, String> quantityCol;
    @FXML private TableColumn<RecipeIngredients, String> unitsCol;
    @FXML private TableColumn<RecipeIngredients, String> ingredientDescCol;

    @FXML private Button addRowBtn;
    @FXML private Button delRowBtn;
    @FXML private Button uploadBtn;

    /* ---------- Services ---------- */
    private final RecipeIngredientsService recipeIngredientsService = new RecipeIngredientsService();
    private final RecipeService            recipeService            = new RecipeService();
    private final IngredientService        ingredientService        = new IngredientService();

    /* ---------- State ---------- */
    private java.io.File imageFile;
    private Recipes editingRecipe;
    private String returnFxmlPath = "/fxml/recipe_list.fxml"; // Default return path
    private Recipes returnRecipe; // Data for the return scene (e.g., for detail view)

    /* =====================================================================
     *  Set the scene to return to after saving or cancelling
     * =================================================================== */
    public void setReturnTarget(String fxmlPath, Recipes recipe) {
        this.returnFxmlPath = fxmlPath;
        this.returnRecipe = recipe;
    }

    public void loadRecipe(Recipes recipe) {
        if (recipe == null) return;

        editingRecipe = recipe;
        titleField.setText(recipe.getTitle());
        serveSpinner.getValueFactory().setValue(recipe.getServings());
        cookTimeField.setText(String.valueOf(recipe.getCookTime()));
        instructionsArea.setText(recipe.getInstructions());

        if (commentsArea != null) commentsArea.setText("");

        List<RecipeIngredients> list = recipeIngredientsService.getByRecipeId(recipe.getId());
        ingredientsTable.setItems(javafx.collections.FXCollections.observableArrayList(list));

        if (recipe.getImageUrl() != null) {
            java.net.URL url = getClass().getResource("/" + recipe.getImageUrl());
            recipeImageView.setImage(url != null ? new javafx.scene.image.Image(url.toString()) : null);
        }
    }

    /* =====================================================================
     *  UI handlers
     * =================================================================== */
    @FXML
    private void onRemoveAll() {
        titleField.clear();
        serveSpinner.getValueFactory().setValue(1);
        cookTimeField.clear();
        instructionsArea.clear();
        if (commentsArea != null) commentsArea.clear();  //  NEW
        recipeImageView.setImage(null);
        ingredientsTable.getItems().clear();
    }

    @FXML
    private void onSave(ActionEvent event) {
        String title        = titleField.getText();
        Integer servings    = serveSpinner.getValue();
        String cookTimeStr  = cookTimeField.getText();
        String instructions = instructionsArea.getText();

        if (servings == null) {
            new Alert(Alert.AlertType.ERROR, "Servings cannot be null!").showAndWait();
            return;
        }  

        for (RecipeIngredients ri : ingredientsTable.getItems()) {
            String amount = ri.getAmount();
                if (amount == null || amount.isBlank() || !amount.matches("\\d+(\\.\\d+)?")) {
                    new Alert(Alert.AlertType.ERROR, "Ingredient amount must be a number (no letters or symbols): " + amount).showAndWait();
                    return; 
                }
        }  

        boolean isEdit = (editingRecipe != null);
        Recipes recipe = isEdit ? editingRecipe : new Recipes();

        recipe.setTitle(title);
        recipe.setServings(servings);
        if (!cookTimeStr.isBlank()) recipe.setCookTime(Integer.parseInt(cookTimeStr));
        recipe.setInstructions(instructions);
        recipe.setUserId(CurrentUser.getId());

        if (imageFile != null) {
            String projectRoot = System.getProperty("user.dir").replace("\\", "/");
            String imagesDir   = projectRoot + "/src/main/resources/images";
            String absPath     = imageFile.getAbsolutePath().replace("\\", "/");
            String relPath     = absPath.startsWith(imagesDir) ? absPath.substring(imagesDir.length() + 1)
                    : imageFile.getName();
            recipe.setImageUrl("images/" + relPath);
        }

        if (isEdit) recipeService.updateRecipe(recipe);
        else        recipeService.addRecipe(recipe);
        int recipeId = recipe.getId();

        if (isEdit) recipeIngredientsService.deleteByRecipeId(recipeId);
        for (RecipeIngredients ri : ingredientsTable.getItems()) {
            ri.setRecipeId(recipeId);

            Integer ingId = ingredientService.getIngredientIdByName(ri.getIngredientName());
            if (ingId == null && !ri.getIngredientName().isBlank())
                ingId = ingredientService.addIngredient(ri.getIngredientName());
            ri.setIngredientId(ingId);

            recipeIngredientsService.addRecipeIngredients(ri);
        }

        new Alert(Alert.AlertType.INFORMATION, "Recipe saved successfully!").showAndWait();

        Recipes updatedRecipe = recipeService.getRecipeById(recipeId);
        switchScene(event, returnFxmlPath, updatedRecipe);
    }

    @FXML private void onAddRow()     { ingredientsTable.getItems().add(new RecipeIngredients()); }
    @FXML private void onDeleteRow()  {
        RecipeIngredients sel = ingredientsTable.getSelectionModel().getSelectedItem();
        if (sel != null) ingredientsTable.getItems().remove(sel);
        else new Alert(Alert.AlertType.WARNING, "Please choose the row to delete!").showAndWait();
    }

    @FXML
    private void onUpload(ActionEvent event) {
        javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
        fc.setTitle("Choose Image");
        fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        java.io.File file = fc.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        if (file != null) {
            imageFile = file;
            recipeImageView.setImage(new javafx.scene.image.Image(file.toURI().toString()));
        }
    }

    @FXML 
    private void onBack(ActionEvent event) {
        // Return to the previous scene (list or detail)
        switchScene(event, returnFxmlPath, this.returnRecipe);
    }

    /* =====================================================================
     *  Initialize table columns
     * =================================================================== */
    @FXML
    public void initialize() {
        ingredientsTable.setEditable(true);
        ingredientsTable.setItems(javafx.collections.FXCollections.observableArrayList());

        ingredientNameCol.setCellValueFactory(d -> d.getValue().ingredientNameProperty());
        ingredientNameCol.setCellFactory(tc -> new javafx.scene.control.cell.TextFieldTableCell<>(new DefaultStringConverter()));
        ingredientNameCol.setOnEditCommit(e -> e.getRowValue().setIngredientName(e.getNewValue()));

        quantityCol.setCellValueFactory(d -> d.getValue().amountProperty());
        quantityCol.setCellFactory(tc -> new javafx.scene.control.cell.TextFieldTableCell<>(new DefaultStringConverter()));
        quantityCol.setOnEditCommit(e -> e.getRowValue().setAmount(e.getNewValue()));

        unitsCol.setCellValueFactory(d -> d.getValue().unitProperty());
        unitsCol.setCellFactory(tc -> new javafx.scene.control.cell.TextFieldTableCell<>(new DefaultStringConverter()));
        unitsCol.setOnEditCommit(e -> e.getRowValue().setUnit(e.getNewValue()));

        ingredientDescCol.setCellValueFactory(d -> d.getValue().descriptionProperty());
        ingredientDescCol.setCellFactory(tc -> new javafx.scene.control.cell.TextFieldTableCell<>(new DefaultStringConverter()));
        ingredientDescCol.setOnEditCommit(e -> e.getRowValue().setDescription(e.getNewValue()));

        if (serveSpinner.getValueFactory() == null) {
            serveSpinner.setValueFactory(
                    new javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        }
    }

    /**
     * Helper to switch scenes.
     * @param event The trigger event to get the source window.
     * @param fxmlFile The FXML file to load.
     * @param recipe The recipe data to pass to the next controller.
     */
    private void switchScene(ActionEvent event, String fxmlFile, Recipes recipe) {
        try {
            Node source = (Node) event.getSource();
            javafx.stage.Stage stage = (javafx.stage.Stage) source.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Pass data to the next controller if needed
            if (fxmlFile.contains("recipe_detail.fxml")) {
                RecipeInteractionController controller = loader.getController();
                controller.setRecipe(recipe);
            }
            // No data needed for recipe_list.fxml as it loads all recipes by default

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load page: " + fxmlFile).showAndWait();
        }
    }
}
