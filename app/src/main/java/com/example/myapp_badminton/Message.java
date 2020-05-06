package com.example.myapp_badminton;

import android.content.Context;
import android.widget.Toast;
/*
* used for displaying toast messages ...*/
public class Message {
    public static void message(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
