package com.example.myapp_badminton;

import android.util.Log;

public class GetOTPImpl implements GetOTP ,AsyncResponse{
    private String mailId;
    private String module;
    private String serverAddr=API.ServerAddress + API.GENERATE_OTP;
    private WebService webService;
    String TAG="GetOTPImpl";


    public GetOTPImpl(String mailId, WebService webService, String module) {
        this.mailId = mailId;
        this.webService=webService;
        this.module=module;
    }

    @Override
    public void requestForOTP() {
        webService.execute(serverAddr, "module="+module+"&mail_id="+mailId);
    }

    @Override
    public void onTaskComplete(String result) {
        Log.e(TAG, "onTaskComplete: "+result );
    }
}
