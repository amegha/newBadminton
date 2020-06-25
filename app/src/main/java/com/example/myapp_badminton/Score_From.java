package com.example.myapp_badminton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class Score_From extends AppCompatActivity {
    public RadioButton fitness, grip, oncourt_skill, singles_oncourt, doubles_oncourt, shadow_oncourt;
    public byte[] imagebytes;
    public String value;
    public LinearLayout linearLayout;
    databaseConnectionAdapter dbcAdapter;
    SQLiteDatabase db;
    long true_id;
    String ImagePlayer;
    //  Spinner spin;
    ByteArrayOutputStream baos;
    String today, yesterday, pass_date, name, userid;
    String cid, coach_name, coachdate, level, academy, playerName, playerId, last_date, aid;
    String type, PName, PId, lastScoreEntryDate, Score;
    int x;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTracker.writeActitivtyLogs(this.getLocalClassName());
        setContentView(R.layout.activity_score__from);

        fitness = findViewById(R.id.cat1);
        grip = findViewById(R.id.cat2);
        oncourt_skill = findViewById(R.id.cat3);

        singles_oncourt = findViewById(R.id.singles);
        doubles_oncourt = findViewById(R.id.doubles);
        shadow_oncourt = findViewById(R.id.shadow_footwork);


        //spin=findViewById(R.id.spinner1);
        linearLayout = findViewById(R.id.rg2);


        fitness.setChecked(false);
        grip.setChecked(false);
        oncourt_skill.setChecked(false);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        yesterday = bundle.getString("prev_date");

        if (today != null && yesterday == null) {
            pass_date = today;
            value = bundle.getString("x");
        } else if (yesterday != null && today == null) {
            pass_date = yesterday;
            value = bundle.getString("x");

        }


        //for player Score Entry Modeule
        Intent IPlayer = getIntent();
        Bundle BPlayer = IPlayer.getExtras();
        type = BPlayer.getString("type");
        if (type.equalsIgnoreCase("Player")) {
            PName = BPlayer.getString("Pname");
            PId = BPlayer.getString("Pid");
            lastScoreEntryDate = BPlayer.getString("lastScoreDate");
            Score = BPlayer.getString("ScoreLast");
            ImagePlayer = BPlayer.getString("Image");
        } else {
            Intent i = getIntent();
            Bundle b = i.getExtras();
            cid = b.getString("coach_id");
            coach_name = b.getString("coachname");
            coachdate = b.getString("date");
            level = b.getString("level");
            academy = b.getString("Academy");
            type = b.getString("type");
            aid = b.getString("aid");

            ExampleItem exampleItem = i.getParcelableExtra("Player Details");
            //converting bitmap to byte array
            Bitmap display = exampleItem.getImageResource();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            display.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imagebytes = baos.toByteArray();

            //imagebytes = baos.toByteArray();
            playerName = exampleItem.getText1();
            playerId = exampleItem.getText2();
            //Bitmap disp=exampleItem.getImageResource();
            //  Bitmap photo =  exampleItem.getImageResource();


        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (type.equalsIgnoreCase("Player")) {
            Intent i = new Intent(Score_From.this, HomePage.class);
            //used to or helps in display particular activity
//            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
//            startActivity(i);
        } else {
            Intent i = new Intent(Score_From.this, DisplayPlayer.class);
            //used to or helps in display particular activity
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            startActivity(i);
        }

        //to avoid flickering or flipped screen or activity use finish() ,which is used to kill or complete the activity
        finish();

    }


    public void fitness_select(View view) {
        fitness.setChecked(true);
        grip.setChecked(false);
        oncourt_skill.setChecked(false);
        linearLayout.setVisibility(View.GONE);

        if (type.equalsIgnoreCase("Coach")) {
            // spin.setVisibility(View.GONE);
            Bundle bundlecoach = new Bundle();
            bundlecoach.putString("coach_id", cid);
            bundlecoach.putString("coachname", coach_name);
            bundlecoach.putString("date", coachdate);
            bundlecoach.putString("level", level);
            bundlecoach.putString("Academy", academy);
            bundlecoach.putString("PlayerName", playerName);
            bundlecoach.putString("playerId", playerId);
            bundlecoach.putByteArray("ImageBytes", imagebytes);
            bundlecoach.putString("type", type);
            bundlecoach.putString("aid", aid);
            Intent intent = new Intent(Score_From.this, SearchActivity.class).putExtra("check_id", "Fitness").putExtra("y", value).putExtras(bundlecoach).putExtra("date_value", pass_date);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
//            startActivity(new Intent(Score_From.this, SearchActivity.class).putExtra("check_id", "Fitness").putExtra("y", value).putExtras(bundlecoach).putExtra("date_value", pass_date));

        } else {
            Bundle bundle1 = new Bundle();
            bundle1.putString("Pname", PName);
            bundle1.putString("Pid", PId);
            bundle1.putString("type", type);
            bundle1.putString("lastScoreDate", lastScoreEntryDate);
            bundle1.putString("ScoreLast", Score);
            bundle1.putString("Image", ImagePlayer);
            Intent intent = new Intent(Score_From.this, SearchActivity.class).putExtras(bundle1).putExtra("check_id", "Fitness");
            startActivity(intent);
        }
    }

    public void grip_select(View view) {
        grip.setChecked(true);
        fitness.setChecked(false);
        oncourt_skill.setChecked(false);
        linearLayout.setVisibility(View.GONE);
        if (type.equalsIgnoreCase("Coach")) {
            //spin.setVisibility(View.GONE);
            Bundle bundlecoach = new Bundle();
            bundlecoach.putString("coach_id", cid);
            bundlecoach.putString("coachname", coach_name);
            bundlecoach.putString("date", coachdate);
            bundlecoach.putString("level", level);
            bundlecoach.putString("Academy", academy);
            bundlecoach.putString("PlayerName", playerName);
            bundlecoach.putString("playerId", playerId);
            bundlecoach.putString("aid", aid);
            bundlecoach.putString("type", type);
            bundlecoach.putByteArray("ImageBytes", imagebytes);
            startActivity(new Intent(Score_From.this, SearchActivity.class).putExtra("check_id", "Grip").putExtra("y", value).putExtras(bundlecoach).putExtra("date_value", pass_date));
        } else {
            Bundle bundle1 = new Bundle();
            bundle1.putString("Pname", PName);
            bundle1.putString("Pid", PId);
            bundle1.putString("type", type);
            bundle1.putString("lastScoreDate", lastScoreEntryDate);
            bundle1.putString("ScoreLast", Score);
            bundle1.putString("Image", ImagePlayer);
            Intent intent = new Intent(Score_From.this, SearchActivity.class).putExtras(bundle1).putExtra("check_id", "Grip");
            startActivity(intent);
        }
    }

    public void oncourt_skill_select(View view) {
        oncourt_skill.setChecked(true);
        fitness.setChecked(false);
        grip.setChecked(false);
        linearLayout.setVisibility(View.VISIBLE);
        // spin.setVisibility(View.VISIBLE);
    }

    public void singles_select(View view) {
        oncourt_skill.setChecked(true);
        linearLayout.setVisibility(View.VISIBLE);
        fitness.setChecked(false);
        grip.setChecked(false);
        singles_oncourt.setChecked(true);
        doubles_oncourt.setChecked(false);
        shadow_oncourt.setChecked(false);
        if (type.equalsIgnoreCase("Coach")) {
            Bundle bundlecoach = new Bundle();
            bundlecoach.putString("coach_id", cid);
            bundlecoach.putString("coachname", coach_name);
            bundlecoach.putString("date", coachdate);
            bundlecoach.putString("level", level);
            bundlecoach.putString("Academy", academy);
            bundlecoach.putString("PlayerName", playerName);
            bundlecoach.putString("playerId", playerId);
            bundlecoach.putByteArray("ImageBytes", imagebytes);
            bundlecoach.putString("aid", aid);
            bundlecoach.putString("type", type);
            startActivity(new Intent(Score_From.this, SearchActivity.class).putExtra("check_id", "Singles").putExtra("y", value).putExtras(bundlecoach).putExtra("date_value", pass_date));

        } else {
            Bundle bundle1 = new Bundle();
            bundle1.putString("Pname", PName);
            bundle1.putString("Pid", PId);
            bundle1.putString("type", type);
            bundle1.putString("lastScoreDate", lastScoreEntryDate);
            bundle1.putString("ScoreLast", Score);
            bundle1.putString("Image", ImagePlayer);
            Intent intent = new Intent(Score_From.this, SearchActivity.class).putExtras(bundle1).putExtra("check_id", "Singles");
            startActivity(intent);
        }
    }

    public void doubles_select(View view) {
        oncourt_skill.setChecked(true);
        linearLayout.setVisibility(View.VISIBLE);
        fitness.setChecked(false);
        grip.setChecked(false);
        singles_oncourt.setChecked(false);
        doubles_oncourt.setChecked(true);
        shadow_oncourt.setChecked(false);
        // spin.setVisibility(View.VISIBLE);
        if (type.equalsIgnoreCase("Coach")) {
            Bundle bundlecoach = new Bundle();
            bundlecoach.putString("coach_id", cid);
            bundlecoach.putString("coachname", coach_name);
            bundlecoach.putString("date", coachdate);
            bundlecoach.putString("level", level);
            bundlecoach.putString("Academy", academy);
            bundlecoach.putString("PlayerName", playerName);
            bundlecoach.putString("playerId", playerId);
            bundlecoach.putByteArray("ImageBytes", imagebytes);
            bundlecoach.putString("type", type);
            bundlecoach.putString("aid", aid);
            startActivity(new Intent(Score_From.this, SearchActivity.class).putExtra("check_id", "Doubles").putExtra("y", value).putExtras(bundlecoach).putExtra("date_value", pass_date));

        } else {
            Bundle bundle1 = new Bundle();
            bundle1.putString("Pname", PName);
            bundle1.putString("Pid", PId);
            bundle1.putString("type", type);
            bundle1.putString("lastScoreDate", lastScoreEntryDate);
            bundle1.putString("ScoreLast", Score);
            bundle1.putString("Image", ImagePlayer);
            Intent intent = new Intent(Score_From.this, SearchActivity.class).putExtras(bundle1).putExtra("check_id", "Doubles");
            startActivity(intent);
        }
    }

    public void shadow_select(View view) {
        oncourt_skill.setChecked(true);
        linearLayout.setVisibility(View.VISIBLE);
        fitness.setChecked(false);
        grip.setChecked(false);
        singles_oncourt.setChecked(false);
        doubles_oncourt.setChecked(false);
        shadow_oncourt.setChecked(true);

        if (type.equalsIgnoreCase("Coach")) {
            Bundle bundlecoach = new Bundle();
            bundlecoach.putString("coach_id", cid);
            bundlecoach.putString("coachname", coach_name);
            bundlecoach.putString("date", coachdate);
            bundlecoach.putString("level", level);
            bundlecoach.putString("Academy", academy);
            bundlecoach.putString("PlayerName", playerName);
            bundlecoach.putString("playerId", playerId);
            bundlecoach.putByteArray("ImageBytes", imagebytes);
            bundlecoach.putString("type", type);
            bundlecoach.putString("aid", aid);

            startActivity(new Intent(Score_From.this, SearchActivity.class).putExtra("check_id", "Shadow/Footwork").putExtra("y", value).putExtras(bundlecoach).putExtra("date_value", pass_date));
        } else {
            Bundle bundle1 = new Bundle();
            bundle1.putString("Pname", PName);
            bundle1.putString("Pid", PId);
            bundle1.putString("type", type);
            bundle1.putString("lastScoreDate", lastScoreEntryDate);
            bundle1.putString("ScoreLast", Score);
            bundle1.putString("Image", ImagePlayer);
            Intent intent = new Intent(Score_From.this, SearchActivity.class).putExtras(bundle1).putExtra("check_id", "Shadow/Footwork");
            startActivity(intent);
        }
    }

}
