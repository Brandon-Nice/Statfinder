package com.statfinder.statfinder;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;


import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    //Declaring variables
    //private TextView info;
    public static LoginButton loginButton;
    public static CallbackManager callbackManager;
    public AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        callbackManager = CallbackManager.Factory.create();
        //gets the login button from activity_login.xml
        loginButton = (LoginButton) findViewById(R.id.fb_button);
        loginButton.setReadPermissions("public_profile");

        //Creates a callback function to handle the results of the login attempts
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginUser(loginResult.getAccessToken().getUserId());
                Intent init = new Intent(LoginActivity.this, MainActivity.class);
                init.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(init);
                finish();
            }

            @Override
            public void onCancel() {
                //info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                //info.setText("Login attempt failed.");
            }

        });

        if (isLoggedIn())
        {
            boolean fromNavMenu = false;
            if (getIntent().hasExtra("FromNavMenu"))
            {
                fromNavMenu = getIntent().getExtras().getBoolean("FromNavMenu");
            }

            if (fromNavMenu == false)
            {
                loginUser(AccessToken.getCurrentAccessToken().getUserId());
                //If already logged in, start MainActivity
                Intent init = new Intent(LoginActivity.this, MainActivity.class);
                init.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(init);
                finish();

            }

        }

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    Intent init = new Intent(LoginActivity.this, LoginActivity.class);
                    init.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(init);
                    finish();
                }
            }
        };

        PieChart pieChart = (PieChart) findViewById(R.id.chart);
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(5.5f, 0));
        entries.add(new Entry(3f, 1));
        entries.add(new Entry(1.5f, 2));
        //entries.add(new Entry(1f, 3));

        ArrayList<String> labels = new ArrayList<>();

        PieDataSet dataSet = new PieDataSet(entries, "");
        for (int i = 0; i < entries.size(); i++){
            labels.add("");
        }

        PieData data = new PieData(labels, dataSet);

        pieChart.setData(data);

        final int[] colors = new int[5];
        colors[0] = getResources().getColor(R.color.darkBlue);
        colors[1] = getResources().getColor(R.color.darkMagenta);
        colors[2] = getResources().getColor(R.color.darkModerateLimeGreen);
        //colors[3] = getResources().getColor(R.color.darkCyan);

        dataSet.setColors(colors);

        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleRadius(0);
        pieChart.setDescription("");
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        dataSet.setDrawValues(false);

        pieChart.animateXY(2000, 2000);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (accessTokenTracker != null) {
            accessTokenTracker.stopTracking();
        }
    }

    public void loginUser(final String userID)
    {
        User currentUser = new User();
        currentUser.setId(userID);
        currentUser.setModStatus(false);
        currentUser.setModPreference(false);
        currentUser.setSelCat(((MyApplication) getApplication()).defCat);
        ((MyApplication) getApplication()).setUser(currentUser);
        Intent init = new Intent(LoginActivity.this, MainActivity.class);
        init.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(init);
        finish();
    }

}