package WeatherApp.models;

import java.util.List;

public class WeatherResponseWrapper {
    public final List<Weather> list;

    public WeatherResponseWrapper(List<Weather> list) {
        this.list = list;
    }

    public List<Weather> getList() {
        return list;
    }
}
