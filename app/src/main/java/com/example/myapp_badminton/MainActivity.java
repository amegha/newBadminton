package com.example.myapp_badminton;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.core.app.ActivityCompat;

import com.example.myapp_badminton.Location.City;
import com.example.myapp_badminton.Location.LocalLocation;
import com.example.myapp_badminton.Location.State;

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

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AsyncResponse, AdapterView.OnItemSelectedListener {

    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4192;
    private static final int CAMERA_REQUEST = 1888;
    private static final int REQUEST_RUNTIME_PERMISSIONS = 1;
    public static String result;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    final ArrayList<String> state_options = new ArrayList<String>();
    final ArrayList<String> city_options = new ArrayList<String>();
    AlertDialog alertDialog;
    DBHandler db;
    String imageString;
    String m_id, email_s, module;
    Parsexml parsexml;
    String[] academyResponse, cities, locations, acaNames, Scities, stateInfo, cityInfo, locationInfo, academyNameInfo, respectivePlaces;
    //    List<String> ;
    //otherpart
    MyDbAdapter helper;
    // ArrayAdapter<CharSequence> adapter;
    //for camera
    ImageView imageView;
    TextView image_name, age, dob;
    Button click, add, buttonConfirm, loginBack;
    String image_uri, sNewPass, sNewPassConfirm;
    //radio buttons and variables
    RadioGroup r_gender, r_edu;
    RadioButton male, female, school, college;
    String radio_gender, radio_Education, age_a;


    EditText fname, email, state_rank, national_rank, phoneNumber, editTextConfirmOtp;
    TextView showDate;
    ProgressDialog progressDialog;
    private Spinner location_spinner;
    private Spinner state_Spinner;
    private Spinner city_Spinner;
    private Spinner academy_spinner;
    private ArrayAdapter<String> stateArrayAdapter;
    private ArrayAdapter<String> cityArrayAdapter;
    /*private ArrayAdapter<Location.State> stateArrayAdapter;
    private ArrayAdapter<Location.City> cityArrayAdapter;
    private ArrayAdapter<Location.LocalLocation> locationArrayAdapter;*/
    private ArrayAdapter<String> locationArrayAdapter;
    private ArrayAdapter<String> academyArrayAdapter;
    private ArrayList<Location.State> statesList;
    private ArrayList<Location.City> citiesList;
    private ArrayList<Location.LocalLocation> locationList;
    private ConfirmOTP confirmOTP;
    private int nextState = 0, nextCity = 0;
    private int tempNextState = 0, tempNextCity = 0;
    private EditText newPass, confirmNewPass;
    private EditText etOTP;
    private String cityName;
    private String stateName;
    private String locationName;
    private AdapterView.OnItemSelectedListener location_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > -1) {
                locationName = (String) location_spinner.getItemAtPosition(position);
//                String cityName = (String) city_Spinner.getItemAtPosition(position);
/*

                cityArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, db.getCities(stateName));
                cityArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                city_Spinner.setAdapter(cityArrayAdapter);
*/

                academyArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, db.getAcademy(cityName, locationName));
                academyArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                academy_spinner.setAdapter(academyArrayAdapter);

//                location_spinner.setOnItemSelectedListener(location_listener);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener city_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > -1) {
                cityName = (String) city_Spinner.getItemAtPosition(position);
