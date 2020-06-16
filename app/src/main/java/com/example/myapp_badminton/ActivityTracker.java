package com.example.myapp_badminton;

import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ActivityTracker {
    static void writeActitivtyLogs(Exception e) {
        String filename = "badmintonLogs.txt";
//        File root = new File("/storage/emulated/0/", filename);
        File root = new File(Environment.getExternalStorageDirectory(), "Badminton");
        if (!root.exists()) {
            root.mkdirs();
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String ss = sw.toString();
        if (!root.exists()) {
            root.mkdirs();
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(filename);
            writer.append(ss);
            writer.flush();
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}

