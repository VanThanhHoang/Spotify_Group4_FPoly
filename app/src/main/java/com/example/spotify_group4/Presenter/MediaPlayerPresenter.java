package com.example.spotify_group4.Presenter;

import android.content.Context;
import android.content.Intent;

import com.example.spotify_group4.Listener.MediaPlayerListener;
import com.example.spotify_group4.Service.MediaPlayerService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class MediaPlayerPresenter {
    public static int ACTION_PLAY = 1;
    public static int ACTION_PAUSE = 2;
    public static int ACTION_STOP = 3;
    public static int ACTION_RESUME = 4;
    Context context;
    MediaPlayerListener mediaPlayerListener;
    Intent intentService;
    public MediaPlayerPresenter(Context context, MediaPlayerListener mediaPlayerListener) {
        this.context = context;
        intentService = new Intent(context, MediaPlayerService.class);
        this.mediaPlayerListener = mediaPlayerListener;
    }
    public void playMusic() {
        intentService.putExtra("ACTION", ACTION_PLAY);
        intentService.putExtra("URL","https://skymusicfpoly.000webhostapp.com/music/anhdinhe.mp3");
        context.startService(intentService);
        mediaPlayerListener.onMusicPlay();
    }

    public void stopMusic() {
        intentService.putExtra("ACTION", ACTION_STOP);
        context.startService(intentService);
        mediaPlayerListener.onMusicStop();
    }
    public void pauseMusic(){
        intentService.putExtra("ACTION", ACTION_PAUSE);
        context.startService(intentService);
        mediaPlayerListener.onMusicPause();
    }

    public RequestCreator loadSongImg(String url) {
        return Picasso.get().load(url);
    }


}
