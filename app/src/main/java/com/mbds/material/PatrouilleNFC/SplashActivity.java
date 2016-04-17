package com.mbds.material.PatrouilleNFC;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.mbds.material.PatrouilleNFC.Utils.SessionManager;
import com.naokistudio.material.PatrouilleNFC.R;

public class SplashActivity extends Activity {
    private static final long SPLASH_DURATION = 5000L;

    private Handler mHandler;
    private Runnable mRunnable;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SessionManager(getApplicationContext());
        boolean b = session.isLoggedIn();
        if(b)
        {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                dismissSplash();
            }
        };

        // allow user to click and dismiss the splash screen prematurely
        View rootView = findViewById(android.R.id.content);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSplash();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, SPLASH_DURATION);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    private void dismissSplash() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}

