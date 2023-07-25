package WeatherApp.models.client;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class OpenWeatherMapIconDownloader {

    public  String iconCode;

    public OpenWeatherMapIconDownloader(String iconCode) {
        this.iconCode = iconCode;

        String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + ".png";

        downloadIcon(iconUrl, "C:\\Users\\Ewelina\\Documents\\Java\\WeatherApp\\src\\main\\resources\\icons\\" + iconCode + ".png");
    }

    private static void downloadIcon(String iconUrl, String destinationPath) {
        try {
            URL url = new URL(iconUrl);
            InputStream inputStream = url.openStream();

            FileOutputStream outputStream = new FileOutputStream(destinationPath);

            byte[] buffer = new byte[2048];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            System.out.println("Weather icon downloaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
