package com.example.myapp_badminton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;import android.util.Log;
import android.util.Log;

public class GraphDisplay extends AppCompatActivity implements AsyncResponse {
    public String score_uid,score_uidplay,username,cid,type,Pdate,PScore,fragment_module,playername;
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;
   // EditText edpid;
    SQLiteDatabase db;
   // Button btndisplaygraph;
    Cursor cursor,cursor1;
    WebService webServiceCoach,webServicePlayer;
    databaseConnectionAdapter dbcAdapter;

    List<String> list1 = new ArrayList<String>();
    List<Float> list2 = new ArrayList<Float>();



    @Override
    public void onBackPressed() {

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
      //  edpid=findViewById(R.id.PlayerIdBar);
     /*   dbcAdapter = new databaseConnectionAdapter(getApplicationContext());
     //   btndisplaygraph=findViewById(R.id.btngetIDbar);

        db = dbcAdapter.allDataHelper.getReadableDatabase();*/

            Intent i=getIntent();
            Bundle bundle=i.getExtras();
             type=bundle.getString("type");
             if(type.equalsIgnoreCase("player")){
                 Player();
             }
             else
                 if(type.equalsIgnoreCase("Coach")){
                     coach();
                 }

    }
    @Override
    public void onTaskComplete(String result) {
        /* if(result.equals("Success")){*/
        Log.e("ViewUserDetails","Upload status "+result);
        String[] arrRes;
        arrRes = result.split(",");
        String locationXml;
//        Log.e("ViewUserDetails", " arrRes[0] " + arrRes[0] + " arrRes[1]  " +  arrRes[1] + "  arrRes[2]" + arrRes[2]);

            arrRes=result.split("/");
            for(int i=1;i<arrRes.length;i++){
                getCorrected(arrRes[i]);
            }


/*

        String [] academyResponse;
        ArrayList<String> Scores=new ArrayList();
        ArrayList<String> ScoreDate=new ArrayList();


        academyResponse = result.split(";");
        for (int i = 0; i < academyResponse.length; i++) {
            LocationInfo(academyResponse[i],ScoreDate,Scores);
            Log.d("Total Entry Size", String.valueOf(academyResponse.length));
        }
        System.out.println("all the cities "+ Collections.singletonList(ScoreDate));
        System.out.println("all the Academy Ids "+ Collections.singletonList(Scores));
*/

//testing player
        if(type.equalsIgnoreCase("Player")){


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

            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(list1));
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
            barChart.getDescription().setEnabled(false);
            barChart.setFitBars(true); // make the x-axis fit exactly all bars
            barChart.invalidate();

        }


        //testing coach
        if(type.equalsIgnoreCase("coach")) {


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
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(list1));


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
            barChart.getDescription().setEnabled(false);
            barChart.setFitBars(true); // make the x-axis fit exactly all bars
            barChart.invalidate();

        }
    }

    private void LocationInfo(String s, ArrayList<String> scoredate, ArrayList<String> score) {

        String[] locInfo = s.split(",");
        Log.d("Total Module Size", String.valueOf(locInfo.length));
        scoredate.add(locInfo[0]);
        score.add(locInfo[1]);
        ArrayList<String> convertedDate=new ArrayList<>();
        for(int i=0;i<scoredate.size();i++) {
            String properDate = scoredate.get(i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd-MM-yyyy");

            try {
                Date strDate = dateFormat.parse(properDate);
                convertedDate.add(i,dateFormatOutput.format(strDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        list1=convertedDate;
        float [] floatValues = new float[score.size()];

        for (int i = 0; i < score.size(); i++) {
            floatValues[i] = Float.parseFloat(score.get(i));
            list2.add(i, floatValues[i]);
        }
    }
    public void callServer(String data)
    {
        //new WebService(getApplicationContext()).execute(API.ServerAddress + "get_all_score.php", "module=coach&coach_id="+score_uid+"&player_id="+pid);
       new WebService(GraphDisplay.this).execute("http://stage1.optipacetech.com/badminton/api/get_all_score.php","module=coach&coach_id="+cid+"&player_id="+data);


    }
  /*  public  void OkClick(View view){
       // pid = edpid.getText().toString();
       *//* request Parameter for coach
        module:coach
        coach_id:22
        player_id:1099*//*
        callServer(pid);

    }*/
    public void getCorrected(String s){

        String [] academyResponse;
        ArrayList<String> Scores=new ArrayList();
        ArrayList<String> ScoreDate=new ArrayList();


        academyResponse = s.split(";");
        for (int i = 0; i < academyResponse.length; i++) {
            LocationInfo(academyResponse[i],ScoreDate,Scores);
            Log.d("Total Entry Size", String.valueOf(academyResponse.length));
        }
        System.out.println("all the cities "+ Collections.singletonList(ScoreDate));
        System.out.println("all the Academy Ids "+ Collections.singletonList(Scores));
    }
    public void Player( ){
        Intent iPlayer=getIntent();
        Bundle Bplayers=iPlayer.getExtras().getBundle("From");
        type=Bplayers.getString("type");
        username=Bplayers.getString("PName");
        score_uidplay=Bplayers.getString("Pid");
        Pdate=Bplayers.getString("lastScoreDate");
        PScore=Bplayers.getString("ScoreLast");
        if(type.equalsIgnoreCase("Player")) {
            new WebService(GraphDisplay.this).execute("http://stage1.optipacetech.com/badminton/api/get_all_score.php", "module=player&player_id=" + score_uidplay);
        }
    }
    public void coach(){

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        cid=bundle.getString("coach_id");
        username=bundle.getString("coachname");
        type=bundle.getString("type");
        ExampleItem exampleItem = intent.getParcelableExtra("Player Details");
        fragment_module=bundle.getString("module");

        //imagebytes = baos.toByteArray();
        playername = exampleItem.getText1();
        score_uid = exampleItem.getText2();

        if(fragment_module.equalsIgnoreCase("BarGraph")){
            callServer(score_uid);
        }

    }
}
