package com.example.myapp_badminton;

/**
 * updated by Pooja_16/01/2020
 * this file is used to fetch data to the training category
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    Cursor c=null;
    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }


    public String[] getOnCourtSkill(int index) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM On_court_skill_Complete where Main_ID="+index, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        String[] itemsArray = new String[list.size()];
        itemsArray = list.toArray(itemsArray);
        return itemsArray;
    }

    public String[] getGrip() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Grip ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        String[] itemsArray = new String[list.size()];
        itemsArray = list.toArray(itemsArray);
        return itemsArray;
    }



    public String[] getFitness() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Fitness ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        String[] itemsArray = new String[list.size()];
        itemsArray = list.toArray(itemsArray);
        return itemsArray;
    }
    //to get all cities 21-04-2020
    public String[] getCities() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT City_name FROM City ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        String[] itemsArray = new String[list.size()];
        itemsArray = list.toArray(itemsArray);
        return itemsArray;
    }


    //to get academy according to city selected 21-04-2020
    public String[] getAcademy() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Academy_Name FROM City_Academy ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        String[] itemsArray = new String[list.size()];
        itemsArray = list.toArray(itemsArray);
        return itemsArray;
    }
    //to get playing levels to select 21-04-2020
    public String[] getLevels() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Level_Selected  FROM Level", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        String[] itemsArray = new String[list.size()];
        itemsArray = list.toArray(itemsArray);
        return itemsArray;
    }


    //to get playing levels to select 21-04-2020
    public String[] getPlayers(String Academy_name ) {
        List<String> list = new ArrayList<>();
        //SELECT PlayerName FROM PlayerDetaiils where AcademyName="ABC academy";
        Cursor cursor = database.rawQuery("SELECT PlayerName  FROM PlayerDetails where AcademyName="+Academy_name, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        String[] itemsArray = new String[list.size()];
        itemsArray = list.toArray(itemsArray);
        return itemsArray;
    }

}
