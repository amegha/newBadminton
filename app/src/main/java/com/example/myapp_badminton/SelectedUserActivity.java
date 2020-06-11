package com.example.myapp_badminton;
/**
 * complete by pooja
 * 12/01/2020
 * used to display selected category
 * design of xml file is being modified for score calculation on 16/01/2020
 * checking if missed sessions been done on 12/02/2020
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelectedUserActivity extends AppCompatActivity {
    public String UNAME,UID,pending_day;
    int x;

    MyDbAdapter helper;
    ScoreStorageAdapter sHelper;
    SQLiteDatabase db,db1;
    Cursor cursor,cursor_days_not_entered,cursor_lastDate,cursor_sec_last_login;
    String last_date,savedID,Curr_date,sec_lastDate;
    Button btn_today;
    EditText score;

    AlertDialog.Builder alertbuilder;

    TextView tvUser,tv_userId,DOScore,tv_MainCat,tv_sub_cat;
    /**
     * 28/01/2020 modified
     */
   @Override
    public void onBackPressed() {
        //super.onBackPressed();
       Bundle bundle=new Bundle();
       bundle.putString("date",Curr_date);
       Intent i=new Intent(SelectedUserActivity.this, Score_From.class).putExtras(bundle);
       //used to or helps in display particular activity
       i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(i);
        //to avoid flickering or flipped screen or activity use finish() ,which is used to kill or complete the activity
        finish();

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_user);
        tvUser = findViewById(R.id.display_userName);
        tv_userId = findViewById(R.id.display_userId);
        tv_MainCat = findViewById(R.id.display_mainCat);
        tv_sub_cat = findViewById(R.id.selectedSubCat);

        alertbuilder = new AlertDialog.Builder(this);

        DOScore = findViewById(R.id.tv_DateOfScoreSubmit);

        DOScore.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);//to set edit text value to be only float
        score = findViewById(R.id.score_training);
        // ####imp.sHelper = new ScoreStorageAdapter(this); used to store score data into Score details table
        sHelper = new ScoreStorageAdapter(this);
        btn_today = findViewById(R.id.btn_scoretoday);
        //btn_prev = findViewById(R.id.btn_scorePrev);


        //to access or get data from user details
        helper = new MyDbAdapter(getApplicationContext());

        db = helper.myhelper.getReadableDatabase();
        //to access or get data from Score details
        db1 = sHelper.scoreStorageHelper.getReadableDatabase();


        /**
         * To display current Date 23/01/2020
         */
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Curr_date = sdf.format(date);
        DOScore.setText(Curr_date);
        //   DOScore.setText(Curr_date);


        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        if (bundle != null) {

            UserModel userModel = (UserModel) bundle.getSerializable("data");

            //display sub-category
            tv_sub_cat.setText(userModel.getUserName());

            tv_MainCat.setText(bundle.getString("main_category"));
            //DOScore.setText(bundle.getString("date"));
            UNAME = bundle.getString("z");

            //display UserName
            tvUser.setText(UNAME);
            cursor = helper.myhelper.getUID(UNAME, db);

            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.isAfterLast() == false) {
                    UID = cursor.getString(0);
                    //display userID
                    tv_userId.setText(UID);
                    savedID = UID;
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
            cursor_sec_last_login=sHelper.scoreStorageHelper.get_sec_last_login(savedID,db1);

            if (cursor_sec_last_login != null) {

                cursor_sec_last_login.moveToFirst();
                if (cursor_sec_last_login.isAfterLast() == false) {
                    sec_lastDate = cursor_sec_last_login.getString(0);

                }
                cursor_sec_last_login.moveToNext();
            }
            cursor_sec_last_login.close();

            cursor_days_not_entered=sHelper.scoreStorageHelper.get_no_days_score_NotEntered(savedID,last_date,db1);
            if (cursor_days_not_entered != null) {

                cursor_days_not_entered.moveToFirst();
                if (cursor_days_not_entered.isAfterLast() == false) {
                    x = cursor_days_not_entered.getInt(0);

                }
                cursor_days_not_entered.moveToNext();
            }
            cursor_days_not_entered.close();

            /*
            for all condition checking
             */


          if(last_date !=null && sec_lastDate == null && last_date!=sec_lastDate && last_date == Curr_date)
            {
              /*  Intent intent1 = new Intent(SelectedUserActivity.this, HomePage.class);
                startActivity(intent1);*/
                Intent i=new Intent(SelectedUserActivity.this, HomePage.class);
                //used to or helps in display particular activity
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(i);
                //to avoid flickering or flipped screen or activity use finish() ,which is used to kill or complete the activity
                finish();
                Toast.makeText(getApplicationContext(), "already Entered today Score!,Wait for Tomorrow......", Toast.LENGTH_SHORT).show();
            }
          else if(last_date != null && sec_lastDate!=null && !last_date.equals(Curr_date)) {

              if (x > 0 || x != 0) {

                  pending_day = getNextDate(last_date);
                  if (!pending_day.equals(Curr_date)) {

                                      DOScore.setText(pending_day);
                                      btn_today.setVisibility(View.VISIBLE);
                                      btn_today.setEnabled(true);
                                      //onBackPressed();
                  }


              }
          }
          else if(x==0) {
              if (last_date != null && sec_lastDate==null && last_date != sec_lastDate && last_date.equals(Curr_date)) {
                  //Intent intent1 = new Intent(SelectedUserActivity.this, HomePage.class);
                  //startActivity(intent1);
                  Intent i=new Intent(SelectedUserActivity.this, HomePage.class);
                  //used to or helps in display particular activity
                  i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                  startActivity(i);
                  //to avoid flickering or flipped screen or activity use finish() ,which is used to kill or complete the activity
                  finish();
                  Toast.makeText(getApplicationContext(), "already Entered today Score!,Wait for Tomorrow......", Toast.LENGTH_SHORT).show();
              }
          }

            if(last_date!=null && sec_lastDate!=null && !last_date.equals(sec_lastDate) && last_date.equals(Curr_date))
            {
               /* Intent intent1 = new Intent(SelectedUserActivity.this, HomePage.class);
                startActivity(intent1);*/
                Intent i=new Intent(SelectedUserActivity.this, HomePage.class);
                //used to or helps in display particular activity
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(i);
                //to avoid flickering or flipped screen or activity use finish() ,which is used to kill or complete the activity
                finish();
                Toast.makeText(getApplicationContext(), "already Entered today Score!,Wait for Tomorrow......", Toast.LENGTH_SHORT).show();
            }
        }
   }



   //add data
        public void addUser(View view) {
            //etName concatenating
            long true_id;
            final String name = tvUser.getText().toString();
            final String uid=tv_userId.getText().toString();
            final String mainCat = tv_MainCat.getText().toString();
            //to get selected item from training center list
            final String subCat = tv_sub_cat.getText().toString();

            final String dosubmit = DOScore.getText().toString();
            final String _score=score.getText().toString();
            //timestamp makes unique etName



            if (tvUser.toString().isEmpty() || tv_userId.toString().isEmpty() || tv_MainCat.toString().isEmpty() || tv_sub_cat.toString().isEmpty() || DOScore.toString().isEmpty() || score.toString().isEmpty()) {

                Message.message(getApplicationContext(), "Enter all Text Fields as well as select Proper Radio Fields !!");
            } else {


                 true_id = sHelper.insertData(uid,name,dosubmit,mainCat,subCat,_score );

                if(true_id<=0) {
                    Toast.makeText(getApplicationContext(),"Insertion Unsucessful",Toast.LENGTH_LONG).show();
                }
                else
                {


                    Message.message(getApplicationContext(), "Insertion Successful");
                    //onBackPressed();
                   if(_score!=null) {
                        /*Bundle bundle_score = new Bundle();
                        bundle_score.putString("score", _score);
                        bundle_score.putString("score_entry_date",dosubmit);
                        bundle_score.putString("user_id",uid);
                        Intent scoreIntent = new Intent(SelectedUserActivity.this, GraphDisplay.class).putExtras(bundle_score);
                        startActivity(scoreIntent);*/

                       Intent i=new Intent(SelectedUserActivity.this, HomePage.class);
                       //used to or helps in display particular activity
                       i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                       startActivity(i);
                       //to avoid flickering or flipped screen or activity use finish() ,which is used to kill or complete the activity
                       finish();
                    }

                    // new UploadFileAsync().execute("");
                    tv_sub_cat.setText("");
                    tv_MainCat.setText("");
                    tv_userId.setText("");
                    tvUser.setText("");
                    score.setText("");

                     }
            }
        }

        public void viewdata(View view)
        {
        String data = sHelper.getData();
        Message.message(this,data);
        }





