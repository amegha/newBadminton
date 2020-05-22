package com.example.myapp_badminton;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Reset_pass extends AppCompatActivity {

    Button reset;
    EditText uname;
    MyDbAdapter helper;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        uname=findViewById(R.id.pass_username);
        reset=findViewById(R.id.btn_reset);
        reset.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        password_update(uname.getText().toString());
        Intent intent = new Intent( Reset_pass.this,Login.class);
        startActivity(intent);
    }
});
    }





    private void password_update(String UserName)
    {
        Cursor cursor_name;
        helper = new MyDbAdapter(getApplicationContext());
        sqLiteDatabase = helper.myhelper.getReadableDatabase();
        cursor_name = helper.myhelper.search_name(UserName, sqLiteDatabase);

        if ((cursor_name != null)) {
            cursor_name.moveToFirst();

            /*the below if statement use to check or match username and phoneNumber entered by user with respective username and phoneNumber in database*/

            if (cursor_name.isAfterLast() == false ) {

                String a = cursor_name.getString(0);
                if((UserName.equals(a)))
                {
                    helper.updatePass(UserName);

                }
                cursor_name.moveToNext();
            }
            cursor_name.close();

        }
    }

}
