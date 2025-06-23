package Init;

import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginView;

public class AppEntrance extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 交给 LoginView 去加载 FXML 并把场景挂到同一个 stage
        new LoginView().start(stage);
    }

    public static void main(String[] args) {
        launch(args);   // 启动 JavaFX 应用
    }
}
