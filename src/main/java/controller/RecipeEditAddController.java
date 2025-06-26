package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.RecipeIngredients;
import model.Recipes;

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

    public void loadRecipe(Recipes recipe) {
        if (recipe == null) return;
        if (titleField != null) titleField.setText(recipe.getTitle());
        if (serveSpinner != null && serveSpinner.getValueFactory() != null) {
            serveSpinner.getValueFactory().setValue(recipe.getServings());
        }
        if (cookTimeField != null) cookTimeField.setText(String.valueOf(recipe.getCookTime()));
        if (instructionsArea != null) instructionsArea.setText(recipe.getInstructions());
        if (recipeImageView != null && recipe.getImageUrl() != null) {
            try {
                recipeImageView.setImage(new javafx.scene.image.Image(recipe.getImageUrl()));
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
        // 只在用户点击“清空”时清空配料表
        ingredientsTable.getItems().clear();
    }

    @FXML
    private void onSave() {
        String title = titleField.getText();
        int servings = serveSpinner.getValue();
        String cookTime = cookTimeField.getText();
        String instructions = instructionsArea.getText();
        Recipes recipe = new Recipes();
        recipe.setTitle(title);
        recipe.setServings(servings);
        recipe.setCookTime(Integer.parseInt(cookTime));
        recipe.setInstructions(instructions);
        // 其它字段赋值...

        // 保存到数据库或列表
        // TODO: 调用service保存
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
            javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
            recipeImageView.setImage(image);
            // 如需保存图片路径，可在此处记录 file.toURI().toString() 到 Recipes 对象
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
        ingredientNameCol.setCellFactory(tc -> new javafx.scene.control.cell.TextFieldTableCell<>());
        ingredientNameCol.setOnEditCommit(event -> {
            event.getRowValue().setIngredientName(event.getNewValue());
        });

        quantityCol.setCellValueFactory(data -> data.getValue().amountProperty());
        quantityCol.setCellFactory(tc -> new javafx.scene.control.cell.TextFieldTableCell<>());
        quantityCol.setOnEditCommit(event -> {
            event.getRowValue().setAmount(event.getNewValue());
        });

        unitsCol.setCellValueFactory(data -> data.getValue().unitProperty());
        unitsCol.setCellFactory(tc -> new javafx.scene.control.cell.TextFieldTableCell<>());
        unitsCol.setOnEditCommit(event -> {
            event.getRowValue().setUnit(event.getNewValue());
        });

        ingredientDescCol.setCellValueFactory(data -> 
            new SimpleStringProperty("") // 你的实体没有desc字段，可留空或自定义
        );
        ingredientDescCol.setCellFactory(tc -> new javafx.scene.control.cell.TextFieldTableCell<>());
        ingredientDescCol.setOnEditCommit(event -> {
            // 如有desc字段可设置
        });
    }
}
