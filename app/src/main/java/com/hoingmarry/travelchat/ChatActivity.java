package com.hoingmarry.travelchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private String nick = "User";      // 단말기 닉네임

    private EditText EditText_chat;
    private ImageButton Button_send;

    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        Button_send = findViewById(R.id.button_send);
        EditText_chat = findViewById(R.id.editText_chat);

        Button_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String msg = EditText_chat.getText().toString();    // msg

                if (msg != null) {
                    // 사용자 입력 메세지 화면에 추가
                    Chat chat = new Chat(nick, "chatbot", msg);

                    ((MessageAdapter)messageAdapter).addChat(chat);
//                    mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                    // URL 설정.
                    String url = "http://192.168.0.154:5000/";
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name",nick);
                    contentValues.put("msg",msg);

                    // AsyncTask를 통해 HttpURLConnection 수행.
                    ChatActivity.NetworkTask networkTask = new ChatActivity.NetworkTask(url, contentValues);
                    networkTask.execute();
                }
            }
        });


        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        mchat = new ArrayList<>();
        Log.d("mAdapter", "mAdapter");
        messageAdapter = new MessageAdapter(ChatActivity.this, mchat,"default", nick);
        // Write a message to the database

        // RecyclerView와 Adapter 연결
        recyclerView.setAdapter(messageAdapter);

    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values){
            Log.d("Enter...", "Construct NetworkTask");
            this.url = url;
            this.values = values;
        }

        // 비동기 송수신 부분
        @Override
        protected String doInBackground(Void... params){
            Log.d("Enter...", "doInBackground");
            String result;  // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로부터 결과물을 얻는다.

            // 수신 받은 데이터 반환
            return result;
        }

        // doInBackground 함수에서 받은 데이터로 로직 처리하는 부분
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            Log.d("Enter...", "onPostExecute");

            try
            {
                // JSon Data parsing
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject)(jsonParser.parse(s));
                Log.d("JSON", jsonObject.toString());

                Chat chat = new Chat((String)(jsonObject.get("sender")),
                         (String)(jsonObject.get("receiver")), (String)(jsonObject.get("message")));

                ((MessageAdapter)messageAdapter).addChat(chat);
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);


            }
            catch(ParseException e)
            {
                e.printStackTrace();
            }

            // doInBackground()로부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }
}