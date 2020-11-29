package com.thetatechno.serviceagent.model.pojo;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appointement implements Parcelable {

    @SerializedName("MRN")
    @Expose
    private String mRN;
    @SerializedName("apptId")
    @Expose
    private Integer apptId;
    @SerializedName("arabicName")
    @Expose
    private String arabicName;
    @SerializedName("englishName")
    @Expose
    private String englishName;
    @SerializedName("sexCode")
    @Expose
    private String sexCode;
    @SerializedName("expected_time")
    @Expose
    private String expectedTime ="";
    @SerializedName("arrivalTime")
    @Expose
    private String arrivalTime ="";
    @SerializedName("checkinTime")
    @Expose
    private String checkinTime ="";
    @SerializedName("callingTime")
    @Expose
    private String callingTime = "";
    @SerializedName("slot_id")
    @Expose
    private String slotId;
    @SerializedName("image_path")
    @Expose
    private String imagePath = "";

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @SerializedName("is_followup")
    @Expose
    private String isFollowup;
    @SerializedName("activeBooking")
    @Expose
    private String activeBooking;

    public String getmRN() {
        return mRN;
    }

    public void setmRN(String mRN) {
        this.mRN = mRN;
    }

    public void setActiveBooking(String activeBooking) {
        this.activeBooking = activeBooking;
    }



    public String getActiveBooking() {
        return activeBooking;
    }

    public Appointement(Parcel in) {
        mRN = in.readString();
        apptId = in.readInt();
        arabicName = in.readString();
        englishName = in.readString();
        sexCode = in.readString();
        expectedTime = in.readString();
        arrivalTime = in.readString();
        callingTime = in.readString();
        slotId = in.readString();
        imagePath = in.readString();
        isFollowup = in.readString();
        activeBooking = in.readString();
    }

    public Appointement(){

    }
    public static final Creator<Appointement> CREATOR = new Creator<Appointement>() {
        @Override
        public Appointement createFromParcel(Parcel in) {
            return new Appointement(in);
        }

        @Override
        public Appointement[] newArray(int size) {
            return new Appointement[size];
        }
    };

    public String getMRN() {
        return mRN;
    }

    public void setMRN(String mRN) {
        this.mRN = mRN;
    }

    public String getArabicName() {
        return arabicName;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getSexCode() {
        return sexCode;
    }

    public void setSexCode(String sexCode) {
        this.sexCode = sexCode;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getCallingTime() {
        return callingTime;
    }

    public void setCallingTime(String callingTime) {
        this.callingTime = callingTime;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getIsFollowup() {
        return isFollowup;
    }

    public void setIsFollowup(String isFollowup) {
        this.isFollowup = isFollowup;
    }


    public void setApptId(Integer apptId) {
        this.apptId = apptId;
    }

    public Integer getApptId() {
        return apptId;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getCheckinTime() {
        return checkinTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRN);
        dest.writeInt(apptId);
        dest.writeString(arabicName);
        dest.writeString(englishName);
        dest.writeString(sexCode);
        dest.writeString(expectedTime);
        dest.writeString(arrivalTime);
        dest.writeString(callingTime);
        dest.writeString(slotId);
        dest.writeString(imagePath);
        dest.writeString(isFollowup);
        dest.writeString(activeBooking);
    }
}