package com.hoingmarry.travelchat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.hoingmarry.travelchat.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;

public class MapSearchActivity extends AppCompatActivity implements OnMapReadyCallback {


    private double lat;
    private double lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        Intent intent = getIntent();

        lat = intent.getDoubleExtra("latitude", 37.0);
        lng = intent.getDoubleExtra("longitude", 127.0);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map_search);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_search, mapFragment).commit();
        }


        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(
                new LatLng(lat, lng));
        naverMap.moveCamera(cameraUpdate);
        locationOverlay.setPosition(new LatLng(lat, lng));
    }
}
