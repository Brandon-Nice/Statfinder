package com.statfinder.statfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class QuestionFilterActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_question_filter);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
    User currentUser = null;// = ((MyApplication) getApplication()).getUser();
    Firebase answeredRef = null; //new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/answeredQuestions/");
    Firebase skippedRef = null; //new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/skippedQuestions/");
    Firebase checkedRef = null; //new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/createdQuestions/");
    boolean flag = false;
    String id = null;
    HashMap.Entry tempQuestion = null;
    MyPagerAdapter mPagerAdapter;


    public String filterPopular(final String category, final String QuestionID) {
        currentUser = ((MyApplication) getApplication()).getUser();
        answeredRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/AnsweredQuestions/");
        skippedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/SkippedQuestions/");
        checkedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/CreatedQuestions/");
        Intent init = getIntent();
        final String cameFrom;
        cameFrom = init.getStringExtra("category");
        System.out.println("this is the category: " + category); //testing

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
                if (QuestionID == null) {
                    id = (String) tempQuestion.getKey();
                } else {
                    //Implies recursive call happened, iterates to next question relative to current question
                    String bufferQuestionID = (String) hashmapBuffer.getKey();
                    while (!bufferQuestionID.equals(QuestionID) && it.hasNext()) {
                        hashmapBuffer = (HashMap.Entry) it.next();
                        bufferQuestionID = (String) hashmapBuffer.getKey();
                        //System.out.println(hashmapBuffer.getKey());
                    }
                    //System.out.println("HashMapBuffer: " + hashmapBuffer);
                    if (it.hasNext()) {
                        hashmapBuffer = (HashMap.Entry) it.next();

                    } else {
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

                                        int tempmax = 0; //keeps track of the most popular item TODO: Change since this might reinstantiate to 0 each time it is being run
                                        int popularQid = 0;
                                        String[] answers = new String[20];
                                        if (answeredSnapshot.getValue() == null && skippedSnapshot.getValue() == null && checkedSnapshot.getValue() == null) {
                                            //Question has yet to be seen in history

                                            //Actual question with variables attached to it
                                            HashMap<String, Object> questionEntry = (HashMap) finalBuffer.getValue(); //stores each node in database
                                            String questionID = (String) finalBuffer.getKey();
                                            System.out.println("QuestionID" + questionID);
                                            //System.out.println("questionEntry: " + questionEntry);
                                            //Get the category first
                                            String category = questionEntry.get("Category").toString();

                                            //Get the User votes
                                            int votes;


                                            //check for the most popular question
                                            Iterator it = questionEntry.entrySet().iterator();
                                            while(it.hasNext()) {
                                                votes = Integer.parseInt(questionEntry.get("Total_Votes").toString());
                                                System.out.print("Total votes for this question: " + votes);

                                                if (votes > tempmax) {
                                                    tempmax = votes;
                                                    System.out.println("Highest number of votes : " + tempmax);

                                                    //Get the Question ID
                                                    questionID = (String) finalBuffer.getKey();
                                                    System.out.println("Iterating.... Question # " + questionID);


                                                    //Get the Question name
                                                    String questionName = questionEntry.get("Name").toString().replace('_', ' ');
                                                    //System.out.println("Question: " + questionName);
                                                    //TextView tv = (TextView) findViewById(R.id.qText);
                                                    //tv.setText(questionName);

                                                    //gets the list of answers for each question
                                                    HashMap<String, Object> answersList = (HashMap) questionEntry.get("Answers");
                                                    System.out.println("Current question's answer list: " + answersList);

                                                    //gets the question name
                                                    Object question = questionEntry.get("Name");
                                                    System.out.println("Current question: " + question);

                                                    //make the answers an array for easy access
                                                    Object[] objectAnswers = answersList.keySet().toArray();
                                                    answers = Arrays.copyOf(objectAnswers, objectAnswers.length, String[].class);

                                                    //                                                popularQid = Integer.parseInt(questionID);
                                                    //                                                Intent p = new Intent();
                                                    //                                                p.putExtra("pop_qid", popularQid);

                                                    //                                                Button popularQuestionButton = (Button) findViewById(R.id.popularButton);
                                                    //                                                Button randomQuestionButton = (Button) findViewById(R.id.randomButton);
                                                    //                                                randomQuestionButton.setText("Random Question:\n" + questionName);
                                                    //                                                popularQuestionButton.setText("Popular Question:\n" + questionName);
                                                }
                                            }

                                            Bundle bundle = new Bundle();
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
                                            id = questionID;
                                            System.out.println("#### QuestionID #### " + questionID);

                                        } else {
                                            //Question has already been seen, go to next question via recursion
                                            id = filterPopular(category, id);
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
            public void onCancelled(FirebaseError checkedError) {

            }
        });
       // return filterPopular(category, id)
        //Intent i = new Intent(QuestionFilterActivity.this, MainActivity.class);
        //Intent ii = getIntent();
        //String popularQid = ii.getStringExtra("pop_qid");
        //i.putExtra("qid", popularQid);
        //startActivity(init);
        return id;
    }



    public void filterRandom(final String QuestionID, String category){

    }

    //TODO: Eventually put QuestionActivity stuff in here
    public void filterHistory(final String QuestionID){

    }
}
