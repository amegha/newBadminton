package com.example.myapp_badminton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp_badminton.pojo.WebPortalVerificationPOJO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.myapp_badminton.Login.PREFS_NAME;

public class WebPortal extends AppCompatActivity {
    static String url;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_portal);
        checkUserLogin();
        initView();

    }

    private void checkUserLogin() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").equals("logged")) {
            Intent intent = new Intent(WebPortal.this, HomePage.class);
            startActivity(intent);
            finish();
        } else if (!settings.getString("baseURL", "").equals("")) {
            startActivity(new Intent(this, Login.class));
            finish();
        }
    }

    private void initView() {
        editText = findViewById(R.id.portal_link);
    }

    public void VerifyWebPortal(View view) {
        url = editText.getText().toString();
        if (url.equals("") || url == null) {
            editText.setError("Empty");
        } else
            serverVerify(url);
    }

    private void serverVerify(String url) {
        APIInterface apiInterface = APIInterface.retrofit.create(APIInterface.class);
        Call<WebPortalVerificationPOJO> call = apiInterface.webportalVerification(url);
        call.enqueue(new Callback<WebPortalVerificationPOJO>() {
            @Override
            public void onResponse(Call<WebPortalVerificationPOJO> call, Response<WebPortalVerificationPOJO> response) {
                if (response.body() != null) {
                    switch (response.body().getStatus()) {
                        case "0":
                            Toast.makeText(WebPortal.this, "Verified", Toast.LENGTH_SHORT).show();
                            saveURL(url);
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                            break;
                        case "01":
                            Toast.makeText(WebPortal.this, "Verification failed", Toast.LENGTH_SHORT).show();
                            break;
                        case "02":
                            Toast.makeText(WebPortal.this, "DB execution error", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<WebPortalVerificationPOJO> call, Throwable t) {
//                startActivity(new Intent(getApplicationContext(), Login.class));
//                saveURL("");
                Toast.makeText(WebPortal.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveURL(String url) {
        if(!url.substring(0,8).equals("https://")) {
            url = url.substring(0, 7).equals("http://") ? url.substring(7) :  url;
            url="https://" +url;
        }
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("baseURL", url);
        editor.apply();
    }
}