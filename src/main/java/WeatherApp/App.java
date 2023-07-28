package WeatherApp;

import WeatherApp.controllers.FxmlDefinedController;
import WeatherApp.controllers.MainViewController;
import WeatherApp.views.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public App() {
        FxmlDefinedController controller = new MainViewController("MainView");
    }

    @Override
    public void init() throws Exception {
        super.init();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showMainWindow(primaryStage);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
