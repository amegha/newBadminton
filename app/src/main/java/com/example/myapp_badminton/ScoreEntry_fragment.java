package com.example.myapp_badminton;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreEntry_fragment extends Fragment {
    FrameLayout frameLayout;
    String date,username;
    ScoreStorageAdapter sHelper;
    SQLiteDatabase db1,db;
    Cursor cursor,cursor_lastDate,cursor_sec_last_login,cursor_usertype,cursor_days_not_entered;
    MyDbAdapter helper;
    String savedID,sec_lastDate,last_date,today,userType,pending_day;
    int x;
    AlertDialog.Builder alertbuilder;
    public ScoreEntry_fragment(String uname) {
        this.username = uname;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_entry, container, false);
        // Inflate the layout for this fragment
        frameLayout = view.findViewById(R.id.frame);
        alertbuilder = new AlertDialog.Builder(this.getActivity());
        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        today = sdf.format(date1);


        Intent intent1 = getActivity().getIntent();
        Bundle b = intent1.getExtras();
        date = b.getString("date");
        username = b.getString("x");


        helper = new MyDbAdapter(getActivity().getApplicationContext());
        db = helper.myhelper.getReadableDatabase();
        sHelper = new ScoreStorageAdapter(this.getContext());
        db1 = sHelper.scoreStorageHelper.getReadableDatabase();

        cursor = helper.myhelper.getUID(username, db);

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.isAfterLast() == false) {
                savedID = cursor.getString(0);
            }
            cursor.moveToNext();
        }
        cursor.close();

        // to get last date of score sumission
        cursor_lastDate = sHelper.scoreStorageHelper.get_last_login(savedID, db1);
        if (cursor_lastDate != null) {
            cursor_lastDate.moveToFirst();
            if (cursor_lastDate.isAfterLast() == false) {
                last_date = cursor_lastDate.getString(0);

            }
            cursor_lastDate.moveToNext();
        }
        cursor_lastDate.close();


        //to get second last score submission date
        cursor_sec_last_login = sHelper.scoreStorageHelper.get_sec_last_login(savedID, db1);

        if (cursor_sec_last_login != null) {

            cursor_sec_last_login.moveToFirst();
            if (cursor_sec_last_login.isAfterLast() == false) {
                sec_lastDate = cursor_sec_last_login.getString(0);

            }
            cursor_sec_last_login.moveToNext();
        }
        cursor_sec_last_login.close();


// to get number of days player not entered score
        cursor_days_not_entered=sHelper.scoreStorageHelper.get_no_days_score_NotEntered(savedID,last_date,db1);
        if (cursor_days_not_entered != null) {

            cursor_days_not_entered.moveToFirst();
            if (cursor_days_not_entered.isAfterLast() == false) {
                x = cursor_days_not_entered.getInt(0);

            }
            cursor_days_not_entered.moveToNext();
        }
        cursor_days_not_entered.close();









//       for first time login
        if(last_date==null && sec_lastDate==null && x==0)
        {
            Bundle bundle=new Bundle();
            bundle.putString("date",today);
            bundle.putString("x",username);
            Intent intent = new Intent(this.getActivity(), Score_From.class).putExtras(bundle);
           startActivity(intent);

        }



        if(last_date !=null && sec_lastDate == null && last_date!=sec_lastDate && last_date == today)
        {

            Toast.makeText(getActivity(), "already Entered today Score!,Wait for Tomorrow......", Toast.LENGTH_SHORT).show();
            Bundle bundle=new Bundle();
            bundle.putString("date",today);
            bundle.putString("x",username);

        }

        else if (last_date != null && sec_lastDate != null && !last_date.equals(sec_lastDate) && last_date.equals(today)) {

            Toast.makeText(this.getActivity(), "already Entered today Score!,Wait for Tomorrow......", Toast.LENGTH_SHORT).show();
            Bundle bundle=new Bundle();
            bundle.putString("date",today);
            bundle.putString("x",username);
        }
        // if user missed session and score entry pending
        else if (last_date != null && sec_lastDate != null && !last_date.equals(today)) {

            if (x > 0 || x != 0) {

                pending_day = getNextDate(last_date);
                if (!pending_day.equals(today)) {
                    alertbuilder.setMessage("Please Enter Score on " + pending_day + " Session.....")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("date", today);
                                   bundle.putString("x", username);
                                    Intent intent = new Intent(getActivity(),Score_From.class).putExtras(bundle);
                                   startActivity(intent);

                                }
                            });
                    AlertDialog alert = alertbuilder.create();
                    //Setting the title manually
                    alert.setTitle("Missed Session Entry Alert!");
                    alert.show();

                }
                //already loged in,entering todays score
                else if(pending_day.equals(today) )
                {

                    Bundle bundle=new Bundle();
                    bundle.putString("date",today);
                    bundle.putString("x",username);
                    Intent intent = new Intent(getActivity(),Score_From.class).putExtras(bundle);
                    startActivity(intent);

                }


            }
        }else if (x == 0 && last_date != null && sec_lastDate == null && last_date != sec_lastDate && last_date.equals(today)) {
            Bundle bundle=new Bundle();
            bundle.putString("date",today);
            bundle.putString("x",username);
            Toast.makeText(this.getActivity(), "already Entered today Score!,Wait for Tomorrow......", Toast.LENGTH_SHORT).show();


        }
        // only one entry is present
        else if(x!=0 && last_date!=null && sec_lastDate==null && last_date!=sec_lastDate && !last_date.equals(today))
        {
            Bundle b1 = new Bundle();
            String next_date=getNextDate(last_date);
            b1.putString("x", username);
            b1.putString("prev_date", next_date);
            Intent intent = new Intent(this.getActivity(), Score_From.class).putExtras(b1);
            startActivity(intent);
        }
        else {
            //check3
            Bundle b1 = new Bundle();
            String next_date=getNextDate(last_date);
            b1.putString("x", username);
            b1.putString("prev_date", next_date);
            Intent intent = new Intent(this.getActivity(), Score_From.class).putExtras(b1);
            startActivity(intent);
        }
            return view;

    }
    private String getNextDate(String inputDate){
        //inputDate = "2015-12-15"; // for example
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
        try {

            Date date = format.parse(inputDate);

            Calendar c = Calendar.getInstance();
            c.setTime(date);

            c.add(Calendar.MONTH, 2);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.DATE, -1);

            Date lastDayOfMonth = c.getTime();
            int month=c.get(Calendar.MONTH)+1;
            int years=c.get(Calendar.YEAR);
            int day=c.get(Calendar.DAY_OF_MONTH);

            if(inputDate.equals(format.format(lastDayOfMonth)))
            {
                c.add(Calendar.MONTH, 1);
                c.set(Calendar.DAY_OF_MONTH, 0);
                c.add(Calendar.DATE, 1);
                Date FirstDayOfMonth = c.getTime();
                inputDate=format.format(FirstDayOfMonth);
                return inputDate;

            }
            else {
                c.setTime(date);
                c.add(Calendar.DATE, +1);
                Date s1=c.getTime();
               /* int months=c.get(Calendar.MONTH)+1;
                int yearss=c.get(Calendar.YEAR);
                int days=c.get(Calendar.DAY_OF_MONTH);*/
                inputDate=format.format(c.getTime());
                Log.d("asd", "selected date : " + inputDate);
                return inputDate;

            }

            // System.out.println(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            inputDate ="";
        }
        return inputDate;
    }

}
