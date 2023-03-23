package com.example.spotify_group4.App;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.annotation.NonNull;

import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.R;
import com.example.spotify_group4.Retrofit.ApiSkyMusic;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApplication extends Application {
    public static final String NOTIFICATION_CHANEL_MUSIC_ID = String.valueOf(R.string.ID_CHANEL_MUSIC);

    @Override
    public void onCreate() {
        createNotificationChanel();
        super.onCreate();
    }
    public void createNotificationChanel() {
        NotificationChannel notificationMusicChannel = new NotificationChannel(NOTIFICATION_CHANEL_MUSIC_ID,
                getResources().getString(R.string.notification_music_chanel_name),
                NotificationManager.IMPORTANCE_LOW);
        NotificationManager manager = getSystemService(NotificationManager.class);
        if(manager!=null){
            manager.createNotificationChannel(notificationMusicChannel);
        }
    }

}
