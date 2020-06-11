package com.example.myapp_badminton;

class ConfirmOTPImpl implements ConfirmOTP {
    String TAG = "GetOTPImpl";
    private String mailId;
    private String module;
    private String intent;
    private String otp;
    private String serverAddr = API.ServerAddress + API.CONFIRM_OTP;
    private WebService webService;

    public ConfirmOTPImpl(String mailId, WebService webService, String module, String intent,String otp) {
        this.mailId = mailId;
        this.webService = webService;
        this.module = module;
        this.intent = intent;
        this.otp = otp;
    }

    @Override
    public void confirmOtp() {
        webService.execute(serverAddr, "module=" + module + "&mail_id=" + mailId + "&intent=" + intent + "&otp=" + otp);

    }
}