package com.hoingmarry.travelchat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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
    private Toolbar mapActionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        Intent intent = getIntent();

        lat = intent.getDoubleExtra("latitude", 37.0);
        lng = intent.getDoubleExtra("longitude", 127.0);

        // 액션바(툴바를 액션바로 세팅)
        mapActionbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mapActionbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Trigo");

        // 액션바 뒤로가기 버튼 추가
        actionBar.setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map_search);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_search, mapFragment).commit();
        }


        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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
