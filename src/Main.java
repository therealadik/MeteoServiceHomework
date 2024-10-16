import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args){
        final String accessKey = "7d5176b4-13e4-482c-abd7-8cc9d8e5379f";
        final String lat = "54.74094085138421";
        final String lon = "55.98916949668384";
        final String limit = "10";

        final String urlPath = String.format("https://api.weather.yandex.ru/v2/forecast?lat=%s&lon=%s&limit=%s",lat, lon, limit);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlPath))
                .header("X-Yandex-Weather-Key", accessKey)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());
            System.out.println("Полный ответ: " + jsonObject);

            JSONObject fact = jsonObject.getJSONObject("fact");
            int temperature = fact.getInt("temp");
            System.out.println("Температура: " + temperature);

            JSONArray forecasts = jsonObject.getJSONArray("forecasts");
            int totalTemp = 0;
            int tempCount = 0;

            String firstDay = forecasts.getJSONObject(0).getString("date");
            String lastDay = forecasts.getJSONObject(forecasts.length()-1).getString("date");

            for (int i = 0; i < forecasts.length(); i++){
                JSONObject forecast = forecasts.getJSONObject(i);
                JSONArray hours = forecast.getJSONArray("hours");

                for (int j = 0; j < hours.length(); j++){
                    totalTemp += hours.getJSONObject(j).getInt("temp");
                    tempCount++;
                }
            }

            System.out.println("Средняя температура с " + firstDay + " по " + lastDay + " равна " + (totalTemp/tempCount) + "℃.");

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка запроса.", e);
        }
    }
}