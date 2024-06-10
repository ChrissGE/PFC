package com.example.opinapp.ui.shop;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.opinapp.ApiService;
import com.example.opinapp.Questionary;
import com.example.opinapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PopUpProduct extends DialogFragment {

    private String nombreProducto,precioProducto;
    private int id_reward;
    private SharedPreferences sharedPreferences;
    private ApiService apiService;

    public static PopUpProduct newInstance(int id_reward,String nombreProducto, String precioProducto) {
        PopUpProduct fragment = new PopUpProduct();
        Bundle args = new Bundle();
        args.putString("nombre_producto", nombreProducto);
        args.putString("precio_producto", precioProducto);
        args.putInt("id_reward",id_reward);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nombreProducto = getArguments().getString("nombre_producto");
            precioProducto = getArguments().getString("precio_producto");
            id_reward = getArguments().getInt("id_reward");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pop_up_product, container, false);

        TextView nombreProductoTextView = rootView.findViewById(R.id.product_name);
        TextView precioProductoTextView = rootView.findViewById(R.id.product_price);
        Button sumbitButton=rootView.findViewById(R.id.btn_sumbit);
        Button dismissButton=rootView.findViewById(R.id.btn_dismiss);
        sharedPreferences = getContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://94.143.138.27:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarCompra();
            }
        });

        sumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarCompra();
            }
        });

        nombreProductoTextView.setText(nombreProducto);
        precioProductoTextView.setText(precioProducto);

        return rootView;
    }

    public void confirmarCompra() {
        // Lógica para confirmar la compra
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("email", sharedPreferences.getString("email","def"));
            jsonObject.addProperty("rewards_id",id_reward);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<JsonObject> call = apiService.setTicket(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    int points= sharedPreferences.getInt("points",0);
                    String limpiarPrecio=precioProducto.replace("puntos", "").trim();
                    int price=Integer.parseInt(limpiarPrecio);
                    sharedPreferences.edit().putInt("points",points-price).apply();
                }
                else {
                    //NO FUNCIONA EL TOAST
                    //Toast.makeText(getActivity(), "No tienes suficientes puntos o la recompensa está agotada", Toast.LENGTH_SHORT).show();
                    Log.e("LlamadaApi", "Request failed with error: " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("LlamadaApi", "Error executing the network request: " + t.getMessage());
            }
        });
        getParentFragmentManager().beginTransaction().remove(PopUpProduct.this).commit();
    }

    public void cancelarCompra() {
        getParentFragmentManager().beginTransaction().remove(PopUpProduct.this).commit();
    }
}