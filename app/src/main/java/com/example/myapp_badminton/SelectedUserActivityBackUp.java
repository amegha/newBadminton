package com.example.myapp_badminton;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.os.Environment.getExternalStorageDirectory;

public class SelectedUserActivityBackUp extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AsyncResponse {

    public String UNAME, UID, pending_day, selected;
    public String cid, coach_name, coachdate, level, academy, playerName, playerId, imageString, aid;
    int x, y, diffInDays;
    Button date_selectionBtn;
    databaseConnectionAdapter helper;
    SQLiteDatabase db;
    Cursor cursor, cursor_days_not_entered, cursor_lastDate, cursor_sec_last_login, cursorSkip;
    Cursor cursorSub, cursorLevel, cursorScore, cursorAcademy;
    String last_date, savedID, savedMID, Curr_date, sec_lastDate, MID;
    Button btn_today;
    EditText et_score;
    ImageView imageView;
    byte[] imageBytes;
    TextView tvUser, tv_userId, DOScore, tv_MainCat, tv_sub_cat;
    String last_sub_cat, last_score, last_academy, last_level, last_mainCat;
    String lastScoreEntryDate, type, Score;
    String ImagePlayer;
    AlertDialog alertDialog;
    private int mYear, mMonth, mDay;
    private String uiDate;
    private String mainCat, subCat, score;
    private TextView dialogScore, dialogMain, dialogSub;
    private String xml;

    /**
     * 28/01/2020 modified
     */
  /*  @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (type.equalsIgnoreCase("Player")) {
            Intent i = new Intent(SelectedUserActivity.this, ScoreFrom.class);
            //used to or helps in display particular activity
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            startActivity(i);
        } else {
            Intent i = new Intent(SelectedUserActivity.this, DisplayPlayer.class);
            //used to or helps in display particular activity
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
        }

        //to avoid flickering or flipped screen or activity use finish() ,which is used to kill or complete the activity
        finish();

    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Log.e("onCreate: ", "***from activity***" + this.getLocalClassName());
            setContentView(R.layout.activity_selected_user);
            tvUser = findViewById(R.id.display_userName);
            tv_userId = findViewById(R.id.display_userId);
            tv_MainCat = findViewById(R.id.display_mainCat);
            tv_sub_cat = findViewById(R.id.selectedSubCat);
            imageView = findViewById(R.id.playerPic);


            DOScore = findViewById(R.id.tv_DateOfScoreSubmit);

            // DOScore.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);//to set edit text value to be only float
            et_score = findViewById(R.id.score_training);
            et_score.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            // ####imp.sHelper = new ScoreStorageAdapter(this); used to store et_score data into Score details table

            btn_today = findViewById(R.id.btn_scoretoday);
            //  date_selectionBtn = findViewById(R.id.selection_date);


            //to access or get data from user details
            helper = new databaseConnectionAdapter(this);
            db = helper.allDataHelper.getReadableDatabase();


            //to select date from calander by coach
            //dob part
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new
                        StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            findViewById(R.id.tv_DateOfScoreSubmit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowDatePickerDialog();
                }
            });


            Intent intent = getIntent();
            final Bundle bundle = intent.getExtras();

            if (bundle != null) {

                UserModel userModel = (UserModel) bundle.getSerializable("data");

                //display sub-category
                tv_sub_cat.setText(userModel.getUserName());
                mainCat = bundle.getString("main_category");
                tv_MainCat.setText(mainCat);

                type = bundle.getString("type");
                if (type.equalsIgnoreCase("coach")) {
                    //getting playername and id from bundle
                    UNAME = bundle.getString("PlayerName");
                    savedID = bundle.getString("playerId");
                    imageBytes = bundle.getByteArray("ImageBytes");
                    cid = bundle.getString("coach_id");
                    coach_name = bundle.getString("coachname");
                    coachdate = bundle.getString("date");
                    academy = bundle.getString("Academy");
                    level = bundle.getString("level");
                    aid = bundle.getString("aid");
                    imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    imageView.setImageBitmap(decodedImage);

                    tv_userId.setText(savedID);
                    tvUser.setText(UNAME);

                    // to get last date of et_score sumission
                    // for student only cursor_lastDate = helper.allDataHelper.get_last_login(savedID, db);
                   /* cursor_lastDate = helper.allDataHelper.getCoachLastScoreEntry(cid, savedID, db);

                    if (cursor_lastDate != null) {
                        cursor_lastDate.moveToFirst();
                        if (cursor_lastDate.isAfterLast() == false) {
                            last_date = cursor_lastDate.getString(0);

                        }
                        cursor_lastDate.moveToNext();
                    }
                    cursor_lastDate.close();
    */

