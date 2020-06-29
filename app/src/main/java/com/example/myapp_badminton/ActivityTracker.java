package com.example.myapp_badminton;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ActivityTracker {
    public static void writeActitivtyLogs(String string) {
        try {
            String fileName = "badmintonLogs.txt";
            File root = new File(Environment.getExternalStorageDirectory(), "Badminton");
            if (!root.exists()) {
                root.mkdirs();
            }
            File textFile = new File(root, fileName);

            BufferedWriter out = new BufferedWriter(
                    new FileWriter(textFile, true));
            out.write(string + "/");
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
}

