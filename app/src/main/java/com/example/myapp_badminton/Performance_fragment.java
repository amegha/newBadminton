package com.example.myapp_badminton;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Performance_fragment extends Fragment {

    String username,userid,utype,lastdate,score,scoreFilter,pCoach;
    public Performance_fragment(String name,String id,String type,String scoreFilter) {
        // Required empty public constructor
        this.username=name;
        this.userid=id;
        this.utype=type;
        this.scoreFilter=scoreFilter;
    }
    public Performance_fragment(String name,String id,String type,String lastdate,String score,String scoreFilter) {
        // Required empty public constructor
        this.username=name;
        this.userid=id;
        this.utype=type;
        this.lastdate=lastdate;
        this.score=score;
        this.scoreFilter=scoreFilter;
    }

    public Performance_fragment(String name, String id, String type, String lastdate, String score, String scoreFilter, String pCoach) {
        this.username=name;
        this.userid=id;
        this.utype=type;
        this.lastdate=lastdate;
        this.score=score;
        this.scoreFilter=scoreFilter;
        this.pCoach=pCoach;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_performance_fragment, container, false);
    /*    Intent intent1=getActivity().getIntent();
        Bundle b=intent1.getExtras();

        //username=b.getString("x");
        username = b.getString("Name");
        userid=b.getString("Id");
        utype=b.getString("type");*/




        if(utype.equalsIgnoreCase("Coach")){
            Bundle b1=new Bundle();
            b1.putString("x", username);
            b1.putString("id",userid);
            b1.putString("type",utype);
            b1.putString("scoreFilter",scoreFilter);
            b1.putString("Module","LineGraph");
            Intent intentCoach = new Intent(this.getActivity(), Coach.class).putExtras(b1);
            startActivity(intentCoach);
        }
        else if(utype.equalsIgnoreCase("Player")){
            Bundle bundle1=new Bundle();
            bundle1.putString("Pname",username);
            bundle1.putString("Pid",userid);
            bundle1.putString("type",utype);
            bundle1.putString("lastScoreDate",lastdate);
            bundle1.putString("ScoreLast",score);
            bundle1.putString("scoreFilter",scoreFilter);
            bundle1.putString("pCoach",pCoach);
            Intent intentPlayer = new Intent(this.getActivity(), PlayerPerformance.class).putExtras(bundle1).putExtra("From",bundle1);
            startActivity(intentPlayer);
        }


        return view;
    }
}