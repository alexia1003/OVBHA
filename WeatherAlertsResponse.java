package com.example.ovbha;
import java.util.List;
import com.google.gson.annotations.SerializedName;


public class WeatherAlertsResponse {

    @SerializedName("alerts")
    private List<Alert> alerts;

    public List<Alert> getAlerts() {
        return alerts;
    }
}

class Alert {
    @SerializedName("event")
    private String event;

    @SerializedName("description")
    private String description;

    public String getEvent() {
        return event;
    }

    public String getDescription() {
        return description;
    }
}