/*

                cityArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, db.getCities(stateName));
                cityArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                city_Spinner.setAdapter(cityArrayAdapter);
*/

                locationArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, db.getLocations(cityName));
                locationArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                location_spinner.setAdapter(locationArrayAdapter);

                location_spinner.setOnItemSelectedListener(location_listener);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    /*private AdapterView.OnItemSelectedListener country_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                final Country country = (Country) country_Spinner.getItemAtPosition(position);
                Log.d("SpinnerCountry", "onItemSelected: country: " + country.getCountryID());
                ArrayList<State> tempStates = new ArrayList<>();

                tempStates.add(new State(0, new Country(0, "Choose a Country"), "Choose a State"));

                for (State singleState : states) {
                    if (singleState.getCountry().getCountryID() == country.getCountryID()) {
                        tempStates.add(singleState);
                    }
                }

                stateArrayAdapter = new ArrayAdapter<State>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, tempStates);
                stateArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                state_Spinner.setAdapter(stateArrayAdapter);
            }

            cityArrayAdapter = new ArrayAdapter<City>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, new ArrayList<City>());
            cityArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            city_Spinner.setAdapter(cityArrayAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };*/
    private AdapterView.OnItemSelectedListener state_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > -1) {
                stateName = (String) state_Spinner.getItemAtPosition(position);
                Log.d("SpinnerCountry", "onItemSelected: state: ");

                cityArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, db.getCities(stateName));
                cityArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                city_Spinner.setAdapter(cityArrayAdapter);

                city_Spinner.setOnItemSelectedListener(city_listener);

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private String xmldate;
    private ProgressDialog sendOtpProgressDialog;

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

    public void verifyStoragePermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
            getAcademyInfo();

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RUNTIME_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    getAcademyInfo();


                } else {
                    Toast.makeText(this, "Grant permissions!!", Toast.LENGTH_LONG).show();
                    Log.i("Permission", "onRequestPermissionsResult: Permission Denied");
                }
                break;
            }

            case CAMERA_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    invokeCamera();
                } else {
                    Toast.makeText(this, "Allow camera to access!", Toast.LENGTH_SHORT).show();

                }

            }
        }
    }


    @Override
    protected void onStop() {
        try {
            super.onStop();
            db.deleteLocations();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Log.e("onCreate: ", "***from activity***" + this.getLocalClassName());
//        ActivityTracker.writeActivityLogs(this.getLocalClassName());
            setContentView(R.layout.activity_main);
            verifyStoragePermissions(this);
            db = new DBHandler(this);
            image_name = findViewById(R.id.image_name);
            dob = findViewById(R.id.tv_Dob);
            click = findViewById(R.id.click);
            //radio values IDSo
            male = findViewById(R.id.rb_male);
            female = findViewById(R.id.rb_female);
            school = findViewById(R.id.rb_school);
            college = findViewById(R.id.rb_college);
        /*autoTextState = findViewById(R.id.state);
        autoTextCity = findViewById(R.id.city);
        autoTextLocation = findViewById(R.id.location);
        autoTextAcademyName = findViewById(R.id.academy_name);*/
            parsexml = new Parsexml();
       /* stateInfo = new ArrayList();
        cityInfo = new ArrayList();
        locationInfo = new ArrayList();
        academyNameInfo = new ArrayList();*/
        /*player = findViewById(R.id.rb_player);
        coach = findViewById(R.id.rb_coach);
        mentor = findViewById(R.id.rb_mentor);*/

            initializeUI();
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
            phoneNumber = findViewById(R.id.et_phone_number);
            email = findViewById(R.id.et_email);
            state_rank = findViewById(R.id.et_stateRank);
            national_rank = findViewById(R.id.tv_nationalrank);


        /*fname.addTextChangedListener(loginTextWatcher);
        email.addTextChangedListener(loginTextWatcher);
        state_rank.addTextChangedListener(loginTextWatcher);
        national_rank.addTextChangedListener(loginTextWatcher);*/
            imageView = findViewById(R.id.image);


            add = findViewById(R.id.btn_add);


            age = findViewById(R.id.tv_age);
            showDate = findViewById(R.id.tv_Dob);
            if (Build.VERSION.SDK_INT > 9) {
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
//        click.setEnabled(false);
            add.setEnabled(false);
            //necessary  condition to run read or write to the file
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }*/

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Radio_gender_selected();
                    Radio_education_selected();
                    //                Radio_playerType_selected();
                    if ((male.isChecked() == true || female.isChecked() == true) && (college.isChecked() == true || school.isChecked() == true) /*&& (coach.isChecked() == true || player.isChecked() == true) || mentor.isChecked() == true*/) {
                        addUser();
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
//        getAcademyInfo();
        /*System.out.println("from mainActivity!!" + Collections.singletonList(parsexml.stateList));
        String[] statesArr = new String[parsexml.stateList.size()];
        statesArr = (String[]) parsexml.stateList.toArray(statesArr);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, statesArr);
        autoTextState.setThreshold(1);//will start working from first character
        autoTextState.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
//        autoTextState.setTextColor(Color.RED);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        try {
            state_Spinner = findViewById(R.id.state);
            city_Spinner = findViewById(R.id.city);
            location_spinner = findViewById(R.id.location);
            academy_spinner = findViewById(R.id.academy_name);

            statesList = new ArrayList<>();
            citiesList = new ArrayList<>();
            locationList = new ArrayList<>();
//        createLists();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void createLists() {
     /*   Country country0 = new Country(0, "Choose a Country");
        Country country1 = new Country(1, "Country1");
        Country country2 = new Country(2, "Country2");

        countries.add(new Country(0, "Choose a Country"));
        countries.add(new Country(1, "Country1"));
        countries.add(new Country(2, "Country2"));*/
/*
        Location.State state0 = new Location.State(0, "Choose a Country");
        Location.State state1 = new Location.State(1, "state1");
        Location.State state2 = new Location.State(2, country1, "state2");
        State state3 = new State(3, country2, "state3");
        State state4 = new State(4, country2, "state4");

        statesList.add(state0);
        citiesList.add(new City(0, country[i], state[i], "Choose a City"));
        locationList.add(new LocalLocation(0, state[i], city[i], "choose a locaation");*/
    }

    private void getAcademyInfo() {
        try {
            module = "academy";
            if (isConnected()) {
                new WebService(this).execute(API.ServerAddress + "" + API.GET_ACADEMY_INFO, "module=" + module);
            } else {
                Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            Intent intent;
            intent = new Intent(MainActivity.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //date calculation as well as age calculation
    private void ShowDatePickerDialog() {
        try {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    R.style.DialogTheme,
                    this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // month = month + 1;
        int age_cal;

        Calendar c = Calendar.getInstance();
        int endYear = c.get(Calendar.YEAR);
        int endMonth = c.get(Calendar.MONTH);
        endMonth++;
        int endDay = c.get(Calendar.DAY_OF_MONTH);

        //age calculation
        age_cal = (endYear - year);
        age_a = age_cal + "";
        //age validation
        if (age_cal <= 5) {
            Message.message(getApplicationContext(), " Age should be Grater than 5 !,please enter relevant data !");
        }
        age.setText("Age is " + age_a);
        age.setVisibility(View.VISIBLE);
        //DOB selected by user
        //String date = " " + dayOfMonth + "-" + month + "-" + year + "\n";
        //   String date = " " + year + "-" + month + "-" + dayOfMonth + "\n";
        c.set(year, month, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat ymdformat = new SimpleDateFormat("yyyy-MM-dd");
        // String date = sdf.format(" " + year + "-" + month + "-" + dayOfMonth + "\n");
        String date = sdf.format(c.getTime());
        xmldate = ymdformat.format(c.getTime());
        showDate.setText(date);
        dob.setText(date);
        /*try {
            xmldate = ymdformat.format(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

    }

    /*public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        try {
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
            xmldate = " " + year + "-" + month + "-" + dayOfMonth ;
            String date = " " + dayOfMonth + "-" + month + "-" + year ;
            showDate.setText(date);
            showDate.setTextSize(16);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/
/*
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


    }*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void OnTakePhotoClicked(View view) {
        try {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();

            } else {
                String[] permissionRequest = {Manifest.permission.CAMERA};
                requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void invokeCamera() {
        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            Toast.makeText(this, "Please wait..", Toast.LENGTH_SHORT).show();
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            imageString = Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("image ", "convertTobase64: " + imageString);
            add.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //Education Details
    public void Radio_education_selected() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //add data
    public void addUser() {
        try {
//            progressDialog = ProgressDialog.show(MainActivity.this, "Sending OTP!!", "Please wait..", false, false);
//            if (fieldEmpty())
            if (fname.getText().length() < 3) {
                fname.setError("short/empty");
                Toast.makeText(this, "Player name too short or empty", Toast.LENGTH_LONG).show();
            } else if (phoneNumber.getText().length() != 10 && phoneNumber.getText().length() != 13) {
                phoneNumber.setError("short");
                Toast.makeText(this, "Enter proper phone number", Toast.LENGTH_LONG).show();
            } else if ((email.getText().toString()).isEmpty()) {
                email.setError("Empty");
                Toast.makeText(this, "E-mail ID cannot be empty", Toast.LENGTH_LONG).show();
            } else if ((showDate.getText().toString()).isEmpty()) {
                showDate.setError("Empty");
                Toast.makeText(this, "DOB cannot be empty", Toast.LENGTH_LONG).show();
            } else {
                final String name = fname.getText().toString();
                final String phone_user = phoneNumber.getText().toString();
                final String mailId = email.getText().toString();
                String stateRank = state_rank.getText().toString();
                String nationalRank = national_rank.getText().toString();
                final String image_uri = this.image_uri;
                final String image_uri_data = imageString;
                final String age = age_a;
                final String dob = showDate.getText().toString();
                final String email_s = showDate.getText().toString();
                final String stateSpinner = state_Spinner.getSelectedItem().toString();
                final String citySpinner = city_Spinner.getSelectedItem().toString();
                final String locationSpinner = location_spinner.getSelectedItem().toString();
                final String academySpinner = academy_spinner.getSelectedItem().toString();

                if (stateRank.isEmpty()) {
                    stateRank = "none";
                }
                if (nationalRank.isEmpty()) {
                    nationalRank = "none";
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final String timestamp1 = sdf.format(new Date());
                String state;
                state = Environment.getExternalStorageState();
                m_id = mailId;
                if (Environment.MEDIA_MOUNTED.equals(state)) {

                    File Root = getExternalStorageDirectory();
                    File Dir = new File(Root.getAbsolutePath() + "/MyFile");
                    if (!Dir.exists()) {
                        Dir.mkdir();
                    }
                    File file = new File(Dir, "Message.xml");
                }
               /* if (name.isEmpty()
                        || mailId.isEmpty()
                        || stateSpinner.isEmpty()
                        || citySpinner.isEmpty()
                        || locationSpinner.isEmpty()
                        || academySpinner.isEmpty()) {

                    Message.message(getApplicationContext(), "Enter all Text Fields as well as select Proper Radio Fields !!");
                } else {*/

                //            long id = helper.insertData(etName, password_user, enc_password, mailId, trainingCenter, stateRank, nationalRank, image_uri_data, radio_gender, radio_Education, radio_playerType, age, dob, timestamp1);
                add.setEnabled(false);
                formXMl(name, phone_user, mailId, stateSpinner, citySpinner, locationSpinner, academySpinner, stateRank, nationalRank, image_uri_data, radio_gender, radio_Education, age, xmldate, timestamp1);
                //            }// end of else
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        progressDialog.dismiss();

    }

    private boolean fieldEmpty() {

        String name_s = fname.getText().toString().trim();
        email_s = email.getText().toString().trim();
        final String phone_user = phoneNumber.getText().toString();
        final String dob = showDate.getText().toString();


//        !name_s.isEmpty() && !email_s.isEmpty() && !s_rank.isEmpty() && !n_rank.isEmpty()
        return name_s.isEmpty() || email_s.isEmpty() || phone_user.isEmpty() || dob.isEmpty() || (phone_user.length() != 10 && phone_user.length() != 13);


    }

    private void formXMl(String name, String phone, String mailId, String state, String
            city, String location, String academy, String stateRank, String nationalRank, String
                                 image_uri, String radio_gender, String radio_education, String age, String dob, String
                                 timestamp1) {
        try {
            Log.e("getdata", "entered form xml: ");

            String xml = "<user_details>\n" +
                    "<userName>" + name + "</userName>\n" +
                    "<phoneNumber>" + phone + "</phoneNumber>\n" + //                "<etPassword>" + radio_playerType + "</etPassword>\n" +
                    "<uAge>" + age + "</uAge>\n" +
                    "<uDob>" + xmldate + "</uDob>\n" +
                    "<usex>" + radio_gender + "</usex>\n" +
                    "<ueducation>" + radio_education + "</ueducation>\n" +
                    "<umailid>" + mailId + "</umailid>\n" +
                    "<state>" + state + "</state>\n" +
                    "<city>" + city + "</city>\n" +
                    "<location>" + location + "</location>\n" +
                    "<academyName>" + academy + "</academyName>\n " +
                    "<uothers>no Center</uothers>\n" +
                    "<ustateRanking>" + stateRank + "</ustateRanking>\n" +
                    "<unationalRank>" + nationalRank + "</unationalRank>\n" +
                    "<uphoto>" + image_uri + "</uphoto>\n" +
                    "</user_details>\n";
            try {
                //   writeToTxtFile(xml);
                Log.e("getdata", "dataXml: " + xml);
                sendRequest(xml);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(String xml) {
        try {
            if (isConnected()) {
                progressDialog = ProgressDialog.show(MainActivity.this, "Sending OTP!!", "Please wait..", false, false);

                new WebService(this).execute(API.ServerAddress + "" + API.USER_REGISTER, xml);
            } else {
                Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskComplete(String result) {
        try {
            Log.e("ontask complt", "response" + result);
            progressDismiss();
            if (result.equals("pre_registration/0/pre_reg ")) {
//                sendOtpProgressDialog.dismiss();
                createConfirmOTPAlertDialog();
            } else if (result.equals("register/0/confirmOTP")) {
                clearFields();
                alertDialog.dismiss();
                Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
                createResetPasswordAlertDialog();

            } else if (result.equals("password_reset/0")) {
                startActivity(new Intent(this, Login.class));
                finish();
                //            signIn(m_id, sNewPass);
            } else if (result.equals("103")) {
//                sendOtpProgressDialog.dismiss();
                email.setError("Already registered");
                Toast.makeText(this, "Already registered", Toast.LENGTH_SHORT).show();
            } else if (result.equals("203")) {//wrong otp
                etOTP.setError("wrong!");

            } else {
                progressDismiss();
                String[] arrRes;
                arrRes = result.split("/");
                String locationXml;
                if (arrRes[0].equals("academy")) {
                    locationXml = arrRes[1];
                    parseResponseSimple(locationXml);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void progressDismiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void clearFields() {
        fname.setText("");
        phoneNumber.setText("");
        email.setText("");
        showDate.setText("");
        dob.setText("");
        state_rank.setText("");
        national_rank.setText("");
        imageView.setImageResource(android.R.color.transparent);
    }
    /*private void signIn(String regEmail, String password) {
        if (isConnected()) {new WebService(this).execute(API.ServerAddress + API.USER_LOGIN, "mail_id=" + regEmail + "&password=" + password);

    }*/

    private void createConfirmOTPAlertDialog() {
        try {
            LayoutInflater li = LayoutInflater.from(this);
            View confirmDialog = li.inflate(R.layout.dialog_confirm, null);
            etOTP = confirmDialog.findViewById(R.id.editTextOtp);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setView(confirmDialog);
            alertDialog = alert.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*    @Override
    public void onTaskComplete(String result) {
        try {
            Log.e("ontask complete", "Upload status" + result);
            String[] arrRes;
            arrRes = result.split("/");
            String locationXml;
            Log.e("ViewUserDetails", "arrRes[0]" + arrRes[0] + " arrRes[1] " + arrRes[1]);
            if (arrRes[1].equals("0 ")) { // space is added
                if (arrRes[0].equals("pre_registration")) {//coming from pre_register after sending the otp to the mailid
                    LayoutInflater li = LayoutInflater.from(this);
                    View confirmDialog = li.inflate(R.layout.dialog_confirm, null);
                    buttonConfirm = confirmDialog.findViewById(R.id.buttonConfirm);
                    editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setView(confirmDialog);
                    alertDialog = alert.create();
                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(false);
                }
            } else if (arrRes[0].equals("register")) {
                if (arrRes[1].equals("0")) {
                    createResetPasswordAlertDialog();

                }
            } else if (result.equals("password_reset/0")) {

            } else {
                Toast.makeText(this, "something went wrong!", Toast.LENGTH_SHORT).show();
            }
            if (arrRes[0].equals("academy")) {
                locationXml = arrRes[1];
//            parsexml.parse_xml_file(locationXml);
//            parseResponse(locationXml);
//            parseResponseNew(locationXml);
                parseResponseSimple(locationXml);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }


    }*/

    private void createResetPasswordAlertDialog() {
        try {
            progressDismiss();
            LayoutInflater li = LayoutInflater.from(this);
            View confirmDialog = li.inflate(R.layout.activity_reset_password, null);
            newPass = confirmDialog.findViewById(R.id.pass_new);
            confirmNewPass = confirmDialog.findViewById(R.id.pass_confirm);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setView(confirmDialog);
            alertDialog = alert.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseResponseSimple(String locationXml) {
        try {
            academyResponse = locationXml.split(";");
            for (int i = 0; i < academyResponse.length; i++) {
                db.storeLocationInfo(academyResponse[i]);
            }
            populateSpinner();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseResponseNew(String result) {
        academyResponse = result.split("-");
        stateInfo = (academyResponse[0].split(","));
        for (int stateCount = 0; stateCount < stateInfo.length; stateCount++) {
            nextState = 0;
            statesList.add(new State(stateCount, stateInfo[stateCount]));
            System.out.println("stateList: " + Collections.singletonList(statesList));
            cityInfo = (academyResponse[1].split(";")); //same state cities are seperated by ;chennai;Hydrabaad;Belagavi.Bengaluru

            for (int cityInfoCount = tempNextState; cityInfoCount < cityInfo.length; cityInfoCount++) {
                nextCity = 0;
                if (tempNextState > 0)
                    break;
                System.out.println("split by dot " + cityInfo[cityInfoCount].split("@").length);
                if (1 < cityInfo[cityInfoCount].split("@").length) { //if more cities are the
                    respectivePlaces = cityInfo[cityInfoCount].split("@");
                    for (int respectiveCitiesCount = 0; respectiveCitiesCount < respectivePlaces.length; respectiveCitiesCount++) {
                        citiesList.add(new City(cityInfoCount, statesList.get(stateCount), respectivePlaces[respectiveCitiesCount]));
                        Log.e("loc ", "---->" + cityInfoCount + "\n" +
                                statesList.get(stateCount) + "\n" +
                                respectivePlaces[respectiveCitiesCount]);
                    }
                } else {
                    citiesList.add(new City(cityInfoCount, statesList.get(stateCount), cityInfo[cityInfoCount]));
                    tempNextState = nextState + 1;
                    Log.e("loc 1 ", "---->" + cityInfoCount + "\n" +
                            statesList.get(stateCount) + "\n" +
                            cityInfo[cityInfoCount]);

                }
//                locationInfo = (academyResponse[2].split(";"));
//                System.out.println("LocationInfo" + Collections.singletonList(locationInfo));
                locationInfo = (academyResponse[2].split(";")); //same state cities are seperated by ;chennai;Hydrabaad;Belagavi.Bengaluru
                for (int locationInfoCount = tempNextCity; locationInfoCount < locationInfo.length; locationInfoCount++) {
                    if (nextCity > 0)
                        break;
                    if (1 < locationInfo[locationInfoCount].split("@").length) {
                        respectivePlaces = locationInfo[locationInfoCount].split("@");
                        for (int respectiveCitiesCount = 0; respectiveCitiesCount < respectivePlaces.length; respectiveCitiesCount++) {
                            locationList.add(new LocalLocation(locationInfoCount, statesList.get(stateCount), citiesList.get(cityInfoCount), respectivePlaces[respectiveCitiesCount]));
                            /*System.out.println(cityInfoCount + "\n" +
                                    statesList.get(cityInfoCount) + "\n" +
                                    respectivePlaces[respectiveCitiesCount]);*/
                        }
                    } else {
                        locationList.add(new LocalLocation(locationInfoCount, statesList.get(stateCount), citiesList.get(cityInfoCount), locationInfo[locationInfoCount]));
                        tempNextCity = nextCity + 1;
                    }
                }

                academyNameInfo = (academyResponse[3].split(";"));
                System.out.println("academyNameInfo " + Collections.singletonList(statesList));
                System.out.println("academyNameInfo " + Collections.singletonList(citiesList));
                System.out.println("academyNameInfo " + Collections.singletonList(locationList));
            }
        }

//        populateTheAutoText();
//        locationValidation();
        populateSpinner();

    }

    private void parseResponse(String result) {
        academyResponse = result.split("-");
        for (int i = 0; i < academyResponse.length; i++) {

            if (i == 0) {
                stateInfo = (academyResponse[0].split(","));
                for (int stateCount = 0; stateCount < stateInfo.length; stateCount++) {
                    statesList.add(new State(stateCount, stateInfo[stateCount]));
                }
//                stateInfo.add(academyResponse[0].split(","));
//                System.out.println("stateInfo " + Collections.singletonList(stateInfo))
                System.out.println("stateList: " + Collections.singletonList(statesList));

            } else if (i == 1) {
                cityInfo = (academyResponse[1].split(";")); //same state cities are seperated by ;chennai;Hydrabaad;Belagavi.Bengaluru
                for (int cityInfoCount = 0; cityInfoCount < cityInfo.length; cityInfoCount++) {
                    System.out.println("split by dot " + cityInfo[cityInfoCount].split("@").length);
                    if (1 < cityInfo[cityInfoCount].split("@").length) {
                        respectivePlaces = cityInfo[cityInfoCount].split("@");
                        for (int respectiveCitiesCount = 0; respectiveCitiesCount < respectivePlaces.length; respectiveCitiesCount++) {
                            citiesList.add(new City(cityInfoCount, statesList.get(cityInfoCount), respectivePlaces[respectiveCitiesCount]));
                            System.out.println(cityInfoCount + "\n" +
                                    statesList.get(cityInfoCount) + "\n" +
                                    respectivePlaces[respectiveCitiesCount]);
                        }
                    } else {
                        citiesList.add(new City(cityInfoCount, statesList.get(cityInfoCount), cityInfo[cityInfoCount]));
                    }
                }
            } else if (i == 2) {
//                locationInfo = (academyResponse[2].split(";"));
//                System.out.println("LocationInfo" + Collections.singletonList(locationInfo));
                for (int stateCount = 0; stateCount < stateInfo.length; stateCount++) {
                    for (int cityCount = 0; cityCount < cityInfo.length; cityCount++) {
                        locationInfo = (academyResponse[2].split(";")); //same state cities are seperated by ;chennai;Hydrabaad;Belagavi.Bengaluru
                        for (int locationInfoCount = 0; locationInfoCount < locationInfo.length; locationInfoCount++) {
                            System.out.println("split by dot " + locationInfo[locationInfoCount].split("@").length);
                            if (1 < locationInfo[locationInfoCount].split("@").length) {
                                respectivePlaces = locationInfo[locationInfoCount].split("@");
                                for (int respectiveCitiesCount = 0; respectiveCitiesCount < respectivePlaces.length; respectiveCitiesCount++) {
                                    locationList.add(new LocalLocation(locationInfoCount, statesList.get(locationInfoCount), citiesList.get(locationInfoCount), respectivePlaces[respectiveCitiesCount]));
                            /*System.out.println(cityInfoCount + "\n" +
                                    statesList.get(cityInfoCount) + "\n" +
                                    respectivePlaces[respectiveCitiesCount]);*/
                                }
                            } else {
                                locationList.add(new LocalLocation(locationInfoCount, statesList.get(locationInfoCount), citiesList.get(locationInfoCount), locationInfo[locationInfoCount]));
                            }
                        }
                    }
                }

            } else if (i == 3) {
                academyNameInfo = (academyResponse[3].split(";"));
                System.out.println("academyNameInfo " + Collections.singletonList(academyNameInfo));
            }

        }
//        populateTheAutoText();
//        locationValidation();
//        populateSpinner();

    }

    private void populateSpinner() {
        try {
            stateArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, db.getStates());
            stateArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
            state_Spinner.setAdapter(stateArrayAdapter);
            state_Spinner.setOnItemSelectedListener(state_listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void validateEmailId(View view) {
        module = "verify_mailID";
        if (isConnected()) {

            new WebService(this).execute(API.ServerAddress + "" + API.GENERATE_OTP, "mail_id=" + email_s + "&module=" + module);
        } else {
            Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show();
        }
    }

    public void validateOTP(View view) {
        try {
//            alertDialog.dismiss();
            module = "register";
            //Displaying a progressbarmail_id
            progressDialog = ProgressDialog.show(MainActivity.this, "Verifying!!", "Please wait..", false, false);
            final String otp = etOTP.getText().toString().trim();
            confirmOTP = new ConfirmOTPImpl(m_id, new WebService(this), module, "confirmOTP", otp);
            confirmOTP.confirmOtp();
//        if (isConnected()) {new WebService(this).execute(API.ServerAddress + "" + API.CONFIRM_OTP, "module=" + module + "&otp=" + otp + "&mail_id=" + m_id+"intent");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void reEnter(View view) {
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

//            player.setChecked(true);
//            coach.setChecked(false);
//            mentor.setChecked(false);
        this.age.setVisibility(View.GONE);
        //academyName.setOnItemSelectedListener(this);
//            academyName.setSelection(0);
        imageView.setVisibility(View.GONE);

    }

    public void resetPasswordOrPin(View view) {
        try {
            alertDialog.dismiss();
            Log.e("reset", "resetPasswordOrPin: " + m_id);
            progressDialog = ProgressDialog.show(this, "Password setting", "Please wait..", false, false);
            sNewPass = newPass.getText().toString();
            sNewPassConfirm = confirmNewPass.getText().toString();
            if (sNewPass.equals(sNewPassConfirm)) {
                alertDialog.dismiss();
                if (isConnected()) {
                    new WebService(this).execute(API.ServerAddress + API.RESET_PASSWORD, "module=password_reset" + "&mail_id=" + m_id + "&new_pin=" + sNewPass);
                } else {
                    Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show();
                }
            } else {
                confirmNewPass.setError("password doesn't match");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        progressDialog.dismiss();
    }

    /*public void bypassReg(View view) {
        if (isConnected()) {new WebService(this).execute(API.ServerAddress + "" + API.USER_PRE_REGISTER*//*, xml*//*);

    }*/
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





