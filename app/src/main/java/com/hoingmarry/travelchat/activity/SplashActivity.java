package com.hoingmarry.travelchat.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hoingmarry.travelchat.R;

public class SplashActivity extends AppCompatActivity {

    private ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // 액션바 비활성화
        actionbar = getSupportActionBar();
        actionbar.hide();

        Handler hd = new Handler();
        hd.postDelayed(new SplashHandler(), 1000);
    }

    private class SplashHandler implements Runnable{
        @Override
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class));
            SplashActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
