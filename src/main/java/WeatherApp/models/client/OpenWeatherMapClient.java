package WeatherApp.models.client;


import WeatherApp.models.DataOfLocations;
import WeatherApp.models.Weather;
import WeatherApp.models.WeatherResponseWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class OpenWeatherMapClient implements WeatherClient {

    private static final String API_KEY = "d39fff73dc8b11dcd2815c231d636586";

    @Override
    public List<Weather> getWeather(String cityName, double lat, double lon) {
        List<Weather> filteredWeatherList = new ArrayList<>();

        try {
            StringBuilder response = getResponse("https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&cnt=40&appid=" + API_KEY + "&units=metric&lang=pl");

            Gson gson = new Gson();
            WeatherResponseWrapper weatherResponse = gson.fromJson(response.toString(), WeatherResponseWrapper.class);

            if (weatherResponse == null || weatherResponse.getList() == null) {
                throw new Exception("Weather data is null or empty.");
            }

            long currentTimeInSeconds = System.currentTimeMillis() / 1000;

            for (Weather data : weatherResponse.getList()) {
                if (data.getDate() >= currentTimeInSeconds) { // Compare timestamps in seconds
                    Weather weather = new Weather(
                            data.getWeather(),
                            data.getMain(),
                            data.getDate() * 1000, // Convert from seconds to milliseconds
                            data.getDt_txt());

                    filteredWeatherList.add(weather);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("List is empty!");
        }

        return filteredWeatherList;
    }

    private List<Weather> getWeatherForNextFiveDays(List<Weather> weatherList) {
        // Group weather data by date
        Map<String, List<Weather>> weatherByDate = groupWeatherDataByDate(weatherList);

        // Get the dates for the next five days
        List<String> nextFiveDays = getNextFiveDays(weatherByDate);

        // Get weather data for each day in the next five days
        List<Weather> nextFiveDaysWeather = new ArrayList<>();
        for (String date : nextFiveDays) {
            List<Weather> weatherForDate = weatherByDate.get(date);
            if (weatherForDate != null && !weatherForDate.isEmpty()) {
                nextFiveDaysWeather.addAll(weatherForDate);
            }
        }

        return nextFiveDaysWeather;
    }

    private Map<String, List<Weather>> groupWeatherDataByDate(List<Weather> weatherList) {
        Map<String, List<Weather>> weatherByDate = new HashMap<>();

        for (Weather data : weatherList) {
            String date = formatDate(data.getDate()); // Pass the timestamp directly
            weatherByDate.putIfAbsent(date, new ArrayList<>());
            weatherByDate.get(date).add(data);
        }

        return weatherByDate;
    }
    private static List<String> getNextFiveDays(Map<String, List<Weather>> weatherByDate) {
        // Get today's date and create a list to store the next five days
        List<String> nextFiveDays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        // Loop to find the next five days (excluding today)
        while (nextFiveDays.size() < 5) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String date = formatDate(calendar.getTimeInMillis());
            if (weatherByDate.containsKey(date)) {
                nextFiveDays.add(date);
            }
        }

        return nextFiveDays;
    }



    private static String formatDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public ObservableList<DataOfLocations> getLocations(String cityName) {
        StringBuilder response = null;
        try {
            response = getResponse("http://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&limit=5&appid=" + API_KEY);
        } catch (IOException e) {
            System.out.println("Lack of such localization");
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Location[] locations = gson.fromJson(response.toString(), Location[].class);


        ObservableList<DataOfLocations> dataOfLocations = FXCollections.observableArrayList();

        for (Location location : locations) {
            String locationWithCode = location.getName() + ", " + location.getCountry();
            double lat = location.getLat();
            double lon = location.getLon();
            dataOfLocations.add(new DataOfLocations(locationWithCode, lat, lon));

        }

        return dataOfLocations;
    }

   public String getCountry(String cityName) {
       StringBuilder response = null;
       try {
           response = getResponse("http://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&limit=5&appid=" + API_KEY);
       } catch (IOException e) {
           e.printStackTrace();
       }
       Gson gson = new Gson();
        Location[] locations = gson.fromJson(response.toString(), Location[].class);
        String country = locations[0].getCountry();
        return country;
    }

    private static double[] getLocationLatLon(String cityName) throws IOException {
        StringBuilder response = getResponse("http://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&limit=5&appid=" + API_KEY);

        Gson gson = new Gson();
        Location[] locations = gson.fromJson(response.toString(), Location[].class);
        double lat = locations[0].getLat();
        double lon = locations[0].getLon();
        return new double[]{lat, lon};
    }

    private static StringBuilder getResponse(String cityName) throws IOException {
        URL url = new URL(cityName);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response;
    }
}
