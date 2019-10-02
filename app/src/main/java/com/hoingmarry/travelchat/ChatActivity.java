package com.hoingmarry.travelchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatList;
    private String nick = "User";      // 단말기 닉네임

    private EditText EditText_chat;
    private Button Button_send;

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
                    ChatData chat = new ChatData();
                    chat.setNickname(nick);
                    chat.setMessage(msg);

                    ((ChatAdapter)mAdapter).addChat(chat);
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



        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        chatList = new ArrayList<>();
        Log.d("mAdapter", "mAdapter");
        mAdapter = new ChatAdapter(chatList, ChatActivity.this, nick);
        // Write a message to the database

        // RecyclerView와 Adapter 연결
        mRecyclerView.setAdapter(mAdapter);



        // 1. recyclerView - loop
        // 2. db 내용 넣는다
        // 3. 상대방 폰에 채팅 내용이 보임 - get

        // 1-1. recyclerview - chat data
        // 1. message, nickname, isMine - Data Transfer Object
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

                ChatData chat = new ChatData((String)(jsonObject.get("message"))
                        , (String)(jsonObject.get("nickname")));

                ((ChatAdapter)mAdapter).addChat(chat);
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);


            }
            catch(ParseException e)
            {
                e.printStackTrace();
            }

            // doInBackground()로부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }
}