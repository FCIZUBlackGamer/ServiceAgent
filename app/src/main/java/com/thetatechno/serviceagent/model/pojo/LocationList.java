package com.thetatechno.serviceagent.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LocationList {
    @SerializedName("error")
    @Expose
    private Error error;

    @SerializedName("items")
    @Expose
    private ArrayList<CurrentLocation> items = null;

    public ArrayList<CurrentLocation> getItems() {
        return items;
    }

    public void setItems(ArrayList<CurrentLocation> currentLocations) {
        this.items = currentLocations;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
