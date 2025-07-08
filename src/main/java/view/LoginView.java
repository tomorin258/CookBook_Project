package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A pre-configured {@link javafx.stage.Stage Stage} that shows the
 * “CookBook&nbsp;—&nbsp;Login” window.
 *
 * <p>The UI layout is loaded from {@code /fxml/login.fxml}, wrapped in a
 * {@link javafx.scene.Scene Scene}, and assigned to this stage. The dimensions
 * of the window are defined by the root node in the FXML; the stage itself is
 * declared <em>non-resizable</em> to keep those dimensions fixed.</p>
 *
 * <p>This class is purely a view helper: it performs no authentication logic.
 * All interaction handling is delegated to the controller specified inside the
 * FXML file.</p>
 *
 * @author Hao He
 */

public class LoginView extends Stage {
    public LoginView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load());
        this.setTitle("CookBook — Login");
        this.setResizable(false);
        this.setScene(scene);
    }
}

