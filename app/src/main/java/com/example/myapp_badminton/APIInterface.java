package com.example.myapp_badminton;

import com.example.myapp_badminton.pojo.WebPortalVerificationPOJO;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {
    String BASE_URL = "http://74.91.20.50:9000/api/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(APIInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @FormUrlEncoded
    @POST("webportal.php")
//    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    Call<WebPortalVerificationPOJO> webportalVerification(
            @Field("url") String url);

}
