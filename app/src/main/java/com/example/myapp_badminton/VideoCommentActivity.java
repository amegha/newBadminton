package com.example.myapp_badminton;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.Arrays;

//import static com.example.myapp_badminton.HomePage.PREFS_NAME;

@RequiresApi(api = Build.VERSION_CODES.O)
public class VideoCommentActivity extends Activity implements AsyncResponse {

    static MediaPlayer mediaPlayer;
    static String link = API.VIDEO_LINK, videoName;
    Context context;

    int[] pauses = {166, 798, 1713, 2101, 3426};
    String[] comments = {"right leg front", "left hand up", "neck rotate", "hand rotate", "left Hand rotate"};

    int initPos, currPos, watchAgainCount, pauseAt, REQUEST_ANSWER = 1, answerCount, score, livePlay, watchNextComment;
    Button watchAgain, answerQuestions;
    MediaController mediaController;
    Bundle bundle = new Bundle();
    TextView ctv;
    Handler handler;
    String[] correctAnswers;
    String[] answerContents;
    ProgressBar progressBar = null;
    final MediaPlayer.OnInfoListener onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                    Log.e("info", "MEDIA_INFO_VIDEO_RENDERING_START");
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                    Log.e("info", "MEDIA_INFO_BUFFERING_START");
                    progressBar.setVisibility(View.VISIBLE);
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                    Log.e("info", "MEDIA_INFO_BUFFERING_END");
                    progressBar.setVisibility(View.GONE);
                    return true;
                }
            }
            return false;
        }
    };
    Intent intent;
    DBHandler db;
    int handlerTime = 0, remainingTime = 500000;
    String sCurrPos = "";
    Runnable stopPlayerTask = new Runnable() {
        @Override
        public void run() {
            remainingTime = remainingTime - handlerTime;
            sCurrPos = String.valueOf(currPos = (mediaPlayer.getCurrentPosition()));/*(pauses[pauseAt])/11; vv.getCurrentPosition();*/
            if (((pauseAt < pauses.length)))
                if (pauses[pauseAt] <= currPos)/*sCurrPos.length() >= 4 && sCurrPos.substring(0, 3).equals(pauses[pauseAt].substring(0, 3))*/ {
                    Log.e("run: ", "String current pos subString " + sCurrPos + " pause[] value " + pauses[pauseAt] + "media Player " + mediaPlayer);
                    mediaPlayer.pause();
//                    buttonEnable();
                    watchAgainCount = 0;
                    showComment();
                }
//            ctv.setText(sCurrPos);
            if (remainingTime > 0 && mediaPlayer.isPlaying()) {
                handler.postDelayed(stopPlayerTask, handlerTime = handlerTime + 1);
            }
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    showNextVideoDialog();
//                    updateWatchFlagInServer();
                }
            });

        }
    };

    private void showNextVideoDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Next!!")
                .setMessage("Watch Next video?")
