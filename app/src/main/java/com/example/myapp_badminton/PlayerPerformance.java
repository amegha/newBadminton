package com.example.myapp_badminton;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PlayerPerformance extends AppCompatActivity implements AsyncResponse {
    LineChart lineChart;

    LineDataSet lineDataSet;
    ArrayList lineEntries;
    databaseConnectionAdapter helper;
    SQLiteDatabase db;
    Cursor cursor, cursor1;
    String uid, uidPlay, uname, cid, type, Pdate, PScore, fragment_module, Coachname;
    String[] score_date;
    //  EditText editTextId;
    // Button ok;

    List<String> list1 = new ArrayList<String>();
    List<Float> list2 = new ArrayList<Float>();

    /*@Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent(PlayerPerformance.this, HomePage.class);
        //used to or helps in display particular activity
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(i);
        //to avoid flickering or flipped screen or activity use finish() ,which is used to kill or complete the activity
        finish();

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_player_performance);
   /*     helper = new databaseConnectionAdapter(getApplicationContext());

        db = helper.allDataHelper.getReadableDatabase();*/
       /* editTextId=findViewById(R.id.PlayerId);
        ok=findViewById(R.id.btngetID);*/

            lineChart = findViewById(R.id.lineChart);


            Intent i = getIntent();
            Bundle bundle = i.getExtras();
            type = bundle.getString("type");
            if (type.equalsIgnoreCase("player")) {
                Player();
            } else if (type.equalsIgnoreCase("Coach")) {
                coach();
            }

  /*      Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        cid=bundle.getString("coach_id");
        type=bundle.getString("type");
        ExampleItem exampleItem = intent.getParcelableExtra("Player Details");
        fragment_module=bundle.getString("module");

        //imagebytes = baos.toByteArray();
        uname = exampleItem.getText1();
        uid = exampleItem.getText2();


        Intent iPlayer=getIntent();
        Bundle Bplayers=iPlayer.getExtras();
        type=Bplayers.getString("type");
        uname=Bplayers.getString("PName");
        uidPlay=Bplayers.getString("Pid");
        Pdate=Bplayers.getString("lastScoreDate");
        PScore=Bplayers.getString("ScoreLast");*/
        } catch (Exception e) {
            e.printStackTrace();
        }


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

    private ArrayList getData() {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0f, 0f));
        entries.add(new Entry(8f, 5f));
        return entries;
    }

    public void callServer(String data) {
        //new WebService(getApplicationContext()).execute(API.ServerAddress + "get_all_score.php", "module=coach&coach_id="+score_uid+"&player_id="+pid);
        new WebService(PlayerPerformance.this).execute("http://stage1.optipacetech.com/badminton/api/get_all_score.php", "module=coach&coach_id=" + cid + "&player_id=" + data);


    }
 /*   public void OKClickLine(View view) {
        uid = editTextId.getText().toString();
       *//* request Parameter for coach
        module:coach
        coach_id:22
        player_id:1099*//*
        callServer(uid);
    }*/

    @Override
    public void onTaskComplete(String result) {

        try {
            /* if(result.equals("Success")){*/
            Log.e("ViewUserDetails", "Upload status " + result);
            String[] arrRes;
            arrRes = result.split(",");
            String locationXml;
//        Log.e("ViewUserDetails", " arrRes[0] " + arrRes[0] + " arrRes[1]  " +  arrRes[1] + "  arrRes[2]" + arrRes[2]);

            arrRes = result.split("/");
            for (int i = 1; i < arrRes.length; i++) {
                getCorrected(arrRes[i]);
            }

      /*  String [] academyResponse;
        ArrayList<String> Scores=new ArrayList();
        ArrayList<String> ScoreDate=new ArrayList();


        academyResponse = result.split(";");
        for (int i = 0; i < academyResponse.length; i++) {
            LocationInfo(academyResponse[i],ScoreDate,Scores);
            Log.d("Total Entry Size", String.valueOf(academyResponse.length));
        }
        System.out.println("all the cities "+ Collections.singletonList(ScoreDate));
        System.out.println("all the Academy Ids "+ Collections.singletonList(Scores));*/

        //player checking
        if(type.equalsIgnoreCase("Player")) {
           /* editTextId.setVisibility(View.GONE);
            ok.setVisibility(View.GONE);*/

            if (list1.size() == 0 && list2.size() == 0) {//used to check if no data is entered
                //if(lineChart.getData().equals(null) || lineChart.getData().equals(""))

                lineChart.setNoDataText("No Data Available !");
                // lineChart.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No Data Available!", Toast.LENGTH_LONG).show();
            } else {
                score_date = new String[list1.size()];

                for (int i = 0; i < list1.size(); i++) {
                    score_date[i] = list1.get(i);
                }
                for (int j = 0; j < list2.size(); j++) {
                    lineEntries = new ArrayList<>();
                    for (int i = 0; i < list1.size(); i++) {
                        if (i == 0 && list1.size() <= 1) {
                            if (lineChart.getData() == null || lineChart.getData().getDataSetCount() == 0 || lineChart.getData().getDataSetCount() <= 1) {
                                lineEntries = (ArrayList) lineChart.getData().getDataSetByIndex(0);
                                //   lineEntries.add(new Entry(0f, 0f));
                                // lineEntries.add(new Entry(Float.valueOf(i), Float.valueOf(list2.get(i))));
                                lineChart.getData().notifyDataChanged();
                                lineChart.notifyDataSetChanged();
                                lineChart.setNoDataText("No graph is available !");
                            }
                            Toast.makeText(getApplicationContext(), "More than 2 Entries can only be Displayed!..", Toast.LENGTH_LONG).show();
                            lineChart.setVisibility(View.GONE);
                            lineChart.setNoDataText("No Chart is Available!");
                        } else {
                            lineEntries.add(new Entry(Float.valueOf(i), Float.valueOf(list2.get(i))));
                        }

                    }

                }
                lineDataSet = new LineDataSet(lineEntries, "Performanace");
                lineDataSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.app_text));
                lineDataSet.setValueTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
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
                //yAxisRight.setEnabled(false);

                YAxis yAxisLeft = lineChart.getAxisLeft();
                yAxisLeft.setGranularity(1f);

                LineData data = new LineData(lineDataSet);
                lineChart.setData(data);
                lineChart.animateX(1000);
                lineChart.getDescription().setEnabled(false);
                lineChart.invalidate();
            }
        }

            //checking coach

        if (type.equalsIgnoreCase("coach")) {

            if (list1.size() == 0 && list2.size() == 0) {//no data is available
                //if(lineChart.getData().equals(null) || lineChart.getData().equals(""))

                lineChart.setNoDataText("No Data Available !");
                // lineChart.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No Data Available!", Toast.LENGTH_LONG).show();
            } else {
                score_date = new String[list1.size()];


                for (int i = 0; i < list1.size(); i++) {
                    score_date[i] = list1.get(i);
                }
                for (int j = 0; j < list2.size(); j++) {
                    lineEntries = new ArrayList<>();
                    for (int i = 0; i < list1.size(); i++) {
                        if (i == 0 && list1.size() == 1) {
                            if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 1) {
                                lineEntries = (ArrayList) lineChart.getData().getDataSetByIndex(0);
                                //lineEntries.add(new Entry(0f, 0f));
                                //  lineEntries.add(new Entry(Float.valueOf(i), Float.valueOf(list2.get(i))));
                                lineChart.getData().notifyDataChanged();
                                lineChart.notifyDataSetChanged();
                                lineChart.setNoDataText("No Chart is Available!");
                            }
                            lineChart.setNoDataText("No Chart is Available!");
                            Toast.makeText(getApplicationContext(), "More than 2 Entries can only be Displayed!..", Toast.LENGTH_LONG).show();
                            lineChart.setVisibility(View.GONE);
                        } else {
                            lineEntries.add(new Entry(Float.valueOf(i), Float.valueOf(list2.get(i))));
                        }

                    }

                }


                lineDataSet = new LineDataSet(lineEntries, "Performanace");
                lineDataSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.app_text));
                lineDataSet.setValueTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
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
                //yAxisRight.setEnabled(false);

                YAxis yAxisLeft = lineChart.getAxisLeft();
                yAxisLeft.setGranularity(1f);

                LineData data = new LineData(lineDataSet);
                lineChart.setData(data);
                lineChart.getDescription().setEnabled(false);
                lineChart.animateX(1000);
                lineChart.invalidate();
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void LocationInfo(String s, ArrayList<String> scoredate, ArrayList<String> score) {
        try {
            if(s.equals(null)|| s.equals("")|| s.isEmpty()==true){
                lineChart.setNoDataText("No Data Available!");
            }
            String[] locInfo = s.split(",");
            Log.d("Total Module Size", String.valueOf(locInfo.length));
            scoredate.add(locInfo[0]);
            score.add(locInfo[1]);
            ArrayList<String> convertedDate = new ArrayList<>();
            for (int i = 0; i < scoredate.size(); i++) {
                String properDate = scoredate.get(i);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd-MM-yyyy");

                try {
                    Date strDate = dateFormat.parse(properDate);
                    convertedDate.add(i, dateFormatOutput.format(strDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            list1 = convertedDate;
            float[] floatValues = new float[score.size()];

            for (int i = 0; i < score.size(); i++) {
                floatValues[i] = Float.parseFloat(score.get(i));
                list2.add(i, floatValues[i]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void getCorrected(String s) {

        try {
            String[] academyResponse;
            ArrayList<String> Scores = new ArrayList();
            ArrayList<String> ScoreDate = new ArrayList();


            academyResponse = s.split(";");
            for (int i = 0; i < academyResponse.length; i++) {
                LocationInfo(academyResponse[i], ScoreDate, Scores);
                Log.d("Total Entry Size", String.valueOf(academyResponse.length));
            }
            System.out.println("all the cities " + Collections.singletonList(ScoreDate));
            System.out.println("all the Academy Ids " + Collections.singletonList(Scores));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Player() {

        try {
            Intent iPlayer = getIntent();
            Bundle Bplayers = iPlayer.getExtras().getBundle("From");
            type = Bplayers.getString("type");
            uname = Bplayers.getString("PName");
            uidPlay = Bplayers.getString("Pid");
            Pdate = Bplayers.getString("lastScoreDate");
            PScore = Bplayers.getString("ScoreLast");
            ActivityTracker.writeActivityLogs(this.getLocalClassName(), uidPlay);
            if (type.equalsIgnoreCase("Player")) {
                new WebService(PlayerPerformance.this).execute("http://stage1.optipacetech.com/badminton/api/get_all_score.php", "module=player&player_id=" + uidPlay);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void coach() {

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            cid = bundle.getString("coach_id");
            uname = bundle.getString("coachname");
            type = bundle.getString("type");
            ExampleItem exampleItem = intent.getParcelableExtra("Player Details");
            fragment_module = bundle.getString("module");

            //imagebytes = baos.toByteArray();
            uname = exampleItem.getText1();
            uid = exampleItem.getText2();
            if (fragment_module.equalsIgnoreCase("LineGraph")) {
                callServer(uid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}