package com.thetatechno.serviceagent.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnedStatus {
    @SerializedName("apptId")
    @Expose
    private Integer apptId;

    @SerializedName("error")
    @Expose
    private Error error;

    public void setError(Error error) {
        this.error = error;
    }

    public void setApptId(Integer apptId) {
        this.apptId = apptId;
    }

    public Error getError() {
        return error;
    }

    public Integer getApptId() {
        return apptId;
    }
}
