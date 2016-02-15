package com.statfinder.statfinder;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Jake on 2/15/2016.
 */
public class PercentFormatter implements ValueFormatter, YAxisValueFormatter {
    protected DecimalFormat mFormat;

    /* Use for "default" format */
    public PercentFormatter() {
        mFormat = new DecimalFormat("###,###,##0");
    }

    /* Use for "custom" format */
    public PercentFormatter(DecimalFormat format) {
        this.mFormat = format;
    }

    /* Add percent symbol to the formatted string */
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + "%";
    }

    /* YAxis formatting, not really sure what this does in a pie chart... */
    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return mFormat.format(value) + "%";
    }
}
