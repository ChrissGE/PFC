package com.example.opinapp;

import android.graphics.Bitmap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface ApiResponseCallback {
    void onResponse(JsonObject jsonObject);
    void onResponse(JsonArray jsonArray);
    void onFailure(String errorMessage);

    void onResponse(Bitmap imagen);
}
