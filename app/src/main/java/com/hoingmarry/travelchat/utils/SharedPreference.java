package com.hoingmarry.travelchat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hoingmarry.travelchat.data.user.LoginData;


public class SharedPreference {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SharedPreference(Context context, String name, int mode){
        pref = context.getSharedPreferences(name, mode);
        editor = pref.edit();
    }

    // Boolean
    public void putBoolean(String key, boolean defaultVal){
        editor.putBoolean(key, defaultVal).commit();
    }
    public boolean getBoolean(String key, boolean defaultVal){
        return pref.getBoolean(key, defaultVal);
    }

    // String
    public void putString(String key, String defaultVal){
        editor.putString(key, defaultVal).commit();
    }
    public String getString(String key, String defaultVal){
        return pref.getString(key, defaultVal);
    }

    public void DataClear(){
        editor.remove("state").commit();
        editor.remove("id").commit();
        editor.remove("password").commit();
    }

    public void saveLoginData(Context context, LoginData loginData){
        if(!this.getBoolean("state", false)){
            this.putBoolean("state", true);
            this.putString("id", loginData.getId());
            this.putString("password", loginData.getPassword());
        }
    }

    public void deleteLoginData(Context context, boolean isLogout){
        if (isLogout)
            DataClear();
    }
}
