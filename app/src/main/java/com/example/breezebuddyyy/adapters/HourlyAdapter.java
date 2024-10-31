package com.example.breezebuddyyy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.breezebuddyyy.R;
import com.example.breezebuddyyy.models.HourlyWeather;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder> {
    private List<HourlyWeather> hourlyList;
    private final SimpleDateFormat timeFormat;

    public HourlyAdapter(List<HourlyWeather> hourlyList) {
        this.hourlyList = hourlyList;
        this.timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly, parent, false);
        return new HourlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        HourlyWeather hourly = hourlyList.get(position);

        try {
            // Set time
            String formattedTime = timeFormat.format(new Date(hourly.getDt() * 1000L));
            holder.timeText.setText(formattedTime);

            // Set temperature
            holder.tempText.setText(String.format(Locale.getDefault(), "%.0f°", hourly.getTemp()));

            // Set weather icon
            if (hourly.getWeather() != null && !hourly.getWeather().isEmpty()) {
                String condition = hourly.getWeather().get(0).getMain().toLowerCase();
                int iconRes = getWeatherIcon(condition);
                holder.weatherIcon.setImageResource(iconRes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return hourlyList != null ? Math.min(hourlyList.size(), 24) : 0;
    }

    public void updateData(List<HourlyWeather> newHourlyList) {
        this.hourlyList = newHourlyList;
        notifyDataSetChanged();
    }

    private int getWeatherIcon(String condition) {
        switch (condition) {
            case "clear":
                return R.drawable.ic_clear;
            case "rain":
                return R.drawable.ic_rain;
            case "clouds":
                return R.drawable.ic_cloudy;
            case "snow":
                return R.drawable.ic_snow;
            case "thunderstorm":
                return R.drawable.ic_thunder;
            case "drizzle":
                return R.drawable.ic_rain;
            case "mist":
            case "fog":
                return R.drawable.ic_cloudy;
            default:
                return R.drawable.ic_clear;
        }
    }

    static class HourlyViewHolder extends RecyclerView.ViewHolder {
        final TextView timeText;
        final TextView tempText;
        final ImageView weatherIcon;

        HourlyViewHolder(View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.timeText);
            tempText = itemView.findViewById(R.id.tempText);
            weatherIcon = itemView.findViewById(R.id.weatherIcon);
        }
    }
}