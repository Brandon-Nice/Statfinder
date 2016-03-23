package com.statfinder.statfinder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

import org.w3c.dom.Text;

/**
 * Created by Jake on 3/21/2016.
 */
public class CountryResultsFragment extends Fragment {

    private RelativeLayout llLayout;
    private FragmentActivity faActivity;
    User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        faActivity  = (FragmentActivity)    super.getActivity();
        llLayout    = (RelativeLayout)    inflater.inflate(R.layout.fragment_results, container, false);
        TextView chartTitle = (TextView)llLayout.findViewById(R.id.textView3);
        chartTitle.setText("(<-State) Country Results (Global->)");
        User currentUser = ((MyApplication) faActivity.getApplication()).getUser();
        final PieChart pieChart = (PieChart) llLayout.findViewById(R.id.chart);
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

        //Gets a reference to Firebase, then goes through the answers of the question and adds them to the pieChart
        final String finalCity = currentUser.getCity();
        final String finalCountry = currentUser.getCountry();
        final String finalState = currentUser.getState();

        final LinkedHashMap<String, Long> questions = new LinkedHashMap();
        Firebase countryRef = new Firebase("https://statfinderproject.firebaseio.com/Questions/" + finalCountry);
        countryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> countryMap = (HashMap) dataSnapshot.getValue();
                System.out.println("CountryMap in Country: " + countryMap);
                for (Map.Entry<String, Object> entry : countryMap.entrySet()) {
                    HashMap<String, Object> statesMap = (HashMap) entry.getValue();
                    System.out.println("StatesMap in Country:" + statesMap);
                    for (Map.Entry<String, Object> entry2 : statesMap.entrySet()) {
                        HashMap<String, Object> value = (HashMap) entry2.getValue();
                        HashMap<String, Object> questionId = (HashMap) value.get(category);
                        HashMap<String, Object> questionInfo = (HashMap) questionId.get(questionID);
                        HashMap<String, Long> answersMap = (HashMap) questionInfo.get("Answers");
                        for (Map.Entry<String, Long> entry3 : answersMap.entrySet()) {
                            long currentValue = 0;
                            if (questions.containsKey(entry3.getKey()))
                            {
                                currentValue = questions.get(entry3.getKey());
                            }
                            questions.put(entry3.getKey(), currentValue + entry3.getValue());
                        }
                    }
                    ArrayList<Entry> entries = new ArrayList();
                    ArrayList<String> labels = new ArrayList();
                    ArrayList<Integer> usedColors = new ArrayList();
                    int currentAnswer = 0;
                    int currentColor = 0;
                    for (Map.Entry<String, Long> question : questions.entrySet()) {
                        if (question.getValue().compareTo(0L) != 0) {
                            entries.add(new Entry(question.getValue().floatValue(), currentAnswer));
                            labels.add(currentAnswer, question.getKey().replace('_', ' '));
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
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });





        return llLayout;

    }

}
