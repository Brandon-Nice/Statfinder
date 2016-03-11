package com.statfinder.statfinder;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HistoryActivity extends AppCompatActivity {
    User currentUser;//  = ((MyApplication) getApplication()).getUser();
    Firebase answeredRef = null; //new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/answeredQuestions/");
    Firebase skippedRef = null; //new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/skippedQuestions/");
    Firebase checkedRef = null; //new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/createdQuestions/");
    Firebase qid = null;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        currentUser = ((MyApplication) getApplication()).getUser();
        answeredRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/AnsweredQuestions/");
        skippedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/SkippedQuestions/");
        checkedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/CreatedQuestions/");
        Firebase questionRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/ModeratorQuestions/General");

//TODO: Finish adding dynamic stuff and make into a fragment
        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final DataSnapshot savedSnapshot = dataSnapshot;
                /* Obtain first question from current category to serve as starting point for iteraton */
                final HashMap<String, Object> questionsHashMap = (HashMap) savedSnapshot.getValue();
                Iterator it = questionsHashMap.entrySet().iterator();
                HashMap.Entry tempQuestion = (HashMap.Entry) it.next();
                id = (String) tempQuestion.getKey();

                                                    /* Nested listeners to obtain all of the user's question history */
                if (dataSnapshot != null) {
                    checkedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot createdSnapshot) {
                            skippedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot skippedSnapshot) {
                                    answeredRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot answeredSnapshot) {
                                            //HashMap<String, Object> questionsHashMap = (HashMap) savedSnapshot.getValue();
                                            //Iterator it = questionsHashMap.entrySet().iterator();
                                            HashMap<String, Object> answeredHistory = (HashMap<String, Object>) answeredSnapshot.getValue();
                                            Iterator it1 = answeredHistory.entrySet().iterator();
                                            HashMap<String, Object> skippedHistory = (HashMap<String, Object>) skippedSnapshot.getValue();
                                            Iterator it2 = skippedHistory.entrySet().iterator();
                                            HashMap<String, Object> createdHistory = (HashMap<String, Object>) createdSnapshot.getValue();
                                            Iterator it3 = createdHistory.entrySet().iterator();

                                            HashMap<String, Object> history = new HashMap<String, Object>(answeredHistory.size() + skippedHistory.size() + createdHistory.size());

                                            //Iterator it = questionsHashMap.entrySet().iterator();
                                            int j = 0;

                                            while(it1.hasNext()) { //checks if there is a next element in ANSWERED HISTORY
                                                HashMap.Entry tempQuestion = (HashMap.Entry) it1.next(); //actually iterates to the next element
                                                System.out.println("Value?? it1 " + tempQuestion.getValue());

                                                if(tempQuestion.getValue().toString().equals("-1")){ //if they've reached the dummy value
                                                    if(it1.hasNext()){ //check to see if there are other elements. skip it
                                                        tempQuestion = (HashMap.Entry) it1.next();
                                                    }

                                                    else //or, get out of the loop
                                                        break;
                                                }

                                                try {
                                                    HashMap<String, Object> questionValues = (HashMap<String, Object>) tempQuestion.getValue();
                                                    history.put(String.valueOf(j), questionValues.get("Name"));
                                                }

                                                catch (NoSuchElementException n){
                                                    n.printStackTrace();
                                                }

                                                System.out.println("History item at indice " + j + " is: " + history.get(String.valueOf(j)).toString()); //testing
                                                j++;
                                            }

                                            while(it2.hasNext()){ //checks if there is a next element in SKIPPED HISTORY
                                                HashMap.Entry tempQuestion = (HashMap.Entry) it2.next();
                                                System.out.println("Value?? it2 " + tempQuestion.getValue());

                                                if(tempQuestion.getValue().toString().equals("-1")){ //if they've reached the dummy value
                                                    if(it2.hasNext()){ //check to see if there are other elements. skip it
                                                        tempQuestion = (HashMap.Entry) it2.next();
                                                    }

                                                    else //or, get out of the loop
                                                        break;
                                                }

                                                try {
                                                    HashMap<String, Object> questionValues = (HashMap<String, Object>) tempQuestion.getValue();
                                                    history.put(String.valueOf(j), questionValues.get("Name"));
                                                }

                                                catch (NoSuchElementException n){
                                                    n.printStackTrace();
                                                }

                                                System.out.println("History item at indice " + j + " is: " + history.get(String.valueOf(j)).toString()); //testing
                                                j++;
                                            }

                                            while(it3.hasNext()){ //checks if there is a next element in CREATED HISTORY
                                                HashMap.Entry tempQuestion = (HashMap.Entry) it3.next();
                                                System.out.println("Value?? it3 " + tempQuestion.getValue());

                                                if(tempQuestion.getValue().toString().equals("-1")){ //if they've reached the dummy value
                                                    if(it3.hasNext()){ //check to see if there are other elements. skip it
                                                        tempQuestion = (HashMap.Entry) it3.next();
                                                    }

                                                    else //or, get out of the loop
                                                        break;
                                                }

                                                try {
                                                    HashMap<String, Object> questionValues = (HashMap<String, Object>) tempQuestion.getValue();
                                                    history.put(String.valueOf(j), questionValues.get("Name"));
                                                }

                                                catch (NoSuchElementException n){
                                                    n.printStackTrace();
                                                }

                                                System.out.println("History item at indice " + j + " is: " + history.get(String.valueOf(j)).toString()); //testing
                                                j++;
                                            }

                                            RelativeLayout r1 = (RelativeLayout)findViewById(R.id.historylayout);
                                            ScrollView sv = new ScrollView(HistoryActivity.this);
                                            sv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
                                            LinearLayout ll = new LinearLayout(HistoryActivity.this);
                                            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            ll.setPadding(0,50,0,0); //left top right bottom
                                            sv.addView(ll);

                                            for(int k = 0; k < history.size(); k++){

                                                String questionName = history.get(String.valueOf(k)).toString(); //gets the first question
                                                System.out.println("questionName: " + questionName);
                                                String newQ = questionName.replaceAll("_", " "); //gets rid of ugly underscores

                                                TextView tv = new TextView(HistoryActivity.this);
                                                tv.setText(newQ);
                                                ll.addView(tv);
//                                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                                View v = inflater.inflate(R.layout.content_history, null);
//
//                                                // Create a LinearLayout element
//                                                LinearLayout ll = (LinearLayout)findViewById(R.id.historylayout);
//                                                ll.setOrientation(LinearLayout.VERTICAL);
//
//                                                // Find the ScrollView
//                                                ScrollView sv = (ScrollView) v.findViewById(R.id.scrollView);
//
//                                                // Add text
//                                                TextView tv = new TextView(HistoryActivity.this);
//                                                tv.setText(newQ);
//                                                ll.addView(tv);
//
//                                                // Add the LinearLayout element to the ScrollView
//                                                sv.addView(ll);
//
//                                                // Display the view
//                                                setContentView(v);

                                            }
                                            r1.addView(sv);
                                            //HistoryActivity.this.setContentView(sv);
                                        }
                                           // }
                                            //Button popularQuestionButton = (Button) findViewById(R.id.popularButton);
                                            //Button randomQuestionButton = (Button) findViewById(R.id.randomButton);
                                            //String questionName;

                                            //HashMap.Entry temptry = (HashMap.Entry) it.next();
                                            //HashMap<String, Object> entries = (HashMap) temptry.getValue();

                                            /* Question gets pulled from it's place in the Hashmap */
                                            // HashMap<String, Object> newQuestion = (HashMap) questionsHashMap.get(id);
//                                            String questionName = newQuestion.get("Name").toString(); //gets the first question
//                                            String newQ = questionName.replaceAll("_", " "); //gets rid of ugly underscores
//                                            System.out.println("Qtext after mod: " + newQ);
//                                            System.out.println("Historyyyyy " + history.get(String.valueOf(0)).toString());
//


//                                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                            View v = inflater.inflate(R.layout.content_history, null);
//
//
//                                            // Find the ScrollView
//                                            ScrollView sv = (ScrollView) v.findViewById(R.id.scrollView);
//
//                                            // Create a LinearLayout element
//                                            LinearLayout ll = new LinearLayout(HistoryActivity.this);
//                                            ll.setOrientation(LinearLayout.VERTICAL);
//
//                                            // Add text
//                                            TextView tv = new TextView(HistoryActivity.this);
//                                            tv.setText(newQ);
//                                            ll.addView(tv);
//
//                                            // Add the LinearLayout element to the ScrollView
//                                            sv.addView(ll);
//
//                                            // Display the view
//                                            setContentView(v);

                                            //randomQuestionButton.setText("Random Question:\n" + moddedQ);
                                            //popularQuestionButton.setText("Popular Question:\n" + moddedQ);
                                        //}


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
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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


