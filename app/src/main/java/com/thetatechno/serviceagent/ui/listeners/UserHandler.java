package com.thetatechno.serviceagent.ui.listeners;


import com.thetatechno.serviceagent.model.pojo.StatusModel;
import com.thetatechno.serviceagent.model.pojo.UserData;

public interface UserHandler {
    public void onUserAddedHandler(StatusModel userId);
    public void onCounterIdReturnedHandler(UserData userId);
}
