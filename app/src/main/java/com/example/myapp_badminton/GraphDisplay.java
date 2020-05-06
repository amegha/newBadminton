package com.example.myapp_badminton;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class GraphDisplay extends AppCompatActivity {


    public String score_uid,username;
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;
    ScoreStorageAdapter sHelper;
    SQLiteDatabase db1,db;
    Cursor cursor,cursor1;
    MyDbAdapter helper;




    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent i = new Intent(GraphDisplay.this, HomePage.class);
        //used to or helps in display particular activity
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(i);
        //to avoid flickering or flipped screen or activity use finish() ,which is used to kill or complete the activity
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_display);
        barChart = findViewById(R.id.barchart);
        helper = new MyDbAdapter(getApplicationContext());

        db = helper.myhelper.getReadableDatabase();

        sHelper = new ScoreStorageAdapter(this);
        db1 = sHelper.scoreStorageHelper.getReadableDatabase();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        username = bundle.getString("user_id");


        cursor1 = helper.myhelper.getUID(username, db);

        if (cursor1 != null) {
            cursor1.moveToFirst();
            if (cursor1.isAfterLast() == false) {
                score_uid = cursor1.getString(0);


            }
            cursor1.moveToNext();
        }
        cursor1.close();





        cursor = sHelper.scoreStorageHelper.getScore(score_uid, db1);

        List<String> list1 = new ArrayList<String>();
        List<Float> list2 = new ArrayList<Float>();
        cursor.moveToFirst();
        if (cursor != null) {

                 for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor.isAfterLast() == false) {
                        list1.add(cursor.getString(1));
                        list2.add(cursor.getFloat(0));


                    }
                cursor.moveToNext();
                 }
        }
        cursor.close();



        XAxis xAxis = barChart.getXAxis();
        //   xAxis.setLabelCount(barEntries.size());

        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        // getEntries(stringData,integerData);
        // getEntries();
        for (int j = 0; j < list2.size(); j++) {
            barEntries = new ArrayList<>();
            for (int i = 0; i < list1.size(); i++) {
                barEntries.add(new BarEntry(i, list2.get(i)));
            /*
            sample data
            barEntries.add(new BarEntry(1, 7.8f));
            barEntries.add(new BarEntry(2, 8.5f));
            barEntries.add(new BarEntry(3, 6.8f));*/
            }
        }

        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        YAxis left = barChart.getAxisLeft();
        left.setAxisMinimum(0f);
        barDataSet.setColors(Color.BLUE);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(10f);

        barDataSet.setDrawValues(true);


        // Bars are sliding in from left to right
        barChart.animateXY(1000, 1000);
        // Display scores inside the bars
        barChart.setDrawValueAboveBar(true);
        barData.setBarWidth(0.3f); // set custom bar width

        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate();
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(list1));


     }


}







