package com.example.opinapp.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.opinapp.ApiResponseCallback;
import com.example.opinapp.ApiService;
import com.example.opinapp.Forgot_passwordV2;
import com.example.opinapp.PopUpQuestionary;
import com.example.opinapp.R;
import com.example.opinapp.databinding.FragmentHomeBinding;
import com.example.opinapp.ui.reviews.ReviewModel;
import com.example.opinapp.ui.reviews.ReviewsAdapter;
import com.example.opinapp.ui.reviews.ReviewsViewModel;
import com.example.opinapp.ui.shop.ProductModel;
import com.example.opinapp.ui.shop.ShopViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView textPoints;
    private ImageButton roundButton;
    private ImageButton qr_button;
    private ReviewsViewModel reviewsViewModel;
    private ShopViewModel shopViewModel;
    private HomeViewModel homeViewModel;
    private SharedPreferences loginSharedPreferences;
    private MyLocationNewOverlay myLocationOverlay;
    private List<ReviewModel> reviewsList;
    private List<ReviewModel> topReviewsList;
    private List<ProductModel> productosList;
    private ApiService apiService;
    private ArrayList<CompanyModel> filteredcompanies;
    private List<CompanyModel> companhias;
    private MapView mapView;
    private RecyclerView recyclerView;
    // Declara una variable para el adaptador del RecyclerView
    private CompaniesAdapter adapter;
    private ReviewsAdapter adapterReviews;
    private RecyclerView recyclerViewReviews;
    private View root;


    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topReviewsList = new ArrayList<>();
        companhias = new ArrayList<>();
        reviewsViewModel = new ViewModelProvider(requireActivity()).get(ReviewsViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        loginSharedPreferences = getContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://94.143.138.27:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        filteredcompanies = new ArrayList<>();
        HacerLlamadaApiProductos();
        HacerLlamadaApiCompanhias();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        roundButton = root.findViewById(R.id.roundButton);
        qr_button = root.findViewById(R.id.qr_scanner);
        String url = loginSharedPreferences.getString("profile_pic", "def");
        gestionarMapa();
        textPoints=root.findViewById(R.id.textViewPoints);
        recyclerView = root.findViewById(R.id.recyclerViewCompanies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewReviews = root.findViewById(R.id.recyclerViewRecentReviews);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CompaniesAdapter(requireContext(), (ArrayList<CompanyModel>) companhias,mapView);
        recyclerView.setAdapter(adapter);
        Log.v("url", url);
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(roundButton);
        SearchView searchView = root.findViewById(R.id.searchView);

        qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("QR", "LE HE DAOOOOO");
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PopUpQuestionary popUpQuestionary = new PopUpQuestionary();
                fragmentTransaction.replace(R.id.fragment_container, popUpQuestionary);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Aquí manejas la acción cuando se envía la consulta de búsqueda
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    filterCompanies(newText);
                }

                return false;
            }
        });

        // Configurar OnClickListener para roundButton
        roundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        int points= loginSharedPreferences.getInt("points", 0);
        String s_points=Integer.toString(points);
        Log.d("Puntos", s_points);
        if (companhias.isEmpty()){
            companhias = homeViewModel.getCompanies();
        }
        addCompanyMarkers();
        textPoints.setText(s_points);
        ActualizarTopReviews();
        UserLocation();

    }

    private void ActualizarTopReviews() {
        topReviewsList = reviewsViewModel.getReviews();
        if (topReviewsList.size() > 3) {
            topReviewsList = topReviewsList.subList(0, 3);
        }
        ArrayList<ReviewModel> arrayListTopReviews = new ArrayList<>(topReviewsList);
        adapterReviews = new ReviewsAdapter(requireContext(), arrayListTopReviews);
        recyclerViewReviews.setAdapter(adapterReviews);
    }

    private void HacerLlamadaApiCompanhias() {
        LlamadaApiGetCompanhias(new ApiResponseCallback() {
            @Override
            public void onResponse(JsonObject jsonObject) {

            }

            @Override
            public void onResponse(JsonArray jsonArray) {
                Gson gson = new Gson();
                Log.e("entro", "entrooo");
                Type listType = new TypeToken<List<CompanyModel>>() {
                }.getType();
                companhias = gson.fromJson(jsonArray, listType);
                homeViewModel.setCompanies(companhias);
                adapter = new CompaniesAdapter(requireContext(), (ArrayList<CompanyModel>) companhias,mapView);
                recyclerView.setAdapter(adapter);
                addCompanyMarkers();
                try {
                    CargarImagenes();
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                HacerLlamadaApiReviews();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("LlamadaApi", errorMessage);
            }

            @Override
            public void onResponse(Bitmap imagen) {

            }
        });
    }
    private void CargarImagenes() throws InterruptedException {
        for(CompanyModel company : companhias){
            Thread.sleep(100);
            HacerLlamadaApiImagen(company,null);
        }
    }
    private void LlamadaApiGetCompanhias(ApiResponseCallback callback) {


        Call<JsonArray> call = apiService.getCompanies();
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

    private void HacerLlamadaApiProductos() {
        LlamadaApiGetProductos(new ApiResponseCallback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
            }

            @Override
            public void onResponse(JsonArray jsonArray) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<ProductModel>>() {
                }.getType();
                try{

                    productosList = gson.fromJson(jsonArray, listType);
                    anhadirImagenesProductos();
                }catch (Exception e){
                    Log.e("productos", e.getMessage() );
                }
                shopViewModel.updateProducts(productosList);


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
    private void anhadirImagenesProductos() throws InterruptedException {
        for(ProductModel producto : productosList){
            Thread.sleep(100);
            HacerLlamadaApiImagen(null,producto);
        }
    }
    private void LlamadaApiGetProductos(ApiResponseCallback callback) {

        JsonObject userData = new JsonObject();
        userData.addProperty("name_language", Locale.getDefault().getDisplayLanguage());
        Call<JsonArray> call = apiService.getProducts(userData);
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
    private void anhadirImagenesAReviews() throws InterruptedException {
        if (reviewsList.isEmpty()){
            reviewsList = reviewsViewModel.getReviews();
        }
        for(ReviewModel review :reviewsList){
            Thread.sleep(100);
            review.SetImage(homeViewModel.getRegistro(review.getBusinessName()));
        }
    }
    private void HacerLlamadaApiReviews() {
        LlamadaApiGetReviews(new ApiResponseCallback() {
            @Override
            public void onResponse(JsonObject jsonObject) {

            }

            @Override
            public void onResponse(JsonArray jsonArray) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<ReviewModel>>() {
                }.getType();
                reviewsList = gson.fromJson(jsonArray, listType);
                Log.e("reviews", String.valueOf(reviewsList.size()));
                try {
                    anhadirImagenesAReviews();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                reviewsViewModel.updateReviews(reviewsList);
                ActualizarTopReviews();
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

    private void LlamadaApiGetReviews(ApiResponseCallback callback) {
        JsonObject userData = new JsonObject();
        String email = loginSharedPreferences.getString("email", "");
        userData.addProperty("email", email);

        Call<JsonArray> call = apiService.getReviews(userData);
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
    private void HacerLlamadaApiImagen(CompanyModel company,ProductModel producto) {

        JsonObject userData = new JsonObject();
        boolean esCompanhia=false;
        if (company==null){
            userData.addProperty("id_image", producto.getId_reward());
            userData.addProperty("tipo", "producto");
        } else if (producto==null) {
            esCompanhia=true;
            userData.addProperty("id_image", company.getCompanyCode());
            userData.addProperty("tipo", "company");
        }
        boolean finalEsCompanhia = esCompanhia;
        LlamadaApiGetImagen(new ApiResponseCallback() {
            @Override
            public void onResponse(Bitmap imagen) {
                if(finalEsCompanhia){
                    Log.e("imagen companhia", imagen.toString());
                    homeViewModel.agregarRegistro(company.getBusinessName(),imagen);
                }else{
                    Log.e("imagen producto ", imagen.toString());
                    producto.setImage(imagen);
                }
            }

            @Override
            public void onResponse(JsonObject jsonObject) {

            }

            @Override
            public void onResponse(JsonArray jsonArray) {

            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("LlamadaApi", errorMessage);
            }
        },userData);
    }

    private void LlamadaApiGetImagen(ApiResponseCallback callback,JsonObject userData) {

        Call<ResponseBody> call = apiService.getImagen(userData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Convertir el cuerpo de la respuesta en un Bitmap
                    Bitmap imagen = BitmapFactory.decodeStream(response.body().byteStream());
                    callback.onResponse(imagen);
                } else {
                    callback.onFailure("Request failed with error: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure("Error executing the network request: " + t.getMessage());
            }
        });
    }
    private void gestionarMapa() {
        Configuration.getInstance().load(getContext(), loginSharedPreferences);

        // Obtener referencia al MapView
        mapView = root.findViewById(R.id.mapView);

        // Configurar el proveedor de azulejos (tiles)
        //mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        // Zoom y centro del mapa
        mapView.getController().setZoom(18.0);
        mapView.getController().setCenter(new GeoPoint(40.469032511805054,-3.783262713665248));
        mapView.setBuiltInZoomControls(false);

        mapView.setMultiTouchControls(true);

        UserLocation();
    }

    private void UserLocation() {
        myLocationOverlay = new MyLocationNewOverlay(mapView);
        myLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(myLocationOverlay);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Log.d("gestionarMapa", "gestionarMapa: Enable");
            // Start showing the user's location
            myLocationOverlay.enableMyLocation();
            GeoPoint userLocation = myLocationOverlay.getMyLocation();
            if (userLocation != null) {
                Log.d("gestionarMapa", userLocation.getLatitude()+"/"+userLocation.getLongitude());
                // Centrar el mapa en la ubicación del usuario
                mapView.getController().setCenter(userLocation);
            }
        }
    }

    private void addCompanyMarkers() {
        // Recorre todas las empresas y agrega un marcador para cada una
        for (CompanyModel company : companhias) {
            GeoPoint companyLocation = new GeoPoint(company.getLatitude(), company.getLongitude());
            Marker marker = new Marker(mapView);
            marker.setPosition(companyLocation);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle(company.getBusinessName());
            marker.setSnippet(company.getAddress());
            // Puedes personalizar el icono del marcador si lo deseas
            marker.setIcon(getResources().getDrawable(R.drawable.custom_marker_icon));
            marker.setInfoWindow(new CustomInfoWindow(R.layout.custom_info_window,mapView));

            mapView.getOverlays().add(marker);
        }
        mapView.invalidate(); // Actualiza el mapa para mostrar los nuevos marcadores
    }


    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.desplegable, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean itemSelected=false;
                if (item.getItemId() == R.id.cerrar_sesion) {
                    Toast.makeText(getActivity(), "Log out", Toast.LENGTH_SHORT).show();
                    itemSelected = true;
                    SharedPreferences.Editor editor = loginSharedPreferences.edit();
                    editor.remove("isLoggedIn"); // eliminar la clave
                    editor.apply();
                    requireActivity().finishAffinity();
                }
                return itemSelected;
            }
        });

        popupMenu.show();
    }

    private void filterCompanies(String query) {
        adapter.filter(query);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}
