package tn.esprit.services;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
public class WeatherService {
    private static final String API_KEY = "4408adf267a2ed5a7760143e074eb381";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=Paris&appid=" + API_KEY;

    public String getWeatherData() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        WeatherService weatherService = new WeatherService();
        String weatherData = weatherService.getWeatherData();
        System.out.println(weatherData);
    }
}

