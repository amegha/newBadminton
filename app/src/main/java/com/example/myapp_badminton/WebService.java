package com.example.myapp_badminton;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebService extends AsyncTask<String, String, String> {
    String res;
    String responseString;
    NetworkAvailability networkAvailability;
    List<String> nameValuePairs = new ArrayList<String>(2);
    private AsyncResponse mCallback;


    public WebService(Context context) {
        Context mContext = context;
        this.mCallback = (AsyncResponse) context;
        networkAvailability = NetworkAvailability.getInstance(context);
    }

    @Override
    protected String doInBackground(String... arg0) {
        if (arg0[1].equals("fileUpload")) {
            try {
                File root = new File(Environment.getExternalStorageDirectory(), "Badminton");

//                String sourceFileUri = "/storage/emulated/0/Badminton/badmintonLogs.txt";
                String sourceFileUri = Uri.fromFile(root);
                Uri.fromFile(root);
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(sourceFileUri);

                if (sourceFile.isFile()) {

                    try {
                        int serverResponseCode;
                        String upLoadServerUri = arg0[0];/*API.ServerAddress+API.OFFLINE_PARKOUT_NEW ,"http://stage1.optipacetech.com/MCGM/optipace_api/parkout_new.php","http://stage1.optipacetech.com/MCGM/optipace_api/parkin_new.php";*/
                        Log.d("the postURL is", " " + upLoadServerUri);

                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(
                                sourceFile);
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
                        conn.setRequestProperty(arg0[2], sourceFileUri);

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=" + arg0[2] + ";filename=\""
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
                            responseString = sb.toString() + ";" + arg0[2];
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

                } // End else block


            } catch (Exception ex) {
                // dialog.dismiss();

                ex.printStackTrace();
            }
            return responseString;
        } else
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
//                Log.e("Response ", "From " + arg0[2] + " " + responseString);
                } else responseString = res;//other http errors-->server busy

            } catch (Exception e) {
                e.printStackTrace();
            }
      /*  } else {
            responseString = "no net";
        }*/
        return responseString;
    }

    protected void onPostExecute(String results) {
        mCallback.onTaskComplete(results);
    }


}


