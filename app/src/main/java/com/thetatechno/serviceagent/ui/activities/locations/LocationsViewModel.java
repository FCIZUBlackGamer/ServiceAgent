package com.thetatechno.serviceagent.ui.activities.locations;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.serviceagent.model.pojo.FacilityList;
import com.thetatechno.serviceagent.model.services.repositories.LocationsRepository;
import com.thetatechno.serviceagent.utils.App;
import com.thetatechno.serviceagent.utils.PreferenceController;


public class LocationsViewModel extends ViewModel {
    LocationsRepository locationsRepository = new LocationsRepository();
    public MutableLiveData<FacilityList> getAllFacilities(String id){
        String langId = PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase();
        return locationsRepository.getAppointmentListData(id,langId);
    }
}
