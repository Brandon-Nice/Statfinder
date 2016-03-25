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
public class QuestionActivityFromHistory extends FragmentActivity {

    MyPagerAdapter mPagerAdapter;
    boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Intent init = getIntent();
        final ArrayList<HashMap<String, Object>> questionList = (ArrayList<HashMap<String, Object>>) init.getSerializableExtra("List");
        final int position = init.getIntExtra("CurrentQuestion", 0);

        final String questionID = questionList.get(position).entrySet().iterator().next().getKey();
        HashMap<String, Object> questionInfo = (HashMap) questionList.get(position).entrySet().iterator().next().getValue();
        final String category = (String) questionInfo.get("Category");
        String city = (String) questionInfo.get("City");
        String state = (String) questionInfo.get("State");
        String country = (String) questionInfo.get("Country");

        if (questionInfo.containsKey("hasBeenAnswered"))
        {
            answered = (boolean) questionInfo.get("hasBeenAnswered");
        }

        final String cameFrom = init.getStringExtra("CameFrom");

        final TextView questionNameLabel = (TextView) findViewById(R.id.qText);
        questionNameLabel.setText(((String) questionInfo.get("Name")).replace('_', ' '));

        TextView categoryLabel = (TextView) findViewById(R.id.categoryLabel);
        if (category.equals("SciTech"))
        {
            categoryLabel.setText("Category: Science and Technology");
        }
        else {
            categoryLabel.setText("Category: " + category);
        }

        final Button home = (Button) findViewById(R.id.homeButton);
        home.setText("History");
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button skip = (Button) findViewById(R.id.skipButton);
        if (cameFrom.equals("AnsweredHistory") || answered)
        {
            skip.setText("Next");
        }
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position + 1 >= questionList.size())
                {
                    if (cameFrom.equals("CreatedHistory"))
                    {
                        Toast.makeText(QuestionActivityFromHistory.this, "You have reached the end of the list for your created history",
                                Toast.LENGTH_SHORT).show();
                    }
                    if (cameFrom.equals("AnsweredHistory"))
                    {
                        Toast.makeText(QuestionActivityFromHistory.this, "You have reached the end of the list for your answered history",
                                Toast.LENGTH_SHORT).show();
                    }
                    if (cameFrom.equals("SkippedHistory"))
                    {
                        Toast.makeText(QuestionActivityFromHistory.this, "You have reached the end of the list for your skipped history",
                            Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Intent init = null;
                    if(cameFrom.equals("SkippedHistory")) {
                        //init = new Intent(QuestionActivityFromHistory.this, QuestionActivityFromHistory.class);
                        //init = new Intent(QuestionActivityFromHistory.this, HistoryActivity.class);
                        finish();
                        return;
                    }
                    else {
                        init = new Intent(QuestionActivityFromHistory.this, QuestionActivityFromHistory.class);
                        init.putExtra("List", questionList);
                        init.putExtra("CurrentQuestion", position + 1);
                        init.putExtra("CameFrom", cameFrom);
                    }
                    startActivity(init);
                }
                finish();
            }
        });

        final Button flag = (Button) findViewById(R.id.flagButton);

        final Firebase questionRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/" + country + "/" + state + "/" + city + "/" + category + "/" + questionID);
        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean modStatus = false;
                ArrayList<String> answers = new ArrayList();

                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    if (child.getKey().equals("Answers"))
                    {
                        for (DataSnapshot answerChild : child.getChildren())
                        {
                            answers.add(answerChild.getKey());
                        }
                    }
                    else if (child.getKey().equals("Moderated"))
                    {
                        modStatus = (boolean) child.getValue();
                    }
                }

                if (!modStatus) {
                    flag.setVisibility(View.VISIBLE);
                }
                skip.setVisibility(View.VISIBLE);
                home.setVisibility(View.VISIBLE);

                if (modStatus) {
                    questionNameLabel.setTextColor(getResources().getColor(R.color.lightBlue));
                }

                //String[] answersArray = Arrays.copyOf(answers.toArray(), answers.size(), String[].class);

                Bundle bundle = new Bundle();
                //bundle.putStringArray("answers", answersArray);
                bundle.putStringArrayList("answers", answers);
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

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


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
