package com.thetatechno.serviceagent.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppointmentItems {
    @SerializedName("error")
    @Expose
    private Error error;

    @SerializedName("items")
    @Expose
    private List<Appointement> items = null;

    public List<Appointement> getItems() {
        return items;
    }

    public void setItems(List<Appointement> items) {
        this.items = items;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
