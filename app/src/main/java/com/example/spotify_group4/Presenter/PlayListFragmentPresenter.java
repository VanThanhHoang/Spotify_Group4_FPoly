package com.example.spotify_group4.Presenter;

import androidx.annotation.NonNull;
import com.example.spotify_group4.Listener.GetSongListListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Retrofit.ApiSkyMusic;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayListFragmentPresenter {
    GetSongListListener mGetSongListListener;

    public PlayListFragmentPresenter(GetSongListListener GetSongListListener) {
        mGetSongListListener = GetSongListListener;
    }

    public void getSongByUserPlayList(int playListId) {
        Call<List<Song>> callGetSongList = ApiSkyMusic.apiSkyMusic.getSongByUserPlayListId(playListId);
        callGetSongList.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                mGetSongListListener.onGetSongListComplete(response.body());

            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                mGetSongListListener.onGetSongListFail();
            }
        });
    }

    public void getSongListByPlayListId(int playListId) {
        Call<List<Song>> callGetSongList = ApiSkyMusic.apiSkyMusic.getSongByPlayListId(playListId);
        callGetSongList.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                mGetSongListListener.onGetSongListComplete(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                mGetSongListListener.onGetSongListFail();
            }
        });
    }

    public void getSongLiked(String userId) {
        Call<List<Song>> callGetListSong = ApiSkyMusic.apiSkyMusic.getSongLiked(userId);
        callGetListSong.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                mGetSongListListener.onGetSongListComplete(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
            }
        });
    }

    public void getSongListBySingerId(int singerId) {
        Call<List<Song>> callGetSongList = ApiSkyMusic.apiSkyMusic.getSongBySingerId(singerId);
        callGetSongList.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                mGetSongListListener.onGetSongListComplete(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
            }
        });
    }
}
