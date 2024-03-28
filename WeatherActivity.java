package com.example.ovbha;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {

    private TextView weatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherTextView = findViewById(R.id.weather_text_view);

        // Get weather data from intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            double latitude = extras.getDouble("latitude");
            double longitude = extras.getDouble("longitude");

            // Call method to fetch weather data using latitude and longitude
            fetchWeatherData(latitude, longitude);
        } else {
            // No location data
            weatherTextView.setText("No location data available");
        }
    }

    private void fetchWeatherData(double latitude, double longitude) {
        // OpenWeatherMap API endpoint for current weather data
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=6840b14478bc6687cc7a08ac568965f9";

        // Start AsyncTask to fetch weather data from API
        new FetchWeatherTask().execute(apiUrl);
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                try {
                    URL apiUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                    connection.setRequestMethod("GET");

                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    response = stringBuilder.toString();

                    bufferedReader.close();
                    inputStreamReader.close();
                    inputStream.close();
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Parse JSON response and update weatherTextView with weather information
            try {
                JSONObject jsonObject = new JSONObject(result);

                // Get weather description
                String weatherDescription = jsonObject.getJSONArray("weather")
                        .getJSONObject(0).getString("description");

                // Get temperature in Celsius
                double temperature = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;

                // Display weather description and temperature
                weatherTextView.setText("Weather: " + weatherDescription + "\nTemperature: " + temperature + "°C");
            } catch (JSONException e) {
                e.printStackTrace();
                weatherTextView.setText("Failed to fetch weather data");
            }
        }
    }
}
