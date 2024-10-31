package com.example.breezebuddyyy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.breezebuddyyy.fragments.LocationSearchFragment;
import com.example.breezebuddyyy.models.ForecastResponse;
import com.example.breezebuddyyy.models.WeatherResponse;
import com.example.breezebuddyyy.service.WeatherService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Weather";
    private static final String API_KEY = "ad428086ac6054ba4beae05e30c92959";
    private static final String BASE_URL = "https://api.openweathermap.org/data/3.0/onecall?";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private FusedLocationProviderClient fusedLocationClient;
    private TextView cityNameText, temperatureText, weatherConditionText;
    private TextView highLowText, humidityText, windSpeedText;
    private TextView hourlyForecastText, weeklyForecastText;
    private ImageView weatherIcon;
    private MaterialCardView mainCard;
    private WeatherService weatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupLocationClick();
        setupRetrofit();
        setupLocationClient();
        checkLocationPermission();
    }

    private void initializeViews() {
        cityNameText = findViewById(R.id.cityNameText);
        temperatureText = findViewById(R.id.temperatureText);
        weatherConditionText = findViewById(R.id.weatherConditionText);
        highLowText = findViewById(R.id.highLowText);
        humidityText = findViewById(R.id.humidityText);
        windSpeedText = findViewById(R.id.windSpeedText);
        weatherIcon = findViewById(R.id.weatherIcon);
        mainCard = findViewById(R.id.mainCard);
        hourlyForecastText = findViewById(R.id.hourlyForecastText);
        weeklyForecastText = findViewById(R.id.weeklyForecastText);
    }

    private void setupRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        weatherService = retrofit.create(WeatherService.class);
    }

    private void setupLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        fetchWeatherData(location.getLatitude(), location.getLongitude());
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, "Error getting location", Toast.LENGTH_SHORT).show()
                );
    }

    private void setupLocationClick() {
        cityNameText.setOnClickListener(v -> {
            LocationSearchFragment searchFragment = new LocationSearchFragment();
            searchFragment.setOnLocationSelectedListener((lat, lon, cityName) -> {
                fetchWeatherData(lat, lon);
            });

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, searchFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }


    private void fetchWeatherData(double latitude, double longitude) {
        Call<WeatherResponse> currentWeatherCall = weatherService.getCurrentWeather(
                latitude,
                longitude,
                API_KEY,
                "metric"
        );

        currentWeatherCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                handleNetworkError(t);
            }
        });


        Call<ForecastResponse> forecastCall = weatherService.getForecast(
                latitude,
                longitude,
                API_KEY,
                "metric",
                "minutely,alerts"
        );


        forecastCall.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.body() != null) {
                    ForecastResponse forecast = response.body();
                    Log.d(TAG, "Hourly data: " + forecast.getHourly());
                    Log.d(TAG, "Daily data: " + forecast.getDaily());
                    updateForecastUI(forecast);
                }

                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d(TAG, "Forecast response: " + jsonResponse);
                    updateForecastUI(response.body());
                } else {
                    handleApiError(response);
                }
            }



            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                handleNetworkError(t);
            }
        });
    }
    @SuppressLint("DefaultLocale")
    private void updateUI(WeatherResponse weather) {
        cityNameText.setText(weather.getName());
        temperatureText.setText(String.format("%.0f°", weather.getMain().getTemp()));
        weatherConditionText.setText(weather.getWeather().get(0).getMain());
        highLowText.setText(String.format(
                "H:%.0f° L:%.0f°",
                weather.getMain().getTempMax(),
                weather.getMain().getTempMin()
        ));
        humidityText.setText(String.format("%d%%", weather.getMain().getHumidity()));
        windSpeedText.setText(String.format("%.1f km/h", weather.getWind().getSpeed()));

        updateBackground(weather.getWeather().get(0).getMain());
    }

    private void updateBackground(String weatherCondition) {
        int backgroundRes;
        switch (weatherCondition.toLowerCase()) {
            case "clear":
                backgroundRes = R.drawable.bg_sunny;
                break;
            case "rain":
                backgroundRes = R.drawable.bg_rainy;
                break;
            case "clouds":
                backgroundRes = R.drawable.bg_cloudy;
                break;
            default:
                backgroundRes = R.drawable.bg_default;
                break;
        }
        mainCard.setBackgroundResource(backgroundRes);
    }

    private void updateForecastUI(ForecastResponse forecast) {
        StringBuilder hourlyBuilder = new StringBuilder();
        StringBuilder dailyBuilder = new StringBuilder();

        if (forecast.getHourly() != null && !forecast.getHourly().isEmpty()) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            for (int i = 0; i < Math.min(24, forecast.getHourly().size()); i++) {
                ForecastResponse.Hourly hourly = forecast.getHourly().get(i);
                String time = timeFormat.format(new Date(hourly.getDt() * 1000L));
                String weather = !hourly.getWeather().isEmpty() ?
                        hourly.getWeather().get(0).getMain() : "N/A";
                hourlyBuilder.append(String.format(Locale.getDefault(),
                        "%s   %s   %.1f°C\n", time, weather, hourly.getTemp()));
            }
        } else {
            hourlyBuilder.append("No hourly forecast available\n");
        }

        if (forecast.getDaily() != null && !forecast.getDaily().isEmpty()) {
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE, MMM dd", Locale.getDefault());
            for (int i = 0; i < Math.min(7, forecast.getDaily().size()); i++) {
                ForecastResponse.Daily daily = forecast.getDaily().get(i);
                String day = dayFormat.format(new Date(daily.getDt() * 1000L));
                String weather = !daily.getWeather().isEmpty() ?
                        daily.getWeather().get(0).getMain() : "N/A";
                dailyBuilder.append(String.format(Locale.getDefault(),
                        "%s   %s   H:%.1f°C L:%.1f°C\n",
                        day, weather, daily.getTemp().getMax(), daily.getTemp().getMin()));
            }
        } else {
            dailyBuilder.append("No daily forecast available\n");
        }

        weeklyForecastText.setText(dailyBuilder.toString());
        hourlyForecastText.setText(hourlyBuilder.toString());
    }

    private void handleApiError(Response<?> response) {
        String errorMessage;
        try {
            if (response.errorBody() != null) {
                JSONObject errorBody = new JSONObject(response.errorBody().string());
                errorMessage = errorBody.optString("message", "Unknown error occurred");
            } else {
                errorMessage = "Error: " + response.code() + " " + response.message();
            }
        } catch (Exception e) {
            errorMessage = "Error: " + response.code() + " " + response.message();
        }
        Log.e(TAG, errorMessage);
        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void handleNetworkError(Throwable t) {
        String errorMessage = "Network error: " + t.getMessage();
        Log.e(TAG, "Failure: " + t.getMessage());
        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}