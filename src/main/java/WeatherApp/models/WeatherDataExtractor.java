package WeatherApp.models;

import WeatherApp.models.client.OpenWeatherMapClient;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class WeatherDataExtractor {
    private static WeatherService weatherService = new WeatherService(new OpenWeatherMapClient());
    private static String cityName;
    private static List<Weather> weatherList;

    public WeatherDataExtractor(String cityName, double lat, double lon) {
        this.cityName = cityName;
        weatherList = weatherService.getWeather(cityName, lat, lon);
    }

    public Map<LocalDate, List<Double>> extractWeatherData(double lat, double lon) {
        weatherList = weatherService.getWeather(cityName, lat, lon);

        LocalDate weatherDate = null;

        // Create the map to store temperatures for each date
        Map<LocalDate, List<Double>> temperaturesByDate = new HashMap<>();

        for (Weather weather : weatherList) {
            // Convert milliseconds to seconds and create Instant
            Instant instant = Instant.ofEpochSecond(weather.getDate() / 1000);

            // Convert Instant to LocalDate using system default timezone
            weatherDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

            // Add temperature to the list for the current date
            List<Double> weatherTemp = temperaturesByDate.getOrDefault(weatherDate, new ArrayList<>());
            weatherTemp.add(weather.getMain().getTemp());
            temperaturesByDate.put(weatherDate, weatherTemp);
        }


        // Calculate max and min temperature for each date
        Map<LocalDate, List<Double>> maxMinTemperaturesByDate = new HashMap<>();
        for (Map.Entry<LocalDate, List<Double>> entry : temperaturesByDate.entrySet()) {
            LocalDate date = entry.getKey();
            List<Double> temperatures = entry.getValue();

            double maxTemp = Collections.max(temperatures);
            double minTemp = Collections.min(temperatures);

            List<Double> maxMinTemperatures = new ArrayList<>();
            maxMinTemperatures.add(maxTemp);
            maxMinTemperatures.add(minTemp);

            maxMinTemperaturesByDate.put(date, maxMinTemperatures);
        }

        return maxMinTemperaturesByDate;
    }
}