//                .setMessage(getResources().getString(R.string.clear))
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Next Video", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //update the watch flag and after response get another set of comments
                        watchNextComment = 1;
                        updateWatchFlagInServer();

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                updateWatchFlagInServer();
                            }
                        }).show();


    }

    private void updateWatchFlagInServer() {
        new WebService(this).execute(API.ServerAddress + "" + API.UPDATE_WATCH_FLAG, "p_id=" + p_id + "&v_name=" + videoName);
    }

    private void showComment() {
        new AlertDialog.Builder(this)
                .setTitle("Coach Comment")
                .setMessage(comments[pauseAt])
//                .setMessage(getResources().getString(R.string.clear))
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton("Watch Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        watchAgain();
                    }
                })
                .setPositiveButton("Next Comment",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if (!(pauseAt > pauses.length - 1)) {
                                    initPos = currPos;
                                    pauseAt = pauseAt + 1;
                                }
                                dialog.dismiss();
                                playVideoWithHandler(mediaPlayer);
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        Toast.makeText(context, "video completed!!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).show();

    }

    Thread downloadVideo = new Thread() {
        public void run() {
            new com.example.myapp_badminton.PlayModule.WebService(context).execute(API.VIDEO_LINK_DOWNLOAD + "" + videoName, "downloadVideo");
        }
    };
    //    private GetAnswers getAnswers;
    private boolean isReceiverRegistered = false;
    private VideoView vv;
    Thread liveStream = new Thread() {
        public void run() {
//            Toast.makeText(context, "live streaming", Toast.LENGTH_SHORT).show();
            connectionAndVideoStream();
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            NetworkInfo info = getNetworkInfo(context);
            if (info != null && info.isConnected()) {
                //Todo code to execute if wifi connected
            } else {                  //Todo code to execute if wifi disconnected
            }
        }
    };
    private String shotLoc, shotType;
    private String[] correctShotLoc, correctShotType, videoId;
    private boolean shdStop = false;
    private long start, playerTime;
    private String p_id;
    private ProgressDialog progressDialog;


    /*private void buttonEnable() {
        watchAgain.setVisibility(View.VISIBLE);
    }*/

   /* private void showQuestions() {
        intent = new Intent(this, SubmitAnswer.class);

        new AlertDialog.Builder(this)
                .setTitle("Answer the question")
                .setMessage(this.getResources().getString((R.string.question_info)))
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("ans", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        start = System.nanoTime();
                        Log.e("playvideo", "showquestions start time" + start);
                        startActivityForResult(intent, REQUEST_ANSWER);
                    }
                }).show();
    }*/

    /*private void buttonDisable() {
        watchAgain.setVisibility(View.GONE);
        answerQuestions.setVisibility(View.GONE);
    }*/

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paly_video);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        p_id = bundle.getString("userId");
        vv = findViewById(R.id.video_view);
        ctv = findViewById(R.id.count_down);
        progressBar = findViewById(R.id.progressbar);
        db = new DBHandler(this);
        watchAgain = findViewById(R.id.watch_again);
        answerQuestions = findViewById(R.id.answer_questions);
//        link = "http://stage1.optipacetech.com/badminton/videos/training.mp4";
        link = "android.resource://" + getPackageName() + "/" + R.raw.training;
        context = this;
        handler = new Handler();
        getCommentsFromServer();
//        connectionAndVideoStream();
    }

    private void getCommentsFromServer() {
        new WebService(this).execute(API.ServerAddress + "" + API.COMMENTS, "user_id=" + p_id);
    }


    private void connectionAndVideoStream() {
       /* link = this.getCacheDir()
                + File.separator + "My_Video/video.mp4";*/
        /*if (livePlay == 1) {
            link = this.getCacheDir()
                    + File.separator + "My_Video/video.mp4";
        } else {
            link = API.VIDEO_LINK + videoName;
            System.out.println("Live streaming!!");
        }*/
        if (isConnectingToInternet(this)) {
            try {
                if (mediaController == null) {
                    mediaController = new MediaController(this);
                }
                mediaController.setAnchorView(vv);
                Uri video = Uri.parse(link);
//                Toast.makeText(context, "link " + link, Toast.LENGTH_SHORT).show();
                vv.setVideoURI(video);
//                buttonDisable();
                progressBar.setVisibility(View.VISIBLE);
                vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer = mp;
                        PlaybackParams myPlayBackParams = null;
                        playVideoWithHandler(mp);
                    }
                });
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("exception in connection", " " + e.getMessage());
            }
        } else {
            Toast.makeText(this, "No Network or Slow network", Toast.LENGTH_SHORT).show();
            Log.e("exception in connection", "No network");
        }
    }

    private void playVideoWithHandler(MediaPlayer mp) {
        PlaybackParams myPlayBackParams;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myPlayBackParams = new PlaybackParams();
//                            myPlayBackParams.setSpeed(0.2f); //you can set speed here
            mp.setVolume(0f, 0f);
            mp.setPlaybackParams(myPlayBackParams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                mp.seekTo(initPos, MediaPlayer.SEEK_CLOSEST);
            else
                mp.seekTo(initPos);
            mp.start();
            mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int arg1,
                                               int arg2) {
                    mediaPlayer = mp;
                    // TODO Auto-generated method stub
                    progressBar.setVisibility(View.GONE);
                    mp.start();
                    /*vv.setOnInfoListener(onInfoToPlayStateListener);
                    handler.postDelayed(stopPlayerTask, handlerTime);
                    buttonDisable();*/
                }
            });
            vv.setOnInfoListener(onInfoToPlayStateListener);
            handler.postDelayed(stopPlayerTask, handlerTime);
