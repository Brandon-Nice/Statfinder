package com.statfinder.statfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by michaelrollberg on 3/23/16.
 */
public class QuestionActivityFromAddQuestion extends FragmentActivity {

    MyPagerAdapter mPagerAdapter;
    boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Intent init = getIntent();
        HashMap<String, Object> question = (HashMap) init.getSerializableExtra("Question");

        String questionID = question.entrySet().iterator().next().getKey();
        HashMap<String, Object> questionInfo = (HashMap) question.entrySet().iterator().next().getValue();

        String cameFrom = "CreatedHistory";
        String[] answers = Arrays.copyOf(((ArrayList) questionInfo.get("Answers")).toArray(), ((ArrayList) questionInfo.get("Answers")).size(), String[].class);
        String category = (String) questionInfo.get("Category");
        boolean modStatus = (boolean) questionInfo.get("Moderated");

        final TextView questionNameLabel = (TextView) findViewById(R.id.qText);
        questionNameLabel.setText(((String) questionInfo.get("Name")).replace('_', ' '));

        TextView categoryLabel = (TextView) findViewById(R.id.categoryLabel);
        categoryLabel.setText("Category: " + category);

        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setVisibility(View.VISIBLE);

        if (modStatus) {
            questionNameLabel.setTextColor(getResources().getColor(R.color.lightBlue));
        }



        Bundle bundle = new Bundle();
        bundle.putStringArray("answers", answers);
        bundle.putString("id", questionID);
        bundle.putString("category", category);
        bundle.putBoolean("modStatus", modStatus);
        bundle.putString("cameFrom", cameFrom);

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

    public boolean getAnswered()
    {
        return answered;
    }

    public void setAnswered(boolean answered)
    {
        this.answered = answered;
    }
}
