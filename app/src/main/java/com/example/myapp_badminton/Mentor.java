package com.example.myapp_badminton;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class Mentor extends AppCompatActivity {

    EditText et_city,et_academy,et_student_name;
    String   city,stud_name,academy,stud_id,date,coach_id,coach_name;
    Button click;
    Cursor cursor;
    MyDbAdapter helper;
    ScoreStorageAdapter sHelper;
    SQLiteDatabase db1,db;

    // List view
    private ListView lv_academy,lv_city,lv_student;

    // Listview Adapter
    ArrayAdapter<String> adapter_city,adapter_academy,adapter_students;
    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);ActivityTracker.writeActitivtyLogs(this.getLocalClassName());
        setContentView(R.layout.activity_mentor);

        et_city=findViewById(R.id.location_select);
        et_academy=findViewById(R.id.academy_select);
        et_student_name=findViewById(R.id.select_student);

        city=et_city.getText().toString();
        stud_name=et_student_name.getText().toString();
        academy=et_academy.getText().toString();

        click=findViewById(R.id.btn_click);
        helper = new MyDbAdapter(this.getApplicationContext());
        db = helper.myhelper.getReadableDatabase();
        sHelper = new ScoreStorageAdapter(this.getApplicationContext());
        db1 = sHelper.scoreStorageHelper.getReadableDatabase();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        coach_name=bundle.getString("username");
        date=bundle.getString("date");


        cursor = helper.myhelper.getUID(stud_name, db);

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.isAfterLast() == false) {
                stud_id = cursor.getString(0);
            }
            cursor.moveToNext();
        }
        cursor.close();

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=new Bundle();
                b.putString("coach_id",coach_id);
                b.putString("date",date);
                b.putString("student_id",stud_id);
                Intent i1=new Intent(Mentor.this,Score_From.class).putExtras(b);
                startActivity(i1);

            }
        });
// Listview Data
        String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE",
                "iPhone 4S", "Samsung Galaxy Note 800",
                "Samsung Galaxy S3", "MacBook Air", "Mac Mini", "MacBook Pro"};


        String cities[]={"Bangalore","Chennai","Hyderabad","Trivandrum","VishakPatnam","Mumbai"};
        String academy[]={"xyz","pqr","lmn","stu","abc","def","hij"};
        String students[]={"a","b","c","d","e","f","g"};

        lv_academy = (ListView) findViewById(R.id.list_view2);
        lv_city = (ListView) findViewById(R.id.list_view1);
        lv_student = (ListView) findViewById(R.id.list_view3);
        et_city = (EditText) findViewById(R.id.location_select);
        et_academy = (EditText) findViewById(R.id.academy_select);
        et_student_name = (EditText) findViewById(R.id.select_student);
        // Adding items to listview
        adapter_city = new ArrayAdapter<String>(this,R.layout.item_list, R.id.list_name, cities);
        adapter_academy = new ArrayAdapter<String>(this,R.layout.item_list, R.id.list_name, academy);
        adapter_students=new ArrayAdapter<String>(this,R.layout.item_list, R.id.list_name, students);
        lv_city.setAdapter(adapter_city);
        lv_academy.setAdapter(adapter_academy);
        lv_student.setAdapter(adapter_students);

        /**
         * Enabling Search Filter
         * */
        et_city.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Mentor.this.adapter_city.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        /**
         * Enabling Search Filter
         * */
        et_academy.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Mentor.this.adapter_academy.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        /**
         * Enabling Search Filter
         * */
        et_student_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Mentor.this.adapter_students.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }
}
