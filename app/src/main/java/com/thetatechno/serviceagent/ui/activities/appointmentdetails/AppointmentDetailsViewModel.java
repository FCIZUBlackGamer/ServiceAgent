package com.thetatechno.serviceagent.ui.activities.appointmentdetails;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.thetatechno.serviceagent.utils.StringUtil;


public class AppointmentDetailsViewModel {

    public String capitalizeName(String name) {
       return StringUtil.toCamelCase(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String displayTime(String time){
      return  StringUtil.displayTime(time);
    }


}
