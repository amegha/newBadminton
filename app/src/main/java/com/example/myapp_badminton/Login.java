package com.example.myapp_badminton;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class Login extends AppCompatActivity implements AsyncResponse {

    public static final String PREFS_NAME = "LoginPrefs";
    public static final String Password_pref = "passKey";
    public static final String Email_pref = "emailKey";
    public Button btn_signIn, reset;
    public EditText etName, etPassword, email, etOTP, newPass, confirmNewPass;
    public TextView Registration, password_forgot;
    public String regEmail, password, today, type, Id, Name, lastScoreEntryDate, Score, image, gender;
    SQLiteDatabase sqLiteDatabase;
    AlertDialog alertDialog;
    String sNewPass, sNewPassConfirm;

    //variables used for sharedpreferences
    SharedPreferences sharedpreferences;
    SQLiteDatabase db;
    Cursor cursor, cursor_name;//cursor_days_not_entered,cursor_lastDate,cursor_sec_last_login;
    String last_date, savedID, UserName, sec_lastDate, pending_day;
    NetworkAvailability networkAvailability;
    private int counter = 5, x;
    private GetOTP getOTP;
    private ConfirmOTP confirmOTP;
    private ProgressDialog password_reset;

    //this is for first time registration
    public void Register(View view) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login_new);

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            if (settings.getString("logged", "").equals("logged")) {
                ActivityTracker.writeActivityLogs(this.getLocalClassName(), settings.getString("Id", ""));

                Intent intent = new Intent(Login.this, HomePage.class);
                //            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            //delete badminton database which existes after signing of previous user!
            deleteFile();

//        networkAvailability = new NetworkAvailability();
            etName = findViewById(R.id.username_signin);
            etPassword = findViewById(R.id.password_signin);
            btn_signIn = findViewById(R.id.btn_signIn);
            reset = findViewById(R.id.btn_reset);
//        Registration = findViewById(R.id.signUp_text);
//        password_forgot = findViewById(R.id.forgot_pass);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deleteFile() {
        try {
            String sourceFileUri = "/storage/emulated/0/Badminton";
            File sourceFile = new File(sourceFileUri + "/badminton.db");
            if (sourceFile.exists()) {
                if (sourceFile.delete()) {
                    System.out.println("file Deleted :" + sourceFile.getPath());
                } else {
                    System.out.println("file not Deleted :" + sourceFile.getPath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            Log.e("sign in", "result " + result);
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
                    etName.setError("wrong");
                    break;
                }case "103":{
                    etPassword.setError("wrong!");
                }
                case "502":
                case "503": {
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
                    password_reset.dismiss();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void prosesseSignInResponse(String result) {
        try {
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
                editor.putString("DateLastScore", lastScoreEntryDate);
                editor.putString("lastScore", Score);
                editor.putString("mail_id", regEmail);
                //            editor.putString("gender", gender);
                editor.apply();
                Intent intent = new Intent(Login.this, HomePage.class);
                //            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

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
                Intent intent = new Intent(Login.this, HomePage.class);
                //            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createResetPasswordAlertDialog() {
        try {
            LayoutInflater li = LayoutInflater.from(this);
            View confirmDialog = li.inflate(R.layout.activity_reset_password, null);
            newPass = confirmDialog.findViewById(R.id.pass_new);
            confirmNewPass = confirmDialog.findViewById(R.id.pass_confirm);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setView(confirmDialog);
            alertDialog = alert.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createConfirmOTPAlertDialog() {
        try {
            LayoutInflater li = LayoutInflater.from(this);
            View confirmDialog = li.inflate(R.layout.dialog_confirm, null);
            etOTP = confirmDialog.findViewById(R.id.editTextOtp);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setView(confirmDialog);
            alertDialog = alert.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forgotPassword(View view) {
        createAlertDialog();

    }

    private void createAlertDialog() {
        try {
            LayoutInflater li = LayoutInflater.from(this);
            View confirmDialog = li.inflate(R.layout.forgot_password_or_pin, null);
            email = confirmDialog.findViewById(R.id.editTextEmail);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setView(confirmDialog);
            alertDialog = alert.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getOtp(View view) {
        try {
            alertDialog.dismiss();
            ProgressDialog.show(this, "Sending OTP!!", "Please wait..", false, false);

            regEmail = email.getText().toString().trim();
//        final String mailId = email.getText().toString().trim();
            if (isConnected()) {
                getOTP = new GetOTPImpl(regEmail, new WebService(this), "forgot_password", "getOTP");
                getOTP.requestForOTP();
            } else {
                Toast.makeText(this, "You are offlne", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void validateOTP(View view) {
        try {
            alertDialog.dismiss();
            String OTP = etOTP.getText().toString().trim();
            if (isConnected()) {

                confirmOTP = new ConfirmOTPImpl(regEmail, new WebService(this), "forgot_password", "confirmOTP", OTP);
                confirmOTP.confirmOtp();
            } else {
                Toast.makeText(this, "You are offlne", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetPasswordOrPin(View view) {
        try {
            password_reset = ProgressDialog.show(this, "Password Resetting", "Please wait..", false, false);
//        alertDialog.dismiss();
            sNewPass = newPass.getText().toString().trim();
            sNewPassConfirm = confirmNewPass.getText().toString().trim();
            if (!sNewPass.equals("")) {
                if (sNewPass.equals(sNewPassConfirm)) {
                    alertDialog.dismiss();
                    if (isConnected()) {
                        new WebService(this).execute(API.ServerAddress + API.RESET_PASSWORD, "module=password_reset" + "&mail_id=" + regEmail + "&new_pin=" + sNewPass);

                    } else {
                        Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    confirmNewPass.setError("password doesnt match");

                }
            } else {
                newPass.setError("can't be empty");

            }
            password_reset.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signInButtonPressed(View view) {
        try {
            regEmail = etName.getText().toString();
            password = etPassword.getText().toString();
            signIn(regEmail, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void signIn(String regEmail, String password) {
        try {
            if (isConnected())
                new WebService(Login.this).execute(API.ServerAddress + API.USER_LOGIN, "mail_id=" + regEmail + "&password=" + password);
            else
                Toast.makeText(this, "You are offlne", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isConnected() {
        try {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}