package com.example.myapp_badminton;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapp_badminton.megha.PlayVideo;
import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity implements AsyncResponse {
    public static final String PREFS_NAME = "LoginPrefs";
    private static final int REQUEST_RUNTIME_PERMISSIONS = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    DrawerLayout dLayout;
    String date, uname, id, utype, lastScoreDate, Score, playerImage;
    AlertDialog alertDialog;
    String sNewPass, sNewPassConfirm, regEmail;
    private EditText newPass, confirmNewPass;

    //    String uname,id,utype,lastScoreDate,Score;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTracker.writeActitivtyLogs(this.getLocalClassName());
        setContentView(R.layout.activity_home_page);
        Log.e("onCreate: ", "get class() " + this.getLocalClassName());
        Toolbar toolbar = findViewById(R.id.toolbar);// get the reference of Toolbar
        SharedPreferences shared = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        verifyStoragePermissions(this);
       /* String userType = shared.getString("userType", "");
        Map<String, ?> userAll = shared.getAll();
        Log.e("HomePage", "onCreate:usertype " + userType);
        Log.e("HomePage", "onCreate:all " + Collections.singleton(userAll));
       */

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
            regEmail = settings.getString("mail_id", "");
        } else {
            uname = settings.getString("Name", "");
            id = settings.getString("Id", "");
            playerImage = settings.getString("image", "");
            regEmail = settings.getString("mail_id", "");
            new WebService(HomePage.this).execute(API.ServerAddress + API.AFTER_LOGIN, "user_id=" + id);

        }

    }

    public void verifyStoragePermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_RUNTIME_PERMISSIONS

            );
        }
    }

    private void setNavigationDrawer() {
        dLayout = findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = findViewById(R.id.navigation); // initiate a Navigation View
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
                    createResetPasswordAlertDialog();

//                    frag = new ForgotPasswordFragment();
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

    private void createResetPasswordAlertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.activity_reset_password, null);
        newPass = confirmDialog.findViewById(R.id.pass_new);
        confirmNewPass = confirmDialog.findViewById(R.id.pass_confirm);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        alertDialog = alert.create();
        alertDialog.show();
//        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void resetPasswordOrPin(View view) {
//        alertDialog.dismiss();
        sNewPass = newPass.getText().toString().trim();
        sNewPassConfirm = confirmNewPass.getText().toString().trim();
        if (!sNewPass.equals("")) {
            if (sNewPass.equals(sNewPassConfirm)) {
                alertDialog.dismiss();
                new WebService(this).execute(API.ServerAddress + API.RESET_PASSWORD, "module=password_reset" + "&mail_id=" + regEmail + "&new_pin=" + sNewPass);

            } else {
                confirmNewPass.setError("password doesnt match");
            }
        }
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

    @Override
    public void onTaskComplete(String result) {
        switch (result) {
            case "00": {
                Toast.makeText(this, "Invalid Request", Toast.LENGTH_LONG).show();
                break;
            }
            case "01":
            case "02": {
                Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show();
                break;
            }
            case "03": {
                Toast.makeText(this, "User not found!", Toast.LENGTH_LONG).show();
                break;
            }
            case "502": {
                Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
                break;
            }
            case "password_reset/0": {
                Toast.makeText(this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                break;
            }
            default: {
                String[] arrRes = result.split(",");

                if (arrRes.length < 3) {
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                } else {
                    lastScoreDate = arrRes[1];
                    Score = arrRes[2];
                    setNavigationDrawer();
                }
            }
        }


    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RUNTIME_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Log.i("Permission", "onRequestPermissionsResult: Permission Denied");
                }
            }
        }
    }
}