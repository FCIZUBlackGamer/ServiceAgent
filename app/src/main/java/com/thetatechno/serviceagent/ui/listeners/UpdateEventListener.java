package com.thetatechno.serviceagent.ui.listeners;


public interface UpdateEventListener {
     String action = "";

     void checkInPatient();
     void confirmArrived();
     void callPatient();
     void checkOutPatient();

}
