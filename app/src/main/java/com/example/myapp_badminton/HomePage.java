package com.example.myapp_badminton;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapp_badminton.PlayModule.PlayVideo;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

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
    CircleImageView profilePic;
    String sNewPass, sNewPassConfirm, regEmail;
    TextView tvUserMainInfo, tvUserSubInfo;
    NetworkAvailability networkAvailability;
    NavigationView navView; // initiate a Navigation View

    private EditText newPass, confirmNewPass;
    private byte[] imageBytes;
    private ProgressDialog progressDialog;
    private boolean permissionGiven;

    //    String uname,id,utype,lastScoreDate,Score;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_page);
            Log.e("onCreate: ", "***from activity***" + this.getLocalClassName());
            networkAvailability = NetworkAvailability.getInstance(this);
            Toolbar toolbar = findViewById(R.id.toolbar);// get the reference of Toolbar
            SharedPreferences shared = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            verifyStoragePermissions(this);
            profilePic = findViewById(R.id.nav_user_image);
            tvUserMainInfo = findViewById(R.id.nav_main_info);
            tvUserSubInfo = findViewById(R.id.nav_sub_info);
//            setNavigationDrawer();
            setSupportActionBar(toolbar);
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            utype = settings.getString("type", "");
            if (utype.equals("coach")) {
                uname = settings.getString("Name", "");
                id = settings.getString("Id", "");
                regEmail = settings.getString("mail_id", "");

                setNavigationDrawer();
                //            displayNavHeaderInfo();
//                setNavigationDrawer();
            } else {
                uname = settings.getString("Name", "");
                id = settings.getString("Id", "");
                playerImage = settings.getString("Image", "");
                regEmail = settings.getString("mail_id", "");
                ActivityTracker.writeActivityLogs(this.getLocalClassName(), id);
                if (isConnected()) {
                    new WebService(HomePage.this).execute(API.ServerAddress + API.AFTER_LOGIN, "user_id=" + id);
                } else {
                    setNavigationDrawer();
                }
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (permissionGiven) {
                        dLayout.openDrawer(Gravity.LEFT);
                        displayNavHeaderInfo();
                    } else {
                        Toast.makeText(getApplicationContext(), "Grant permissions!!", Toast.LENGTH_LONG).show();
                    }
                    /*tvUserMainInfo = findViewById(R.id.nav_main_info);
                    tvUserSubInfo = findViewById(R.id.nav_sub_info);
                    tvUserMainInfo.setText(uname);
                    tvUserSubInfo.setText(regEmail);*/

                }
            });
            setNavigationDrawer();
        } catch (Exception e) {
            e.printStackTrace();
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
        } else {
            permissionGiven = true;
        }
    }

    private void setNavigationDrawer() {
        try {
            profilePic = findViewById(R.id.nav_user_image);
            tvUserMainInfo = findViewById(R.id.nav_main_info);
            tvUserSubInfo = findViewById(R.id.nav_sub_info);
            Bitmap bmp = null;
            dLayout = findViewById(R.id.drawer_layout); // initiate a DrawerLayout

            navView = findViewById(R.id.navigation);
//            displayNavHeaderInfo();
            Menu menu = navView.getMenu();
            if (utype.equalsIgnoreCase("coach")) {
                menu.findItem(R.id.five).setVisible(false);
                menu.findItem(R.id.five1).setVisible(false);

            }

            navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    Fragment frag = null; // create a Fragment Object
                    int itemId = menuItem.getItemId();

                    // get selected menu item's id
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
                        if (utype.equalsIgnoreCase("Player")) {
//                            sendLog();
                            if(isConnected())
                            startActivity(new Intent(getApplicationContext(), PlayVideo.class));
                            else
                                Toast.makeText(getApplicationContext(), "You are offline", Toast.LENGTH_SHORT).show();
                        }
                        //                    else{
                        //                        menu.findItem(itemId).setVisible(false);
                        //                    }
                    } else if (itemId == R.id.five1) {
                        if (utype.equalsIgnoreCase("Player")) {
                            if (isConnected())
                                sendLog();
                            else {
                                Toast.makeText(HomePage.this, "No internet!", Toast.LENGTH_SHORT).show();
                            }
                        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendLog() {
        new WebService(this).execute(API.ServerAddress + API.LOG, "badmintonLogs");

    }

    private void createResetPasswordAlertDialog() {
        try {
            LayoutInflater li = LayoutInflater.from(this);
            View confirmDialog = li.inflate(R.layout.activity_reset_password, null);
            newPass = confirmDialog.findViewById(R.id.pass_new);
            confirmNewPass = confirmDialog.findViewById(R.id.pass_confirm);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setView(confirmDialog);
            alertDialog = alert.create();
            alertDialog.show();
//        alertDialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetPasswordOrPin(View view) {
        try {
            progressDialog = ProgressDialog.show(this, "Password Resetting", "Please wait..", false, false);
//        alertDialog.dismiss();
            sNewPass = newPass.getText().toString();
            sNewPassConfirm = confirmNewPass.getText().toString();
            if (!sNewPass.equals("")) {
                if (sNewPass.equals(sNewPassConfirm)) {
                    alertDialog.dismiss();
                    if (isConnected()) {
                        new WebService(this).execute(API.ServerAddress + API.RESET_PASSWORD, "module=password_reset" + "&mail_id=" + regEmail + "&new_pin=" + sNewPass);

                    } else {
                        Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    confirmNewPass.setError("password doesnt match");

                }
            } else {
                newPass.setError("can't be empty");

            }
            progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLogoutDialog() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void logout() {
        startActivity(new Intent(this, Login.class));
        finish();
    }

    @Override
    public void onTaskComplete(String result) {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            setNavigationDrawer();
            Log.e("onTaskComplete: ", "res " + result);
            switch (result) {
                case "00": {
                    Toast.makeText(this, "Invalid Request", Toast.LENGTH_LONG).show();
                    break;
                }
                case "01":
                case "02": {
                    Toast.makeText(this, "Server busy!", Toast.LENGTH_LONG).show();
                    break;
                }
                case "03": {
                    Toast.makeText(this, "User not found!", Toast.LENGTH_LONG).show();
                    break;
                }
                case "404": {
                    Toast.makeText(this, "Nothing to sync!", Toast.LENGTH_LONG).show();

                }
                case "502": {
                    Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
                    break;
                }
                case "password_reset/0": {
                    Toast.makeText(this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                    break;
                }
                case "file-0": { //sync data
                    Toast.makeText(this, "Sync successful", Toast.LENGTH_SHORT).show();
                    deleteFile();

                    break;
                }
                default: {
                    String[] arrRes = result.split(",");

                    if (arrRes.length < 3) {
                        Toast.makeText(this, "Request not processed!", Toast.LENGTH_SHORT).show();
                    } else /*if (arrRes.length == 3) */ {
                        lastScoreDate = arrRes[1];
                        Score = arrRes[2];
                        setNavigationDrawer();

                        /* displayNavHeaderInfo();*/
                    } /*else {
                        imageBytes = Base64.decode(result, Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        profilePic.setImageBitmap(decodedImage);

                    }*/
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void deleteFile() {
        try {
            String sourceFileUri = "/storage/emulated/0/Badminton";
            File sourceFile = new File(sourceFileUri + "/badmintonLogs.txt");
            if (sourceFile.exists()) {
                if (sourceFile.delete()) {
                    System.out.println("file Deleted :" + sourceFile.getPath());
                } else {
                    System.out.println("file not Deleted :" + sourceFile.getPath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayNavHeaderInfo() {
        try {
            profilePic = findViewById(R.id.nav_user_image);
            tvUserMainInfo = findViewById(R.id.nav_main_info);
            tvUserSubInfo = findViewById(R.id.nav_sub_info);

            if (utype.equals("player")) {
                imageBytes = Base64.decode(playerImage, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                profilePic.setImageBitmap(decodedImage);
                profilePic.setVisibility(View.VISIBLE);
                tvUserMainInfo.setText(uname);
                tvUserSubInfo.setText(regEmail);
            } else {
                tvUserMainInfo.setText(uname);
                tvUserSubInfo.setText(regEmail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RUNTIME_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    permissionGiven = true;
                } else {
                    permissionGiven = false;
                    Toast.makeText(this, "Grant permissions!!", Toast.LENGTH_LONG).show();
                    Log.i("Permission", "onRequestPermissionsResult: Permission Denied");
                }
            }
        }
    }

    boolean isConnected() {
        try {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}