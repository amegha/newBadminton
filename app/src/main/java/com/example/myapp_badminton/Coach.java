package com.example.myapp_badminton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//import static com.example.badmintonapp.R.layout.abc_popup_menu_header_item_layout;
//import static com.example.badmintonapp.R.layout.support_simple_spinner_dropdown_item;

public class Coach extends AppCompatActivity implements AsyncResponse{

    AutoCompleteTextView et_academy,et_level,et_city;
    String   Academy,aid,date,coach_id,coach_name,city,levels,type,fragment_module,graph_cid;
    Button click,btn_city,btn_academy;
    Cursor cursor;
    databaseConnectionAdapter datahelper;

    SQLiteDatabase db;

    public String academy[]={};
    ArrayAdapter<String> adapter_academy;
    ArrayAdapter<String> adapter_city;
    ArrayAdapter<String> adapter_levels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);ActivityTracker.writeActitivtyLogs(this.getLocalClassName());
        setContentView(R.layout.activity_coach);

        datahelper = new databaseConnectionAdapter(getApplicationContext());
        db=datahelper.allDataHelper.getReadableDatabase();



        et_academy=findViewById(R.id.academy_select_coach);
        et_level=findViewById(R.id.select_student_coach_level);
        et_city=findViewById(R.id.academy_select_city);


        levels=et_level.getText().toString();
        Academy=et_academy.getText().toString();
        city=et_city.getText().toString();
        Intent intent=getIntent();
        Bundle bundle1=intent.getExtras();
        date=bundle1.getString("date");
        coach_name= bundle1.getString("x");
        coach_id=bundle1.getString("userid");
        type=bundle1.getString("type");

        fragment_module=bundle1.getString("Module");
        if(fragment_module.equalsIgnoreCase("LineGraph") || fragment_module.equalsIgnoreCase("BarGraph")){
            coach_id=bundle1.getString("id");
        }
        else
        {
            coach_id=bundle1.getString("userid");
        }
        //requesting server for getting city,academy,level and other details
        new WebService(this).execute(API.ServerAddress + "get_academy.php", "module=coach&coach_id="+coach_id);

        et_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String passcity= adapter_city.getItem(position);
                adapter_academy=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,datahelper.allDataHelper.getAcademies(passcity));
                et_academy.setAdapter(adapter_academy);
                et_academy.setThreshold(1);
            }
        });

        et_academy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String passAcademy= adapter_academy.getItem(position);
                adapter_levels=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,datahelper.allDataHelper.getLevels(passAcademy));
                et_level.setAdapter(adapter_levels);
                et_level.setThreshold(1);
                aid=datahelper.allDataHelper.getAid(passAcademy);
            }
        });



        /**
         * Enabling Search Filter
         * */
        et_city.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Coach.this.adapter_city.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        /**
         * Enabling Search Filter
         * */
        et_academy.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Coach.this.adapter_academy.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        /**
         * Enabling Search Filter
         * */
        et_level.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Coach.this.adapter_levels.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }
    //used to get response from server
    @Override
    public void onTaskComplete(String result) {
        /* if(result.equals("Success")){*/
        Log.e("ViewUserDetails","Upload status "+result);
        String[] arrRes;
        arrRes = result.split(",");
        String locationXml;
//        Log.e("ViewUserDetails", " arrRes[0] " + arrRes[0] + " arrRes[1]  " +  arrRes[1] + "  arrRes[2]" + arrRes[2]);

        String [] academyResponse;
        ArrayList<String> cities=new ArrayList();
        ArrayList<String> academy_name=new ArrayList();
        ArrayList<String> aid=new ArrayList<>();
        ArrayList<String> gamelevels=new ArrayList<>();
        ArrayList<String> state=new ArrayList<>();
        ArrayList<String> area=new ArrayList<>();

        academyResponse = result.split(";");
        for (int i = 0; i < academyResponse.length; i++) {
            LocationInfo(academyResponse[i],cities,academy_name,aid,gamelevels,state,area);
            Log.d("Total Size", String.valueOf(academyResponse.length));
        }
        System.out.println("all the cities "+ Collections.singletonList(cities));
        System.out.println("all the Academy Ids "+ Collections.singletonList(aid));
        System.out.println("all the academy names " +Collections.singletonList(academy_name));
        //used to display city
        adapter_city=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,datahelper.allDataHelper.getCity());
        et_city.setAdapter(adapter_city);
        et_city.setThreshold(0);

    }
    private void LocationInfo(String s, ArrayList<String> cities, ArrayList<String> academy_name,ArrayList<String> aid,ArrayList<String> gamelevels,ArrayList<String> states,ArrayList<String> area) {
        String[] locInfo = s.split(",");
        Log.d("Total Location Size", String.valueOf(locInfo.length));
        aid.add(locInfo[0]);
        academy_name.add(locInfo[1]);
        states.add(locInfo[2]);
        cities.add(locInfo[3]);
        area.add(locInfo[4]);
        gamelevels.add(locInfo[5]);


        for (int i = 0; i < cities.size(); i++) {
            datahelper.allDataHelper.insertAcademyDatas(aid.get(i), academy_name.get(i), states.get(i), cities.get(i), area.get(i), gamelevels.get(i));

        }



    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent i = new Intent(Coach.this, HomePage.class);
        //used to or helps in display particular activity
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(i);


        //to avoid flickering or flipped screen or activity use finish() ,which is used to kill or complete the activity
        finish();

    }

    public void select_level(View view) {


        //used to pass data to display player class
        if (fragment_module.equalsIgnoreCase("ScoreEntry")) {
            Bundle b = new Bundle();
            b.putString("coach_id", coach_id);
            b.putString("coachname", coach_name);
            b.putString("date", date);
            b.putString("level", et_level.getText().toString());
            b.putString("Academy", et_academy.getText().toString());
            b.putString("type", type);
            b.putString("aid", aid);
            b.putString("module",fragment_module);
            Intent i1 = new Intent(Coach.this, DisplayPlayer.class).putExtras(b);
            startActivity(i1);
            datahelper.allDataHelper.delete();
        }
        else if(fragment_module.equalsIgnoreCase("LineGraph")){
            Bundle b = new Bundle();
            b.putString("coach_id", coach_id);
            b.putString("coachname", coach_name);
            b.putString("date", date);
            b.putString("level", et_level.getText().toString());
            b.putString("Academy", et_academy.getText().toString());
            b.putString("type", type);
            b.putString("aid", aid);
            b.putString("module",fragment_module);
            Intent i1 = new Intent(Coach.this, DisplayPlayer.class).putExtras(b);
            startActivity(i1);
            datahelper.allDataHelper.delete();
        }
        else if(fragment_module.equalsIgnoreCase("BarGraph")){
            Bundle b = new Bundle();
            b.putString("coach_id", coach_id);
            b.putString("coachname", coach_name);
            b.putString("date", date);
            b.putString("level", et_level.getText().toString());
            b.putString("Academy", et_academy.getText().toString());
            b.putString("type", type);
            b.putString("aid", aid);
            b.putString("module",fragment_module);
            Intent i1 = new Intent(Coach.this, DisplayPlayer.class).putExtras(b);
            startActivity(i1);
            datahelper.allDataHelper.delete();
        }
    }

}