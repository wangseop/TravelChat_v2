package com.hoingmarry.travelchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hoingmarry.travelchat.R;
import com.hoingmarry.travelchat.RequestHttpURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editID;
    private EditText editPassword;
    private EditText editPasswordConfirm;
    private Button signupBtn;
    private Button backBtn;

    private final static String signupUrl = "http://192.168.0.154:5000/signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editID = (EditText) findViewById(R.id.id_input_signup);
        editPassword = (EditText) findViewById(R.id.password_confirm_input_signup);
        editPasswordConfirm = (EditText) findViewById(R.id.password_input_signup);
        signupBtn = (Button) findViewById(R.id.signup_request_btn);
        backBtn = (Button) findViewById(R.id.back_login_btn);


        signupBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        v.startAnimation(animation);
        switch (v.getId()) {
            case R.id.signup_request_btn: {
                // HttpUrlRequest에 담을 값
                if (!validatePassword()) {
                    Toast.makeText(getApplicationContext(),
                            "패스워드 확인값과 일치하지않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues values = new ContentValues();
                values.put("id", editID.getText().toString());
                values.put("password", editPassword.getText().toString());


                SignupActivity.NetworkSignupTask task = new SignupActivity.NetworkSignupTask(signupUrl, values);
                task.execute();

//                Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
//
//                intent.putExtra("nick", editID.getText().toString());
//                intent.putExtra("cookie", editID.getText().toString());
//                startActivity(intent);
            }
            break;
            case R.id.back_login_btn: {
                this.finish();
            }
            break;
        }
    }

    public boolean validatePassword(){
        return editPassword.getText().toString().equals(editPasswordConfirm.getText().toString());
    }

    public class NetworkSignupTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;

        public NetworkSignupTask(String url, ContentValues values) {
            Log.d("Enter...", "Construct NetworkTask");
            this.url = url;
            this.values = values;
        }

        // 비동기 송수신 부분
        @Override
        protected String doInBackground(Void... params) {
            Log.d("Enter...", "doInBackground");
            String result;  // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로부터 결과물을 얻는다.

            // 수신 받은 데이터 반환
            return result;
        }

        // doInBackground 함수에서 받은 데이터로 로직 처리하는 부분
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Enter...", "onPostExecute");

            try {
                // JSon Data parsing
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) (jsonParser.parse(s));
                Log.d("JSON", jsonObject.toString());

                String state = (String) jsonObject.get("state");

                if (state.equals("OK")) {
                    Toast.makeText(SignupActivity.this.getApplicationContext(),
                            "회원 가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                    SignupActivity.this.finish();
                }


            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "ID / Password 를 확인해주세요", Toast.LENGTH_SHORT).show();

            }

            // doInBackground()로부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }
}