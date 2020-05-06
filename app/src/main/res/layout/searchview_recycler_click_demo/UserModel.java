package com.example.searchview_recycler_click_demo;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserModel(String userName) {
        this.userName = userName;
    }
}
