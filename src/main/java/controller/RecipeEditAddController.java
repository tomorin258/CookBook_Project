package controller;

import java.util.List;

import config.MyBatisUtil; // 确保导入你的 Service
import javafx.fxml.FXML;
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
import service.RecipeIngredientsService; // 确保有这行
import service.RecipeService; // 确保导入
import util.CurrentUser; // 假设你有这个工具类

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
    private final RecipeService recipeService = new RecipeService(); // 新增 RecipesService 实例
    private final IngredientService ingredientService = new IngredientService(MyBatisUtil.getSqlSessionFactory());
    private java.io.File imageFile; // 用于保存上传的图片文件对象
    private Recipes editingRecipe; // 当前编辑的菜谱（新增字段）

    public void loadRecipe(Recipes recipe) {
        if (recipe == null) return;
        editingRecipe = recipe; // 记录正在编辑的菜谱
        if (titleField != null) titleField.setText(recipe.getTitle());
        if (serveSpinner != null && serveSpinner.getValueFactory() != null) {
            serveSpinner.getValueFactory().setValue(recipe.getServings());
        }
        if (cookTimeField != null) cookTimeField.setText(String.valueOf(recipe.getCookTime()));
        if (instructionsArea != null) instructionsArea.setText(recipe.getInstructions());

        // 推荐将图片加载逻辑替换为如下
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

        // ...其它字段赋值...
        List<RecipeIngredients> ingredientList = recipeIngredientsService.getByRecipeId(recipe.getId());
        ingredientsTable.setItems(javafx.collections.FXCollections.observableArrayList(ingredientList));
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
        // 只在用户点击“清空”时清空配料表
        ingredientsTable.getItems().clear();
    }

    @FXML
    private void onSave() {
        String title = titleField.getText();
        Integer servings = serveSpinner.getValue();
        if (servings == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR, "份数不能为空！");
            alert.showAndWait();
            return;
        }
        String cookTime = cookTimeField.getText();
        String instructions = instructionsArea.getText();

        // 判断是编辑还是新增
        boolean isEdit = (editingRecipe != null);

        Recipes recipe = isEdit ? editingRecipe : new Recipes();

        recipe.setTitle(title);
        recipe.setServings(servings);
        if (cookTime != null && !cookTime.isEmpty()) {
            recipe.setCookTime(Integer.parseInt(cookTime));
        }
        recipe.setInstructions(instructions);
        int currentUserId = CurrentUser.getId();
        recipe.setUserId(currentUserId);

        // 新建时点赞数为0，编辑时保持原有
        if (!isEdit) {
            recipe.setLikeCount(0);
        }

        // 图片路径处理
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

        Integer recipeId;
        if (isEdit) {
            // 编辑：更新原有菜谱
            recipeService.updateRecipe(recipe);
            recipeId = recipe.getId();
            // 先删除原有配料
            recipeIngredientsService.deleteByRecipeId(recipeId);
        } else {
            // 新增
            recipeId = recipeService.addRecipeAndReturnId(recipe);
            if (recipeId == null) {
                // 保存失败，弹窗提示
                return;
            }
            recipe.setId(recipeId);
        }

        // 保存配料
        for (RecipeIngredients ri : ingredientsTable.getItems()) {
            ri.setRecipeId(recipeId);
            Integer ingredientId = ingredientService.getIngredientIdByName(ri.getIngredientName());
            if (ingredientId == null) {
                ingredientId = ingredientService.addIngredient(ri.getIngredientName());
            }
            ri.setIngredientId(ingredientId);
            recipeIngredientsService.addRecipeIngredients(ri);
        }

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, "保存成功！");
        alert.showAndWait();
        saveBtn.getScene().getWindow().hide();
    }

    @FXML
    private void onAddRow() {
        RecipeIngredients newRow = new RecipeIngredients();
        newRow.setAmount("0"); // 传递字符串，而不是 BigDecimal
        newRow.setUnit("");
        newRow.setIngredientName(""); // 允许用户输入
        ingredientsTable.getItems().add(newRow); // 只add，不setItems
    }

    @FXML
    private void onDeleteRow() {
        // 获取选中的行
        model.RecipeIngredients selected = ingredientsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ingredientsTable.getItems().remove(selected);
        } else {
            // 可选：弹窗提示未选中
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.WARNING, "请先选择要删除的配料行！");
            alert.showAndWait();
        }
    }

    @FXML
    private void onUpload() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("选择图片");
        fileChooser.getExtensionFilters().addAll(
            new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        java.io.File file = fileChooser.showOpenDialog(uploadBtn.getScene().getWindow());
        if (file != null) {
            this.imageFile = file; // 保存文件对象
            javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
            recipeImageView.setImage(image);
        }
    }

    @FXML
    private void onBack() {
        // 关闭当前窗口
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
