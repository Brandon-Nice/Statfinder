package com.statfinder.statfinder;

import android.app.Application;

/**
 * Created by niceb on 2/10/2016.
 */
public class MyApplication extends Application {

    // Stores the application's current user
    private User user;

    // Use to get the application's current user
    public User getUser()
    {
        return user;
    }

    // Use to set the application's current user
    public void setUser(User user)
    {
        this.user = user;
    }

}
