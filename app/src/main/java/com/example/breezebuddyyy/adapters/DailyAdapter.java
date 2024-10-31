
// DailyAdapter.java
package com.example.breezebuddyyy.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.breezebuddyyy.R;
import com.example.breezebuddyyy.models.DailyWeather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyAdapter extends RecyclerView.Adapter<com.example.breezebuddyyy.adapters.DailyAdapter.DailyViewHolder> {
    private final List<DailyWeather> dailyList;
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());

    public DailyAdapter(List<DailyWeather> dailyList) {
        this.dailyList = dailyList;
    }

    @NonNull
    @Override
    public com.example.breezebuddyyy.adapters.DailyAdapter.DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily, parent, false);
        return new com.example.breezebuddyyy.adapters.DailyAdapter.DailyViewHolder(view);
    }



    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull com.example.breezebuddyyy.adapters.DailyAdapter.DailyViewHolder holder, int position) {
        DailyWeather daily = dailyList.get(position);

        holder.dayText.setText(dayFormat.format(new Date(daily.getDt() * 1000)));
        holder.highText.setText(String.format("%.0f°", daily.getTemp().getMax()));
        holder.lowText.setText(String.format("%.0f°", daily.getTemp().getMin()));
        holder.precipText.setText(String.format("%.0f%%", daily.getPop() * 100));

        // Set weather icon
        String condition = daily.getWeather().get(0).getMain().toLowerCase();
        int iconRes = getWeatherIcon(condition);
        holder.weatherIcon.setImageResource(iconRes);
    }

    @Override
    public int getItemCount() {
        return Math.min(dailyList.size(), 3); // Show only 3 days
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
            default:
                return R.drawable.ic_clear;
        }
    }

    static class DailyViewHolder extends RecyclerView.ViewHolder {
        TextView dayText;
        TextView highText;
        TextView lowText;
        TextView precipText;
        ImageView weatherIcon;

        DailyViewHolder(View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayText);
            highText = itemView.findViewById(R.id.highText);
            lowText = itemView.findViewById(R.id.lowText);
            precipText = itemView.findViewById(R.id.precipText);
            weatherIcon = itemView.findViewById(R.id.weatherIcon);
        }
    }
}