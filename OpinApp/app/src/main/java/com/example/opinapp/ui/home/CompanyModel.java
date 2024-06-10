package com.example.opinapp.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;

import com.example.opinapp.ApiResponseCallback;
import com.example.opinapp.ApiService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyModel {
    public String getCompanyCode() {
        return company_code;
    }

    private String  company_code;
    private String company_name;
    private String address,coords;
    private float mark;

    private Bitmap image_company;
    public CompanyModel( String company_code,String company_name,String address,String coords,float mark) {
        this.company_code = company_code;
        this.company_name = company_name;
        this.address = address;
        this.coords = coords;
        this.mark=mark;
    }


    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        double latitude=0.0;
        if (coords != null && !coords.isEmpty()) {
            String[] parts = coords.split("/");
            if (parts.length >= 2) {
                try {
                    latitude= Double.parseDouble(parts[0].trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return latitude;
    }

    public double getLongitude() {
        double longitude=0.0;
        if (coords != null && !coords.isEmpty()) {
            String[] parts = coords.split("/");
            if (parts.length >= 2) {
                try {
                    longitude=Double.parseDouble(parts[1].trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return longitude;
    }
    public String getBusinessName() {return company_name;}

    public int getColorScore(){return cambiarColor();}
    public float getMark(){return mark;}


    public int cambiarColor(){
        // Definir el número

        // Obtener los colores del gradiente definido en XML
        int startColor = Color.parseColor("#FF0000"); // Rojo
        int centerColor = Color.parseColor("#FFFF00"); // Amarillo
        int endColor = Color.parseColor("#00FF00"); // Verde
        int color=0;
        if(mark!=-1){
            // Calcular los colores intermedios
            float ratio = (float) mark / 100;
            color= interpolateColor(endColor, centerColor, startColor, ratio);
        }
        return color;
    }
    private int interpolateColor(int color1, int color2, int color3, float ratio) {
        float inverseRatio = 1f - ratio;

        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRatio);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRatio);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRatio);

        float r2 = (Color.red(color2) * ratio) + (Color.red(color3) * inverseRatio);
        float g2 = (Color.green(color2) * ratio) + (Color.green(color3) * inverseRatio);
        float b2 = (Color.blue(color2) * ratio) + (Color.blue(color3) * inverseRatio);

        float finalR = (r * ratio) + (r2 * inverseRatio);
        float finalG = (g * ratio) + (g2 * inverseRatio);
        float finalB = (b * ratio) + (b2 * inverseRatio);

        return Color.rgb((int) finalR, (int) finalG, (int) finalB);
    }
}
