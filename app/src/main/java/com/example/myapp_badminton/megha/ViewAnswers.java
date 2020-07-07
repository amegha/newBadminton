package com.example.myapp_badminton.megha;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapp_badminton.ActivityTracker;
import com.example.myapp_badminton.R;

public class ViewAnswers extends Activity implements AsyncResponse {
    public static final String PREFS_NAME = "LoginPrefs";
    //    private ArrayAdapter<String> adapter;
    private static CustomAdapter adapter;
    TextView score, time;
    Bundle bundle;
    DBHandler db;
    String totaltime;
    String playerAnswers;
    Button nextVideo;
    private GetAnswers getAnswers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            ActivityTracker.writeActivityLogs(this.getLocalClassName(), settings.getString("Id", ""));

            setContentView(R.layout.activity_view_answer);
            bundle = getIntent().getExtras();
            bundle.getParcelable("answerModel");
            score = findViewById(R.id.score);
            time = findViewById(R.id.time);
            nextVideo = findViewById(R.id.next_video);
            db = new DBHandler(this);
            displayTotalTimeTaken();
            getGameScore();
            playerAnswers = db.getPlayerAnswers();
            sendResultToServer(playerAnswers);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayScore(String s_score) {
    }

    public void ViewMyAnswers(View view) {
        startActivity(new Intent(this, ListOfAnswers.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
//        db.deletePlayerAnswerData();
    }

    public void playNextVideo(View view) {
        try {
            db.deletePlayerAnswerData();
            startActivity(new Intent(this, PlayVideo.class)/*.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)*/);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskComplete(String result) {
        try {
            if (result.equals("Success")) {
    //            db.deletePlayerAnswerData();
                //empty table answers and correct answers
                nextVideo.setEnabled(true);
                Log.e("ViewAnswers", "upload status " + result);
            } else {
                Toast.makeText(this, "could not sync to server!!", Toast.LENGTH_SHORT).show();
            }

//        getAnswers.parseCorrectAnswers(result);
//        displayScore(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        db.deletePlayerAnswerData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.deletePlayerAnswerData();
    }

    private void displayTotalTimeTaken() {
        try {
            totaltime = String.valueOf(db.getPlayerTotalTime());
            time.setText(totaltime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getGameScore() {
        try {
            score.setText(String.valueOf(db.getGameSCore()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendResultToServer(String playerAnswers) {

        new WebService(this).execute(API.ServerAddress + API.SYNC_TO_SERVER, playerAnswers);
//        getAnswers = new GetAnswersImpl1(this, new WebService(this));
        //syncToServer
       /* JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject selection1=new JSONObject();
        JSONObject selection2=new JSONObject();
        selection1.put("shot_type","");
        selection1.put("shot_loc","");
        selection1.put("time","");
        selection2.put("shot_type","");
        selection2.put("shot_loc","");
        selection2.put("time","");



        try {
            jsonObject.put("userId", "1");
            jsonObject.put("total time", totaltime);
            jsonObject.put("totalscore", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray array = new JSONArray();

*/
    }

}
