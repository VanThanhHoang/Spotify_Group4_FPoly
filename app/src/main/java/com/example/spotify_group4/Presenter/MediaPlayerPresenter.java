package com.example.spotify_group4.Presenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.spotify_group4.Helper.Constants;
import com.example.spotify_group4.Listener.MediaPlayerListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.R;
import com.example.spotify_group4.Retrofit.ApiSkyMusic;
import com.example.spotify_group4.Service.MediaPlayerService;
import com.example.spotify_group4.SharedPreferences.AppSharedPreferenceHelper;
import com.example.spotify_group4.View.Dialog.DialogAddSongToPlayList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void setShuffleMode() {
        if (appSharedPreferenceHelper.isShuffleModeOn()) {
            appSharedPreferenceHelper.setShuffleMode(false);
            mediaPlayerListener.onChangeShuffleMode(false);
        } else {
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
        context.startService(intentService);
    }

    public void continuesMediaPlayer() {
        intentService.putExtra(Constants.ACTION_MEDIA_PLAYER, Constants.MEDIA_PLAYER_ACTION_CONTINUES_MEDIA_PLAYER);
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

    public void pauseMusic() {
        intentService.putExtra(Constants.ACTION_MEDIA_PLAYER, Constants.MEDIA_PLAYER_ACTION_PAUSE);
        context.startService(intentService);
    }

    public void likeSong(String userId, int songId) {
        Call<Void> likeSong = ApiSkyMusic.apiSkyMusic.likeSong(userId, songId);
        likeSong.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });
    }

    public void getSumLike(int songId) {
        Call<Integer> getSumLike = ApiSkyMusic.apiSkyMusic.getSumLike(songId);
        getSumLike.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if(response.body()!=null){
                    mediaPlayerListener.onGetSumLike(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
            }
        });
    }

    public void stopService() {
        context.stopService(intentService);
    }

    public void isSongLiked(String userId, int songId) {
        Call<String> callIsLikeSong = ApiSkyMusic.apiSkyMusic.isLikeSong(userId, songId);
        callIsLikeSong.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.body() != null) {
                    mediaPlayerListener.likeSong(response.body().equals("isLike"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, Throwable t) {
            }
        });
    }

}
