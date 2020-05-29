package com.example.myapp_badminton;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp_badminton.megha.API;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity implements AsyncResponse {
    public Button btn_signIn, reset;
    public EditText name, password;
    public TextView Registration;
    public String Username, Password, today;
    MyDbAdapter helper;
    SQLiteDatabase sqLiteDatabase;
    LoginInterface loginInterface;
    ScoreStorageAdapter sHelper;
    SQLiteDatabase db, db1;
    Cursor cursor, cursor_days_not_entered, cursor_lastDate, cursor_sec_last_login;
    String last_date, savedID, sec_lastDate, pending_day;
    AlertDialog.Builder alertbuilder;
    DBHandler dbHandler;
    ImageView imageView;
    private int counter = 5, x;

    public void Register(View view) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHandler = new DBHandler(this);
        name = findViewById(R.id.username_signin);
        password = findViewById(R.id.password_signin);
        btn_signIn = findViewById(R.id.btn_signIn);
        reset = findViewById(R.id.btn_reset);
        Registration = findViewById(R.id.signUp_text);
        imageView = findViewById(R.id.userImage);

        alertbuilder = new AlertDialog.Builder(this);

        //to access or get data from Score details

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Username = name.getText().toString();
                Password = password.getText().toString();

              /*  Intent intent = new Intent(Login.this, HomePage.class);
                startActivity(intent);*/
                validateCredentialOnline(Username, Password);
//                password_validate(Username, Password);
                //sendData(Username);

            }
        });

    }

    private void validateCredentialOnline(String Username, String Password) {
//        new WebService(this).execute(API.ServerAddress + API.USER_LOGIN, "mail_id=" + Username + "&password=" + Password);
        new WebService(this).execute(API.ServerAddress + "player_details.php", "academy_id=2&level=1&coach_id=22");

    }


    public void sendData(String value) {
        Intent intent = new Intent(Login.this, SearchActivity.class).putExtra("x", value).putExtra("P", "fromLogin");
        startActivity(intent);
    }

    private void reset_password() {
        Intent intent = new Intent(Login.this, Reset_pass.class);
        startActivity(intent);
    }


    public void password_validate(final String UserName, String UserPassword) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        today = sdf.format(date);

        Cursor cursor_name, cursor_pass;
        sHelper = new ScoreStorageAdapter(this);
        helper = new MyDbAdapter(getApplicationContext());
        db1 = sHelper.scoreStorageHelper.getReadableDatabase();
        db = helper.myhelper.getReadableDatabase();
        sqLiteDatabase = helper.myhelper.getReadableDatabase();
        cursor_name = helper.myhelper.search_name(UserName, sqLiteDatabase);
        cursor_pass = helper.myhelper.search_pass(UserPassword, sqLiteDatabase);

        if ((cursor_name != null) && (cursor_pass != null)) {
            cursor_name.moveToFirst();
            cursor_pass.moveToFirst();

            /*the below if statement use to check or match username and phoneNumber entered by user with respective username and phoneNumber in database
             * */
            if (cursor_name.isAfterLast() == false && cursor_pass.isAfterLast() == false) {

                String a = cursor_name.getString(0);
                String b = cursor_pass.getString(0);


                if ((UserName.equals(a)) && (UserPassword.equals(b))) {

//                    loginInterface = new LoginImpl(new WebService(this), this, "username=" + UserName + "&phoneNumber=" + UserPassword);
//                    loginInterface.loginToApp();
////                        startActivity(new Intent(this,HomePage.class));
//                        new WebService(this).execute(API.ServerAddress + "" + API.USER_LOGIN, UserName,UserPassword);

                    cursor = helper.myhelper.getUID(UserName, db);

                    if (cursor != null) {
                        cursor.moveToFirst();
                        if (cursor.isAfterLast() == false) {
                            savedID = cursor.getString(0);
                        }
                        cursor.moveToNext();
                    }
                    cursor.close();


                    // to get last date of score sumission
                    cursor_lastDate = sHelper.scoreStorageHelper.get_last_login(savedID, db1);
                    if (cursor_lastDate != null) {
                        cursor_lastDate.moveToFirst();
                        if (cursor_lastDate.isAfterLast() == false) {
                            last_date = cursor_lastDate.getString(0);

                        }
                        cursor_lastDate.moveToNext();
                    }
                    cursor_lastDate.close();


                    //to get second last score submission date
                    cursor_sec_last_login = sHelper.scoreStorageHelper.get_sec_last_login(savedID, db1);

                    if (cursor_sec_last_login != null) {

                        cursor_sec_last_login.moveToFirst();
                        if (cursor_sec_last_login.isAfterLast() == false) {
                            sec_lastDate = cursor_sec_last_login.getString(0);

                        }
                        cursor_sec_last_login.moveToNext();
                    }
                    cursor_sec_last_login.close();

                    cursor_days_not_entered = sHelper.scoreStorageHelper.get_no_days_score_NotEntered(savedID, last_date, db1);
                    if (cursor_days_not_entered != null) {

                        cursor_days_not_entered.moveToFirst();
                        if (cursor_days_not_entered.isAfterLast() == false) {
                            x = cursor_days_not_entered.getInt(0);

                        }
                        cursor_days_not_entered.moveToNext();
                    }
                    cursor_days_not_entered.close();

//                  for first time login
                    if (last_date == null && sec_lastDate == null && x == 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("date", today);
                        bundle.putString("x", UserName);
                        //onBackPressed();
                        Intent intent = new Intent(Login.this, HomePage.class).putExtras(bundle);
                        startActivity(intent);
                        name.setText("");
                        password.setText("");
                    }


                    if (last_date != null && sec_lastDate == null && last_date != sec_lastDate && last_date == today) {

                        Toast.makeText(getApplicationContext(), "already Entered today Score!,Wait for Tomorrow......", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("date", today);
                        bundle.putString("x", UserName);
                        Intent intent = new Intent(Login.this, HomePage.class).putExtras(bundle);
                        startActivity(intent);
                        name.setText("");
                        password.setText("");
                    } else if (last_date != null && sec_lastDate != null && !last_date.equals(sec_lastDate) && last_date.equals(today)) {

                        Toast.makeText(getApplicationContext(), "already Entered today Score!,Wait for Tomorrow......", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("date", today);
                        bundle.putString("x", UserName);
                        Intent intent = new Intent(Login.this, HomePage.class).putExtras(bundle);
                        startActivity(intent);
                        name.setText("");
                        password.setText("");

                    }
                    // if user missed session and score entry pending
                    else if (last_date != null && sec_lastDate != null && !last_date.equals(today)) {

                        if (x > 0 || x != 0) {

                            pending_day = getNextDate(last_date);
                            if (!pending_day.equals(today)) {
                                alertbuilder.setMessage("Please Enter Score on " + pending_day + " Session.....")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Bundle bundle = new Bundle();
                                                bundle.putString("date", today);
                                                bundle.putString("x", UserName);
                                                //onBackPressed();
                                                //Intent intent = new Intent(Login.this, Score_From.class).putExtras(bundle);
                                                Intent intent = new Intent(Login.this, HomePage.class).putExtras(bundle);
                                                startActivity(intent);
                                                name.setText("");
                                                password.setText("");
                                            }
                                        });
                                AlertDialog alert = alertbuilder.create();
                                //Setting the title manually
                                alert.setTitle("Missed Session Entry Alert!");
                                alert.show();

                            }
                            //already loged in,entering todays score
                            else if (pending_day.equals(today)) {

                                Bundle bundle = new Bundle();
                                bundle.putString("date", today);
                                bundle.putString("x", UserName);
                                //onBackPressed();
                                Intent intent = new Intent(Login.this, HomePage.class).putExtras(bundle);
                                startActivity(intent);
                                name.setText("");
                                password.setText("");
                            }


                        }
                    } else if (x == 0 && last_date != null && sec_lastDate == null && last_date != sec_lastDate && last_date.equals(today)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("date", today);
                        bundle.putString("x", UserName);
                        Toast.makeText(getApplicationContext(), "already Entered today Score!,Wait for Tomorrow......", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Login.this, HomePage.class).putExtras(bundle);
                        startActivity(intent);
                        name.setText("");
                        password.setText("");
                    }


                }
                cursor_name.moveToNext();
                cursor_pass.moveToNext();

            } else {
                counter--;

                Toast.makeText(getApplicationContext(), "Remaining :" + String.valueOf(counter) + " attempts,please Enter Correct Username/Password", Toast.LENGTH_LONG).show();
                if (counter == 0) {
                    btn_signIn.setEnabled(false);
                    btn_signIn.setVisibility(View.VISIBLE);
                    btn_signIn.setBackgroundColor(222);
                    reset.setVisibility(View.VISIBLE);
                    reset.setEnabled(true);
                    reset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reset_password();
                        }
                    });
                    name.setText("");
                    password.setText("");
                }
            }
            cursor_name.close();
            cursor_pass.close();

        }

    }

    private String getNextDate(String inputDate) {
        //inputDate = "2015-12-15"; // for example
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {

            Date date = format.parse(inputDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            c.add(Calendar.MONTH, 2);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.DATE, -1);


            Date lastDayOfMonth = c.getTime();
            int month = c.get(Calendar.MONTH) + 1;
            int years = c.get(Calendar.YEAR);
            int day = c.get(Calendar.DAY_OF_MONTH);

           /* String s;
            if(month < 10)
            {
                s=years+"-0"+month+"-"+day;
            }
            else
            {
                s=years+"-"+month+"-"+day;
            }*/
            if (inputDate.equals(format.format(lastDayOfMonth))) {
                c.add(Calendar.MONTH, 1);
                c.set(Calendar.DAY_OF_MONTH, 0);
                c.add(Calendar.DATE, 1);

                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                Date FirstDayOfMonth = c.getTime();
                int new_day = c.get(Calendar.DAY_OF_MONTH);
                int new_month = c.get(Calendar.MONTH) + 1;
                int new_year = c.get(Calendar.YEAR);
                /*if(month < 10)
                {
                    inputDate=new_year+"-0"+new_month+"-"+new_day;
                }
                else
                {
                    inputDate=new_year+"-"+new_month+"-"+new_day;
                }*/
                inputDate = sdf.format(FirstDayOfMonth);

                return inputDate;

            } else {
                c.setTime(date);
                c.add(Calendar.DATE, +1);
                Date s1 = c.getTime();
                int months = c.get(Calendar.MONTH) + 1;
                int yearss = c.get(Calendar.YEAR);
                int days = c.get(Calendar.DAY_OF_MONTH);
                /*if(month < 10)
                {
                    inputDate=yearss+"-0"+months+"-"+days;
                }
                else
                {
                    inputDate=yearss+"-"+months+"-"+days;
                }*/
                inputDate = format.format(c.getTime());
                Log.d("asd", "selected date : " + inputDate);
                return inputDate;

            }

            // System.out.println(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            inputDate = "";
        }
        return inputDate;
    }

    @Override
    public void onTaskComplete(String result) {
        String[] arrRes;
        String[] arrPlayerData;
        arrRes = result.split(";");
        for (int i = 0; i < arrRes.length; i++) {
            arrPlayerData = arrRes[i].split(",");
            byte[] byteImage=Base64.decode(arrPlayerData[arrPlayerData.length - 1], Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
            imageView.setImageBitmap(decodedByte);
        }
//        dbHandler.getAllData();

        /*if (result.equals("Sucessfully logged in")) {
            Log.e(this.getPackageName(), "onTaskComplete: " + result);
            startActivity(new Intent(this, HomePage.class));
        }*/
    }
}
