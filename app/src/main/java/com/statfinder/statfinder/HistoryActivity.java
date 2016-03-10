package com.statfinder.statfinder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class HistoryActivity extends AppCompatActivity {
    User currentUser;//  = ((MyApplication) getApplication()).getUser();
    Firebase answeredRef = null; //new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/answeredQuestions/");
    Firebase skippedRef = null; //new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/skippedQuestions/");
    Firebase checkedRef = null; //new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/createdQuestions/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        currentUser = ((MyApplication) getApplication()).getUser();
        answeredRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/AnsweredQuestions/");
        skippedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/SkippedQuestions/");
        checkedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/CreatedQuestions/");

//TODO: Finish adding dynamic stuff and make into a fragment
//        HashMap<String, Object> questionsHashMap = (HashMap) dataSnapshot.getValue();
//        final HashMap.Entry finalBuffer = hashmapBuffer;
//
//        //System.out.println("Current question key: " + id);
//        //Nested listeners to check the answered, skipped, and created questions
//        checkedRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(final DataSnapshot checkedSnapshot) {
//                skippedRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(final DataSnapshot skippedSnapshot) {
//                        answeredRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot answeredSnapshot) {
//
//                                if (answeredSnapshot.getValue() == null && skippedSnapshot.getValue() == null && checkedSnapshot.getValue() == null) {
//                                    //Question has yet to be seen in history
//
//                                    //Actual question with variables attached to it
//                                    HashMap<String, Object> questionEntry = (HashMap) finalBuffer.getValue(); //stores each node in database
//                                    String questionID = (String)finalBuffer.getKey();
//                                    System.out.println("QuestionID" + questionID);
//                                    //System.out.println("questionEntry: " + questionEntry);
//                                    //Get the category first
//                                    String category = questionEntry.get("Category").toString();
//
//                                    //if the category from the question matches what the user selects
//                                    //if (category.equals(cameFrom)) {
//
//                                    //Get the Question name
//                                    String questionName = questionEntry.get("Name").toString().replace('_', ' ');
//                                    System.out.println("Question: " + questionName);
//                                    TextView tv = (TextView) findViewById(R.id.qText);
//                                    tv.setText(questionName);
//
//                                                     /* Set category at top left */
//                                    TextView cat = (TextView) findViewById(R.id.categoryLabel);
//                                    cat.setText("Category: " + category);
//
//                                    //gets the list of answers for each question
//                                    HashMap<String, Object> answersList = (HashMap) questionEntry.get("Answers");
//
//                                    //ArrayList for storing vote counts for each answer
//                                    ArrayList<Integer> answerCount = new ArrayList<Integer>();
//
//                                    System.out.println("Current question's answer list: " + answersList);
//
//                                    //gets the question name
//                                    Object question = questionEntry.get("Name");
//                                    System.out.println("Current question: " + question);
//
//                                    //make the answers an array for easy access
//                                    Object[] objectAnswers = answersList.keySet().toArray();
//                                    String[] answers = Arrays.copyOf(objectAnswers, objectAnswers.length, String[].class);
//
//                                    //Cast all answerCount strings into integers
//                                    for (int j = 0; j < answersList.size(); j++) {
//                                        answerCount.add(Integer.parseInt(answersList.get(answers[j]).toString()));
//                                    }
//                                    System.out.println("answerCount : " + answerCount);
//
//                                    Bundle bundle = new Bundle();
//                                    bundle.putIntegerArrayList("votes", answerCount);
//                                    bundle.putStringArray("answers", answers);
//                                    bundle.putString("id", questionID);
//                                    bundle.putString("category", category);
//                                    AnswersFragment answerFragment = new AnswersFragment();
//                                    answerFragment.setArguments(bundle);
//
//                                    ResultsFragment resultFragment = new ResultsFragment();
//                                    resultFragment.setArguments(bundle);
//
//                                    //For each answer add a button
//
//
//                                    //} else {
//                                    //    System.out.println("Error");
//                                    // }
//
//                                } else {
//                                    //Question has already been seen, go to next question via recursion
//                                    System.out.println("Error");
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(FirebaseError firebaseError) {
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(FirebaseError checkedError) {
//
//            }
//        });
    }

}


