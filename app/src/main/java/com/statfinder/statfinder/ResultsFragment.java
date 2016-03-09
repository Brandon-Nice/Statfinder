package com.statfinder.statfinder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultsFragment extends Fragment {

    private RelativeLayout llLayout;
    private FragmentActivity faActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        faActivity  = (FragmentActivity)    super.getActivity();
        llLayout    = (RelativeLayout)    inflater.inflate(R.layout.fragment_results, container, false);

        PieChart pieChart = (PieChart) llLayout.findViewById(R.id.chart);
        /* Turn off pie chart spinning */
        pieChart.setTouchEnabled(false);

        ArrayList<Integer> voteCount = getArguments().getIntegerArrayList("votes");
        int voteSize = voteCount.size();
        /* creating data values */
        /* Use ValueFormatter to remove the decimals and add percentage % */
        final ArrayList<Entry> entries = new ArrayList<>();

        final String category = getArguments().getString("category");
        final String questionID = getArguments().getString("id");

        //Gets a reference to Firebase, then goes through the answers of the question and adds them to the pieChart
        Firebase ref = new Firebase("https://statfinderproject.firebaseio.com/Questions/ModeratorQuestions/" +
                category + "/" + questionID + "/" + "Answers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                HashMap<String,Object> answers = (HashMap) dataSnapshot.getValue();
                System.out.println(answers);
                //Loop through the HashMap and set the keys(answers) and values(number of answers) to the pieChart
                Iterator it = answers.entrySet().iterator();
                int i = 1;
                while(it.hasNext()) {
                    Map.Entry temp = (Map.Entry)it.next();
                    entries.add(i++, (Entry) temp.getValue());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        /* Populate entries with vote counts */
        /* The arraylist called "entries" deals with the number of votes */
//        for (int k = 0; k < voteSize; k++) {
//            //TODO: Firebase for updates, have to update the number of the index of the answer that is being updated
//            //entries.add(new Entry(voteCount.get(k), k));
//            entries.add(new Entry(3, k));
//        }
        //entries.add(new Entry(1, 0));
        //entries.add(new Entry(3, 1));
        //entries.add(new Entry(4, 2));
        //entries.add(new Entry(5, 3));
        //entries.add(new Entry(2, 4));

        /* Parse the entries and remove the entry if value is 0.0 */
        /* Record index at which entry was removed */
        int i;
        int entrySize = entries.size();
        int[] removedList = new int[entrySize];
        ArrayList<Entry> newEntries = new ArrayList<>();

        for (i = 0; i < entrySize; i++) {
            float val = entries.get(i).getVal();
            if (val == 0.0) {
                //entries.remove(i);
                removedList[i] = 1;
            }
            else {
                newEntries.add(entries.get(i));
            }
        }

        PieDataSet dataset = new PieDataSet(newEntries, "");

        /* Answers Array */
        ArrayList<String> answers = new ArrayList<>();
        /* Populate answers list */
        String[] questionAnswers = getArguments().getStringArray("answers");
        for(String a: questionAnswers) {
            String replaced = a.replaceAll("_", " ");
            answers.add(replaced);
        }

       // int answerSize = answers.size();
        //System.out.println("answersSize = " + answerSize);

        /* creating labels */
        ArrayList<String> labels = new ArrayList<String>();
        for (int n = 0; n < entrySize; n++) {
            if (removedList[n] != 1) {
                labels.add(answers.get(n));
            }
        }

        PieData data = new PieData(labels, dataset); // initialize Pie data
        pieChart.setData(data); //set data into chart

        /* Remove text in slices */
        pieChart.setDrawSliceText(false);

        /* Pie chart section colors */
        dataset.setColors(ColorTemplate.VORDIPLOM_COLORS); // set the color

        /* Text size in pie chart */
        dataset.setValueTextSize(13);

        /* Changing size of hole in pie chart */
        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleRadius(0);

        /* Access chart legend */
        Legend legend = pieChart.getLegend();

        /* Set legend text color, size, and location */
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(15);
        legend.setPosition(LegendPosition.BELOW_CHART_CENTER);

        /* Remove pie chart description */
        pieChart.setDescription("");

        pieChart.setUsePercentValues(true);

        /* Setting formatter */
        dataset.setValueFormatter(new PercentFormatter());

        /* Animate pie chart */
        //pieChart.animateXY(1500, 1000);

        /* Button code to go back to the home page (MainActivity) */


        return llLayout;

    }

}
