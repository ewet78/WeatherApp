package WeatherApp.models.client;

public class Location {
    private double lat;
    private double lon;
    private String name;
    private String country;

    public Location(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public  String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
