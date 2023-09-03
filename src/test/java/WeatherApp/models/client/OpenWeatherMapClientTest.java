package WeatherApp.models.client;

import WeatherApp.models.DataOfLocations;
import WeatherApp.models.Weather;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenWeatherMapClientTest {

    private OpenWeatherMapClient openWeatherMapClient;

    @BeforeEach
    void setUp() {
        openWeatherMapClient = new OpenWeatherMapClient();
    }


    @Test
    void shouldReturnListOf40WeatherObjects() throws IOException {
        //given
        String sampleResponse = new String(Files.readAllBytes(Path.of("src/test/resources/weather.json")));
        openWeatherMapClient.setResponseProvider(url -> new StringBuilder(sampleResponse));

        //when
        List<Weather> myWeatherList = openWeatherMapClient.getWeather("Kraków", 50.06, 19.94);

        //then
        assertEquals(40, myWeatherList.size());
        assertNotEquals(null, myWeatherList);
    }

    @Test
    void shouldThrowExceptionForEmptyList() throws IOException {
        //given
        String sampleResponse = new String(Files.readAllBytes(Path.of("src/test/resources/emptyWeather.json")));
        openWeatherMapClient.setResponseProvider(url -> new StringBuilder(sampleResponse));
        //when
        List<Weather> weatherResponse = openWeatherMapClient.getWeather("SomeCity", 0.0, 0.0);

        //then
        assertTrue(weatherResponse.isEmpty());
    }


    @Test
    void shouldReturnListOf5DataOfLocationsObjects() throws IOException {
        //given
        String sampleResponse = new String(Files.readAllBytes(Path.of("src/test/resources/locations.json")));
        openWeatherMapClient.setResponseProvider(url -> new StringBuilder(sampleResponse));
        //when
        ObservableList<DataOfLocations> locations = openWeatherMapClient.getLocations("Rzym");

        //then
        assertEquals(5, locations.size());
    }

}
