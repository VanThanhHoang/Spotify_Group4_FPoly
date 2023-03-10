package com.example.spotify_group4.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.spotify_group4.Presenter.MediaPlayerPresenter;

import java.io.IOException;

public class MediaPlayerService extends Service {
    MediaPlayer mediaPlayer;
    //aa
    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int Action = intent.getIntExtra("ACTION", 0);
        if (Action == MediaPlayerPresenter.ACTION_PLAY) {
            playMusic();
        } else if (Action == MediaPlayerPresenter.ACTION_STOP) {
            stopMusic();
        }
        return START_NOT_STICKY;
    }

    void playMusic() {
        try {
            mediaPlayer.setDataSource("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3");
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
    }

    void stopMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
