package com.hoingmarry.travelchat.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.hoingmarry.travelchat.R;
public class MainActivity extends AppCompatActivity{

//    private ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d("Enter...", "MainActivity OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // 액션바 비활성화
//        actionbar = getSupportActionBar();
//        actionbar.hide();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

}
