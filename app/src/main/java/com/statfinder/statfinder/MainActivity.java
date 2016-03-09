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

        //Gets the name of the user
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", null,
                HttpMethod.GET, new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                //handle the response
                final JSONObject jsonObject = response.getJSONObject();
                String name;
                try {
                    final TextView user_name = (TextView) findViewById(R.id.usertextView);
                    name = jsonObject.getString("name");
                    user_name.setText(name);
                    String firstName = name.substring(0, name.indexOf(" "));
                    String lastName = name.substring(name.indexOf(" ") + 1);
                    if((firstName.equals("Brandon")&& lastName.equals("Nice")) || (firstName.equals("Michael")&& lastName.equals("Rollberg"))
                            || (firstName.equals("Milia")&& lastName.equals("Enane")) || (firstName.equals("Jake")&& lastName.equals("Losin"))
                            || (firstName.equals("Kenny")&& lastName.equals("Tam"))) {
                        ((MyApplication) getApplication()).getUser().setModStatus(true);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).executeAsync();

        //setUser
        setUser();

        /* Create button object for making a question */
        Button makeQuestionButton = (Button)findViewById(R.id.makequestionButton);
        /* Send user to the add question page on button press */
        makeQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddQuestionActivity.class));
            }
        });

        /* Create button object for making a question */
        Button popularQuestionButton = (Button)findViewById(R.id.popularButton);
        /* Send user to the add question page on button press */
        popularQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent init = new Intent(MainActivity.this, QuestionActivity.class);
                init.putExtra("category", "Popular");
                startActivity(init);
                //startActivity(new Intent(MainActivity.this, QuestionActivity.class));

            }
        });
        final Button randomQuestionButton = (Button)findViewById(R.id.randomButton);
        /* Send user to the add question page on button press */
        randomQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent init = new Intent(MainActivity.this, QuestionActivity.class);
                init.putExtra("category", "Random");
                startActivity(init);
                //startActivity(new Intent(MainActivity.this, QuestionActivity.class));
            }
        });

        /* Use database to set text in buttons to any popular or random question */
        /* Question will be added from the database when we have that working */
        //popularQuestionButton.setText("Popular Questions\n" + "This is a popular question");
        //randomQuestionButton.setText("Random Questions\n" + "This is a random question");

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
        }
        else if (id == R.id.search_button) {
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
        } else if (id == R.id.nav_sports) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "Sports");
            startActivity(init);

        } else if (id == R.id.nav_entertainment) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "Entertainment");
            startActivity(init);

        } else if (id == R.id.nav_games) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "Games");
            startActivity(init);

        } else if (id == R.id.nav_art) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "Art");
            startActivity(init);

        } else if (id == R.id.nav_history) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "History");
            startActivity(init);

        } else if (id == R.id.nav_scitech) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "Science & Tech");
            startActivity(init);

        } else if (id == R.id.nav_general) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            init.putExtra("category", "General");
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
        //Firebase
        final Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Kenny's playground
                Firebase questionRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/ModeratorQuestions/General");
                questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, Object> questionsHashMap = (HashMap) dataSnapshot.getValue();
                        Iterator it = questionsHashMap.entrySet().iterator();

                        Button popularQuestionButton = (Button)findViewById(R.id.popularButton);
                        Button randomQuestionButton = (Button)findViewById(R.id.randomButton);
                        String questionName;

                        //while(it.hasNext()) {
                        HashMap.Entry temptry = (HashMap.Entry) it.next();
                        HashMap<String, Object> entries = (HashMap)temptry.getValue();
                        questionName = entries.get("Name").toString();
                        int questionSize = questionName.length();

                        String moddedQ = "";
                        char c;
                        for(int i = 0; i < questionSize; i++) {
                            c = questionName.charAt(i);
                            if (c == '_') {
                                c = ' ';
                                moddedQ += c;
                            }
                            else {
                                moddedQ += c;
                            }
                        }
                        randomQuestionButton.setText("Random Question:\n" + moddedQ);
                        popularQuestionButton.setText("Popular Question:\n" + moddedQ);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                //Kenny's domain ends here

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
                }
                else {
                    //User has information stored on Firebase, retrieve it
                    Boolean dbModStatus = (Boolean) val.get("modStatus");
                    Boolean dbModPreference = (Boolean) val.get("modPreference");
                    ArrayList<String> dbSelCat = (ArrayList<String>) val.get("selectedCategory");

                    if (dbModStatus != null) {
                        currentUser.setModStatus(dbModStatus);
                    }
                    if (dbSelCat != null) {
                        currentUser.setSelCat(dbSelCat);
                    }
                    if (dbModPreference != null) {
                        currentUser.setModPreference(dbModPreference);
                    }

                }

                MainActivity.this.setUserLocation();

                final String finalCity = currentUser.getCity().replace(' ', '_');
                final String finalCountry = currentUser.getCountry().replace(' ', '_');
                final String finalState = currentUser.getState().replace(' ', '_');

                final Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/Questions/" + finalCountry + "/" + finalState + "/" + finalCity + "/");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot);
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
                                                questionInfo.put("Total_Votes", moderatedQuestion.get("Total_Votes"));
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
                                    public void onCancelled (FirebaseError firebaseError){

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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    public String incrementHex(String value)
    {
        char[] hexValue = value.toCharArray();
        boolean carryOne = true;
        for (int i = hexValue.length - 1; i >= 0 && carryOne == true; i--)
        {
            carryOne = false;
            if (hexValue[i] == '0')
            {
                hexValue[i] = '1';
            }
            else if (hexValue[i] == '1')
            {
                hexValue[i] = '2';
            }
            else if (hexValue[i] == '2')
            {
                hexValue[i] = '3';
            }
            else if (hexValue[i] == '3')
            {
                hexValue[i] = '4';
            }
            else if (hexValue[i] == '4')
            {
                hexValue[i] = '5';
            }
            else if (hexValue[i] == '5')
            {
                hexValue[i] = '6';
            }
            else if (hexValue[i] == '6')
            {
                hexValue[i] = '7';
            }
            else if (hexValue[i] == '7')
            {
                hexValue[i] = '8';
            }
            else if (hexValue[i] == '8')
            {
                hexValue[i] = '9';
            }
            else if (hexValue[i] == '9')
            {
                hexValue[i] = 'a';
            }
            else if (hexValue[i] == 'a')
            {
                hexValue[i] = 'b';
            }
            else if (hexValue[i] == 'b')
            {
                hexValue[i] = 'c';
            }
            else if (hexValue[i] == 'c')
            {
                hexValue[i] = 'd';
            }
            else if (hexValue[i] == 'd')
            {
                hexValue[i] = 'e';
            }
            else if (hexValue[i] == 'e')
            {
                hexValue[i] = 'f';
            }
            else if (hexValue[i] == 'f')
            {
                hexValue[i] = '0';
                carryOne = true;
            }
        }

        if (carryOne == true)
        {
            String tempHexValue = "1" + new String(hexValue);
            hexValue = tempHexValue.toCharArray();
        }
        return new String(hexValue);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setUserLocation();
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
            Toast.makeText(getApplicationContext(), "Please enable location services so we could get your location", Toast.LENGTH_LONG).show();
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
        currentUser.setCity(city);
        currentUser.setState(state);
        currentUser.setCountry(country);
    }

}
