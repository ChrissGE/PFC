package com.example.opinapp.ui.shop;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opinapp.PopUpQuestionary;
import com.example.opinapp.R;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ProductModel> products;
    private ArrayList<ProductModel> filteredProducts; // Lista filtrada

    public ProductsAdapter(Context context, ArrayList<ProductModel> products) {
        this.context = context;
        this.products = products;
        this.filteredProducts = new ArrayList<>(products); // Inicializa la lista filtrada con todos los productos
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_model, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductModel product = filteredProducts.get(position);
        holder.descriptionProduct.setText(product.getDescripcion());
        holder.priceProduct.setText(product.getPrecio()+" puntos");
        Bitmap imageReward=product.getImage();
        if (imageReward != null) {
            holder.imageProduct.setImageBitmap(imageReward);
        }
        else{
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupCompra(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredProducts.size();
    }

    private void mostrarPopupCompra(ProductModel product) {
        // Aquí puedes mostrar el popup de compra
        // Por ejemplo, puedes abrir un fragmento de compra pasándole los datos del producto
        PopUpProduct productPopupFragment = PopUpProduct.newInstance(product.getId_reward(),product.getDescripcion(),product.getPrecio()+" puntos");
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        productPopupFragment.show(fragmentManager, "compra_popup_fragment");
    }

    public void filterList(ArrayList<ProductModel> filteredList) {
        filteredProducts = filteredList;
        notifyDataSetChanged(); // Notificar al RecyclerView que la lista ha cambiado
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView descriptionProduct, priceProduct;
        private ImageView imageProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionProduct = itemView.findViewById(R.id.description_text);
            priceProduct = itemView.findViewById(R.id.price_text);
            imageProduct = itemView.findViewById(R.id.product_image);
        }
    }
}
