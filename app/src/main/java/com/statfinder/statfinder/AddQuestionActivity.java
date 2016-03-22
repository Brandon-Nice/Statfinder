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
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
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
    final int SPACE = 32;


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
                    if (!(source.charAt(i) >= SPACE && source.charAt(i) <= '~') || source.charAt(i) == '/' || source.charAt(i) == '.' || source.charAt(i) == '#' || source.charAt(i) == '$'
                            || source.charAt(i) == '[' || source.charAt(i) == ']' || source.charAt(i) == '_') {
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
                    if (secondAnswer.getText().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please enter a second answer before adding third", Toast.LENGTH_LONG).show();
                        return;
                    }
                    minusAnswer.setVisibility(View.VISIBLE);
                    thirdAnswer.setVisibility(View.VISIBLE);
                } else if (numAnswers == 3) {
                    if (thirdAnswer.getText().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please enter a third answer before adding fourth", Toast.LENGTH_LONG).show();
                        return;
                    }
                    fourthAnswer.setVisibility(View.VISIBLE);
                } else if (numAnswers == 4) {
                    if (fourthAnswer.getText().length() == 0) {
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
                if (question.getText().toString().equals("Reset Database"))
                {
                    resetDatabase();
                    return;
                }
                if (question.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter a question.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (firstAnswer.getText().toString().trim().length() == 0 || secondAnswer.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter at least two answers.", Toast.LENGTH_LONG).show();
                    return;
                }
                /*if (firstAnswer.getText().toString().equals(secondAnswer.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter two different answers.", Toast.LENGTH_LONG).show();
                    return;
                }*/

                final User currentUser = ((MyApplication) getApplication()).getUser();
                String city = currentUser.getCity();
                String state = currentUser.getState();
                String country = currentUser.getCountry();
                final Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/");

                final String questionName = question.getText().toString().trim();
                final ArrayList<String> answers = new ArrayList();
                answers.add(firstAnswer.getText().toString().trim());
                answers.add(secondAnswer.getText().toString().trim());
                if (thirdAnswer.getText().length() != 0 && thirdAnswer.getVisibility() == View.VISIBLE) {
                    answers.add(thirdAnswer.getText().toString().trim());
                }
                if (fourthAnswer.getText().length() != 0 && fourthAnswer.getVisibility() == View.VISIBLE) {
                    answers.add(fourthAnswer.getText().toString().trim());
                }
                if (fifthAnswer.getText().length() != 0 && fifthAnswer.getVisibility() == View.VISIBLE) {
                    answers.add(fifthAnswer.getText().toString().trim());
                }
                if (otherCheckBox.isChecked()) {
                    answers.add("Other");
                }
                Boolean moderated = ((MyApplication) getApplication()).getUser().getModStatus();
                final String category = categorySpinner.getSelectedItem().toString();
                final HashMap questionInfo = createQuestion(questionName, moderated, answers);
                final String finalCity = city.replace(" ", "_");
                final String finalCountry = country.replace(" ", "_");
                final String finalState = state.replace(" ", "_");
                final Firebase numQuestionRef = ref.child("Questions/NumQuestions");
                numQuestionRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        if (mutableData.getValue() == null) {
                            mutableData.setValue("1");
                        } else {
                            String value = AddQuestionActivity.this.incrementHex((String) mutableData.getValue());
                            mutableData.setValue(value);
                        }
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot);
                        String idNumber = decrementHex((String) dataSnapshot.getValue());
                        Firebase questionRef = ref.child("Questions/" + finalCountry + "/" + finalState + "/" + finalCity + "/" + category + "/" + idNumber);
                        questionRef.setValue(questionInfo);
                        questionRef.setPriority(0);
                        for (int i = 0; i < answers.size(); i++) {
                            Firebase answerRef = questionRef.child("/Answers/" + answers.get(i));
                            answerRef.setValue(0);
                            answerRef.setPriority(i);
                        }

                        Firebase userRef = ref.child("Users/" + ((MyApplication) getApplication()).getUser().getId() + "/CreatedQuestions/" + idNumber);
                        HashMap historyMap = new HashMap();
                        Long tsLong = System.currentTimeMillis() / 1000;
                        historyMap.put("TimeCreated", tsLong);
                        historyMap.put("City", currentUser.getCity());
                        historyMap.put("State", currentUser.getState());
                        historyMap.put("Country", currentUser.getCountry());
                        historyMap.put("Category", category);
                        historyMap.put("Name", questionName);
                        userRef.setValue(historyMap);
                        userRef.setPriority(0 - tsLong);

                        finish();
                    }
                });
            }
        });
    }


    private HashMap createQuestion(String question, Boolean moderated, ArrayList<String> answers)
    {
        HashMap questionMap = new HashMap();
        questionMap.put("Flags", 0);
        questionMap.put("Name", question.trim().replace(" ", "_"));
        questionMap.put("Moderated", moderated);
        questionMap.put("Total_Votes", 0);
        return questionMap;
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

    public String decrementHex(String value)
    {
        char[] hexValue = value.toCharArray();
        boolean carryOne = true;
        for (int i = hexValue.length - 1; i >= 0 && carryOne == true; i--)
        {
            carryOne = false;
            if (hexValue[i] == 'f')
            {
                hexValue[i] = 'e';
            }
            else if (hexValue[i] == 'e')
            {
                hexValue[i] = 'd';
            }
            else if (hexValue[i] == 'd')
            {
                hexValue[i] = 'c';
            }
            else if (hexValue[i] == 'c')
            {
                hexValue[i] = 'b';
            }
            else if (hexValue[i] == 'b')
            {
                hexValue[i] = 'a';
            }
            else if (hexValue[i] == 'a')
            {
                hexValue[i] = '9';
            }
            else if (hexValue[i] == '9')
            {
                hexValue[i] = '8';
            }
            else if (hexValue[i] == '8')
            {
                hexValue[i] = '7';
            }
            else if (hexValue[i] == '7')
            {
                hexValue[i] = '6';
            }
            else if (hexValue[i] == '6')
            {
                hexValue[i] = '5';
            }
            else if (hexValue[i] == '5')
            {
                hexValue[i] = '4';
            }
            else if (hexValue[i] == '4')
            {
                hexValue[i] = '3';
            }
            else if (hexValue[i] == '3')
            {
                hexValue[i] = '2';
            }
            else if (hexValue[i] == '2')
            {
                hexValue[i] = '1';
            }
            else if (hexValue[i] == '1')
            {
                hexValue[i] = '0';
            }
            else if (hexValue[i] == '0')
            {
                hexValue[i] = 'f';
                carryOne = true;
            }
        }

        if (carryOne == true)
        {
            String tempHexValue = new String(hexValue).substring(1);
            hexValue = tempHexValue.toCharArray();
        }
        return new String(hexValue);
    }

    private void resetDatabase()
    {
        Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/");
        Firebase questionRef = ref.child("Questions");
        Firebase moderatorQuestions = questionRef.child("ModeratorQuestions");
        Firebase moderatorGeneral = moderatorQuestions.child("General");
        Firebase moderatorGames = moderatorQuestions.child("Games");
        Firebase moderatorSports = moderatorQuestions.child("Sports");
    }
}
