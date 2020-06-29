package com.example.myapp_badminton;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreEntry_fragment extends Fragment {

    FrameLayout frameLayout;
    String date,username,userid,usertype;
    String imagePlayer;
    SQLiteDatabase db;
    Cursor cursor,cursor_lastDate,cursor_sec_last_login,cursor_usertype,cursor_days_not_entered;
    databaseConnectionAdapter dbcAdapter;
    String today,type,CId,CName,PName,PId,lastScoreEntryDate,Score;
    int x;
    AlertDialog.Builder alertbuilder;


    public ScoreEntry_fragment( String uname, String id, String utype) {
        this.username = uname;
        this.userid=id;
        this.usertype=utype;
        // Required empty public constructor
    }
    public ScoreEntry_fragment(String uname,String id,String utype,String lastDateScore,String score,String playerImage ) {
        this.username = uname;
        this.userid=id;
        this.usertype=utype;
        this.lastScoreEntryDate=lastDateScore;
        this.Score=score;
        this.imagePlayer=playerImage;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_entry_fragment, container, false);
        // Inflate the layout for this fragment
        frameLayout = view.findViewById(R.id.frame);
        alertbuilder = new AlertDialog.Builder(this.getActivity());
        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        today = sdf.format(date1);




        Intent intentCoach=getActivity().getIntent();
        Bundle bundleCoach=intentCoach.getExtras();

        /*type=bundleCoach.getString("type");
        CId=bundleCoach.getString("Id");
        CName=bundleCoach.getString("Name");*/

        Intent intentPlayer=getActivity().getIntent();
        Bundle bundlePlayer=intentPlayer.getExtras();

        /*type=bundlePlayer.getString("type");
        PId=bundlePlayer.getString("Id");
        PName=bundlePlayer.getString("Name");
        lastScoreEntryDate=bundlePlayer.getString("DateLastScore");
        Score=bundlePlayer.getString("lastScore");
        imagePlayer=bundlePlayer.getString("Image");*/
        if(usertype.equalsIgnoreCase("Coach")){
            Bundle bundle1=new Bundle();
            bundle1.putString("date",today);
            bundle1.putString("x",username);
            bundle1.putString("userid",userid);
            bundle1.putString("type",usertype);
            bundle1.putString("Module","ScoreEntry");
            Intent intent = new Intent(this.getActivity(), Coach.class).putExtras(bundle1);
            startActivity(intent);
            getActivity().getFragmentManager().popBackStack();

        }
        else if(usertype.equalsIgnoreCase("Player")){
            Bundle bundle1=new Bundle();
            bundle1.putString("Pname",username);
            bundle1.putString("Pid",userid);
            bundle1.putString("type",usertype);
            bundle1.putString("lastScoreDate",lastScoreEntryDate);
            bundle1.putString("ScoreLast",Score);
            bundle1.putString("Image",imagePlayer);
            Intent intent = new Intent(this.getActivity(), Score_From.class).putExtras(bundle1);
            startActivity(intent);
        }

return view;
    }


}
