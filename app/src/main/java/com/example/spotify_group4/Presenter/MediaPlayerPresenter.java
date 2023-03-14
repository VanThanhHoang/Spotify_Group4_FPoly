package com.example.spotify_group4.Presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import com.example.spotify_group4.Listener.MediaPlayerListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Service.MediaPlayerService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MediaPlayerPresenter {
    public static int ACTION_PLAY_LIST_SONG = 1;
    public static int ACTION_PLAY = 1;
    public static int ACTION_PAUSE = 2;
    public static int ACTION_STOP = 3;
    public static int ACTION_RESUME = 4;
    public static int ACTION_SEEK = 5;
    public static int ACTION_PLAY_NEXT_SONG = 6;
    public static int ACTION_PLAY_PREV_SONG = 7;
    public static int ACTION_TRANS_SONG_VIEWPAGER =8 ;
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
    public void startPlayList(List<Song> songList,int position) {
        context.stopService(intentService);
        intentService.putExtra("ACTION", ACTION_PLAY_LIST_SONG);
        intentService.putExtra("CURRENT_SONG_POSITION",position);
        intentService.putParcelableArrayListExtra("SONG_LIST",(ArrayList<? extends Parcelable>) songList);
        mediaPlayerListener.onSongLoad();
        context.startService(intentService);
    }
    public void resumeMusic() {
        intentService.putExtra("ACTION", ACTION_RESUME);
        mediaPlayerListener.onMusicPlay();
        context.startService(intentService);
    }
    public void playNextSong() {
        intentService.putExtra("ACTION", ACTION_PLAY_NEXT_SONG);
        mediaPlayerListener.onTransSong();
        context.startService(intentService);
    }
    public void transSongByViewPager(int position){
        intentService.putExtra("ACTION", ACTION_TRANS_SONG_VIEWPAGER);
        intentService.putExtra("CURRENT_SONG_POSITION",position);
        context.startService(intentService);
    }
    public void playPrevSong() {
        intentService.putExtra("ACTION", ACTION_PLAY_PREV_SONG);
        mediaPlayerListener.onTransSong();
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
