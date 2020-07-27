package com.example.myapp_badminton;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.File;

class DatabaseContext extends ContextWrapper {

    private static final String DEBUG_CONTEXT = "DatabaseContext";
    Context mContext;

    public DatabaseContext(Context base) {
        super(base);
        this.mContext=base;
    }

    @Override
    public File getDatabasePath(String name) {
        File sdcard = Environment.getExternalStorageDirectory();
        if(mContext.getResources().getBoolean(R.bool.internalstorage)){
            sdcard= mContext.getFilesDir();
        }
        else  if (!mContext.getResources().getBoolean(R.bool.standalone)) {
            // sdcard/AWARE/ (shareable, does not delete when uninstalling)
            sdcard = new File(Environment.getExternalStoragePublicDirectory(name).toString());
            } else {
            if ("goldfish".equals(Build.HARDWARE)) {
                sdcard = mContext.getFilesDir();
            } else {
                // sdcard/Android/<app_package_name>/AWARE/ (not shareable, deletes when uninstalling package)
                sdcard = new File(ContextCompat.getExternalFilesDirs(mContext, null)[0] + "/"+name);
            }
        }
        if (!sdcard.exists()) {
            sdcard.mkdirs();
        }



        String dbfile = sdcard.getAbsolutePath() + File.separator + "databases" + File.separator + name;
        if (!dbfile.endsWith(".db")) {
            dbfile += ".db";
        }

        File result = new File(dbfile);

        if (!result.getParentFile().exists()) {
            result.getParentFile().mkdirs();
        }

        if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN)) {
            Log.w(DEBUG_CONTEXT, "getDatabasePath(" + name + ") = " + result.getAbsolutePath());
        }

        return result;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return openOrCreateDatabase(name, mode, factory);
    }

}