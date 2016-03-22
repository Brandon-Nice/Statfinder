package com.statfinder.statfinder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
//import com.github.mikephil.charting.utils.Highlight;
//import com.github.mikephil.charting.utils.PercentFormatter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultsFragment extends Fragment {

    private RelativeLayout llLayout;
    private FragmentActivity faActivity;
    User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        faActivity  = (FragmentActivity)    super.getActivity();
        llLayout    = (RelativeLayout)    inflater.inflate(R.layout.fragment_results, container, false);
        User currentUser = ((MyApplication) faActivity.getApplication()).getUser();
        final PieChart pieChart = (PieChart) llLayout.findViewById(R.id.chart);
        TextView chartTitle = (TextView)llLayout.findViewById(R.id.textView3);

        /* Turn off pie chart spinning */
        pieChart.setTouchEnabled(false);
        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleRadius(0);
        pieChart.setDescription("");
        pieChart.setUsePercentValues(true);
        Legend legend = pieChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(11);
        legend.setPosition(LegendPosition.BELOW_CHART_CENTER);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setWordWrapEnabled(true);

        final int[] colors = new int[6];
        colors[0] = getResources().getColor(R.color.darkBlue);
        colors[1] = getResources().getColor(R.color.darkMagenta);
        colors[2] = getResources().getColor(R.color.darkModerateLimeGreen);
        colors[3] = getResources().getColor(R.color.darkCyan);
        colors[4] = getResources().getColor(R.color.vividOrange);
        colors[5] = getResources().getColor(R.color.gray);

        final String category = getArguments().getString("category");
        final String questionID = getArguments().getString("id");
        final boolean modStatus = getArguments().getBoolean("modStatus");
        if (modStatus) {
            chartTitle.setText("Local Results (Global ->)");
        }

        //Gets a reference to Firebase, then goes through the answers of the question and adds them to the pieChart
        final String finalCity = currentUser.getCity();
        final String finalCountry = currentUser.getCountry();
        final String finalState = currentUser.getState();

        Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/Questions/" + finalCountry + "/" + finalState + "/" + finalCity + "/" +
                category + "/" + questionID + "/" + "Answers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Entry> entries = new ArrayList();
                ArrayList<String> labels = new ArrayList();
                ArrayList<Integer> usedColors = new ArrayList();
                int currentAnswer = 0;
                int currentColor = 0;
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    System.out.println(child);
                    if (((Long) child.getValue()).compareTo(0L) != 0)
                    {
                        entries.add(new Entry( ((Long) child.getValue()).floatValue(), currentAnswer));
                        labels.add(currentAnswer, child.getKey().replace('_', ' '));
                        usedColors.add(colors[currentColor]);
                        currentAnswer++;
                    }
                    currentColor++;
                }

                PieDataSet dataset = new PieDataSet(entries, "");
                PieData data = new PieData(labels, dataset);

                pieChart.setData(data); //set data into chart
                pieChart.setDrawSliceText(false);


                dataset.setColors(usedColors);
                dataset.setValueTextColor(Color.WHITE);
                dataset.setValueTextSize(13);
                dataset.setValueFormatter(new PercentFormatter());

                pieChart.notifyDataSetChanged();
                pieChart.invalidate();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return llLayout;

    }

}
