package com.statfinder.statfinder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by michaelrollberg on 2/25/16.
 */
public class AnswersFragment extends Fragment {

    private RelativeLayout llLayout;
    private FragmentActivity faActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        faActivity = (FragmentActivity) super.getActivity();
        llLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_answers, container, false);

        return llLayout;
    }

}