                    if (coachdate != Curr_date) {
                        String properDate = getNextDate(coachdate, 1);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd-MM-yyyy");

                        try {
                            Date strDate = dateFormat.parse(properDate);
                            uiDate = dateFormatOutput.format(strDate);
                            DOScore.setText(uiDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        //try to display alert box
                    }
                } else {

                    //
                    //player Part
                    Intent playerIntent = getIntent();
                    Bundle playerBundle = playerIntent.getExtras();
                    playerName = playerBundle.getString("Pname");
                    playerId = playerBundle.getString("Pid");
                    type = playerBundle.getString("type");
                    lastScoreEntryDate = playerBundle.getString("lastScoreDate");
                    Score = playerBundle.getString("ScoreLast");
                    ImagePlayer = playerBundle.getString("Image");
                    tv_userId.setText(playerId);
                    tvUser.setText(playerName);
                    ActivityTracker.writeActivityLogs(this.getLocalClassName(), playerId,getApplicationContext());

                    //getting image from db

                    imageString = ImagePlayer;

                    // imageBytes=helper.allDataHelper.retreiveImageFromDBPlayer(playerId,db);

                    //imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    imageView.setImageBitmap(decodedImage);
                    if (lastScoreEntryDate.equals("DATE_NOTSET")) {
                        Date date = new Date();
                        DOScore.setText(new SimpleDateFormat("dd-MM-yyyy").format(date));
                    } else if (lastScoreEntryDate != Curr_date) {

                        /*String properDate=getNextDate(lastScoreEntryDate,1);
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String strDate = dateFormat.format(properDate);
                        DOScore.setText(strDate);
                        //try to display alert box*/
                        String properDate = getNextDate(lastScoreEntryDate, 1);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd-MM-yyyy");

                        try {
                            Date strDate = dateFormat.parse(properDate);
                            DOScore.setText(dateFormatOutput.format(strDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    } else {

                        String properDate = lastScoreEntryDate;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd-MM-yyyy");

                        try {
                            Date strDate = dateFormat.parse(properDate);
                            DOScore.setText(dateFormatOutput.format(strDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //add data
    public void addUser(View view) {
        try {
            //name concatenating
            long true_id;
            final String name = tvUser.getText().toString();
            final String uid = tv_userId.getText().toString();
            final String mainCat = tv_MainCat.getText().toString();
            //to get selected item from training center list
            final String subCat = tv_sub_cat.getText().toString();
            // final String mid=savedMID;


            final String _score = et_score.getText().toString();
            this.mainCat = mainCat;
            this.subCat = subCat;
            this.score = _score;
            String properDate = DOScore.getText().toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd-MM-yyyy");

            try {
                Date strDate = dateFormatOutput.parse(properDate);
                selected = dateFormat.format(strDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            final String dosubmit = selected;

            // String selected=DOScore.getText().toString();
            //get Current date
            Date date = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Curr_date = sdf1.format(date);

            if (selected.equals("Select Date") || selected.equals(null) || selected.equals(" ")) {
                DOScore.setText(new SimpleDateFormat("dd-MM-yyyy").format(date));
                if ((Curr_date.compareTo(selected) < 0) || (lastScoreEntryDate.compareTo(selected) > 0)) {
                    DOScore.setError("Enter Proper date!!");
                }
                if (_score.equals("") || _score.matches(".*[a-zA-Z]+.*")) {
                    et_score.setError("Enter the score");
                }
            } else {
                //Skip module

                //coach Part
                if (type.equalsIgnoreCase("Coach")) {
                    //get difference between last date and selected date
                    try {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(selected);
                        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(coachdate);
                        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(Curr_date);
                        diffInDays = (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
                        if ((date1.compareTo(date2) > 0)) {

                            if ((date1.compareTo(date3) < 0) || (date1.equals(date3))) {
                                if (diffInDays > 0) {
                                    for (int i = 1; i <= diffInDays; i++) {

                                        db = helper.allDataHelper.getWritableDatabase();


                                        pending_day = getNextDate(coachdate, i);
                                        if (!pending_day.equals(selected)) {
                                            String[] data = helper.allDataHelper.getAllData(coachdate, db);
                                            last_mainCat = data[0];
                                            last_sub_cat = data[1];
                                            last_score = data[2];
                                            last_academy = data[3];
                                            last_level = data[4];


                                            //pending_day = getNextDate(pending_day);
                                            helper.allDataHelper.insertCoachScore(cid, coach_name, savedID, tvUser.getText().toString(), pending_day, last_mainCat, last_sub_cat, last_score, last_academy, last_level);
                                            formXMlCoach(cid, savedID, pending_day, last_mainCat, last_sub_cat, last_score, level, aid);
                                        }

                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please Select Appropriate Date!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Select Appropriate Date!", Toast.LENGTH_LONG).show();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else//Player Part
                    try {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(selected);
                        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(lastScoreEntryDate);
                        Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(Curr_date);
                        diffInDays = (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
                        if ((date1.compareTo(date2) > 0)) {

                            if ((date1.compareTo(date3) < 0) || (date1.equals(date3))) {
                                if (diffInDays > 0) {
                                    for (int i = 1; i <= diffInDays; i++) {

                                        db = helper.allDataHelper.getWritableDatabase();

                                        pending_day = getNextDate(lastScoreEntryDate, i);
                                        if (!pending_day.equals(selected)) {
                                            String[] data = helper.allDataHelper.getAllDataPlayer(lastScoreEntryDate, db);
                                            last_mainCat = data[0];
                                            last_sub_cat = data[1];
                                            last_score = data[2];


                                            //pending_day = getNextDate(pending_day);
                                            helper.allDataHelper.insertData(playerId, playerName, pending_day, last_mainCat, last_sub_cat, last_score);
                                            formXMlPlayer(playerId, pending_day, last_mainCat, last_sub_cat, last_score);
                                        }

                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please Select Appropriate Date!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Select Appropriate Date!", Toast.LENGTH_LONG).show();
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                //normal scoreEntry saved


                if (tvUser.toString().isEmpty() || tv_userId.toString().isEmpty() || tv_MainCat.toString().isEmpty() || tv_sub_cat.toString().isEmpty() || DOScore.toString().isEmpty() || et_score.toString().isEmpty()) {

                    Message.message(getApplicationContext(), "Enter all Text Fields as well as select Proper Radio Fields !!");
                } else {
                    if (type.equalsIgnoreCase("player")) {
                        true_id = helper.allDataHelper.insertData(uid, name, dosubmit, mainCat, subCat, _score);
                        formXMlPlayer(uid, dosubmit, mainCat, subCat, _score);
                    } else {
                        true_id = helper.allDataHelper.insertCoachScore(cid, coach_name, uid, name, dosubmit, mainCat, subCat, _score, academy, level);
                        formXMlCoach(cid, uid, dosubmit, mainCat, subCat, _score, level, aid);
                    }

                    if (true_id <= 0) {
                        Toast.makeText(getApplicationContext(), "Insertion Unsucessful", Toast.LENGTH_LONG).show();
                    } else {
//                        Message.message(getApplicationContext(), "Insertion Successful");
                        //onBackPressed();
                        if (_score != null) {
                            /*Intent i = new Intent(SelectedUserActivity.this, HomePage.class);//.putExtras(bundle_score);
                            i.setFlags((Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) | (Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            startActivity(i);
                            finish();*/
                        }
                        // new UploadFileAsync().execute("");
                       /* tv_sub_cat.setText("");
                        tv_MainCat.setText("");
                        tv_userId.setText("");
                        tvUser.setText("");
                        et_score.setText("");*/


                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private String getNextDate(String inputDate, int i) {
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

            if (inputDate.equals(format.format(lastDayOfMonth))) {
                c.add(Calendar.MONTH, 1);
                c.set(Calendar.DAY_OF_MONTH, 0);
                c.add(Calendar.DATE, 1);
                Date FirstDayOfMonth = c.getTime();

                inputDate = format.format(FirstDayOfMonth);
                return inputDate;

            } else {
                c.setTime(date);
                c.add(Calendar.DATE, +i);
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


    //date calculation as well as age calculation
    private void ShowDatePickerDialog() {
        try {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        try {
            // month = month + 1;
            int age_cal;

            Calendar c = Calendar.getInstance();
            int endYear = c.get(Calendar.YEAR);
            int endMonth = c.get(Calendar.MONTH);
            endMonth++;
            int endDay = c.get(Calendar.DAY_OF_MONTH);


            c.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            // String date = sdf.format(" " + year + "-" + month + "-" + dayOfMonth + "\n");
            String date = sdf.format(c.getTime());
            DOScore.setText(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*  public void SkipDate(View view){
          String selected=DOScore.getText().toString();
          //get Current date
          Date date = new Date();
          SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
          currDate = sdf1.format(date);


          if(selected.equals("Select Date") || selected.equals(null) || selected.equals(" ")){
              Toast.makeText(getApplicationContext(),"Please Click on Select Date Field to select date!",Toast.LENGTH_LONG).show();
          }
          //coach Part
          if(type.equalsIgnoreCase("Coach")) {
              //get difference between last date and selected date
              try {
                  Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(selected);
                  Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(coachdate);
                  Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(currDate);
                  diffInDays = (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
                  if ((date1.compareTo(date2) > 0)) {

                      if ((date1.compareTo(date3) < 0) || (date1.equals(date3))) {
                          if (diffInDays > 0) {
                              for (int i = 1; i <= diffInDays; i++) {

                                  db = helper.allDataHelper.getWritableDatabase();


                          pending_day = getNextDate(coachdate,i);
                          if (!pending_day.equals(selected)) {
                              String[] data = helper.allDataHelper.getAllData(coachdate,db);
                              last_mainCat = data[0];
                              last_sub_cat = data[1];
                              last_score = data[2];
                              last_academy = data[3];
                              last_level = data[4];


                              //pending_day = getNextDate(pending_day);
                              helper.allDataHelper.insertCoachScore(cid, coach_name, savedID, tvUser.getText().toString(), pending_day, last_mainCat, last_sub_cat, last_score, last_academy, last_level);
                              formXMlCoach(cid,savedID,pending_day,last_mainCat,last_sub_cat,last_score,level);
                          }

                              }
                          }
                      }
                      else {
                          Toast.makeText(getApplicationContext(),"Please Select Appropriate Date!",Toast.LENGTH_LONG).show();
                      }
                  }
                  else {
                      Toast.makeText(getApplicationContext(),"Please Select Appropriate Date!",Toast.LENGTH_LONG).show();
                  }

              } catch (ParseException e) {
                  e.printStackTrace();
              }
          }
          else//Player Part
              try {
                  Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(selected);
                  Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(lastScoreEntryDate);
                  Date date3=new SimpleDateFormat("yyyy-MM-dd").parse(currDate);
                  diffInDays = (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
                  if((date1.compareTo(date2)>0)){

                      if((date1.compareTo(date3)<0) || (date1.equals(date3))){
                          if(diffInDays>0){
                              for(int i=1;i<=diffInDays;i++){

                                  db=helper.allDataHelper.getWritableDatabase();

                          pending_day=getNextDate(lastScoreEntryDate,i);
                          if (!pending_day.equals(selected)) {
                              String[] data = helper.allDataHelper.getAllDataPlayer(lastScoreEntryDate,db);
                              last_mainCat = data[0];
                              last_sub_cat = data[1];
                              last_score = data[2];


                              //pending_day = getNextDate(pending_day);
                              helper.allDataHelper.insertData(playerId, playerName, pending_day, last_mainCat, last_sub_cat, last_score);
                              formXMlPlayer(playerId,pending_day,last_mainCat,last_sub_cat,last_score);
                          }

                              }
                          }
                      }
                      else {
                          Toast.makeText(getApplicationContext(),"Please Select Appropriate Date!",Toast.LENGTH_LONG).show();
                      }
                  }
                  else {
                      Toast.makeText(getApplicationContext(),"Please Select Appropriate Date!",Toast.LENGTH_LONG).show();
                  }


              }
              catch (ParseException e){
                  e.printStackTrace();
              }
      }
  */
    private void writeToTxtFile(String text) {
        try {
            String filename = "base.txt";
            File root = new File(getExternalStorageDirectory(), "Badminton");
            if (!root.exists()) {
                root.mkdirs();
            }
            File textFile = new File(root, filename);
            FileWriter writer = null;
            try {
                writer = new FileWriter(textFile);
                writer.append(text);
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void formXMlPlayer(String playerId, String date, String last_mainCat, String last_sub_cat, String last_score) {
        try {
            Log.e("getdata", "entered form xml: ");
//<playerScore>
//<module>player</module>
//<pid>1089</pid>
//<date>2020-05-31</date>
//<main_cat>Fittness</main_cat>
//<sub_cat>Agility</sub_cat>
//<score>70</score>
//<level>2</level>
//</playerScore>
            String xml = "<playerScore>\n" +
                    "<module>" + type + "</module>\n" +
                    "<pid>" + playerId + "</pid>\n" +
                    "<date>" + date + "</date>\n" +
                    "<main_cat>" + last_mainCat + "</main_cat>\n" +
                    "<sub_cat>" + last_sub_cat + "</sub_cat>\n" +
                    "<score>" + last_score + "</score>\n" +
                    "<aid>1</aid>\n" +
                    "<level>2</level>\n" +
                    "</playerScore>";
            try {
                writeToTxtFile(xml);
                Log.e("getdata", "dataXml: " + xml);
//                confirmRequest(xml);
                sendRequest(xml);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void formXMlCoach(String Cid, String playerId, String date, String last_mainCat, String last_sub_cat, String last_score, String level, String aid) {
        try {
            Log.e("getdata", "entered form xml: ");
//<playerScore>
//    <module>coach</module>
//    <pid>1099</pid>
//    <cid>4</cid>
//    <aid>1</aid>
//    <date>2020-05-31</date>
//    <main_cat>Fittness</main_cat>
//    <sub_cat>Agility</sub_cat>
//    <score>70</score>
//    <level>4</level>
//</playerScore>
            xml = "<playerScore>\n" +
                    "<module>" + type + "</module>\n" +
                    "<pid>" + playerId + "</pid>\n" +
                    "<cid>" + Cid + "</cid>\n" +
                    "<aid>" + aid + "</aid>\n" +
                    "<date>" + date + "</date>\n" +
                    "<main_cat>" + last_mainCat + "</main_cat>\n" +
                    "<sub_cat>" + last_sub_cat + "</sub_cat>\n" +
                    "<score>" + last_score + "</score>" +
                    "<level>" + level + "</level>" +
                    "</playerScore>";
            try {
                writeToTxtFile(xml);
                Log.e("getdata", "dataXml: " + xml);
                confirmRequest(xml);
//                sendRequest(xml);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void confirmRequest(String xml) {
        try {
            LayoutInflater li = LayoutInflater.from(this);
            View confirmDialog = li.inflate(R.layout.confirm_xml, null);
            dialogMain = confirmDialog.findViewById(R.id.tv_confirmMainCat);
            dialogSub = confirmDialog.findViewById(R.id.tvConfirmmSubCat);
            dialogScore = confirmDialog.findViewById(R.id.tvConfirmScore);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setView(confirmDialog);
            alertDialog = alert.create();
            alertDialog.show();

            dialogMain.setText(mainCat);
            dialogSub.setText(subCat);
            dialogScore.setText(score);
        } catch (Exception e) {
            e.printStackTrace();
        }


       /* new AlertDialog.Builder(this)
                .setTitle("Confirm!!")
                .setMessage("Date:" + uiDate + "\n" +
                        "Main Category:" + mainCat + "\n" +
                        "Sub Category:" + subCat + "\n" +
                        "Score:" + score + "\n")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendRequest(xml);
                    }
                }).show();*/
//                .setPositiveButton("ok", (dialog, which) -> sendRequest(xml)).show();
    }


    private void sendRequest(String xml) {
        try {
            if (isConnected()) {
                new WebService(this).execute(API.ServerAddress + "" + "upload_score.php", xml);
            } else {
                Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskComplete(String result) {
        try {
            if (result.equals("0")) {
                Toast.makeText(this, "sync Successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, HomePage.class);//.putExtras(bundle_score);
                i.setFlags((Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) | (Intent.FLAG_ACTIVITY_CLEAR_TOP));
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "Data failed!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isConnected() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void sendXMl(View view) {
        sendRequest(xml);
    }
}
