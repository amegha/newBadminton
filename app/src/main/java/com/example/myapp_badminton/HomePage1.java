//package com.example.myapp_badminton;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Base64;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.GridLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.example.myapp_badminton.PlayModule.PlayVideo;
//import com.google.android.material.navigation.NavigationView;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class HomePage1 extends AppCompatActivity implements AsyncResponse {
//    public static final String PREFS_NAME = "LoginPrefs";
//    private static final int REQUEST_RUNTIME_PERMISSIONS = 1;
//    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA,
//    };
//    DrawerLayout dLayout;
//    MainActivity mainActivity;
//    String date, uname, id, utype, lastScoreDate, Score, playerImage, scoreFilter;
//    AlertDialog alertDialog;
//    CircleImageView profilePic;
//    ImageView tipsImage;
//    String sNewPass, sNewPassConfirm, regEmail;
//    TextView tvUserMainInfo, tvUserSubInfo, totVid, playedVid, correctAns, wrongAns;
//    NetworkAvailability networkAvailability;
//    NavigationView navView; // initiate a Navigation View
//    SharedPreferences settings;
//    SharedPreferences.Editor editor;
//    private EditText newPass, confirmNewPass;
//    private byte[] imageBytes;
//    private ProgressDialog progressDialog;
//    private boolean permissionGiven;
//    private GridLayout gridLayout;
//    private String imageString;
//    private boolean scoreFilterFlag;
//
//
//    //    String uname,id,utype,lastScoreDate,Score;
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        try {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_home_page);
//            Log.e("onCreate: ", "***from activity***" + this.getLocalClassName());
//            networkAvailability = NetworkAvailability.getInstance(this);
//            Toolbar toolbar = findViewById(R.id.toolbar);// get the reference of Toolbar
//            verifyStoragePermissions(this);
////            mainActivity=new MainActivity();
//            gridLayout = findViewById(R.id.mainGrid);
////            setSingleEvent(gridLayout);
//            profilePic = findViewById(R.id.nav_user_image);
//            tvUserMainInfo = findViewById(R.id.nav_main_info);
//            tvUserSubInfo = findViewById(R.id.nav_sub_info);
//
//            totVid = findViewById(R.id.tot_video);
//            playedVid = findViewById(R.id.played_video);
//            correctAns = findViewById(R.id.right_answers);
//            wrongAns = findViewById(R.id.wrong_answers);
//
//            tipsImage = findViewById(R.id.play_game);
////            setNavigationDrawer();
//            setSupportActionBar(toolbar);
//            settings = getSharedPreferences(PREFS_NAME, 0);
//            editor = settings.edit();
//
//            utype = settings.getString("type", "");
//            if (utype.equals("coach")) {
//                uname = settings.getString("Name", "");
//                id = settings.getString("Id", "");
//                regEmail = settings.getString("mail_id", "");
//                tipsImage.setVisibility(View.GONE);
//                setNavigationDrawer();
//                //            displayNavHeaderInfo();
////                setNavigationDrawer();
//            } else {
//                uname = settings.getString("Name", "");
//                id = settings.getString("Id", "");
//                playerImage = settings.getString("Image", "");
//                Log.e("oncreate image", "image " + playerImage);
//                regEmail = settings.getString("mail_id", "");
//                ActivityTracker.writeActivityLogs(this.getLocalClassName(), id, getApplicationContext());
//                if (isConnected()) {
//                    new WebService(HomePage1.this).execute(API.ServerAddress + API.AFTER_LOGIN, "user_id=" + id);
//                } else {
//                    setNavigationDrawer();
//                }
//            }
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (permissionGiven) {
//                        dLayout.openDrawer(Gravity.LEFT);
//                        displayNavHeaderInfo();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Grant permissions!!", Toast.LENGTH_LONG).show();
//                    }
//                    /*tvUserMainInfo = findViewById(R.id.nav_main_info);
//                    tvUserSubInfo = findViewById(R.id.nav_sub_info);
//                    tvUserMainInfo.setText(uname);
//                    tvUserSubInfo.setText(regEmail);*/
//
//                }
//            });
//            setNavigationDrawer();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//
//    public void verifyStoragePermissions(Activity activity) {
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            Log.e("verify", "permission not given ");
//
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_RUNTIME_PERMISSIONS
//
//            );
//        } else {
//            Log.e("verify", "permission given ");
//            permissionGiven = true;
//        }
//    }
//
//    /*@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.filter1:
//                scoreFilter = "Fitness";
//                return true;
//            case R.id.filter2:
//                scoreFilter = "Grip";
//                return true;
//            case R.id.filter3:
//                scoreFilter = "On Court Skills";
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//
//        }
//    }*/
//
//    private void setNavigationDrawer() {
//        try {
//            profilePic = findViewById(R.id.nav_user_image);
//            tvUserMainInfo = findViewById(R.id.nav_main_info);
//            tvUserSubInfo = findViewById(R.id.nav_sub_info);
//            Bitmap bmp = null;
//            dLayout = findViewById(R.id.drawer_layout); // initiate a DrawerLayout
//
//            navView = findViewById(R.id.navigation);
////            displayNavHeaderInfo();
//            Menu menu = navView.getMenu();
//            if (utype.equalsIgnoreCase("coach")) {
//                menu.findItem(R.id.five).setVisible(false);
//                menu.findItem(R.id.five1).setVisible(false);
//                menu.findItem(R.id.five2).setVisible(false);
//
//            }
//
//            navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(MenuItem menuItem) {
//                    Fragment frag = null; // create a Fragment Object
//                    int itemId = menuItem.getItemId();
//
//                    // get selected menu item's id
//                    if (itemId == R.id.first) {
//                        if (utype.equals("coach")) {
//                            frag = new ScoreEntry_fragment(uname, id, utype);
//                        } else {
//                            frag = new ScoreEntry_fragment(uname, id, utype, lastScoreDate, Score, playerImage);
//                        }
//                    } else if (itemId == R.id.second) {
//                        if (utype.equalsIgnoreCase("Player")) {
///*
//                            if (itemId == R.id.filter1) {
//                                scoreFilter = "Fitness";
//                            } else if (itemId == R.id.filter2) {
//                                scoreFilter = "Grip";
//                            } else if (itemId == R.id.filter3) {
//                                scoreFilter = "On Court Skills";
//                        }*/
////                            frag = new Performance_fragment(uname, id, utype, lastScoreDate, Score, scoreFilter);
//
//                            scoreFilter = createScoreFilterDialogBox();
////                            if (scoreFilterFlag) {
////                            frag = new Performance_fragment(uname, id, utype, lastScoreDate, Score, scoreFilter);
////                            } else {
//                            Toast.makeText(HomePage1.this, "select the categoty", Toast.LENGTH_SHORT).show();
////                            }
//                        } else {
//                            frag = new Performance_fragment(uname, id, utype);
//                        }
//
//                    } else if (itemId == R.id.third) {
//                        if (utype.equalsIgnoreCase("Player")) {
//                            frag = new ScoreObtained_fragment(uname, id, utype, lastScoreDate, Score);
//                        } else {
//                            frag = new ScoreObtained_fragment(uname, id, utype);
//                        }
//                    } else if (itemId == R.id.four) {
//                        createResetPasswordAlertDialog();
//
//                        //                    frag = new ForgotPasswordFragment();
//                    } else if (itemId == R.id.five) {
//                        if (utype.equalsIgnoreCase("Player")) {
////                            sendLog();
//                            if (isConnected())
//                                startActivity(new Intent(getApplicationContext(), PlayVideo.class));
//                            else
//                                Toast.makeText(getApplicationContext(), "You are offline", Toast.LENGTH_SHORT).show();
//                        }
//                        //                    else{
//                        //                        menu.findItem(itemId).setVisible(false);
//                        //                    }
//                    } else if (itemId == R.id.five1) {
//                        if (utype.equalsIgnoreCase("Player")) {
//                            if (isConnected())
//                                sendLog();
//                            else {
//                                Toast.makeText(HomePage1.this, "No internet!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    } else if (itemId == R.id.five2) {
//                        if (utype.equalsIgnoreCase("Player")) {
//                            if (isConnected())
//                                changeProfilePic();
//                            else {
//                                Toast.makeText(HomePage1.this, "No internet!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    } else if (itemId == R.id.six)//refer to about
//                    {
//                        frag = new About();
//                    } else if (itemId == R.id.seven)//refer to signout
//                    {
//                        showLogoutDialog();
//                        //                    frag = new Signout();
//                    }
//                    if (frag != null) {
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
//                        transaction.commit(); // commit the changes
//                        dLayout.closeDrawers(); // close the all open Drawer Views
//                        return true;
//                    }
//                    return false;
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String createScoreFilterDialogBox() {
//        final CharSequence[] options = {"Fitness", "Grip", "On court", "All"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Select the category");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//
//                if (options[item].equals("Fitness")) {
//                    scoreFilter = "Fitness";
//                    submenuClicked("fitness");
//                } else if (options[item].equals("Grip")) {
//                    scoreFilter = "Grip";
//                    submenuClicked("grip");
//                } else if (options[item].equals("On court")) {
//                    scoreFilter = "On Court Skills";
//                    submenuClicked("onCourt");
//                } /*else if (options[item].equals("All")) {
//                        scoreFilter = "All";
//                    }*/
//                scoreFilterFlag = true;
//            }
//        });
//        builder.show();
//        return scoreFilter;
//    }
//
//    private void submenuClicked(String subMenuName) {
//        Toast.makeText(HomePage1.this, " categoty selected!!", Toast.LENGTH_SHORT).show();
//        Performance_fragment frag = new Performance_fragment(uname, id, utype, lastScoreDate, Score, scoreFilter);
//
//        if (frag != null) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
//            transaction.commit(); // commit the changes
//            dLayout.closeDrawers(); // close the all open Drawer Views
//        }
//
//
//    }
//
//    private void changeProfilePic() {
//        Log.e("changeProfilePic", "permission " + permissionGiven);
//        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Choose your profile picture");
//
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//
//                if (options[item].equals("Take Photo")) {
//                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePicture, 0);
//
//                } else if (options[item].equals("Choose from Gallery")) {
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
//
//                } else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != RESULT_CANCELED) {
//            switch (requestCode) {
//                case 0:
//                    if (resultCode == RESULT_OK && data != null) {
//                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
//                        profilePic.setImageBitmap(selectedImage);
//                        convertTobase64(selectedImage);
//
//                    }
//
//                    break;
//                case 1:
//                    if (resultCode == RESULT_OK && data != null) {
//                        Uri selectedImage = data.getData();
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//                            Cursor cursor = getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                profilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                convertTobase64(BitmapFactory.decodeFile(picturePath));
//                                cursor.close();
//                            }
//                        }
//
//                    }
//                    break;
//            }
//        }
//    }
//
//    private void convertTobase64(Bitmap image) {
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//            byte[] b = baos.toByteArray();
//            imageString = Base64.encodeToString(b, Base64.DEFAULT);
//
//            /*editor.putString("Image", imageString);
//            editor.apply();*/
//            Log.e("image string", "HomePage1" + imageString + "User id " + id);
//
//            new WebService(this).execute(API.ServerAddress + API.PROFILE_PIC_UPDATE, "user_id=" + id + "&image=" + imageString);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    private void sendLog() {
//        new WebService(this).execute(API.ServerAddress + API.LOG, "badmintonLogs");
////        new WebService(this).execute(API.ServerAddress + API.LOG,new DBHandler(getApplicationContext()).getLogString() );
////        new WebService(this).execute(API.ServerAddress + API.LOG, new DBHandler(this).getLogString());
//
//
//    }
//
//    private void createResetPasswordAlertDialog() {
//        try {
//            LayoutInflater li = LayoutInflater.from(this);
//            View confirmDialog = li.inflate(R.layout.activity_reset_password, null);
//            newPass = confirmDialog.findViewById(R.id.pass_new);
//            confirmNewPass = confirmDialog.findViewById(R.id.pass_confirm);
//            AlertDialog.Builder alert = new AlertDialog.Builder(this);
//            alert.setView(confirmDialog);
//            alertDialog = alert.create();
//            alertDialog.show();
////        alertDialog.setCanceledOnTouchOutside(false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void resetPasswordOrPin(View view) {
//        try {
//            progressDialog = ProgressDialog.show(this, "Password Resetting", "Please wait..", false, false);
////        alertDialog.dismiss();
//            sNewPass = newPass.getText().toString();
//            sNewPassConfirm = confirmNewPass.getText().toString();
//            if (!sNewPass.equals("")) {
//                if (sNewPass.equals(sNewPassConfirm)) {
//                    alertDialog.dismiss();
//                    if (isConnected()) {
//                        new WebService(this).execute(API.ServerAddress + API.RESET_PASSWORD, "module=password_reset" + "&mail_id=" + regEmail + "&new_pin=" + sNewPass);
//
//                    } else {
//                        Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    confirmNewPass.setError("password mismatch");
//
//                }
//            } else {
//                newPass.setError("can't be empty");
//
//            }
//            progressDialog.dismiss();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void showLogoutDialog() {
//        try {
//            AlertDialog.Builder alert = new AlertDialog.Builder(HomePage1.this);
//            alert.setMessage("Are you sure?")
//                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//                            editor = settings.edit();
//                            editor.putString("logged", "not");
//                            editor.apply();
//                            logout(); // Last step. Logout function
//
//                        }
//                    }).setNegativeButton("Cancel", null);
//
//            AlertDialog alert1 = alert.create();
//            alert1.show();
//            alert1.setCanceledOnTouchOutside(false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void logout() {
//        startActivity(new Intent(this, Login.class));
//        finish();
//    }
//
//    @Override
//    public void onTaskComplete(String result) {
//        try {
//            Log.e("onTaskComplete: ", "res " + result);
//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
//            setNavigationDrawer();
//            switch (result) {
//                case "00": {
//                    Toast.makeText(this, "Invalid Request", Toast.LENGTH_LONG).show();
//                    break;
//                }
//                case "01":
//                case "02": {
//                    Toast.makeText(this, "Server busy!", Toast.LENGTH_LONG).show();
//                    break;
//                }
//                case "03": {
//                    Toast.makeText(this, "User not found!", Toast.LENGTH_LONG).show();
//                    break;
//                }
//                case "404": {
//                    Toast.makeText(this, "Nothing to sync!", Toast.LENGTH_LONG).show();
//
//                }
//                case "502": {
//                    Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                case "password_reset/0": {
//                    Toast.makeText(this, "Password reset successfully", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                case "file-0": { //sync data
//                    Toast.makeText(this, "Sync successful", Toast.LENGTH_SHORT).show();
//                    deleteFile();
//
//                    break;
//                }
//                case "pic_update-0": { //profile pic update
//                    Toast.makeText(this, "Pic update successful", Toast.LENGTH_SHORT).show();
//                    editor.putString("Image", imageString);
//                    editor.apply();
//                    break;
//                }
//                default: { // after login
//                    String[] arrRes = result.split(",");
//
//                    if (arrRes.length < 3) {
//                        Toast.makeText(this, "Request not processed!", Toast.LENGTH_SHORT).show();
//                    } else if (arrRes.length == 4) { // dashboard data
//                        totVid.setText(arrRes[0]);
//                        playedVid.setText(arrRes[1]);
//                        correctAns.setText(arrRes[2]);
//                        wrongAns.setText(arrRes[3]);
//
//                    } else /*if (arrRes.length == 3) */ {
//                        lastScoreDate = arrRes[1];
//                        Score = arrRes[2];
//                        setNavigationDrawer();
//                        new WebService(HomePage1.this).execute(API.ServerAddress + API.PLAYER_DASHBOARD, "user_id=" + id);
//                        /* displayNavHeaderInfo();*/
//                    } /*else {
//                        imageBytes = Base64.decode(result, Base64.DEFAULT);
//                        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                        profilePic.setImageBitmap(decodedImage);
//
//                    }*/
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private void deleteFile() {
//        try {
////            String sourceFileUri = "/storage/emulated/0/Badminton";
//            String sourceFileUri = getFileUri(getApplicationContext());
//            File sourceFile = new File(sourceFileUri + "/badmintonLogs.txt");
//            if (sourceFile.exists()) {
//                if (sourceFile.delete()) {
//                    System.out.println("file Deleted :" + sourceFile.getPath());
//                } else {
//                    System.out.println("file not Deleted :" + sourceFile.getPath());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String getFileUri(Context mContext) {
//        try {
//     /*       String fileName = "badmintonLogs.txt";
//            File root = new File(Environment.getExternalStorageDirectory(), "Badminton");*/
//
//            //
//            String name = "Badminton";
//            File sdcard; /*= Environment.getExternalStorageDirectory();*/
//            if (mContext.getResources().getBoolean(R.bool.internalstorage)) {
//                sdcard = mContext.getFilesDir();
//            } else if (!mContext.getResources().getBoolean(R.bool.standalone)) {
//                sdcard = new File(Environment.getExternalStoragePublicDirectory(name).toString());
//            } else {
//                if ("goldfish".equals(Build.HARDWARE)) {
//                    sdcard = mContext.getFilesDir();
//                } else {
//                    // sdcard/Android/<app_package_name>/AWARE/ (not shareable, deletes when uninstalling package)
//                    sdcard = new File(ContextCompat.getExternalFilesDirs(mContext, null)[0] + "/" + name);
//                }
//            }
//            if (!sdcard.exists()) {
//                sdcard.mkdirs();
//            }
//            return sdcard.toString();
////            return new File(sdcard, fileName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private void displayNavHeaderInfo() {
//        try {
//            profilePic = findViewById(R.id.nav_user_image);
//            tvUserMainInfo = findViewById(R.id.nav_main_info);
//            tvUserSubInfo = findViewById(R.id.nav_sub_info);
//
//            if (utype.equals("player")) {
//                imageBytes = Base64.decode(playerImage, Base64.DEFAULT);
//                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                profilePic.setImageBitmap(decodedImage);
//                profilePic.setVisibility(View.VISIBLE);
//                tvUserMainInfo.setText(uname);
//                tvUserSubInfo.setText(regEmail);
//            } else {
//                tvUserMainInfo.setText(uname);
//                tvUserSubInfo.setText(regEmail);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_RUNTIME_PERMISSIONS: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
//                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
//                    permissionGiven = true;
//                } else {
//                    permissionGiven = false;
//                    Toast.makeText(this, "Grant permissions!!", Toast.LENGTH_LONG).show();
//                    Log.i("Permission", "onRequestPermissionsResult: Permission Denied");
//                }
//            }
//        }
//    }
//
//    boolean isConnected() {
//        try {
//            boolean haveConnectedWifi = false;
//            boolean haveConnectedMobile = false;
//
//            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
//            for (NetworkInfo ni : netInfo) {
//                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
//                    if (ni.isConnected())
//                        haveConnectedWifi = true;
//                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
//                    if (ni.isConnected())
//                        haveConnectedMobile = true;
//            }
//            return haveConnectedWifi || haveConnectedMobile;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//}