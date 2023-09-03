package WeatherApp.models;

import WeatherApp.models.client.OpenWeatherMapClient;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherDataExtractorTest {
    private WeatherDataExtractor weatherDataExtractor;

    @BeforeEach
    void setUp() {
        weatherDataExtractor = new WeatherDataExtractor("Kraków");
    }

    @Test
    void shouldReturnMaxAndMinTemperatureInNextFiveDays() throws IOException {
        //given
        String sampleResponse = new String(Files.readAllBytes(Path.of("src/test/resources/weather.json")));
        weatherDataExtractor.setResponseProvider(url -> new StringBuilder(sampleResponse));
        //when
        Map<LocalDate, List<Double>> maxMinTemperaturesByDate = weatherDataExtractor.extractWeatherData(50.06, 19.94);
        //then
        assertEquals(6, maxMinTemperaturesByDate.size());
        assertThat(maxMinTemperaturesByDate).doesNotContainKey(LocalDate.parse("2023-09-03"));
    }

}
