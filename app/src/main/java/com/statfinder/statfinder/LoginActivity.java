package com.statfinder.statfinder;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


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

        BarChart chart = (BarChart) findViewById(R.id.chart);
        BarData data = new BarData(getXAxisValues(), getDataSet());
        getXAxisValues().clear();
        YAxis leftAxis = chart.getAxisLeft();                           /* left, right and x axises set to be disabled*/
        YAxis rightAxis = chart.getAxisRight();
        XAxis xAxis = chart.getXAxis();
        rightAxis.setEnabled(false);
        xAxis.setEnabled(false);
        leftAxis.setEnabled(false);
        chart.setData(data);
        chart.setDescription(" ");
        chart.setDrawGridBackground(false);
        chart.animateXY(2000, 2000);
        chart.invalidate();
        chart.setTouchEnabled(false);

    }

    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(40.000f, 0);
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(60.000f, 1);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(80.000f, 2);
        valueSet1.add(v1e3);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, " ");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet1.setValueTextSize(0);
        barDataSet1.setLabel(" ");
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("1");
        xAxis.add("2");
        xAxis.add("3");
        return xAxis;
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