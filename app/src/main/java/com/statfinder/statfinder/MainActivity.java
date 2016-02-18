package com.statfinder.statfinder;

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


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize Facebook SDK and Firebase
        FacebookSdk.sdkInitialize(getApplicationContext());
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
//                    if((firstName.equals("Brandon")&& lastName.equals("Nice")) || (firstName.equals("Michael")&& lastName.equals("Rollberg"))
//                            || (firstName.equals("Milia")&& lastName.equals("Enane")) || (firstName.equals("Jake")&& lastName.equals("Losin"))
//                            || (firstName.equals("Kenny")&& lastName.equals("Tam"))) {
//                        ((MyApplication) getApplication()).getUser().setModStatus(true);
//                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).executeAsync();

        //setUser
        setUser();

        /* Connect to results page on button click (For answer 1 and answer 2 */
        Button btn1 = (Button)findViewById(R.id.answer1Button);
        Button btn2 = (Button)findViewById(R.id.answer2Button);

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResultsActivity.class));
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResultsActivity.class));
            }
        });

        //Gets the picture of the user
//        setContentView(R.layout.nav_header_main);
//
//        final ProfilePictureView profilePictureView;
//        profilePictureView = (ProfilePictureView) findViewById(R.id.userimageView);
//        profilePictureView.setCropped(true);
//        profilePictureView.setProfileId(AccessToken.getCurrentAccessToken().getUserId());

        //Gets the categories selected
        //setNavDrawer();
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
        Log.i("yo", "In first method!");
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            Intent init = new Intent(MainActivity.this, AddQuestionActivity.class);
            startActivity(init);
        } else if (id == R.id.nav_gallery) {
            Intent init = new Intent(MainActivity.this, QuestionActivity.class);
            startActivity(init);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUser() {
        Firebase.setAndroidContext(this);
        final User currentUser = ((MyApplication) getApplication()).getUser();
        if(currentUser.getSelCat().size() == 0) {
            //Sets the general category
            ArrayList<String> selCat = new ArrayList<String>() {{
                add("General");
            }};
            currentUser.setSelCat(selCat);
        }
        //Firebase
        final Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                HashMap<String, Object> val = (HashMap) snapshot.getValue();
                if (val == null) {
                    HashMap<String, Boolean> dataBaseUser = new HashMap();
                    dataBaseUser.put("modStatus", currentUser.getModStatus());
                    ref.setValue(dataBaseUser);
                }
                else {
                    HashMap<String, Boolean> dataBaseUser = new HashMap();
                    dataBaseUser.put("modStatus", currentUser.getModStatus());
                    ref.setValue(dataBaseUser);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    //Menu stuff - TODO: Add category stuff once Firebase setup is done
//    private void setNavDrawer(){
//        //Initializing NavigationView
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        // Initializing Toolbar and setting it as the actionbar
////        toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
//
//        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//
//            // This method will trigger on item Click of navigation menu
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                //Checking if the item is in checked state or not, if not make it in checked state
//                // I THINK THAT I NEED EDIT HERE...
//                Log.i("yo", "In second method!");
//                if (menuItem.isChecked()) {
//                    menuItem.setChecked(false);
//                }
//
//                else {
//                    menuItem.setChecked(true);
//                }
//
//                //Closing drawer on item click
//                //drawerLayout.closeDrawers();
////                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////                drawer.closeDrawer(GravityCompat.START);
//
//
//                //Check to see which item was being clicked and perform appropriate action
////                switch (menuItem.getItemId()) {
////                    //Replacing the main content with ContentFragment
////                    case R.id.home:
////                        return true;
////                }
//                int id = menuItem.getItemId();
//
//                if (id == R.id.nav_add) {
//                    Intent init = new Intent(MainActivity.this, AddQuestionActivity.class);
//                    startActivity(init);
//                } else if (id == R.id.nav_gallery) {
//
//                } else if (id == R.id.nav_slideshow) {
//
//                } else if (id == R.id.nav_manage) {
//
//                } else if (id == R.id.nav_share) {
//
//                } else if (id == R.id.nav_send) {
//
//                }
//
//                return true;
//            }
//        });
//    }

}
