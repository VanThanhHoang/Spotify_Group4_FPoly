package com.example.spotify_group4.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.spotify_group4.Listener.MediaPlayerListener;

public class MediaPlayerReceiver extends BroadcastReceiver {
    //action
    public static final String ACTION_TRANS_SONG = "PLAY_NEXT_SONG_FRAGMENT_MUSIC_PLAYER";
    public static final String ACTION_MUSIC_COMPLETE = "MUSIC_COMPLETE_FRAGMENT_MUSIC_PLAYER";
    public static final String ACTION_INIT_DURATION = "INIT_UI_FRAGMENT_MUSIC_PLAYER";
    public static final String ACTION_UPDATE_DURATION = "UPDATE_UI_FRAGMENT_MUSIC_PLAYER";
    // Extra
    MediaPlayerListener mediaPlayerListener;

    public MediaPlayerReceiver(MediaPlayerListener mediaPlayerListener) {
        this.mediaPlayerListener = mediaPlayerListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_INIT_DURATION:
                int fullIntDuration = intent.getIntExtra("fullIntDuration", 0);
                mediaPlayerListener.onInitInfo(fullIntDuration);
                mediaPlayerListener.onMusicPlay();
                break;
            case ACTION_UPDATE_DURATION:
                String currentDuration = intent.getStringExtra("currentDuration");
                int currentProcess = intent.getIntExtra("positionSeekbar", 0);
                mediaPlayerListener.onUpdateSeekbar(currentDuration,currentProcess);
                break;
            case ACTION_MUSIC_COMPLETE:
                mediaPlayerListener.onMusicStop();

                break;
            case ACTION_TRANS_SONG:
                int currentSongPosition = intent.getIntExtra("CURS_POSITION", 0);
                mediaPlayerListener.onTransSong(currentSongPosition);
                break;
        }
    }
}
