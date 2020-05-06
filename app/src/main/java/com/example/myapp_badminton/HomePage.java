package com.example.myapp_badminton;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
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

public class HomePage extends AppCompatActivity {
    DrawerLayout dLayout;
    String date, uname;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);// get the reference of Toolbar

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //  righ now commen*/
//        date = bundle.getString("date");
//        uname = bundle.getString("x");

        setSupportActionBar(toolbar);
        // setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar

// implement setNavigationOnClickListener event
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });
        setNavigationDrawer();
    }


    private void setNavigationDrawer() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) findViewById(R.id.navigation); // initiate a Navigation View
// implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
// check selected menu item's id and replace a Fragment Accordingly
                if (itemId == R.id.first) {
                    frag = new ScoreEntry_fragment(uname);
                } else if (itemId == R.id.second) {
                    frag = new Performance_fragment(uname);
                } else if (itemId == R.id.third) {
                    frag = new ScoreObtained_fragment(uname);
                } else if (itemId == R.id.four) {
                    frag = new ForgotPasswordFragment();
                } else if (itemId == R.id.five) {
                    startActivity(new Intent(getApplicationContext(), PlayVideo.class));
                }
                //added on 08/04/2020
                else if (itemId == R.id.six)//refer to about
                {
                    frag = new About();
                } else if (itemId == R.id.seven)//refer to signout
                {
                    frag = new Signout();
                }
// display a toast message with menu item's title
                //  Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onBackPressed() {

        Intent i = new Intent(HomePage.this, Login.class);
        //used to or helps in display particular activity
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(i);
        //to avoid flickering or flipped screen or activity use finish() ,which is used to kill or complete the activity
        finish();

    }
}
