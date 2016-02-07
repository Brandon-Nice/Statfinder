package com.statfinder.statfinder;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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
                //loginStudent(loginResult.getAccessToken().getUserId());
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
                //loginStudent(AccessToken.getCurrentAccessToken().getUserId());
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

        //add visualizations here
        ArrayList<String> labels = new ArrayList<String>();
        //labels.add("Jua")


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    //LoginButton getter
    static LoginButton getLoginButton() {
        return loginButton;
    }
    //CallbackManager getter
    static CallbackManager getCallbackManager() {
        return callbackManager;
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

}