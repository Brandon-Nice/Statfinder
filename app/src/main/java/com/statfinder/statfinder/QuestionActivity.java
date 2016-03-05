package com.statfinder.statfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class QuestionActivity extends FragmentActivity {

    MyPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(QuestionActivity.this, MainActivity.class));
            }
        });

        newQuestion();
        //listen for if the user presses the skip button
//        Button skip = ((Button) findViewById(R.id.skipButton));
//
//        skip.setOnClickListener(new View.OnClickListener()
//
//                                {
//                                    public void onClick(View view) {
//                                        newQuestion();
//                                    }
//                                }
//
//        );

    }

    public void newQuestion() {
        Intent init = getIntent();
        final String cameFrom;
        cameFrom = init.getStringExtra("category");
        System.out.println("this is the category:" + cameFrom); //testing

        Firebase questionRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/ModeratorQuestions/General");
        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, Object> questionsHashMap = (HashMap) dataSnapshot.getValue();
                Iterator it = questionsHashMap.entrySet().iterator();

                HashMap.Entry temptry = (HashMap.Entry) it.next();
                HashMap<String, Object> coolStuff = (HashMap) temptry.getValue(); //stores each node in database

                //Get the category first
                String category = coolStuff.get("Category").toString();

                //if the category from the question matches what the user selects
                if (category.equals(cameFrom)) {

                    //Get the Question name
                    String questionName = coolStuff.get("Name").toString();
                    System.out.println("Question: " + questionName);
                    TextView tv = (TextView) findViewById(R.id.qText);
                    tv.setText(questionName);


                    HashMap<String, Object> answersList = (HashMap) coolStuff.get("Answers"); //gets the list of answers for each question
                    System.out.println("Check");
                    System.out.println("Current question's answer list: " + answersList);
                    Object question = coolStuff.get("Name"); //gets the question
                    System.out.println("Current question: " + question);
                    //System.out.println("Yooooo " + answersList.entrySet() + " : " +  answersList.keySet()); //testing

                    Object[] objectAnswers = answersList.keySet().toArray(); //make the answers an array for easy access
                    String[] answers = Arrays.copyOf(objectAnswers, objectAnswers.length, String[].class);

                    //pass the size to the activity
                    int mapsize = answersList.size();

                    Bundle bundle = new Bundle();
                    bundle.putStringArray("answers", answers);
                    AnswersFragment answerFragment = new AnswersFragment();
                    answerFragment.setArguments(bundle);

                    ResultsFragment resultFragment = new ResultsFragment();

                    //For each answer add a button
                    ArrayList<Fragment> fragments = new ArrayList<Fragment>();
                    fragments.add(answerFragment);
                    fragments.add(resultFragment);
                    mPagerAdapter  = new MyPagerAdapter(getSupportFragmentManager(), fragments);
                    final MyViewPager pager = (MyViewPager) findViewById(R.id.viewpager);
                    pager.setAdapter(mPagerAdapter);

                    /*for (int i = 0; i < mapsize; i++) {
                        LinearLayout ll = (LinearLayout) findViewById(R.id.buttonlayout);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        Button btn = new Button(QuestionActivity.this);
                        btn.setText(answers[i].toString()); //set each button with the corresponding text
                        //btn.setId(i);
                        LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        ll.addView(btn, lp);
                    }*/

                } else {
                    System.out.println("Error");
                }

            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

}

