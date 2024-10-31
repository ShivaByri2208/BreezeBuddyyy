// LocationSearchFragment.java
package com.example.breezebuddyyy.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.breezebuddyyy.R;
import com.example.breezebuddyyy.adapters.LocationAdapter;
import com.example.breezebuddyyy.models.LocationResult;
import com.example.breezebuddyyy.service.WeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;

public class LocationSearchFragment extends Fragment {
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final String API_KEY = "ad428086ac6054ba4beae05e30c92959"; // Use your API key

    private EditText searchEditText;
    private RecyclerView resultsRecyclerView;
    private LocationAdapter locationAdapter;
    private WeatherService weatherService;
    private OnLocationSelectedListener locationSelectedListener;

    public interface OnLocationSelectedListener {
        void onLocationSelected(double lat, double lon, String cityName);
    }

    public void setOnLocationSelectedListener(OnLocationSelectedListener listener) {
        this.locationSelectedListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupRetrofit();
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherService = retrofit.create(WeatherService.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchEditText = view.findViewById(R.id.searchEditText);
        resultsRecyclerView = view.findViewById(R.id.resultsRecyclerView);
        ImageButton backButton = view.findViewById(R.id.backButton);

        setupRecyclerView();
        setupSearchListener();

        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void setupRecyclerView() {
        locationAdapter = new LocationAdapter(new ArrayList<>(), (lat, lon, cityName) -> {
            if (locationSelectedListener != null) {
                locationSelectedListener.onLocationSelected(lat, lon, cityName);
            }
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        resultsRecyclerView.setAdapter(locationAdapter);
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    searchLocations(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchLocations(String query) {
        Call<List<LocationResult>> call = weatherService.searchLocation(
                query,
                5, // limit results
                API_KEY
        );

        call.enqueue(new Callback<List<LocationResult>>() {
            @Override
            public void onResponse(Call<List<LocationResult>> call, Response<List<LocationResult>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    locationAdapter.updateLocations(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<LocationResult>> call, Throwable t) {
                // Handle error
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error searching locations", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}