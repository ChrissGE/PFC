package com.example.opinapp.ui.reviews;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.opinapp.R;
import com.example.opinapp.databinding.FragmentReviewsBinding;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewsFragment extends Fragment {

    private ArrayList<ReviewModel> reviews;
    private ReviewsAdapter adapter;
    private FragmentReviewsBinding binding;
    private SearchView searchView;
    private ArrayList<ReviewModel> filteredReviews;

    private SharedPreferences loginSharedPreferences;
    private ReviewsViewModel reviewsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReviewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginSharedPreferences = getContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        reviewsViewModel = new ViewModelProvider(requireActivity()).get(ReviewsViewModel.class);

        reviews = reviewsViewModel.getReviews();
        filteredReviews= new ArrayList<>();

        RecyclerView recyclerView = getView().findViewById(R.id.RecyclerViewReviews);
        adapter = new ReviewsAdapter(requireContext(), reviews);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        searchView=view.findViewById(R.id.searchViewReviews);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterReview(newText);
                return false;
            }
        });
    }

    private void filterReview(String query) {
        filteredReviews.clear();

        if (query.isEmpty()) {
            filteredReviews.addAll(reviews); // Mostrar todas las reseñas si la búsqueda está vacía
        } else {
            //TODO: Mirar por que el filtro no aplica bien en la busqueda por numero
            for (ReviewModel review : reviews) {
                String businessName = review.getBusinessName();
                if (businessName != null && businessName.toLowerCase().startsWith(query.trim().toLowerCase())) {
                    filteredReviews.add(review);
                }
            }
        }
        adapter.filterList(filteredReviews); // Aplicar filtro al adaptador
    }


    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
