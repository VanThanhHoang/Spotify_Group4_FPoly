package com.example.spotify_group4.Presenter;

import android.content.Context;
import android.content.Intent;

import com.example.spotify_group4.Listener.MediaPlayerListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Service.MediaPlayerService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class MediaPlayerPresenter {
    public static int ACTION_PLAY = 1;
    public static int ACTION_PAUSE = 2;
    public static int ACTION_STOP = 3;
    public static int ACTION_RESUME = 4;
    public static int ACTION_SEEK = 5;
    Context context;
    MediaPlayerListener mediaPlayerListener;
    Intent intentService;
    public MediaPlayerPresenter(Context context, MediaPlayerListener mediaPlayerListener) {
        this.context = context;
        intentService = new Intent(context, MediaPlayerService.class);
        this.mediaPlayerListener = mediaPlayerListener;
    }

    public void playMusic(Song song) {
        context.stopService(intentService);
        intentService.putExtra("ACTION", ACTION_PLAY);
        intentService.putExtra("SONG",song);
        mediaPlayerListener.onSongLoad();
        context.startService(intentService);
    }
    public void resumeMusic() {
        intentService.putExtra("ACTION", ACTION_RESUME);
        mediaPlayerListener.onMusicPlay();
        context.startService(intentService);
    }
    public void seekMusic(int positionUpdate) {
        intentService.putExtra("POSITION_TO_SEEK",positionUpdate);
        intentService.putExtra("ACTION", ACTION_SEEK);
        context.startService(intentService);
    }
    public void stopMusic() {
        intentService.putExtra("ACTION", ACTION_STOP);
        context.startService(intentService);
        mediaPlayerListener.onMusicStop();
    }

    public void pauseMusic() {
        intentService.putExtra("ACTION", ACTION_PAUSE);
        context.startService(intentService);
        mediaPlayerListener.onMusicPause();
    }

    public RequestCreator loadSongImg(String url) {
        return Picasso.get().load(url);
    }
}
