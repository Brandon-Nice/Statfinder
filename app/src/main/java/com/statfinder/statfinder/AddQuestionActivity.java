package com.statfinder.statfinder;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

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

        final EditText question = (EditText) findViewById(R.id.questionText);
        final EditText firstAnswer = (EditText) findViewById(R.id.answer1);
        final EditText secondAnswer = (EditText) findViewById(R.id.answer2);
        final EditText thirdAnswer = (EditText) findViewById(R.id.answer3);
        final EditText fourthAnswer = (EditText) findViewById(R.id.answer4);
        final EditText fifthAnswer = (EditText) findViewById(R.id.answer5);
        final CheckBox otherCheckBox = (CheckBox) findViewById(R.id.otherCheckBox);
        final Button addAnswer = (Button) findViewById(R.id.addButton);
        final Button minusAnswer = (Button) findViewById(R.id.minusButton);
        final View focusTheif = findViewById(R.id.focus_thief);

        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
                return false;
            }
        });


        addAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numAnswers++;
                if (numAnswers == 3) {
                    minusAnswer.setVisibility(View.VISIBLE);
                    thirdAnswer.setVisibility(View.VISIBLE);
                } else if (numAnswers == 4) {
                    fourthAnswer.setVisibility(View.VISIBLE);
                } else if (numAnswers == 5) {
                    addAnswer.setVisibility(View.INVISIBLE);
                    fifthAnswer.setVisibility(View.VISIBLE);
                }
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
    }


}
