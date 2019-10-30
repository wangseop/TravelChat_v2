package com.hoingmarry.travelchat.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hoingmarry.travelchat.adapter.MessageAdapter;
import com.hoingmarry.travelchat.chat.Chat;
import com.hoingmarry.travelchat.R;
import com.hoingmarry.travelchat.RequestHttpURLConnection;
import com.hoingmarry.travelchat.chat.ImageChat;
import com.hoingmarry.travelchat.chat.ImageThumbChat;
import com.hoingmarry.travelchat.chat.MapChat;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.hoingmarry.travelchat.contracts.StringContract.MessageType.*;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private final int GET_GALLERY_IMAGE = 200;

    private String nick = "User";      // 단말기 닉네임
    private String cookie;
    private ImageButton button_attach;
    private EditText EditText_chat;
    private ImageButton Button_send;
    private RelativeLayout selectImgLayout;
    private ActionBar actionbar;


    private final String welcomePath = "http://192.168.0.154:5000/hello";
    private final String messagePath = "http://192.168.0.154:5000/msgmap";
    private String messageImgPath = "http://192.168.0.154:5000/img";
    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;


    private static final Pattern IP_ADDRESS
            = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");
    final int SELECT_MULTIPLE_IMAGES = 1;
    ArrayList<String> selectedImagesPaths; // Paths of the image(s) selected by the user.
    boolean imagesSelected = false; // Whether the user selected at least an image or not.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        nick = intent.getStringExtra("nick");
        cookie = intent.getStringExtra("cookie");

        // 액션바 비활성화
        actionbar = getSupportActionBar();
        actionbar.hide();

        // Manifest에 설정할 권한 부여
        ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.INTERNET}, 2);
        ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        setContentView(R.layout.activity_chat);

        button_attach = findViewById(R.id.button_attach);
        Button_send = findViewById(R.id.button_send);
        EditText_chat = findViewById(R.id.editText_chat);

        selectImgLayout = findViewById(R.id.add_img_layout);
        ((ImageButton)selectImgLayout.getChildAt(1)).setOnClickListener(this);

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



        //  MessageAdapter 설정
        messageAdapter = new MessageAdapter(
                ChatActivity.this, mchat,"default", nick);
        // Write a message to the database

        // RecyclerView와 Adapter 연결
        recyclerView.setAdapter(messageAdapter);


//        // Hello Message event 처리
        String url = welcomePath;
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",nick);
        contentValues.put("msg","welcom");
        // 쿠키 추가
        contentValues.put("cookie", cookie);

        // 입력 내용 비우기
        EditText_chat.setText("");

        // AsyncTask를 통해 HttpURLConnection 수행.
        ChatActivity.NetworkTask networkTask = new ChatActivity.NetworkTask(url, contentValues);
        networkTask.execute();
        //
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    // 클릭 리스너 재정의
    @Override
    public void onClick(View v) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        v.startAnimation(animation);
        switch(v.getId()){
            case R.id.button_attach:
                selectImage(v);
//                uploadImage();
                break;

            case R.id.button_send:
                if (imagesSelected) connectServer(v);
                else sendMessage();
                break;
            case R.id.img_delete_btn:
                RelativeLayout parent = (RelativeLayout)v.getParent();
                imageSelectedLayoutClose(parent);
                break;
        }
    }
    private void imageSelectedLayoutClose(RelativeLayout parent){

        ((ImageView)parent.findViewById(R.id.add_img)).setImageURI(null);
        parent.setVisibility(View.GONE);

        selectedImagesPaths = null;
        imagesSelected = false;
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
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
            // URL 설정.
            String url = messagePath;
            ContentValues contentValues = new ContentValues();
            contentValues.put("name",nick);
            contentValues.put("msg",msg);
            contentValues.put("cookie", cookie);
            // 입력 내용 비우기
            EditText_chat.setText("");

            // AsyncTask를 통해 HttpURLConnection 수행.
            ChatActivity.NetworkTask networkTask = new ChatActivity.NetworkTask(url, contentValues);
            networkTask.execute();
        }
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {
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

                Chat chat = null;
                if(jsonObject.get("latitude") != null && jsonObject.get("longitude") != null){
                    chat = new MapChat(MSG_MAP_LEFT, (String) (jsonObject.get("sender")),
                            (String) (jsonObject.get("receiver")), (String) (jsonObject.get("message")),
                            (String) (jsonObject.get("imageurl")), (String) (jsonObject.get("link")),
                            Double.parseDouble((String)(jsonObject.get("latitude"))),
                            Double.parseDouble((String)(jsonObject.get("longitude"))));
                }
                else if (jsonObject.get("imageurl") != null) {
                    chat = new ImageChat(MSG_IMG_LEFT, (String) (jsonObject.get("sender")),
                            (String) (jsonObject.get("receiver")), (String) (jsonObject.get("message")),
                            (String) (jsonObject.get("imageurl")), "");
//                    chat = new ImageThumbChat(MSG_IMG_THUMB_LEFT, (String) (jsonObject.get("sender")),
//                            (String) (jsonObject.get("receiver")), (String) (jsonObject.get("message")),
//                            (String) (jsonObject.get("imageurl")), (String) (jsonObject.get("link")),
//                            (String) (jsonObject.get("message")));

                }
                else {
                    chat = new Chat(MSG_LEFT, (String) (jsonObject.get("sender")),
                            (String) (jsonObject.get("receiver")), (String) (jsonObject.get("message")));
                }


                ((MessageAdapter) messageAdapter).addChat(chat);
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);


            } catch (ParseException e) {
                e.printStackTrace();
            }catch(NullPointerException e){
                Toast.makeText(getApplicationContext(),
                        "Data 누락", Toast.LENGTH_SHORT).show();

            }

            // doInBackground()로부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.

        }
    }


    // permission 처리 로직
    @Override
    public void onRequestPermissionsResult ( int requestCode, String permissions[],
    int[] grantResults){
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "Access to Storage Permission Granted. Thanks.", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getApplicationContext(), "Access to Storage Permission Denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "Access to Internet Permission Granted. Thanks.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Access to Internet Permission Denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

//
    public void connectServer(View v) {


        String ipv4Address = "192.168.0.154";
        String portNumber = "5000";
        String otherPath = "/img";
        String postUrl = "http://" + ipv4Address + ":" + portNumber + otherPath;

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (int i = 0; i < selectedImagesPaths.size(); i++) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                // Read BitMap by file path.
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagesPaths.get(i), options);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Please Make Sure the Selected File is an Image.", Toast.LENGTH_SHORT).show();
                return;
            }
            byte[] byteArray = stream.toByteArray();

            multipartBodyBuilder.addFormDataPart("image" + i, "Android_Flask_" + i + ".jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray));
        }


        RequestBody postBodyImage = multipartBodyBuilder.build();

        // Image 사용자 Message로 출력


        ((MessageAdapter)messageAdapter).addChat(new ImageChat(IMG_RIGHT, nick, "chatbot", "",
                String.copyValueOf(selectedImagesPaths.get(0).toCharArray()), ""));

        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

