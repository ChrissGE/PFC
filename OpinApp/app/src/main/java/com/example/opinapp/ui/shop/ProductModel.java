package com.example.opinapp.ui.shop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public class ProductModel {
    private String text,rewards_price;

    public Bitmap getImage() {
        return image_reward;
    }

    public void setImage(Bitmap image_reward) {
        this.image_reward = image_reward;
    }

    private Bitmap image_reward;
    private int id_reward;
    public ProductModel(int id_reward,int rewards_price,String text){
        this.text=text;
        this.rewards_price= rewards_price +" puntos";
        this.id_reward=id_reward;
    }

    public String getDescripcion() {
        return text;
    }

    public String getPrecio() {
        return rewards_price;
    }



    public int getId_reward(){return id_reward;}
}
