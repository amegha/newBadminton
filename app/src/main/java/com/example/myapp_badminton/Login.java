package com.example.myapp_badminton;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements AsyncResponse{

    public Button btn_signIn,reset;
    public EditText name,password;
    public TextView Registration,password_forgot;
    SQLiteDatabase sqLiteDatabase;
    private int counter=5,x;
    public String Reg_email,Password,today,type,Id,Name,lastScoreEntryDate,Score;
    AlertDialog alertDialog;
    //variables used for sharedpreferences
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "myPrefs" ;
    public static final String Password_pref = "passKey";
    public static final String Email_pref = "emailKey";



    SQLiteDatabase db;
    Cursor cursor,cursor_name;//cursor_days_not_entered,cursor_lastDate,cursor_sec_last_login;
    String last_date,savedID,UserName,sec_lastDate,pending_day;


    //this is for first time registration
    public void Register(View view)
    {
        Intent intent=new Intent(Login.this,MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name=findViewById(R.id.username_signin);
        password=findViewById(R.id.password_signin);
        btn_signIn=findViewById(R.id.btn_signIn);
        reset=findViewById(R.id.btn_reset);
        Registration=findViewById(R.id.signUp_text);
        password_forgot=findViewById(R.id.forgot_pass);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reg_email=name.getText().toString();
                Password=password.getText().toString();
                Save();
                new WebService(Login.this).execute(API.ServerAddress + "" + API.USER_LOGIN, "mail_id=" + Reg_email+"&password="+Password);
                name.setText("");
                password.setText("");
            }
        });

        password_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Reset_pass.class);
                startActivity(intent);
            }
        });

    }
    //to save in xml file called "myPrefs"
    public void Save() {
        String n = name.getText().toString();
        String e = password.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Email_pref, n);
        editor.putString(Password_pref, e);
        editor.commit();

        if (sharedpreferences.contains(Email_pref)) {
            name.setText(sharedpreferences.getString(Email_pref, ""));
        }
        if (sharedpreferences.contains(Password_pref)) {
            password.setText(sharedpreferences.getString(Password_pref, ""));

        }

    }
    @Override
    public void onTaskComplete(String result) {
        String[] arrRes;
        arrRes = result.split(",");
        String locationXml;
        Log.e("ViewUserDetails", " arrRes[0] " + arrRes[0] + " arrRes[1]  " +  arrRes[1] + "  arrRes[2]" + arrRes[2]);

        if(arrRes.length>4){
            type=arrRes[0];
            Id=arrRes[1];
            Name=arrRes[2];
            lastScoreEntryDate=arrRes[3];
            Score=arrRes[4];}
        else
        {
            type=arrRes[0];
            Id=arrRes[1];
            Name=arrRes[2];
        }

        if(type.equals("coach")) {
            Bundle bcoach = new Bundle();
            bcoach.putString("type", type);
            bcoach.putString("Name", Name);
            bcoach.putString("Id", Id);
            Intent intent = new Intent(Login.this, HomePage.class).putExtras(bcoach);
            startActivity(intent);
        }
        else if(type.equals("player")) {
            Bundle bplayer = new Bundle();
            bplayer.putString("type", type);
            bplayer.putString("Name", Name);
            bplayer.putString("Id", Id);
            bplayer.putString("DateLastScore", lastScoreEntryDate);
            bplayer.putString("lastScore", Score);
            Intent intent = new Intent(Login.this, HomePage.class).putExtras(bplayer);
            startActivity(intent);
        }


    }

    public void forgotPassword(View view) {
        createAlertDialog();

    }

    private void createAlertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.forgot_password_or_pin, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        alertDialog = alert.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void getOtp(View view) {


    }
}