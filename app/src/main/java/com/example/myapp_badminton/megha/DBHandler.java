package com.example.myapp_badminton.megha;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {
    private static final String TAG = DBHandler.class.getSimpleName();
    // Database Version
    private static final int NEW_DATABASE_VERSION = 2;
    private static final int OLD_DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Badminton_Database";    // Database Name
    private static final String USER_DETAILS = "user_details";   // Table Name
    private static final String ANSWERS = "answers";   // Table Name
    private static final String CORRECT_ANSWERS = "CORRECT_ANSWER";   // Table Name

    private static final String UID = "id";     // Column I (Primary Key)
    private static final String FULL_NAME = "Name";    //Column II
    private static final String EMAIL = "Email";    //Column V
    private static final String TRAINING_CENTERS = "Training_centers";    // Column VI
    private static final String STATE_RANK = "STATE_RANK";// Column VII
    private static final String NATIONAL_RANK = "NATIONAL_RANK";//Column VIII
    private static final String IMAGE_DATA = "IMAGE";//IX
    private static final String AGE = "AGE";//X
    private static final String GENDER = "GENDER";
    private static final String EDUCATION = "EDUCATION";
    private static final String PLAYER_TYPE = "PLAYER_TYPE";
    private static final String DOB = "DATE_OF_BIRTH";
    private static final String TIMESTAMP = "TIME_STAMP";

    private static final String SHOT_TYPE = "SHOT_TYPE";
    private static final String VIDEO_ID = "VIDEO_ID";
    private static final String VIDEO_NAME = "VIDEO_NAME";
    private static final String SCORE = "SCORE";
    private static final String TIME_TAKEN_TO_ANSWER = "TIME_TAKEN_TO_ANSWER";
    private static final String MAX_TIME = "SERVER_TIME";
    private static final String PAUSES = "PAUSES";
    private static final String AID = "AID";
    private static final String PID = "PID";
    private static final String SHOT_LOCATION = "SHOT_LOCTION";

    //    FareDetails fd = new FareDetails();`
    //variables
    String xmldata;
    String vBrand = "vehicle";
    String vModel = "model";
    String isPass = "0";
    String[] selection;
    Object[] object;
    StringBuilder stringBuilder;


    String s_selelction = "";
    Cursor c, c2;
    int timeSum, scoreSum;
    int[] arrayIselection, arrTotal, arrOccupied, arrAvailable;
    SQLiteDatabase db;
    double totalHrs, totalFare;
    ContentValues values;
    int[] pause;
//    private int scoreSum,timeSum;


    public DBHandler(final Context context) {
        super(new DatabaseContext(context), "badminton", null, NEW_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_DETAILS_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_DETAILS + "("
                + UID + " INTEGER PRIMARY KEY,"
                + FULL_NAME + " TEXT,"
                + EMAIL + " TEXT,"
                + TRAINING_CENTERS + " TEXT,"
                + STATE_RANK + " INTEGER,"
                + NATIONAL_RANK + " INTEGER,"
                + IMAGE_DATA + " TEXT,"
                + GENDER + " TEXT,"
                + EDUCATION + " TEXT,"
                + PLAYER_TYPE + " TEXT,"
                + AGE + " TEXT,"
                + DOB + " TEXT,"
                + TIMESTAMP + " TEXT "
                + ")";
        db.execSQL(CREATE_USER_DETAILS_TABLE);

        String CREATE_ANSWERS = "CREATE TABLE IF NOT EXISTS " + ANSWERS + "("
                + AID + " INTEGER PRIMARY KEY,"
                + SHOT_TYPE + " TEXT,"
                + SHOT_LOCATION + " TEXT,"
                + TIME_TAKEN_TO_ANSWER + " NUMBER,"
                + SCORE + " NUMBER"
                + ")";
        Log.e(TAG, "create table" + ANSWERS);
        db.execSQL(CREATE_ANSWERS);

        String CREATE_CORRECT_ANSWERS = "CREATE TABLE IF NOT EXISTS " + CORRECT_ANSWERS + "("
                + AID + " INTEGER PRIMARY KEY,"
                + VIDEO_ID + " NUMBER,"
                + SHOT_LOCATION + " TEXT,"
                + SHOT_TYPE + " TEXT,"
                + PAUSES + " NUMBER,"
                + MAX_TIME + " NUMBER,"
                + VIDEO_NAME + " TEXT"
                + ")";
        Log.e(TAG, "create table" + CREATE_CORRECT_ANSWERS);
        db.execSQL(CREATE_CORRECT_ANSWERS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      /*  if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE ANSWER ADD COLUMN " + MAX_TIME + "INTGER DEFAULT 0");
        }*/

        // Creating new table/ adding new Column / Drop table
    }


//    public boolean deleteRecord(String rcptNo) {
//        db = this.getWritableDatabase();
//        Log.e("delete", "inside delete");
//        db.delete(PARK_IN, RECEIPT_NUM + "=" + rcptNo, null);
//        Log.e("db", "do nothing");
//        return true;
//    }

//    public long getFailedDataCount(String action) {
//        long numRows;
//        db = this.getReadableDatabase();
//        if (action.equals("offlineParkOut")) {
//            numRows = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM PARK_IN WHERE FLAG=2 AND loc_id=" + LoginActivity.locId, null);
//            Log.i("count", " " + Long.toString(numRows));
//        } else {
//            numRows = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM PARK_IN WHERE FLAG=0 AND loc_id=" + LoginActivity.locId, null);
//            Log.i("count", " " + Long.toString(numRows));
//        }
//        Log.e("db", "do nothing");
//        return numRows;
//
//    }

    public void storeDataOffline(String name, String mailId, String trainingCenter, int stateRank, int nationalRank, String radio_gender, String radio_education, String radio_playerType, String age, String dob) {
        db = this.getWritableDatabase();
        values = new ContentValues();
        values.put(FULL_NAME, name);
        values.put(EMAIL, mailId);
        values.put(TRAINING_CENTERS, trainingCenter);
        values.put(STATE_RANK, stateRank);
        values.put(NATIONAL_RANK, nationalRank);
        values.put(GENDER, radio_gender);
        values.put(EDUCATION, radio_education);
        values.put(PLAYER_TYPE, radio_playerType);
        values.put(AGE, age);
        values.put(DOB, dob);
        db.insert(USER_DETAILS, null, values);

    }

    public void saveAnswers(String shotLoc, String shotType, long elapsedTime, int score) {
        db = this.getWritableDatabase();
        values = new ContentValues();
        values.put(SHOT_LOCATION, shotLoc);
        values.put(SHOT_TYPE, shotType);
        values.put(TIME_TAKEN_TO_ANSWER, elapsedTime);
        values.put(SCORE, score);
        Log.e(TAG, "ANSWER SAVED!!" + db.insert(ANSWERS, null, values));
    }

    public void storeCorrectAnswers(String vid, String shotLoc, String shotType, long endtime, int maxTime, String videoName) {
        db = this.getWritableDatabase();
        values = new ContentValues();
        values.put(SHOT_LOCATION, shotLoc);
        values.put(SHOT_TYPE, shotType);
        values.put(PAUSES, endtime);
        values.put(MAX_TIME, maxTime);
        values.put(VIDEO_ID, vid);
        values.put(VIDEO_NAME, videoName);

        Log.e(TAG, "pauses!!" + db.insert(CORRECT_ANSWERS, null, values));
    }

    public boolean isDataEmpty() {
            db = this.getReadableDatabase();
        c = db.rawQuery("SELECT * FROM CORRECT_ANSWER ", null);
        if (c.getCount() > 0) {
            Log.e("db", "correct answers table not empty " + c.getCount());
            return false;
        } else {
            Log.e("db", "correct answers table is empty " + c.getCount());
            return true;
        }

    }

    public int[] getVideoPause(int position) {
        db = this.getReadableDatabase();
//        String query="SELECT PAUSES  FROM CORRECT_ANSWERS WHERE PAUSES IN("+position
        c = db.rawQuery("SELECT PAUSES  FROM CORRECT_ANSWER WHERE AID IN(" + (position + 1) + "," + (position + 2) + ")", null); //pid starts from 1, but listItem starts from 0.
        if (c.getCount() > 0) {
            c.moveToFirst();
            pause = new int[c.getCount()];
            pause[0] = c.getInt(0);
            if (c.moveToNext())
                pause[1] = c.getInt(0);
//            Log.e("db", "the end pause 2" + c.getInt(1));
        }

        return pause;
    }

    public int getPlayerTotalTime() {
        long numRows;
        db = this.getReadableDatabase();
        c = db.rawQuery("SELECT sum(TIME_TAKEN_TO_ANSWER) FROM ANSWERS", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
        }

        return c.getInt(0);
    }

    public int getServerTotalTime() {
        long numRows;
        db = this.getReadableDatabase();
        c = db.rawQuery("SELECT sum(SERVER_TIME) FROM CORRECT_ANSWER", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
        }
        return c.getInt(0);
    }

    public String getAllData() {
        db = this.getReadableDatabase();
        c = db.rawQuery("SELECT * FROM CORRECT_ANSWER", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
//            selection = new String[c.getColumnCount()*c.getCount()];
            for (int j = 0; j < c.getCount(); j++) {
                c.moveToPosition(j);
                for (int i = 1; i < c.getColumnCount() - 1; i++) {
                    if (i > 3) {
                        s_selelction = s_selelction + String.valueOf(c.getInt(i)); //gets the integer value which starts from 3rd column.
                    } else {
                        s_selelction = s_selelction + c.getString(i);// first 2 column are string
                    }
                    s_selelction = s_selelction + ":";
                }
                s_selelction = s_selelction + ",";
            }

        } else
            return null;
        return s_selelction = s_selelction + c.getString(c.getColumnCount() - 1); // gets video name

    }


    public int getGameSCore() {
        db = this.getReadableDatabase();
        c = db.rawQuery("SELECT sum(SCORE) FROM ANSWERS ", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
        }
        return c.getInt(0);
    }

    @SuppressLint("DefaultLocale")
    public String getPlayerAnswers() {
        stringBuilder = new StringBuilder();
        db = this.getReadableDatabase();
        object = new Object[c.getColumnCount()];

        c = db.rawQuery("SELECT * FROM ANSWERS", null);
        if (c.getCount() > 0) {
            selection = new String[c.getColumnCount() + 1];
            stringBuilder.append(String.format("<player_record>\n"));

            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                for (int j = 0; j < c.getColumnCount(); j++) {
                    switch (c.getType(j)) {
                        case 1:
                            selection[j] = String.valueOf(c.getInt(j));
                            break;
                        case 3:
                            selection[j] = c.getString(j);
                            break;
                    }
                }
                c2 = db.rawQuery("SELECT VIDEO_ID FROM CORRECT_ANSWER", null);
                c2.moveToFirst();
                selection[0] = c2.getString(0);
                stringBuilder.append(String.format("<selection>\n" +
                        "<shot_type>%s</shot_type>\n" +
                        "<shot_location>%s</shot_location>\n" +
                        "<time>%s</time>\n" +
                        "<score>%s</score>\n" +
                        "<vid>%s</vid>" +
                        "</selection>\n\n", selection[1], selection[2], selection[3], selection[4], selection[0]));
                timeSum = (int) calcSum(selection[3]);
                scoreSum = (int) calcScoreSum(selection[4]);
            }
            stringBuilder.append(String.format("<total_time>%s</total_time>\n" +
                    "<total_score>%s</total_score>\n" +
                    "<pid>%s</pid>\n" +
                    "</player_record>\n\n", timeSum, scoreSum, "047"));

            xmldata = stringBuilder.toString();
            Log.e("db handler", "xml data \n" + xmldata);
        }
        return xmldata;
    }

    private Object calcSum(String param) {
        return timeSum + Integer.parseInt(param);
    }

    private Object calcScoreSum(String param) {
        return scoreSum + Integer.parseInt(param);
    }

    public void deletePlayeraAnswerData() {
        //DELETE BOTH THE TABLE CONTENT ON SUCCESS SYNCHRONIZATOIN
        db = this.getWritableDatabase();
        db.execSQL("delete from answers");
        db.execSQL("delete from CORRECT_ANSWER");
    }
}


