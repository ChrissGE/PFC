package com.example.opinapp.ui.shop;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.opinapp.ui.reviews.ReviewModel;

import java.util.ArrayList;
import java.util.List;

public class ShopViewModel extends ViewModel {


    private List<ProductModel> productos;

    public ShopViewModel() {
        productos = new ArrayList<>();
    }

    public ArrayList<ProductModel> getProductos() {
        return (ArrayList<ProductModel>) productos;
    }

    public void updateProducts(List<ProductModel> productos) {
        this.productos=productos;
    }
}