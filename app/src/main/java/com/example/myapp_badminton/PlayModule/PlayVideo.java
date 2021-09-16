package com.example.myapp_badminton.PlayModule;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myapp_badminton.API;
import com.example.myapp_badminton.HomePage;
import com.example.myapp_badminton.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PlayVideo extends AppCompatActivity implements AsyncResponse {
    public static final String PREFS_NAME = "LoginPrefs";
    private static final int REQUEST_RUNTIME_PERMISSIONS = 1;
    static MediaPlayer mediaPlayer;
    public static ArrayList<AnswersModel> answersModelsArray = new ArrayList<>();
    static int[] pauses, maxTime;
    static String link ,videoName;
    private static CustomAdapter adapter;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    Context context;
    int initPos, currPos, watchAgainCount, pauseAt, REQUEST_ANSWER = 1, answerCount, score, livePlay;
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
                    //Log.e("info", "MEDIA_INFO_VIDEO_RENDERING_START");
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                    //Log.e("info", "MEDIA_INFO_BUFFERING_START");
                    progressBar.setVisibility(View.VISIBLE);
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                    //Log.e("info", "MEDIA_INFO_BUFFERING_END");
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
                    //Log.e("run: ", "String current pos subString " + sCurrPos + " pause[] value " + pauses[pauseAt] + "media Player " + mediaPlayer);
                    mediaPlayer.pause();
                    buttonEnable();
                    watchAgainCount = 0;
                }
//            ctv.setText(sCurrPos);
            if (remainingTime > 0 && mediaPlayer.isPlaying()) {
                handler.postDelayed(stopPlayerTask, handlerTime = handlerTime + 1);
            }
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //Log.e("redirecting", "to view answer");
                    startActivity(new Intent(getApplication(), ViewAnswers.class).putExtras(bundle));
                    finish();
