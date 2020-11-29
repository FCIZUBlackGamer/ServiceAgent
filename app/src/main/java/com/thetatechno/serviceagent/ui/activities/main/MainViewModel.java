package com.thetatechno.serviceagent.ui.activities.main;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.serviceagent.model.pojo.CurrentLocation;
import com.thetatechno.serviceagent.model.pojo.LocationList;
import com.thetatechno.serviceagent.model.services.repositories.AppointmentRepository;
import com.thetatechno.serviceagent.model.services.repositories.UserRepository;
import com.thetatechno.serviceagent.ui.listeners.OnDataChangedCallBackListener;
import com.thetatechno.serviceagent.utils.App;
import com.thetatechno.serviceagent.utils.PreferenceController;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainAgentActivity";
    UserRepository userRepository = new UserRepository();
    AppointmentRepository appointmentRepository = new AppointmentRepository();
    private MutableLiveData<String> fullNameLivedata;
    private MutableLiveData<String> emailLiveData;
    private MutableLiveData<String> imageUrlLiveData;
    private MutableLiveData<Integer> numOfUnarrivedCustomersLiveData;

    public void clearDataFromSharedPreference() {
        PreferenceController.getInstance(App.getContext()).clear(PreferenceController.PREF_EMAIL);
        PreferenceController.getInstance(App.getContext()).clear(PreferenceController.PREF_IMAGE_PROFILE_URL);
        PreferenceController.getInstance(App.getContext()).clear(PreferenceController.PREF_USER_NAME);
        PreferenceController.getInstance(App.getContext()).clear(PreferenceController.PREF_USER_ID);

    }

    public MutableLiveData<String> getFullName() {
        if (fullNameLivedata == null) {
            fullNameLivedata = new MutableLiveData<String>();
        }
        String name = PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_USER_NAME);
        fullNameLivedata.setValue(name);
        return fullNameLivedata;
    }

    public MutableLiveData<String> getEmail() {
        if (emailLiveData == null) {
            emailLiveData = new MutableLiveData<String>();
        }
        String email = PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_EMAIL);
        emailLiveData.setValue(email);
        return emailLiveData;
    }

    public MutableLiveData<String> getImageUrl() {
        if (imageUrlLiveData == null) {
            imageUrlLiveData = new MutableLiveData<String>();
        }
        String imageUrl = PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_IMAGE_PROFILE_URL);
        imageUrlLiveData.setValue(imageUrl);
        return imageUrlLiveData;
    }

    public String getDataFromSharedPreference(String key) {
        return PreferenceController.getInstance(App.getContext()).get(key);
    }

    public void getLocationData(String counterId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {
        userRepository.getLocationList(counterId,PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), new OnDataChangedCallBackListener<LocationList>() {
            @Override
            public void onResponse(LocationList dataChanged) {
                if (dataChanged != null && dataChanged.getItems() != null) {
                    for (CurrentLocation currentLocation : dataChanged.getItems()) {
                        Log.i(TAG, "facility id : " + currentLocation.getServiceTypeDesc());
                        Log.i(TAG, "session id " + currentLocation.getSessionId());
                    }
                }
                onDataChangedCallBackListener.onResponse(dataChanged);
            }
        });

    }

    public MutableLiveData getNumOfUnArrivedData(String locationCode, String langId) {
        return appointmentRepository.getUnArrivedNumber(locationCode, langId);

    }

}
