package WeatherApp.models.client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class OpenWeatherMapIconDownloader {

    public  String iconCode = "10n";

    public String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + ".png";

    public String destinationPath = "C:\\Users\\Ewelina\\Documents\\Java\\WeatherApp\\src\\main\\resources\\icons\\" + iconCode + ".png";

    public OpenWeatherMapIconDownloader(String iconCode) throws IOException {
        this.iconCode = iconCode;
        downloadIcon();
    }

    public String downloadIcon() throws IOException {
        boolean downloadSuccessful = false;
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

            downloadSuccessful = true;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        if (downloadSuccessful){
            return "Weather icon downloaded successfully!";
        } else return "Cannot download icon!";
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
