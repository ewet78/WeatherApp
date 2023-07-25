package WeatherApp;

import WeatherApp.controllers.FxmlDefinedController;
import WeatherApp.controllers.MainViewController;

public class App {
    public App() {
        FxmlDefinedController controller = new MainViewController("MainView");
    }
}
