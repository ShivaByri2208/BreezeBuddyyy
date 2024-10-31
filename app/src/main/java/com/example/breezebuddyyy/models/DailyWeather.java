package com.example.breezebuddyyy.models;

import java.util.List;

// DailyWeather.java
public class DailyWeather {
    private long dt;
    private Temp temp;
    private double pop;
    private List<Weather> weather;

    // Getters
    public long getDt() { return dt; }
    public Temp getTemp() { return temp; }
    public double getPop() { return pop; }
    public List<Weather> getWeather() { return weather; }
}
