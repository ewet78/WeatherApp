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
import java.util.*;
import java.util.function.Function;

public class OpenWeatherMapClient implements WeatherClient {

    private static final String API_KEY = "...";

    private Function<String, StringBuilder> responseProvider;

    public void setResponseProvider(Function<String, StringBuilder> responseProvider) {
        this.responseProvider = responseProvider;
    }

    @Override
    public List<Weather> getWeather(String cityName, double lat, double lon) {
        List<Weather> filteredWeatherList = new ArrayList<>();
        StringBuilder response;
        try {
            if (responseProvider != null) {
                String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&cnt=40&appid=" + API_KEY + "&units=metric&lang=pl";
                response = responseProvider.apply(apiUrl);
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

                            filteredWeatherList.add(weather);
                        }
                    }
                }

            } else {
                response = getResponse("https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&cnt=40&appid=" + API_KEY + "&units=metric&lang=pl");

                Gson gson = new Gson();
                WeatherResponseWrapper weatherResponse = gson.fromJson(response.toString(), WeatherResponseWrapper.class);
                if (weatherResponse == null || weatherResponse.getList() == null) {
                    System.out.println("Weather data is null or empty!");
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
            }

            } catch(IOException e){
                e.printStackTrace();
                return Collections.emptyList();
            } catch(JsonSyntaxException e){
                e.printStackTrace();
                return Collections.emptyList();
            } catch(Exception e){
                e.printStackTrace();
                System.out.println("List is empty!");
            }

        return filteredWeatherList;
    }

    public ObservableList<DataOfLocations> getLocations(String cityName) {
        StringBuilder response = null;
        try {
            if (responseProvider != null) {
                String apiUrl = "http://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&limit=5&appid=" + API_KEY;
                response = responseProvider.apply(apiUrl);
            } else {
                response = getResponse("http://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&limit=5&appid=" + API_KEY);
            }
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

    public StringBuilder getResponse(String cityName) throws IOException {

        if (responseProvider != null) {
            return responseProvider.apply(cityName);
        } else {

            URL url = new URL(cityName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response;
            } else {
                System.out.println("HTTP request failed with response code: " + responseCode);
                return new StringBuilder();
            }
        }
    }
}
