package com.example.ovbha;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class WeatherActivity extends AppCompatActivity {

    private static final String weather_api_key = "6840b14478bc6687cc7a08ac568965f9";
    private WeatherService weatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // Initialize TextView to display weather information
        TextView weatherTextView = findViewById(R.id.weather_text_view);

        // Initialize WeatherService instance
        weatherService = RetrofitClientInstance.getRetrofitInstance().create(WeatherService.class);

        // Get latitude and longitude values passed from CoordActivity
        double latitude = getIntent().getDoubleExtra("latitude", 0.0);
        double longitude = getIntent().getDoubleExtra("longitude", 0.0);

        // Fetch weather information using latitude and longitude
        fetchWeatherData(weatherTextView, latitude, longitude);
    }

    private void fetchWeatherData(final TextView weatherTextView, double latitude, double longitude) {
        Call<WeatherResponse> call = weatherService.getWeatherData(weather_api_key, String.valueOf(latitude), String.valueOf(longitude));

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    double temperature = weatherResponse.getMainInfo().getTemperature();
                    String weatherInfo = "Temperature: " + temperature + "°C";
                    weatherTextView.setText(weatherInfo);
                } else {
                    // Handle unsuccessful response
                    weatherTextView.setText("Failed to fetch weather data");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Handle network errors
                t.printStackTrace();
                weatherTextView.setText("Network error occurred");
            }
        });
    }
}
