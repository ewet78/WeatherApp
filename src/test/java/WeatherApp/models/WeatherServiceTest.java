package WeatherApp.models;

import WeatherApp.models.client.OpenWeatherMapClient;
import WeatherApp.models.client.WeatherClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class WeatherServiceTest {
    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private OpenWeatherMapClient openWeatherMapClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnWeatherForecast() {
        //given
        LocalDate date = LocalDate.now();
        List<Weather> expectedForecastWeather = List.of(createWeather(date), createWeather(date.plusDays(1)), createWeather(date.plusDays(2)));
        when(openWeatherMapClient.getWeather("Kraków", 50.06, 19.94))
                .thenReturn(expectedForecastWeather);

        //when
        List<Weather> result = weatherService.getWeather("Kraków", 50.06, 19.94);

        //then
        assertEquals(result, expectedForecastWeather);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenCannotGetWeather() {
        //given
        when(openWeatherMapClient.getWeather("City", 50.06, 19.94)).thenThrow(new RuntimeException());

        //when & then
        assertThatThrownBy(() -> weatherService.getWeather("City", 50.06, 19.94)).isInstanceOf(RuntimeException.class);
    }

    MainData mainTemp = new MainData(22.8);
    long dateInMillis = 1693234800L;

    private Weather createWeather(LocalDate date) {
        WeatherData[] weather = new WeatherData[1];
        WeatherData data = new WeatherData("zachmurzenie umiarkowane", "04n");
        weather[0] = data;
        return new Weather(weather, mainTemp, dateInMillis, date.toString());
    }
}
