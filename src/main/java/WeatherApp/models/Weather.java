package WeatherApp.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Weather {
    public final WeatherData[] weather;
    public final MainData main;
    public final long dt;
    public final String dt_txt;

    public Weather(WeatherData[] weather, MainData main, long dt, String dt_txt) {
        this.weather = weather;
        this.main = main;
        this.dt = dt;
        this.dt_txt = dt_txt;
    }

    public WeatherData[] getWeather() {
        return weather;
    }

    public MainData getMain() {
        return main;
    }

    public long getDate() {
        return dt;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "description='" + getWeather()[0].getDescription() + '\'' +
                ", icon='" + getWeather()[0].getIcon() + '\'' +
                ", temperature=" + getMain().getTemp() +
                ", date=" + formatDate(dt) +
                '}';
    }

    private static String formatDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
