package com.thetatechno.serviceagent.model.services.interfaces;



import com.thetatechno.serviceagent.model.pojo.AppointmentItems;
import com.thetatechno.serviceagent.model.pojo.FacilityList;
import com.thetatechno.serviceagent.model.pojo.LocationList;
import com.thetatechno.serviceagent.model.pojo.ReturnedStatus;
import com.thetatechno.serviceagent.model.pojo.StartServiceResponse;
import com.thetatechno.serviceagent.model.pojo.StatusModel;
import com.thetatechno.serviceagent.model.pojo.User;
import com.thetatechno.serviceagent.model.pojo.UserData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyServicesInterface {
    @GET("/ords/fluidgp/appt/getSessionAppointments")
    Call<AppointmentItems> getAppointmentListData(@Query("sessionId") String sessionId, @Query("langId") String langId);

    @GET("/ords/fluid/api/getClinicSchedule/{doctor_code}")
    Call<AppointmentItems> getProviderAppointment(@Path("doctor_code") String doctorCode);

    @PUT("/ords/fluidgp/q/callNext")
    Call<ReturnedStatus> callPatient(@Query("sessionId") String sessionId, @Query("providerId") String providerId, @Query("facilityId") String facilityId, @Query("langId") String langId);

    @PUT("/ords/fluidgp/q/startService")
    Call<StartServiceResponse> checkIn(@Query("slotId") String slotId, @Query("langId") String langId);

    @PUT("/ords/fluidgp/api/checkout/{slot_id}")
    Call<ResponseBody> checkout(@Path("slot_id") String slotId);

    @PUT("/ords/fluid/api/arrive/{slot_id}")
    Call<ResponseBody> confirmArrive(@Path("slot_id") String slotId);

    @POST("/ords/fluid/api/addUser")
    Call<StatusModel> createUser(@Body User user);

    @GET("/ords/fluidgp/stff/getStaff")
    Call<UserData> getStaff(@Query("email") String email, @Query("langId") String langId);

    @GET("/ords/fluidgp/fclty/getCounterSessions")
    Call<LocationList> getCurrentRunningLocationList(@Query("counterId") String counterId, @Query("langId") String langId);

    @GET("/ords/fluid/api/getAgentFacilities/{id}")
    Call<FacilityList> getFacilities(@Path("id") String id, @Query("langId") String langId);
}
