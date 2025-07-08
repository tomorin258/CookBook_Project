package init;
import javafx.stage.Stage;
import view.*;

public class AppEntrance extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws Exception {
        LoginView loginView = new LoginView();
        loginView.show();
    }
}
