package com.statfinder.statfinder;

/**
 * Created by niceb on 2/10/2016.
 */
public class User {

    //The user's unique identification number
    private String id;

    //The user's first name
    private String firstName;

    //The user's last name
    private String lastName;

    //The user's Latitude
    private double Latitude;

    //The user's Longitude
    private double Longitude;


    //An empty User
    public User()
    {

    }

    //A complete User
    public User(String id, String firstName, String lastName, double Latitude, double Longitude) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    // Get methods

    // Get the user's id
    public String getId() { return id; }

    // Get the user's first name
    public String getFirstName() { return firstName; }

    // Get the user's last name
    public String getLastName() { return lastName; }

    // Get the user's Latitude
    public double getLatitude() { return Latitude; }

    // Get the user's Longitude
    public double getLongitude() { return Longitude;}

    // Set methods

    // Set the user's unique identification number
    public void setId(String id)
    {
        this.id = id;
    }

    // Set the user's first name
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    // Set the user's last name
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    // Set the user's Latitude
    public void setLatitude(double Latitude)
    {
        this.Latitude = Latitude;
    }

    // Set the user's Latitude
    public void setLongitude(double Longitude)
    {
        this.Longitude = Longitude;
    }


}
