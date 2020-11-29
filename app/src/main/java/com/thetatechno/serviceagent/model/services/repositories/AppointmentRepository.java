package com.thetatechno.serviceagent.model.services.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.thetatechno.serviceagent.model.pojo.Appointement;
import com.thetatechno.serviceagent.model.pojo.AppointmentItems;
import com.thetatechno.serviceagent.model.pojo.ReturnedStatus;
import com.thetatechno.serviceagent.model.pojo.StartServiceResponse;
import com.thetatechno.serviceagent.model.services.interfaces.MyServicesInterface;
import com.thetatechno.serviceagent.model.services.interfaces.RetrofitInstance;
import com.thetatechno.serviceagent.ui.listeners.OnDataChangedCallBackListener;
import com.thetatechno.serviceagent.utils.App;
import com.thetatechno.serviceagent.utils.Constants;
import com.thetatechno.serviceagent.utils.PreferenceController;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentRepository {
    List<Appointement> myItemList = new ArrayList<>();
    boolean responseReturned = true;
    boolean failureOnResponse = false;
    private MutableLiveData<AppointmentItems> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> numberOfUnArrivedCustomers = new MutableLiveData<>();
    AppointmentItems appointmentItems;
    public MutableLiveData getAppointmentListData(String sessionId, String langId) {
        myItemList = new ArrayList<>();
        MyServicesInterface myServicesInterface = (MyServicesInterface) RetrofitInstance.getService();
        Call<AppointmentItems> call = myServicesInterface.getAppointmentListData(sessionId, langId);
        call.enqueue(new Callback<AppointmentItems>() {
            @Override
            public void onResponse(Call<AppointmentItems> call, Response<AppointmentItems> response) {
                if (response.isSuccessful()) {
                    System.out.println("*********************** on response ********************");
                    appointmentItems = response.body();
                    if(appointmentItems != null ) {
                        myItemList = (ArrayList<Appointement>) appointmentItems.getItems();
                        if(appointmentItems.getItems()!=null)
                            for (Appointement item : myItemList) {
                                Log.i("appointmentItems", "  ************** item arrival time ************   " + item.getArrivalTime() + "slot " + item.getSlotId());
                                Log.i("appointmentItems", "  ************** item apptId ************   " + item.getArrivalTime() + "slot " + item.getApptId());
                                Log.i("appointmentItems", "  ************** item calling time ************   " + item.getCallingTime());
                                Log.i("appointmentItems", "  ************** item expected time ************   " + item.getExpectedTime());

                            }
                        //TODO: For testing
                        mutableLiveData.setValue(appointmentItems);
                    }
                } else {
                    Log.i("appointmentItems","no access to resources");
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AppointmentItems> call, Throwable t) {
                call.cancel();
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData getProviderAppointment(String doctorCode) {
        myItemList = new ArrayList<>();
        MyServicesInterface myServicesInterface = (MyServicesInterface) RetrofitInstance.getService();
        Call<AppointmentItems> call = myServicesInterface.getProviderAppointment(doctorCode);
        call.enqueue(new Callback<AppointmentItems>() {
            @Override
            public void onResponse(Call<AppointmentItems> call, Response<AppointmentItems> response) {
                if (response.isSuccessful()) {
                    System.out.println("*********************** on response ********************");
                    appointmentItems = response.body();
                    if(appointmentItems != null ) {
                        myItemList = (ArrayList<Appointement>) appointmentItems.getItems();
                        if(appointmentItems.getItems()!=null)
                            for (Appointement item : myItemList) {
                                Log.i("appointmentItems", "  ************** item arrival time ************   " + item.getArrivalTime() + "slot " + item.getSlotId());
                                Log.i("appointmentItems", "  ************** item apptId ************   " + item.getArrivalTime() + "slot " + item.getApptId());
                                Log.i("appointmentItems", "  ************** item calling time ************   " + item.getCallingTime());
                                Log.i("appointmentItems", "  ************** item expected time ************   " + item.getExpectedTime());

                            }
                        //TODO: For testing
                        mutableLiveData.setValue(appointmentItems);
                    }
                } else {
                    Log.i("appointmentItems","no access to resources");
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AppointmentItems> call, Throwable t) {
                call.cancel();
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public void callPatient(String sessionId,String providerId,String facilityId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = (MyServicesInterface) RetrofitInstance.getService();
        Call<ReturnedStatus> call = myServicesInterface.callPatient(sessionId, providerId, facilityId, PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());

        call.enqueue(new Callback<ReturnedStatus>() {

            @Override
            public void onResponse(Call<ReturnedStatus> call, Response<ReturnedStatus> response) {
                if (response.body() != null && response.body().getError() != null && response.body().getError().getErrorCode() == 0) {
                    if(response.body()!= null) {
                        Log.i("AppointmentListFragment", "response from repo: " + response.toString());
                        onDataChangedCallBackListener.onResponse(response.body());

                    }else {
                        onDataChangedCallBackListener.onResponse(null);
                    }
                }

            }

            @Override
            public void onFailure(Call<ReturnedStatus> call, Throwable t) {

                onDataChangedCallBackListener.onResponse(null);
                call.cancel();
            }
        });
    }

    public void checkIn(String slotId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = (MyServicesInterface) RetrofitInstance.getService();
        Call<StartServiceResponse> call = myServicesInterface.checkIn(slotId, PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());

        call.enqueue(new Callback<StartServiceResponse>() {
            @Override
            public void onResponse(Call<StartServiceResponse> call, Response<StartServiceResponse> response) {
                if (response.body() != null && response.body().getError() != null && response.body().getError().getErrorCode() == 0)
                    onDataChangedCallBackListener.onResponse(responseReturned);
                else
                    onDataChangedCallBackListener.onResponse(failureOnResponse);

            }


            @Override
            public void onFailure(Call<StartServiceResponse> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(failureOnResponse);
            }
        });
    }

    public void checkOut(String slotId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {
        MyServicesInterface myServicesInterface = (MyServicesInterface) RetrofitInstance.getService();
        Call<ResponseBody> call = myServicesInterface.checkout(slotId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == Constants.STATE_OK)
                    onDataChangedCallBackListener.onResponse(responseReturned);
                else
                    onDataChangedCallBackListener.onResponse(failureOnResponse);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(failureOnResponse);
            }
        });


    }

    public void confirmArrive(String slotId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {
        MyServicesInterface myServicesInterface = (MyServicesInterface) RetrofitInstance.getService();
        Call<ResponseBody> call = myServicesInterface.confirmArrive(slotId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == Constants.STATE_OK)
                    onDataChangedCallBackListener.onResponse(responseReturned);
                else
                    onDataChangedCallBackListener.onResponse(failureOnResponse);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(failureOnResponse);
            }
        });
    }

    int returnNumber;
    // For testing
    public MutableLiveData getUnArrivedNumber(String locationCode, String langId){

        MyServicesInterface myServicesInterface = (MyServicesInterface) RetrofitInstance.getService();
        Call<AppointmentItems> call = myServicesInterface.getAppointmentListData(locationCode, langId);
        call.enqueue(new Callback<AppointmentItems>() {
            @Override
            public void onResponse(Call<AppointmentItems> call, Response<AppointmentItems> response) {
                if (response.isSuccessful()) {
                    System.out.println("*********************** on response in get number arrived ********************");
                    appointmentItems = response.body();
                    if(appointmentItems != null ) {
                        if(appointmentItems.getItems()!=null) {
                            for (int i = 0; i < appointmentItems.getItems().size(); i++) {
                                if (appointmentItems.getItems().get(i).getArrivalTime().isEmpty()) {
                                    myItemList.add(appointmentItems.getItems().get(i));
                                }
                            }
                            returnNumber = myItemList.size();
                            numberOfUnArrivedCustomers.setValue(returnNumber);
                        }

                    }
                } else {

                    returnNumber = 0;
                    numberOfUnArrivedCustomers.setValue(returnNumber);
                }
            }

            @Override
            public void onFailure(Call<AppointmentItems> call, Throwable t) {
                call.cancel();
            }
        });
        return numberOfUnArrivedCustomers;
    }



}