//        postRequest(postUrl, postBodyImage);
        postRequest(messageImgPath, postBodyImage);
        imageSelectedLayoutClose(selectImgLayout);
    }

    void postRequest(String postUrl, RequestBody postBody) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .addHeader("cookie", cookie)
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();
                Log.d("FAIL", e.getMessage());

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Failed to Connect to Server. Please Try Again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                try {
                    String res = response.body().string();
                    // 응답 성공시
                    Chat chat = null;
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject)jsonParser.parse(res);
//
                    chat = new ImageChat(MSG_IMG_LEFT, (String)jsonObject.get("sender")
                            , (String)jsonObject.get("receiver"),
                            (String)jsonObject.get("message"), (String) (jsonObject.get("imageurl")), "");


                    ((MessageAdapter) messageAdapter).addChat(chat);
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                    imagesSelected = false;
//                            Toast.makeText(getApplicationContext(),
//                                    "Server's Response\n" + response.body().string(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch(ParseException e){
                    e.printStackTrace();
                }catch(NullPointerException e){
                    Toast.makeText(getApplicationContext(), "사진 데이터 누락", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void selectImage(View v) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_MULTIPLE_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == SELECT_MULTIPLE_IMAGES && resultCode == RESULT_OK && null != data) {
                // When a single image is selected.
                String currentImagePath;
                selectedImagesPaths = new ArrayList<>();
                if (data.getData() != null) {
                    Uri uri = data.getData();
                    currentImagePath = getPath(getApplicationContext(), uri);
                    Log.d("ImageDetails", "Single Image URI : " + uri);
                    Log.d("ImageDetails", "Single Image Path : " + currentImagePath);
                    selectedImagesPaths.add(currentImagePath);
                    imagesSelected = true;
                    Toast.makeText(getApplicationContext(),
                            "Number of Selected Images : " + selectedImagesPaths.size(), Toast.LENGTH_SHORT).show();
                } else {
                    // When multiple images are selected.
                    // Thanks tp Laith Mihyar for this Stackoverflow answer : https://stackoverflow.com/a/34047251/5426539
                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();
//                        for (int i = 0; i < clipData.getItemCount(); i++) {
                        for (int i = 0; i < 1; i++) {

                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();

                            currentImagePath = getPath(getApplicationContext(), uri);
                            selectedImagesPaths.add(currentImagePath);
                            Log.d("ImageDetails", "Image URI " + i + " = " + uri);
                            Log.d("ImageDetails", "Image Path " + i + " = " + currentImagePath);
                            imagesSelected = true;
                            Toast.makeText(getApplicationContext(),
                                    "Number of Selected Images : " + 1, Toast.LENGTH_SHORT).show();

//                            Toast.makeText(getApplicationContext(),
//                                    "Number of Selected Images : " + selectedImagesPaths.size(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                ((ImageView)selectImgLayout.getChildAt(0)).setImageURI(Uri.parse(selectedImagesPaths.get(0)));
                selectImgLayout.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "You haven't Picked any Image.", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getApplicationContext(), selectedImagesPaths.size() + " Image(s) Selected.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Something Went Wrong.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // Implementation of the getPath() method and all its requirements is taken from the StackOverflow Paul Burke's answer: https://stackoverflow.com/a/20559175/5426539
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


}