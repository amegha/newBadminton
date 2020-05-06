package com.example.myapp_badminton;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class PlayerPerformance extends AppCompatActivity {
    LineChart lineChart;

    LineDataSet lineDataSet;
    ArrayList lineEntries;
    ScoreStorageAdapter sHelper;
    SQLiteDatabase db1,db;
    Cursor cursor,cursor1;
    String uid,uname;
    String[] score_date;
    MyDbAdapter helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_performance);
        helper = new MyDbAdapter(getApplicationContext());

        db = helper.myhelper.getReadableDatabase();
        sHelper = new ScoreStorageAdapter(this);
        db1 = sHelper.scoreStorageHelper.getReadableDatabase();
        lineChart=findViewById(R.id.lineChart);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        uname=bundle.getString("x");



        /*ArrayList<Entry> dataset2 = new ArrayList();
        dataset2.add(new Entry(3f, 0));
        dataset2.add(new Entry(4f, 2));
        dataset2.add(new Entry(5f, 4));
        dataset2.add(new Entry(6f, 5));
        dataset2.add(new Entry(7f, 6));
        dataset2.add(new Entry(8f, 7));
        dataset2.add(new Entry(9f, 8));*/

        cursor1 = helper.myhelper.getUID(uname, db);

        if (cursor1 != null) {
            cursor1.moveToFirst();
            if (cursor1.isAfterLast() == false) {
                uid = cursor1.getString(0);


            }
            cursor1.moveToNext();
        }
        cursor1.close();


        cursor = sHelper.scoreStorageHelper.getScore(uid, db1);

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
        score_date=new String[list1.size()];

        for(int i=0;i<list1.size();i++)
        {
            score_date[i]=list1.get(i);
        }
        for (int j = 0; j < list2.size(); j++) {
            lineEntries = new ArrayList<>();
            for (int i = 0; i < list1.size(); i++) {
                if(i==0 && list1.size()==1)
                {
                    lineEntries.add(new Entry(0f,0f));
                    lineEntries.add(new Entry(Float.valueOf(i),Float.valueOf(list2.get(i))));
                }
                else {
                    lineEntries.add(new Entry(Float.valueOf(i), Float.valueOf(list2.get(i))));
                }
            /*
            sample data
            barEntries.add(new BarEntry(1, 7.8f));
            barEntries.add(new BarEntry(2, 8.5f));
            barEntries.add(new BarEntry(3, 6.8f));*/
            }

        }
        lineDataSet = new LineDataSet(lineEntries, "Inducesmile");
        lineDataSet.setColor(ContextCompat.getColor(this, R.color.main_color));
        lineDataSet.setValueTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        lineChart.setVerticalScrollBarEnabled(true);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
     // final String[] months = new String[]{"Jan", "Feb", "Mar", "Apr"};
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return score_date[(int) value];
            }
        };
        xAxis.setGranularity(1f);
       // lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(list1));
        xAxis.setValueFormatter(formatter);

        YAxis yAxisRight = lineChart.getAxisRight();
       // yAxisRight.setEnabled(false);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setGranularity(1f);

        LineData data = new LineData(lineDataSet);
        lineChart.setData(data);
        lineChart.animateX(1000);
        lineChart.invalidate();
    }


/*
    private ArrayList getData(){
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0f, 4f));
        entries.add(new Entry(1f, 1f));
        entries.add(new Entry(2f, 2f));
        entries.add(new Entry(3f, 4f));
        return entries;
    }
*/

    private ArrayList getData()
    {
        ArrayList<Entry> entries=new ArrayList<>();
        entries.add(new Entry(0f,0f));
        entries.add(new Entry(8f,5f));
        return entries;
    }
}
