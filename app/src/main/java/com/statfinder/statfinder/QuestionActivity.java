package com.statfinder.statfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {
    Firebase questionRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent init = getIntent();
        String cameFrom;
        cameFrom = init.getStringExtra("category");

        TextView categoryText = (TextView)findViewById(R.id.categoryText);
        categoryText.setText(cameFrom);

        //cameFrom now has the category that the user clicks on in the drawer menu. TODO: This will eventually include "popular" and "random"

        questionRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/United%20States/Indiana/West%20Lafayette");
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> questionsHashMap = (HashMap)dataSnapshot.getValue();
                //Random r = new Random();
                //int randomQuestionNumber = r.nextInt(questionsHashMap.size());
                //HashMap<String, Object> questionEntry = questionsHashMap


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


}
