package com.example.ovbha;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {

    @SerializedName("weather")
    private WeatherInfo[] weatherInfo;

    @SerializedName("main")
    private MainInfo mainInfo;

    public String getTemperature() {
        return mainInfo.getTemperature() + "°C";
    }

    public String getConditions() {
        if (weatherInfo != null && weatherInfo.length > 0) {
            return weatherInfo[0].getCondition();
        } else {
            return "Unknown";
        }
    }

    // Constructor
    public WeatherResponse(WeatherInfo[] weatherInfo, MainInfo mainInfo) {
        this.weatherInfo = weatherInfo;
        this.mainInfo = mainInfo;
    }

    // Getters and Setters
    public WeatherInfo[] getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(WeatherInfo[] weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public MainInfo getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(MainInfo mainInfo) {
        this.mainInfo = mainInfo;
    }
}

class WeatherInfo {
    @SerializedName("main")
    private String condition;

    @SerializedName("description")
    private String description;

    // Constructor
    public WeatherInfo(String condition, String description) {
        this.condition = condition;
        this.description = description;
    }

    // Getters and Setters
    public String getCondition() {
        return condition;
    }

/*
    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
*/
}

class MainInfo {
    @SerializedName("temp")
    private double temperature;

    @SerializedName("humidity")
    private double humidity;

    // Constructor
    public MainInfo(double temperature, double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    // Getters and Setters
    public double getTemperature() {
        return temperature;
    }
/*
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
*/
}
