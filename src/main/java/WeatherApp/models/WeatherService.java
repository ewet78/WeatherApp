package WeatherApp.models;

import WeatherApp.models.client.OpenWeatherMapClient;
import WeatherApp.models.client.WeatherClient;

import java.util.List;

public class WeatherService {
    public final WeatherClient weatherClient;

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public List<Weather> getWeather(String cityName, double lat, double lon) {
        OpenWeatherMapClient openWeatherMapClient = new OpenWeatherMapClient();
        return weatherClient.getWeather(cityName, lat, lon);
    }
}