//                        new WebService(this).execute(API.ServerAddress + "" + API.USER_REGISTER, xml);
                }
            });

        }
    };
    Thread downloadVideo = new Thread() {
        public void run() {
//            Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show();

            new WebService(context).execute(API.VIDEO_LINK_DOWNLOAD + "" + videoName, "downloadVideo");
        }
    };
    private GetAnswers getAnswers;
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
    static String baseURL;

    public void verifyStoragePermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)//required for blutooth scan
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_RUNTIME_PERMISSIONS
            );
        } else {
            checkDBAndGetCorrectAnswer();
        }


    }

    private void checkDBAndGetCorrectAnswer() {
        if (db.isDataEmpty()) {
            if (isConnectingToInternet(this)) {

                getAnswers = new GetAnswersImpl1(this, new WebService(this), p_id,baseURL);
                getAnswers.getCorrectAnswersFromServer();
//            connectionAndVideoStream();
            } else {
                Toast.makeText(this, "You are offline!", Toast.LENGTH_SHORT).show();
            }
        } else {
            livePlay = 1;
            String res = db.getAllData();
            correctAnswers = res.split(",");
//            videoName = correctAnswers[correctAnswers.length - 1];
//            link = link +videoName;
            pauses = new int[correctAnswers.length - 1];
            correctShotLoc = new String[correctAnswers.length - 1];
            correctShotType = new String[correctAnswers.length - 1];
            videoId = new String[correctAnswers.length - 1];
            maxTime = new int[correctAnswers.length - 1];
            for (int i = 0; i < correctAnswers.length - 1; i++) {
                answerContents = correctAnswers[i].split(":");
                videoId[i] = answerContents[0];
                correctShotLoc[i] = answerContents[1];
                correctShotType[i] = answerContents[2];
                pauses[i] = Integer.parseInt(answerContents[3]);
                maxTime[i] = Integer.parseInt(answerContents[4]);

            }
            videoName = correctAnswers[correctAnswers.length - 1];
            link = baseURL + videoName;
            /*link = this.getCacheDir()
                    + File.separator + "My_Video/video.mp4";*/
            connectionAndVideoStream();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RUNTIME_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
//                    connectionAndVideoStream();
                    getAnswers = new GetAnswersImpl1(this, new WebService(this), p_id,baseURL);
                    getAnswers.getCorrectAnswersFromServer();
//                    //Log.i(TAG, "onRequestPermissionsResult: Permission Granted");
                } else {
                    Log.i("Permission", "Permission Denied");
                }
            }
            /*getAnswers = new GetAnswersImpl1(this, new WebService(this));
            getAnswers.getCorrectAnswersFromServer();*/
        }
    }

    private void buttonEnable() {
        watchAgain.setVisibility(View.VISIBLE);
        answerQuestions.setVisibility(View.VISIBLE);
    }

    private void showQuestions() {
        intent = new Intent(this, SubmitAnswer.class);

        new AlertDialog.Builder(this)
                .setTitle("Answer the question")
                .setMessage(this.getResources().getString((R.string.question_info)))
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("ans", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        start = System.nanoTime();
                        //Log.e("playvideo", "showquestions start time" + start);
                        startActivityForResult(intent, REQUEST_ANSWER);
                    }
                }).show();
    }

    private void buttonDisable() {
        watchAgain.setVisibility(View.GONE);
        answerQuestions.setVisibility(View.GONE);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paly_video);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        p_id = settings.getString("Id", "");
        baseURL =settings.getString("baseURL","")+"/";
        baseURL = checkForHTTP(baseURL);
        initView();
        verifyStoragePermissions(this);
        context = this;
        handler = new Handler();
    }

    private void initView() {
        vv = findViewById(R.id.video_view);
        ctv = findViewById(R.id.count_down);
        progressBar = findViewById(R.id.progressbar);
        db = new DBHandler(this);
        watchAgain = findViewById(R.id.watch_again);
        answerQuestions = findViewById(R.id.answer_questions);
    }

    private String checkForHTTP(String url) {
        if(!url.substring(0,8).equals("https://")) {
            url = url.substring(0, 7).equals("http://") ? url.substring(7) :  url;
            url="https://" +url;
        }
        return url;
    }

    private void connectionAndVideoStream() {
        if (isConnectingToInternet(this)) {
            try {
                if (mediaController == null) {
                    mediaController = new MediaController(this);
                }
                mediaController.setAnchorView(vv);
                Uri video = Uri.parse(link);
                Log.e("Playvideo", "connectionAndVideoStream:------>"+link );
                vv.setMediaController(null);
                vv.setVideoURI(video);
                buttonDisable();
                progressBar.setVisibility(View.VISIBLE);
                vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer = mp;
                        PlaybackParams myPlayBackParams = null;
                        playVideoWithHandler(mediaPlayer);                    }
                });
            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("exception in connection", " " + e.getMessage());
            }
        } else {
            Toast.makeText(this, "No Network or Slow network", Toast.LENGTH_SHORT).show();
            //Log.e("exception in connection", "No network");
        }
    }

    private void playVideoWithHandler(MediaPlayer mp) {
        PlaybackParams myPlayBackParams;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                myPlayBackParams = new PlaybackParams();
    //            mp.setVolume(0f, 0f);
    //            mp.setPlaybackParams(myPlayBackParams);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    mp.seekTo(initPos, MediaPlayer.SEEK_CLOSEST);
                else
                    mp.seekTo(initPos);
                //Log.e("TAG", "playVideoWithHandler: initPOS "+initPos );
                mp.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int arg1,
                                                   int arg2) {
                        mediaPlayer = mp;
                        // TODO Auto-generated method stub
                        progressBar.setVisibility(View.GONE);
                        mp.start();
                    }
                });
                vv.setOnInfoListener(onInfoToPlayStateListener);
                handler.postDelayed(stopPlayerTask, handlerTime);
                buttonDisable();
            }// END OF IF
        } catch (IllegalStateException e) {
            e.printStackTrace();
            //Log.e("exception", "playVideoWithHandler: "+e.getMessage() );
        }
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
        buttonEnable();
        watchAgainCount = 0;
        //Log.e("autoPauseVideo:", " timer " + pauseAt + "\n video time " + nonTimer);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void answerQuestions(View view) {
        showQuestions();
    }

    public void watchAgain(View view) {
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

            buttonDisable();
        } else {
            Toast.makeText(this, "please wait!!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnectingToInternet(Context applicationContext) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int mExitValue = mIpAddrProcess.waitFor();
            ////System.out.println(" mExitValue " + mExitValue);
            if (mExitValue == 0) {
                return true;
            } else if (mExitValue == 2) {
                return isNetworkAvailable();
            } else {
                return false;
            }
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
            ////System.out.println(" Exception:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            ////System.out.println(" Exception:" + e);
        }
        return false;
    }

    @Override
    protected void onStart() {

        super.onStart();
     /*   if (livePlay == 1) {
//            link = this.getCacheDir()
                    + File.separator + "My_Video/video.mp4";
        } else {*/
            link = baseURL + videoName;
            System.out.println("Live streaming---------->"+ link);
//        }

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
        buttonDisable();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ANSWER) {
            if (resultCode == Activity.RESULT_OK) {
                playerTime = (System.nanoTime() - start) / 1_000_000_000;
                bundle = data.getExtras();
                shotLoc = (String) bundle.get("shot_location");
                shotType = (String) bundle.get("shot_type");
                score = 0;
                if (correctShotType[answerCount].equalsIgnoreCase(shotType)) {
                    score += 1;
                }
                if (correctShotLoc[answerCount].equalsIgnoreCase(shotLoc)) {
                    score += 1;
                }
                if ((maxTime[answerCount] >= (playerTime)) && (score == 2)) {// bonus only when both the above answers are correct
                    score += 1;
                }
                db.saveAnswers(shotLoc, shotType, playerTime, score);
                //Log.e("type and loc", "onActivityResult: " + shotLoc + "\n" + shotType + "\n elapsed time " + playerTime);

                answersModelsArray.add(new AnswersModel(answerCount + 1, shotType, shotLoc, correctShotType[answerCount], correctShotLoc[answerCount], playerTime, maxTime[answerCount]));
                if (!(pauseAt > pauses.length - 1)) {
                    initPos = currPos;
                    pauseAt = pauseAt + 1;
                    answerCount++;
                }
//                connectionAndVideoStream();
                playVideoWithHandler(mediaPlayer);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //Log.e("redirecting", "to view answer");
                        bundle.putParcelable("answerModel", (Parcelable) answersModelsArray);
                        startActivity(new Intent(getApplication(), ViewAnswers.class).putExtras(bundle));
                        finish();
                    }
                });

            }
        } else {
            Toast.makeText(getApplicationContext(), "Answer is not submitted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskComplete(String result) {
        ////System.out.println("download_answers.php response " + result);
        if (result != null)
            switch (result) {
                case "00":
                case "01":
                case "02":
                case "103":
                    Toast.makeText(this, "Completed all the levels!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomePage.class));
                    finish();
                    break;
                case "done":
//                    progressDialog.dismiss();
                    livePlay = 1;
//                    connectionAndVideoStream();
                    break;
                default:
                    parseCorrectAnswers(result);
                    break;
            }
        else
            Toast.makeText(this, "server down!!", Toast.LENGTH_SHORT).show();
    }

    private void parseCorrectAnswers(String result) {
        try {
//        if (db.isDataEmpty()) {
            if (result.split(">").length > 1) {
                String[] playAnswers = result.split(">");
                result = playAnswers[1];

            correctAnswers = result.split(",");
            videoName = correctAnswers[correctAnswers.length - 1];
            link = baseURL + videoName;
            pauses = new int[correctAnswers.length - 1];
            correctShotLoc = new String[correctAnswers.length - 1];
            correctShotType = new String[correctAnswers.length - 1];
            videoId = new String[correctAnswers.length - 1];
            maxTime = new int[correctAnswers.length - 1];
            for (int i = 0; i < correctAnswers.length - 1; i++) {  //correctAnswer.length-1 coz it fetches the video name also at the end which is appended to result, seperated by ','
                answerContents = correctAnswers[i].split(":");
                //            for (int j = 0; j < answerContents.length; j++) {
                videoId[i] = answerContents[0];
                correctShotLoc[i] = answerContents[1];
                correctShotType[i] = answerContents[2];
                pauses[i] = Integer.parseInt(answerContents[3]);
                maxTime[i] = Integer.parseInt(answerContents[4]);

                db.storeCorrectAnswers(videoId[i], correctShotLoc[i], correctShotType[i], pauses[i], maxTime[i], videoName);
                //Log.e("playVideo", "pauses are " + pauses[i] + " ");
            }
            ////System.out.println("correct ansers" + correctAnswers);
            ////System.out.println("pauses" + pauses.toString());
            connectionAndVideoStream();
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void answersFromServer(String[] shotLoc, String[] shotType, int[] pauses, int length) {
        //Log.e("playVideo", "answersFromServer: pauses are set");
//        this.pauses = pauses;
        correctShotLoc = shotLoc;
        correctShotType = shotType;
        connectionAndVideoStream();
    }
}