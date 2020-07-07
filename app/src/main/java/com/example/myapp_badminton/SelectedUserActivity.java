package com.example.myapp_badminton;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.os.Environment.getExternalStorageDirectory;

/*
 * coachdate and lastScoreEntryDate is in the form yyyy-mm-dd
 * */

public class SelectedUserActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AsyncResponse {

    public String UNAME, UID, pending_day, selected;
    public String cid, coach_name, coachdate, level, academy, playerName, playerId, imageString, aid;
    int x, y, diffInDays;
    Button date_selectionBtn;
    databaseConnectionAdapter helper;
    SQLiteDatabase db;

    String last_date, savedID, savedMID, ymdCurrDate, dmyCurrDate, sec_lastDate, MID;
    Button btn_today;
    EditText et_score;
    ImageView imageView;
    byte[] imageBytes;
    TextView tvUser, tv_userId, DOScore, tv_MainCat, tv_sub_cat;
    String last_sub_cat, last_score, last_academy, last_level, last_mainCat;
    String lastScoreEntryDate, type, Score;
    String ImagePlayer;
    AlertDialog alertDialog;
    //    Exception e;
//    ParseException e;
    private int mYear, mMonth, mDay;
    private String uiDate;
    private String mainCat, subCat, score;
    private TextView dialogScore, dialogMain, dialogSub;
    private String xml;
    private Intent intent;
    private Bundle bundle;
    private UserModel userModel;
    private Bitmap decodedImage;
    private Date date;
    private SimpleDateFormat ymdDateFormat, dmyDateFormat;
    private String ymdnextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Log.e("onCreate: ", "***from activity***" + this.getLocalClassName());
            setContentView(R.layout.activity_selected_user);
            initUI();
            date = new Date();
            getCurrentDate();

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

