package com.thetatechno.serviceagent.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserData {

    @SerializedName("data")
    @Expose
    private List<UserDataModel> data;

    @SerializedName("error")
    @Expose
    private Error error;

    public void setData(List<UserDataModel> data) {
        this.data = data;
    }

    public List<UserDataModel> getData() {
        return data;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
