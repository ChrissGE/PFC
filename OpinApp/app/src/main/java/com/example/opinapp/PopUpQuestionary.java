package com.example.opinapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PopUpQuestionary extends Fragment {

    private Button sumbitButton,scanButton;
    private EditText codeEditText;
    private ApiService apiService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_pop_up_questionary, container, false);
        sumbitButton=view.findViewById(R.id.button_popup);
        codeEditText=view.findViewById(R.id.edit_text_popup);
        scanButton=view.findViewById(R.id.button_scan);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://94.143.138.27:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        Log.d("Lenguage", Locale.getDefault().getDisplayLanguage());
        sumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(codeEditText.getText().toString()!=null){
                    HacerLlamadaApi();
                }
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(PopUpQuestionary.this);
                integrator.setPrompt("Escanea un código QR");
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                codeEditText.setText("prueba");
            }
            else{

                codeEditText.setText(result.getContents());
            }
        }
        else{
            codeEditText.setText("prueba2");
        }
    }

    private void HacerLlamadaApi() {
        LlamadaApiGetQuestionary(new ApiResponseCallback() {

            @Override
            public void onResponse(JsonObject jsonObject) {

            }

            @Override
            public void onResponse(JsonArray jsonArray) {
                Bundle bundle = new Bundle();
                bundle.putString("jsonArray", jsonArray.toString());
                bundle.putString("company_code",codeEditText.getText().toString());
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Questionary questionary = new Questionary();
                questionary.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, questionary);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            @Override
            public void onFailure(String errorMessage) {
                // Manejar el error aquí
                Log.e("LlamadaApi", errorMessage);
            }

            @Override
            public void onResponse(Bitmap imagen) {

            }
        });
        }
        private void LlamadaApiGetQuestionary(ApiResponseCallback callback) {
            JsonObject cuestionary_code = new JsonObject();
            cuestionary_code.addProperty("name_language", Locale.getDefault().getDisplayLanguage());
            cuestionary_code.addProperty("company_code", codeEditText.getText().toString());
            Call<JsonArray> call = apiService.getQuestionary(cuestionary_code);
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.isSuccessful()) {
                        JsonArray jsonArray = response.body();
                        callback.onResponse(jsonArray);
                    } else {
                        callback.onFailure("Request failed with error: " + response.errorBody());
                    }
                }
                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    callback.onFailure("Error executing the network request: " + t.getMessage());
                }
            });
        }
    }