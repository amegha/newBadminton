package com.example.myapp_badminton;
/**
 * updated by_pooja_16/01/2020
 * used for importing database and other database related application
 */

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {


    private static final String DATABASE_NAME ="Category.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
