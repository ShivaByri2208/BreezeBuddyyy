// WeatherResponse.java
package com.example.breezebuddyyy.models;

import java.util.List;

public class WeatherResponse {
    private String name;
    private MainData main;
    private List<Weather> weather;
    private Wind wind;

    // Getters
    public String getName() { return name; }
    public MainData getMain() { return main; }
    public List<Weather> getWeather() { return weather; }
    public Wind getWind() { return wind; }
}

