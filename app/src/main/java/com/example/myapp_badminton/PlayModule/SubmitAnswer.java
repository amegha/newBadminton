package com.example.myapp_badminton.PlayModule;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapp_badminton.ActivityTracker;
import com.example.myapp_badminton.R;
import com.google.android.material.tabs.TabLayout;

/*import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;*/

/**
 * Created by megha on 10/7/19.
 */

public class SubmitAnswer extends AppCompatActivity {
    public static final String PREFS_NAME = "LoginPrefs";
    static String answerLoc, answerType;
    ViewPager viewPager;
    int currentPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        ActivityTracker.writeActivityLogs(this.getLocalClassName(), settings.getString("Id", ""), getApplicationContext());
        setContentView(R.layout.activity_submit_answer);
        viewPager = findViewById(R.id.pager);

        answerType = null;
        answerLoc = null;
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Shot Loc"));
        tabLayout.addTab(tabLayout.newTab().setText("Shot Type"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(currentPos = tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(currentPos = tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public void outLeft(View view) {
        Toast.makeText(this, "Out left clicked!", Toast.LENGTH_SHORT).show();
        answerLoc = "Out left";
        viewPager.setCurrentItem(2);
        //        viewPager.setCurrentItem(2); automatically switches to shotType fragment


    }

    public void outRight(View view) {
        Toast.makeText(this, "Out right clicked!", Toast.LENGTH_SHORT).show();
        answerLoc = "Out right";
        viewPager.setCurrentItem(2);

    }


    public void topLeft(View view) {
        Toast.makeText(this, "Top left clicked!", Toast.LENGTH_SHORT).show();
        answerLoc = "Top left";
        viewPager.setCurrentItem(2);

    }

    public void topMiddle(View view) {
        Toast.makeText(this, "Top middle clicked!", Toast.LENGTH_SHORT).show();
        answerLoc = "Top middle";
        viewPager.setCurrentItem(2);

    }

    public void topRight(View view) {
        Toast.makeText(this, "Top right clicked!", Toast.LENGTH_SHORT).show();
        answerLoc = "Top right";
        viewPager.setCurrentItem(2);

    }

    public void middleLeft(View view) {
        Toast.makeText(this, "Middle left clicked!", Toast.LENGTH_SHORT).show();
        answerLoc = "Middle left";
        viewPager.setCurrentItem(2);

    }

    public void middleMiddle(View view) {
        Toast.makeText(this, "Middle middle clicked!", Toast.LENGTH_SHORT).show();
        answerLoc = "Middle middle";
        viewPager.setCurrentItem(2);

    }

    public void middleRight(View view) {
        Toast.makeText(this, "Middle right clicked!", Toast.LENGTH_SHORT).show();
        answerLoc = "Middle right";
        viewPager.setCurrentItem(2);

    }

    public void bottomLeft(View view) {
        Toast.makeText(this, "Bottom left clicked!", Toast.LENGTH_SHORT).show();
        answerLoc = "Bottom left";
        viewPager.setCurrentItem(2);

    }

    public void bottomMiddle(View view) {
        Toast.makeText(this, "Bottom middle clicked!", Toast.LENGTH_SHORT).show();
        answerLoc = "Bottom middle";
        viewPager.setCurrentItem(2);

    }

    public void bottomRight(View view) {
        Toast.makeText(this, "Bottom right clicked!", Toast.LENGTH_SHORT).show();
        answerLoc = "Bottom right";
        viewPager.setCurrentItem(2);

    }

    public void outNet(View view) {
        Toast.makeText(this, "Out net clicked!", Toast.LENGTH_SHORT).show();
        answerLoc = " Out Net";
        viewPager.setCurrentItem(2);
    }

    //shot type
    public void spinNet(View view) {
        Toast.makeText(this, "Spin net clicked!", Toast.LENGTH_SHORT).show();
        answerType = "spinNet";
    }

    public void slice(View view) {
        Toast.makeText(this, "Slice clicked!", Toast.LENGTH_SHORT).show();
        answerType = "slice";
    }

    public void crossCourtBlock(View view) {
        Toast.makeText(this, "Cross court block clicked!", Toast.LENGTH_SHORT).show();
        answerType = "crossCourtBlock";
    }

    public void straightBlock(View view) {
        Toast.makeText(this, "Straight block clicked!", Toast.LENGTH_SHORT).show();
        answerType = "straightBlock";
    }

    /*public void slice(View view) {
        Toast.makeText(this, "slice clicked!", Toast.LENGTH_SHORT).show();
        answerType = "Block";
    }*/

    public void longDefence(View view) {
        Toast.makeText(this, "Long defence clicked!", Toast.LENGTH_SHORT).show();
        answerType = "longDefence";
    }

    public void driveDefence(View view) {
        Toast.makeText(this, "Drive defence clicked!", Toast.LENGTH_SHORT).show();
        answerType = "driveDefence";
    }

    public void submit(View view) {
        if (answerType != null) {
            final Intent intent = new Intent();
            intent.putExtra("shot_location", answerLoc);
            intent.putExtra("shot_type", answerType);
            new AlertDialog.Builder(this)
                    .setTitle("Confirm!!")
                    .setMessage(this.getResources().getString((R.string.confirm_answer)) + "\nLocation is: " + answerLoc + "\nShot type is: " + answerType)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Confirm ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    }).show();

        } else {
            Toast.makeText(this, "select shot type!!", Toast.LENGTH_SHORT).show();
        }

    }

    public void smash(View view) {
        Toast.makeText(this, "Smash clicked!", Toast.LENGTH_SHORT).show();
        answerType = "smash";
    }

    public void crossCourtNet(View view) {
        Toast.makeText(this, "Cross court net clicked!", Toast.LENGTH_SHORT).show();
        answerType = "crossCourtNet";
    }

    public void reverseSlice(View view) {
        Toast.makeText(this, "Reverse slice clicked!", Toast.LENGTH_SHORT).show();
        answerType = "reverseSlice";

    }

    public void straightSlice(View view) {
        Toast.makeText(this, "Straight slice clicked!", Toast.LENGTH_SHORT).show();
        answerType = "straightSlice";
    }


}



