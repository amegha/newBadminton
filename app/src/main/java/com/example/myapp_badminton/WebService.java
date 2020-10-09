package com.example.myapp_badminton;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebService extends AsyncTask<String, String, String> {
    String res;
    String responseString;
    NetworkAvailability networkAvailability;
    List<String> nameValuePairs = new ArrayList<String>(2);
    Context mContext;
    private AsyncResponse mCallback;


    public WebService(Context context) {
        mContext = context;
        this.mCallback = (AsyncResponse) context;
        networkAvailability = NetworkAvailability.getInstance(context);
    }

    @Override
    protected String doInBackground(String... arg0) {
        if (arg0[1].equals("badmintonLogs") || arg0[1].equals("scoreUpload")) {
            try {
                File root = new File(Environment.getExternalStorageDirectory(), "Badminton");

//                String sourceFileUri = "/storage/emulated/0/Badminton";
                String sourceFileUri = getFileUri();
//                String sourceFileUri = root.getAbsolutePath();
//                Log.e("file path", "web service " + sourceFileUri);
                HttpURLConnection conn = null;

                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
//                File sourceFile = new File(sourceFileUri + "/" + arg0[1] + ".txt");
                File sourceFile = new File(sourceFileUri , arg0[1] + ".txt");


                Log.e("file path", "web service " + sourceFile.toString());

                if (sourceFile.isFile() && (sourceFile.exists())) {

                    try {
                        int serverResponseCode;
                        String upLoadServerUri = arg0[0];/*API.ServerAddress+API.OFFLINE_PARKOUT_NEW ,"http://stage1.optipacetech.com/MCGM/optipace_api/parkout_new.php","http://stage1.optipacetech.com/MCGM/optipace_api/parkin_new.php";*/
                        Log.d("the postURL is", " " + upLoadServerUri);

                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(sourceFile);
                        URL url = new URL(upLoadServerUri);

                        // Open a HTTP connection to the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE",
                                "multipart/form-data");
                        conn.setRequestProperty("Content-Type",
                                "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty(arg0[1], sourceFileUri);

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=" + arg0[1] + ";filename=\""
                                + sourceFileUri + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                        // create a buffer of maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math
                                    .min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0,
                                    bufferSize);

                        }

                        // send multipart form data necesssary after file
                        // data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens
                                + lineEnd);

                        // Responses from the server (code and message)
                        serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn
                                .getResponseMessage();
                        Log.e("serverResp", "getResponseCode" + serverResponseCode + " getResponseMessage " + serverResponseMessage);
                        if (serverResponseCode == 200) {

                            // messageText.setText(msg);
                            //Toast.makeText(ctx, "File Upload Complete.",
                            //      Toast.LENGTH_SHORT).show();

                            // recursiveDelete(mDirectory1);
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }
                            br.close();
                            responseString = sb.toString();
                            Log.e("RESPONSE:Offline", "" + responseString);

                        }

                        // close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (Exception e) {

                        // dialog.dismiss();
                        e.printStackTrace();

                    }
                    // dialog.dismiss();

                } else {
                    responseString = "404";
                } // End else slice


            } catch (Exception ex) {
                // dialog.dismiss();

                ex.printStackTrace();
            }
            return responseString;
        } else if(!arg0[1].equals("downloadVideo")) {
            try {
                URL obj = new URL(arg0[0]);//arg0[0]=url
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");//add request header
                con.setConnectTimeout(7000);
                con.setDoOutput(true);  //indicates POST request
                con.setDoInput(true);   //indicates server returns response
                con.setUseCaches(false);
                Log.d("the postURL is", " " + arg0[0]);
                Log.d("the postData is", " " + arg0[1]);
                OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
            /*if(arg0.length==3){
                nameValuePairs.add(new BasicNameValuePair("id", "12345"));
                nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }else*/
                os.write(arg0[1]);//arg0[1]=data
                os.flush();
                os.close();
                Log.d("Response from Server", " " + con.getResponseCode());
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
//                Log.e("Response ", "From " + "badmintonLogs" + " " + responseString);
                } else responseString = res;//other http errors-->server busy

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            downloadfile(arg0[0]);
        }
      /*  } else {
            responseString = "no crossCourtBlock";
        }*/
        return responseString;
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
            Log.d("Error....", e.toString());
        }
    }




    private String getFileUri() {
        try {
            String fileName = "badmintonLogs.txt";
            File root = new File(Environment.getExternalStorageDirectory(), "Badminton");

            //
            String name = "Badminton";
            File sdcard; /*= Environment.getExternalStorageDirectory();*/
            if (mContext.getResources().getBoolean(R.bool.internalstorage)) {
                sdcard = mContext.getFilesDir();
            } else if (!mContext.getResources().getBoolean(R.bool.standalone)) {
                sdcard = new File(Environment.getExternalStoragePublicDirectory(name).toString());
            } else {
                if ("goldfish".equals(Build.HARDWARE)) {
                    sdcard = mContext.getFilesDir();
                } else {
                    // sdcard/Android/<app_package_name>/AWARE/ (not shareable, deletes when uninstalling package)
                    sdcard = new File(ContextCompat.getExternalFilesDirs(mContext, null)[0] + "/" + name);
                }
            }
            if (!sdcard.exists()) {
                sdcard.mkdirs();
            }
            return sdcard.toString();
//            return new File(sdcard, fileName);
        } catch (Exception e) {
            e.printStackTrace();

        }


        return null;
    }

    protected void onPostExecute(String results) {
        mCallback.onTaskComplete(results);
    }


}


