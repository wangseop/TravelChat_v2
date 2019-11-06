package com.hoingmarry.travelchat.network;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hoingmarry.travelchat.RequestHttpURLConnection;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.Map;

abstract public class NetworkTask extends AsyncTask<Void, Void, String> {
    protected String url;
    protected ContentValues values;
    protected Context context;

    public NetworkTask(Context context, String url, ContentValues values) {
        Log.d("Enter...", "Construct NetworkTask");
        this.context = context;
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


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }

    abstract Map<String, String> parseJSONData(String s) throws ParseException;

}
