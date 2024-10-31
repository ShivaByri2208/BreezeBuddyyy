package com.example.breezebuddyyy.models;

import com.google.gson.annotations.SerializedName;

public class WeatherInfo {
    @SerializedName("main")
    private String main;

    @SerializedName("description")
    private String description;

    @SerializedName("icon")
    private String icon;

    // Getters and setters
    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
