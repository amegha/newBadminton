package com.example.myapp_badminton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;

public class DBHandler extends SQLiteOpenHelper {
    private static final String TAG = DBHandler.class.getSimpleName();
    // Database Version
    private static final int DATABASE_VERSION = 1;
    //      table names
    private static final String ACADEMY_INFO = "academy_info";
    private static final String PLAYER_LOG = "player_log";

    //      column names
    private static final String ACA_ID = "id";
    private static final String ACA_NAME = "academy_name";
    private static final String STATE = "state";
    private static final String CITY = "city";
    private static final String LOCATION = "location";
    //    column names for player_log table
    private static final String LOG_ID = "id";
    private static final String LOG_STRING = "log_string";


    SQLiteDatabase db;
    ContentValues values;
    Cursor c;

    String[] selection;


    public DBHandler(final Context context) {
        super(new DatabaseContext(context), "SHUTTLE BADMINTON", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATION_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " + ACADEMY_INFO + "("
                + ACA_ID + " INTEGER PRIMARY KEY,"
                + ACA_NAME + " TEXT,"
                + STATE + " TEXT,"
                + CITY + " TEXT,"
                + LOCATION + " TEXT"
                + ")";
        db.execSQL(CREATE_LOCATION_INFO_TABLE);

        String CREATE_PLAYER_LOG = "CREATE TABLE IF NOT EXISTS " + PLAYER_LOG + "("
                + LOG_ID + " INTEGER PRIMARY KEY,"
                + LOG_STRING + " TEXT"
                + ")";

        db.execSQL(CREATE_PLAYER_LOG);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Creating new table/ adding new Column / Drop table
    }

    public void storeLocationInfo(String info) {
//        info="-Academy-,-State-,-city-,-location-"+info
        String[] locInfo = info.split(",");

        db = this.getWritableDatabase();
        values = new ContentValues();
        values.put(ACA_NAME, locInfo[0]);
        values.put(STATE, locInfo[1]);
        values.put(CITY, locInfo[2]);
        values.put(LOCATION, locInfo[3]);
        try {
            db.insertOrThrow(ACADEMY_INFO, null, values);
        } catch (SQLiteConstraintException e) {
            // TODO Auto-generated catch slice
            return;
        }

    }

    public void storeLog(String logString) {
        db = this.getWritableDatabase();
        values = new ContentValues();
        values.put(LOG_STRING, logString);

        try {
            db.insertOrThrow(PLAYER_LOG, null, values);
        } catch (SQLiteConstraintException e) {
           System.out.println(e.getMessage());
            // TODO Auto-generated catch slice
            return;
        }

    }

    public String getLogString() {
        int i = 0;
        String selection;
        db = this.getReadableDatabase();
        c = db.rawQuery("SELECT  " + LOG_STRING + " FROM " + PLAYER_LOG, null);
        c.moveToFirst();
        selection = c.getString(0);
        System.out.println("LogString " + selection);
        return selection;
    }


    public String[] getStates() {
        int i = 0;
        db = this.getReadableDatabase();
        c = db.rawQuery("SELECT distinct state FROM academy_info ", null);
        selection = new String[c.getCount()];
        c.moveToFirst();
        while (!c.isAfterLast()) {
            selection[i] = c.getString(0);
            i++;
            c.moveToNext();
        }
        System.out.println("get states " + Arrays.toString(selection));

        return selection;
    }

    public String[] getCities(String stateName) {
        int i = 0;
        db = this.getReadableDatabase();
        c = db.rawQuery("SELECT distinct city FROM academy_info where state = '" + stateName + "'", null);
        selection = new String[c.getCount()];
        c.moveToFirst();
        while (!c.isAfterLast()) {
            selection[i] = c.getString(0);
            i++;
            c.moveToNext();
        }
        System.out.println("get states " + Arrays.toString(selection));

        return selection;
    }

    public String[] getLocations(String cityName) {
        int i = 0;
        db = this.getReadableDatabase();
        c = db.rawQuery("SELECT distinct location FROM academy_info where city = '" + cityName + "'", null);
        selection = new String[c.getCount()];
        c.moveToFirst();
        while (!c.isAfterLast()) {
            selection[i] = c.getString(0);
            i++;
            c.moveToNext();
        }
        System.out.println("get states " + Arrays.toString(selection));

        return selection;
    }

    public String[] getAcademy(String cityName, String locationName) {
        int i = 0;
        db = this.getReadableDatabase();
        c = db.rawQuery("SELECT distinct academy_name FROM academy_info where city = '" + cityName + "' and location ='" + locationName + "'", null);
        selection = new String[c.getCount()];
        c.moveToFirst();
        while (!c.isAfterLast()) {
            selection[i] = c.getString(0);
            i++;
            c.moveToNext();
        }
        System.out.println("get states " + Arrays.toString(selection));

        return selection;
    }

    public String[] getAllData() {
        db = this.getReadableDatabase();
        c = db.rawQuery("SELECT * FROM academy_info where state='Karnataka' ", null);
        selection = new String[c.getColumnCount()];
        c.moveToFirst();
        for (int i = 0; i < c.getColumnCount(); i++) {
            selection[i] = c.getString(i);
        }
        System.out.println("get all data " + Arrays.toString(selection));

        return selection;
    }

    public void deleteLocations() {
        db = this.getWritableDatabase();
        db.execSQL("delete from academy_info");
    }
}

