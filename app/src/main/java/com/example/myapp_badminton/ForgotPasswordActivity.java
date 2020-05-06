package com.example.myapp_badminton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText newPassword,confirmPassword;
    String newpass,confirmpass;
    Button ok;
    MyDbAdapter helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        newPassword=findViewById(R.id.pass_new);
        confirmPassword=findViewById(R.id.pass_confirm);
        ok=findViewById(R.id.btn_resetpassword);
        helper = new MyDbAdapter(this);
        newpass=newPassword.getText().toString();
        confirmpass=confirmPassword.getText().toString();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPassword.getText().toString()!="" && confirmPassword.getText().toString()!="" ) {
                    if (newpass.equals(confirmpass) ) {
                        //long id=helper.insertForgotData(id,newpass);
                        Toast.makeText(getApplicationContext(), "Password Matched..", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ForgotPasswordActivity.this, Login.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Password Mis-Matched! Try again ..", Toast.LENGTH_LONG).show();
                        newPassword.setText("");
                        confirmPassword.setText("");
                    }
                }

            }
        });



    }
}
