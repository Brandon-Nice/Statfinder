package com.statfinder.statfinder;

import android.app.Application;

import java.util.ArrayList;

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

    //Default ArrayList of categories
    public ArrayList<String> defCat = new ArrayList<String>() {{
        add("General");
        add("Sports");
        add("Entertainment");
        add("Games");
        add("Art");
        add("History");
        add("SciTech");
    }};

}
