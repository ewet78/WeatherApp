package WeatherApp.models.client;

import WeatherApp.models.Weather;

import java.util.List;

public interface WeatherClient {
    List<Weather> getWeather(String cityName, double lat, double lon);
}
