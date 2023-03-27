package com.example.spotify_group4.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.spotify_group4.Helper.Constants;
import com.example.spotify_group4.Listener.MediaPlayerListener;
import com.example.spotify_group4.Model.Song;

import java.util.List;

public class MediaPlayerReceiver extends BroadcastReceiver {
    //action
    public static final String ACTION_TRANS_SONG = "PLAY_NEXT_SONG_FRAGMENT_MUSIC_PLAYER";
    public static final String ACTION_MUSIC_COMPLETE = "MUSIC_COMPLETE_FRAGMENT_MUSIC_PLAYER";
    public static final String ACTION_INIT_DURATION = "INIT_UI_FRAGMENT_MUSIC_PLAYER";
    public static final String ACTION_UPDATE_DURATION = "UPDATE_UI_FRAGMENT_MUSIC_PLAYER";
    public static final String ACTION_SHUFFLED_PLAY_LIST = "SHUFFLED_PLAY_LIST_MUSIC_PLAYER";
    public static final String ACTION_PAUSE_MUSIC = "ACTION_PAUSE_MUSIC";
    public static final String ACTION_RESUME_MUSIC = "ACTION_PLAY_MUSIC";
    // Extra
    MediaPlayerListener mMediaPlayerListener;

    public MediaPlayerReceiver(MediaPlayerListener mediaPlayerListener) {
        this.mMediaPlayerListener = mediaPlayerListener;
    }

    public void setMediaPlayerListener(MediaPlayerListener mediaPlayerListener) {
        this.mMediaPlayerListener = mediaPlayerListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_INIT_DURATION:
                int fullIntDuration = intent.getIntExtra("fullIntDuration", 0);
                mMediaPlayerListener.onInitInfo(fullIntDuration);
                mMediaPlayerListener.onMusicPlay();
                break;
            case ACTION_UPDATE_DURATION:
                String currentDuration = intent.getStringExtra("currentDuration");
                int currentProcess = intent.getIntExtra("positionSeekbar", 0);
                mMediaPlayerListener.onUpdateSeekbar(currentDuration, currentProcess);
                break;
            case ACTION_MUSIC_COMPLETE:
                mMediaPlayerListener.onMusicStop();
                break;
            case ACTION_TRANS_SONG:
                int currentSongPosition = intent.getIntExtra(Constants.MEDIA_PLAYER_EXTRA_CURRENT_SONG_POSITION, 0);
                mMediaPlayerListener.onTransSong(currentSongPosition);
                break;
            case ACTION_SHUFFLED_PLAY_LIST:
                List<Song> songList = intent.getParcelableArrayListExtra(Constants.MEDIA_PLAYER_EXTRA_LIST_SHUFFLED);
                mMediaPlayerListener.onPlayListShuffled(songList);
                break;
            case ACTION_PAUSE_MUSIC:
                mMediaPlayerListener.onMusicPause();
                break;
            case ACTION_RESUME_MUSIC:
                mMediaPlayerListener.onMusicPlay();
                break;
        }
    }
}
