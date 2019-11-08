package com.hoingmarry.travelchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.hoingmarry.travelchat.R;
import com.hoingmarry.travelchat.data.user.LoginData;
import com.hoingmarry.travelchat.network.NetworkLoginTask;
import com.hoingmarry.travelchat.utils.SharedPreference;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editID;
    private EditText editPassword;
    private Button loginBtn;
    private Button signupBtn;
    private SharedPreference pref;
    private CheckBox autoLogin;
//    private ActionBar actionbar;

    private final static String loginUrl = "http://192.168.0.154:5000/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        boolean logState = intent.getBooleanExtra("logout", false);

        // SharedPreference 생성

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        // 액션바 비활성화
//        actionbar = getSupportActionBar();
//        actionbar.hide();

        editID = findViewById(R.id.id_input_login);
        editPassword = findViewById(R.id.password_input_login);
        loginBtn = findViewById(R.id.login_btn);
        signupBtn = findViewById(R.id.signup_btn);
        autoLogin = findViewById(R.id.auto_login_check);


        findViewById(R.id.login_thumbnail).
                startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein));


        loginBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);

        autoLogin.setChecked(logState);

        // SharedPreference 호출
//        if(pref.getBoolean("state", false)){
//            Log.d("State True", "Log Confirm");
//            ContentValues values = new ContentValues();
//            values.put("id", pref.getString("id", null));
//            values.put("password", pref.getString("password", null));
//
//            NetworkLoginTask task = new NetworkLoginTask(loginUrl, values);
//            task.execute();
//        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause()", " LoginActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onStop()", " LoginActivity");
//        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy()", " LoginActivity");
    }

    @Override
    public void onClick(View v) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        v.startAnimation(animation);

        switch(v.getId())
        {
            case R.id.login_btn:
            {
                // HttpUrlRequest에 담을 값
                ContentValues values = new ContentValues();
                String id = editID.getText().toString();
                String password = editPassword.getText().toString();
                boolean autoChecked = autoLogin.isChecked();

                values.put("id", id);
                values.put("password", password);


                NetworkLoginTask task = new NetworkLoginTask(this, loginUrl, values,
                        new SharedPreference(this, "login_shared", Context.MODE_PRIVATE),
                        new LoginData(id, password, autoChecked));
                task.execute();

            }break;
            case R.id.signup_btn:
            {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }break;
        }
    }



}
