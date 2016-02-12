package com.statfinder.statfinder;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        PieChart pieChart = (PieChart) findViewById(R.id.chart);

        /* creating data values */
        /* Use ValueFormatter to remove the decimals and add percentage % */
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4, 0));
        entries.add(new Entry(6, 1));
        entries.add(new Entry(8, 2));
        entries.add(new Entry(10, 3));
        /* entries.add(new Entry(18f, 4)); */


        PieDataSet dataset = new PieDataSet(entries, "");

        /* creating labels */
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        // labels.add("May");

        PieData data = new PieData(labels, dataset); // initialize Pie data
        pieChart.setData(data); //set data into chart

        /* Remove text in slices */
        pieChart.setDrawSliceText(false);

        /* Pie chart section colors */
        dataset.setColors(ColorTemplate.VORDIPLOM_COLORS); // set the color

        /* Text size in pie chart */
        dataset.setValueTextSize(15);

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

        /* Animate pie chart */
        //pieChart.animateXY(5000, 5000);

        /* Button code to send to MainActivity after viewing question results */
        Button next = (Button)findViewById(R.id.nextButton);

        /*  In the future, need to pass this the string for the next question in
        *   the unanswered list so that when the next button is clicked, a new
        *   question appears in the MainActivity. Known way to do this is to use
         *  a "Bundle"  */
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ResultsActivity.this, MainActivity.class));
            }
        });
    }

}
