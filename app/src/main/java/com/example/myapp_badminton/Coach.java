package com.example.myapp_badminton;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class Coach extends AppCompatActivity implements AsyncResponse {

    public String[] academy = {};
    String Academy, aid, date, coach_id, coach_name, city, levels, type, fragment_module, graph_cid,scoreFilter;
    Button click, btn_city, btn_academy;
    Cursor cursor;
    databaseConnectionAdapter datahelper;
    SQLiteDatabase db;

    String cityName, academyName, locationName;

    ArrayAdapter<String> adapter_academy;
    ArrayAdapter<String> adapter_city;
    ArrayAdapter<String> adapter_location;
    ArrayAdapter<String> adapter_levels;

    private Spinner city_Spinner, academy_spinner, level_spinner, location_Spinner;
    private AdapterView.OnItemSelectedListener getAcademyLister = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > -1) {
                academyName = (String) academy_spinner.getItemAtPosition(position);
                adapter_levels = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, datahelper.allDataHelper.getLevels(cityName, locationName, academyName));
                adapter_levels.setDropDownViewResource(R.layout.spinner_item);
                level_spinner.setAdapter(adapter_levels);
                aid = datahelper.allDataHelper.getAid(cityName, locationName, academyName);

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener locationListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > -1) {
                locationName = (String) location_Spinner.getItemAtPosition(position);
                adapter_academy = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, datahelper.allDataHelper.getAcademies(cityName, locationName));
                adapter_academy.setDropDownViewResource(R.layout.spinner_item);
                academy_spinner.setAdapter(adapter_academy);
                academy_spinner.setOnItemSelectedListener(getAcademyLister);

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener cityListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > -1) {
                cityName = (String) city_Spinner.getItemAtPosition(position);
                Log.d("SpinnerCountry", "onItemSelected: state: ");

                adapter_location = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, datahelper.allDataHelper.getLocations(cityName));
                adapter_location.setDropDownViewResource(R.layout.spinner_item);
                location_Spinner.setAdapter(adapter_location);

                location_Spinner.setOnItemSelectedListener(locationListener);

            } else {
                city_Spinner.setPrompt("select");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
//        ActivityTracker.writeActivityLogs(this.getLocalClassName());
            Log.e("onCreate: ","***from activity***"+this.getLocalClassName() );

            setContentView(R.layout.activity_coach);

            datahelper = new databaseConnectionAdapter(getApplicationContext());
            db = datahelper.allDataHelper.getReadableDatabase();

            city_Spinner = findViewById(R.id.sp_city);
            location_Spinner = findViewById(R.id.sp_location);
            level_spinner = findViewById(R.id.sp_level);
            academy_spinner = findViewById(R.id.sp_academy);

            Intent intent = getIntent();
            Bundle bundle1 = intent.getExtras();
            date = bundle1.getString("date");
            coach_name = bundle1.getString("x");
            coach_id = bundle1.getString("userid");
            type = bundle1.getString("type");
            scoreFilter = bundle1.getString("scoreFilter");

            fragment_module = bundle1.getString("Module");
            if (fragment_module.equalsIgnoreCase("LineGraph") || fragment_module.equalsIgnoreCase("playGameScore")) {
                coach_id = bundle1.getString("id");
            } else {
                coach_id = bundle1.getString("userid");
            }
            if (isConnected()) {
//                new WebService(this).execute(API.ServerAddress + "get_academy.php", "module=coach&coach_id=" + coach_id);
                new WebService(this).execute(API.ServerAddress + API.GET_ACADEMY_INFO, "module=coach&coach_id=" + coach_id);
            } else {
                Toast.makeText(this, "you are offline", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isConnected() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch slice
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onTaskComplete(String result) {
        try {
            Log.e("onTaskComplete: ", "" + result);
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
                    String[] academyResponse;
                    ArrayList<String> cities = new ArrayList();
                    ArrayList<String> academy_name = new ArrayList();
                    ArrayList<String> aid = new ArrayList<>();
                    ArrayList<String> gamelevels = new ArrayList<>();
                    ArrayList<String> state = new ArrayList<>();
                    ArrayList<String> area = new ArrayList<>();

                    academyResponse = result.split(";");
                    for (int i = 0; i < academyResponse.length; i++) {
                        LocationInfo(academyResponse[i], cities, academy_name, aid, gamelevels, state, area);
                        Log.d("Total Size", String.valueOf(academyResponse.length));
                    }
                    System.out.println("all the cities " + Collections.singletonList(cities));
                    System.out.println("all the Academy Ids " + Collections.singletonList(aid));
                    System.out.println("all the academy names " + Collections.singletonList(academy_name));
                    //used to display city
                    adapter_city = new ArrayAdapter<String>(this, R.layout.spinner_item, datahelper.allDataHelper.getCity());
                    adapter_city.setDropDownViewResource(R.layout.spinner_item);
                    city_Spinner.setAdapter(adapter_city);
                    city_Spinner.setOnItemSelectedListener(cityListener);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void LocationInfo(String s, ArrayList<String> cities, ArrayList<String> academy_name, ArrayList<String> aid, ArrayList<String> gamelevels, ArrayList<String> states, ArrayList<String> area) {
        try {
            String[] locInfo = s.split(",");
            Log.d("Total Location Size", String.valueOf(locInfo.length));
            aid.add(locInfo[0]);
            academy_name.add(locInfo[1]);
            states.add(locInfo[2]);
            cities.add(locInfo[3]);
            area.add(locInfo[4]);
            gamelevels.add(locInfo[5]);


            for (int i = 0; i < cities.size(); i++) {
                datahelper.allDataHelper.insertAcademyDatas(aid.get(i), academy_name.get(i), states.get(i), cities.get(i), area.get(i), gamelevels.get(i));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

 /*   @Override
    public void onBackPressed() {
        Intent i = new Intent(Coach.this, HomePage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();
    }*/

    public void select_level(View view) {
        try {
            //used to pass data to display player class
            if (fragment_module.equalsIgnoreCase("ScoreEntry")) {
                Bundle b = new Bundle();
                b.putString("coach_id", coach_id);
                b.putString("coachname", coach_name);
                b.putString("date", date);
                b.putString("level", level_spinner.getSelectedItem().toString());
                b.putString("Academy", academy_spinner.getSelectedItem().toString());
                b.putString("type", type);
                b.putString("aid", aid);
                b.putString("module", fragment_module);
    //            startActivity(new Intent(Coach.this, DisplayPlayer.class));
                Intent i1 = new Intent(Coach.this, DisplayPlayer.class).putExtras(b);
                startActivity(i1);
            } else if (fragment_module.equalsIgnoreCase("LineGraph")) {
                Bundle b = new Bundle();
                b.putString("coach_id", coach_id);
                b.putString("coachname", coach_name);
                b.putString("date", date);
                b.putString("level", level_spinner.getSelectedItem().toString());
                b.putString("Academy", academy_spinner.getSelectedItem().toString());
                b.putString("type", type);
                b.putString("scoreFilter",scoreFilter);
                b.putString("aid", aid);
                b.putString("module", fragment_module);
                Intent i1 = new Intent(Coach.this, DisplayPlayer.class).putExtras(b);
                startActivity(i1);
    //            datahelper.allDataHelper.delete();
            } else if (fragment_module.equalsIgnoreCase("playGameScore")) {
                Bundle b = new Bundle();
                b.putString("coach_id", coach_id);
                b.putString("coachname", coach_name);
                b.putString("date", date);
                b.putString("level", level_spinner.getSelectedItem().toString());
                b.putString("Academy", academy_spinner.getSelectedItem().toString());
                b.putString("type", type);
                b.putString("aid", aid);
                b.putString("module", fragment_module);
                Intent i1 = new Intent(Coach.this, DisplayPlayer.class).putExtras(b);
                startActivity(i1);
    //            datahelper.allDataHelper.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {

        try {
            super.onStop();
            Log.e("onStop: ", "onstop called");
//            datahelper.allDataHelper.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        datahelper.allDataHelper.delete();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("pause: ", "pause called");

    }

}