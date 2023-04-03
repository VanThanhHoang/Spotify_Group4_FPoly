package com.example.spotify_group4.Presenter;



import com.example.spotify_group4.Listener.SearchListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Retrofit.ApiSkyMusic;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter {
    SearchListener searchListener ;

    public SearchPresenter(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public void search(String name){
        Call<List<Song>> callGetListSong = ApiSkyMusic.apiSkyMusic.searchSong(name);
        callGetListSong.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                searchListener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });
        searchListener.onSearching();
    }
}
