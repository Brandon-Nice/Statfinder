package com.statfinder.statfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import com.firebase.client.Firebase;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
