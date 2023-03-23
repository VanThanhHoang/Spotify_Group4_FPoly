package com.example.spotify_group4.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.spotify_group4.Helper.Constants;
import com.example.spotify_group4.R;

public class AppSharedPreferenceHelper {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor shareEditor;

    public AppSharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(
                String.valueOf(R.string.nameAppSharedPreference),
                Context.MODE_PRIVATE);
        shareEditor = sharedPreferences.edit();
    }

    public void setRepeatMode(String repeatMode) {
        shareEditor.putString(Constants.MEDIA_KEY_REPEAT_MODE, repeatMode);
        shareEditor.apply();
    }

    public String getRepeatMode() {
        return sharedPreferences.getString(Constants.MEDIA_KEY_REPEAT_MODE, Constants.MEDIA_PLAYER_EXTRA_REPEAT_MODE_NOT_REPEAT);
    }

    public void setShuffleMode(Boolean isTurnOnShuffleMde) {
        shareEditor.putBoolean(Constants.MEDIA_KEY_SHUFFLE_MODE, isTurnOnShuffleMde);
        shareEditor.apply();
    }

    public boolean isShuffleModeOn() {
        return sharedPreferences.getBoolean(Constants.MEDIA_KEY_SHUFFLE_MODE, false);
    }


}
