package com.example.spotify_group4.Retrofit;

import com.example.spotify_group4.Model.HomeContent;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.Model.Singer;
import com.example.spotify_group4.Model.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiSkyMusic {
    Retrofit retrofitSkyMusic = new Retrofit.Builder()
            .baseUrl("https://skymusicfpolygroup3.000webhostapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ApiSkyMusic apiSkyMusic = retrofitSkyMusic.create(ApiSkyMusic.class);

    @GET("get_song_from_play_list.php")
    Call<List<Song>> getSongByPlayListId(@Query("playListId") int playListId);

    @GET("get_home_content.php")
    Call<List<HomeContent>> getHomeContent();

    @GET("get_slider_playlist.php")
    Call<List<PlayList>> getSliderPlayList();
    @GET("get_song_by_id")
    Call<List<Song>> getSongById(@Query("songId") int songId);

    @GET("get_song_by_name")
    Call<List<Song>> searchSong(@Query("songName") String songName);

    @GET("get_singer_by_name")
    Call<List<Singer>> searchSinger(@Query("singerName") String singerName);

    @GET("get_song_by_singer")
    Call<List<Song>> getSongBySingerId(@Query("singerId") int singerId);
}
