package com.statfinder.statfinder;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
//import com.github.mikephil.charting.utils.Highlight;
//import com.github.mikephil.charting.utils.PercentFormatter;

import android.app.Activity;
import android.graphics.Color;
import android.R.color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        /* Getting category from QuestionActivity */
        Intent init = getIntent();
        String cameFrom;
        cameFrom = init.getStringExtra("category");

        PieChart pieChart = (PieChart) findViewById(R.id.chart);

        /* creating data values */
        /* Use ValueFormatter to remove the decimals and add percentage % */
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 0));
        entries.add(new Entry(3, 1));
        entries.add(new Entry(4, 2));
        entries.add(new Entry(5, 3));

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

        int newSize = newEntries.size();

        PieDataSet dataset = new PieDataSet(newEntries, "");

        /* Answers Array */
        ArrayList<String> answers = new ArrayList<>();
        /* Populate answers list */
        answers.add("January");
        answers.add("February");
        answers.add("March");
        answers.add("April");

        /* creating labels */
        ArrayList<String> labels = new ArrayList<String>();
        for (int n = 0; n < entrySize; n++) {
            if (removedList[n] != 1) {
                labels.add(answers.get(n));
                /* Debugging content of answers */
                //Log.d("ANSWERS", answers.get(n));
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
        legend.setPosition(LegendPosition.RIGHT_OF_CHART_CENTER);

        /* Remove pie chart description */
        pieChart.setDescription("");

        pieChart.setUsePercentValues(true);

        /* Setting formatter */
        dataset.setValueFormatter(new PercentFormatter());

        /* Animate pie chart */
        //pieChart.animateXY(1500, 1000);

        /* Button code to send to MainActivity after viewing question results */
        Button next = (Button)findViewById(R.id.nextButton);

        /*  In the future, need to pass this the string for the next question in
        *   the unanswered list so that when the next button is clicked, a new
        *   question appears in the MainActivity. Known way to do this is to use
         *  a "Bundle"  */
        final String cat = cameFrom;
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent init = new Intent(ResultsActivity.this, QuestionActivity.class);
                init.putExtra("category", cat);
                startActivity(init);
            }
        });

        /* Button code to go back to the home page (MainActivity) */
        Button home = (Button)findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ResultsActivity.this, MainActivity.class));
            }
        });

    }

}
