package com.example.myapp_badminton.megha;

import android.app.Activity;
import android.app.Dialog;
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

import com.example.myapp_badminton.R;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ListOfAnswers extends Activity {
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHandler(this);
        setContentView(R.layout.list_of_answers);
        listView = (ListView) findViewById(R.id.list);
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
                    Toast.makeText(ListOfAnswers.this, "Database empty!!", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void review(final int[] pause) {
        VideoView vv = new VideoView(this);
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        vv.setLayoutParams(layoutParams);
        mediaController.setAnchorView(vv);
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
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int arg1,
                                                       int arg2) {
                            mp.start();
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
    }
}
