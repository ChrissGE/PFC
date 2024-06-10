package com.example.opinapp.ui.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opinapp.R;
import com.example.opinapp.ui.home.CompanyModel;
import com.example.opinapp.ui.shop.ProductModel;
import com.example.opinapp.ui.shop.ProductsAdapter;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.function.LongFunction;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<CompanyModel> companies;
    private ArrayList<CompanyModel> originalCompanies;
    private MapView mapView;

    public CompaniesAdapter(Context context, ArrayList<CompanyModel> companies,MapView mapView) {
        this.context = context;
        this.companies = companies;
        this.originalCompanies = new ArrayList<>(companies); // Mantener una copia de la lista original
        this.mapView=mapView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.company_model, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompaniesAdapter.MyViewHolder holder, int position) {
        CompanyModel company = companies.get(position);
        holder.textCompanyName.setText(company.getBusinessName());
        Log.d("color score", Integer.toString(company.getColorScore()));

        // Obtén el SVG desde los recursos
        Drawable svgDrawableSmile = AppCompatResources.getDrawable(context,R.drawable.face_smile);
        Drawable svgDrawableMeh = AppCompatResources.getDrawable(context,R.drawable.face_meh);
        Drawable svgDrawableFrown = AppCompatResources.getDrawable(context,R.drawable.face_frown);

        Drawable drawable=null;
        if(company.getMark()>60){
            drawable = DrawableCompat.wrap(svgDrawableSmile).mutate();
        }
        else if(company.getMark()>27){
            drawable = DrawableCompat.wrap(svgDrawableMeh).mutate();
        }
        else{
            drawable = DrawableCompat.wrap(svgDrawableFrown).mutate();
        }
        // Cambia el color del SVG dinámicamente
        int color = company.getColorScore();
        DrawableCompat.setTint(drawable, color);

        // Establece el SVG como fondo de tu View
        holder.viewColorScore.setBackground(drawable);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CompaniesAdapter", "Company clicked!");
                moveMapToLocation(company.getLatitude(), company.getLongitude());
            }
        });
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textCompanyName;
        private View viewColorScore;
        private Drawable backgroundDrawable;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textCompanyName = itemView.findViewById(R.id.companyName);
            viewColorScore=itemView.findViewById(R.id.circle);
            backgroundDrawable = itemView.getBackground();
        }
    }

    public void filter(String query) {
        companies.clear();
        if (query.isEmpty()) {
            companies.addAll(originalCompanies); // Si la consulta está vacía, mostrar todas las compañías
        } else {
            query = query.toLowerCase();
            for (CompanyModel company : originalCompanies) {
                if (company.getBusinessName().toLowerCase().contains(query)) {
                    companies.add(company); // Agregar compañías que coincidan con la consulta
                }
            }
        }
        notifyDataSetChanged(); // Notificar al RecyclerView que los datos han cambiado
    }

    private void moveMapToLocation(double latitude, double longitude) {
        GeoPoint companyLocation = new GeoPoint(latitude, longitude);
        mapView.getController().animateTo(companyLocation);
    }
}
