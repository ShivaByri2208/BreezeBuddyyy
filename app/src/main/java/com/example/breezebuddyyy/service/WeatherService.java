package com.example.breezebuddyyy.service;

import com.example.breezebuddyyy.models.ForecastResponse;
import com.example.breezebuddyyy.models.LocationResult;
import com.example.breezebuddyyy.models.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("weather")
    Call<WeatherResponse> getCurrentWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units
    );

    @GET("forecast")
    Call<ForecastResponse> getForecast(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("exclude") String exclude
    );

    @GET("geo/1.0/direct")
    Call<List<LocationResult>> searchLocation(
            @Query("q") String query,
            @Query("limit") int limit,
            @Query("appid") String apiKey
    );
}