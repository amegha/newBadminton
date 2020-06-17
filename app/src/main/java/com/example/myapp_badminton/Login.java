package com.example.myapp_badminton;

import android.app.AlertDialog;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity implements AsyncResponse {

    public static final String PREFS_NAME = "LoginPrefs";
    public static final String Password_pref = "passKey";
    public static final String Email_pref = "emailKey";
    public Button btn_signIn, reset;
    public EditText etName, etPassword, email, etOTP, newPass, confirmNewPass;
    public TextView Registration, password_forgot;
    public String regEmail, password, today, type, Id, Name, lastScoreEntryDate, Score, image;
    SQLiteDatabase sqLiteDatabase;
    AlertDialog alertDialog;
    String sNewPass, sNewPassConfirm;

    //variables used for sharedpreferences
    SharedPreferences sharedpreferences;
    SQLiteDatabase db;
    Cursor cursor, cursor_name;//cursor_days_not_entered,cursor_lastDate,cursor_sec_last_login;
    String last_date, savedID, UserName, sec_lastDate, pending_day;
    private int counter = 5, x;
    private GetOTP getOTP;
    private ConfirmOTP confirmOTP;

    //this is for first time registration
    public void Register(View view) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTracker.writeActitivtyLogs(this.getLocalClassName());
        setContentView(R.layout.activity_login);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").equals("logged")) {
            Intent intent = new Intent(Login.this, HomePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        etName = findViewById(R.id.username_signin);
        etPassword = findViewById(R.id.password_signin);
        btn_signIn = findViewById(R.id.btn_signIn);
        reset = findViewById(R.id.btn_reset);
        Registration = findViewById(R.id.signUp_text);
        password_forgot = findViewById(R.id.forgot_pass);

//        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

       /* btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regEmail = etName.getText().toString();
                password = etPassword.getText().toString();
                Save();
                new WebService(Login.this).execute(API.ServerAddress + "" + API.USER_LOGIN, "mail_id=" + regEmail + "&etPassword=" + password);
                etName.setText("");
                etPassword.setText("");
            }
        });*/

        /*password_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Reset_pass.class);
                startActivity(intent);
            }
        });*/

    }

    //to save in xml file called "myPrefs"
    public void Save() {
        String n = etName.getText().toString();
        String e = etPassword.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Email_pref, n);
        editor.putString(Password_pref, e);
        editor.apply();

        if (sharedpreferences.contains(Email_pref)) {
            etName.setText(sharedpreferences.getString(Email_pref, ""));
        }
        if (sharedpreferences.contains(Password_pref)) {
            etPassword.setText(sharedpreferences.getString(Password_pref, ""));

        }

    }

    @Override
    public void onTaskComplete(String result) {
//        try {
        switch (result) {
            case "00": {
                Toast.makeText(this, "Invalid Request", Toast.LENGTH_LONG).show();
                break;
            }
            case "01":
            case "02": {
                Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show();
                break;
            }
            case "03": {
                Toast.makeText(this, "User not found!", Toast.LENGTH_LONG).show();
                etPassword.setError("wrong!");
                etName.setError("wrong");
                break;
            }
            case "502": {
                Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
                break;
            }
            case "forgot_password/0/getOTP ": {
                createConfirmOTPAlertDialog();
                break;
            }
            case "forgot_password/0/confirmOTP": {
                createResetPasswordAlertDialog();
                break;
            }
            case "password_reset/0": {
                signIn(regEmail, sNewPass);
                break;
            }
            default:
                prosesseSignInResponse(result);
                break;
        }
        /*Log.e("here", "onTaskComplete: " + result);
        if (result.equals("forgot_password/0/getOTP ")) {
            createConfirmOTPAlertDialog();
        } else if (result.equals("forgot_password/0/confirmOTP")) {
            createResetPasswordAlertDialog();

        } else if (result.equals("password_reset/0")) {
            signIn(regEmail, sNewPass);
        } else {
            prosesseSignInResponse(result);
        }*/
        /*}catch (Exception e){

        }*/

    }

    private void prosesseSignInResponse(String result) {
        Log.e("here", "onTaskComplete: " + result);
        String[] arrRes;
        arrRes = result.split(",");
        String locationXml;
//        Log.e("ViewUserDetails", " arrRes[0] " + arrRes[0] + " arrRes[1]  " + arrRes[1] + "  arrRes[2]" + arrRes[2]);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        if (arrRes.length > 4) {
            type = arrRes[0];
            Id = arrRes[1];
            Name = arrRes[2];
            lastScoreEntryDate = arrRes[3];
            Score = arrRes[4];
            image = arrRes[5];
            //player
            editor.putString("logged", "logged");
            editor.putString("type", type);
            editor.putString("Id", Id);
            editor.putString("Name", Name);
            editor.putString("Image", image);
            editor.putString("DateLastScore", image);
            editor.putString("lastScore", image);
            editor.putString("mail_id", regEmail);
            editor.apply();
            startActivity(new Intent(this, HomePage.class));

        } else {
            //coach
            type = arrRes[0];
            Id = arrRes[1];
            Name = arrRes[2];

            editor.putString("logged", "logged");
            editor.putString("type", type);
            editor.putString("Id", Id);
            editor.putString("Name", Name);
            editor.putString("mail_id", regEmail);
            editor.apply();
            startActivity(new Intent(this, HomePage.class));
        }

       /* if (type.equals("coach")) {
            Bundle bcoach = new Bundle();
            bcoach.putString("type", type);
            bcoach.putString("Name", Name);
            bcoach.putString("Id", Id);
            Intent intent = new Intent(Login.this, HomePage.class).putExtras(bcoach);
            startActivity(intent);
        } else if (type.equals("player")) {
            Bundle bplayer = new Bundle();
            bplayer.putString("type", type);
            bplayer.putString("Name", Name);
            bplayer.putString("Id", Id);
            bplayer.putString("DateLastScore", lastScoreEntryDate);
            bplayer.putString("lastScore", Score);
            Intent intent = new Intent(Login.this, HomePage.class).putExtras(bplayer);
            startActivity(intent);
        }*/
    }

    private void createResetPasswordAlertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.activity_reset_password, null);
        newPass = confirmDialog.findViewById(R.id.pass_new);
        confirmNewPass = confirmDialog.findViewById(R.id.pass_confirm);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        alertDialog = alert.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void createConfirmOTPAlertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);
        etOTP = confirmDialog.findViewById(R.id.editTextOtp);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        alertDialog = alert.create();
        alertDialog.show();
    }

    public void forgotPassword(View view) {
        createAlertDialog();

    }

    private void createAlertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.forgot_password_or_pin, null);
        email = confirmDialog.findViewById(R.id.editTextEmail);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        alertDialog = alert.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void getOtp(View view) {
        alertDialog.dismiss();
        regEmail = email.getText().toString().trim();
//        final String mailId = email.getText().toString().trim();
        getOTP = new GetOTPImpl(regEmail, new WebService(this), "forgot_password", "getOTP");
        getOTP.requestForOTP();

    }

    public void validateOTP(View view) {
        alertDialog.dismiss();
        String OTP = etOTP.getText().toString().trim();
        confirmOTP = new ConfirmOTPImpl(regEmail, new WebService(this), "forgot_password", "confirmOTP", OTP);
        confirmOTP.confirmOtp();
    }

    public void resetPasswordOrPin(View view) {
//        alertDialog.dismiss();
        sNewPass = newPass.getText().toString().trim();
        sNewPassConfirm = confirmNewPass.getText().toString().trim();
        if (sNewPass.equals(sNewPassConfirm)) {
            alertDialog.dismiss();
            new WebService(this).execute(API.ServerAddress + API.RESET_PASSWORD, "module=password_reset" + "&mail_id=" + regEmail + "&new_pin=" + sNewPass);

        } else {
            confirmNewPass.setError("password doesnt match");
        }
    }

    public void signInButtonPressed(View view) {
        regEmail = etName.getText().toString();
        password = etPassword.getText().toString();
        signIn(regEmail, password);

    }

    private void signIn(String regEmail, String password) {
        new WebService(Login.this).execute(API.ServerAddress + API.USER_LOGIN, "mail_id=" + regEmail + "&password=" + password);

    }

}