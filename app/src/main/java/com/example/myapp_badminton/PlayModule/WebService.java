package com.example.myapp_badminton.PlayModule;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WebService extends AsyncTask<String, String, String> {
    String res;
    Context mContext;
    String responseString;
    private AsyncResponse mCallback;


    public WebService(Context context) {
        mContext = context;
        this.mCallback = (AsyncResponse) context;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            if (!arg0[1].equals("downloadVideo")) {
                URL obj = new URL(arg0[0]);//arg0[0]=url
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");//add request header
                con.setConnectTimeout(7000);
                con.setDoOutput(true);  //indicates POST request
                con.setDoInput(true);   //indicates server returns response
                con.setUseCaches(false);
                //Log.d("the postURL is", " " + arg0[0]);
                //Log.d("the postData is", " " + arg0[1]);
                OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
                os.write(arg0[1]);//arg0[1]=data
                os.flush();
                os.close();
                //Log.d("Response from Server", " " + con.getResponseCode());
                res = String.valueOf(con.getResponseCode());
                if (res.equals("200")) {//success
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    responseString = sb.toString();//result form the server
//                //Log.e("Response ", "From " + arg0[2] + " " + responseString);
                } else responseString = res;//other http errors-->server busy
            }else{
                downloadfile(arg0[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }

    protected void onPostExecute(String results) {
        mCallback.onTaskComplete(results);
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private void downloadfile(String vidurl) {

        SimpleDateFormat sd = new SimpleDateFormat("yymmhh");
        String date = sd.format(new Date());
            String name = "video.mp4";

        try {
            /*String rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + "My_Video";*/
            String rootDir = mContext.getCacheDir()
                    + File.separator + "My_Video";
            File rootFile = new File(rootDir);
            rootFile.mkdir();
            URL url = new URL(vidurl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    name));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
//            encrypt(rootDir+"/"+name);
            responseString="done";
            f.close();
        } catch (Exception e) {
            //Log.d("Error....", e.toString());
        }
    }
    public static boolean encrypt(String path) {
        try {
            if (path == null) return false;
            File source = new File(path);
            RandomAccessFile f = new RandomAccessFile(source, "rw");
            f.seek(0);
            int byteToReverse = source.length() < 1024 ? ((int) source.length()) : 1024;
            byte b[] = new byte[byteToReverse];
            f.read(b);
            f.seek(0);
            reverseBytes(b);
            f.write(b);
            f.seek(0);
            b = new byte[byteToReverse];
            f.read(b);
            f.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private static void reverseBytes(byte[] array) {
        if (array == null) return;
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

}




