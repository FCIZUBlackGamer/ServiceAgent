package com.thetatechno.serviceagent.ui.activities.main;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.thetatechno.serviceagent.R;
import com.thetatechno.serviceagent.model.pojo.CurrentLocation;
import com.thetatechno.serviceagent.model.pojo.LocationList;
import com.thetatechno.serviceagent.ui.activities.BaseActivity;
import com.thetatechno.serviceagent.ui.activities.NoInternetConnectionActivity;
import com.thetatechno.serviceagent.ui.activities.locations.LocationsActivity;
import com.thetatechno.serviceagent.ui.activities.login.LoginActivity;
import com.thetatechno.serviceagent.ui.adapters.ViewPagerAdapter;
import com.thetatechno.serviceagent.ui.home.HomeAgentFragment;
import com.thetatechno.serviceagent.ui.listeners.OnDataChangedCallBackListener;
import com.thetatechno.serviceagent.ui.listeners.OnUpdateDataEvent;
import com.thetatechno.serviceagent.utils.CheckForNetwork;
import com.thetatechno.serviceagent.utils.Constants;
import com.thetatechno.serviceagent.utils.PreferenceController;
import com.thetatechno.serviceagent.utils.SaveLogFile;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class MainAgentActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, HomeAgentFragment.OnFragmentInteractionListener {
    OnUpdateDataEvent listener = new OnUpdateDataEvent();
    private ArrayList<CurrentLocation> currentLocationList = new ArrayList<>();
    boolean isAppointmentStarted = true;
    private FloatingActionButton startOrEndFab, callFab, arrivalFab;
    private Toolbar toolbar;
    private ViewPager2 mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;
    private ProgressBar mProgressBar;
    DrawerLayout drawer;
    NavigationView navigationView;
    GoogleSignInClient mGoogleSignInClient;
    TextView tabTitle;
    TextView tabCount;
    View customTabView;
    // user data UI
    ImageView mImageView;
    TextView mEmailTxtView;
    TextView mUserNameTxtView;
    ConstraintLayout noLocationAvailableConstraintLayout;
    ConstraintLayout noServerLayout;
    MainViewModel mainViewModel;
    Button retryButton;
    AlertDialog alertDialog;
    private static final int DRAWABLE_RESOURCE_FOR_START_STATE = R.drawable.animation_fab_finish;
    private static final int COLOR_ID_FOR_START_STATE = R.color.colorAccent;
    private static final int DRAWABLE_RESOURCE_FOR_FINISH_STATE = R.drawable.animation_fab_start;
    private static final int COLOR_ID_FOR_FINISH_STATE = R.color.colorPrimary;
    public static final String TAG = "MainAgentActivity";
    private int numOfCalls;
    // The Idling Resource which will be null in production.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        if (savedInstanceState != null) {
            currentLocationList = savedInstanceState.getParcelableArrayList("locationList");

        }
        setContentView(R.layout.activity_main);
        SaveLogFile saveLogFile = new SaveLogFile();
        saveLogFile.createLogFilesToSaveLogcatException();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.request_id_token))
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        initializeViews();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkOnTheCurrentLanguage();
        View header = navigationView.getHeaderView(0);
        mImageView = header.findViewById(R.id.userImageView);
        mUserNameTxtView = header.findViewById(R.id.userNameTxt);
        mEmailTxtView = header.findViewById(R.id.emailTxtView);
        displayUserInfo();
        mProgressBar.setVisibility(View.VISIBLE);
        hideButtonAndTabLayout();
        callFab.setOnClickListener(v -> {

            if (CheckForNetwork.isConnectionOn(MainAgentActivity.this)) {
                Log.i(TAG, "call button clicked");
                if (isAppointmentStarted) {
                    getCurrentFragment();
                    listener.setAction(Constants.ACTION_CALL);
                    EventBus.getDefault().post(listener);

                } else {
                    showAlertWithMessage(getResources().getString(R.string.error_call_when_there_is_customer_checked_in));
                }
            } else {
                redirectToNoInternetConnection();
            }
        });
        arrivalFab.setOnClickListener(v -> {

            Log.i(TAG, " arrive button clicked");
            if (CheckForNetwork.isConnectionOn(MainAgentActivity.this)) {
                getCurrentFragment();
                listener.setAction(Constants.ACTION_ARRIVE);
                EventBus.getDefault().post(listener);
            } else {
                redirectToNoInternetConnection();
            }

        });
        startOrEndFab.setOnClickListener(v -> {
            if (CheckForNetwork.isConnectionOn(MainAgentActivity.this)) {
                if (numOfCalls > 0) {
                    getCurrentFragment();
                    if (isAppointmentStarted) {
                        getCurrentFragment();
                        listener.setAction(Constants.ACTION_CHECK_IN);
                        EventBus.getDefault().post(listener);

                    } else {
                        listener.setAction(Constants.ACTION_CHECK_OUT);
                        EventBus.getDefault().post(listener);


                    }
                } else {
                    // TODO: alert with there is no customer called to be checked in.
                    showAlertWithMessage(getResources().getString(R.string.error_no_customer_called));
                }
            } else {
                redirectToNoInternetConnection();
            }
        });
        retryButton.setOnClickListener(v -> getAllLocations());
    }

    private void getCurrentFragment() {
        listener.setFacilityCode(currentLocationList.get(mViewPager.getCurrentItem()).getServiceTypeDesc());
        listener.setSessionId(currentLocationList.get(mViewPager.getCurrentItem()).getSessionId());
    }

    private void getAllLocations() {
        disableServerLayout();
        disableNoLocationLayout();

        if (!CheckForNetwork.isConnectionOn(this)) {
            redirectToNoInternetConnection();
        } else {
            mainViewModel.getLocationData(PreferenceController.getInstance(this).get(PreferenceController.PREF_COUNTER_ID), new OnDataChangedCallBackListener<LocationList>() {
                @Override
                public void onResponse(LocationList locationsList) {
                    mProgressBar.setVisibility(View.GONE);
                    if (locationsList != null) {
                        if (locationsList.getError().getErrorCode() == 0) {
                            disableNoLocationLayout();
                                currentLocationList = locationsList.getItems();
                                setupViewPager(mViewPager);

                        } else {
                            enableLayoutForNoLocations();
                            hideButtonAndTabLayout();

                        }
                    } else {
                        hideButtonAndTabLayout();
                        disableNoLocationLayout();
                        enableNoServerLayout();
                    }
                }
            });

        }
    }

    /** display data in navigation menu */
    private void displayUserInfo() {
        mainViewModel.getFullName().observe(this, name -> mUserNameTxtView.setText(name));
        mainViewModel.getEmail().observe(this, email -> mEmailTxtView.setText(email));
        mainViewModel.getImageUrl().observe(this, imgUrl -> {
            // TODO: use glide to update image view src
            Glide.with(MainAgentActivity.this)
                    .load(Constants.BASE_GOOGLE_URL_FOR_IMAGES + imgUrl)
                    .circleCrop()
                    .into(mImageView);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart method");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume method");
        getAllLocations();

    }

    public void checkOnTheCurrentLanguage() {
        if (PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ARABIC)) {
            navigationView.getMenu().findItem(R.id.language_reference).setTitle(R.string.menu_english_language);
        } else if (PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ENGLISH)) {
            navigationView.getMenu().findItem(R.id.language_reference).setTitle(R.string.menu_arabic_language);

        }
    }

    private void animateStartOrEndBtn(String state) {

        if (state == Constants.STARTING_STATE) {
            changeColorAndSrcOfStartAndEndButton(DRAWABLE_RESOURCE_FOR_START_STATE, COLOR_ID_FOR_START_STATE);
            startOrEndFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_start_to_finish_fab_with_rotation));

        } else if (state == Constants.ENDING_STATE) {
            changeColorAndSrcOfStartAndEndButton(DRAWABLE_RESOURCE_FOR_FINISH_STATE, COLOR_ID_FOR_FINISH_STATE);

            startOrEndFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_finish_to_start_fab_with_rotation));

        }

    }

    public void initializeViews() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        startOrEndFab = findViewById(R.id.start_fab);
        callFab = findViewById(R.id.call_fab);
        arrivalFab = findViewById(R.id.confirm_arrive_fab);
        mProgressBar = findViewById(R.id.loading_data_progress_bar);
        noLocationAvailableConstraintLayout = findViewById(R.id.noLocationAvaliable);
        retryButton = findViewById(R.id.retryBtn);
        noServerLayout = findViewById(R.id.serverErrorLayout);
    }

    private void setupViewPager(final ViewPager2 myViewPager) {
        mViewPagerAdapter = new ViewPagerAdapter(this, currentLocationList);
        myViewPager.setAdapter(mViewPagerAdapter);
        myViewPager.setOffscreenPageLimit(1);
        new TabLayoutMediator(mTabLayout, mViewPager,
                (tab, position) -> tab.setText(currentLocationList.get(position).getServiceTypeDesc())
        ).attach();
        for (int i = 0; i < currentLocationList.size(); i++) {
            if (mTabLayout.getTabAt(i).getCustomView() == null) {
                try {
                    if (currentLocationList.get(i).getCount() != null)
                        mTabLayout.getTabAt(i).setCustomView(updateTabTextView(i, Integer.parseInt(currentLocationList.get(i).getCount())));
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    Log.i(TAG, "No appointments here");
                }
            }
        }

    }

    private View updateTabTextView(int pos, final int listSize) {
        customTabView = getLayoutInflater().inflate(R.layout.location_tab, null);
        tabTitle = customTabView.findViewById(R.id.location_tab_txt_view);
        tabCount = customTabView.findViewById(R.id.new_notifications_for_list_size);
        tabTitle.setText(currentLocationList.get(pos).getServiceTypeDesc());
        if (listSize != 0) {
            tabCount.setText(listSize + "");
        } else {

            tabCount.setVisibility(View.GONE);
        }
        Log.i(TAG, "tab count " + tabCount.getText().toString());
        return customTabView;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.language_reference:
                changeLanguage((String) menuItem.getTitle());
                break;

            case R.id.nav_location:
                Intent intent = new Intent(this, LocationsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                signOut();
                break;

        }
        menuItem.setChecked(false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeLanguage(String language) {
        if (!PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(language)) {
            if (PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ARABIC)) {
                PreferenceController.getInstance(this).persist(PreferenceController.LANGUAGE, Constants.ENGLISH);
                mTabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            } else {
                PreferenceController.getInstance(this).persist(PreferenceController.LANGUAGE, Constants.ARABIC);
                mTabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            finish();
            startActivity(new Intent(this, MainAgentActivity.class));


        }

    }


    public void signOut() {
        signOutFromGoogle();
        redirectToLogin();
    }


    public void redirectToLogin() {
        Intent intent = new Intent(MainAgentActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void redirectToNoInternetConnection() {
        Intent intent = new Intent(MainAgentActivity.this, NoInternetConnectionActivity.class);
        startActivity(intent);

    }

    @Override
    public void onIconChanged(boolean isAppointmentStarted) {
        this.isAppointmentStarted = isAppointmentStarted;
        if (this.isAppointmentStarted) {
            changeColorAndSrcOfStartAndEndButton(DRAWABLE_RESOURCE_FOR_START_STATE, COLOR_ID_FOR_START_STATE);
            this.isAppointmentStarted = false;
        } else {
            changeColorAndSrcOfStartAndEndButton(DRAWABLE_RESOURCE_FOR_FINISH_STATE, COLOR_ID_FOR_FINISH_STATE);

            this.isAppointmentStarted = true;

        }
    }

    @Override
    public void onCallCustomer(int numOfCalls) {
        this.numOfCalls = numOfCalls;
    }

    @Override
    public void onNoDataReturned() {
        redirectToNoInternetConnection();
    }

    @Override
    public void notifyByListSize(final int listSize) {

        if (mTabLayout.getTabAt(mViewPager.getCurrentItem()).getCustomView() == null)
            mTabLayout.getTabAt(mViewPager.getCurrentItem()).setCustomView(updateTabTextView(mViewPager.getCurrentItem(), listSize));
        else
            ((TextView) mTabLayout.getTabAt(mViewPager.getCurrentItem()).getCustomView().findViewById(R.id.new_notifications_for_list_size)).setText("" + listSize);


    }

    @Override
    public void allowProgressBarToBeVisible() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void allowProgressBarToBeGone() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void enableLayoutForNoLocations() {
        mViewPager.setVisibility(View.GONE);
        noLocationAvailableConstraintLayout.setVisibility(View.VISIBLE);

    }

    private void disableNoLocationLayout() {
        mViewPager.setVisibility(View.VISIBLE);
        enableButtonsAndLayoutToBeVisible();
        noLocationAvailableConstraintLayout.setVisibility(View.GONE);

    }

    @SuppressLint("RestrictedApi")
    private void hideButtonAndTabLayout() {
        callFab.setVisibility(View.GONE);
        arrivalFab.setVisibility(View.GONE);
        startOrEndFab.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    private void enableButtonsAndLayoutToBeVisible() {
        callFab.setVisibility(View.VISIBLE);
        arrivalFab.setVisibility(View.VISIBLE);
        startOrEndFab.setVisibility(View.VISIBLE);
        mTabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissFloatingButtons() {
        callFab.setVisibility(View.GONE);
        arrivalFab.setVisibility(View.GONE);
        startOrEndFab.setVisibility(View.GONE);
    }

    @Override
    public void enableFloatingButtons() {
        callFab.setVisibility(View.VISIBLE);
        arrivalFab.setVisibility(View.VISIBLE);
        startOrEndFab.setVisibility(View.VISIBLE);
    }

    public void enableNoServerLayout(){
        noServerLayout.setVisibility(View.VISIBLE);
    }

    public void disableServerLayout(){
        noServerLayout.setVisibility(View.GONE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy method");
        if (alertDialog != null)
            alertDialog.dismiss();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause method");
    }

    private void signOutFromGoogle() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mainViewModel.clearDataFromSharedPreference();
                    }
                });
        mGoogleSignInClient = null;

    }

    @Override
    public void animateStartOrFinishButton(String state) {
        animateStartOrEndBtn(state);
    }

    public void changeColorAndSrcOfStartAndEndButton(int drawableId, int colorId) {
        startOrEndFab.setImageDrawable(getResources().getDrawable(drawableId));
        startOrEndFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, colorId)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void showAlertWithMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();

    }

    int numOfUnArrivedData;

    private void getNumOfUnArrivedCustomers() {

        mainViewModel.getNumOfUnArrivedData("OPTH1","EN").observe(this, (Observer<Integer>) o ->
                {
                    numOfUnArrivedData = o.intValue();
                }
        );

    }

    @VisibleForTesting
    public int getNumberofUnArrivedCustomer() {
        return numOfUnArrivedData;
    }

    @VisibleForTesting
    public int getNumberOfCustomerAtFirstLocation() {
        return Integer.parseInt(currentLocationList.get(0).getCount());
    }

    private void callButtonClicked(String clientId) {
//
//        Call call = getSinchServiceInterface().callUserVideo(clientId);
//        String callId = call.getCallId();
//
//        Intent callScreen = new Intent(this, CallScreenActivity.class);
//        callScreen.putExtra(SinchService.CALL_ID, callId);
//        startActivity(callScreen);
    }

}

