package com.example.myapp_badminton;

import android.content.Context;

import com.example.myapp_badminton.LoginInterface;
import com.example.myapp_badminton.megha.API;
import com.example.myapp_badminton.megha.WebService;

public class LoginImpl implements LoginInterface {
    private com.example.myapp_badminton.megha.WebService webService;

    private Login login;
    private String data;

    public LoginImpl(WebService webService, Login login, String data) {
        this.webService = webService;
        this.login = login;
        this.data = data;
    }


    @Override
    public void loginToApp() {
        webService.execute(API.SYNC_TO_SERVER + API.USER_LOGIN, data);
    }
}
