package com.example.myapp_badminton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class databaseConnectionAdapter {
    private static final String TAG = "databaseConnectionAdapter";
    AllDataHelper allDataHelper;


    public databaseConnectionAdapter(Context context) {
        allDataHelper = new AllDataHelper(context);
    }


    //to create  database and tables
    static class AllDataHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String STATE = "State";
        private static final String CITY = "City";
        //used to create Academy table
        private static final String A_id = "Academy_id";     // Column I (Primary Key)
        private static final String Academy_name = "Academy_name";
        //Registration Data
        private static final String TABLE_NAME = "user_details";   // Table Name
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
        private static final String DOB = "DATE_OF_BIRTH";
        private static final String TIMESTAMP = "TIME_STAMP";
        private static final String PASSWORD = "PASSWORD";
        private static final String ENC_PASSWORD = "ENCRYPTED_PASSWORD";
        private static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FULL_NAME + " VARCHAR(255) ," + PASSWORD + " VARCHAR(255) ," + ENC_PASSWORD + " VARCHAR(255) ," + EMAIL + " VARCHAR(255) ," + STATE + " VARCHAR(255)," + CITY + " VARCHAR(255) ," + TRAINING_CENTERS + " VARCHAR(255)," + STATE_RANK + " Integer ," + NATIONAL_RANK + " Integer," + IMAGE_DATA + " BLOB," + GENDER + " VARCHAR(255)," + EDUCATION + " VARCHAR(255)," + AGE + " VARCHAR(255)," + DOB + " VARCHAR(255)," + TIMESTAMP + " VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        //et_score details
        private static final String TABLE_NAME1 = "Score_details";   // Table Name
        private static final String UsID = "_id";     // Column I (Primary Key)
        //private static final String FULL_NAME = "Name";    //Column II
        private static final String MAIN_CATEGORY = "Main_category";
        private static final String SUB_CATEGORY = "Sub_Category";
        private static final String DATE_OF_SCORE_SUBMIT = "Date_Score_Submit";

        //   private static final String NEW_PASSWORD="NEW_PASSWORD";
        private static final String SCORE = "Score";
        private static final String CREATE_TABLE_SCORE = "CREATE TABLE " + TABLE_NAME1 + " (" + UsID + " INTEGER , " + FULL_NAME + " VARCHAR(255) ," + DATE_OF_SCORE_SUBMIT + " VARCHAR(255) ," + MAIN_CATEGORY + " VARCHAR(255) ," + SUB_CATEGORY + " VARCHAR(255)  ," + SCORE + " VARCHAR(255) );";
        private static final String DROP_TABLE_SCORE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        //et_score given by coach details
        private static final String TABLE_COACH = "ScoresByCoach";   // Table Name
        private static final String PID = "Pid";     // Column I (Primary Key)
        private static final String CID = "Cid";
        private static final String CNAME = "CoachName";
        private static final String DROP_TABLE_COACH = "DROP TABLE IF EXISTS " + TABLE_COACH;
        private static String TABLE_ACADEMY = "ACADEY_TB";
        //used to create City_Academy table
        private static final String DROP_ACADEMY = "DROP TABLE IF EXISTS " + TABLE_ACADEMY;
        private static String LEVEL = "Level";
        private static final String CREATE_TABLE_COACH = "CREATE TABLE " + TABLE_COACH + " (" + CID + " INTEGER , " + CNAME + " VARCHAR(255) ," + PID + " INTEGER ," + FULL_NAME + " VARCHAR(255) ," + DATE_OF_SCORE_SUBMIT + " VARCHAR(255) ," + MAIN_CATEGORY + " VARCHAR(255) ," + SUB_CATEGORY + " VARCHAR(255)  ," + SCORE + " VARCHAR(255) ," + TRAINING_CENTERS + " VARCHAR(255) ," + LEVEL + " VARCHAR(255) );";
        private static String AREA = "LocationArea";
        private static final String CREATE_TABLE_ACADEMY = "CREATE TABLE " + TABLE_ACADEMY + " (" + A_id + " INTEGER, " + Academy_name + " TEXT ," + STATE + " TEXT," + CITY + " TEXT ," + AREA + " TEXT ," + LEVEL + " TEXT);";
        private SQLiteDatabase database;
        private Context context;


        public AllDataHelper(Context context) {
            super(new DatabaseContext(context), "ALL_Data", null, DATABASE_VERSION);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_ACADEMY);
                db.execSQL(CREATE_TABLE);
                db.execSQL(CREATE_TABLE_SCORE);
                db.execSQL(CREATE_TABLE_COACH);

            } catch (Exception e) {
                Message.message(context, "" + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context, "OnUpgrade");

                db.execSQL(DROP_ACADEMY);
                db.execSQL(DROP_TABLE);
                db.execSQL(DROP_TABLE_SCORE);
                db.execSQL(DROP_TABLE_COACH);

                onCreate(db);
            } catch (Exception e) {
                Message.message(context, "" + e);
            }
        }

        /*Opens the database connection*/
        public void open() {
            this.database = getWritableDatabase();
        }

        /*Close the database connection.*/
        public void close() {
            if (database != null) {
                this.database.close();
            }
        }


        // Insert the image to the Sqlite DB
        public void insertAcademyDatas(String aid, String academy, String State, String City, String area, String level) {
            SQLiteDatabase sd = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(A_id, aid);
            cv.put(Academy_name, academy);
            cv.put(STATE, State);
            cv.put(CITY, City);
            cv.put(AREA, area);
            cv.put(LEVEL, level);
            sd.insert(TABLE_ACADEMY, null, cv);
        }

        public void delete() {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_ACADEMY, null, null);
        }


        //to retrieve all Academies
        public ArrayList<String> getCity() {
            // String all="";
            SQLiteDatabase db = getReadableDatabase();
            ArrayList<String> arrayList = new ArrayList<>();
            String[] columns = {CITY};

            Cursor cursor = db.query(true, TABLE_ACADEMY, columns, null, null, CITY, null, null, null);
            //Cursor cursor=db.rawQuery("SELECT DISTINCT "+CITY+" from "+TABLE_ACADEMY,null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                arrayList.add((cursor.getString(cursor.getColumnIndex(CITY))));
                cursor.moveToNext();
            }
            cursor.close();
            return arrayList;
        }


        //to retrieve all Academies
        public ArrayList<String> getAcademies(String city, String location) {
            // String all="";
            SQLiteDatabase db = getReadableDatabase();
            ArrayList<String> arrayList = new ArrayList<>();
            String[] columns = {Academy_name};
            String selection = CITY + " = ? ";
            String[] selection_args = {city};
//            Cursor cursor = db.query(true, TABLE_ACADEMY, columns, selection, selection_args, Academy_name, null, null, null);
            Cursor cursor = db.rawQuery("SELECT DISTINCT " + Academy_name + " from " + TABLE_ACADEMY + " where City='" + city + "' and " + AREA + "='" + location + "'", null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                arrayList.add((cursor.getString(0)));
                cursor.moveToNext();
            }
            Log.e("academy name", "getAcademies: "+ Collections.singleton(arrayList));
            cursor.close();
            return arrayList;
        }

        public ArrayList<String> getLocations(String city) {
            // String all="";
            Cursor cursor;
            SQLiteDatabase db = getReadableDatabase();
            ArrayList<String> arrayList = new ArrayList<>();
           /* String[] columns = {AREA};
            String selection = CITY + " = ? ";
            String[] selection_args = {city};*/
            cursor = db.rawQuery("SELECT distinct " + AREA + " FROM " + TABLE_ACADEMY + " where " + CITY + " = '" + city + "'", null);

//            Cursor cursor = db.query(true, TABLE_ACADEMY, columns, selection, selection_args, Academy_name, null, null, null);
            //Cursor cursor = db.rawQuery("SELECT DISTINCT " + Academy_name + " from" + TABLE_ACADEMY + " where City=" + city, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                arrayList.add((cursor.getString(0)));
                cursor.moveToNext();
            }
            cursor.close();
            return arrayList;
        }


        //to retrieve all levels
        public ArrayList<String> getLevels(String academy) {
            // String all="";
            SQLiteDatabase db = getWritableDatabase();
            ArrayList<String> arrayList = new ArrayList<>();
            String[] columns = {LEVEL};
            String selection = Academy_name + " = ? ";
            String[] selection_args = {academy};
            Cursor cursor = db.query(true, TABLE_ACADEMY, columns, selection, selection_args, LEVEL, null, null, null);
            // Cursor cursor = db.rawQuery("SELECT DISTINCT " + LEVEL + " from " + TABLE_ACADEMY + " where Academy_name =" + academy, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                arrayList.add((cursor.getString(cursor.getColumnIndex(LEVEL))));
                cursor.moveToNext();
            }
            cursor.close();
            return arrayList;
        }

        //used to get academy id
        public String getAid(String academy) {

            SQLiteDatabase db = getReadableDatabase();
            String aid = null;
            String[] columns = {A_id};
            String selection = Academy_name + " = ? ";
            String[] selection_args = {academy};
            Cursor cursor = db.query(true, TABLE_ACADEMY, columns, selection, selection_args, A_id, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                aid = cursor.getString(cursor.getColumnIndex(A_id));
                cursor.moveToNext();
            }
            cursor.close();
            return aid;
        }


        /**
         * used to get only username column and search for name in db
         */
