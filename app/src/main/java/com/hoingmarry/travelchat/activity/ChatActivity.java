package com.hoingmarry.travelchat.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hoingmarry.travelchat.adapter.MessageAdapter;
import com.hoingmarry.travelchat.chat.Chat;
import com.hoingmarry.travelchat.R;
import com.hoingmarry.travelchat.RequestHttpURLConnection;
import com.hoingmarry.travelchat.chat.ImageChat;
import com.hoingmarry.travelchat.customview.AttachmentTypeSelector;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

import static com.hoingmarry.travelchat.contracts.StringContract.MessageType.*;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private final int GET_GALLERY_IMAGE = 200;

    private String nick = "User";      // 단말기 닉네임

    private ImageButton button_attach;
    private EditText EditText_chat;
    private ImageButton Button_send;


    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;
    Intent intent;

    AttachmentTypeSelector attachmentTypeSelector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        button_attach = findViewById(R.id.button_attach);
        Button_send = findViewById(R.id.button_send);
        EditText_chat = findViewById(R.id.editText_chat);

        // button 리스너 추가
        button_attach.setOnClickListener(this);
        Button_send.setOnClickListener(this);



        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // 대화리스트 초기화
        mchat = new ArrayList<>();
        Log.d("mAdapter", "mAdapter");
        messageAdapter = new MessageAdapter(ChatActivity.this, mchat,"default", nick);
        // Write a message to the database

        // RecyclerView와 Adapter 연결
        recyclerView.setAdapter(messageAdapter);

        // Hello Message event 처리
        String url = "http://192.168.0.154:5000/hello";
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",nick);
        contentValues.put("msg","hello");
        // 입력 내용 비우기
        EditText_chat.setText("");

        // AsyncTask를 통해 HttpURLConnection 수행.
        ChatActivity.NetworkTask networkTask = new ChatActivity.NetworkTask(url, contentValues);
        networkTask.execute();
        //
    }

    // 클릭 리스너 재정의
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_attach:
//                showPopUp();
                break;

            case R.id.button_send:
                sendMessage();
                break;
        }
    }

    // 전송 버튼 선택
    private void sendMessage(){
        String msg = EditText_chat.getText().toString();    // msg
        Chat chat;
        if (msg != null) {
            // 사용자 입력 메세지 화면에 추가
            chat = new Chat(MSG_RIGHT, nick, "chatbot", msg);
            // 개행 처리하는 코드(json에서 parsing 못하는 에러 방지)
            // 받는쪽에서 \n -> n 으로 인식
            msg = msg.replaceAll("\n", "\\n");
            Log.d("Confirm chat", chat.getMessage());
            Log.d("Confirm msg", msg);

            ((MessageAdapter)messageAdapter).addChat(chat);
//                    mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            // URL 설정.
//            String url = "http://192.168.0.154:5000/seq2seq";
            String url = "http://192.168.0.154:5000/msgimage";
            ContentValues contentValues = new ContentValues();
            contentValues.put("name",nick);
            contentValues.put("msg",msg);
            // 입력 내용 비우기
            EditText_chat.setText("");

            // AsyncTask를 통해 HttpURLConnection 수행.
            ChatActivity.NetworkTask networkTask = new ChatActivity.NetworkTask(url, contentValues);
            networkTask.execute();
        }
    }
    private void uploadImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_GALLERY_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null &&
                data.getData() != null)
        {
            Uri selectedImageUri = data.getData();

        }
    }

    //    private void showPopUp() {
//
//        if (attachmentTypeSelector == null) {
//            attachmentTypeSelector =
//                    new AttachmentTypeSelector(ChatActivity.this,
//                            new AttachmentTypeListener());
//        }
//        attachmentTypeSelector.show(OneToOneChatActivity.this, ivAttchament);
//    }

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

                Chat chat = null;
                if (jsonObject.get("imageurl") == null)
                {
                    chat = new Chat(MSG_LEFT, (String)(jsonObject.get("sender")),
                            (String)(jsonObject.get("receiver")), (String)(jsonObject.get("message")));
                }
                else
                {
                    chat = new ImageChat(MSG_IMG_LEFT, (String)(jsonObject.get("sender")),
                            (String)(jsonObject.get("receiver")), (String)(jsonObject.get("message")),
                            (String)(jsonObject.get("imageurl")));
                }


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