package com.example.daily_function;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;


public class DiseaseInfoActivity extends AppCompatActivity {

    PieChart pieChart;
    int[] colorArray = new int[]{Color.LTGRAY, Color.BLUE, Color.RED};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_info);

        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList diseaseList = new ArrayList();

        PieDataSet pieDataSet = new PieDataSet(data1(), "좋음싫음 설문조사");
        pieDataSet.setColors(colorArray);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setDrawEntryLabels(true);
        pieChart.setUsePercentValues(true);
        pieData.setValueTextSize(30);
        pieChart.setCenterText("설문조사");
        pieChart.setCenterTextSize(25);
        pieChart.setHoleRadius(30);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private ArrayList<PieEntry> data1() {
        ArrayList<PieEntry> datavalue = new ArrayList<>();

        datavalue.add(new PieEntry(30, "무응답"));
        datavalue.add(new PieEntry(50, "좋음"));
        datavalue.add(new PieEntry(20, "싫음"));


        return datavalue;
    }
}