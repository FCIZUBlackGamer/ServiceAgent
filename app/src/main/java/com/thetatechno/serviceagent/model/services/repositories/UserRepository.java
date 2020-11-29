package com.thetatechno.serviceagent.model.services.repositories;


import com.thetatechno.serviceagent.model.pojo.LocationList;
import com.thetatechno.serviceagent.model.pojo.StatusModel;
import com.thetatechno.serviceagent.model.pojo.User;
import com.thetatechno.serviceagent.model.pojo.UserData;
import com.thetatechno.serviceagent.model.services.interfaces.MyServicesInterface;
import com.thetatechno.serviceagent.model.services.interfaces.RetrofitInstance;
import com.thetatechno.serviceagent.ui.listeners.OnDataChangedCallBackListener;
import com.thetatechno.serviceagent.ui.listeners.UserHandler;
import com.thetatechno.serviceagent.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRepository {
    //TODO: call sign in method
    boolean isUserCreated = false;
    StatusModel status = null;
    UserData userStatus = null;
    LocationList locationList = new LocationList();

    public void createNewUser(final User user, final UserHandler userHandler) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<StatusModel> call = myServicesInterface.createUser(user);
        call.enqueue(new Callback<StatusModel>() {

            @Override
            public void onResponse(Call<StatusModel> call, Response<StatusModel> response) {
                if (response.code() == Constants.STATE_OK) {

                    status = response.body();
                    userHandler.onUserAddedHandler(status);
                } else {
                    userHandler.onUserAddedHandler(status);
                }
            }

            @Override
            public void onFailure(Call<StatusModel> call, Throwable t) {
                userHandler.onUserAddedHandler(status);
                t.printStackTrace();

            }

        });

    }

    public void getUserData(final String email, final String langId, final UserHandler userHandler) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<UserData> call = myServicesInterface.getStaff(email, langId);
        call.enqueue(new Callback<UserData>() {

            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.code() == Constants.STATE_OK) {
                    userStatus = response.body();
                    userHandler.onCounterIdReturnedHandler(userStatus);
                } else {
                    userHandler.onCounterIdReturnedHandler(userStatus);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                userHandler.onCounterIdReturnedHandler(userStatus);
                t.printStackTrace();

            }

        });

    }

    public void getLocationList(final String counterId, String langId, final OnDataChangedCallBackListener<LocationList> onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<LocationList> call = myServicesInterface.getCurrentRunningLocationList(counterId, langId);
        call.enqueue(new Callback<LocationList>() {

            @Override
            public void onResponse(Call<LocationList> call, Response<LocationList> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    locationList = response.body();
                    onDataChangedCallBackListener.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<LocationList> call, Throwable t) {
                t.printStackTrace();
                locationList = null;
                onDataChangedCallBackListener.onResponse(locationList);


            }

        });

    }


}
