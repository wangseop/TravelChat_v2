package com.hoingmarry.travelchat.network;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.hoingmarry.travelchat.activity.ChatActivity;
import com.hoingmarry.travelchat.data.user.LoginData;
import com.hoingmarry.travelchat.utils.SharedPreference;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

public class NetworkLoginTask extends NetworkTask {
    private SharedPreference pref;
    private LoginData loginData;

    public NetworkLoginTask(Context context, String url, ContentValues values,
                            SharedPreference pref, LoginData loginData) {
        super(context, url, values);
        this.pref = pref;
        this.loginData = loginData;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try{

            Map<String, String> jsonMap = parseJSONData(s);

            // 로그인 정보 확인 성공 시
            if(jsonMap.get("state").equals("OK")){
                // 로그인 정보 저장
                if(loginData.isAutoChecked())
                    pref.saveLoginData(context, loginData);

                Intent intent = new Intent(context, ChatActivity.class);

//                    intent.putExtra("nick", pref.getString("id",null));
                // 출력할 닉네임 전달(지금은 ID 사용)
                intent.putExtra("nick", jsonMap.get("nick"));
                // 쿠키값 전달
                intent.putExtra("cookie", jsonMap.get("cookie"));

                context.startActivity(intent);
            }
        }
        catch(ParseException e){
            e.printStackTrace();
        }catch(NullPointerException e){
            // 로그인 실패 시
            Toast.makeText(context, "ID / Password 를 확인해주세요", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected Map<String, String> parseJSONData(String s) throws ParseException {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) (jsonParser.parse(s));
        Log.d("JSON", jsonObject.toString());
        String state = (String)jsonObject.get("state");
        String nick = (String)jsonObject.get("nick");
        String cookie = (String)jsonObject.get("cookie");

        Map<String, String> data = new HashMap<>();
        data.put("state", state);
        data.put("nick", nick);
        data.put("cookie", cookie);

        return data;
    }



}
