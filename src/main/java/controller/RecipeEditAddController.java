package controller;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import model.RecipeIngredients;
import model.Recipes;
import service.IngredientService;
import service.RecipeIngredientsService;
import service.RecipeService; 
import util.CurrentUser; 

public class RecipeEditAddController {

    @FXML private Button saveBtn;
    @FXML private Button backBtn;
    @FXML private TextField titleField;
    @FXML private Spinner<Integer> serveSpinner;
    @FXML private TextField cookTimeField;
    @FXML private TextArea instructionsArea;
    @FXML private ImageView recipeImageView;
    @FXML private javafx.scene.control.TableView<model.RecipeIngredients> ingredientsTable;
    @FXML private TableColumn<model.RecipeIngredients, String> ingredientNameCol;
    @FXML private TableColumn<model.RecipeIngredients, String> quantityCol;
    @FXML private TableColumn<model.RecipeIngredients, String> unitsCol;
    @FXML private TableColumn<model.RecipeIngredients, String> ingredientDescCol;
    @FXML private Button addRowBtn;
    @FXML private Button delRowBtn;
    @FXML private Button uploadBtn;

    private final RecipeIngredientsService recipeIngredientsService = new RecipeIngredientsService();
    private final RecipeService recipeService = new RecipeService(); 
    // 使用默认的无参数构造函数
    private final IngredientService ingredientService = new IngredientService();
    private java.io.File imageFile; 
    private Recipes editingRecipe; 

