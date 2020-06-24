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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTracker.writeActitivtyLogs(this.getLocalClassName());
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
        AID = bundle.getString("aid");
        fragment_module = bundle.getString("module");

        new WebService(DisplayPlayer.this).execute(API.ServerAddress + "player_details.php", "academy_id=" + AID + "&level=" + level + "&coach_id=" + cid);


    }

    private List<ExampleItem> fillExampleList() {
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
    }

    private void setUpRecyclerView() {
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
                    b.putString("date", date);
                    b.putString("level", level);
                    b.putString("Academy", academy);
                    b.putString("type", type);
                    b.putString("aid", AID);
                    b.putString("module", fragment_module);
                    b.putParcelable("Player Details", exampleList.get(position));
                    Intent intent = new Intent(DisplayPlayer.this, Score_From.class).putExtras(b);
                    intent.putExtra("Player Details", exampleList.get(position));
                    startActivity(intent);
                } else if (fragment_module.equalsIgnoreCase("LineGraph")) {
                    Bundle b = new Bundle();
                    b.putString("coach_id", cid);
                    b.putString("coachname", coach_name);
                    b.putString("date", date);
                    b.putString("level", level);
                    b.putString("Academy", academy);
                    b.putString("type", type);
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
                    b.putString("date", date);
                    b.putString("level", level);
                    b.putString("Academy", academy);
                    b.putString("type", type);
                    b.putString("aid", AID);
                    b.putString("module", fragment_module);
                    b.putParcelable("Player Details", exampleList.get(position));
                    Intent intent = new Intent(DisplayPlayer.this, GraphDisplay.class).putExtras(b);
                    //  intent.putExtra("Player Details", exampleList.get(position));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_view) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(String result) {
        /* if(result.equals("Success")){*/
          Log.e("ViewUserDetails","Upload status "+result);
        String[] arrRes;
        arrRes = result.split(",");
        String locationXml;
        Log.e("ViewUserDetails", " arrRes[0] " + arrRes[0] + " arrRes[1]  " + arrRes[1] + "  arrRes[2]" + arrRes[2]);

        String[] academyResponse;
        ArrayList<String> Ids = new ArrayList();
        ArrayList<String> name = new ArrayList();
        ArrayList<String> image = new ArrayList<>();
        ArrayList<String> lastdate = new ArrayList<>();

        academyResponse = result.split(";");
        for (int i = 0; i < academyResponse.length; i++) {
            ImageInfo(academyResponse[i], Ids, name, image, lastdate);
            Log.d("Total display Player", String.valueOf(academyResponse.length));
        }
        System.out.println("all the Ids " + Collections.singletonList(Ids));
        System.out.println("all the names " + Collections.singletonList(name));
        System.out.println("all Images" + Collections.singleton(image));


    }

    private void ImageInfo(String s, ArrayList<String> Ids, ArrayList<String> names, ArrayList<String> image, ArrayList<String> lastdate) {
        String[] locInfo = s.split(",");
        Ids.add(locInfo[0]);
        names.add(locInfo[1]);
        image.add(locInfo[2]);
        lastdate.add(locInfo[4]);
        arrayListId = Ids;
        Log.e("ArrayListId ", "Id =" + arrayListId);
        arrayListName = names;
        Log.e("ArrayListName ", "Id =" + arrayListName);
        arrayListImage = image;
        Log.e("ArrayListImage ", "Id =" + arrayListImage);
        arrayListLastdate = lastdate;
        fillExampleList();
        setUpRecyclerView();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(DisplayPlayer.this, Coach.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();

    }
}