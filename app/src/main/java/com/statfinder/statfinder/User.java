package com.statfinder.statfinder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by niceb on 2/10/2016.
 */
public class User {

    //The user's unique identification number
    private String id;

    //The user's moderator status
    private Boolean modStatus;

    //The user's selected catagories
    private ArrayList<String> selCat;

    //The user's created questions
    private HashMap<String, ArrayList<String>> createdQuestions;

    //The user's skipped questions
    private HashMap<String, ArrayList<String>> skippedQuestions;

    //The user's answered questions
    private HashMap<String, ArrayList<String>> answeredQuestions;

    //An empty User
    public User()
    {

    }

    //A complete User
    public User(String id, Boolean modStatus) {
        this.id = id;
        this.modStatus = modStatus;
    }

    // Get methods

    // Get the user's id
    public String getId() { return id; }

    // Get the user's mod status
    public Boolean getModStatus() {return modStatus;}

    // Get the user's createdQuestions
    public HashMap<String, ArrayList<String>> getCreatedQuestions() {return createdQuestions;}

    // Get the user's skippedQuestions
    public HashMap<String, ArrayList<String>> getSkippedQuestions() {return skippedQuestions;}

    // Get the user's answeredQuestions
    public HashMap<String, ArrayList<String>> getAnsweredQuestions() {return answeredQuestions;}

    // Get the user's selected categories
    public ArrayList<String> getSelCat() {return selCat;}

    // Set methods

    // Set the user's unique identification number
    public void setId(String id)
    {
        this.id = id;
    }

    // Set the user's mod status
    public void setModStatus(Boolean modStatus) {
        this.modStatus = modStatus;
    }

    // Set the user's createdQuestions
    public void setCreatedQuestions(HashMap<String, ArrayList<String>> createdQuestions) {
        this.createdQuestions = createdQuestions;
    }

    // Set the user's skippedQuestions
    public void setSkippedQuestions(HashMap<String, ArrayList<String>> skippedQuestions) {
        this.skippedQuestions = skippedQuestions;
    }

    // Set the user's answeredQuestions
    public void setAnsweredQuestions(HashMap<String, ArrayList<String>> answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    // Set the user's selected categories
    public void setSelCat(ArrayList<String> selCat) {
        this.selCat = selCat;
    }

}
