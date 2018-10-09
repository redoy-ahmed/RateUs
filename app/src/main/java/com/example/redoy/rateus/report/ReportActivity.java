package com.example.redoy.rateus.report;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.redoy.rateus.R;
import com.example.redoy.rateus.database.DatabaseHelper;
import com.example.redoy.rateus.database.Rating;
import com.example.redoy.rateus.utils.MyMarkerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private BarChart mChart;
    private DatabaseHelper db;
    private List<Rating> ratingList;
    private List<Integer> yearList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_report);

        yearList = new ArrayList<>();
        db = new DatabaseHelper(this);
        ratingList = db.getAllRating();

        for (Rating rating : ratingList) {
            yearList.add(Integer.valueOf(rating.getDate().substring(0, 4)));
        }

        mChart = findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);

        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);

        setData();
    }

    private void setData() {
        float groupSpace = 0.05f;
        float barSpace = 0.02f;
        float barWidth = 0.1f;

        int startYear = 2018;
        int endYear = startYear + 2;

        if (yearList.size() > 0) {
            startYear = yearList.get(0);
            endYear = yearList.get(yearList.size() - 1) + 2;
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        ArrayList<BarEntry> yVals2 = new ArrayList<>();
        ArrayList<BarEntry> yVals3 = new ArrayList<>();

        for (int i = startYear; i < endYear; i++) {
            yVals1.add(new BarEntry(i, (float) 1));
            yVals2.add(new BarEntry(i, (float) 2));
            yVals3.add(new BarEntry(i, (float) 3));
        }

        BarDataSet goodBarDataSet, averageBarDataSet, badBarDataSet;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            goodBarDataSet = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            averageBarDataSet = (BarDataSet) mChart.getData().getDataSetByIndex(1);
            badBarDataSet = (BarDataSet) mChart.getData().getDataSetByIndex(2);
            goodBarDataSet.setValues(yVals1);
            averageBarDataSet.setValues(yVals2);
            badBarDataSet.setValues(yVals3);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

        } else {
            goodBarDataSet = new BarDataSet(yVals1, "Good");
            goodBarDataSet.setColor(Color.rgb(104, 241, 175));
            averageBarDataSet = new BarDataSet(yVals2, "Average");
            averageBarDataSet.setColor(Color.rgb(164, 228, 251));
            badBarDataSet = new BarDataSet(yVals3, "Bad");
            badBarDataSet.setColor(Color.rgb(242, 247, 158));

            BarData data = new BarData(goodBarDataSet, averageBarDataSet, badBarDataSet);
            data.setValueFormatter(new LargeValueFormatter());

            mChart.setData(data);
        }

        mChart.getBarData().setBarWidth(barWidth);
        mChart.getXAxis().setAxisMinimum(startYear);
        mChart.getXAxis().setAxisMaximum(startYear + mChart.getBarData().getGroupWidth(groupSpace, barSpace) * 5);
        mChart.groupBars(startYear, groupSpace, barSpace);
        mChart.invalidate();

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        mChart.getAxisRight().setEnabled(false);
    }
}

