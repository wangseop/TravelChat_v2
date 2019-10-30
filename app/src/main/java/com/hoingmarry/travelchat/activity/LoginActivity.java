package com.hoingmarry.travelchat.activity;

import androidx.appcompat.app.ActionBar;
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
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hoingmarry.travelchat.R;
import com.hoingmarry.travelchat.RequestHttpURLConnection;
import com.hoingmarry.travelchat.adapter.MessageAdapter;
import com.hoingmarry.travelchat.chat.Chat;
import com.hoingmarry.travelchat.chat.ImageChat;
import com.hoingmarry.travelchat.chat.MapChat;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static com.hoingmarry.travelchat.contracts.StringContract.MessageType.MSG_IMG_LEFT;
import static com.hoingmarry.travelchat.contracts.StringContract.MessageType.MSG_LEFT;
import static com.hoingmarry.travelchat.contracts.StringContract.MessageType.MSG_MAP_LEFT;

import static com.hoingmarry.travelchat.contracts.StringContract.ResultCode.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editID;
    private EditText editPassword;
    private Button loginBtn;
    private Button signupBtn;
    private ActionBar actionbar;

    private final static String loginUrl = "http://192.168.0.154:5000/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 액션바 비활성화
        actionbar = getSupportActionBar();
        actionbar.hide();

        editID = (EditText)findViewById(R.id.id_input_login);
        editPassword = (EditText)findViewById(R.id.password_input_login);
        loginBtn = (Button)findViewById(R.id.login_btn);
        signupBtn = (Button)findViewById(R.id.signup_btn);


        ((LinearLayout)findViewById(R.id.login_thumbnail)).
                startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein));


        loginBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);

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
                values.put("id", editID.getText().toString());
                values.put("password", editPassword.getText().toString());


                NetworkLoginTask task = new NetworkLoginTask(loginUrl, values);
                task.execute();
//                Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
//
//                intent.putExtra("nick", editID.getText().toString());
//                intent.putExtra("cookie", editID.getText().toString());
//                startActivity(intent);
            }break;
            case R.id.signup_btn:
            {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }break;
        }
    }
    public class NetworkLoginTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;

        public NetworkLoginTask(String url, ContentValues values) {
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

                String state = (String)jsonObject.get("state");
                String nick = (String)jsonObject.get("nick");
                String cookie = (String)jsonObject.get("cookie");

                if (state.equals("OK"))
                {

                    Intent intent = new Intent(LoginActivity.this, ChatActivity.class);

                    intent.putExtra("nick", editID.getText().toString());
                    intent.putExtra("cookie", cookie);
                    startActivity(intent);

                }


            } catch (ParseException e) {
                e.printStackTrace();
            }catch(NullPointerException e){
                Toast.makeText(getApplicationContext(), "ID / Password 를 확인해주세요", Toast.LENGTH_SHORT).show();

            }

            // doInBackground()로부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }

}