//just changed parameter from name to uid
        public Cursor search_name(String uid, SQLiteDatabase sqLiteDatabase) {

            String[] projection = {FULL_NAME};
            String selection = UID + " = ? ";
            String[] selection_args = {uid};
            Cursor cursor = sqLiteDatabase.query(TABLE_NAME, projection, selection, selection_args, null, null, null);
            return cursor;
        }

        //changed from name email
        public Cursor getUID_email(String email, SQLiteDatabase sqLiteDatabase) {

            String[] projection = {UID};
            String selection = EMAIL + " = ? ";
            String[] selection_args = {email};
            Cursor cursor = sqLiteDatabase.query(TABLE_NAME, projection, selection, selection_args, null, null, null);
            return cursor;
        }

        //changed UID
        public Cursor getUID(String name, SQLiteDatabase sqLiteDatabase) {

            String[] projection = {UID};
            String selection = FULL_NAME + " = ? ";
            String[] selection_args = {name};
            Cursor cursor = sqLiteDatabase.query(TABLE_NAME, projection, selection, selection_args, null, null, null);
            return cursor;
        }

        public Cursor search_Email(String name, SQLiteDatabase sqLiteDatabase) {

            String[] projection = {EMAIL};
            String selection = EMAIL + " = ? ";
            String[] selection_args = {name};
            Cursor cursor = sqLiteDatabase.query(TABLE_NAME, projection, selection, selection_args, null, null, null);
            return cursor;
        }

        /**
         * used to get only password column and search for password in db
         */
        public Cursor search_pass(String pass, SQLiteDatabase sqLiteDatabase) {

            String[] projection = {PASSWORD};
            String selection = PASSWORD + " = ? ";
            String[] selection_args = {pass};
            Cursor cursor = sqLiteDatabase.query(TABLE_NAME, projection, selection, selection_args, null, null, null);
            return cursor;
        }


        public void insertReg_data(String name, String pass, String encPass, String email, String state, String City, String training_academy, int sRank, int nRank, byte[] b, String gender, String education, String age, String dob, String timestamp) {
            SQLiteDatabase sd = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(FULL_NAME, name);
            cv.put(PASSWORD, pass);
            cv.put(ENC_PASSWORD, encPass);
            cv.put(EMAIL, email);
            cv.put(STATE, state);
            cv.put(CITY, City);
            cv.put(TRAINING_CENTERS, training_academy);
            cv.put(STATE_RANK, sRank);
            cv.put(NATIONAL_RANK, nRank);
            cv.put(IMAGE_DATA, b);
            cv.put(AGE, age);
            cv.put(GENDER, gender);
            cv.put(EDUCATION, education);
            cv.put(DOB, dob);
            cv.put(TIMESTAMP, timestamp);
            sd.insert(TABLE_NAME, null, cv);
        }


        public Cursor get_lastScoreData(String UID, String date, SQLiteDatabase sqLiteDatabase) {

            // Cursor cursor=sqLiteDatabase.rawQuery("select max(Date_Score_submit) from Score_details where "+ScoreStorageHelper.UID +"= "+UID+" and (select max(Date_Score_submit) from Score_details where "+ScoreStorageHelper.UID +"= "+UID+")<(select strftime(\"%Y-%m-%d\",\"now\"));",null);
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from Score_details where _id=" + UID + " AND Date_Score_Submit=" + date, null);
            return cursor;
        }


        public Cursor getScore(String uid, SQLiteDatabase sqLiteDatabase) {

            String[] projection = {SCORE, DATE_OF_SCORE_SUBMIT};
            String selection = UsID + " = ? ";
            String[] selection_args = {uid};

            Cursor cursor = sqLiteDatabase.rawQuery("select " + SCORE + "," + DATE_OF_SCORE_SUBMIT + " from Score_details where " + UsID + " = " + uid, null);

            return cursor;
        }

        public Cursor getPreviousDateDb(String UID, String p_date, SQLiteDatabase sqLiteDatabase) {

            String[] Columns = {DATE_OF_SCORE_SUBMIT};
            String where_Clasuse = UsID + " = ?  And " + DATE_OF_SCORE_SUBMIT + " = ? ";
            String[] where_args = {UID, p_date};
            Cursor cursor = sqLiteDatabase.query(TABLE_NAME1, Columns, where_Clasuse, where_args, null, null, null);

            return cursor;
        }
