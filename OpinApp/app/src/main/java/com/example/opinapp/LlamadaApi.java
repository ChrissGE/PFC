package com.example.opinapp;

import retrofit2.Call;
import retrofit2.Response;
import android.util.Log;

import com.google.gson.JsonObject;

public class LlamadaApi<T> implements Runnable {
    private final Call<T> call;
    private JsonObject respuesta;
    public LlamadaApi(Call<T> call) {

        this.call = call;
    }

    @Override
    public void run() {
        try {
            Response<T> response = call.execute();

            if (response.isSuccessful()) {
                respuesta = (JsonObject) response.body();
                Log.d("LlamadaApi", "Request successful: " + respuesta);
            } else {
                Log.e("LlamadaApi", "Request failed with error: " + response.errorBody());
            }
        } catch (Exception e) {
            Log.e("LlamadaApi", "Error executing the network request", e);

        }
    }

    public JsonObject getRespuesta(){
        return respuesta;
    }
}
