package com.example.myapp_badminton;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AsyncResponse, AdapterView.OnItemSelectedListener {

    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4192;
    private static final int CAMERA_REQUEST = 1888;
    public static String result;
    final ArrayList<String> state_options = new ArrayList<String>();
    final ArrayList<String> city_options = new ArrayList<String>();
    String imageString;
    String email_s, module;
    Parsexml parsexml;

    //otherpart
    MyDbAdapter helper;
    // ArrayAdapter<CharSequence> adapter;
    //for camera
    ImageView imageView;
    TextView image_name, age, dob;
    Button click, add;
    String image_uri;
    //radio buttons and variables
    RadioGroup r_gender, r_edu, r_playertype;
    RadioButton male, female, school, college, player, coach, mentor;
    String radio_gender, radio_Education, radio_playerType, age_a;
    //other data variables
    EditText fname, email, state_rank, national_rank, password;
    TextView showDate, loginBack;
    //    Spinner academyName,state,city,location;
    private AppCompatAutoCompleteTextView autoTextState, autoTextCity, autoTextLocation, autoTextAcademyName;
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String name_s = fname.getText().toString().trim();
            email_s = email.getText().toString().trim();
            String s_rank = state_rank.getText().toString().trim();
            String n_rank = national_rank.getText().toString().trim();

            click.setEnabled(!name_s.isEmpty() && !email_s.isEmpty() && !s_rank.isEmpty() && !n_rank.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static String md5(String input) {
        try {
            //md5 type(Message digest algorithm)
            MessageDigest md = MessageDigest.getInstance("MD5");
            BigInteger md5Data = new BigInteger(1, md.digest(input.getBytes()));
            String converted = md5Data.toString();

            result = converted;
            return String.format("%032X", md5Data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image_name = findViewById(R.id.image_name);
        dob = findViewById(R.id.tv_Dob);
        click = findViewById(R.id.click);
        //radio values IDSo
        male = findViewById(R.id.rb_male);
        female = findViewById(R.id.rb_female);
        school = findViewById(R.id.rb_school);
        college = findViewById(R.id.rb_college);
        autoTextState = findViewById(R.id.state);
        autoTextCity = findViewById(R.id.city);
        autoTextLocation = findViewById(R.id.location);
        autoTextAcademyName = findViewById(R.id.academy_name);
        parsexml = new Parsexml();
        /*player = findViewById(R.id.rb_player);
        coach = findViewById(R.id.rb_coach);
        mentor = findViewById(R.id.rb_mentor);*/


        male.setChecked(true);
        female.setChecked(false);

        school.setChecked(true);
        college.setChecked(false);

        /*player.setChecked(true);
        coach.setChecked(false);
        mentor.setChecked(false);*/
        //radio group IDS
        r_edu = findViewById(R.id.rg_edu);
        r_gender = findViewById(R.id.rg_gender);
//        r_playertype = findViewById(R.id.rg_playertype);
        helper = new MyDbAdapter(this);


        //other data
        fname = findViewById(R.id.et_fname);
        password = findViewById(R.id.et_phone_number);
        email = findViewById(R.id.et_email);
        state_rank = findViewById(R.id.et_stateRank);
        national_rank = findViewById(R.id.tv_nationalrank);


        fname.addTextChangedListener(loginTextWatcher);
        email.addTextChangedListener(loginTextWatcher);
        state_rank.addTextChangedListener(loginTextWatcher);
        national_rank.addTextChangedListener(loginTextWatcher);
        imageView = findViewById(R.id.image);


        add = findViewById(R.id.btn_add);


        age = findViewById(R.id.tv_age);
        showDate = findViewById(R.id.tv_Dob);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        findViewById(R.id.tv_Dob).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDatePickerDialog();
            }
        });


        // Spinner click listener
//        academyName.setOnItemSelectedListener(this);
        click.setEnabled(false);
        add.setEnabled(false);
        //necessary  condition to run read or write to the file
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Radio_gender_selected();
                Radio_education_selected();
//                Radio_playerType_selected();
                if ((male.isChecked() == true || female.isChecked() == true) && (college.isChecked() == true || school.isChecked() == true) /*&& (coach.isChecked() == true || player.isChecked() == true) || mentor.isChecked() == true*/) {
                    if (autoTextAcademyName.getText().toString().trim().equals("--Training Center--")) {
                        Toast.makeText(MainActivity.this, "You Have Not selected Training Center !! ",
                                Toast.LENGTH_LONG).show();
                    } else {
                        addUser();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Please Select Appropriate radio fields!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        loginBack = findViewById(R.id.Login_back);
        loginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login_screen();
            }
        });
        getAcademyInfo();
        /*System.out.println("from mainActivity!!" + Collections.singletonList(parsexml.stateList));
        String[] statesArr = new String[parsexml.stateList.size()];
        statesArr = (String[]) parsexml.stateList.toArray(statesArr);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, statesArr);
        autoTextState.setThreshold(1);//will start working from first character
        autoTextState.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
//        autoTextState.setTextColor(Color.RED);*/
    }

    private void getAcademyInfo() {
        module = "academy";
        new WebService(this).execute(API.ServerAddress + "" + API.GET_ACADEMY_INFO, "module=" + module);

    }

    //for spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(MainActivity.this, "You Have Not selected Training Center !! ",
                Toast.LENGTH_LONG).show();

    }

    //login form
    public void Login_screen() {
        Intent intent;
        intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

    //date calculation as well as age calculation
    private void ShowDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        int age_cal;

        Calendar c = Calendar.getInstance();
        int endYear = c.get(Calendar.YEAR);
        int endMonth = c.get(Calendar.MONTH);
        endMonth++;
        int endDay = c.get(Calendar.DAY_OF_MONTH);

        //age calculation
        age_cal = (endYear - year);
        age_a = age_cal + "";
        age.setText("Age is " + age_a);
        age.setVisibility(View.VISIBLE);
        //DOB selected by user
        //String date = " " + dayOfMonth + "-" + month + "-" + year + "\n";
        String date = " " + year + "-" + month + "-" + dayOfMonth + "\n";
        showDate.setText(date);

    }

    //Camera functions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //necessary function to check and run read or write to the file
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            } else {
                Toast.makeText(this, R.string.cannotopencamera, Toast.LENGTH_LONG).show();
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void OnTakePhotoClicked(View view) {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            invokeCamera();

        } else {
            String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
        }


    }

    private void invokeCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            convertTobase64(photo);
        }
    }

    private void convertTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        imageString = Base64.encodeToString(b, Base64.DEFAULT);
        Log.e("image ", "convertTobase64: " + imageString);
        add.setEnabled(true);
    }

    private void writeToTxtFile(String text) {
        String filename = "base.txt";
        File root = new File(getExternalStorageDirectory(), "Badminton");
        if (!root.exists()) {
            root.mkdirs();
        }
        File textFile = new File(root, filename);
        FileWriter writer = null;
        try {
            writer = new FileWriter(textFile);
            writer.append(text);
            writer.flush();
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    //Gender Info
    public void Radio_gender_selected() {

        //This variable will store whether the user was male or female
        String userGender = "";

        // Check which radio button was clicked
        if (female.isChecked() == true) {
            userGender = female.getText().toString();
            radio_gender = userGender;
            //   Toast.makeText(this, " selected Female ", Toast.LENGTH_LONG).show();
        } else if (male.isChecked() == true) {
            userGender = male.getText().toString();
            radio_gender = userGender;
            //  Toast.makeText(this," selected Male ",Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, " please Select Respective Radio Fields !! ", Toast.LENGTH_LONG).show();


    }

    //Education Details
    public void Radio_education_selected() {
        //This variable will store whether the user was male or female
        String userEducation = "";
        // Check which radio button was clicked
        if (school.isChecked() == true) {
            userEducation = school.getText().toString();
            radio_Education = userEducation;
            // Toast.makeText(this, " Selected School ", Toast.LENGTH_LONG).show();
        } else if (college.isChecked() == true) {
            userEducation = college.getText().toString();
            radio_Education = userEducation;
            // Toast.makeText(this," Selected College ",Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, " Please Select Respective Radio Fields !! ", Toast.LENGTH_LONG).show();

        //Now insert it into your database using userGender instead of gender
    }

    //Player Type Details
    public void Radio_playerType_selected() {
        //This variable will store whether the user was male or female
        String playerType = "";

        // Check which radio button was clicked
        if (player.isChecked() == true) {
            playerType = player.getText().toString();
            radio_playerType = playerType;
            //  Toast.makeText(this," selected Player ",Toast.LENGTH_LONG).show();
        } else if (coach.isChecked() == true) {
            playerType = coach.getText().toString();
            radio_playerType = playerType;
            //Toast.makeText(this," selected Coach ",Toast.LENGTH_LONG).show();
        } else if (mentor.isChecked() == true) {
            playerType = mentor.getText().toString();
            radio_playerType = playerType;
            //Toast.makeText(this," selected Coach ",Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, " Please Select Respective Radio Fields !! ", Toast.LENGTH_LONG).show();

        //Now insert it into your database using userGender instead of gender
    }

    //add data
    public void addUser() {
        //name concatenating
        final String name = fname.getText().toString();
        final String password_user = password.getText().toString();
        final String mailId = email.getText().toString();
        //to get selected item from training center list
//        final String trainingCenter = academyName.getSelectedItem().toString();
        final String trainingCenter = "xyz";
        final int stateRank = Integer.parseInt(state_rank.getText().toString());
        final int nationalRank = Integer.parseInt(national_rank.getText().toString());
        final String image_uri = this.image_uri;
        final String image_uri_data = imageString;
        final String age = age_a;
        final String dob = showDate.getText().toString();
        final String enc_password = md5(password_user);
        //timestamp makes unique name
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String timestamp1 = sdf.format(new Date());
        String state;
        state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            File Root = getExternalStorageDirectory();
            File Dir = new File(Root.getAbsolutePath() + "/MyFile");
            if (!Dir.exists()) {
                Dir.mkdir();
            }
            File file = new File(Dir, "Message.xml");
        }
        if (name.isEmpty() || mailId.isEmpty() || state_rank.getText().toString().isEmpty() || national_rank.getText().toString().isEmpty()) {

            Message.message(getApplicationContext(), "Enter all Text Fields as well as select Proper Radio Fields !!");
        } else {

//            long id = helper.insertData(name, password_user, enc_password, mailId, trainingCenter, stateRank, nationalRank, image_uri_data, radio_gender, radio_Education, radio_playerType, age, dob, timestamp1);
            formXMl(name, password_user, mailId, trainingCenter, stateRank, nationalRank, image_uri, radio_gender, radio_Education, radio_playerType, age, dob, timestamp1);

//            Message.message(getApplicationContext(), "Insertion Successful");
            // new UploadFileAsync().execute("");
            fname.setText("");
            email.setText("");
            state_rank.setText("");
            national_rank.setText("");
            //dob.setHint("Date of Birth");
            this.dob.setText("");
            male.setChecked(true);
            female.setChecked(false);

            school.setChecked(true);
            college.setChecked(false);

            player.setChecked(true);
            coach.setChecked(false);
            mentor.setChecked(false);
            this.age.setVisibility(View.GONE);
            //academyName.setOnItemSelectedListener(this);
//            academyName.setSelection(0);
            imageView.setVisibility(View.GONE);


        }


    }

    private void formXMl(String name, String password, String mailId, String trainingCenter, int stateRank, int nationalRank, String image_uri, String radio_gender, String radio_education, String radio_playerType, String age, String dob, String timestamp1) {
        Log.e("getdata", "entered form xml: ");

        String xml = "<user_details>\n<userName>" + name + "</userName>\n<password>" + password + "</password>\n<userType>" + radio_playerType + "</userType>\n<uAge>" + age + "</uAge>\n<uDob>" + dob + "</uDob>\n<usex>" + radio_gender + "</usex>\n<ueducation>" + radio_education + "</ueducation>\n<umailid>" + mailId + "</umailid>\n<utraining>" + trainingCenter + "</utraining>\n <uothers>no Center</uothers>\n<ustateRanking>" + stateRank + "</ustateRanking>\n<unationalRank>" + nationalRank + "</unationalRank>\n<uphoto>" + imageString + "</uphoto>\n</user_details>\n";
        try {
            writeToTxtFile(xml);
            Log.e("getdata", "dataXml: " + xml);
            sendRequest(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(String xml) {
        new WebService(this).execute(API.ServerAddress + "" + API.USER_REGISTER, xml);
    }

    @Override
    public void onTaskComplete(String result) {
        Log.e("ontask complete", "Upload status" + result);
        String[] arrRes;
        arrRes = result.split(",");
        String locationXml;
        if (result.equals("Registered Successfully")) {
            Log.e("ViewUserDetails", "Upload status" + result);
        } else if (arrRes[0].equals("academy")) {
            locationXml = arrRes[1];
            parsexml.parse_xml_file(locationXml);
        } else {
            Toast.makeText(this, "Could not connect to server " + result, Toast.LENGTH_SHORT).show();
        }


    }

    public void validateEmailId(View view) {
        module = "verify_mailID";
        new WebService(this).execute(API.ServerAddress + "" + API.GENERATE_OTP, "mail_id=" + email_s + "&module=" + module);
    }
}