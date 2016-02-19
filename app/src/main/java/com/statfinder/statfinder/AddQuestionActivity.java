package com.statfinder.statfinder;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class AddQuestionActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    int numAnswers = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Firebase.setAndroidContext(this);

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (source.charAt(i) == '/' || source.charAt(i) == '.' || source.charAt(i) == '#' || source.charAt(i) == '$'
                            || source.charAt(i) == '[' || source.charAt(i) == ']') {
                        return "";
                    }
                }
                return null;
            }
        };


        final EditText question = (EditText) findViewById(R.id.questionText);
        final EditText firstAnswer = (EditText) findViewById(R.id.answer1);
        final EditText secondAnswer = (EditText) findViewById(R.id.answer2);
        final EditText thirdAnswer = (EditText) findViewById(R.id.answer3);
        final EditText fourthAnswer = (EditText) findViewById(R.id.answer4);
        final EditText fifthAnswer = (EditText) findViewById(R.id.answer5);
        final CheckBox otherCheckBox = (CheckBox) findViewById(R.id.otherCheckBox);
        final Button addAnswer = (Button) findViewById(R.id.addButton);
        final Button minusAnswer = (Button) findViewById(R.id.minusButton);
        final Button submitButton = (Button) findViewById(R.id.submitButton);
        final View focusTheif = findViewById(R.id.focus_thief);

        question.setFilters(new InputFilter[]{filter});
        firstAnswer.setFilters(new InputFilter[]{filter});
        secondAnswer.setFilters(new InputFilter[]{filter});
        thirdAnswer.setFilters(new InputFilter[]{filter});
        fourthAnswer.setFilters(new InputFilter[]{filter});
        fifthAnswer.setFilters(new InputFilter[]{filter});

        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        question.requestFocus();

        question.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    focusTheif.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
                return false;
            }
        });
        firstAnswer.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    focusTheif.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
                return false;
            }
        });
        secondAnswer.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    focusTheif.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
                return false;
            }
        });
        thirdAnswer.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    focusTheif.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
                return false;
            }
        });
        fourthAnswer.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    focusTheif.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
                return false;
            }
        });
        fifthAnswer.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    focusTheif.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
                return false;
            }
        });


        addAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (numAnswers == 2) {
                    if (secondAnswer.getText().length() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Please enter a second answer before adding third", Toast.LENGTH_LONG).show();
                        return;
                    }
                    minusAnswer.setVisibility(View.VISIBLE);
                    thirdAnswer.setVisibility(View.VISIBLE);
                } else if (numAnswers == 3) {
                    if (thirdAnswer.getText().length() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Please enter a third answer before adding fourth", Toast.LENGTH_LONG).show();
                        return;
                    }
                    fourthAnswer.setVisibility(View.VISIBLE);
                } else if (numAnswers == 4) {
                    if (fourthAnswer.getText().length() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Please enter a fourth answer before adding fifth", Toast.LENGTH_LONG).show();
                        return;
                    }
                    addAnswer.setVisibility(View.INVISIBLE);
                    fifthAnswer.setVisibility(View.VISIBLE);
                }
                numAnswers++;
            }
        });


        minusAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numAnswers--;
                if (numAnswers == 2) {
                    minusAnswer.setVisibility(View.INVISIBLE);
                    thirdAnswer.setVisibility(View.GONE);
                } else if (numAnswers == 3) {
                    fourthAnswer.setVisibility(View.GONE);
                } else if (numAnswers == 4) {
                    addAnswer.setVisibility(View.VISIBLE);
                    fifthAnswer.setVisibility(View.GONE);
                }


            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (question.getText().length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please enter a question.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (firstAnswer.getText().length() == 0 && secondAnswer.getText().length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please enter at least two answers.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (firstAnswer.getText().toString().equals(secondAnswer.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Please enter two different answers.", Toast.LENGTH_LONG).show();
                    return;
                }
                Location location = null;
                double longitude;
                double latitude;
                if (ActivityCompat.checkSelfPermission(AddQuestionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    location = getLastKnownLocation();
                }
                if (location == null) {
                    Toast.makeText(getApplicationContext(), "Please enable location services so we could get your location", Toast.LENGTH_LONG).show();
                    return;
                }
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                Geocoder geocoder = new Geocoder(AddQuestionActivity.this);
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
                final Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/");
                if (question.getText().toString().equals("Reset database") && firstAnswer.getText().toString().equals("Do") && secondAnswer.getText().toString().equals("It") && ((MyApplication) getApplication()).getUser().getModStatus())
                {
                    ref.child("Questions/").removeValue();
                    ref.child("Questions/NumQuestions").setValue("0");
                }
                final HashMap<String, Object> idMap = new HashMap();
                final HashMap<String, Object> questionInfo = new HashMap();
                HashMap<String, String> answersMap = new HashMap();
                answersMap.put(firstAnswer.getText().toString(), "0");
                answersMap.put(secondAnswer.getText().toString(), "0");
                if (thirdAnswer.getText().length() != 0 && thirdAnswer.getVisibility() == View.VISIBLE)
                {
                    answersMap.put(thirdAnswer.getText().toString(), "0");
                }
                if (fourthAnswer.getText().length() != 0 && fourthAnswer.getVisibility() == View.VISIBLE)
                {
                    answersMap.put(fourthAnswer.getText().toString(), "0");
                }
                if (fifthAnswer.getText().length() != 0 && fifthAnswer.getVisibility() == View.VISIBLE)
                {
                    answersMap.put(fourthAnswer.getText().toString(), "0");
                }
                if (otherCheckBox.isChecked())
                {
                    answersMap.put("Other", "0");
                }
                questionInfo.put("Name", question.getText().toString());
                questionInfo.put("Answers", answersMap);
                questionInfo.put("Flags", "0");
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                questionInfo.put("Time Created", ts);
                questionInfo.put("Moderated", ((MyApplication) getApplication()).getUser().getModStatus());
                //TODO: Add categories to the questions
                // questionInfo.put("Categories", ...);
                final String finalCity = city;
                final String finalCountry = country;
                final String finalState = state;
                ref.child("Questions/NumQuestions").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        int value = Integer.parseInt((String) snapshot.getValue(), 16);
                        value++;
                        String incHex = Integer.toHexString(value);
                        ref.child("Questions/NumQuestions").setValue(incHex);
                        ref.child("Questions/" + finalCountry + "/" + finalState + "/" + finalCity + "/" + (String) snapshot.getValue()).setValue(questionInfo);
                        finish();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            final ViewGroup viewGroup = (ViewGroup) this.findViewById(R.id.page);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewGroup.getWindowToken(), 0);
        }
        return super.onOptionsItemSelected(item);
    }
}
