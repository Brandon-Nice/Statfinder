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
    String globalName;
    HashMap<String, Object> uniqueQuestionEntry;
    String uniqueCategory;
    String uniqueName;

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
                    historyMap.put("Name", globalName);
                    userRef.setValue(historyMap);
                    userRef.setPriority(0 - tsLong);
                }
                Intent newInit;
                if(getIntent().getStringExtra("category").equals("Popular") || getIntent().getStringExtra("category").equals("Random")) {
                    newInit = new Intent(QuestionActivity.this, QuestionActivity.class);
                    newInit.putExtra("category", getIntent().getStringExtra("category"));
                    newInit.putExtra("questionID", "null");
                    newInit.putExtra("Name", "null");
                    newInit.putExtra("categoryOrigin", "");

                }
                else {
                    newInit = new Intent(QuestionActivity.this, QuestionActivity.class);
                    newInit.putExtra("category", getIntent().getStringExtra("category"));
                    newInit.putExtra("questionID", getIntent().getStringExtra("questionID"));
                    newInit.putExtra("Name", getIntent().getStringExtra("Name"));
                    newInit.putExtra("categoryOrigin", getIntent().getStringExtra("categoryOrigin"));
                }

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
                final Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/Questions/" + currentUser.getCountry() + "/"
                + currentUser.getState() + "/" + currentUser.getCity() + "/" + category + "/" + questionID);
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
                                if (totalInteractions > 10 && totalInteractions < 20) {
                                    if (totalFlags > totalVotes) {
                                        ref.removeValue();
                                    }
                                } else {
                                    long percentRage = totalFlags / totalInteractions;
                                    if (percentRage > 0.25) {
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
        if(init.getStringExtra("category").equals("Popular") || init.getStringExtra("category").equals("Random")) {
            cameFrom = init.getStringExtra("categoryOrigin");
        }else {
            cameFrom = init.getStringExtra("category");
        }


        final String finalCity = currentUser.getCity();
        final String finalCountry = currentUser.getCountry();
        final String finalState = currentUser.getState();

        Firebase questionRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/" + finalCountry + "/" + finalState + "/" + finalCity + "/" + cameFrom);

                questionRef.orderByPriority().addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(final DataSnapshot dataSnapshot) {
                         final HashMap<String, Object> questionsHashMap = (HashMap) dataSnapshot.getValue();


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

                                                 HashMap<String, String> answeredHistory = (HashMap<String, String>) answeredSnapshot.getValue();
                                                 HashMap<String, String> skippedHistory = (HashMap<String, String>) skippedSnapshot.getValue();
                                                 HashMap<String, String> createdHistory = (HashMap<String, String>) createdSnapshot.getValue();
                                                 String bestID = "";
                                                 HashMap<String, Object> bestQuestion = null;
                                                 boolean firstCheck = false;

                                                 /* Signifies the question was a skipped Popular or previous, a new history
                                                  needs to be generated similar to the MainActivity */
                                                 if(cameFrom.equals("")) {
                                                     if(init.getStringExtra("category").equals("Random")) {
                                                         boolean randomCheck = false;
                                                         HashMap<String, Object> allCategories = (HashMap<String, Object>) dataSnapshot.getValue();
                                                         int numberOfCategories = allCategories.size();
                                                         int randomCategoryIndex = (int)(Math.random() * numberOfCategories);
                                                         int currentIndex = 0;
                                                         for(DataSnapshot randomCategory : dataSnapshot.getChildren()) {
                                                             if(currentIndex == randomCategoryIndex) {
                                                                 HashMap<String, Object> categoryQuestions = (HashMap<String, Object>) randomCategory.getValue();
                                                                 int randomQuestionIndex = (int)(Math.random() * categoryQuestions.size());
                                                                 Iterator it = categoryQuestions.entrySet().iterator();
                                                                 for(int j = 0; j < categoryQuestions.size(); j++) {
                                                                     if(j == randomQuestionIndex) {
                                                                         HashMap.Entry chosenRandomQuestion = (HashMap.Entry)it.next();
                                                                         HashMap<String, Object> chosenValue = (HashMap<String, Object>) chosenRandomQuestion.getValue();
                                                                         id = (String)chosenRandomQuestion.getKey();
                                                                         uniqueQuestionEntry = chosenValue;
                                                                         uniqueCategory = randomCategory.getKey();
                                                                         randomCheck = true;
                                                                         break;
                                                                     }
                                                                     else{
                                                                         it.next();
                                                                     }
                                                                 }
                                                                 if(randomCheck) {
                                                                     break;
                                                                 }
                                                             }
                                                             else{
                                                                 currentIndex++;
                                                             }

                                                         }
                                                     }
                                                     else {
                                                         for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                             /* Checks for a question user has not seen yet in category */
                                                             for (DataSnapshot currentCategory : child.getChildren()) {
                                                                 String firstID = (String) currentCategory.getKey();
                                                                 if (!answeredHistory.containsKey(firstID) && !skippedHistory.containsKey(firstID) && !createdHistory.containsKey(firstID)) {
                                                                     bestQuestion = (HashMap<String, Object>) currentCategory.getValue();
                                                                     bestID = (String) currentCategory.getKey();
                                                                     firstCheck = true;
                                                                     uniqueCategory = child.getKey();

                                                                     break;
                                                                 }
                                                                 if (firstCheck) {
                                                                     break;
                                                                 }
                                                             }
                                                             if (firstCheck) {
                                                                 break;
                                                             }
                                                         }
                                                         if (firstCheck == false) {
                                                             //No questions left
                                                         }
                                                         boolean bestCheck = false;
                                                         /* Initializes user's history if it does not exist */

                                                            /* Finds category with best question by comparing first found with all categories' best */
                                                         for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                             for (DataSnapshot currentCategory : child.getChildren()) {
                                                                 HashMap<String, Object> currentQuestion = (HashMap<String, Object>) currentCategory.getValue();
                                                                 if (!answeredHistory.containsKey(currentCategory.getKey()) && !skippedHistory.containsKey(currentCategory.getKey())
                                                                         && !createdHistory.containsKey(currentCategory.getKey())) {
                                                                     if ((long) bestQuestion.get("Total_Votes") < (long) currentQuestion.get("Total_Votes")) {
                                                                         bestID = (String) currentCategory.getKey();
                                                                         bestQuestion = currentQuestion;
                                                                         bestCheck = true;
                                                                         uniqueCategory = child.getKey();
                                                                     } else {
                                                                         break;
                                                                     }
                                                                 }
                                                                 if (bestCheck) {
                                                                     break;
                                                                 }
                                                             }
                                                         }
                                                         id = bestID;
                                                         uniqueQuestionEntry = bestQuestion;

                                                                /* Generate a random question */

                                                     }
                                                 }  /* End of Popular and Random Question initialization */

                                                 else { /* Regular category Check */

                                                 for (DataSnapshot currentCategory : dataSnapshot.getChildren()) {
                                                     String firstID = (String) currentCategory.getKey();
                                                     if (!answeredHistory.containsKey(firstID) && !skippedHistory.containsKey(firstID) && !createdHistory.containsKey(firstID)) {
                                                         bestQuestion = (HashMap<String, Object>) currentCategory.getValue();
                                                         bestID = (String) currentCategory.getKey();

                                                     }
                                                 }

                                                 boolean bestCheck = false;
                                                  /* Finds category with best question by comparing first found with all categories' best */
                                                 for (DataSnapshot currentCategory : dataSnapshot.getChildren()) {
                                                     HashMap<String, Object> currentQuestion = (HashMap<String, Object>) currentCategory.getValue();
                                                     if (!answeredHistory.containsKey(currentCategory.getKey()) && !skippedHistory.containsKey(currentCategory.getKey())
                                                             && !createdHistory.containsKey(currentCategory.getKey())) {
                                                         /* Checks first unseen question in the current category which should also be the most popular
                                                         given that our database is sorted by most popular first */
                                                         if ((long) bestQuestion.get("Total_Votes") < (long) currentQuestion.get("Total_Votes")) {
                                                             bestID = (String) currentCategory.getKey();
                                                             bestQuestion = currentQuestion;
                                                             bestCheck = true;
                                                         } else {
                                                             break;
                                                         }
                                                     }
                                                     if (bestCheck) {
                                                         break;
                                                     }
                                                 }

                                                     id = bestID;
                                             }
                                                 HashMap<String, Object> questionEntry;
                                                 /* Initialize Popular and Random questions Hashmaps' differently */
                                                 if(init.getStringExtra("category").equals("Popular") || init.getStringExtra("category").equals("Random")  ) {
                                                     /* Special case for skipped Popular/Random */
                                                     if(init.getStringExtra("categoryOrigin").equals("")) {
                                                         questionEntry = (HashMap) uniqueQuestionEntry;
                                                     }
                                                     else {
                                                         questionEntry = (HashMap) questionsHashMap.get(id);
                                                     }

                                                 } else {
                                                     questionEntry = (HashMap) questionsHashMap.get(id); //(HashMap) finalBuffer.getValue(); //stores each node in database

                                                 }
                                                 String questionID = id; //(String)finalBuffer.getKey();

                                                     //Get the category first
                                                 String category;
                                                 String questionName;
                                                 if(init.getStringExtra("category").equals("Popular") || init.getStringExtra("category").equals("Random")) {
                                                     if(init.getStringExtra("categoryOrigin").equals("")) {
                                                         category = uniqueCategory;
                                                         questionName = questionEntry.get("Name").toString().replace('_', ' ');
                                                     }
                                                     else {
                                                         category = init.getStringExtra("categoryOrigin");
                                                         questionName = init.getStringExtra("Name").toString().replace('_', ' ');

                                                     }
                                                 }
                                                 else {
                                                     category = cameFrom; //questionEntry.get("Category").toString();
                                                     questionName = questionEntry.get("Name").toString().replace('_', ' ');
                                                 }
                                                 globalCategory = category;


                                                     //if the category from the question matches what the user selects
                                                     //if (category.equals(cameFrom)) {

                                                     //Get the Question name

                                                 globalName = questionName;
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


                                                 //gets the question name
                                                 Object question = questionEntry.get("Name");

                                                 //make the answers an array for easy access
                                                 Object[] objectAnswers = answersList.keySet().toArray();
                                                 String[] answers = Arrays.copyOf(objectAnswers, objectAnswers.length, String[].class);

                                                 //Cast all answerCount strings into integers
                                                 for (int j = 0; j < answersList.size(); j++) {
                                                     answerCount.add(Integer.parseInt(answersList.get(answers[j]).toString()));
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


