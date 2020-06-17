package com.example.myapp_badminton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements UsersAdapter.SelectedUser{

    UsersAdapter usersAdapter;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<UserModel> userModelList=new ArrayList<>();
    public String value,date;
    public String MainCategory_name;
    public String cid,coach_name,coachdate,level,academy,playerName,playerId,aid;
    public byte[] imagebytes;
    String playerImage;


    //players part
    String type,PName,PId,lastScoreEntryDate,Score;
    /**
     * Initializing variables to fetch from databases category
     */
    public String[] Fitness={};
    public String[] Grip={};
    public String[] On_court_Skill_strokes_singles={};
    public String[] On_court_Skill_strokes_doubles={};
    public String[] On_court_Skill_shadow_foorwork={};


    //String[] Fitness={" Agility (indoor)","Strength","Power","Speed","Stamina(Endurance)","Core-Strength /Stability","Reaction /Reflex Training","Flexibility"};
    // String[] Grip={" Forehand Forward","Forehand Flank","Forehand Back-Court","Backhand Tap","Backhand Edge/Back-Court","Backhand Keep","Neutral"};

    //String[] On_court_Skill_shadow_foorwork={"Forehand Net Approach","Forehand Net Stroke-Execution","Forehand Net Recovery"," Backhand Net","Forehand Flank","Backhand Flank","Forehand SideCourt(Middle)","Backhand SideCourt(Middle)","Overhead /Around the Head","Centre Positioning/Transition"};
    // String[] On_court_Skill_strokes_doubles={"Defense Block","Defense Drive/Counter","Defense Clear/Lift","Offence(BackCourt) Smash","Offence(BackCourt) Half-Smash","Offence(BackCourt) Drop","Offence(MidCourt) Smash","Offence(MidCourt) Parallel-Drive","Offence(MidCourt) Drop/Slice","Offence(FrontCourt) Push","Offence(FrontCourt) Attack","Offence(FrontCourt) Blocks/Interception","Service Short","Service Flick","Service Receiving Short","Service Receiving Flick","Net Play"};
    //  String[] On_court_Skill_strokes_singles={"Forehand Net Defensive Keep/Block","Forehand Net Lift/Clear","Forehand Net Flick/Push","Forehand Net Dribble","Forehand Net Cross/Netshot",  "Forehand Net Tap", "Backhand Net Defensive Keep/Block", "Backhand Net Lift/Clear", "Backhand Net Flick/Push", "Backhand Net Dribble", "Backhand Net Cross/Netshot", "Backhand Net Tap", "Forehand Flank Toss/ Clear(Defensive/Attacking)", "Forehand Flank Drop", "Forehand Flank Half-Smash", "Forehand Flank Smash", "Forehand Flank Reverse Slice", "Forehand Flank Defensive Drive(Low)", "Backhand Flank Toss/ Clear(Defensive/Attacking)", "Backhand Flank Drop", "Backhand Flank Half-Smash", "Backhand Flank Smash", "Backhand Flank Reverse Slice", "Backhand Flank Defensive Drive(Low)", "Forehand Side-Court(Middle)Defensive-Block/Straight/Cross", " Forehand Side-Court(Middle)Counter-Drive", " Forehand Side-Court(Middle)Attacking-Strokes", " Backhand Side-Court(MiddleDefensive-Block/Straight/Cross)", " Backhand Side-Court(Middle)Counter-Drive", " Backhand Side-Court(Middle)Attacking-Strokes", " Overhead/Around the head Toss/Clear", " Overhead/Around the head Drop", "Overhead/Around the head Half-Smash", "Overhead/Around the head Smash", "Overhead/Around the head Reverse-Slice", "Service Forehand-Short", "Service Forehand Flick", "Service Forehand-High", "Service Backhand Short", "Service Backhand Flick"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);ActivityTracker.writeActitivtyLogs(this.getLocalClassName());
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.toolbar);

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        if (type.equalsIgnoreCase("Coach")) {
            value = bundle.getString("y");
            date = bundle.getString("date_value");

            cid = bundle.getString("coach_id");
            coach_name = bundle.getString("coachname");
            coachdate = bundle.getString("date");
            level = bundle.getString("level");
            academy = bundle.getString("Academy");
            imagebytes = bundle.getByteArray("ImageBytes");
            playerName = bundle.getString("PlayerName");
            playerId = bundle.getString("playerId");
            aid=bundle.getString("aid");
            MainCategory_name = bundle.getString("check_id");

        }
        else{
        //player Part
        Intent playerIntent = getIntent();
        Bundle playerBundle = playerIntent.getExtras();
        playerName = playerBundle.getString("Pname");
        playerId = playerBundle.getString("Pid");
        type = playerBundle.getString("type");
        lastScoreEntryDate = playerBundle.getString("lastScoreDate");
        Score = playerBundle.getString("ScoreLast");
        playerImage=playerBundle.getString("Image");
        MainCategory_name = playerBundle.getString("check_id");
    }

        if(bundle.getString("check_id").equals("Fitness")){
            DatabaseAccess databaseAccess1=DatabaseAccess.getInstance(getApplicationContext());
            databaseAccess1.open();

            Fitness=databaseAccess1.getFitness();
            for(String s:Fitness)
            {
                UserModel userModel=new UserModel(s);
                userModelList.add(userModel);
            }
            databaseAccess1.close();


        }
        else if(bundle.getString("check_id").equals("Grip"))
        {  DatabaseAccess databaseAccess1=DatabaseAccess.getInstance(getApplicationContext());
            databaseAccess1.open();

            Grip=databaseAccess1.getGrip();
            for(String s:Grip)
            {
                UserModel userModel=new UserModel(s);
                userModelList.add(userModel);
            }
            databaseAccess1.close();
        }
        else if(bundle.getString("check_id").equals("Singles"))
        {
            DatabaseAccess databaseAccess1=DatabaseAccess.getInstance(getApplicationContext());
            databaseAccess1.open();
            On_court_Skill_strokes_singles=databaseAccess1.getOnCourtSkill(1);

            for(String s:On_court_Skill_strokes_singles)
            {
                UserModel userModel=new UserModel(s);
                userModelList.add(userModel);
            }
            databaseAccess1.close();

        }
        else if(bundle.getString("check_id").equals("Doubles"))
        {

            DatabaseAccess databaseAccess1=DatabaseAccess.getInstance(getApplicationContext());
            databaseAccess1.open();

            On_court_Skill_strokes_doubles=databaseAccess1.getOnCourtSkill(2);
            for(String s:On_court_Skill_strokes_doubles)
            {
                UserModel userModel=new UserModel(s);
                userModelList.add(userModel);
            }
            databaseAccess1.close();
        }
        else if(bundle.getString("check_id").equals("Shadow/Footwork"))
        {
            DatabaseAccess databaseAccess1=DatabaseAccess.getInstance(getApplicationContext());
            databaseAccess1.open();

            On_court_Skill_shadow_foorwork=databaseAccess1.getOnCourtSkill(3);
            for(String s: On_court_Skill_shadow_foorwork)
            {
                UserModel userModel=new UserModel(s);
                userModelList.add(userModel);
            }
            databaseAccess1.close();
        }
