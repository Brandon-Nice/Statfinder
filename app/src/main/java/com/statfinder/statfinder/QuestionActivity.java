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
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
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
    String globalCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        currentUser = ((MyApplication) getApplication()).getUser();
        answeredRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/AnsweredQuestions/");
        skippedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/SkippedQuestions/");
        checkedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/CreatedQuestions/");

        final boolean moderatorQuestionStatus = true;

        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(QuestionActivity.this, MainActivity.class));
            }
        });

        final Button skip = (Button) findViewById(R.id.skipButton);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (skip.getText().equals("Skip"))
                {
                    Firebase userRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + ((MyApplication) getApplication()).getUser().getId() + "/SkippedQuestions/" + id);
                    HashMap historyMap = new HashMap();
                    Long tsLong = System.currentTimeMillis() / 1000;
                    historyMap.put("TimeCreated", tsLong);
                    historyMap.put("City", currentUser.getCity());
                    historyMap.put("State", currentUser.getState());
                    historyMap.put("Country", currentUser.getCountry());
                    historyMap.put("Category", globalCategory);
                    userRef.setValue(historyMap);
                    userRef.setPriority(0 - tsLong);
                }

                //startActivity(new Intent(QuestionActivity.this, QuestionActivity.class));
                Intent newInit = new Intent(QuestionActivity.this, QuestionActivity.class);
                newInit.putExtra("category", getIntent().getStringExtra("category"));
                //Tells questionActivity what to pull from database
                newInit.putExtra("questionID", getIntent().getStringExtra("questionID"));

                startActivity(newInit);
                finish();
            }
        });
        final Button flag = (Button) findViewById(R.id.flagButton);
        if (moderatorQuestionStatus)
        {
            flag.setVisibility(View.INVISIBLE);
        }
        flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = globalCategory;
                String questionID = getIntent().getStringExtra("questionID");
                final Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/Questions/" + currentUser.getCountry().replace(' ', '_') + "/"
                + currentUser.getState().replace(' ', '_') + "/" + currentUser.getCity().replace(' ', '_') + "/" + category + "/" + questionID);
                final Firebase flagRef = ref.child("/Flags");
                final Firebase totalRef = ref.child("/Total_Votes");
                flagRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData currentData) {
                        if (currentData.getValue() == null) {
                            currentData.setValue(1);
                        } else {
                            currentData.setValue((Long) currentData.getValue() + 1);
                        }
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(FirebaseError firebaseError, boolean b, final DataSnapshot flagSnapshot) {
                        totalRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot votesSnapshot) {
                                System.out.println(totalRef);
                                System.out.println(votesSnapshot);
                                long totalFlags = (Long) flagSnapshot.getValue();
                                long totalVotes = (Long) votesSnapshot.getValue();
                                long totalInteractions = totalFlags + totalVotes;
                                if (totalInteractions > 10 && totalInteractions < 20)
                                {
                                    if (totalFlags > totalVotes)
                                    {
                                        ref.removeValue();
                                    }
                                }
                                else
                                {
                                    long percentRage = totalFlags/totalInteractions;
                                    if (percentRage > 0.25)
                                    {
                                        ref.removeValue();
                                    }
                                }
                                skip.performClick();
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }
                });
            }
        });



        newQuestion();

    }

    public void newQuestion() {
        final Intent init = getIntent();
        final String cameFrom;
        cameFrom = init.getStringExtra("category");
        //System.out.println("this is the category:" + cameFrom); //testing
        System.out.println("Iterated");
                Firebase questionRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/ModeratorQuestions/General");
                questionRef.orderByPriority().addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         final HashMap<String, Object> questionsHashMap = (HashMap) dataSnapshot.getValue();
                         Iterator it = questionsHashMap.entrySet().iterator();
                         HashMap.Entry tempQuestion = (HashMap.Entry) it.next();
                         id = (String) tempQuestion.getKey();

                         checkedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(final DataSnapshot createdSnapshot) {
                                 //skippedRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                 skippedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(final DataSnapshot skippedSnapshot) {
                                         //answeredRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                         answeredRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                             @Override
                                             public void onDataChange(DataSnapshot answeredSnapshot) {
                                                    if(!cameFrom.equals("Popular") || !cameFrom.equals("Random")) {
                                                        //if (answeredSnapshot.getValue() == null && skippedSnapshot.getValue() == null && checkedSnapshot.getValue() == null) {
                                                        //Question has yet to be seen in history

                                                        //Actual question with variables attached to it
                                                        HashMap<String, String> answeredHistory = (HashMap<String, String>) answeredSnapshot.getValue();
                                                        HashMap<String, String> skippedHistory = (HashMap<String, String>) skippedSnapshot.getValue();
                                                        HashMap<String, String> createdHistory = (HashMap<String, String>) createdSnapshot.getValue();
                                                        Iterator it = questionsHashMap.entrySet().iterator();

                                                 /* Initializes user's history if it does not exist */
                                                        if (answeredSnapshot.getValue() == null || skippedSnapshot.getValue() == null || createdSnapshot.getValue() == null) {
                                                            if (answeredSnapshot.getValue() == null) {
                                                                answeredRef.child("-1").setValue("-1");
                                                            }
                                                            if (skippedSnapshot.getValue() == null) {
                                                                skippedRef.child("-1").setValue("-1");
                                                            }
                                                            if (createdSnapshot.getValue() == null) {
                                                                checkedRef.child("-1").setValue("-1");
                                                            }

                                                        }
                                                 /* Checks for a question user has not seen yet in category */
                                                        else {
                                                            while (answeredHistory.containsKey(id) || skippedHistory.containsKey(id) || createdHistory.containsKey(id)) {
                                                                if (it.hasNext()) {
                                                                    HashMap.Entry tempQuestion = (HashMap.Entry) it.next();
                                                                    System.out.println("Current tempQuestion: " + tempQuestion);
                                                                    id = (String) tempQuestion.getKey();
                                                                } else {
                                                                    //Handle when category is out of questions
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        //Question has already been seen, go to next question via recursion
                                                        //newQuestion(id);
                                                        id = init.getStringExtra("questionID");
                                                    }
                                                     HashMap<String, Object> questionEntry = (HashMap) questionsHashMap.get(id); //(HashMap) finalBuffer.getValue(); //stores each node in database
                                                    String questionID = id; //(String)finalBuffer.getKey();

                                                     System.out.println("QuestionID" + questionID);
                                                     //System.out.println("questionEntry: " + questionEntry);
                                                     //Get the category first
                                                     String category = questionEntry.get("Category").toString();
                                                    globalCategory = category;


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

                                                //}
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


