package com.example.spotify_group4.Service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.spotify_group4.App.MyApplication;
import com.example.spotify_group4.Presenter.MediaPlayerPresenter;

import java.io.IOException;

public class MediaPlayerService extends Service {
    MediaPlayer mediaPlayer;
    boolean isExits = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    void prepareMusic(String url) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int Action = intent.getIntExtra("ACTION", 0);
        String url = intent.getStringExtra("URL");
        if (!isExits) {
            isExits = true;
            prepareMusic(url);
        }
        if (Action == MediaPlayerPresenter.ACTION_PLAY) {
            playMusic();
        } else if (Action == MediaPlayerPresenter.ACTION_STOP) {
            stopMusic();
        } else {
            pauseMusic();
        }
        return START_NOT_STICKY;
    }

    void playMusic() {
        mediaPlayer.start();
    }

    void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            stopSelf();
        }
    }

    void pauseMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    void createNotification(){
        /*Notification notification = new NotificationCompat.Builder(this,
                MyApplication.NOTIFICATION_CHANEL_MUSIC_ID,
                "test",);*/
    }
}