            intent = getIntent();
            bundle = intent.getExtras();
            if (bundle != null) {
                userModel = (UserModel) bundle.getSerializable("data");
                mainCat = bundle.getString("main_category");
                tv_sub_cat.setText(userModel.getUserName());
                tv_MainCat.setText(mainCat);

                type = bundle.getString("type");
                if (type.equalsIgnoreCase("coach")) {
                    //getting playername and id from bundle
                    getCoachPlayerBundleData();

                    imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                    decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    imageView.setImageBitmap(decodedImage);

                    tv_userId.setText(savedID);
                    tvUser.setText(UNAME);
                    if (coachdate.compareTo(ymdCurrDate) < 0) {
                        ymdnextDate = getNextDate(coachdate, 1);
                        selected = dmyDateFormat.format(ymdnextDate);
                        DOScore.setText(dmyDateFormat.format(ymdnextDate));
                    } else if (coachdate.compareTo(ymdCurrDate) == 0 || coachdate.equals("DATE_NOTSET")) {
                        selected = dmyDateFormat.format(dmyCurrDate);
                        DOScore.setText(dmyCurrDate);
                    } else if (coachdate.compareTo(ymdCurrDate) > 0) { // this condition should nor come at all..
                        DOScore.setError("Future Date not allowed");
                    }
                } else {
                    Intent playerIntent = getIntent();
                    Bundle playerBundle = playerIntent.getExtras();
                    getPlayerBundleData(playerBundle);

                    tv_userId.setText(playerId);
                    tvUser.setText(playerName);

                    ActivityTracker.writeActivityLogs(this.getLocalClassName(), playerId);
                    imageString = ImagePlayer;
                    imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    imageView.setImageBitmap(decodedImage);

                    if (lastScoreEntryDate.compareTo(ymdCurrDate) < 0) {
                        ymdnextDate = getNextDate(lastScoreEntryDate, 1);
                        selected = dmyDateFormat.format(ymdnextDate);
                        DOScore.setText(selected);
                    } else if (lastScoreEntryDate.compareTo(ymdCurrDate) == 0 || lastScoreEntryDate.equals("DATE_NOTSET")) {
                        selected = dmyDateFormat.format(ymdnextDate);
                        DOScore.setText(selected);
                    } else if (lastScoreEntryDate.compareTo(ymdCurrDate) > 0) {
                        DOScore.setError("Future Date not allowed");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPlayerBundleData(Bundle playerBundle) {
        playerName = playerBundle.getString("Pname");
        playerId = playerBundle.getString("Pid");
        type = playerBundle.getString("type");
        lastScoreEntryDate = playerBundle.getString("lastScoreDate");
        Score = playerBundle.getString("ScoreLast");
        ImagePlayer = playerBundle.getString("Image");
    }

    private void getCurrentDate() {
        ymdDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dmyDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        ymdCurrDate = ymdDateFormat.format(date);
        dmyCurrDate = dmyDateFormat.format(date);
    }

    private void getCoachPlayerBundleData() {
        UNAME = bundle.getString("PlayerName");
        savedID = bundle.getString("playerId");
        imageBytes = bundle.getByteArray("ImageBytes");
        cid = bundle.getString("coach_id");
        coach_name = bundle.getString("coachname");
        coachdate = bundle.getString("date");
        academy = bundle.getString("Academy");
        level = bundle.getString("level");
        aid = bundle.getString("aid");
    }

    private void initUI() {
        tvUser = findViewById(R.id.display_userName);
        tv_userId = findViewById(R.id.display_userId);
        tv_MainCat = findViewById(R.id.display_mainCat);
        tv_sub_cat = findViewById(R.id.selectedSubCat);
        imageView = findViewById(R.id.playerPic);
        DOScore = findViewById(R.id.tv_DateOfScoreSubmit);
        et_score = findViewById(R.id.score_training);

    }

    //add data
    public void addUser(View view) {
        try {
            score = (String) DOScore.getText();
            if (score.equals("") || score.matches(".*[a-zA-Z]+.*")) {
                et_score.setError("Enter the score");
            } else {
                if (type.equalsIgnoreCase("Coach")) {
                    //get difference between last date and selected date
                    if (coachdate.compareTo(selected) < 0) {
                        diffInDays = (int) ((ymdDateFormat.parse(coachdate).getTime() - ymdDateFormat.parse(selected).getTime()) / (1000 * 60 * 60 * 24));
                        if (diffInDays > 0) { //obviously it will be grater than zero
                            xml = "<player_root>";
                            for (int i = 0; i < diffInDays; i++) {
                                createXml("coach", i);
                            }
                            xml += "</player_root>";
                            writeToTxtFile(xml);
                        }
                    } else if ((coachdate.compareTo(selected) > 0) || (coachdate.compareTo(selected) == 0)) {
                        xml = "<player_root>";
                        createXml("coach", 0);
                        xml += "</player_root>";
                        writeToTxtFile(xml);

                    } else {
                        DOScore.setError("Select proper date");
                    }

                } else {
                    //get difference between last date and selected date
                    if (lastScoreEntryDate.compareTo(selected) < 0) {
                        diffInDays = (int) ((ymdDateFormat.parse(lastScoreEntryDate).getTime() - ymdDateFormat.parse(selected).getTime()) / (1000 * 60 * 60 * 24));
                        if (diffInDays > 0) { //obviously it will be grater than zero
                            xml = "<player_root>\n<total_score>"+diffInDays+"</total_score>";
                            for (int i = 0; i < diffInDays; i++) {
                                createXml("player", i);
                            }
                            xml += "</player_root>";
                            writeToTxtFile(xml);
                        }
                    } else if ((lastScoreEntryDate.compareTo(selected) > 0) || (lastScoreEntryDate.compareTo(selected) == 0)) {
                        xml = "<player_root>\n<total_score>\"+diffInDays+\"</total_score>\";";
                        createXml("player", 0);
                        xml += "</player_root>";
                        writeToTxtFile(xml);
                    } else {
                        DOScore.setError("Select proper date");
                    }
                } //end of player
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void createXml(String user, int i) {
        if (user.equalsIgnoreCase("coach")) {
            xml += "<playerScore" + i + ">\n" +
                    "<module>" + type + "</module>\n" +
                    "<pid>" + playerId + "</pid>\n" +
                    "<cid>" + cid + "</cid>\n" +
                    "<aid>" + aid + "</aid>\n" +
                    "<date>" + getNextDate(coachdate, i) + "</date>\n" +
                    "<main_cat>" + last_mainCat + "</main_cat>\n" +
                    "<sub_cat>" + last_sub_cat + "</sub_cat>\n" +
                    "<score>" + last_score + "</score>\n" +
                    "<level>" + level + "</level>\n" +
                    "</playerScore" + i + ">\n";
        } else {
            xml += "<playerScore>\n" +
                    "<module>" + type + "</module>\n" +
                    "<pid>" + playerId + "</pid>\n" +
                    "<date>" + date + "</date>\n" +
                    "<main_cat>" + last_mainCat + "</main_cat>\n" +
                    "<sub_cat>" + last_sub_cat + "</sub_cat>\n" +
                    "<score>" + last_score + "</score>\n" +
                    "<aid>1</aid>\n" +
                    "<level>2</level>\n" +
                    "</playerScore>";
        }

    }

    private void writeToTxtFile(String text) {
        try {
            String filename = "base.txt";
            File root = new File(getExternalStorageDirectory(), "Badminton");
            if (!root.exists()) {
                root.mkdirs();
            }
            File textFile = new File(root, filename);
            FileWriter writer = null;

            writer = new FileWriter(textFile);
            writer.append(text);
            writer.flush();
            writer.close();

            uploadScoreXml();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadScoreXml() {
        new WebService(this).execute(API.ServerAddress + API.UPLOAD_SCORE, "scoreUpload");
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



    @Override
    public void onTaskComplete(String result) {
        try {
            if (result.equals("0")) {
                Toast.makeText(this, "sync Successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SelectedUserActivity.this, HomePage.class);//.putExtras(bundle_score);
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
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        try {
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
}
