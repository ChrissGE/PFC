package com.example.opinapp.ui.home;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.opinapp.R;
import com.example.opinapp.ui.reviews.ReviewModel;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class HomeViewModel extends ViewModel {


    private String userProfileImageUrl;
    private List<ReviewModel> topReviews;

    public List<CompanyModel> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyModel> companies) {
        this.companies = companies;
    }
    private Dictionary<String, Bitmap> imagenes;
    private List<CompanyModel> companies ;
    public void agregarRegistro(String companyName, Bitmap imagen) {
        imagenes.put(companyName, imagen);
        Log.e("tamanho en set"+companyName, String.valueOf(imagenes.size()));
    }
    public Bitmap getRegistro(String companyName){
        Log.e("tamanho en get"+companyName, String.valueOf(imagenes.size()));
        return imagenes.get(companyName);
    }
    public HomeViewModel() {
        imagenes = new Hashtable<>();
        this.topReviews = new ArrayList<>();
        this.companies = new ArrayList<>();
    }
//    public void setTopReviews(List<ReviewModel> reviews){
//        this.topReviews = reviews;
//        Log.e("topReviews",reviews.toString());
//    }
//    public List<ReviewModel> getTopReviews(){
//        Log.e("getTopReviews",topReviews.toString());
//        return this.topReviews;
//    }
    // Método para establecer la URL de la imagen de perfil
    public void setUserProfileImageUrl(String imageUrl) {
        this.userProfileImageUrl = imageUrl;
    }

    // Método para obtener la URL de la imagen de perfil
    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

}