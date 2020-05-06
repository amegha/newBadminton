package com.example.myapp_badminton;

/**
 * updated by Pooja_16/01/2020
 * This file is used to select training category
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;


public class Score_From extends AppCompatActivity  {

    public RadioButton fitness, grip, oncourt_skill,singles_oncourt,doubles_oncourt,shadow_oncourt;
  //  Spinner academyName;
    public String value;
    public LinearLayout linearLayout;
    String today,yesterday,pass_date,name,userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_from);
        fitness = findViewById(R.id.cat1);
        grip = findViewById(R.id.cat2);
        oncourt_skill = findViewById(R.id.cat3);

        singles_oncourt=findViewById(R.id.singles);
        doubles_oncourt=findViewById(R.id.doubles);
        shadow_oncourt=findViewById(R.id.shadow_footwork);


        //academyName=findViewById(R.id.spinner1);
        linearLayout=findViewById(R.id.rg2);


        fitness.setChecked(false);
        grip.setChecked(false);
        oncourt_skill.setChecked(false);
       // academyName.setOnItemSelectedListener(this);

   Intent i=getIntent();
   Bundle b=i.getExtras();

    today=b.getString("date");




    Intent intent=getIntent();
    Bundle bundle=intent.getExtras();
    yesterday=bundle.getString("prev_date");

        if(today!=null && yesterday==null)
        {
            pass_date=today;
            value=b.getString("x");
        }

   else if(yesterday!=null && today==null ){
        pass_date=yesterday;
        value=b.getString("uname");

    }



    //passData();
    }

    public void fitness_select(View view) {
        fitness.setChecked(true);
        grip.setChecked(false);
        oncourt_skill.setChecked(false);
        linearLayout.setVisibility(View.GONE);
       // academyName.setVisibility(View.GONE);
        startActivity(new Intent(Score_From.this,SearchActivity.class).putExtra("check_id","Fitness").putExtra("y",value).putExtra("date_value",pass_date));

    }

    public void grip_select(View view) {
        grip.setChecked(true);
        fitness.setChecked(false);
        oncourt_skill.setChecked(false);
        linearLayout.setVisibility(View.GONE);
        //academyName.setVisibility(View.GONE);
        startActivity(new Intent(Score_From.this,SearchActivity.class).putExtra("check_id","Grip").putExtra("y",value).putExtra("date_value",pass_date));
    }

    public void oncourt_skill_select(View view) {
        oncourt_skill.setChecked(true);
        fitness.setChecked(false);
        grip.setChecked(false);
        linearLayout.setVisibility(View.VISIBLE);
       // academyName.setVisibility(View.VISIBLE);


    }

    public void singles_select(View view) {
        oncourt_skill.setChecked(true);
        linearLayout.setVisibility(View.VISIBLE);
        fitness.setChecked(false);
        grip.setChecked(false);
        singles_oncourt.setChecked(true);
        doubles_oncourt.setChecked(false);
        shadow_oncourt.setChecked(false);

       // academyName.setVisibility(View.VISIBLE);
        startActivity(new Intent(Score_From.this,SearchActivity.class).putExtra("check_id","Singles").putExtra("y",value).putExtra("date_value",pass_date));


    }

    public void doubles_select(View view) {
        oncourt_skill.setChecked(true);
        linearLayout.setVisibility(View.VISIBLE);
        fitness.setChecked(false);
        grip.setChecked(false);
        singles_oncourt.setChecked(false);
        doubles_oncourt.setChecked(true);
        shadow_oncourt.setChecked(false);
       // academyName.setVisibility(View.VISIBLE);
        startActivity(new Intent(Score_From.this,SearchActivity.class).putExtra("check_id","Doubles").putExtra("y",value).putExtra("date_value",pass_date));


    }

    public void shadow_select(View view) {
        oncourt_skill.setChecked(true);
        linearLayout.setVisibility(View.VISIBLE);
        fitness.setChecked(false);
        grip.setChecked(false);
        singles_oncourt.setChecked(false);
        doubles_oncourt.setChecked(false);
        shadow_oncourt.setChecked(true);
       // academyName.setVisibility(View.VISIBLE);
        startActivity(new Intent(Score_From.this,SearchActivity.class).putExtra("check_id","Shadow/Footwork").putExtra("y",value).putExtra("date_value",pass_date));


    }
}
