package WeatherApp;

import WeatherApp.views.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;




public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showMainWindow(primaryStage);
    }
}