/*         private String getNextDate(String inputDate){
        //inputDate = "2015-12-15"; // for example
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-mm-dd");
        try {

            Date date = format.parse(inputDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            c.add(Calendar.DATE, +1);
            inputDate = format.format(c.getTime());
            Log.d("asd", "selected date : "+inputDate);

            System.out.println(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            inputDate ="";
        }
        return inputDate;
        }*/

    private String getNextDate(String inputDate){
        //inputDate = "2015-12-15"; // for example
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
        try {

            Date date = format.parse(inputDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            c.add(Calendar.MONTH, 2);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.DATE, -1);




            Date lastDayOfMonth = c.getTime();
            int month=c.get(Calendar.MONTH)+1;
            int years=c.get(Calendar.YEAR);
            int day=c.get(Calendar.DAY_OF_MONTH);

           /* String s;
            if(month < 10)
            {
                s=years+"-0"+month+"-"+day;
            }
            else
            {
                s=years+"-"+month+"-"+day;
            }*/

            if(inputDate.equals(format.format(lastDayOfMonth)))
            {
                c.add(Calendar.MONTH, 1);
                c.set(Calendar.DAY_OF_MONTH, 0);
                c.add(Calendar.DATE, 1);
                Date FirstDayOfMonth = c.getTime();

               /* int new_day=c.get(Calendar.DAY_OF_MONTH);
                int new_month=  c.get(Calendar.MONTH)+1;
                int new_year=c.get(Calendar.YEAR);
                if(month < 10)
                {
                    inputDate=new_year+"-0"+new_month+"-"+new_day;
                }
                else
                {
                    inputDate=new_year+"-"+new_month+"-"+new_day;
                }*/
                inputDate=format.format(FirstDayOfMonth);
                return inputDate;

            }
            else {
                c.setTime(date);
                c.add(Calendar.DATE, +1);
                Date s1=c.getTime();
                int months=c.get(Calendar.MONTH)+1;
                int yearss=c.get(Calendar.YEAR);
                int days=c.get(Calendar.DAY_OF_MONTH);
                /*if(month < 10)
                {
                    inputDate=yearss+"-0"+months+"-"+days;
                }
                else
                {
                    inputDate=yearss+"-"+months+"-"+days;
                }*/
                inputDate=format.format(c.getTime());
                Log.d("asd", "selected date : " + inputDate);
                return inputDate;

            }

            // System.out.println(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            inputDate ="";
        }
        return inputDate;
    }

    }
