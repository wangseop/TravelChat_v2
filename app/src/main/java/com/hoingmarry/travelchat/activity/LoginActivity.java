package com.hoingmarry.travelchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hoingmarry.travelchat.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editID;
    private EditText editPassword;
    private Button loginBtn;
    private Button signupBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editID = (EditText)findViewById(R.id.id_input_login);
        editPassword = (EditText)findViewById(R.id.password_input_login);
        loginBtn = (Button)findViewById(R.id.login_btn);
        signupBtn = (Button)findViewById(R.id.signup_btn);

        loginBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.login_btn:
            {

                Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                intent.putExtra("id", editID.getText().toString());
                startActivity(intent);
            }break;
            case R.id.signup_btn:
            {

            }break;
        }
    }
}
