package WeatherApp.models;

public class WeatherData {
    public final String description;
    public final String icon;

    public WeatherData(String description, String icon) {
        this.description = description;
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}
