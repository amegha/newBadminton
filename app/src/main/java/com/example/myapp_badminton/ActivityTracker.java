package com.example.myapp_badminton;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityTracker {
    public static void writeActivityLogs(String string, String pid, Context mContext) {
       /* DBHandler dbHandler=new DBHandler(mContext);
        dbHandler.storeLog(getDate() + "_" + string + "_" + pid + "/");*/

        try {
            String fileName = "badmintonLogs.txt";
            File root = new File(Environment.getExternalStorageDirectory(), "Badminton");

            //
            String name="Badminton";
            File sdcard; /*= Environment.getExternalStorageDirectory();*/
            if(mContext.getResources().getBoolean(R.bool.internalstorage)){
                sdcard= mContext.getFilesDir();
            }
            else  if (!mContext.getResources().getBoolean(R.bool.standalone)) {
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
            File textFile = new File(sdcard, fileName);


            /*String ssdcard = sdcard.getAbsolutePath() + File.separator + name + File.separator +fileName;
            if (!ssdcard.endsWith(".txt")) {
                ssdcard += ".txt";
            }
            File textFile = new File(ssdcard);*/


            Log.e("Activity Tracker ","text file name :"+textFile.toString());
//            File textFile = new File(sdcard, fileName);

            BufferedWriter out = new BufferedWriter(
                    new FileWriter(textFile, true));
            out.write(getDate() + "_" + string + "_" + pid + "/");
            out.close();
        } catch (IOException e) {
            System.out.println("exception occoured" + e);
        }



        /*Log.e("String", " to be written " + string +" / ");
        String filename = "badmintonLogs.txt";
//        File root = new File("/storage/emulated/0/Badminton", filename);
        File root = new File(Environment.getExternalStorageDirectory(), "Badminton");
        File textFile = new File(root, filename);
        if (!root.exists()) {
            root.mkdirs();
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(textFile);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(string);
            bw.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }*/
    }

    private static String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}

