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

/**
 * Created by michaelrollberg on 2/25/16.
 */
public class AnswersFragment extends Fragment {

    private LinearLayout llLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        llLayout = (LinearLayout) inflater.inflate(R.layout.fragment_answers, container, false);
        llLayout.setOrientation(LinearLayout.VERTICAL);

        String[] answers = getArguments().getStringArray("answers");
        final String category = getArguments().getString("category");
        final String questionID = getArguments().getString("id");

        final MyViewPager viewpager = (MyViewPager) getActivity().findViewById(R.id.viewpager);
        final Button nextButton = (Button) getActivity().findViewById(R.id.skipButton);

        System.out.println(answers);

        for (int i = 0; i < answers.length; i++) {
            System.out.println(answers[i]);
            Button btn = new Button(getActivity());
            btn.setText(answers[i].replace('_', ' ')); //set each button with the corresponding text
            //btn.setId(i);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    String answeredText = (String) b.getText();
                    String replacedATexted = answeredText.replaceAll(" ", "_");
                    nextButton.setText("Next");
                    //TODO: Specify between Moderator and User question in URL
                    final Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/Questions/ModeratorQuestions/" +
                            category + "/" + questionID + "/" + "Answers" + "/" + replacedATexted);
                    System.out.println("Firebase URL: " + ref);
                    ref.runTransaction(new Transaction.Handler() {
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
                            //transaction complete
                            viewpager.setCurrentItem(1);
                            //Commented this out, due to users being able to go back to question and answer again, over and over
                            //viewpager.setPagingEnabled(true);
                        }
                    });

                }
            });
            llLayout.addView(btn, lp);
        }

        return llLayout;
    }


}