    public void loadRecipe(Recipes recipe) {
        if (recipe == null) {
            System.out.println("loadRecipe 接收到的 recipe 对象为 null，操作中止。");
            return;
        }
        editingRecipe = recipe; 
        if (titleField != null) titleField.setText(recipe.getTitle());
        if (serveSpinner != null && serveSpinner.getValueFactory() != null) {
            serveSpinner.getValueFactory().setValue(recipe.getServings());
        }
        if (cookTimeField != null) cookTimeField.setText(String.valueOf(recipe.getCookTime()));
        if (instructionsArea != null) instructionsArea.setText(recipe.getInstructions());

        // **** 在这里加入下面的调试代码 ****
        System.out.println("--- 开始调试 Edit/Add 界面配料 ---");
        System.out.println("正在为 Recipe ID: " + recipe.getId() + " 查询配料...");
        List<RecipeIngredients> ingredientList = recipeIngredientsService.getByRecipeId(recipe.getId());
        System.out.println("获取到的配料数量: " + ingredientList.size());
        for (RecipeIngredients ri : ingredientList) {
            // 打印所有关键字段
            System.out.println(
                "配料名: '" + ri.getIngredientName() + 
                "', 数量: '" + ri.getAmount() + 
                "', 单位: '" + ri.getUnit() + "'"
            );
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
    }

    @FXML
    private void onRemoveAll() {
        if (titleField != null) titleField.clear();
        if (serveSpinner != null && serveSpinner.getValueFactory() != null) {
            serveSpinner.getValueFactory().setValue(1);
        }
        if (cookTimeField != null) cookTimeField.clear();
        if (instructionsArea != null) instructionsArea.clear();
        if (recipeImageView != null) recipeImageView.setImage(null);
        ingredientsTable.getItems().clear();
    }

    @FXML
    private void onSave() {
        String title = titleField.getText();
        Integer servings = serveSpinner.getValue();
        if (servings == null) {
            new Alert(Alert.AlertType.ERROR, "Servings cannot be null!").showAndWait();
            return;
        }
        String cookTime = cookTimeField.getText();
        String instructions = instructionsArea.getText();

        boolean isEdit = (editingRecipe != null);
        Recipes recipe = isEdit ? editingRecipe : new Recipes();

        recipe.setTitle(title);
        recipe.setServings(servings);
        if (cookTime != null && !cookTime.isEmpty()) {
            recipe.setCookTime(Integer.parseInt(cookTime));
        }
        recipe.setInstructions(instructions);
        recipe.setUserId(CurrentUser.getId());

        if (imageFile != null) {
            String projectRoot = System.getProperty("user.dir").replace("\\", "/");
            String imagesDir = projectRoot + "/src/main/resources/images";
            String absPath = imageFile.getAbsolutePath().replace("\\", "/");
            String relativePath;
            if (absPath.startsWith(imagesDir)) {
                relativePath = absPath.substring(imagesDir.length() + 1);
                recipe.setImageUrl("images/" + relativePath);
            } else {
                recipe.setImageUrl(imageFile.getName());
            }
        }

        // 保存或更新菜谱，获取 recipeId
        if (isEdit) {
            recipeService.updateRecipe(recipe);
        } else {
            recipeService.addRecipe(recipe);
        }
        int recipeId = recipe.getId();

        // 如果是编辑模式，先删除所有旧的配料
        if (isEdit) {
            recipeIngredientsService.deleteByRecipeId(recipeId);
        }

        // 循环遍历 TableView 中的每一行，保存配料
        for (RecipeIngredients ri : ingredientsTable.getItems()) {
            ri.setRecipeId(recipeId);
            
            // 查找或创建配料ID
            Integer ingredientId = ingredientService.getIngredientIdByName(ri.getIngredientName());
            if (ingredientId == null) {
                // 确保 ingredientName 不为空
                if (ri.getIngredientName() != null && !ri.getIngredientName().trim().isEmpty()) {
                    ingredientId = ingredientService.addIngredient(ri.getIngredientName());
                }
            }
            ri.setIngredientId(ingredientId);

            // 保存这行配料数据
            recipeIngredientsService.addRecipeIngredients(ri);
        }

        new Alert(Alert.AlertType.INFORMATION, "Recipe saved successfully!").showAndWait();
        ((Stage) titleField.getScene().getWindow()).close();
    }

    @FXML
    private void onAddRow() {
        RecipeIngredients newRow = new RecipeIngredients();
        newRow.setAmount("0");
        newRow.setUnit("");
        newRow.setIngredientName(""); 
        ingredientsTable.getItems().add(newRow);
    }

    @FXML
    private void onDeleteRow() {
        model.RecipeIngredients selected = ingredientsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ingredientsTable.getItems().remove(selected);
        } else {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.WARNING, "Please choose the row to delete!");
            alert.showAndWait();
        }
    }

    @FXML
    private void onUpload() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
            new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        java.io.File file = fileChooser.showOpenDialog(uploadBtn.getScene().getWindow());
        if (file != null) {
            this.imageFile = file; 
            javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
            recipeImageView.setImage(image);
        }
    }

    @FXML
    private void onBack() {
        backBtn.getScene().getWindow().hide();
    }

    @FXML
    public void initialize() {
        ingredientsTable.setEditable(true);
        ingredientsTable.setItems(javafx.collections.FXCollections.observableArrayList());

        ingredientNameCol.setCellValueFactory(data -> data.getValue().ingredientNameProperty());
        ingredientNameCol.setCellFactory(tc -> new javafx.scene.control.cell.TextFieldTableCell<>(new DefaultStringConverter()));
        ingredientNameCol.setOnEditCommit(event -> {
            event.getRowValue().setIngredientName(event.getNewValue());
        });

        quantityCol.setCellValueFactory(data -> data.getValue().amountProperty());
        quantityCol.setCellFactory(tc -> new javafx.scene.control.cell.TextFieldTableCell<>(new DefaultStringConverter()));
        quantityCol.setOnEditCommit(event -> {
            event.getRowValue().setAmount(event.getNewValue());
        });

        unitsCol.setCellValueFactory(data -> data.getValue().unitProperty());
        unitsCol.setCellFactory(tc -> new javafx.scene.control.cell.TextFieldTableCell<>(new DefaultStringConverter()));
        unitsCol.setOnEditCommit(event -> {
            event.getRowValue().setUnit(event.getNewValue());
        });

        ingredientDescCol.setCellValueFactory(data -> data.getValue().descriptionProperty());
        ingredientDescCol.setCellFactory(tc -> new javafx.scene.control.cell.TextFieldTableCell<>(new DefaultStringConverter()));
        ingredientDescCol.setOnEditCommit(event -> {
            event.getRowValue().setDescription(event.getNewValue());
        });

        if (serveSpinner.getValueFactory() == null) {
            serveSpinner.setValueFactory(new javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        }
    }
}
