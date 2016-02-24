package com.statfinder.statfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {
    Firebase questionRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent init = getIntent();
        final String cameFrom;
        cameFrom = init.getStringExtra("category");
        System.out.println("this is the category:" + cameFrom); //testing
        TextView categoryText = (TextView)findViewById(R.id.categoryText);
        categoryText.setText(cameFrom);

        //cameFrom now has the category that the user clicks on in the drawer menu. TODO: This will eventually include "popular" and "random"

//        questionRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/United%20States/Indiana/West%20Lafayette");
//        questionRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                HashMap<String, Object> questionsHashMap = (HashMap)dataSnapshot.getValue();
//                //Random r = new Random();
//                //int randomQuestionNumber = r.nextInt(questionsHashMap.size());
//                //HashMap<String, Object> questionEntry = questionsHashMap
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
        //Kenny's playground
        Firebase questionRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/United States/Indiana/West Lafayette");
        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Random r = new Random();
                //int randomQuestionNumber = r.nextInt(questionsHashMap.size());
                //HashMap<String, Object> questionEntry = questionsHashMap
                HashMap<String, Object> questionsHashMap = (HashMap) dataSnapshot.getValue();
                //System.out.println("VALUE OF datasnapshot ENTRY: " + dataSnapshot);
                Iterator it = questionsHashMap.entrySet().iterator();
                //for(int i = 0; i < questionsHashMap.size(); i++) {

                int cool = 0;
                while (it.hasNext()) {
                    HashMap.Entry temptry = (HashMap.Entry) it.next();
                    HashMap<String, Object> coolStuff = (HashMap)temptry.getValue(); //stores each node in database

                    //listen for if the user presses the skip button
                    Button skip = ((Button) findViewById(R.id.skipButton));
                    skip.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                           //TODO: Add code for skip button
                        }
                    });

                    //Get the category first
                    String category = coolStuff.get("Category").toString();
                    if(category.equals(cameFrom)) { //if the category from the question matches what the user selects

                        //Get the Question name
                        String questionName = coolStuff.get("Name").toString();
//                        if(questionName.isEmpty()){
//                            Toast.makeText(getApplicationContext(),
//                                    "Sorry! No questions available at this time :(", Toast.LENGTH_LONG).show();
//                        }
                        TextView tv = (TextView) findViewById(R.id.questionContent);
                        tv.setText(questionName);

                        //Answers are essentially an array list with String keys and int values for each entry
                        System.out.println("Shit that matters: " + coolStuff.get("Answers")); //testing
                        System.out.println(cool + " : " + temptry.getKey() + " - " + temptry.getValue()); //testing

                        HashMap<String, String> answersList = (HashMap) coolStuff.get("Answers"); //gets the list of answers for each question
                        System.out.println("Yooooo" + answersList.get(0)); //testing

                        if (answersList.size() > 2) {
                            System.out.println("Multiple answers detected");
                            //startActivity(new Intent(QuestionActivity.this, DynamicLayoutActivity.class));

                            //pass the size to the activity
                            Intent init = new Intent(QuestionActivity.this, DynamicLayoutActivity.class);
                            init.putExtra("size", String.valueOf(answersList.size()));

                            //pass the actual answers to the activity
                            for (int i = 0; i < answersList.size(); i++){
                                init.putExtra("answer", answersList.get("")); //TODO: Figure out how to get the actual answers
                            }
                            startActivity(init);

                            //for(int i = 0; i < answersList.size(); i++) { //for each answer add a button
//                                LinearLayout ll = (LinearLayout) findViewById(R.id.buttonlayout);
//                                ll.setOrientation(LinearLayout.VERTICAL);
//                                Button btn = new Button(QuestionActivity.this);
//                                btn.setText("Push Me");
//                                btn.setId(i);
                                //AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
                                //ll.addView(btn, lp);
                           //}

                        } else if (answersList.size() == 2) {
                            /* Creating buttons */
                            Button answer1Button = (Button) findViewById(R.id.answer1Button);
                            Button answer2Button = (Button) findViewById(R.id.answer2Button);

            /* Send user to ResultsActivity on answer selection */
                            answer1Button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(QuestionActivity.this, ResultsActivity.class));
                                }
                            });

                            answer2Button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(QuestionActivity.this, ResultsActivity.class));
                                }
                            });
                        } else {
                            System.out.println("Error");
                        }
                        cool++;

                    }



//                    else {
//                        Toast.makeText(getApplicationContext(),
//                                "Sorry! No questions available at this time :(", Toast.LENGTH_LONG).show();
//
//                    }

                    //cool++;
                }
                //}


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //Kenny's domain ends here

//        /* Creating buttons */
//        Button answer1Button = (Button)findViewById(R.id.answer1Button);
//        Button answer2Button = (Button)findViewById(R.id.answer2Button);
//
//        /* Send user to ResultsActivity on answer selection */
//        answer1Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(QuestionActivity.this, ResultsActivity.class));
//            }
//        });
//
//        answer2Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(QuestionActivity.this, ResultsActivity.class));
//            }
//        });
    }


}
