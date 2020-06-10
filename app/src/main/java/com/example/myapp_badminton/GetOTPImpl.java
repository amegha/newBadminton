package com.example.myapp_badminton;

import android.util.Log;

public class GetOTPImpl implements GetOTP, AsyncResponse {
    String TAG = "GetOTPImpl";
    private String mailId;
    private String module;
    private String intent;
    private String serverAddr = API.ServerAddress + API.GENERATE_OTP;
    private WebService webService;


    public GetOTPImpl(String mailId, WebService webService, String module, String intent) {
        this.mailId = mailId;
        this.webService = webService;
        this.module = module;
        this.intent = intent;
    }

    @Override
    public void requestForOTP() {
        webService.execute(serverAddr, "module=" + module + "&mail_id=" + mailId+"&intent=" + intent);
    }

    @Override
    public void onTaskComplete(String result) {
        Log.e(TAG, "onTaskComplete: " + result);
    }
}
