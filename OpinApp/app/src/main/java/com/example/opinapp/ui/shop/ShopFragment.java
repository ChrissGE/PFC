package com.example.opinapp.ui.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opinapp.R;
import com.example.opinapp.databinding.FragmentShopBinding;
import com.example.opinapp.ui.reviews.ReviewModel;
import com.example.opinapp.ui.reviews.ReviewsViewModel;

import java.util.ArrayList;

public class ShopFragment extends Fragment {

    private ArrayList<ProductModel> products;
    private ArrayList<ProductModel> filteredProducts;
    private ProductsAdapter adapter;
    private FragmentShopBinding binding;
    private SearchView searchView;
    private  ShopViewModel shopViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);

        products = shopViewModel.getProductos();
        filteredProducts= new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.RecyclerViewShop);
        adapter = new ProductsAdapter(requireContext(), products);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),2));

        searchView=view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return false;
            }
        });
    }

    private void filterProducts(String query) {
        filteredProducts.clear();

        if (query.isEmpty()) {
            filteredProducts.addAll(products); // Si el texto de búsqueda está vacío, mostrar todos los productos
        } else {
            for (ProductModel product : products) {
                if (product.getDescripcion().toLowerCase().contains(query.toLowerCase())) {
                    filteredProducts.add(product);
                }
            }
        }
        adapter.filterList(filteredProducts); // Notificar al adaptador que los datos han cambiado
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
