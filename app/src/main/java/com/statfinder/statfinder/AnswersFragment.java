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
        String category = getArguments().getString("category");

        final MyViewPager viewpager = (MyViewPager) getActivity().findViewById(R.id.viewpager);
        final Button nextButton = (Button) getActivity().findViewById(R.id.skipButton);

        System.out.println(answers);

        for (int i = 0; i < answers.length; i++) {
            System.out.println(answers[i]);
            Button btn = new Button(getActivity());
            btn.setText(answers[i]); //set each button with the corresponding text
            //btn.setId(i);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;

                    nextButton.setText("Next");
                    viewpager.setCurrentItem(1);
                    viewpager.setPagingEnabled(true);
                }
            });
            llLayout.addView(btn, lp);
        }

        return llLayout;
    }


}
