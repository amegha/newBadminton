package com.example.myapp_badminton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapp_badminton.megha.PlayVideo;
import com.google.android.material.navigation.NavigationView;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class HomePage extends AppCompatActivity {
    public static final String PREFS_NAME = "LoginPrefs";
    DrawerLayout dLayout;
    String date, uname, id, utype, lastScoreDate, Score, playerImage;

    //    String uname,id,utype,lastScoreDate,Score;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);// get the reference of Toolbar
        SharedPreferences shared = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userType = shared.getString("userType", "");
        Map<String, ?> userAll = shared.getAll();
        Log.e("HomePage", "onCreate:usertype " + userType);
        Log.e("HomePage", "onCreate:all " + Collections.singleton(userAll));
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });
        Bundle bcoach = intent.getExtras();

      /*  utype = bcoach.getString("type");
        if (utype.equals("coach")) {
            uname = bcoach.getString("Name");
            id = bcoach.getString("Id");
        } else {
            Intent intentp = getIntent();
            Bundle bplayer = intentp.getExtras();

            uname = bplayer.getString("Name");
            id = bplayer.getString("Id");
            lastScoreDate = bplayer.getString("DateLastScore");
            Score = bplayer.getString("lastScore");
            playerImage = bplayer.getString("Image");
        }*/

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        utype = settings.getString("type", "");
        if (utype.equals("coach")) {
            uname = settings.getString("Name", "");
            id = settings.getString("Id", "");
        } else {
            uname = settings.getString("Name", "");
            id = settings.getString("Id", "");
            Score = settings.getString("lastScore", "");
            lastScoreDate = settings.getString("DateLastScore", "");
            playerImage = settings.getString("image", "");
        }

        setNavigationDrawer();
    }


    private void setNavigationDrawer() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) findViewById(R.id.navigation); // initiate a Navigation View
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
                if (itemId == R.id.first) {
                    if (utype.equals("coach")) {
                        frag = new ScoreEntry_fragment(uname, id, utype);
                    } else {
                        frag = new ScoreEntry_fragment(uname, id, utype, lastScoreDate, Score, playerImage);
                    }
                } else if (itemId == R.id.second) {
                    if (utype.equalsIgnoreCase("Player")) {
                        frag = new Performance_fragment(uname, id, utype, lastScoreDate, Score);
                    } else {
                        frag = new Performance_fragment(uname, id, utype);
                    }

                } else if (itemId == R.id.third) {
                    if (utype.equalsIgnoreCase("Player")) {
                        frag = new ScoreObtained_fragment(uname, id, utype, lastScoreDate, Score);
                    } else {
                        frag = new ScoreObtained_fragment(uname, id, utype);
                    }
                } else if (itemId == R.id.four) {
                    frag = new ForgotPasswordFragment();
                } else if (itemId == R.id.five) {
                    startActivity(new Intent(getApplicationContext(), PlayVideo.class));
                } else if (itemId == R.id.six)//refer to about
                {
                    frag = new About();
                } else if (itemId == R.id.seven)//refer to signout
                {
                    showLogoutDialog();
                    //                    frag = new Signout();
                }
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
                    transaction.commit(); // commit the changes
                    dLayout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }
                return false;
            }
        });
    }

    private void showLogoutDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(HomePage.this);
        alert.setMessage("Are you sure?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("logged", "not");
                        editor.apply();
                        logout(); // Last step. Logout function

                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
        alert1.setCanceledOnTouchOutside(false);

    }

    private void logout() {
        startActivity(new Intent(this, Login.class));
        finish();
    }
}