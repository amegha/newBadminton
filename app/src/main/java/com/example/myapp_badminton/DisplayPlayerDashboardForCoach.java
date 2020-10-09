package com.example.myapp_badminton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayPlayerDashboardForCoach extends AppCompatActivity implements AsyncResponse {
    TextView totVid, playedVid, correctAns, wrongAns;
    private String type;
    private String userName;
    private String playerId;
    private String cid;
    LinearLayout playGameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dashboard_player);
        totVid = findViewById(R.id.tot_video);
        playedVid = findViewById(R.id.played_video);
        correctAns = findViewById(R.id.right_answers);
        wrongAns = findViewById(R.id.wrong_answers);
        playGameLayout = findViewById(R.id.play_game_layout);

        playGameLayout.setVisibility(View.GONE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        cid = bundle.getString("coach_id");
        ExampleItem exampleItem = intent.getParcelableExtra("Player Details");
        playerId = exampleItem.getText2();
        callServer(playerId);
    }

    private void callServer(String playerId) {
        new WebService(this).execute(API.ServerAddress + API.PLAYER_DASHBOARD, "user_id=" + playerId);

    }

    @Override
    public void onTaskComplete(String result) {
        switch (result) {
            case "00": {
                Toast.makeText(this, "Invalid Request", Toast.LENGTH_LONG).show();
                break;
            }
            case "01":
            case "02": {
                Toast.makeText(this, "Server busy!", Toast.LENGTH_LONG).show();
                break;
            }
            case "03": {
                Toast.makeText(this, "User not found!", Toast.LENGTH_LONG).show();
                break;
            }
            case "103": {
                Toast.makeText(this, "completed all the videos", Toast.LENGTH_LONG).show();
                break;
            }
            case "404": {
                Toast.makeText(this, "Nothing to sync!", Toast.LENGTH_LONG).show();

            }
            case "502": {
                Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                String[] arrRes = result.split(",");

                if (arrRes.length == 4) { // dashboard data
                    totVid.setText(arrRes[0]);
                    playedVid.setText(arrRes[1]);
                    correctAns.setText(arrRes[2]);
                    wrongAns.setText(arrRes[3]);
                }
        }
    }
}
