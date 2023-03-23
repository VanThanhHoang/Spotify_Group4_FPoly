package com.example.spotify_group4.Presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.example.spotify_group4.Helper.Constants;
import com.example.spotify_group4.Listener.MediaPlayerListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Service.MediaPlayerService;
import com.example.spotify_group4.SharedPreferences.AppSharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

public class MediaPlayerPresenter {
    AppSharedPreferenceHelper appSharedPreferenceHelper;
    Context context;
    MediaPlayerListener mediaPlayerListener;
    Intent intentService;
    String currentRepeatMode;

    public MediaPlayerPresenter(Context context, MediaPlayerListener mediaPlayerListener) {
        this.context = context;
        intentService = new Intent(context, MediaPlayerService.class);
        this.mediaPlayerListener = mediaPlayerListener;
        appSharedPreferenceHelper = new AppSharedPreferenceHelper(context);
    }

    public void getRepeatMode() {
        currentRepeatMode = appSharedPreferenceHelper.getRepeatMode();
        mediaPlayerListener.onChangeRepeatMode(currentRepeatMode);
    }

    public void getShuffleMode() {
        mediaPlayerListener.onChangeShuffleMode(appSharedPreferenceHelper.isShuffleModeOn());
    }
    public void setShuffleMode(){
        if(appSharedPreferenceHelper.isShuffleModeOn()){
            appSharedPreferenceHelper.setShuffleMode(false);
            mediaPlayerListener.onChangeShuffleMode(false);
        }else {
            appSharedPreferenceHelper.setShuffleMode(true);
            mediaPlayerListener.onChangeShuffleMode(true);
        }
    }
    public void setRepeatMode() {
        currentRepeatMode = appSharedPreferenceHelper.getRepeatMode();
        if (currentRepeatMode.equals(Constants.MEDIA_PLAYER_EXTRA_REPEAT_MODE_NOT_REPEAT)) {
            appSharedPreferenceHelper.setRepeatMode(Constants.MEDIA_PLAYER_EXTRA_REPEAT_MODE_REPEAT_ALL);
        } else if (currentRepeatMode.equals(Constants.MEDIA_PLAYER_EXTRA_REPEAT_MODE_REPEAT_ALL)) {
            appSharedPreferenceHelper.setRepeatMode(Constants.MEDIA_PLAYER_EXTRA_REPEAT_MODE_REPEAT_ONCE);
        } else {
            appSharedPreferenceHelper.setRepeatMode(Constants.MEDIA_PLAYER_EXTRA_REPEAT_MODE_NOT_REPEAT);
        }
        currentRepeatMode = appSharedPreferenceHelper.getRepeatMode();
        mediaPlayerListener.onChangeRepeatMode(currentRepeatMode);
        changeRepeatMode();
    }

    void changeRepeatMode() {
        intentService.putExtra(Constants.ACTION_MEDIA_PLAYER, Constants.MEDIA_PLAYER_ACTION_CHANGE_REPEAT_MODE);
        context.startService(intentService);
    }

    public void startPlayList(List<Song> songList, int position) {
        context.stopService(intentService);
        intentService.putExtra(Constants.ACTION_MEDIA_PLAYER, Constants.MEDIA_PLAYER_ACTION_PLAY_LIST_SONG);
        intentService.putExtra(Constants.MEDIA_PLAYER_EXTRA_CURRENT_SONG_POSITION, position);
        intentService.putParcelableArrayListExtra(Constants.MEDIA_PLAYER_EXTRA_SONG_LIST, (ArrayList<? extends Parcelable>) songList);
        mediaPlayerListener.onSongLoad();
        context.startService(intentService);
    }

    public void resumeMusic() {
        intentService.putExtra(Constants.ACTION_MEDIA_PLAYER, Constants.MEDIA_PLAYER_ACTION_RESUME);
        mediaPlayerListener.onMusicPlay();
        context.startService(intentService);
    }

    public void playNextSong() {
        intentService.putExtra(Constants.ACTION_MEDIA_PLAYER, Constants.MEDIA_PLAYER_ACTION_PLAY_NEXT_SONG);
        context.startService(intentService);
    }

    public void transSongByViewPager(int position) {
        intentService.putExtra(Constants.ACTION_MEDIA_PLAYER, Constants.MEDIA_PLAYER_ACTION_TRANS_SONG_VIEWPAGER);
        intentService.putExtra(Constants.MEDIA_PLAYER_EXTRA_CURRENT_SONG_POSITION, position);
        context.startService(intentService);
    }

    public void playPrevSong() {
        intentService.putExtra(Constants.ACTION_MEDIA_PLAYER, Constants.MEDIA_PLAYER_ACTION_PLAY_PREV_SONG);
        context.startService(intentService);
    }

    public void seekMusic(int positionSeek) {
        intentService.putExtra(Constants.MEDIA_PLAYER_EXTRA_SEEK_POSITION, positionSeek);
        intentService.putExtra(Constants.ACTION_MEDIA_PLAYER, Constants.MEDIA_PLAYER_ACTION_SEEK);
        context.startService(intentService);
    }

/*    public void stopMusic() {
        intentService.putExtra(Constants.ACTION_MEDIA_PLAYER, Constants.MEDIA_PLAYER_ACTION_STOP);
        context.startService(intentService);
        mediaPlayerListener.onMusicStop();
    }*/

    public void pauseMusic() {
        intentService.putExtra(Constants.ACTION_MEDIA_PLAYER, Constants.MEDIA_PLAYER_ACTION_PAUSE);
        context.startService(intentService);
        mediaPlayerListener.onMusicPause();
    }


}
