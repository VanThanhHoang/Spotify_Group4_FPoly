package com.example.spotify_group4.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.spotify_group4.Presenter.MediaPlayerPresenter;
import com.example.spotify_group4.View.Fragment.MusicPlayFragment;

import java.io.IOException;

public class MediaPlayerService extends Service {
    MediaPlayer mediaPlayer;
    boolean isExits = false;
    Handler handler;
    private Runnable updateSeekBar;

    @Override
    public void onCreate() {
        handler = new Handler();
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    updateDuration();
                    handler.postDelayed(this, 1000);
                }
            }
        };
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            handler.removeCallbacks(updateSeekBar);
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    void prepareMusic(String url) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
            initDuration();
            handler.postDelayed(updateSeekBar, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void seekMedia(int position){
        mediaPlayer.seekTo(position);
    }
    void initDuration() {
        Intent intent = new Intent(MusicPlayFragment.ACTION_INIT_DURATION);
        String fullDuration = formatTime(mediaPlayer.getDuration());
        intent.putExtra("fullIntDuration", mediaPlayer.getDuration());
        intent.putExtra("fullDuration", fullDuration);
        sendBroadcast(intent);
    }

    void updateDuration() {
        Intent intent = new Intent(MusicPlayFragment.ACTION_UPDATE_DURATION);
        int positionMusic = mediaPlayer.getCurrentPosition();
        String currentDuration = formatTime(positionMusic);
        int positionSeekbar = getPositionSeekbar(positionMusic);
        intent.putExtra("positionSeekbar", positionSeekbar);
        intent.putExtra("currentDuration", currentDuration);
        sendBroadcast(intent);
    }

    int getPositionSeekbar(int position) {
        float currentPosition = (((float) position/mediaPlayer.getDuration())*1000);
        return (int) currentPosition;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra("ACTION", 0);
        String url = intent.getStringExtra("URL");
        int positionToSeek = intent.getIntExtra("POSITION_TO_SEEK",0);
        if (!isExits) {
            isExits = true;
            prepareMusic(url);
        }
        if(action == MediaPlayerPresenter.ACTION_SEEK){
            seekMedia(positionToSeek);
        }
        //action
        handleAction(action);
        return START_NOT_STICKY;
    }

    void handleAction(int action) {
        if (action == MediaPlayerPresenter.ACTION_PLAY || action == MediaPlayerPresenter.ACTION_RESUME) {
            playMusic();
        } else if (action == MediaPlayerPresenter.ACTION_STOP) {
            stopMusic();
        } else if(action==MediaPlayerPresenter.ACTION_PAUSE){
            pauseMusic();
        }
    }

    void playMusic() {
        Toast.makeText(this, "play", Toast.LENGTH_SHORT).show();
        handler.postDelayed(updateSeekBar, 1000);
        mediaPlayer.start();

    }

    void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            handler.removeCallbacks(updateSeekBar);
            mediaPlayer.release();
        }
    }

    void pauseMusic() {
        handler.removeCallbacks(updateSeekBar);
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

    @SuppressLint("DefaultLocale")
    private String formatTime(int duration) {
        int minutes = (duration / 1000) / 60; // tính số phút
        int seconds = (duration / 1000) % 60; // tính số giây
        return String.format("%02d:%02d", minutes, seconds);
    }
}