//            buttonDisable();
        }// END OF IF
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void autoPauseVideo(final int pauseAt, int nonTimer) {
        vv.pause();
        currPos = vv.getCurrentPosition();
//        buttonEnable();
        watchAgainCount = 0;
        Log.e("autoPauseVideo:", " timer " + pauseAt + "\n video time " + nonTimer);
//        handler.postDelayed(stopPlayerTask, vv.getCurrentPosition()/+ (secondsCompleted+1000)/);//pauses at 9 secs
//        handler.post(stopPlayerTask);
    }

   /* @RequiresApi(api = Build.VERSION_CODES.O)
    public void answerQuestions(View view) {
        // testing.. this view is acted as play button
        *//*if (!(pauseAt > pauses.length)) {
            initPos = currPos;
            pauseAt = pauseAt + 1;
            Log.e("answerButton pressed", " init pos is " + initPos);
            vv.requestFocus();
//            mediaPlayer.start();
            mediaPlayer.seekTo(initPos,MediaPlayer.SEEK_CLOSEST);
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            buttonDisable();
            handler.postDelayed(stopPlayerTask, handlerTime);

        }*//*
        showQuestions();
    }*/

    public void watchAgain(/*View view*/) {
        if (watchAgainCount == 0) {
            watchAgainCount++;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                mediaPlayer.seekTo(initPos, MediaPlayer.SEEK_CLOSEST);
            else
                mediaPlayer.seekTo(initPos);
            mediaPlayer.start();
            /*vv.seekTo(initPos);
            vv.start();*/
            handler.postDelayed(stopPlayerTask, handlerTime);

//            buttonDisable();
        } else {
            Toast.makeText(this, "please wait!!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnectingToInternet(Context applicationContext) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int mExitValue = mIpAddrProcess.waitFor();
            System.out.println(" mExitValue " + mExitValue);
            if (mExitValue == 0) {
                return true;
            } else if (mExitValue == 2) {
                return isNetworkAvailable();
            } else {
                return false;
            }
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
        }
        return false;
    }

    @Override
    protected void onStart() {

        super.onStart();
        if (livePlay == 1) {
            link = "android.resource://" + getPackageName() + "/" + R.raw.training;

//            link = this.getCacheDir()                    + File.separator + "My_Video/video.mp4";
        } else {
            link = "android.resource://" + getPackageName() + "/" + R.raw.training;

//            link = API.VIDEO_LINK + videoName;
            System.out.println("Live streaming!!");
        }

    }

    private void enableWifi() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);
        alertDialog.setTitle("Confirm...");
        alertDialog.setMessage("Do you want to go to wifi settings?");
        alertDialog.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
        alertDialog.setNegativeButton("no",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        buttonDisable();
        handler.removeCallbacks(stopPlayerTask);
        if (isReceiverRegistered) {
            isReceiverRegistered = false;
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        registerReceiver(receiver, new IntentFilter("android.crossCourtBlock.conn.CONNECTIVITY_CHANGE"));
        if (!isReceiverRegistered) {
            isReceiverRegistered = true;
            registerReceiver(receiver, new IntentFilter("android.crossCourtBlock.wifi.STATE_CHANGE"));
            vv.resume();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
//        checkDBAndGetCorrectAnswer();

    }

    private NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_ANSWER) {
//            if (resultCode == Activity.RESULT_OK) {
//                playerTime = (System.nanoTime() - start) / 1_000_000_000;
//                bundle = data.getExtras();
//                shotLoc = (String) bundle.get("shot_location");
//                shotType = (String) bundle.get("shot_type");
//                score = 0;
//                if (correctShotType[answerCount].equalsIgnoreCase(shotType)) {
//                    score += 1;
//                }
//                if (correctShotLoc[answerCount].equalsIgnoreCase(shotLoc)) {
//                    score += 1;
//                }
//                if ((maxTime[answerCount] >= (playerTime)) && (score == 2)) {// bonus only when both the above answers are correct
//                    score += 1;
//                }
//                db.saveAnswers(shotLoc, shotType, playerTime, score);
//                Log.e("type and loc", "onActivityResult: " + shotLoc + "\n" + shotType + "\n elapsed time " + playerTime);
//
//                answersModelsArray.add(new AnswersModel(answerCount + 1, shotType, shotLoc, correctShotType[answerCount], correctShotLoc[answerCount], playerTime, maxTime[answerCount]));
//                Log.e("array val ", "here " + answersModelsArray.size());
//               /* answersModel.setShotLocation((String) bundle.get("shot_location"));
//                answersModel.setShotType((String) bundle.get("shot_type"));*/
//
//                    /*Intent intent;
//                    intent = new Intent(SwipeCard.this, OnlineTransActivity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);*/
//                Log.e("onActivityResult0", "curr pos " + currPos + "\n init pos " + initPos);
//
//                if (!(pauseAt > pauses.length - 1)) {
//                    Log.e("onActivityResult1", "curr pos " + currPos + "\n init pos " + initPos);
//                    initPos = currPos;
//                    pauseAt = pauseAt + 1;
//                    answerCount++;
//                    Log.e("onActivityResult2", "curr pos " + currPos + "\n init pos " + initPos);
//                }
//                connectionAndVideoStream();
//
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        Log.e("redirecting", "to view answer");
//                        bundle.putParcelable("answerModel", (Parcelable) answersModelsArray);
//                        startActivity(new Intent(getApplication(), ViewAnswers.class).putExtras(bundle));
//                        finish();
////                        new WebService(this).execute(API.ServerAddress + "" + API.USER_REGISTER, xml);
//                    }
//                });
//
//            }
//        } else {
//            Toast.makeText(getApplicationContext(), "Answer is not submitted", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onTaskComplete(String result) {
        System.out.println("get_comment.php response " + result);
        if (result != null)
            switch (result) {
                case "00":
                case "01":
                case "02":
                case "103":
                    Toast.makeText(this, "Completed all the videos!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomePage.class));
                    finish();
                    break;
                case "done":
//                    progressDialog.dismiss();
                    livePlay = 1;
//                    connectionAndVideoStream();
                    break;
                case "update done":
                    if(watchNextComment==1){
                    getCommentsFromServer();
                    }else{
                        finish();
                    }
                default:
                    parseCorrectAnswers(result);
                    break;
            }
        else
            Toast.makeText(this, "server down!!", Toast.LENGTH_SHORT).show();
    }

    private void parseCorrectAnswers(String result) {
        try {
//            result="super admin:241,Left leg position incorrect :1409,Shot type Incorrect :5076,Shot location should be top left :8742,training/k2.mp4";
            correctAnswers = result.split(",");
            videoName = correctAnswers[correctAnswers.length - 1];
            link = API.VIDEO_LINK + videoName;
            pauses = new int[correctAnswers.length - 1];
            comments = new String[correctAnswers.length - 1];
            for (int i = 0; i < correctAnswers.length - 1; i++) {  //correctAnswer.length-1 coz it fetches the video name also at the end which is appended to result, seperated by ','
                answerContents = correctAnswers[i].split(":");
                //            for (int j = 0; j < answerContents.length; j++) {
                comments[i] = answerContents[0];
                pauses[i] = Integer.parseInt(answerContents[1]);
            }
            System.out.println("correct answers" + Arrays.toString(correctAnswers));
            System.out.println("correct comment" + Arrays.toString(comments));
            System.out.println("pauses" + Arrays.toString(pauses));
            connectionAndVideoStream();
//                downloadVideo.start();

//        progressDialog = ProgressDialog.show(this, "Loading the games for you!", "Please wait..", false, false);
//        new WebService(this).execute(API.VIDEO_LINK_DOWNLOAD + "" + videoName, "downloadVideo");

//            downloadVideo.start();
//            liveStream.start();
           /* downloadVideo.join();
            liveStream.join();*/
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
