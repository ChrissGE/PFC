package com.example.opinapp.ui.home;

import android.view.View;
import android.widget.TextView;

import com.example.opinapp.R;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class CustomInfoWindow extends InfoWindow {

    public CustomInfoWindow(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);
    }

    @Override
    public void onOpen(Object item) {
        if (item instanceof Marker) {
            Marker marker = (Marker) item;
            TextView titleTextView = mView.findViewById(R.id.bubble_title);
            TextView descriptionTextView = mView.findViewById(R.id.bubble_description);
            if (titleTextView != null) {
                titleTextView.setText(marker.getTitle());
            }
            if (descriptionTextView != null) {
                descriptionTextView.setText(marker.getSnippet());
            }
        }
    }

    @Override
    public void onClose() {
    }
}


