package com.statfinder.statfinder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.ProfilePictureView;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.firebase.client.Firebase;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Firebase answeredRef = null;
    Firebase skippedRef = null;
    Firebase checkedRef = null;
    /* String id to store question ID in setUser*/
    String id;
    String idPopular = null;
    String namePopular = null;
    String categoryPopular = null;
    String idRandom = null;
    String nameRandom = null;
    String categoryRandom = null;
    boolean modStatusPopular = false;
    boolean modStatusRandom = false;
    boolean outOfQuestions = false;
    String globalName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this is a comment
        //Initialize Facebook SDK and Firebase
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final TextView user_name = (TextView) findViewById(R.id.usertextView);
        //String name;

        //Gets the name of the user
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", null,
                HttpMethod.GET, new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                //handle the response
                final JSONObject jsonObject = response.getJSONObject();
                try {
                    globalName = jsonObject.getString("name");

                    String firstName = globalName.substring(0, globalName.indexOf(" "));
                    String lastName = globalName.substring(globalName.indexOf(" ") + 1);
                    if ((firstName.equals("Brandon") && lastName.equals("Nice")) || (firstName.equals("Michael") && lastName.equals("Rollberg"))
                            || (firstName.equals("Milia") && lastName.equals("Enane")) || (firstName.equals("Jake") && lastName.equals("Losin"))
                            || (firstName.equals("Kenny") && lastName.equals("Tam"))) {
                        ((MyApplication) getApplication()).getUser().setModStatus(true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).executeAsync();

        //user_name.setText(globalName);

        //setUser
        setUser();

        /* Create button object for making a question */
        Button makeQuestionButton = (Button) findViewById(R.id.makequestionButton);
        /* Send user to the add question page on button press */
        makeQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddQuestionActivity.class));
            }
        });

        /* Create button object for making a question */
        Button popularQuestionButton = (Button) findViewById(R.id.popularButton);
        /* Send user to the add question page on button press */
        popularQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent init = new Intent(MainActivity.this, QuestionActivity.class);
                init.putExtra("category", "Popular");
                init.putExtra("Name", namePopular);
                //Tells questionActivity what to pull from database
                init.putExtra("questionID", idPopular);
                init.putExtra("categoryOrigin", categoryPopular);

                startActivity(init);
                //startActivity(new Intent(MainActivity.this, QuestionActivity.class));

            }
        });
        final Button randomQuestionButton = (Button) findViewById(R.id.randomButton);
        /* Send user to the add question page on button press */
        randomQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent init = new Intent(MainActivity.this, QuestionActivity.class);
                //init.putExtra("category", "Random");
                init.putExtra("category", "Random");
                init.putExtra("Name", nameRandom);

                //Tells questionActivity what to pull from database
                init.putExtra("questionID", idRandom);
                init.putExtra("categoryOrigin", categoryRandom);

                startActivity(init);
                //startActivity(new Intent(MainActivity.this, QuestionActivity.class));
            }
        });

    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent init = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(init);
            return true;
        } else if (id == R.id.search_button) {
            Intent init = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(init);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            Intent init = new Intent(MainActivity.this, AddQuestionActivity.class);
            startActivity(init);
        }
        else if (id == R.id.nav_questionhist)
        {
            Intent init = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(init);
        }
        else if (id == R.id.nav_sports) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "Sports");
            init.putExtra("questionID", "null");
            init.putExtra("categoryOrigin", "null");
            init.putExtra("Name", "null");


            startActivity(init);

        } else if (id == R.id.nav_entertainment) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "Entertainment");
            init.putExtra("questionID", "null");
            init.putExtra("categoryOrigin", "null");
            init.putExtra("Name", "null");


            startActivity(init);

        } else if (id == R.id.nav_games) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "Games");
            init.putExtra("questionID", "null");
            init.putExtra("categoryOrigin", "null");
            init.putExtra("Name", "null");

            startActivity(init);

        } else if (id == R.id.nav_art) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "Art");
            init.putExtra("questionID", "null");
            init.putExtra("categoryOrigin", "null");
            init.putExtra("Name", "null");

            startActivity(init);

        } else if (id == R.id.nav_history) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "History");
            init.putExtra("questionID", "null");
            init.putExtra("categoryOrigin", "null");
            init.putExtra("Name", "null");

            startActivity(init);

        } else if (id == R.id.nav_scitech) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "SciTech");
            init.putExtra("questionID", "null");
            init.putExtra("categoryOrigin", "null");
            init.putExtra("Name", "null");

            startActivity(init);

        } else if (id == R.id.nav_general) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "General");
            init.putExtra("questionID", "null");
            init.putExtra("categoryOrigin", "null");
            init.putExtra("Name", "null");

            startActivity(init);

        } else if (id == R.id.nav_about) {
            Intent init = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(init);
        } else if (id == R.id.nav_contact) {
            Intent init = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(init);
        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setUser() {
        Firebase.setAndroidContext(this);
        final User currentUser = ((MyApplication) getApplication()).getUser();
        final Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                HashMap<String, Object> val = (HashMap) snapshot.getValue();
                //User does not have any information stored on Firebase, set defaults
                if (val == null) {
                    HashMap<String, Boolean> ModStatus = new HashMap();
                    ModStatus.put("modStatus", currentUser.getModStatus());
                    ref.setValue(ModStatus);

                    //selectedCategory
                    ref.child("selectedCategory").setValue(currentUser.getSelCat());

                    //modPreference
                    ref.child("modPreference").setValue(currentUser.getModPreference());

                    //ref.child("skippedQuestions").setValue(currentUser.getSkippedQuestions());

                    //ref.child("answeredQuestions").setValue(currentUser.getAnsweredQuestions());
                } else {
                    //User has information stored on Firebase, retrieve it
                    Boolean dbModStatus = (Boolean) val.get("modStatus");
                    Boolean dbModPreference = (Boolean) val.get("modPreference");
                    ArrayList<String> dbSelCat = (ArrayList<String>) val.get("selectedCategory");
                    // ArrayList<String> dbskippedQuestion = (ArrayList<String>) val.get

                    if (dbModStatus != null) {
                        currentUser.setModStatus(dbModStatus);
                    }
                    if (dbSelCat != null) {
                        currentUser.setSelCat(dbSelCat);
                    }
                    if (dbModPreference != null) {
                        currentUser.setModPreference(dbModPreference);
                    }

                    //)

                }

                MainActivity.this.setUserLocation();

                final String finalCity = currentUser.getCity();
                final String finalCountry = currentUser.getCountry();
                final String finalState = currentUser.getState();
                final Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/Questions/" + finalCountry + "/" + finalState + "/" + finalCity + "/");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            ArrayList<String> categories = ((MyApplication) getApplication()).defCat;
                            for (int i = 0; i < categories.size(); i++) {
                                final Firebase moderatedRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/ModeratorQuestions/" + categories.get(i));
                                moderatedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.getValue() != null) {
                                            HashMap<String, Object> questions = (HashMap) snapshot.getValue();
                                            final String category = snapshot.getKey();
                                            for (Map.Entry<String, Object> question : questions.entrySet()) {
                                                HashMap moderatedQuestion = (HashMap) question.getValue();
                                                HashMap<String, Object> questionInfo = new HashMap();
                                                questionInfo.put("Flags", moderatedQuestion.get("Flags"));
                                                questionInfo.put("Moderated", moderatedQuestion.get("Moderated"));
                                                questionInfo.put("Name", moderatedQuestion.get("Name"));
                                                questionInfo.put("Total_Votes", 0);
                                                final Firebase questionRef = ref.child(category + "/" + question.getKey() + "/");
                                                questionRef.setValue(questionInfo);
                                                questionRef.setPriority(0);
                                                Firebase answersRef = moderatedRef.child(question.getKey() + "/Answers");
                                                answersRef.orderByPriority().addListenerForSingleValueEvent(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        int childNumber = 0;
                                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                            Firebase answerRef = questionRef.child("Answers/" + child.getKey());
                                                            answerRef.setValue(0);
                                                            answerRef.setPriority(childNumber++);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(FirebaseError firebaseError) {

                                                    }
                                                });
                                                Firebase numQuestionRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/NumQuestions");
                                                numQuestionRef.runTransaction(new Transaction.Handler() {
                                                    @Override
                                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                                        if (mutableData.getValue() == null) {
                                                            mutableData.setValue("1");
                                                        } else {
                                                            String value = MainActivity.this.incrementHex((String) mutableData.getValue());
                                                            mutableData.setValue(value);
                                                        }
                                                        return Transaction.success(mutableData);
                                                    }

                                                    @Override
                                                    public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {

                                                    }
                                                });
                                            }


                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }
                        }
                        /* Start of nested listeners to obtain question for preview */
                        Firebase questionRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/" + finalCountry + "/" + finalState + "/" + finalCity + "/");
                        answeredRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/AnsweredQuestions/");
                        skippedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/SkippedQuestions/");
                        checkedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/CreatedQuestions/");

                        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    final DataSnapshot savedSnapshot = dataSnapshot;
                                    checkedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot createdSnapshot) {
                                            skippedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(final DataSnapshot skippedSnapshot) {
                                                    answeredRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot answeredSnapshot) {
                                                            HashMap<String, String> answeredHistory = (HashMap<String, String>) answeredSnapshot.getValue();
                                                            HashMap<String, String> skippedHistory = (HashMap<String, String>) skippedSnapshot.getValue();
                                                            HashMap<String, String> createdHistory = (HashMap<String, String>) createdSnapshot.getValue();
                                                            String bestID = "";
                                                            HashMap<String, Object> bestQuestion = null;
                                                            boolean firstCheck = false;
                                                            boolean bestCheck = false;

                                                            /* Initializes user's history if it does not exist */
                                                            if (answeredSnapshot.getValue() == null || skippedSnapshot.getValue() == null || createdSnapshot.getValue() == null) {
                                                                if (answeredSnapshot.getValue() == null) {
                                                                    answeredRef.child("-1").setValue("-1");
                                                                }
                                                                if (skippedSnapshot.getValue() == null) {
                                                                    skippedRef.child("-1").setValue("-1");
                                                                }
                                                                if (createdSnapshot.getValue() == null) {
                                                                    checkedRef.child("-1").setValue("-1");
                                                                }
                                                                for (DataSnapshot child : savedSnapshot.getChildren()) {
                                                                    /* Checks for a question user has not seen yet in category */
                                                                    for (DataSnapshot currentCategory : child.getChildren()) {
                                                                        bestQuestion = (HashMap<String, Object>) currentCategory.getValue();
                                                                        bestID = (String) currentCategory.getKey();
                                                                        //firstCheck = true;
                                                                        categoryPopular = child.getKey();
                                                                        break;

                                                                    }
                                                                    break;
                                                                }
                                                                for (DataSnapshot child : savedSnapshot.getChildren()) {
                                                                    for (DataSnapshot currentCategory : child.getChildren()) {
                                                                        HashMap<String, Object> currentQuestion = (HashMap<String, Object>) currentCategory.getValue();
                                                                            /* Checks first unseen question in the current category which should also be the most popular
                                                                            given that our database is sorted by most popular first */
                                                                            if ((long) bestQuestion.get("Total_Votes") < (long) currentQuestion.get("Total_Votes")) {
                                                                                bestID = (String) currentCategory.getKey();
                                                                                bestQuestion = currentQuestion;
                                                                                categoryPopular = child.getKey();
                                                                                bestCheck = true;
                                                                            }
                                                                        //}
                                                                        if (bestCheck) {
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                                if(bestCheck == false) {
                                                                    //Somehow ran out of questions despite this being their first time loading
                                                                }
                                                                idPopular = bestID;
                                                                namePopular = (String) bestQuestion.get("Name");

                                                                /* Random question generation for when user's history is empty */

                                                                HashMap<String, Object> allCategories = (HashMap<String, Object>) savedSnapshot.getValue();
                                                                int numCategories = allCategories.size();
                                                                ArrayList<HashMap.Entry> randomQuestions = new ArrayList<HashMap.Entry>(numCategories);
                                                                ArrayList<String> randomCategory = new ArrayList<String>(numCategories);
                                                                int index = 0;
                                                                for(DataSnapshot child : savedSnapshot.getChildren()) {
                                                                    /* Loop through each category available, add first question from each to HashMap */
                                                                    HashMap<String, Object> categoryQuestions = (HashMap<String, Object>) child.getValue();
                                                                    Iterator it = categoryQuestions.entrySet().iterator();
                                                                    HashMap.Entry topQuestion = (HashMap.Entry)it.next();
                                                                    randomQuestions.add(index, topQuestion);
                                                                    randomCategory.add(index, child.getKey());
                                                                    index++;
                                                                }
                                                                int numberOfCategories = randomQuestions.size();
                                                                int randomCategoryIndex = (int) (Math.random() * numberOfCategories);
                                                                /*HashMap<String, Object> chosenValue = (HashMap<String, Object>) chosenRandomQuestion.getValue();
                                                                                    idRandom = (String) chosenRandomQuestion.getKey();
                                                                                    nameRandom = (String) chosenValue.get("Name");
                                                                                    categoryRandom = randomCategory.getKey();
                                                                                    randomCheck = true;
                                                                                    break;*/
                                                                HashMap.Entry chosenRandomQuestion = randomQuestions.get(randomCategoryIndex);
                                                                HashMap<String, Object> chosenRandomValue = (HashMap<String, Object>) chosenRandomQuestion.getValue();
                                                                idRandom = (String) chosenRandomQuestion.getKey();
                                                                nameRandom = (String) chosenRandomValue.get("Name");
                                                                categoryRandom = randomCategory.get(randomCategoryIndex);


                                                            } else {
                                                                /* Finds category with best question by comparing first found with all categories' best */
                                                                for (DataSnapshot child : savedSnapshot.getChildren()) {
                                                                    /* Checks for a question user has not seen yet in category */
                                                                    for (DataSnapshot currentCategory : child.getChildren()) {
                                                                        String firstID = (String) currentCategory.getKey();
                                                                        if (!answeredHistory.containsKey(firstID) && !skippedHistory.containsKey(firstID) && !createdHistory.containsKey(firstID)) {
                                                                            bestQuestion = (HashMap<String, Object>) currentCategory.getValue();
                                                                            bestID = (String) currentCategory.getKey();
                                                                            firstCheck = true;
                                                                            categoryPopular = child.getKey();

                                                                            break;
                                                                        }
                                                                        if (firstCheck) {
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (firstCheck) {
                                                                        break;
                                                                    }
                                                                }
                                                                if (firstCheck == false) {
                                                                    //No questions left, set boolean
                                                                    outOfQuestions = true;
                                                                    idPopular = "Out of questions!";
                                                                    namePopular = "Out of questions!";
                                                                    categoryPopular = "Out of questions!";
                                                                } else {
                                                                    for (DataSnapshot child : savedSnapshot.getChildren()) {
                                                                        for (DataSnapshot currentCategory : child.getChildren()) {
                                                                            HashMap<String, Object> currentQuestion = (HashMap<String, Object>) currentCategory.getValue();
                                                                            if (!answeredHistory.containsKey(currentCategory.getKey()) && !skippedHistory.containsKey(currentCategory.getKey())
                                                                                    && !createdHistory.containsKey(currentCategory.getKey())) {
                                                                            /* Checks first unseen question in the current category which should also be the most popular
                                                                            given that our database is sorted by most popular first */
                                                                                if ((long) bestQuestion.get("Total_Votes") < (long) currentQuestion.get("Total_Votes")) {
                                                                                    bestID = (String) currentCategory.getKey();
                                                                                    bestQuestion = currentQuestion;
                                                                                    bestCheck = true;
                                                                                    categoryPopular = child.getKey();
                                                                                } else {
                                                                                    break;
                                                                                }
                                                                            }
                                                                            if (bestCheck) {
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    idPopular = bestID;
                                                                    namePopular = (String) bestQuestion.get("Name");
                                                                }



                                                            /* Generate a random question */
                                                             /*HashMap<String, Object> chosenValue = (HashMap<String, Object>) chosenRandomQuestion.getValue();
                                                                                    idRandom = (String) chosenRandomQuestion.getKey();
                                                                                    nameRandom = (String) chosenValue.get("Name");
                                                                                    categoryRandom = randomCategory.getKey();
                                                                                    randomCheck = true;
                                                                                    break;*/
                                                                if (!outOfQuestions) {
                                                                    HashMap<String, Object> allCategories = (HashMap<String, Object>) savedSnapshot.getValue();
                                                                    int numCategories = allCategories.size();
                                                                    ArrayList<HashMap.Entry> randomQuestions = new ArrayList<HashMap.Entry>(numCategories);
                                                                    ArrayList<String> randomCategory = new ArrayList<String>(numCategories);
                                                                    int index = 0;
                                                                    for(DataSnapshot child : savedSnapshot.getChildren()) {
                                                                    /* Loop through each category available, add first question from each to HashMap */
                                                                        HashMap<String, Object> categoryQuestions = (HashMap<String, Object>)child.getValue();
                                                                        Iterator it = categoryQuestions.entrySet().iterator();
                                                                        while(it.hasNext()) {
                                                                            HashMap.Entry currentQuestion = (HashMap.Entry)it.next();
                                                                            if (!answeredHistory.containsKey(currentQuestion.getKey()) && !skippedHistory.containsKey(currentQuestion.getKey())
                                                                                    && !createdHistory.containsKey(currentQuestion.getKey())) {
                                                                                randomQuestions.add(index, currentQuestion);
                                                                                randomCategory.add(index, child.getKey());
                                                                                index++;
                                                                                break;
                                                                            }

                                                                        }
                                                                    }
                                                                    int numberOfCategories = randomQuestions.size();
                                                                    int randomCategoryIndex = (int) (Math.random() * numberOfCategories);
                                                                    HashMap.Entry chosenRandomQuestion = randomQuestions.get(randomCategoryIndex);
                                                                    HashMap<String, Object> chosenRandomValue = (HashMap<String, Object>) chosenRandomQuestion.getValue();
                                                                    idRandom = (String) chosenRandomQuestion.getKey();
                                                                    nameRandom = (String) chosenRandomValue.get("Name");
                                                                    categoryRandom = randomCategory.get(randomCategoryIndex);
                                                                } else {
                                                                    idRandom = "Out of questions!";
                                                                    nameRandom = "Out of questions!";
                                                                    categoryRandom = "Out of questions!";
                                                                }
                                                            }

                                                            Button popularQuestionButton = (Button) findViewById(R.id.popularButton);
                                                            Button randomQuestionButton = (Button) findViewById(R.id.randomButton);

                                                            String popularPreview = namePopular.replaceAll("_", " ");
                                                            String randomPreview = nameRandom.replaceAll("_", " ");
                                                            popularQuestionButton.setText("Popular Question:\n" + popularPreview);
                                                            randomQuestionButton.setText("Random Question:\n" + randomPreview);
                                                        }


                                                        @Override
                                                        public void onCancelled(FirebaseError firebaseError) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(FirebaseError firebaseError) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError checkedError) {

                                        }
                                    });

                                }

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = null;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                l = mLocationManager.getLastKnownLocation(provider);
            }

            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public String incrementHex(String value) {
        char[] hexValue = value.toCharArray();
        boolean carryOne = true;
        for (int i = hexValue.length - 1; i >= 0 && carryOne == true; i--) {
            carryOne = false;
            if (hexValue[i] == '0') {
                hexValue[i] = '1';
            } else if (hexValue[i] == '1') {
                hexValue[i] = '2';
            } else if (hexValue[i] == '2') {
                hexValue[i] = '3';
            } else if (hexValue[i] == '3') {
                hexValue[i] = '4';
            } else if (hexValue[i] == '4') {
                hexValue[i] = '5';
            } else if (hexValue[i] == '5') {
                hexValue[i] = '6';
            } else if (hexValue[i] == '6') {
                hexValue[i] = '7';
            } else if (hexValue[i] == '7') {
                hexValue[i] = '8';
            } else if (hexValue[i] == '8') {
                hexValue[i] = '9';
            } else if (hexValue[i] == '9') {
                hexValue[i] = 'a';
            } else if (hexValue[i] == 'a') {
                hexValue[i] = 'b';
            } else if (hexValue[i] == 'b') {
                hexValue[i] = 'c';
            } else if (hexValue[i] == 'c') {
                hexValue[i] = 'd';
            } else if (hexValue[i] == 'd') {
                hexValue[i] = 'e';
            } else if (hexValue[i] == 'e') {
                hexValue[i] = 'f';
            } else if (hexValue[i] == 'f') {
                hexValue[i] = '0';
                carryOne = true;
            }
        }

        if (carryOne == true) {
            String tempHexValue = "1" + new String(hexValue);
            hexValue = tempHexValue.toCharArray();
        }
        return new String(hexValue);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserLocation();
        setUser();
    }

    public void setUserLocation() {
        System.out.println("Set user location");
        User currentUser = ((MyApplication) getApplication()).getUser();
        Location location = null;
        double longitude;
        double latitude;
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = getLastKnownLocation();
        }
        if (location == null) {
            Toast.makeText(getApplicationContext(), "Please enable location services so we can get your location", Toast.LENGTH_LONG).show();
            return;
        }
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        Geocoder geocoder = new Geocoder(MainActivity.this);
        String city = null;
        String state = null;
        String country = null;
        try {
            Address address = geocoder.getFromLocation(latitude, longitude, 1).get(0);
            city = address.getLocality();
            state = address.getAdminArea();
            country = address.getCountryName();

        } catch (IOException e) {
            e.printStackTrace();
        }
        currentUser.setCity(city.replace(' ', '_'));
        currentUser.setState(state.replace(' ', '_'));
        currentUser.setCountry(country.replace(' ', '_'));
    }

}
