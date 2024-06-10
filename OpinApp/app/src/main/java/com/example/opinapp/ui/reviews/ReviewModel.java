package com.example.opinapp.ui.reviews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.example.opinapp.ui.home.HomeViewModel;

public class ReviewModel {
    private String  company_name;
    private byte[] image_company_bytes;
    private String insert_date,points_reward;
    private Bitmap image_company;
    private HomeViewModel homeViewModel;
    //TODO:ADAPTAR TODO A SQL
    public ReviewModel( String insert_date, String company_name,String points_reward) {
        this.company_name = company_name;
        this.insert_date = insert_date;
        this.points_reward = points_reward;
    }


    public void SetImage(Bitmap image_company){
        this.image_company =image_company;
    }
    public Bitmap getImage() {
        return image_company;
    }

    public String getBusinessName() {
        return company_name;}

    public String getDate() {return insert_date;}

    public String getPuntos() {
        return points_reward;
    }
}
