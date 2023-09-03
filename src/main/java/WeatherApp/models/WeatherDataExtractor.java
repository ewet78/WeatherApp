package WeatherApp.models;

import WeatherApp.models.client.OpenWeatherMapClient;
import com.google.gson.Gson;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

public class WeatherDataExtractor {
    private static WeatherService weatherService = new WeatherService(new OpenWeatherMapClient());
    private static final String API_KEY = "...";
    private static String cityName;
    private static List<Weather> weatherList;
    private Function<String, StringBuilder> responseProvider;

    public WeatherDataExtractor(String cityName, double lat, double lon) {
        this.cityName = cityName;
        weatherList = weatherService.getWeather(cityName, lat, lon);
    }

    public WeatherDataExtractor(String cityName) {
        this.cityName = cityName;
        weatherList = new ArrayList<>();
    }

    public void setResponseProvider(Function<String, StringBuilder> responseProvider) {
        this.responseProvider = responseProvider;
    }

    public Map<LocalDate, List<Double>> extractWeatherData(double lat, double lon) {
        LocalDate weatherDate = null;
        Instant instant;
        Map<LocalDate, List<Double>> temperaturesByDate = new HashMap<>();
        if (responseProvider != null) {
            String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&cnt=40&appid=" + API_KEY + "&units=metric&lang=pl";
            StringBuilder response = responseProvider.apply(apiUrl);
            Gson gson = new Gson();
            WeatherResponseWrapper weatherResponse = gson.fromJson(response.toString(), WeatherResponseWrapper.class);
            if (weatherResponse == null || weatherResponse.getList() == null) {
                System.out.println("Weather data is null or empty!");

            } else {
                long currentTimeInSeconds = 1693234800 / 1000;

                for (Weather data : weatherResponse.getList()) {
                    if (data.getDate() >= currentTimeInSeconds) { // Compare timestamps in seconds
                        Weather weather = new Weather(
                                data.getWeather(),
                                data.getMain(),
                                data.getDate() * 1000, // Convert from seconds to milliseconds
                                data.getDt_txt());

                        weatherList.add(weather);
                    }
                }
            }
            for (Weather weather : weatherList) {
            instant = Instant.ofEpochSecond(weather.getDate() / 1000);
            weatherDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                List<Double> weatherTemp = temperaturesByDate.getOrDefault(weatherDate, new ArrayList<>());
                weatherTemp.add(weather.getMain().getTemp());
                temperaturesByDate.put(weatherDate, weatherTemp);
            }

        } else {
            weatherList = weatherService.getWeather(cityName, lat, lon);

            for (Weather weather : weatherList) {
                // Convert milliseconds to seconds and create Instant
                instant = Instant.ofEpochSecond(weather.getDate() / 1000);

                // Convert Instant to LocalDate using system default timezone
                weatherDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

                // Add temperature to the list for the current date
                List<Double> weatherTemp = temperaturesByDate.getOrDefault(weatherDate, new ArrayList<>());
                weatherTemp.add(weather.getMain().getTemp());
                temperaturesByDate.put(weatherDate, weatherTemp);
            }
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
