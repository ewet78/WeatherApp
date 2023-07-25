package WeatherApp.models;

public class DataOfLocations {
    private String name;
    private double lat;
    private double lon;

    public DataOfLocations(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return name + ", lat: " + lat + ", lon: " + lon;
    }
}
