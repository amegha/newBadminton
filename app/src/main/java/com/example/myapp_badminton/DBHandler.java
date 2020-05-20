package com.example.myapp_badminton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class DBHandler extends SQLiteOpenHelper {
    private static final String TAG = DBHandler.class.getSimpleName();
    // Database Version
    private static final int DATABASE_VERSION = 1;
    //table names
    private static final String ACADEMY_INFO = "academy_info";

    //colomn names
    private static final String ACA_ID = "id";
    private static final String ACA_NAME = "academy_name";
    private static final String STATE = "state";
    private static final String CITY = "city";
    private static final String LOCATION = "location";

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

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Creating new table/ adding new Column / Drop table
    }

    public void storeLocationInfo(String info) {
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
            // TODO Auto-generated catch block
            return;
        }

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
}

