package com.example.ovbha;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("weather")
    Call<WeatherResponse> getWeatherData(
            @Query("api_key") String apiKey,
            @Query("lat") String latitude,
            @Query("lon") String longitude
    );

    @GET("alerts")
    Call<WeatherAlertsResponse> getWeatherAlerts(
            @Query("appid") String apiKey,
            @Query("lat") double latitude,
            @Query("lon") double longitude
    );
}
