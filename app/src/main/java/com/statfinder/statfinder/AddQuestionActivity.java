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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.io.IOException;
import java.util.ArrayList;
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

        InputFilter characterFilter = new InputFilter() {
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

        InputFilter answerLength = new InputFilter.LengthFilter(50);
        InputFilter questionLength = new InputFilter.LengthFilter(80);

        InputFilter[] answerFilters = new InputFilter[]{characterFilter, answerLength};
        InputFilter[] questionFilters = new InputFilter[]{characterFilter, questionLength};



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
        final Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, ((MyApplication) getApplication()).defCat);
        categorySpinner.setAdapter(adapter);

        question.setFilters(questionFilters);
        firstAnswer.setFilters(answerFilters);
        secondAnswer.setFilters(answerFilters);
        thirdAnswer.setFilters(answerFilters);
        fourthAnswer.setFilters(answerFilters);
        fifthAnswer.setFilters(answerFilters);

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
                if (question.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please enter a question.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (firstAnswer.getText().toString().trim().length() == 0 || secondAnswer.getText().length() == 0)
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
                if (question.getText().toString().trim().equals("Reset database")
                        && firstAnswer.getText().toString().trim().equals("Do")
                        && secondAnswer.getText().toString().trim().equals("It")
                        && ((MyApplication) getApplication()).getUser().getModStatus())
                {
                    ref.child("Questions/").removeValue();
                    ref.child("Questions/NumQuestions").setValue("0");
                    addQuestions();
                }
                String questionName = question.getText().toString().trim();
                final ArrayList<String> answers = new ArrayList();
                answers.add(firstAnswer.getText().toString());
                answers.add(secondAnswer.getText().toString());
                if (thirdAnswer.getText().length() != 0 && thirdAnswer.getVisibility() == View.VISIBLE)
                {
                    answers.add(thirdAnswer.getText().toString());
                }
                if (fourthAnswer.getText().length() != 0 && fourthAnswer.getVisibility() == View.VISIBLE)
                {
                    answers.add(fourthAnswer.getText().toString());
                }
                if (fifthAnswer.getText().length() != 0 && fifthAnswer.getVisibility() == View.VISIBLE)
                {
                    answers.add(fifthAnswer.getText().toString());
                }
                if (otherCheckBox.isChecked())
                {
                    answers.add("Other");
                }
                addQuestions();
                Boolean moderated = ((MyApplication) getApplication()).getUser().getModStatus();
                final String category = categorySpinner.getSelectedItem().toString();
                final HashMap questionInfo = createQuestion(questionName, moderated, answers);
                final String finalCity = city.replaceAll(" ", "_");
                final String finalCountry = country.replaceAll(" ", "_");
                final String finalState = state.replaceAll(" ", "_");
                ref.child("Questions/NumQuestions").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        int value = Integer.parseInt((String) snapshot.getValue(), 16);
                        value++;
                        String incHex = Integer.toHexString(value);
                        ref.child("Questions/NumQuestions").setValue(incHex);
                        Firebase questionRef = ref.child("Questions/" + finalCountry + "/" + finalState + "/" + finalCity + "/" + category + "/" + (String) snapshot.getValue());
                        questionRef.setValue(questionInfo);
                        questionRef.setPriority(0);
                        for (int i = 0; i < answers.size(); i++)
                        {
                            Firebase answerRef = ref.child("Questions/" + finalCountry + "/" + finalState + "/" + finalCity + "/" + category + "/" + (String) snapshot.getValue() + "/Answers/" + answers.get(i));
                            answerRef.setValue(0);
                            answerRef.setPriority(i);
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }
        });
    }

    private void addQuestions() {
        Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/Questions/United_States/Indiana/West_Lafayette/General/");
        ref.child("0").setValue(createQuestion("Which kind of milk do you prefer?", true, new ArrayList<String>() {{add("2%");add("Chocolate");add("Skim");add("Whole milk");add("Other");}}));
        ref.child("1").setValue(createQuestion("Do you believe in love at first sight?", true, new ArrayList<String>() {{add("Yes");add("No");add("Love is Dead");}}));
        ref.child("2").setValue(createQuestion("Which do you prefer:", true, new ArrayList<String>() {{add("Apples");add("Bananas");add("Oranges");}}));
        ref.child("3").setValue(createQuestion("Have you ever stayed up all night?", true, new ArrayList<String>() {{add("Yes");add("No");add("Too high to remember");}}));
        ref.child("4").setValue(createQuestion("What is the best day", true, new ArrayList<String>() {{add("Christmas");add("Halloween");add("Thanksgiving");add("Valentine's");add("Other");}}));
        ref.child("5").setValue(createQuestion("How many puppies would you get if you had unlimited money?", true, new ArrayList<String>() {{add("1");add("5");add("Unlimited puppies!");}}));
        ref.child("6").setValue(createQuestion("jhihihuv", true, new ArrayList<String>() {{add("hrxuif");add("If kcjx");}}));
        ref.child("7").setValue(createQuestion("How can I get rich quick?", true, new ArrayList<String>() {{add("Ask your dad for a small loan of 1 million dollars");add("Die poor");add("Work at Ford for the rest of your life");}}));
        ref.child("8").setValue(createQuestion("What time is it?", true, new ArrayList<String>() {{add("7:12pm");add("Adventure Time!");add("Mail Time");}}));
        ref.child("9").setValue(createQuestion("What theory about the universe do you believe?", true, new ArrayList<String>() {{add("Multiverse theory");add("Reality is an illusion, the universe is a hologram, buy gold, buuuuuuuuuuyyyy!");}}));
        ref.child("a").setValue(createQuestion("Fudge", true, new ArrayList<String>() {{add("buttzzzzz");add("do you");add("feel like");add("making some");}}));
        ref.child("b").setValue(createQuestion("bing", true, new ArrayList<String>() {{add("bang");add("bong");add("kapow");}}));

    }

    private HashMap createQuestion(String question, Boolean moderated, ArrayList<String> answers)
    {
        HashMap questionMap = new HashMap();
        questionMap.put("Flags", 0);
        questionMap.put("Name", question.trim().replaceAll(" ", "_"));
        questionMap.put("Moderated", moderated);
        Long tsLong = System.currentTimeMillis()/1000;
        questionMap.put("Time_Created", tsLong);
        questionMap.put("Total_Votes", 0);
        return questionMap;
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
