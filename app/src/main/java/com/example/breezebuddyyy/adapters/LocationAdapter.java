// LocationAdapter.java
package com.example.breezebuddyyy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.breezebuddyyy.R;
import com.example.breezebuddyyy.models.LocationResult;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private List<LocationResult> locations;
    private final OnLocationClickListener listener;

    public interface OnLocationClickListener {
        void onLocationClick(double lat, double lon, String cityName);
    }

    public LocationAdapter(List<LocationResult> locations, OnLocationClickListener listener) {
        this.locations = locations;
        this.listener = listener;
    }

    public void updateLocations(List<LocationResult> newLocations) {
        this.locations = newLocations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationResult location = locations.get(position);
        holder.locationName.setText(location.getName());
        holder.locationDetails.setText(String.format("%s, %s", location.getState(), location.getCountry()));

        holder.itemView.setOnClickListener(v ->
                listener.onLocationClick(location.getLat(), location.getLon(), location.getName())
        );
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationName;
        TextView locationDetails;

        ViewHolder(View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.locationName);
            locationDetails = itemView.findViewById(R.id.locationDetails);
        }
    }
}