//arguement need to added i.e last date need to be send as parameter to find second last login date

        public Cursor get_sec_last_login(String UID, SQLiteDatabase sqLiteDatabase) {

            String[] Columns = {"max(" + DATE_OF_SCORE_SUBMIT + ")"};
            //select strftime("%Y-%m-%d","now")
            String year = "\"%Y";
            String month = "%m";
            String day = "%d\"";
            String getToday = "\"now\"";
            String q_sub = "(select strftime(" + year + "-" + month + "-" + day + "," + getToday + ")";
            String where_Clasuse = UsID + " = ? And " + DATE_OF_SCORE_SUBMIT + " != ?";
            String[] where_args = {UID, q_sub};
            //Cursor cursor=sqLiteDatabase.query(ScoreStorageHelper.TABLE_NAME,Columns,where_Clasuse,where_args,null,null,null);
            //** Cursor cursor=sqLiteDatabase.rawQuery("select max(Date_Score_submit) from Score_details where _id="+UID+" and (select Date_Score_submit from Score_details where "+ScoreStorageHelper.UID +"= "+UID+")!=(select strftime(\"%Y-%m-%d\",\"now\"))",null);
            //Cursor cursor=sqLiteDatabase.rawQuery("select max(Date_Score_submit) from Score_details where "+ScoreStorageHelper.UID +"= "+UID+" and (select max(Date_Score_submit) from Score_details where "+ScoreStorageHelper.UID +"= "+UID+" and "+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+"!= "+last_date+");",null);
            Cursor cursor = sqLiteDatabase.rawQuery("select max(" + DATE_OF_SCORE_SUBMIT + ") from Score_details where " + UsID + "=" + UID + " and " + DATE_OF_SCORE_SUBMIT + " < (select max(" + DATE_OF_SCORE_SUBMIT + ") from Score_details);", null);
            return cursor;
        }

        public Cursor get_no_days_score_NotEntered(String UID, String p_date, SQLiteDatabase sqLiteDatabase) {
            int a;
            String[] Columns = {"round(julianday('now') - julianday(max(" + DATE_OF_SCORE_SUBMIT + ")))"};
            String where_Clasuse = UsID + " = ?  And " + DATE_OF_SCORE_SUBMIT + " = ? ";
            String[] where_args = {UID, p_date};
            String getToday = "\"now\"";
            // Cursor cursor=sqLiteDatabase.query(ScoreStorageHelper.TABLE_NAME,Columns,where_Clasuse,where_args,null,null,null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT round(julianday('now') - julianday(max(" + DATE_OF_SCORE_SUBMIT + ")))  from Score_details where _id=" + UID + " and (select max(" + DATE_OF_SCORE_SUBMIT + ") from Score_details) <= strftime(\"%Y-%m-%d\",\"now\");", null);
            // SELECT round(julianday('now') - julianday(max(Date_Score_submit)))  from Score_details where _id="1" and (select max(Date_Score_submit) from Score_details) <= strftime("%Y-%m-%d","now")
            StringBuffer buffer = new StringBuffer();
            while (cursor.moveToNext()) {
                a = cursor.getInt(0);
                buffer.append(a);
            }

            // return Integer.parseInt(buffer.toString());
            return cursor;
        }


        public Cursor get_last_login(String UID, SQLiteDatabase sqLiteDatabase) {

            String[] Columns = {"max(" + DATE_OF_SCORE_SUBMIT + ")"};
            //select strftime("%Y-%m-%d","now")
            String year = "\"%Y";
            String month = "%m";
            String day = "%d\"";
            String getToday = "\"now\"";
            String q_sub = "(select strftime(" + year + "-" + month + "-" + day + "," + getToday + ")";
            String where_Clasuse = AllDataHelper.UID + " = ? And " + DATE_OF_SCORE_SUBMIT + " == ?";
            String[] where_args = {UID, q_sub};
           /* String result=check_leap_year(UID,sqLiteDatabase);
            if(result.equals("leap"))
            {

            }*/
            //Cursor cursor=sqLiteDatabase.query(ScoreStorageHelper.TABLE_NAME,Columns,where_Clasuse,where_args,null,null,null);
            Cursor cursor = sqLiteDatabase.rawQuery("select max(Date_Score_submit) from Score_details where _id = " + UID + " and (select max(Date_Score_submit) from Score_details where " + UsID + "= " + UID + ")<=(select strftime(\"%Y-%m-%d\",\"now\"));", null);
            return cursor;
        }

        //inserting
        public long insertData(String uid, String name, String DOSS, String main_category, String sub_category, String score) {
            long result;
            SQLiteDatabase dbb = getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(UsID, uid);
            contentValues.put(FULL_NAME, name);
            contentValues.put(DATE_OF_SCORE_SUBMIT, DOSS);
            contentValues.put(MAIN_CATEGORY, main_category);
            contentValues.put(SUB_CATEGORY, sub_category);
            contentValues.put(SCORE, score);

            return result = dbb.insert("Score_details", null, contentValues);

        }


        //inserting coach Scores
        public long insertCoachScore(String cid, String cname, String pid, String pname, String DOSS, String main_category, String sub_category, String score, String academy, String level) {
            long result;
            SQLiteDatabase dbb = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CID, cid);
            contentValues.put(CNAME, cname);
            contentValues.put(PID, pid);
            contentValues.put(FULL_NAME, pname);
            contentValues.put(CID, cid);
            contentValues.put(DATE_OF_SCORE_SUBMIT, DOSS);
            contentValues.put(MAIN_CATEGORY, main_category);
            contentValues.put(SUB_CATEGORY, sub_category);
            contentValues.put(SCORE, score);
            contentValues.put(TRAINING_CENTERS, academy);
            contentValues.put(LEVEL, level);
            return result = dbb.insert("ScoresByCoach", null, contentValues);

        }

        //to retrieve all Ids
        public ArrayList<String> getDataID(String cid) {
            // String all="";
            SQLiteDatabase db = getWritableDatabase();
            ArrayList<String> arrayList = new ArrayList<>();
            String[] columns = {UID};
            String selection = UID + " != ? ";
            String[] selection_args = {cid};
            Cursor cursor = db.query(TABLE_NAME, columns, selection, selection_args, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                arrayList.add((cursor.getString(cursor.getColumnIndex(UID))));
                cursor.moveToNext();
            }
            cursor.close();
            return arrayList;
        }

        //to retrieve all names
        //to retrieve
        public ArrayList<String> getDataName(String cid) {
            // String all="";
            SQLiteDatabase db = getWritableDatabase();
            ArrayList<String> arrayList = new ArrayList<>();
            String[] columns = {FULL_NAME};
            String selection = UID + " != ? ";
            String[] selection_args = {cid};
            Cursor cursor = db.query(TABLE_NAME, columns, selection, selection_args, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                arrayList.add((cursor.getString(cursor.getColumnIndex(FULL_NAME))));
                cursor.moveToNext();


            }
            cursor.close();
            return arrayList;

        }

        // Get the image from SQLite DB
