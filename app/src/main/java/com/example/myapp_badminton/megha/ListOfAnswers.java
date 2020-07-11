package com.example.myapp_badminton.megha;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.myapp_badminton.ActivityTracker;
import com.example.myapp_badminton.R;

import java.util.ArrayList;

//import static com.example.myapp_badminton.HomePage.PREFS_NAME;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ListOfAnswers extends Activity {
    public static final String PREFS_NAME = "LoginPrefs";
    private static CustomAdapter adapter;
    ListView listView;
    ArrayList<AnswersModel> dataModels;
    DBHandler db;
    int[] pause;
    String link = PlayVideo.link;
    MediaController mediaController;
    MediaPlayer mediaPlayer;
    Uri video = Uri.parse(link);
    Handler handler = new Handler();
    Runnable stopPlayerTask = new Runnable() {
        @Override
        public void run() {
            try {
                mediaPlayer.pause();
            } catch (Exception e) {
                Toast.makeText(ListOfAnswers.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private String id;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        id = settings.getString("Id", "");

        ActivityTracker.writeActivityLogs(this.getLocalClassName(), id);
        db = new DBHandler(this);
        setContentView(R.layout.list_of_answers);
        listView = findViewById(R.id.list);
        dataModels = PlayVideo.answersModelsArray;
        adapter = new CustomAdapter(dataModels, getApplicationContext());
        listView.setAdapter(adapter);
        mediaController = new MediaController(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!db.isDataEmpty()) {
                    pause = db.getVideoPause(position);
                    review(pause);
                } else {
                    Toast.makeText(ListOfAnswers.this, "Cannot review!!", Toast.LENGTH_SHORT).show(); //db is empty
                }
            }

        });
    }

    private void review(final int[] pause) {
        try {
            VideoView vv = new VideoView(this);
            ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vv.setLayoutParams(layoutParams);
            mediaController.setAnchorView(vv);
            Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
            vv.setMediaController(null);
            vv.setVideoURI(video);
            vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer = mp;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mp.seekTo(pause[0], MediaPlayer.SEEK_CLOSEST);
                        mp.start();
                        mp.setVolume(0f,0f);
                        mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                            @Override
                            public void onVideoSizeChanged(MediaPlayer mp, int arg1,
                                                           int arg2) {
                                mp.start();
                                mp.setVolume(0f,0f);
                                if (pause.length == 1) {
                                    handler.postDelayed(stopPlayerTask, mp.getDuration());
                                } else
                                    handler.postDelayed(stopPlayerTask, pause[1]);
                            }
                        });
    //                    handler.postDelayed(stopPlayerTask, pause[1]);
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                Toast.makeText(ListOfAnswers.this, "completed!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }// END OF IF
                }
            });


            LinearLayout ll = new LinearLayout(this);
            ll.setLayoutParams(layoutParams);
            ll.addView(vv);
            final Dialog dialog = new Dialog(this);// add here your class etName
            dialog.setTitle("Review video!!");
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(ll);//add your own xml with defied with and height of videoview
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
