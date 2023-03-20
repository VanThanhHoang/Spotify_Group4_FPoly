package com.example.spotify_group4.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.spotify_group4.R;

public class AppSharedPreferenceHelper {
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor shareEditor;
    public AppSharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(
                String.valueOf(R.string.nameAppSharedPreference),
                Context.MODE_PRIVATE);
        shareEditor = sharedPreferences.edit();
    }
    void insertBooleanValue(String key,boolean value){
        shareEditor.putBoolean(key,value);
        shareEditor.apply();
    }
    boolean getBooleanValue(String key){
        return sharedPreferences.getBoolean(key,false);
    }
}
