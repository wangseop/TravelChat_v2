package com.hoingmarry.travelchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView tv_output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Enter...", "MainActivity OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        startActivity(intent);
        finish();

        // 위젯에 대한 참조.
        tv_output = findViewById(R.id.tv_output);


    }

}
