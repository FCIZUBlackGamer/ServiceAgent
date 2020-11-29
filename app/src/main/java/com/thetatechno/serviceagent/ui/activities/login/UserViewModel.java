package com.thetatechno.serviceagent.ui.activities.login;


import com.thetatechno.serviceagent.model.pojo.StatusModel;
import com.thetatechno.serviceagent.model.pojo.User;
import com.thetatechno.serviceagent.model.pojo.UserData;
import com.thetatechno.serviceagent.model.services.repositories.UserRepository;
import com.thetatechno.serviceagent.ui.listeners.OnDataChangedCallBackListener;
import com.thetatechno.serviceagent.ui.listeners.UserHandler;
import com.thetatechno.serviceagent.utils.App;
import com.thetatechno.serviceagent.utils.PreferenceController;

public class UserViewModel {
    boolean isUserCreated = false;
    UserRepository userRepository = new UserRepository();

    public void createUser(final String email, String firstName, String familyName, final String imageProfile, final String displayName, final String token, final OnDataChangedCallBackListener onDataChangedCallBackListener) {
        final User user = new User(email, firstName, familyName, "F", token);

        userRepository.createNewUser(user, new UserHandler() {
            @Override
            public void onUserAddedHandler(StatusModel returnedStatus) {
                isUserCreated = checkOnReturnedStatus(returnedStatus);
                if (isUserCreated) {
                    saveDataInSharedPreference(email, displayName, imageProfile, 3385);

                }
                onDataChangedCallBackListener.onResponse(isUserCreated);
            }

            @Override
            public void onCounterIdReturnedHandler(UserData userId) {
            }
        });
    }

    public void getUserData(final String email, String langId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {
        userRepository.getUserData(email, langId, new UserHandler() {
            @Override
            public void onUserAddedHandler(StatusModel returnedStatus) {
            }

            @Override
            public void onCounterIdReturnedHandler(UserData userId) {
                PreferenceController.getInstance(App.getContext()).persist(PreferenceController.PREF_COUNTER_ID, userId.getData().get(0).getCounterId()+"");
                onDataChangedCallBackListener.onResponse(true);
            }
        });
    }

    public void getIpAndPortToCreateRetrofitInstance(final OnDataChangedCallBackListener<Boolean> onDataChangedCallBackListener) {
//        FirebaseDatabaseService.getPortAndIpAddress(new OnDataChangedCallBackListener<Boolean>() {
//            @Override
//            public void onResponse(Boolean dataChanged) {
//                onDataChangedCallBackListener.onResponse(dataChanged);
//            }
//        });
    }

    private void saveDataInSharedPreference(String email, String displayName, String imageProfile, int id) {
        PreferenceController.getInstance(App.getContext()).persist(PreferenceController.PREF_EMAIL, email);
        PreferenceController.getInstance(App.getContext()).persist(PreferenceController.PREF_USER_NAME, displayName);
        PreferenceController.getInstance(App.getContext()).persist(PreferenceController.PREF_IMAGE_PROFILE_URL, imageProfile);
        PreferenceController.getInstance(App.getContext()).persist(PreferenceController.PREF_USER_ID, String.valueOf(id));
    }

    public boolean checkOnReturnedStatus(StatusModel status) {
        if (status != null && status.getStatus().intValue() > 0) {
            return true;
        } else {
            return false;
        }

    }


}
