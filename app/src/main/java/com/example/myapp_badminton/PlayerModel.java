package com.example.myapp_badminton;


import java.io.Serializable;

public class PlayerModel implements Serializable {

    String City;
    String Aid;
    String Level;
    String Academy;


    public String getAid() {
        return Aid;
    }


    public String getLevel() {
        return Level;
    }


    public String getAcademy() {
        return Academy;
    }


    public PlayerModel(String city, String aid, String level, String academy) {
        City = city;
        Aid = aid;
        Level = level;
        Academy = academy;
    }
}