//use to pass array of data
   /*     for(String s:names)
        {
            UserModel userModel=new UserModel(s);

            userModelList.add(userModel);
        }*/
        usersAdapter=new UsersAdapter(userModelList,this);
        recyclerView.setAdapter(usersAdapter);

    }



    //for CoachScore Entry
        @Override
    public void selectedUser(UserModel userModel) {
        Bundle bundle=new Bundle();
            bundle.putSerializable("data", userModel);
        if(type.equalsIgnoreCase("Coach")) {
            bundle.putString("z", value);

            bundle.putString("coach_id", cid);
            bundle.putString("coachname", coach_name);
            bundle.putString("date", coachdate);
            bundle.putString("level", level);
            bundle.putString("Academy", academy);
            bundle.putString("PlayerName", playerName);
            bundle.putString("playerId", playerId);
            bundle.putByteArray("ImageBytes", imagebytes);
            bundle.putString("type",type);
            bundle.putString("aid",aid);
            if (MainCategory_name.equals("Singles") || MainCategory_name.equals("Doubles") || MainCategory_name.equals("Shadow/Footwork")) {
                MainCategory_name = "On Court Skills => " + MainCategory_name;
            } else {
                MainCategory_name = MainCategory_name + "";
            }
            bundle.putString("main_category", MainCategory_name);
            bundle.putString("date", coachdate);

            Intent i = new Intent(SearchActivity.this, SelectedUserActivity.class).putExtras(bundle);
            startActivity(i);
        }
        else
        {
            Bundle bundle1=new Bundle();
            bundle1.putString("Pname",playerName);
            bundle1.putString("Pid",playerId);
            bundle1.putString("type",type);
            bundle1.putString("lastScoreDate",lastScoreEntryDate);
            bundle1.putString("ScoreLast",Score);
            bundle1.putString("Image",playerImage);

            if(MainCategory_name.equals("Singles") || MainCategory_name.equals("Doubles") || MainCategory_name.equals("Shadow/Footwork"))
            {
                MainCategory_name="On Court Skills => "+MainCategory_name;
            }
            else
            {
                MainCategory_name=MainCategory_name+"";
            }
            bundle1.putString("main_category",MainCategory_name);
            bundle1.putSerializable("data", userModel);
            Intent i=new Intent(SearchActivity.this,SelectedUserActivity.class).putExtras(bundle1);
            startActivity(i);
        }
    }

 /*
 for student score entry
  @Override
    public void selectedUser(UserModel userModel) {
        Bundle bundle=new Bundle();
        bundle.putString("z",value);
        bundle.putSerializable("data",userModel);


        if(MainCategory_name.equals("Singles") || MainCategory_name.equals("Doubles") || MainCategory_name.equals("Shadow/Footwork"))
        {
            MainCategory_name="On Court Skills => "+MainCategory_name;
        }
        else
        {
            MainCategory_name=MainCategory_name+"";
        }
        bundle.putString("main_category",MainCategory_name);
        bundle.putString("date",date);

//        bundle.putString("data",userModel);
        Intent i=new Intent(SearchActivity.this,SelectedUserActivity.class).putExtras(bundle);
        //Intent i=new Intent(SearchActivity.this,SelectedUserActivity.class).putExtra("data",userModel);
        startActivity(i);
       *//* Intent j=new Intent(SearchActivity.this,SelectedUserActivity.class).putExtra("z",value).putExtra("from","Login");
        startActivity(j);*//*
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search_view);

        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                usersAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id == R.id.search_view){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
