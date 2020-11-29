package com.thetatechno.serviceagent.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.thetatechno.serviceagent.R;
import com.thetatechno.serviceagent.utils.CheckForNetwork;


public class NoInternetConnectionActivity extends BaseActivity {
Button retryBtn;
ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
        retryBtn = findViewById(R.id.retryConnectBtn);
        mProgressBar = findViewById(R.id.internet_progress_bar);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                retryBtn.setEnabled(false);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(CheckForNetwork.isConnectionOn(NoInternetConnectionActivity.this))
                        {
                            onBackPressed();
                        }
                        mProgressBar.setVisibility(View.GONE);
                        retryBtn.setEnabled(true);

                    }

                }, 1000);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
