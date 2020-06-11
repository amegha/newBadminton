package com.example.myapp_badminton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyDbAdapter {
    private static final String TAG = "MyDbAdapter";
    myDbHelper myhelper;

    public MyDbAdapter(Context context) {
        myhelper = new myDbHelper(context);

    }

    //inserting
    public long insertData(String fname,String password, String enc_pass, String email, String training_centers, int s_rank, int n_rank, String image_data, String gender, String Education, String Player_type, String age, String DOB, String timestamp) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.FULL_NAME, fname);
        contentValues.put(myDbHelper.PASSWORD, password);
        contentValues.put(myDbHelper.ENC_PASSWORD, enc_pass);
        contentValues.put(myDbHelper.EMAIL, email);
        contentValues.put(myDbHelper.TRAINING_CENTERS, training_centers);
        contentValues.put(myDbHelper.STATE_RANK, s_rank);
        contentValues.put(myDbHelper.NATIONAL_RANK, n_rank);
        contentValues.put(myDbHelper.IMAGE_DATA, image_data);
        contentValues.put(myDbHelper.AGE, age);
        contentValues.put(myDbHelper.GENDER, gender);
        contentValues.put(myDbHelper.EDUCATION, Education);
        contentValues.put(myDbHelper.PLAYER_TYPE, Player_type);
        contentValues.put(myDbHelper.DOB, DOB);
        contentValues.put(myDbHelper.TIMESTAMP, timestamp);

        long id = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        return id;
    }





    //to retrieve
    public String getData() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID, myDbHelper.FULL_NAME, myDbHelper.PASSWORD, myDbHelper.ENC_PASSWORD,myDbHelper.EMAIL, myDbHelper.TRAINING_CENTERS, myDbHelper.STATE_RANK, myDbHelper.NATIONAL_RANK, myDbHelper.IMAGE_DATA, myDbHelper.GENDER, myDbHelper.EDUCATION, myDbHelper.PLAYER_TYPE, myDbHelper.AGE, myDbHelper.DOB, myDbHelper.TIMESTAMP};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToFirst()) {
            int cid = cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String fname = cursor.getString(cursor.getColumnIndex(myDbHelper.FULL_NAME));
            //String  mname =cursor.getString(cursor.getColumnIndex(myDbHelper.MNAME));
            String  pass =cursor.getString(cursor.getColumnIndex(myDbHelper.PASSWORD));
            String  encpass =cursor.getString(cursor.getColumnIndex(myDbHelper.ENC_PASSWORD));
            String email = cursor.getString(cursor.getColumnIndex(myDbHelper.EMAIL));
            String training_center = cursor.getString(cursor.getColumnIndex(myDbHelper.TRAINING_CENTERS));
            String s_rank = cursor.getString(cursor.getColumnIndex(myDbHelper.STATE_RANK));
            String n_rank = cursor.getString(cursor.getColumnIndex(myDbHelper.NATIONAL_RANK));
            String image = cursor.getString(cursor.getColumnIndex(myDbHelper.IMAGE_DATA));
            String age = cursor.getString(cursor.getColumnIndex(myDbHelper.AGE));
            String gender = cursor.getString(cursor.getColumnIndex(myDbHelper.GENDER));
            String education = cursor.getString(cursor.getColumnIndex(myDbHelper.EDUCATION));
            String player_type = cursor.getString(cursor.getColumnIndex(myDbHelper.PLAYER_TYPE));
            String dob = cursor.getString(cursor.getColumnIndex(myDbHelper.DOB));
            String timestamp = cursor.getString(cursor.getColumnIndex(myDbHelper.TIMESTAMP));

            String toXml = "<user_details>\n<userName>" + fname + "</userName>\n<userType>" + player_type + "</userType>\n<uAge>" + age + "</uAge>\n<uDob>" + dob + "</uDob>\n<usex>" + gender + "</usex>\n<ueducation>" + education + "</ueducation>\n<umailid>" + email + "</umailid>\n<utraining>" + training_center + "</utraining>\n <uothers>no Center</uothers>\n<ustateRanking>" + s_rank + "</ustateRanking>\n<unationalRank>" + n_rank + "</unationalRank>\n<uphoto>" + image + "</uphoto>\n</user_details>\n";
            Log.e(TAG, "getData: " + toXml);
            writeToTxtFile(toXml);
            try {
                sendRequest(toXml);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return buffer.toString();
    }

    private void writeToTxtFile(String toXml) {
        String filename = "req.txt";
        File root = new File(Environment.getExternalStorageDirectory(), "Badminton");
        if (!root.exists()) {
            root.mkdirs();
        }
        File textFile = new File(root, filename);
        FileWriter writer = null;
        try {
            writer = new FileWriter(textFile);
            writer.append(toXml);
            writer.flush();
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }





    //server connection
    public Boolean sendRequest(String requestContent) throws Exception {
        String responseString;

        ConnectivityManager cm = (ConnectivityManager) myhelper.context.getSystemService(myhelper.context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");  // Overwrite http header host 9096987

        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {

                String url = "http://stage1.optipacetech.com/badminton/user_details.php";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                int len = requestContent.length();
                //add request header
                con.setRequestMethod("POST");
                // Send post request
                con.setConnectTimeout(15000);
                con.setDoOutput(true);  //indicates POST request
                con.setDoInput(true);   //indicates server returns response
                con.setUseCaches(false);

                OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
                os.write(requestContent);
                os.flush();
                os.close();
                int res = con.getResponseCode();
                String result = String.valueOf(res);

                if (res == 200) {
                    Toast.makeText(myhelper.context, "200", Toast.LENGTH_SHORT).show();
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    responseString = sb.toString();
                    String filename = "xml_resp.txt";
                    File root = new File(Environment.getExternalStorageDirectory(), "Bank");
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File gpx = new File(root, filename);
                    FileWriter writer = new FileWriter(gpx);
                    writer.append(responseString);
                    writer.flush();
                    writer.close();

                } else {
                    Toast.makeText(myhelper.context, result, Toast.LENGTH_SHORT).show();

                }
            } catch (MalformedURLException e) {
                Toast.makeText(myhelper.context, "Malformed URLException", Toast.LENGTH_SHORT).show();
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Toast.makeText(myhelper.context, "IOException", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(myhelper.context, "No Network", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    //to delete
    public int delete(String uname) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs = {uname};

        int count = db.delete(myDbHelper.TABLE_NAME, myDbHelper.FULL_NAME + " = ?", whereArgs);
        return count;
    }

    //to update
    public int updateName(String oldName, String newName) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.FULL_NAME, newName);
        String[] whereArgs = {oldName};
        int count = db.update(myDbHelper.TABLE_NAME, contentValues, myDbHelper.FULL_NAME + " = ?", whereArgs);
        return count;
    }

    //to update
    public int updatePass(String name) {
        SQLiteDatabase db=myhelper.getWritableDatabase();
        //String[] projection={myDbHelper.PASSWORD};
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.PASSWORD, "1234");
        String selection=myDbHelper.FULL_NAME + " = ? ";
        String[] selection_args={name};
        int count=db.update(myDbHelper.TABLE_NAME,contentValues,selection,selection_args);
        Toast.makeText(myhelper.context,"Your phoneNumber is set to 1234",Toast.LENGTH_LONG).show();
        return count;
    }


    //to create  database and tables
    static class myDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "badminton";    // Database Name
        private static final String TABLE_NAME = "user_details";   // Table Name
        private static final int DATABASE_VERSION = 1;  // Database Version
        private static final String UID = "_id";     // Column I (Primary Key)
        private static final String FULL_NAME = "Name";    //Column II
        private static final String EMAIL = "Email";    //Column V
        private static final String TRAINING_CENTERS = "Training_centers";    // Column VI
        private static final String STATE_RANK = "STATE_RANK";// Column VII
        private static final String NATIONAL_RANK = "NATIONAL_RANK";//Column VIII
        private static final String IMAGE_DATA = "IMAGE";//IX
        private static final String AGE = "AGE";//X
        private static final String GENDER = "GENDER";
        private static final String EDUCATION = "EDUCATION";
        private static final String PLAYER_TYPE = "PLAYER_TYPE";
        private static final String DOB = "DATE_OF_BIRTH";
        private static final String TIMESTAMP = "TIME_STAMP";
        private static final String PASSWORD="PASSWORD";
        private static final String ENC_PASSWORD="ENCRYPTED_PASSWORD";


        private static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FULL_NAME + " VARCHAR(255) ,"+PASSWORD+" VARCHAR(255) ,"+ENC_PASSWORD+" VARCHAR(255) ," + EMAIL + " VARCHAR(255)," + TRAINING_CENTERS + " VARCHAR(255)," + STATE_RANK + " Integer ," + NATIONAL_RANK + " Integer," + IMAGE_DATA + " BLOB," + GENDER + " VARCHAR(255)," + EDUCATION + " VARCHAR(255)," + PLAYER_TYPE + " VARCHAR(255)," + AGE + " VARCHAR(255)," + DOB + " VARCHAR(255)," + TIMESTAMP + " VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
       private Context context;

        public myDbHelper(Context context) {
            super(new DatabaseContext(context), "badminton", null, DATABASE_VERSION);

            //super(context, DATABASE_NAME, null, DATABASE_VERSION);
            //this.context = context;*/
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);

            } catch (Exception e) {
                Message.message(context, "" + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context, "OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (Exception e) {
                Message.message(context, "" + e);
            }
        }




/**
 * used to get only username column and search for etName in db
 */

        public Cursor search_name(String name, SQLiteDatabase sqLiteDatabase) {

            String[] projection={myDbHelper.FULL_NAME};
            String selection=myDbHelper.FULL_NAME + " = ? ";
            String[] selection_args={name};
            Cursor cursor=sqLiteDatabase.query(myDbHelper.TABLE_NAME,projection,selection,selection_args,null,null,null);
            return cursor;
        }

        public Cursor getUID(String name, SQLiteDatabase sqLiteDatabase) {

            String[] projection={myDbHelper.UID};
            String selection=myDbHelper.FULL_NAME + " = ? ";
            String[] selection_args={name};
            Cursor cursor=sqLiteDatabase.query(myDbHelper.TABLE_NAME,projection,selection,selection_args,null,null,null);
            return cursor;
        }

        /**
         * used to get only phoneNumber column and search for phoneNumber in db
         */
        public Cursor search_pass(String pass, SQLiteDatabase sqLiteDatabase) {

            String[] projection={myDbHelper.PASSWORD};
            String selection=myDbHelper.PASSWORD + " = ? ";
            String[] selection_args={pass};
            Cursor cursor=sqLiteDatabase.query(myDbHelper.TABLE_NAME,projection,selection,selection_args,null,null,null);
            return cursor;
        }
        public Cursor check_usertype(String id, SQLiteDatabase sqLiteDatabase) {

            String[] projection={myDbHelper.PLAYER_TYPE};
            String selection=myDbHelper.UID + " = ? ";
            String[] selection_args={id};
            Cursor cursor=sqLiteDatabase.query(myDbHelper.TABLE_NAME,projection,selection,selection_args,null,null,null);
            return cursor;
        }

    }
}
