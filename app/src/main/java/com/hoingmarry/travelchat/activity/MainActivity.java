package com.hoingmarry.travelchat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.hoingmarry.travelchat.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tv_output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d("Enter...", "MainActivity OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FragmentManager fm = getSupportFragmentManager();
//        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
//        if (mapFragment == null) {
//            mapFragment = MapFragment.newInstance();
//            fm.beginTransaction().add(R.id.map, mapFragment).commit();
//        }
//        mapFragment.getMapAsync(this);

        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        startActivity(intent);
        finish();


    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(
                new LatLng(37.563996959956604, 127.11477513394202));
        naverMap.moveCamera(cameraUpdate);
        locationOverlay.setPosition(new LatLng(37.563996959956604, 127.11477513394202));
    }
}
