package com.example.spotify_group4.Presenter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.example.spotify_group4.Listener.MediaPlayerListener;
import com.example.spotify_group4.Service.MediaPlayerService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class MediaPlayerPresenter {
    public static int ACTION_PLAY = 1;
    public static int ACTION_PAUSE = 2;
    public static int ACTION_STOP = 3;
    Context context;
    MediaPlayerListener mediaPlayerListener;
    Intent intentService;

    public void playMusic() {
        intentService.putExtra("ACTION", ACTION_PLAY);
        context.startService(intentService);
        mediaPlayerListener.onMusicPlay();
    } public void stopMusic() {
        intentService.putExtra("ACTION", ACTION_STOP);
        context.startService(intentService);
        mediaPlayerListener.onMusicStop();
    }

    public RequestCreator loadSongImg(String url){
       return Picasso.get().load(url);
    }
    public MediaPlayerPresenter(Context context, MediaPlayerListener mediaPlayerListener) {
        this.context = context;
        this.mediaPlayerListener = mediaPlayerListener;
        intentService = new Intent(context, MediaPlayerService.class);
    }
}
