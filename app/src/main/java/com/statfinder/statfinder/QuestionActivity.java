package com.statfinder.statfinder;

import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import android.os.AsyncTask;

public class QuestionActivity extends FragmentActivity {
    User currentUser;//  = ((MyApplication) getApplication()).getUser();

    Firebase answeredRef = null; //new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/answeredQuestions/");
    Firebase skippedRef = null; //new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/skippedQuestions/");
    Firebase checkedRef = null; //new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/createdQuestions/");
    boolean flag = false;
    String id = null;
    HashMap.Entry tempQuestion = null;
    MyPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        currentUser = ((MyApplication) getApplication()).getUser();
        answeredRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/AnsweredQuestions/");
        skippedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/SkippedQuestions/");
        checkedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/CreatedQuestions/");


        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(QuestionActivity.this, MainActivity.class));
            }
        });

        Button skip = (Button) findViewById(R.id.skipButton);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase userRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + ((MyApplication) getApplication()).getUser().getId() + "/SkippedQuestions/" + id);
                Long tsLong = System.currentTimeMillis() / 1000;
                userRef.setValue(tsLong);
                startActivity(new Intent(QuestionActivity.this, QuestionActivity.class));
                finish();
            }
        });

        newQuestion(null);

    }

    public void newQuestion(final String questionID) {

        Intent init = getIntent();
        final String cameFrom;
        cameFrom = init.getStringExtra("category");
        //System.out.println("this is the category:" + cameFrom); //testing
        System.out.println("Iterated");
                Firebase questionRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/ModeratorQuestions/General");
                questionRef.orderByPriority().addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         HashMap<String, Object> questionsHashMap = (HashMap) dataSnapshot.getValue();
                         //System.out.println(dataSnapshot);
                         //System.out.println("Classic Print - questionsHashMap: " + questionsHashMap);
                         Iterator it = questionsHashMap.entrySet().iterator();
                         //Temporary entry for iterator that represents a question.
                         tempQuestion = (HashMap.Entry) it.next();
                         //System.out.println("tempQuestion: " + tempQuestion);

                         HashMap.Entry hashmapBuffer = tempQuestion;
                         //while(!flag) {
                         // System.out.println("####################While Loop#####################");
                         //Currently evaluating first question in database
                         if (questionID == null) {
                             id = (String) tempQuestion.getKey();
                         } else {
                             //Implies recursive call happened, iterates to next question relative to current question
                             String bufferQuestionID = (String) hashmapBuffer.getKey();
                             while (!bufferQuestionID.equals(questionID) && it.hasNext()) {
                                 hashmapBuffer = (HashMap.Entry) it.next();
                                 bufferQuestionID = (String) hashmapBuffer.getKey();
                                 //System.out.println(hashmapBuffer.getKey());
                             }
                             //System.out.println("HashMapBuffer: " + hashmapBuffer);
                             if (it.hasNext()) {
                                 hashmapBuffer = (HashMap.Entry) it.next();

                             }
                             else
                             {
                                 finish();
                                 return;

                             }

                             id = (String) hashmapBuffer.getKey();
                         }

                         final HashMap.Entry finalBuffer = hashmapBuffer;

                         //System.out.println("Current question key: " + id);
                         //Nested listeners to check the answered, skipped, and created questions
                         checkedRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(final DataSnapshot checkedSnapshot) {
                                 skippedRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(final DataSnapshot skippedSnapshot) {
                                         answeredRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(DataSnapshot answeredSnapshot) {

                                                 if (answeredSnapshot.getValue() == null && skippedSnapshot.getValue() == null && checkedSnapshot.getValue() == null) {
                                                     //Question has yet to be seen in history

                                                     //Actual question with variables attached to it
                                                     HashMap<String, Object> questionEntry = (HashMap) finalBuffer.getValue(); //stores each node in database
                                                     String questionID = (String)finalBuffer.getKey();
                                                     System.out.println("QuestionID" + questionID);
                                                     //System.out.println("questionEntry: " + questionEntry);
                                                     //Get the category first
                                                     String category = questionEntry.get("Category").toString();

                                                     //if the category from the question matches what the user selects
                                                     //if (category.equals(cameFrom)) {

                                                     //Get the Question name
                                                     String questionName = questionEntry.get("Name").toString().replace('_', ' ');
                                                     System.out.println("Question: " + questionName);
                                                     TextView tv = (TextView) findViewById(R.id.qText);
                                                     tv.setText(questionName);

                                                     /* Set category at top left */
                                                     TextView cat = (TextView) findViewById(R.id.categoryLabel);
                                                     cat.setText("Category: " + category);

                                                     //gets the list of answers for each question
                                                     HashMap<String, Object> answersList = (HashMap) questionEntry.get("Answers");

                                                     //ArrayList for storing vote counts for each answer
                                                     ArrayList<Integer> answerCount = new ArrayList<Integer>();

                                                     System.out.println("Current question's answer list: " + answersList);

                                                     //gets the question name
                                                     Object question = questionEntry.get("Name");
                                                     System.out.println("Current question: " + question);

                                                     //make the answers an array for easy access
                                                     Object[] objectAnswers = answersList.keySet().toArray();
                                                     String[] answers = Arrays.copyOf(objectAnswers, objectAnswers.length, String[].class);

                                                     //Cast all answerCount strings into integers
                                                     for (int j = 0; j < answersList.size(); j++) {
                                                         answerCount.add(Integer.parseInt(answersList.get(answers[j]).toString()));
                                                     }
                                                     System.out.println("answerCount : " + answerCount);

                                                     Bundle bundle = new Bundle();
                                                     bundle.putIntegerArrayList("votes", answerCount);
                                                     bundle.putStringArray("answers", answers);
                                                     bundle.putString("id", questionID);
                                                     bundle.putString("category", category);
                                                     AnswersFragment answerFragment = new AnswersFragment();
                                                     answerFragment.setArguments(bundle);

                                                     ResultsFragment resultFragment = new ResultsFragment();
                                                     resultFragment.setArguments(bundle);

                                                     //For each answer add a button
                                                     ArrayList<Fragment> fragments = new ArrayList<Fragment>();
                                                     fragments.add(answerFragment);
                                                     fragments.add(resultFragment);
                                                     mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
                                                     final MyViewPager pager = (MyViewPager) findViewById(R.id.viewpager);
                                                     pager.setAdapter(mPagerAdapter);
                                                     flag = true;

                                                     //} else {
                                                     //    System.out.println("Error");
                                                     // }

                                                 } else {
                                                     //Question has already been seen, go to next question via recursion
                                                     newQuestion(id);
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

                             @Override
                             public void onCancelled(FirebaseError checkedError) {

                             }
                         });
                     }

                     @Override
                     public void onCancelled(FirebaseError firebaseError) {

                     }
                 }

                );
            }
    }


