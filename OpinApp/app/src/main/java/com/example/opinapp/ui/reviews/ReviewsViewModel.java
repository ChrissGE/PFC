package com.example.opinapp.ui.reviews;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.opinapp.ui.reviews.ReviewModel;

import java.util.ArrayList;
import java.util.List;

public class ReviewsViewModel extends ViewModel {
    private List<ReviewModel> reviews;

    public ReviewsViewModel() {
        reviews = new ArrayList<>();
    }

    public ArrayList<ReviewModel> getReviews() {
        return (ArrayList<ReviewModel>) reviews;
    }

    public void updateReviews(List<ReviewModel> reviews) {
        this.reviews=reviews;
        Log.e("reviews", reviews.toString());
    }

    public void anhadirReview(ReviewModel review) {
        reviews.add(0,review);
    }
}
