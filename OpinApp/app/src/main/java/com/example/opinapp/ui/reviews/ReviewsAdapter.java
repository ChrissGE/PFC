package com.example.opinapp.ui.reviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opinapp.R;
import com.example.opinapp.ui.shop.ProductModel;
import com.example.opinapp.ui.shop.ProductsAdapter;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ReviewModel> reviews;
    private ArrayList<ReviewModel> filteredReviews;

    public ReviewsAdapter(Context context, ArrayList<ReviewModel> reviews) {
        this.context = context;
        this.reviews = reviews;
        this.filteredReviews = new ArrayList<>(reviews);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_model, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.MyViewHolder holder, int position) {
        ReviewModel review = reviews.get(position);
        holder.textCompanyName.setText(review.getBusinessName());
        holder.textReviewDate.setText(review.getDate());
        holder.textViewPuntos.setText(review.getPuntos());
        Bitmap imageReward=review.getImage();
        if (imageReward != null) {
            holder.imageReview.setImageBitmap(imageReward);
        }
    }
    public void filterList(ArrayList<ReviewModel> filteredList) {
        filteredReviews = new ArrayList<>(filteredList);
        notifyDataSetChanged(); // Notificar al RecyclerView que los datos han cambiado
    }

    @Override
    public int getItemCount() {
        return filteredReviews.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textCompanyName, textReviewDate,textViewPuntos;
        private ImageView imageReview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textCompanyName = itemView.findViewById(R.id.textCompanyName);
            textReviewDate = itemView.findViewById(R.id.textReviewDate);
            imageReview = itemView.findViewById(R.id.imageCompany);
            textViewPuntos = itemView.findViewById(R.id.textViewPuntos);
        }
    }
}
