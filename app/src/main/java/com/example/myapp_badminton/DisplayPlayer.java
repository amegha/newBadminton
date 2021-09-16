package com.example.myapp_badminton;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DisplayPlayer extends AppCompatActivity implements AsyncResponse {
    ArrayList<String> arrayListId;
    ArrayList<String> arrayListName;
    ArrayList<String> arrayListImage;
    ArrayList<String> arrayListLastdate;
    Toolbar toolbar;
    SQLiteDatabase db;
    byte[] imagebytes;
    String imageString, date, coach_name, playername, pid, cid, level, academy, ID, Name, type, AID, fragment_module;
    Bitmap decodedImage;
    Cursor cursor, cursor1;
    databaseConnectionAdapter datahelper;
    private ExampleAdapter adapter;
    private List<ExampleItem> exampleList;
    private ArrayList<String> Ids, names, image, lastdate,regDate;
    private String scoreFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
//        ActivityTracker.writeActivityLogs(this.getLocalClassName());
            //Log.e("onCreate: ", "***from activity***" + this.getLocalClassName());
            setContentView(R.layout.activity_display_player);
            datahelper = new databaseConnectionAdapter(getApplicationContext());
            db = datahelper.allDataHelper.getReadableDatabase();

            //decodedImage=datahelper.allDataHelper.retreiveImageFromDB(db);


            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            date = bundle.getString("date");
            coach_name = bundle.getString("coachname");
            cid = bundle.getString("coach_id");
            level = bundle.getString("level");
            academy = bundle.getString("Academy");
            type = bundle.getString("type");
            scoreFilter = bundle.getString("scoreFilter");
            AID = bundle.getString("aid");
            fragment_module = bundle.getString("module");

            Ids = new ArrayList<>();
            names = new ArrayList<>();
            image = new ArrayList<>();
            lastdate = new ArrayList<>();
            regDate=new ArrayList<>();
            exampleList = new ArrayList<>();

//            new WebService(DisplayPlayer.this).execute(  "player_details.php", "academy_id=" + AID + "&level=" + level + "&coach_id=" + cid);
            new WebService(DisplayPlayer.this).execute(  API.PLAYER_DETAILS, "academy_id=" + AID + "&level=" + level + "&coach_id=" + cid);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
/*
    private List<ExampleItem> fillExampleList() {
        try {
            exampleList = new ArrayList<>();
            for (int i = 0, j = 0, k = 0; i < arrayListName.size() && j < arrayListId.size() && k < arrayListImage.size(); i++, j++, k++) {
                Name = arrayListName.get(i);
                ID = arrayListId.get(j);
                imageString = arrayListImage.get(k);
                date = arrayListLastdate.get(i);
                imagebytes = Base64.decode(imageString, Base64.DEFAULT);
                decodedImage = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);


                exampleList.add(new ExampleItem(decodedImage, Name, ID));
                // exampleList.add(new ExampleItem(exampleItem.getImageResource(),exampleItem.getText1(),exampleItem.getText2()));
            }
            return exampleList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    private void setUpRecyclerView() {
        try {
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            toolbar = findViewById(R.id.toolbar);

            this.setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle("");

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            // adapter = new ExampleAdapter(exampleList);
            adapter = new ExampleAdapter(exampleList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {


                    if (fragment_module.equalsIgnoreCase("ScoreEntry")) {
                        exampleList.get(position);

                        Bundle b = new Bundle();
                        b.putString("coach_id", cid);
                        b.putString("coachname", coach_name);
                        b.putString("date", lastdate.get(position));
                        b.putString("regDate", regDate.get(position));
                        b.putString("level", level);
                        b.putString("Academy", academy);
                        b.putString("type", type);
                        b.putString("aid", AID);
                        b.putString("module", fragment_module);
                        b.putParcelable("Player Details", exampleList.get(position));
                        Intent intent = new Intent(DisplayPlayer.this, ScoreFrom.class).putExtras(b);
                        intent.putExtra("Player Details", exampleList.get(position));
                        startActivity(intent);
                    } else if (fragment_module.equalsIgnoreCase("LineGraph")) {
                        Bundle b = new Bundle();
                        b.putString("coach_id", cid);
                        b.putString("coachname", coach_name);
                        b.putString("date", lastdate.get(position));
                        b.putString("level", level);
                        b.putString("Academy", academy);
                        b.putString("type", type);
                        b.putString("scoreFilter",scoreFilter);

                        b.putString("aid", AID);
                        b.putString("module", fragment_module);
                        b.putParcelable("Player Details", exampleList.get(position));
                        Intent intent = new Intent(DisplayPlayer.this, PlayerPerformance.class).putExtras(b);
                        //  intent.putExtra("Player Details", exampleList.get(position));
                        startActivity(intent);
                    } else if (fragment_module.equalsIgnoreCase("BarGraph")) {
                        Bundle b = new Bundle();
                        b.putString("coach_id", cid);
                        b.putString("coachname", coach_name);
                        b.putString("date", lastdate.get(position));
                        b.putString("level", level);
                        b.putString("Academy", academy);
                        b.putString("type", type);
                        b.putString("aid", AID);
                        b.putString("module", fragment_module);
                        b.putParcelable("Player Details", exampleList.get(position));
                        Intent intent = new Intent(DisplayPlayer.this, GraphDisplay.class).putExtras(b);
                        //  intent.putExtra("Player Details", exampleList.get(position));
                        startActivity(intent);
                    }else if (fragment_module.equalsIgnoreCase("playGameScore")) {
                        Bundle b = new Bundle();
                        b.putString("coach_id", cid);
                        b.putString("coachname", coach_name);
                        b.putString("date", lastdate.get(position));
                        b.putString("level", level);
                        b.putString("Academy", academy);
                        b.putString("type", type);
                        b.putString("aid", AID);
                        b.putString("module", fragment_module);
                        b.putParcelable("Player Details", exampleList.get(position));
                        Intent intent = new Intent(DisplayPlayer.this, DisplayPlayerDashboardForCoach.class).putExtras(b);
                        //  intent.putExtra("Player Details", exampleList.get(position));
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            MenuItem searchItem = menu.findItem(R.id.search_view);

            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return true;
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            int id = item.getItemId();
            if (id == R.id.search_view) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onTaskComplete(String result) {
        try {
            String[] academyResponse;
            academyResponse = result.split(";");
            for (int i = 0; i < academyResponse.length; i++) {

                ImageInfo(academyResponse[i], i);
                //Log.d("Total display Player", String.valueOf(academyResponse.length));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void ImageInfo(String s, int i) {
        try {
            String[] locInfo = s.split(",");
            Ids.add(locInfo[0]);
            names.add(locInfo[1]);
            image.add(locInfo[2]);
            lastdate.add(locInfo[4]);
            regDate.add(locInfo[5]);

            /*arrayListId = Ids;
            arrayListName = names;
            arrayListImage = image;*/
            arrayListLastdate = lastdate;
//            fillExampleList();

            imagebytes = Base64.decode(locInfo[2], Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
            exampleList.add(new ExampleItem(decodedImage, names.get(i), Ids.get(i)));

            setUpRecyclerView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public void onBackPressed() {
        Intent i = new Intent(DisplayPlayer.this, Coach.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();

    }*/
}