package WeatherApp.controllers;

import WeatherApp.models.*;
import WeatherApp.models.client.OpenWeatherMapClient;
import WeatherApp.models.client.OpenWeatherMapIconDownloader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class MainViewController implements FxmlDefinedController, Initializable {
    private final String fxmlName;

    @FXML
    private Button checkWeatherCityLiving;

    @FXML
    private Button checkWeatherCityDestination;

    @FXML
    private ComboBox<DataOfLocations> chooseLocalization;


    @FXML
    private ComboBox<DataOfLocations> chooseLocalizationOfDestination;

    @FXML
    private TextField findCityDestination;

    @FXML
    private TextField findCityLiving;

    @FXML
    private Label weatherState1;
    @FXML
    private Label weatherState2;

    @FXML
    private Label weatherState3;

    @FXML
    private Label weatherState4;

    @FXML
    private Label weatherState5;

    @FXML
    private Label weatherStateDestination1;

    @FXML
    private Label weatherStateDestination2;

    @FXML
    private Label weatherStateDestination3;

    @FXML
    private Label weatherStateDestination4;

    @FXML
    private Label weatherStateDestination5;

    @FXML
    private ImageView weatherStateImage1;


    @FXML
    private ImageView weatherStateImage2;

    @FXML
    private ImageView weatherStateImage3;

    @FXML
    private ImageView weatherStateImage4;

    @FXML
    private ImageView weatherStateImage5;

    @FXML
    private ImageView weatherStateImageDestination1;

    @FXML
    private ImageView weatherStateImageDestination2;

    @FXML
    private ImageView weatherStateImageDestination3;

    @FXML
    private ImageView weatherStateImageDestination4;

    @FXML
    private ImageView weatherStateImageDestination5;

    @FXML
    private Label cityDestination;

    @FXML
    private Label cityLiving;


    private WeatherService weatherService;

    public MainViewController(String fxmlName) {
        this.fxmlName = fxmlName;
    }
    @FXML
    void checkWeatherCityLivingAction() throws InterruptedException {
        DataOfLocations selectedLocation = chooseLocalization.getValue();
        double latitude = selectedLocation.getLat();
        double longitude = selectedLocation.getLon();

        String cityName = selectedLocation.getName();

        List<Weather> weatherList = weatherService.getWeather(cityName, latitude, longitude);

        cityLiving.setText(cityName);

        if (weatherList != null && !weatherList.isEmpty()) {
            // Weather data is available, proceed with displaying the information

            int loopCount = Math.min(weatherList.size(), 5);
            for (int i = 0; i < loopCount; i++) {
                Weather weather = weatherList.get(i);
                double temperature = weather.getMain().getTemp();
                String description = weather.getWeather()[0].getDescription();
                OpenWeatherMapIconDownloader openWeatherMapIconDownloader = new OpenWeatherMapIconDownloader(weather.getWeather()[0].getIcon());
                Image image = new Image("icons/" + weather.getWeather()[0].getIcon() + ".png");
                switch (i) {
                    case 0:
                        weatherStateImage1.setImage(image);
                        weatherState1.setText(description);
                        break;
                    case 1:
                        weatherStateImage2.setImage(image);
                        weatherState2.setText(description);
                        break;
                    case 2:
                        weatherStateImage3.setImage(image);
                        weatherState3.setText(description);
                        break;
                    case 3:
                        weatherStateImage4.setImage(image);
                        weatherState4.setText(description);
                        break;
                    case 4:
                        weatherStateImage5.setImage(image);
                        weatherState5.setText(description);
                        break;
                    default:
                        break;
                }
            }


        } else {
            // Weather data is not available or empty
            weatherState1.setText("No weather data available.");
        }

        WeatherDataExtractor weatherDataExtractor = new WeatherDataExtractor(cityName, latitude, longitude);
        Map<LocalDate, List<Double>> weatherDataByDates = weatherDataExtractor.extractWeatherData(latitude, longitude);
        Map<LocalDate, List<Double>> sortedWeatherDataByDates = sortByDates(weatherDataByDates);

        int count = 0;

        for (Map.Entry<LocalDate, List<Double>> entry : sortedWeatherDataByDates.entrySet()) {
            LocalDate date = entry.getKey();
            List<Double> temperatures = entry.getValue();

            // Calculate max and min temperatures for the current date
            double maxTemperature = temperatures.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
            double minTemperature = temperatures.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);

                switch (count) {
                    case 0:
                        String existingText1 = weatherState1.getText();
                        weatherState1.setText(date + ": " + maxTemperature + "°C\\" + minTemperature + "°C,\n" + existingText1);
                        break;
                    case 1:
                        String existingText2 = weatherState2.getText();
                        weatherState2.setText(date + ": " + maxTemperature + "°C\\" + minTemperature + "°C,\n" + existingText2);
                        break;
                    case 2:
                        String existingText3 = weatherState3.getText();
                        weatherState3.setText(date + ": " + maxTemperature + "°C\\" + minTemperature + "°C,\n" + existingText3);
                        break;
                    case 3:
                        String existingText4 = weatherState4.getText();
                        weatherState4.setText(date + ": " + maxTemperature + "°C\\" + minTemperature + "°C,\n" + existingText4);
                        break;
                    case 4:
                        String existingText5 = weatherState5.getText();
                        weatherState5.setText(date + ": " + maxTemperature + "°C\\" + minTemperature + "°C,\n" + existingText5);
                        break;
                    default:
                        break;
            }

            count++;
        }

    }

    @FXML
    void setOnKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String localization = capitalizeFirstLetter(findCityLiving.getText());
            if (!localization.isEmpty()) {
                OpenWeatherMapClient openWeatherMapClient = new OpenWeatherMapClient();
                ObservableList<DataOfLocations> locationsWithCountryCode = openWeatherMapClient.getLocations(localization);

                chooseLocalization.getItems().clear();

                for (DataOfLocations location : locationsWithCountryCode) {
                    chooseLocalization.getItems().add(location);
                }
            } else {
                String customLocation = "Brak podobnej lokalizacji";
                DataOfLocations customData = new DataOfLocations(customLocation, 0.0, 0.0);
            }
        }
    }


    @FXML
    void setOnKeyPressedDestination(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String localization = capitalizeFirstLetter(findCityDestination.getText());
            if (!localization.isEmpty()) {
                OpenWeatherMapClient openWeatherMapClient = new OpenWeatherMapClient();
                ObservableList<DataOfLocations> locationsWithCountryCode = openWeatherMapClient.getLocations(localization);

                chooseLocalizationOfDestination.getItems().clear();

                for (DataOfLocations location : locationsWithCountryCode) {
                    chooseLocalizationOfDestination.getItems().add(location);
                }
            } else {
                String customLocation = "Brak podobnej lokalizacji";
                DataOfLocations customData = new DataOfLocations(customLocation, 0.0, 0.0);
            }
        }

    }


    @FXML
    void checkWeatherCityDestinationAction() {
        DataOfLocations selectedLocation = chooseLocalizationOfDestination.getValue();
        double latitude = selectedLocation.getLat();
        double longitude = selectedLocation.getLon();

        String cityName = selectedLocation.getName();

        List<Weather> weatherList = weatherService.getWeather(cityName, latitude, longitude);

        cityDestination.setText(cityName);

        if (weatherList != null && !weatherList.isEmpty()) {
            // Weather data is available, proceed with displaying the information

            int loopCount = Math.min(weatherList.size(), 5);
            for (int i = 0; i < loopCount; i++) {
                Weather weather = weatherList.get(i);
                double temperature = weather.getMain().getTemp();
                String description = weather.getWeather()[0].getDescription();
                OpenWeatherMapIconDownloader openWeatherMapIconDownloader = new OpenWeatherMapIconDownloader(weather.getWeather()[0].getIcon());
                Image image = new Image("icons/" + weather.getWeather()[0].getIcon() + ".png");
                switch (i) {
                    case 0:
                        weatherStateImageDestination1.setImage(image);
                        weatherStateDestination1.setText(description);
                        break;
                    case 1:
                        weatherStateImageDestination2.setImage(image);
                        weatherStateDestination2.setText(description);
                        break;
                    case 2:
                        weatherStateImageDestination3.setImage(image);
                        weatherStateDestination3.setText(description);
                        break;
                    case 3:
                        weatherStateImageDestination4.setImage(image);
                        weatherStateDestination4.setText(description);
                        break;
                    case 4:
                        weatherStateImageDestination5.setImage(image);
                        weatherStateDestination5.setText(description);
                        break;
                    default:
                        break;
                }
            }


        } else {
            // Weather data is not available or empty
            weatherStateDestination1.setText("No weather data available.");
        }

        WeatherDataExtractor weatherDataExtractor = new WeatherDataExtractor(cityName, latitude, longitude);
        Map<LocalDate, List<Double>> weatherDataByDates = weatherDataExtractor.extractWeatherData(latitude, longitude);
        Map<LocalDate, List<Double>> sortedWeatherDataByDates = sortByDates(weatherDataByDates);

        int count = 0;

        for (Map.Entry<LocalDate, List<Double>> entry : sortedWeatherDataByDates.entrySet()) {
            LocalDate date = entry.getKey();
            List<Double> temperatures = entry.getValue();

            // Calculate max and min temperatures for the current date
            double maxTemperature = temperatures.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
            double minTemperature = temperatures.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);

            switch (count) {
                case 0:
                    String existingText1 = weatherStateDestination1.getText();
                    weatherStateDestination1.setText(date + ": " + maxTemperature + "°C\\" + minTemperature + "°C,\n" + existingText1);
                    break;
                case 1:
                    String existingText2 = weatherStateDestination2.getText();
                    weatherStateDestination2.setText(date + ": " + maxTemperature + "°C\\" + minTemperature + "°C,\n" + existingText2);
                    break;
                case 2:
                    String existingText3 = weatherStateDestination3.getText();
                    weatherStateDestination3.setText(date + ": " + maxTemperature + "°C\\" + minTemperature + "°C,\n" + existingText3);
                    break;
                case 3:
                    String existingText4 = weatherStateDestination4.getText();
                    weatherStateDestination4.setText(date + ": " + maxTemperature + "°C\\" + minTemperature + "°C,\n" + existingText4);
                    break;
                case 4:
                    String existingText5 = weatherStateDestination5.getText();
                    weatherStateDestination5.setText(date + ": " + maxTemperature + "°C\\" + minTemperature + "°C,\n" + existingText5);
                    break;
                default:
                    break;
            }

            count++;
        }

    }

    public static Map<LocalDate, List<Double>> sortByDates(Map<LocalDate, List<Double>> unsortedMap) {
        // Create a custom comparator to compare LocalDate objects
        Comparator<LocalDate> dateComparator = LocalDate::compareTo;

        // Use a TreeMap with the custom comparator to automatically sort the entries based on keys (dates)
        TreeMap<LocalDate, List<Double>> sortedMap = new TreeMap<>(dateComparator);
        sortedMap.putAll(unsortedMap);

        // Copy the sorted entries to a new LinkedHashMap to preserve insertion order
        LinkedHashMap<LocalDate, List<Double>> sortedByDatesMap = new LinkedHashMap<>();
        sortedMap.forEach((key, value) -> sortedByDatesMap.put(key, value));

        return sortedByDatesMap;
    }

    private String capitalizeFirstLetter(String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            return cityName;
        }

        String firstLetter = cityName.substring(0, 1).toUpperCase();
        String restOfString = cityName.substring(1).toLowerCase();

        return firstLetter + restOfString;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        weatherService = WeatherServiceFactory.createWeatherService();

    }

    public String getFxmlName() {
        return fxmlName;
    }
}
