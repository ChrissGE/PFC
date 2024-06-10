package com.example.opinapp;

import android.graphics.Bitmap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/setUser")
    Call<Void> setUser(@Body JsonObject json);

    @POST("/getUser")
    Call<JsonObject> getUser(@Body JsonObject json);

     @POST("/getReviews")
     Call<JsonArray> getReviews(@Body JsonObject json);
     @POST("/getTopReviews")
     Call<JsonArray> getTopReviews(@Body JsonObject json);
     @POST("/setReview")
     Call<JsonObject> setReview(@Body JsonObject json);

     @POST("/getQuestionary")
     Call<JsonArray> getQuestionary(@Body JsonObject json);
     @POST("/getProducts")
     Call<JsonArray> getProducts(@Body JsonObject json);
     @POST("/getCompanies")
     Call<JsonArray> getCompanies();
     @POST("/getImage")
     Call<ResponseBody> getImagen(@Body JsonObject json);

     @POST("/setTicket")
     Call<JsonObject> setTicket(@Body JsonObject json);
}
