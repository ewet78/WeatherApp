package WeatherApp.models;

import WeatherApp.models.client.OpenWeatherMapClient;
import WeatherApp.models.client.WeatherClient;

public class WeatherServiceFactory {
    public static WeatherService createWeatherService() {
        return new WeatherService(createWeatherClient());
    }

    private static WeatherClient createWeatherClient() {
        return new OpenWeatherMapClient();
    }
}
