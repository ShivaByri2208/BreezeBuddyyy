package com.example.breezebuddyyy.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HourlyWeather {
    @SerializedName("dt")
    private long dt;

    @SerializedName("temp")
    private double temp;

    @SerializedName("weather")
    private List<WeatherInfo> weather;

    // Getters and setters
    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public List<WeatherInfo> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherInfo> weather) {
        this.weather = weather;
    }
}
