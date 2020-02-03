package com.gb.rating.ui.search;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gb.rating.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

public class SearchFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPoiClickListener {

    private View view;

    private final String TAG = "myLog";
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    private void init() {
        mMap.setOnMapClickListener(latLng -> Log.d(TAG, "onMapClick: " + latLng.latitude + ", " + latLng.longitude));

        mMap.setOnMapLongClickListener(latLng -> Log.d(TAG, "onMapLongClick: " + latLng.latitude + ", " + latLng.longitude));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng moscow = new LatLng(55.6902907, 37.6850383);
        mMap.addMarker(new MarkerOptions().position(moscow).title("Marker in Moscow"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(moscow));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnPoiClickListener(this);
        init();
    }

    @Override
    public void onPoiClick(PointOfInterest poi) {
        Toast.makeText(getContext(), "Clicked: " +
                        poi.name + "\nPlace ID:" + poi.placeId +
                        "\nLatitude:" + poi.latLng.latitude +
                        " Longitude:" + poi.latLng.longitude,
                Toast.LENGTH_SHORT).show();
    }


}
