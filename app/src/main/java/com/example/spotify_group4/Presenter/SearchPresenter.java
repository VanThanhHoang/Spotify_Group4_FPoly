package com.example.spotify_group4.Presenter;


import android.util.Log;

import com.example.spotify_group4.Listener.SearchListener;
import com.example.spotify_group4.Model.Singer;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Retrofit.ApiSkyMusic;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter {
    SearchListener searchListener;

    public SearchPresenter(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public void searchSong(String name) {
        Call<List<Song>> callGetListSong = ApiSkyMusic.apiSkyMusic.searchSong(name);
        callGetListSong.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                searchListener.onSongResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                searchListener.onSongResponse(null);
            }
        });
    }

    public void searchSinger(String name) {
        Call<List<Singer>> callGetListSong = ApiSkyMusic.apiSkyMusic.searchSinger(name);
        callGetListSong.enqueue(new Callback<List<Singer>>() {
            @Override
            public void onResponse(Call<List<Singer>> call, Response<List<Singer>> response) {
                searchListener.onSingerResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Singer>> call, Throwable t) {

            }
        });
    }
}
