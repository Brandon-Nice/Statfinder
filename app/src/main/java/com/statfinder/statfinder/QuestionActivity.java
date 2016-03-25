package com.statfinder.statfinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.DialogPreference;
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
import java.util.LinkedHashMap;
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
    String id = null;
    HashMap.Entry tempQuestion = null;
    MyPagerAdapter mPagerAdapter;
    String globalCategory;
    String globalName;
    HashMap<String, Object> uniqueQuestionEntry;
    String uniqueCategory;
    String uniqueName;
    boolean modStatus = false;
    Button flag;
    Button home;
    Button skip;
    boolean answered = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        currentUser = ((MyApplication) getApplication()).getUser();
        answeredRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/AnsweredQuestions/");
        skippedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/SkippedQuestions/");
        checkedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + currentUser.getId() + "/CreatedQuestions/");

        home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        skip = (Button) findViewById(R.id.skipButton);
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
                    historyMap.put("Name", globalName.replace(' ', '_'));
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
                    newInit.putExtra("modStatus", false);
                }
                else {
                    newInit = new Intent(QuestionActivity.this, QuestionActivity.class);
                    newInit.putExtra("category", getIntent().getStringExtra("category"));
                    newInit.putExtra("questionID", getIntent().getStringExtra("questionID"));
                    newInit.putExtra("Name", getIntent().getStringExtra("Name"));
                    newInit.putExtra("categoryOrigin", getIntent().getStringExtra("categoryOrigin"));
                    newInit.putExtra("modStatus", false);

                }

                startActivity(newInit);
                finish();
            }
        });
        flag = (Button) findViewById(R.id.flagButton);
        flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = globalCategory;
                String questionID = id;
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
                             skippedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(final DataSnapshot skippedSnapshot) {
                                     answeredRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                         @Override
                                         public void onDataChange(DataSnapshot answeredSnapshot) {
                                             HashMap<String, String> answeredHistory = (HashMap<String, String>) answeredSnapshot.getValue();
                                             HashMap<String, String> skippedHistory = (HashMap<String, String>) skippedSnapshot.getValue();
                                             HashMap<String, String> createdHistory = (HashMap<String, String>) createdSnapshot.getValue();
                                             String bestID = "";
                                             HashMap<String, Object> bestQuestion = null;
                                             boolean firstCheck = false;
                                             User tempCurrentUser = ((MyApplication) getApplication()).getUser();
                                             ArrayList<String> userCategories = tempCurrentUser.getSelCat();

                                             /* Signifies the question was a skipped Popular or previous, a new history
                                              needs to be generated similar to the MainActivity */
                                             if (cameFrom.equals("")) {
                                                 if (init.getStringExtra("category").equals("Random")) {
                                                     /* Generate a random question */
                                                     boolean randomCheck = false;
                                                     HashMap<String, Object> allCategories = (HashMap<String, Object>) dataSnapshot.getValue();
                                                     int numCategories = allCategories.size();
                                                     ArrayList<HashMap.Entry> randomQuestions = new ArrayList<HashMap.Entry>(numCategories);
                                                     ArrayList<String> randomCategory = new ArrayList<String>(numCategories);
                                                     int index = 0;
                                                     for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                         if(!userCategories.contains(child.getKey())) {
                                                             continue;
                                                         }
                                                        /* Loop through each category available, add first question from each to HashMap */
                                                         HashMap<String, Object> categoryQuestions = (HashMap<String, Object>) child.getValue();
                                                         Iterator it = categoryQuestions.entrySet().iterator();
                                                         while (it.hasNext()) {
                                                             HashMap.Entry currentQuestion = (HashMap.Entry) it.next();
                                                             HashMap<String, Object> categoryQuestion = (HashMap<String, Object>)currentQuestion.getValue();
                                                             if((boolean)categoryQuestion.get("Moderated") == false && currentUser.getModPreference() == true) {
                                                                 continue;
                                                             }
                                                             if (!answeredHistory.containsKey(currentQuestion.getKey()) && !skippedHistory.containsKey(currentQuestion.getKey())
                                                                     && !createdHistory.containsKey(currentQuestion.getKey())) {
                                                                 randomQuestions.add(index, currentQuestion);
                                                                 randomCategory.add(index, child.getKey());
                                                                 index++;
                                                                 randomCheck = true;
                                                                 break;
                                                             }

                                                         }
                                                     }
                                                    /* For when random question cannot find anymore questions */
                                                     if (randomCheck == false) {
                                                         //When out of questions, returns to home page but home page still shows last questions seen instead of default message
                                                         //TODO dialogueBox 1
                                                         final AlertDialog alertDialog = new AlertDialog.Builder(QuestionActivity.this).create();
                                                         alertDialog.setTitle("Alert");
                                                         alertDialog.setMessage("Sorry there are no available Random questions at the moment.");
                                                         alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                                 new DialogInterface.OnClickListener() {
                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                         dialog.dismiss();
                                                                         finish();
                                                                     }
                                                                 });
                                                         alertDialog.show();
                                                         return;
                                                     }
                                                     /* Generates random index to pull question from */
                                                     int numberOfCategories = randomQuestions.size();
                                                     int randomCategoryIndex = (int) (Math.random() * numberOfCategories);
                                                     System.out.println("Random category index: " + randomCategoryIndex);
                                                     System.out.println("Random questions: " + randomQuestions);
                                                     HashMap.Entry chosenRandomQuestion = randomQuestions.get(randomCategoryIndex);
                                                     HashMap<String, Object> chosenRandomValue = (HashMap<String, Object>) chosenRandomQuestion.getValue();
                                                     id = (String) chosenRandomQuestion.getKey();

                                                     uniqueQuestionEntry = chosenRandomValue;
                                                     uniqueCategory = randomCategory.get(randomCategoryIndex);
                                                     modStatus = (boolean) chosenRandomValue.get("Moderated");
                                                 } else {
                                                     for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                         if(!userCategories.contains(child.getKey())) {
                                                             continue;
                                                         }
                                                         /* Checks for a question user has not seen yet in category */
                                                         for (DataSnapshot currentCategory : child.getChildren()) {
                                                             String firstID = (String) currentCategory.getKey();
                                                             HashMap<String, Object> categoryQuestion = (HashMap<String, Object>)currentCategory.getValue();
                                                             if((boolean)categoryQuestion.get("Moderated") == false && currentUser.getModPreference() == true) {
                                                                 continue;
                                                             }
                                                             if (!answeredHistory.containsKey(firstID) && !skippedHistory.containsKey(firstID) && !createdHistory.containsKey(firstID)) {
                                                                 bestQuestion = (HashMap<String, Object>) currentCategory.getValue();
                                                                 bestID = (String) currentCategory.getKey();
                                                                 firstCheck = true;
                                                                 uniqueCategory = child.getKey();
                                                                 modStatus = (boolean) bestQuestion.get("Moderated");

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
                                                         //No questions left in category or in general, redirect user to home page
                                                         //TODO dialogueBox 2
                                                         final AlertDialog alertDialog = new AlertDialog.Builder(QuestionActivity.this).create();
                                                         alertDialog.setTitle("Alert");
                                                         alertDialog.setMessage("Sorry there are no available questions in this category at the moment.");
                                                         alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                                 new DialogInterface.OnClickListener() {
                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                         dialog.dismiss();
                                                                         finish();
                                                                     }
                                                                 });
                                                         alertDialog.show();
                                                         return;
                                                     }
                                                     boolean bestCheck = false;
                                                    /* Finds category with best question by comparing first found with all categories' best */
                                                     for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                         if(!userCategories.contains(child.getKey())) {
                                                             continue;
                                                         }
                                                         for (DataSnapshot currentCategory : child.getChildren()) {
                                                             HashMap<String, Object> currentQuestion = (HashMap<String, Object>) currentCategory.getValue();
                                                             if ((boolean)currentQuestion.get("Moderated") == false && currentUser.getModPreference() == true) {
                                                                 continue;
                                                             }
                                                             if (!answeredHistory.containsKey(currentCategory.getKey()) && !skippedHistory.containsKey(currentCategory.getKey())
                                                                     && !createdHistory.containsKey(currentCategory.getKey())) {
                                                                 if ((long) bestQuestion.get("Total_Votes") < (long) currentQuestion.get("Total_Votes")) {

                                                                     bestID = currentCategory.getKey();
                                                                     bestQuestion = currentQuestion;
                                                                     bestCheck = true;
                                                                     uniqueCategory = child.getKey();
                                                                     modStatus = (boolean) currentQuestion.get("Moderated");

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

                                                 }
                                             }  /* End of Popular and Random Question initialization */
                                             else { /* Regular category Check */
                                                 if (init.getStringExtra("category").equals("Popular") || init.getStringExtra("category").equals("Random")) {
                                                     id = init.getStringExtra("questionID");
                                                     modStatus = init.getBooleanExtra("modStatus", false);

                                                     if (id.equals("Out of questions!")) {
                                                         //TODO dialogueBox 3
                                                         final AlertDialog alertDialog = new AlertDialog.Builder(QuestionActivity.this).create();
                                                         alertDialog.setTitle("Alert");
                                                         alertDialog.setMessage("Sorry there are no more questions available at the moment.");
                                                         alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                                 new DialogInterface.OnClickListener() {
                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                         dialog.dismiss();
                                                                         finish();
                                                                     }
                                                                 });
                                                         alertDialog.show();
                                                         return;
                                                     }
                                                 } else {
                                                     boolean bestCheck = false;
                                                     for (DataSnapshot currentCategory : dataSnapshot.getChildren()) {
                                                         String firstID = currentCategory.getKey();
                                                         if (!answeredHistory.containsKey(firstID) && !skippedHistory.containsKey(firstID) && !createdHistory.containsKey(firstID)) {
                                                             HashMap<String, Object> currentQuestion = (HashMap<String, Object>) currentCategory.getValue();
                                                             if((boolean)currentQuestion.get("Moderated") == false && currentUser.getModPreference() == true) {
                                                                 continue;
                                                             }
                                                             bestID = currentCategory.getKey();
                                                             bestCheck = true;
                                                             modStatus = (boolean) currentQuestion.get("Moderated");
                                                             break;
                                                         }
                                                     }
                                                     /* No more questions left in category, redirect user to home page */
                                                     if (bestCheck == false) {
                                                         //TODO dialogueBox 4
                                                         final AlertDialog alertDialog = new AlertDialog.Builder(QuestionActivity.this).create();
                                                         alertDialog.setTitle("Alert");
                                                         alertDialog.setMessage("Sorry there are no available questions in this category at the moment.");
                                                         alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                                 new DialogInterface.OnClickListener() {
                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                         dialog.dismiss();
                                                                         finish();
                                                                     }
                                                                 });
                                                         alertDialog.show();
                                                         return;
                                                     }

                                                     id = bestID;
                                                 }

                                             }

                                             HashMap<String, Object> questionEntry;// = null;
                                             /* Initialize Popular and Random questions Hashmaps' differently */
                                             if (init.getStringExtra("category").equals("Popular") || init.getStringExtra("category").equals("Random")) {
                                                 /* Special case for skipped Popular/Random */
                                                 if (init.getStringExtra("categoryOrigin").equals("")) {
                                                     questionEntry = (HashMap) uniqueQuestionEntry;
                                                 } else {
                                                     questionEntry = (HashMap) questionsHashMap.get(id);
                                                 }

                                             } else {
                                                 try {
                                                     questionEntry = (HashMap) questionsHashMap.get(id); //(HashMap) finalBuffer.getValue(); //stores each node in database
                                                 } catch (NullPointerException e) {
                                                /* User selected a category that has no questions */
                                                     //TODO dialogueBox 5
                                                     final AlertDialog alertDialog = new AlertDialog.Builder(QuestionActivity.this).create();
                                                     alertDialog.setTitle("Alert");
                                                     alertDialog.setMessage("Sorry there are no available questions in this category at the moment.");
                                                     alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                             new DialogInterface.OnClickListener() {
                                                                 public void onClick(DialogInterface dialog, int which) {
                                                                     dialog.dismiss();
                                                                     finish();
                                                                 }
                                                             });
                                                     alertDialog.show();
                                                     return;
                                                 }

                                             }

                                             String questionID = id;

                                             //Get the category first
                                             String category;
                                             String questionName;
                                             ArrayList<String> answersList = new ArrayList<String>(6);

                                             if (init.getStringExtra("category").equals("Popular") || init.getStringExtra("category").equals("Random")) {
                                                 if (init.getStringExtra("categoryOrigin").equals("")) {
                                                     category = uniqueCategory;
                                                     questionName = questionEntry.get("Name").toString().replace('_', ' ');

                                                     for(DataSnapshot child : dataSnapshot.getChildren()) {
                                                         if (child.getKey().equals(category)) {
                                                             for (DataSnapshot currentCategory : child.getChildren()) {
                                                                 int answerIndex = 0;
                                                                 if (currentCategory.getKey().equals(id)) {
                                                                     for (DataSnapshot currentQuestion : currentCategory.getChildren()) {
                                                                         for (DataSnapshot currentAnswers : currentQuestion.getChildren()) {
                                                                             answersList.add(answerIndex, currentAnswers.getKey().toString());
                                                                             answerIndex++;
                                                                         }
                                                                     }
                                                                     break;
                                                                 }
                                                             }
                                                         }
                                                     }
                                                 } else {
                                                     category = init.getStringExtra("categoryOrigin");
                                                     questionName = init.getStringExtra("Name").toString().replace('_', ' ');

                                                     int answerIndex = 0;
                                                     System.out.println("CURRENT SPOT 1");
                                                     for(DataSnapshot child : dataSnapshot.getChildren()) {
                                                         System.out.println("child: " + child);
                                                         if(child.getKey().equals(id)) {
                                                             for (DataSnapshot currentQuestion : child.getChildren()) {
                                                                 System.out.println("currentQuestions: " + currentQuestion);
                                                                 for(DataSnapshot currentAnswers : currentQuestion.getChildren()) {
                                                                     System.out.println("currentAnswer: " + currentAnswers);
                                                                     System.out.println("current answer: " + currentAnswers.getKey());
                                                                     System.out.println("current value: " + currentAnswers.getValue());
                                                                     answersList.add(answerIndex, currentAnswers.getKey().toString());
                                                                     answerIndex++;
                                                                 }
                                                             }
                                                             break;
                                                         }
                                                     }

                                                 }
                                             } else {
                                                 category = cameFrom; //questionEntry.get("Category").toString();
                                                 try {
                                                     questionName = questionEntry.get("Name").toString().replace('_', ' ');
                                                 } catch (NullPointerException e) {
                                                     //Question entry passed to QuestionActivity is null
                                                     questionName = null;
                                                 }
                                                 if (questionName == null) {
                                                     //TODO dialogueBox 6
                                                     final AlertDialog alertDialog = new AlertDialog.Builder(QuestionActivity.this).create();
                                                     alertDialog.setTitle("Alert");
                                                     alertDialog.setMessage("Sorry there are no available questions in this category at the moment.");
                                                     alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                             new DialogInterface.OnClickListener() {
                                                                 public void onClick(DialogInterface dialog, int which) {
                                                                     dialog.dismiss();
                                                                     finish();
                                                                 }
                                                             });
                                                     alertDialog.show();

                                                     return;

                                                 }

                                                 for(DataSnapshot child : dataSnapshot.getChildren()) {
                                                     int answerIndex = 0;
                                                     if (child.getKey().equals(id)) {
                                                         for (DataSnapshot currentQuestion : child.getChildren()) {
                                                             for (DataSnapshot currentAnswers : currentQuestion.getChildren()) {
                                                                 answersList.add(answerIndex, currentAnswers.getKey().toString());
                                                                 answerIndex++;
                                                             }
                                                         }
                                                         break;
                                                     }
                                                 }
                                             }
                                             globalCategory = category;

                                             //Get the Question name
                                             globalName = questionName;

                                             if (!modStatus) {
                                                 flag.setVisibility(View.VISIBLE);
                                             }
                                             skip.setVisibility(View.VISIBLE);
                                             home.setVisibility(View.VISIBLE);

                                             TextView tv = (TextView) findViewById(R.id.qText);
                                             if (modStatus) {
                                                 tv.setTextColor(getResources().getColor(R.color.lightBlue));
                                             }
                                             tv.setText(questionName);

                                            /* Set category at top left */
                                             TextView cat = (TextView) findViewById(R.id.categoryLabel);
                                             if (category.equals("SciTech"))
                                             {
                                                 cat.setText("Category: Science and Technology");
                                             }
                                             else {
                                                 cat.setText("Category: " + category);
                                             }

                                             Bundle bundle = new Bundle();
                                             //bundle.putStringArray("answers", answers);
                                             bundle.putStringArrayList("answers", answersList);
                                             bundle.putString("id", questionID);
                                             bundle.putString("category", category);
                                             bundle.putBoolean("modStatus", modStatus);
                                             bundle.putString("cameFrom", "QuestionActivity");

                                             AnswersFragment answerFragment = new AnswersFragment();
                                             answerFragment.setArguments(bundle);

                                             ResultsFragment resultFragment = new ResultsFragment();
                                             resultFragment.setArguments(bundle);


                                             //For each answer add a button
                                             ArrayList<Fragment> fragments = new ArrayList<Fragment>();
                                             fragments.add(answerFragment);
                                             fragments.add(resultFragment);

                                             if (modStatus) {
                                                 StateResultsFragment stateFragment = new StateResultsFragment();
                                                 stateFragment.setArguments(bundle);
                                                 fragments.add(stateFragment);

                                                 CountryResultsFragment countryFragment = new CountryResultsFragment();
                                                 countryFragment.setArguments(bundle);
                                                 fragments.add(countryFragment);

                                                 GlobalResultsFragment globalFragment = new GlobalResultsFragment();
                                                 globalFragment.setArguments(bundle);
                                                 fragments.add(globalFragment);
                                             }

                                             mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
                                             final MyViewPager pager = (MyViewPager) findViewById(R.id.viewpager);
                                             pager.setAdapter(mPagerAdapter);
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

    public boolean getAnswered()
    {
        return answered;
    }

    public void setAnswered(boolean answered)
    {
        this.answered = answered;
    }
}


