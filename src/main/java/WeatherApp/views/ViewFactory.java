package WeatherApp.views;

import WeatherApp.App;
import WeatherApp.controllers.FxmlDefinedController;
import WeatherApp.controllers.MainViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {

    public static final int MAIN_WINDOW_HEIGHT = 800;
    public static final int MAIN_WINDOW_WIDTH = 800;
    private static final HBox MAIN_VIEW = (HBox) loadFXML(new MainViewController("MainView"));
    private static final Scene SCENE = new Scene(MAIN_VIEW);

    public static Parent loadFXML(FxmlDefinedController controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(ViewFactory.class.getResource("/views/fxml/" + controller.getFxmlName() + ".fxml"));
        fxmlLoader.setController(controller);
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error with loadFXML function!");
            return null;
        }
    }

    public static void showMainWindow(Stage primaryStage) {
        primaryStage.setScene(SCENE);
        primaryStage.setMinWidth(MAIN_WINDOW_WIDTH);
        primaryStage.setMinHeight(MAIN_WINDOW_HEIGHT);
        primaryStage.setMaximized(false);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Aplikacja pogodowa");
        primaryStage.show();
    }

}
