package com.example.myapp_badminton.megha;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;

public class GetAnswersImpl1 implements GetAnswers {
    private static final String TAG = "GetAnswersImpl1";
    String serverAddress = API.ServerAddress + API.ANSWERS;
    private PlayVideo playVideo;
    private WebService webService;
    private HashMap<Integer, String> map1 = new HashMap<>();

    public  GetAnswersImpl1(PlayVideo playVideo, WebService webService) {
        this.serverAddress = serverAddress;
        this.playVideo = playVideo;
        this.webService = webService;
    }


    @Override
    public void getCorrectAnswersFromServer() {
        webService.execute(serverAddress, "user_id=47");

    }

    /*@Override
    public void getScore() {

    }*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void parseCorrectAnswers(String result) {
      /*  String[] correctAnswers = result.split(",");
        int[] pauses;
        String[] shotLoc;
        String[] shotType;
        String[] answerContents;
        Log.e(TAG, "length is" + correctAnswers.length);
        Log.e(TAG, "V_ID" + correctAnswers[0].split(":").length);
        pauses = new int[correctAnswers[0].length()];
        shotLoc = new String[correctAnswers[0].length()];
        shotType = new String[correctAnswers[0].length()];
        for (int i = 0; i < correctAnswers.length; i++) {
            answerContents = correctAnswers[i].split(":");
            for (int j = 0; j < answerContents.length; i++) {
                shotLoc[j] = answerContents[1];
                shotType[j] = answerContents[2];
//                pauses[j] = Integer.parseInt(answerContents[3]);
            }
            playVideo.answersFromServer(shotLoc,shotType,pauses,correctAnswers.length);

        }*/
    }
}
