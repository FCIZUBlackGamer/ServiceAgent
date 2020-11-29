package com.thetatechno.serviceagent.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.serviceagent.model.pojo.AppointmentItems;
import com.thetatechno.serviceagent.model.pojo.ReturnedStatus;
import com.thetatechno.serviceagent.model.services.repositories.AppointmentRepository;
import com.thetatechno.serviceagent.ui.listeners.OnDataChangedCallBackListener;


public class HomeViewModel extends ViewModel {
    AppointmentRepository appointmentRepository = new AppointmentRepository();
    MutableLiveData myliveData = new MutableLiveData();
    public LiveData<AppointmentItems> getAllItems(String sessionId, String langId){
        myliveData = appointmentRepository.getAppointmentListData(sessionId, langId);
        return myliveData;
    }
    public LiveData<AppointmentItems> getAllProviderItems(String doctorCode){
        myliveData = appointmentRepository.getProviderAppointment(doctorCode);
        return myliveData;
    }
    public void updateWithCalling(final String sessionId,final String providerId,final String facilityId, final OnDataChangedCallBackListener<Integer> onDataChangedCallBackListener){
        appointmentRepository.callPatient(sessionId, providerId, facilityId, new OnDataChangedCallBackListener<ReturnedStatus>() {
            @Override
            public void onResponse(ReturnedStatus status) {
                Log.i("AppointmentListFragment","status return from call "+ status.getApptId().intValue());
                onDataChangedCallBackListener.onResponse(status.getApptId());

            }
        });

    }
    public void updateWithCheckIn(String slotId, final OnDataChangedCallBackListener<Boolean> onDataChangedCallBackListener){
        appointmentRepository.checkIn(slotId, new OnDataChangedCallBackListener<Boolean>() {
            @Override
            public void onResponse(Boolean dataChanged) {
                    onDataChangedCallBackListener.onResponse(dataChanged);
            }
        });
    }
    public void updateWithCheckOut( String slotId,final OnDataChangedCallBackListener<Boolean> onDataChangedCallBackListener){
        appointmentRepository.checkOut(slotId,new OnDataChangedCallBackListener<Boolean>(){
            @Override
            public void onResponse(Boolean dataChanged) {
                onDataChangedCallBackListener.onResponse(dataChanged);
            }
        });
    }
    public void confirmArrival(String slotId, final OnDataChangedCallBackListener<Boolean> onDataChangedCallBackListener){
        appointmentRepository.confirmArrive(slotId, new OnDataChangedCallBackListener<Boolean>() {
            @Override
            public void onResponse(Boolean dataChanged) {
                onDataChangedCallBackListener.onResponse(dataChanged);
            }
        });

    }
}