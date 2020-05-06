package com.example.myapp_badminton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ScoreStorageAdapter {
    ScoreStorageHelper scoreStorageHelper;

   private static final String TAG = "ScoreStorageAdapter";

    public ScoreStorageAdapter(Context context) { scoreStorageHelper = new ScoreStorageHelper(context);    }


    //inserting
    public long insertData(String uid,String name,String  DOSS, String main_category, String sub_category,String score ) {
        SQLiteDatabase dbb = scoreStorageHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ScoreStorageHelper.UID,uid);
        contentValues.put(ScoreStorageHelper.FULL_NAME, name);
        contentValues.put(ScoreStorageHelper.DATE_OF_SCORE_SUBMIT, DOSS);
        contentValues.put(ScoreStorageHelper.MAIN_CATEGORY, main_category);
        contentValues.put(ScoreStorageHelper.SUB_CATEGORY, sub_category);
        contentValues.put(ScoreStorageHelper.SCORE, score);

         long s_id = dbb.insert("Score_details", null, contentValues);
        return s_id;
    }

    //to retrieve
    public String getData() {
       // String all="";
        SQLiteDatabase db = scoreStorageHelper.getWritableDatabase();
        String[] columns = {ScoreStorageHelper.UID, ScoreStorageHelper.FULL_NAME, ScoreStorageHelper.DATE_OF_SCORE_SUBMIT, ScoreStorageHelper.MAIN_CATEGORY,ScoreStorageHelper.SUB_CATEGORY,ScoreStorageHelper.SCORE};
        Cursor cursor = db.query(ScoreStorageHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            int cid = cursor.getInt(cursor.getColumnIndex(ScoreStorageHelper.UID));
            String name = cursor.getString(cursor.getColumnIndex(ScoreStorageHelper.FULL_NAME));
            String  main =cursor.getString(cursor.getColumnIndex(ScoreStorageHelper.MAIN_CATEGORY));
            String  sub =cursor.getString(cursor.getColumnIndex(ScoreStorageHelper.SUB_CATEGORY));
            String  dos = cursor.getString(cursor.getColumnIndex(ScoreStorageHelper.DATE_OF_SCORE_SUBMIT));
            String score_entered = cursor.getString(cursor.getColumnIndex(ScoreStorageHelper.SCORE));
            buffer.append("Id : "+cid+"Name : "+name+"Main : "+main+"Sub : "+sub+"Date : "+dos+"Score : "+score_entered);

        }

        return buffer.toString();
    }


    //to create  database and tables
    static class ScoreStorageHelper extends SQLiteOpenHelper {

        private static final String TABLE_NAME = "Score_details";   // Table Name
        private static final int DATABASE_VERSION = 1;  // Database Version
        private static final String UID = "_id";     // Column I (Primary Key)
        private static final String FULL_NAME = "Name";    //Column II
        private static final String MAIN_CATEGORY = "Main_category";
        private static final String SUB_CATEGORY = "Sub_Category";
        private static final String DATE_OF_SCORE_SUBMIT = "Date_Score_Submit";
        private static final String SCORE = "Score";
        private static final String CREATE_TABLE ="CREATE TABLE " +TABLE_NAME+ " (" +UID+ " VARCHAR(255) NOT NULL, "+FULL_NAME+" VARCHAR(255) NOT NULL,"+DATE_OF_SCORE_SUBMIT+ " VARCHAR(255) NOT NULL,"+MAIN_CATEGORY+ " VARCHAR(255) NOT NULL,"+SUB_CATEGORY+ " VARCHAR(255) NOT NULL ,"+SCORE+ " VARCHAR(255) NOT NULL);";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;
        private SQLiteDatabase database;

        private static final String CREATE_TABLE_Mentor ="CREATE TABLE Mentor_Score_Entry (" +UID+ " VARCHAR(255) NOT NULL, "+FULL_NAME+" VARCHAR(255) NOT NULL,"+DATE_OF_SCORE_SUBMIT+ " VARCHAR(255) NOT NULL,"+MAIN_CATEGORY+ " VARCHAR(255) NOT NULL,"+SUB_CATEGORY+ " VARCHAR(255) NOT NULL ,"+SCORE+ " VARCHAR(255) NOT NULL);";




        public ScoreStorageHelper(Context context) {
            super(new DatabaseContext(context), "Score_Data", null, DATABASE_VERSION);

            //super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context = context;
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


        public void open() {
            this.database = getWritableDatabase();
        }

        /**
         * Close the database connection.
         */
        public void close() {
            if (database != null) {
                this.database.close();
            }
        }
       public Cursor getScore(String uid, SQLiteDatabase sqLiteDatabase) {

            String[] projection={ScoreStorageHelper.SCORE,ScoreStorageHelper.DATE_OF_SCORE_SUBMIT};
            String selection=ScoreStorageHelper.UID + " = ? ";
            String[] selection_args={uid};
            //Cursor cursor=sqLiteDatabase.query(ScoreStorageHelper.TABLE_NAME,projection,selection,selection_args,null,null,null);
           Cursor cursor=sqLiteDatabase.rawQuery("select "+ScoreStorageHelper.SCORE+","+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+" from Score_details where "+ScoreStorageHelper.UID+" = "+uid,null);

            return cursor;
        }









        public Cursor getPreviousDateDb(String UID,String p_date, SQLiteDatabase sqLiteDatabase) {

            String[] Columns={ScoreStorageHelper.DATE_OF_SCORE_SUBMIT};
            String where_Clasuse=ScoreStorageHelper.UID + " = ?  And "+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+ " = ? ";
            String[] where_args={UID,p_date};
           Cursor cursor=sqLiteDatabase.query(ScoreStorageHelper.TABLE_NAME,Columns,where_Clasuse,where_args,null,null,null);
            //Cursor cursor=sqLiteDatabase.rawQuery("select "+p_date+" from "+ScoreStorageHelper.TABLE_NAME + " where "+ScoreStorageHelper.UID+ "= ? AND "+ ScoreStorageHelper.DATE_OF_SCORE_SUBMIT + " = ?",new String[]{ UID,p_date});

            return cursor;
        }
//arguement need to added i.e last date need to be send as parameter to find second last login date

        public Cursor get_sec_last_login(String UID, SQLiteDatabase sqLiteDatabase) {

            String[] Columns={"max("+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+")"};
            //select strftime("%Y-%m-%d","now")
            String year="\"%Y";
            String month="%m";
            String day="%d\"";
            String getToday="\"now\"";
            String q_sub="(select strftime("+year+"-"+month+"-"+day+","+getToday+")";
            String where_Clasuse=ScoreStorageHelper.UID + " = ? And "+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+" != ?";
            String[] where_args={UID,q_sub};
           //Cursor cursor=sqLiteDatabase.query(ScoreStorageHelper.TABLE_NAME,Columns,where_Clasuse,where_args,null,null,null);
          //** Cursor cursor=sqLiteDatabase.rawQuery("select max(Date_Score_submit) from Score_details where _id="+UID+" and (select Date_Score_submit from Score_details where "+ScoreStorageHelper.UID +"= "+UID+")!=(select strftime(\"%Y-%m-%d\",\"now\"))",null);
            //Cursor cursor=sqLiteDatabase.rawQuery("select max(Date_Score_submit) from Score_details where "+ScoreStorageHelper.UID +"= "+UID+" and (select max(Date_Score_submit) from Score_details where "+ScoreStorageHelper.UID +"= "+UID+" and "+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+"!= "+last_date+");",null);
Cursor cursor=sqLiteDatabase.rawQuery("select max("+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+") from Score_details where "+ScoreStorageHelper.UID+"="+UID+" and "+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+" < (select max("+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+") from Score_details);",null);
            return cursor;
        }

        public Cursor get_no_days_score_NotEntered(String UID,String p_date,SQLiteDatabase sqLiteDatabase)
        {
            int a;
            String[] Columns={"round(julianday('now') - julianday(max("+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+")))"};
            String where_Clasuse=ScoreStorageHelper.UID + " = ?  And "+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+ " = ? ";
            String[] where_args={UID,p_date};
            String getToday="\"now\"";
           // Cursor cursor=sqLiteDatabase.query(ScoreStorageHelper.TABLE_NAME,Columns,where_Clasuse,where_args,null,null,null);
          Cursor cursor=sqLiteDatabase.rawQuery("SELECT round(julianday('now') - julianday(max("+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+")))  from Score_details where _id="+UID+" and (select max("+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+") from Score_details) <= strftime(\"%Y-%m-%d\",\"now\");",null);
           // SELECT round(julianday('now') - julianday(max(Date_Score_submit)))  from Score_details where _id="1" and (select max(Date_Score_submit) from Score_details) <= strftime("%Y-%m-%d","now")
            StringBuffer buffer = new StringBuffer();
            while (cursor.moveToNext()) {
                a= cursor.getInt(0);
                buffer.append(a);
            }

           // return Integer.parseInt(buffer.toString());
            return cursor;
        }

        public Cursor get_last_login(String UID, SQLiteDatabase sqLiteDatabase) {

            String[] Columns={"max("+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+")"};
            //select strftime("%Y-%m-%d","now")
            String year="\"%Y";
            String month="%m";
            String day="%d\"";
            String getToday="\"now\"";
            String q_sub="(select strftime("+year+"-"+month+"-"+day+","+getToday+")";
            String where_Clasuse=ScoreStorageHelper.UID + " = ? And "+ScoreStorageHelper.DATE_OF_SCORE_SUBMIT+" == ?";
            String[] where_args={UID,q_sub};
           /* String result=check_leap_year(UID,sqLiteDatabase);
            if(result.equals("leap"))
            {

            }*/
            //Cursor cursor=sqLiteDatabase.query(ScoreStorageHelper.TABLE_NAME,Columns,where_Clasuse,where_args,null,null,null);
            Cursor cursor=sqLiteDatabase.rawQuery("select max(Date_Score_submit) from Score_details where "+ScoreStorageHelper.UID +"= "+UID+" and (select max(Date_Score_submit) from Score_details where "+ScoreStorageHelper.UID +"= "+UID+")<=(select strftime(\"%Y-%m-%d\",\"now\"));",null);
            return cursor;
        }
    }
}
