package com.example.myapp_badminton.PlayModule;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.NameValuePair;

public class WebService extends AsyncTask<String, String, String> {
    String res;
    String responseString;
    private AsyncResponse mCallback;


    public WebService(Context context) {
        Context mContext = context;
        this.mCallback = (AsyncResponse) context;
    }

    @Override
    protected String doInBackground(String... arg0) {
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
}




