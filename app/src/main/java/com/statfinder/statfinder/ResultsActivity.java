package com.statfinder.statfinder;

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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        PieChart pieChart = (PieChart) findViewById(R.id.chart);
        // creating data values
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(12f, 3));
        //entries.add(new Entry(18f, 4));


        PieDataSet dataset = new PieDataSet(entries, "# of Calls");

        // creating labels
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        //labels.add("May");

        PieData data = new PieData(labels, dataset); // initialize Pie data
        pieChart.setData(data); //set data into chart

        dataset.setColors(ColorTemplate.VORDIPLOM_COLORS); // set the color

        dataset.setValueTextSize(13);
        //Paint hole = pieChart.getPaint(Chart.PAINT_HOLE);
        //hole.setColor(Color.BLUE);

        //pieChart.animateXY(5000, 5000);


    }

}
