package com.thetatechno.serviceagent.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentLocation implements Parcelable {
    @SerializedName("sessionId")
    @Expose
    private String sessionId;

    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("serviceTypeDesc")
    @Expose
    private String serviceTypeDesc;

    protected CurrentLocation(Parcel in) {
        sessionId = in.readString();
        serviceTypeDesc = in.readString();
    }

    public static final Creator<CurrentLocation> CREATOR = new Creator<CurrentLocation>() {
        @Override
        public CurrentLocation createFromParcel(Parcel in) {
            return new CurrentLocation(in);
        }

        @Override
        public CurrentLocation[] newArray(int size) {
            return new CurrentLocation[size];
        }
    };


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getServiceTypeDesc() {
        return serviceTypeDesc;
    }

    public void setServiceTypeDesc(String serviceTypeDesc) {
        this.serviceTypeDesc = serviceTypeDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(sessionId);
        dest.writeString(serviceTypeDesc);
    }
}