// We will just get the last image we just saved for convenience...
        public ArrayList<String> retreiveImageFromDB(String cid, SQLiteDatabase sd) {

            String imageString;

            ArrayList<String> arrayList = new ArrayList<>();
            String[] columns = {IMAGE_DATA};
            String selection = UID + " != ? ";
            String[] selection_args = {cid};
            Cursor cursor = sd.query(TABLE_NAME, columns, selection, selection_args, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(IMAGE_DATA));
                imageString = Base64.encodeToString(blob, Base64.DEFAULT);

                arrayList.add(imageString);
                cursor.moveToNext();


            }
            cursor.close();

            return arrayList;

        }


        //for skip button working ,getting only max of date i.e last date of et_score entry
        public Cursor get_lastdateCoach(String ID, SQLiteDatabase sqLiteDatabase) {

            //Cursor cursor=sqLiteDatabase.query(ScoreStorageHelper.TABLE_NAME,Columns,where_Clasuse,where_args,null,null,null);
            Cursor cursor = sqLiteDatabase.rawQuery("select max(Date_Score_submit) from ScoresByCoach where " + PID + "= " + ID + " and (select max(Date_Score_submit) from Score_details where " + PID + "= " + ID + ")<(select strftime(\"%Y-%m-%d\",\"now\"));", null);
            return cursor;
        }


        //Coach et_score entry last date
        public Cursor getCoachLastScoreEntry(String cid, String pid, SQLiteDatabase db) {
            Cursor cursor = db.rawQuery("SELECT max(Date_Score_Submit) from ScoresByCoach where Pid=" + pid + " and Cid=" + cid, null);
            return cursor;

        }


        public Cursor getScoreCoach(String cid, String uid, SQLiteDatabase sqLiteDatabase) {

            String[] projection = {SCORE, DATE_OF_SCORE_SUBMIT};
            String selection = UsID + " = ? ";
            String[] selection_args = {uid};
            //Cursor cursor=sqLiteDatabase.query(ScoreStorageHelper.TABLE_NAME,projection,selection,selection_args,null,null,null);
            Cursor cursor = sqLiteDatabase.rawQuery("select " + SCORE + "," + DATE_OF_SCORE_SUBMIT + " from ScoresByCoach where Cid=" + cid + " and Pid= " + uid, null);

            return cursor;
        }


        public String[] getAllData(String pday, SQLiteDatabase db) {
            String[] Columns = {MAIN_CATEGORY, SUB_CATEGORY, SCORE, TRAINING_CENTERS, LEVEL};
            String where_Clasuse = DATE_OF_SCORE_SUBMIT + " = ? ";
            String[] where_args = {pday};
            Cursor c = db.query(TABLE_COACH, Columns, where_Clasuse, where_args, null, null, null);

            // Cursor c = db.rawQuery("SELECT "+MAIN_CATEGORY+","+SUB_CATEGORY+","+SCORE+","+TRAINING_CENTERS+","+LEVEL+" FROM "+TABLE_COACH+" where "+DATE_OF_SCORE_SUBMIT+"="+pday, null);
            String[] selection = new String[c.getColumnCount()];
            c.moveToFirst();
            for (int i = 0; i < c.getColumnCount(); i++) {
                selection[i] = c.getString(i);
            }
            System.out.println("get all data " + Arrays.toString(selection));

            return selection;
        }

        public String[] getAllDataPlayer(String pday, SQLiteDatabase db) {
            String[] Columns = {MAIN_CATEGORY, SUB_CATEGORY, SCORE};
            String where_Clasuse = DATE_OF_SCORE_SUBMIT + " = ? ";
            String[] where_args = {pday};
            Cursor c = db.query(TABLE_NAME1, Columns, where_Clasuse, where_args, null, null, null);
            //Cursor c = db.rawQuery("SELECT "+MAIN_CATEGORY+","+SUB_CATEGORY+","+SCORE+" FROM "+TABLE_NAME1+" where "+DATE_OF_SCORE_SUBMIT+" = "+pday, null);
            String[] selection = new String[c.getColumnCount()];
            c.moveToFirst();
            for (int i = 0; i < c.getColumnCount(); i++) {
                selection[i] = c.getString(i);
            }
            System.out.println("get all data " + Arrays.toString(selection));

            return selection;
        }

        public byte[] retreiveImageFromDBPlayer(String id, SQLiteDatabase sd) {

            byte[] blob = new byte[0];
            String[] columns = {IMAGE_DATA};
            String selection = UID + " = ? ";
            String[] selection_args = {id};
            Cursor cursor = sd.query(TABLE_NAME, columns, selection, selection_args, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                blob = cursor.getBlob(cursor.getColumnIndex(IMAGE_DATA));
                cursor.moveToNext();
            }
            cursor.close();

            return blob;

        }

        public ArrayList<String> getLevels(String cityName, String locationName, String academyName) {
            SQLiteDatabase db = getReadableDatabase();
            ArrayList<String> arrayList = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT DISTINCT " + LEVEL + " from " + TABLE_ACADEMY + " where " + CITY + "='" + cityName + "' and " + AREA + "='" + locationName + "' and " + Academy_name + " = '" + academyName + "'", null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                arrayList.add((cursor.getString(0)));
                cursor.moveToNext();
            }
            cursor.close();
            return arrayList;
        }

        public String getAid(String cityName, String locationName, String academyName) {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT DISTINCT " + A_id + " from " + TABLE_ACADEMY + " where " + CITY + "='" + cityName + "' and " + AREA + "='" + locationName + "' and " + Academy_name + " = '" + academyName + "'", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                return (cursor.getString(0));
            }
            cursor.close();
            return null;
        }
    }
}
