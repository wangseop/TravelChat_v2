package com.hoingmarry.travelchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hoingmarry.travelchat.R;
import com.hoingmarry.travelchat.data.user.LoginData;
import com.hoingmarry.travelchat.network.NetworkLoginTask;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editID;
    private EditText editPassword;
    private Button loginBtn;
    private Button signupBtn;
    private SharedPreferences pref;
//    private ActionBar actionbar;

    private final static String loginUrl = "http://192.168.0.154:5000/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // SharedPreference 생성
        pref = getSharedPreferences("login_shared", Context.MODE_PRIVATE);
//        pref.edit().putBoolean("state", false).commit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        // 액션바 비활성화
//        actionbar = getSupportActionBar();
//        actionbar.hide();

        editID = (EditText)findViewById(R.id.id_input_login);
        editPassword = (EditText)findViewById(R.id.password_input_login);
        loginBtn = (Button)findViewById(R.id.login_btn);
        signupBtn = (Button)findViewById(R.id.signup_btn);


        ((LinearLayout)findViewById(R.id.login_thumbnail)).
                startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein));


        loginBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);

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

                values.put("id", id);
                values.put("password", password);


                NetworkLoginTask task = new NetworkLoginTask(this,
                        loginUrl, values, new LoginData(id, password));
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
