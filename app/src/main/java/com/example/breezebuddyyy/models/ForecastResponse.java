package com.example.breezebuddyyy.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastResponse {
    @SerializedName("lat")
    private double lat;

    @SerializedName("lon")
    private double lon;

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("current")
    private Current current;

    @SerializedName("hourly")
    private List<Hourly> hourly;

    @SerializedName("daily")
    private List<Daily> daily;

    // Current weather
    public static class Current {
        @SerializedName("dt")
        private long dt;

        @SerializedName("temp")
        private double temp;

        @SerializedName("feels_like")
        private double feelsLike;

        @SerializedName("pressure")
        private int pressure;

        @SerializedName("humidity")
        private int humidity;

        @SerializedName("weather")
        private List<Weather> weather;

        // Getters
        public long getDt() { return dt; }
        public double getTemp() { return temp; }
        public double getFeelsLike() { return feelsLike; }
        public int getPressure() { return pressure; }
        public int getHumidity() { return humidity; }
        public List<Weather> getWeather() { return weather; }
    }

    // Hourly forecast
    public static class Hourly {
        @SerializedName("dt")
        private long dt;

        @SerializedName("temp")
        private double temp;

        @SerializedName("feels_like")
        private double feelsLike;

        @SerializedName("pressure")
        private int pressure;

        @SerializedName("humidity")
        private int humidity;

        @SerializedName("weather")
        private List<Weather> weather;

        // Getters
        public long getDt() { return dt; }
        public double getTemp() { return temp; }
        public double getFeelsLike() { return feelsLike; }
        public int getPressure() { return pressure; }
        public int getHumidity() { return humidity; }
        public List<Weather> getWeather() { return weather; }
    }

    // Daily forecast
    public static class Daily {
        @SerializedName("dt")
        private long dt;

        @SerializedName("temp")
        private Temp temp;

        @SerializedName("feels_like")
        private FeelsLike feelsLike;

        @SerializedName("pressure")
        private int pressure;

        @SerializedName("humidity")
        private int humidity;

        @SerializedName("weather")
        private List<Weather> weather;

        // Getters
        public long getDt() { return dt; }
        public Temp getTemp() { return temp; }
        public FeelsLike getFeelsLike() { return feelsLike; }
        public int getPressure() { return pressure; }
        public int getHumidity() { return humidity; }
        public List<Weather> getWeather() { return weather; }
    }

    // Temperature details for daily forecast
    public static class Temp {
        @SerializedName("day")
        private double day;

        @SerializedName("min")
        private double min;

        @SerializedName("max")
        private double max;

        @SerializedName("night")
        private double night;

        @SerializedName("eve")
        private double eve;

        @SerializedName("morn")
        private double morn;

        // Getters
        public double getDay() { return day; }
        public double getMin() { return min; }
        public double getMax() { return max; }
        public double getNight() { return night; }
        public double getEve() { return eve; }
        public double getMorn() { return morn; }
    }

    // Feels like temperature details for daily forecast
    public static class FeelsLike {
        @SerializedName("day")
        private double day;

        @SerializedName("night")
        private double night;

        @SerializedName("eve")
        private double eve;

        @SerializedName("morn")
        private double morn;

        // Getters
        public double getDay() { return day; }
        public double getNight() { return night; }
        public double getEve() { return eve; }
        public double getMorn() { return morn; }
    }

    // Weather condition details
    public static class Weather {
        @SerializedName("id")
        private int id;

        @SerializedName("main")
        private String main;

        @SerializedName("description")
        private String description;

        @SerializedName("icon")
        private String icon;

        // Getters
        public int getId() { return id; }
        public String getMain() { return main; }
        public String getDescription() { return description; }
        public String getIcon() { return icon; }
    }

    // Main class getters
    public double getLat() { return lat; }
    public double getLon() { return lon; }
    public String getTimezone() { return timezone; }
    public Current getCurrent() { return current; }
    public List<Hourly> getHourly() { return hourly; }
    public List<Daily> getDaily() { return daily; }
}