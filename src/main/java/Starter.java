import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;

public class Starter extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Bold.ttf"), 24);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-BoldItalic.ttf"), 24);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Italic.ttf"), 24);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Light.ttf"), 24);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Medium.ttf"), 24);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Regular.ttf"), 24);

        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/home_page.fxml"))));
        stage.setTitle("Home Page");
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
    }
}
