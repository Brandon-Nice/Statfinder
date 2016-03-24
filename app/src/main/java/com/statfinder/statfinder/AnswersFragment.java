package com.statfinder.statfinder;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

/**
 * Created by michaelrollberg on 2/25/16.
 */
public class AnswersFragment extends Fragment {

    private LinearLayout llLayout;
    private FragmentActivity factivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        factivity = getActivity();
        llLayout = (LinearLayout) inflater.inflate(R.layout.fragment_answers, container, false);
        llLayout.setOrientation(LinearLayout.VERTICAL);

        String activityType = null;

        String[] answers = getArguments().getStringArray("answers");
        final String category = getArguments().getString("category");
        final String questionID = getArguments().getString("id");
        final Boolean modStatus = getArguments().getBoolean("modStatus");
        final String cameFrom = getArguments().getString("cameFrom");



        final MyViewPager viewpager = (MyViewPager) getActivity().findViewById(R.id.viewpager);
        final Button nextButton = (Button) getActivity().findViewById(R.id.skipButton);

        final Button flagButton = (Button) getActivity().findViewById(R.id.flagButton);

        final Button[] buttonList = new Button[answers.length];

        System.out.println("CameFrom: " + cameFrom);

        if (cameFrom.equals("AnsweredHistory") )
        {
            viewpager.setPagingEnabled(true);
            if (factivity instanceof QuestionActivityFromHistory) {
                ((QuestionActivityFromHistory) getActivity()).setAnswered(true);
            }
            else if (factivity instanceof QuestionActivityFromSearch)
            {
                ((QuestionActivityFromSearch) getActivity()).setAnswered(true);
            }
        }

        for (int i = 0; i < answers.length; i++) {
            System.out.println(answers[i]);
            Button btn = new Button(getActivity());
            btn.setText(answers[i].replace('_', ' ')); //set each button with the corresponding text
            btn.setId(i);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            if (factivity instanceof QuestionActivity)
            {
                if (((QuestionActivity) getActivity()).getAnswered())
                {
                    btn.setClickable(false);
                    viewpager.setPagingEnabled(true);
                }
            }
            else if (factivity instanceof QuestionActivityFromHistory)
            {
                if (((QuestionActivityFromHistory) getActivity()).getAnswered())
                {
                    btn.setClickable(false);
                    viewpager.setPagingEnabled(true);
                }
            }
            else if (factivity instanceof QuestionActivityFromAddQuestion)
            {
                if (((QuestionActivityFromAddQuestion) getActivity()).getAnswered())
                {
                    btn.setClickable(false);
                    viewpager.setPagingEnabled(true);
                }
            }
            else if (factivity instanceof QuestionActivityFromSearch)
            {
                if (((QuestionActivityFromSearch) getActivity()).getAnswered())
                {
                    btn.setClickable(false);
                    viewpager.setPagingEnabled(true);
                }
            }


            if (btn.isClickable()) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (factivity instanceof QuestionActivity) {
                            ((QuestionActivity) getActivity()).setAnswered(true);
                        }
                        else if (factivity instanceof QuestionActivityFromHistory)
                        {
                            ((QuestionActivityFromHistory) getActivity()).setAnswered(true);
                        }
                        else if (factivity instanceof QuestionActivityFromAddQuestion)
                        {
                            ((QuestionActivityFromAddQuestion) getActivity()).setAnswered(true);
                        }
                        else if (factivity instanceof QuestionActivityFromSearch)
                        {
                            ((QuestionActivityFromSearch) getActivity()).setAnswered(true);
                        }
                        final Button b = (Button) v;
                        String answeredText = (String) b.getText();
                        String replacedATexted = answeredText.replaceAll(" ", "_");
                        nextButton.setText("Next");
                        flagButton.setVisibility(View.INVISIBLE);
                        //TODO: Specify between Moderator and User question in URL
                        if (modStatus) {
                            final Firebase moderatorRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/ModeratorQuestions/" +
                                    category + "/" + questionID);
                            final Firebase moderatorAnswerRef = moderatorRef.child("/Answers/" + replacedATexted);
                            moderatorAnswerRef.runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData currentData) {
                                    if (currentData.getValue() == null) {
                                        currentData.setValue(1);
                                    } else {
                                        currentData.setValue((Long) currentData.getValue() + 1);
                                        currentData.setPriority(b.getId());
                                    }
                                    return Transaction.success(currentData);
                                }

                                @Override
                                public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                                    //transaction complete
                                    viewpager.setCurrentItem(1);
                                    for (int i = 0; i < buttonList.length; i++) {
                                        buttonList[i].setClickable(false);
                                    }
                                    //Commented this out, due to users being able to go back to question and answer again, over and over
                                    viewpager.setPagingEnabled(true);
                                }
                            });
                            final Firebase moderatorTotalRef = moderatorRef.child("/Total_Votes");
                            moderatorTotalRef.runTransaction(new Transaction.Handler() {
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
                                public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                                    moderatorRef.setPriority(0 - (Long) dataSnapshot.getValue());
                                }
                            });
                        }
                        final User currentUser = ((MyApplication) factivity.getApplication()).getUser();
                        final Firebase localRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/" + currentUser.getCountry() +
                                "/" + currentUser.getState() + "/" + currentUser.getCity() + "/" + category + "/" + questionID);
                        final Firebase localAnswerRef = localRef.child("/Answers/" + replacedATexted);
                        localAnswerRef.runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData currentData) {
                                if (currentData.getValue() == null) {
                                    currentData.setValue(1);
                                } else {
                                    currentData.setValue((Long) currentData.getValue() + 1);
                                    currentData.setPriority(b.getId());

                                }
                                return Transaction.success(currentData);
                            }

                            @Override
                            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {

                            }
                        });

                        final Firebase localTotalRef = localRef.child("/Total_Votes/");
                        localTotalRef.runTransaction(new Transaction.Handler() {
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
                            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                                localRef.setPriority(0 - (Long) dataSnapshot.getValue());
                            }
                        });
                        localRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (cameFrom.equals("CreatedHistory"))
                                {
                                    Firebase createdRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + ((MyApplication) factivity.getApplication()).getUser().getId() + "/CreatedQuestions/" + questionID + "/hasBeenAnswered");
                                    createdRef.setValue(true);
                                }

                                else if (cameFrom.equals("SkippedHistory"))
                                {
                                    HashMap questionInfo = (HashMap) dataSnapshot.getValue();
                                    Firebase userRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + ((MyApplication) factivity.getApplication()).getUser().getId() + "/AnsweredQuestions/" + questionID);
                                    HashMap historyMap = new HashMap();
                                    Long tsLong = System.currentTimeMillis() / 1000;
                                    historyMap.put("TimeCreated", tsLong);
                                    historyMap.put("City", currentUser.getCity());
                                    historyMap.put("State", currentUser.getState());
                                    historyMap.put("Country", currentUser.getCountry());
                                    historyMap.put("Category", category);
                                    historyMap.put("Name", questionInfo.get("Name"));
                                    userRef.setValue(historyMap);
                                    userRef.setPriority(0 - tsLong);
                                    Firebase skippedRef = new Firebase("https://statfinderproject.firebaseio.com/Users/" + ((MyApplication) factivity.getApplication()).getUser().getId() + "/SkippedQuestions/" + questionID);
                                    skippedRef.removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });


                    }
                });
            }
            llLayout.addView(btn, lp);
            buttonList[i] = btn;
        }

        return llLayout;
    }


}
