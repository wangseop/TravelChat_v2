package com.hoingmarry.travelchat.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.hoingmarry.travelchat.R;
import com.hoingmarry.travelchat.data.user.LoginData;
import com.hoingmarry.travelchat.network.NetworkLoginTask;
import com.hoingmarry.travelchat.utils.SharedPreference;

public class MainActivity extends AppCompatActivity{

//    private ActionBar actionbar;

    private SharedPreference pref;
    private final static String loginUrl = "http://192.168.0.154:30001/login";

    protected void onCreate(Bundle savedInstanceState) {
//        Log.d("Enter...", "MainActivity OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        pref = new SharedPreference(this, "login_shared", Context.MODE_PRIVATE);
//        pref.putBoolean("state", false);


        if (pref.getBoolean("state", false)){
            ContentValues values = new ContentValues();
            String id = pref.getString("id", null);
            String password = pref.getString("password", null);
            boolean state = pref.getBoolean("state", false);

            values.put("id", id);
            values.put("password", password);


            NetworkLoginTask task = new NetworkLoginTask(this, loginUrl, values,
                    new SharedPreference(this, "login_shared", Context.MODE_PRIVATE),
                    new LoginData(id, password, state));

            task.execute();
        }
        else{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
