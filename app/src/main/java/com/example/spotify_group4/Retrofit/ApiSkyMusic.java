package com.example.spotify_group4.Retrofit;

import com.example.spotify_group4.Model.HomeContent;
import com.example.spotify_group4.Model.PlayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
public interface ApiSkyMusic {
    Retrofit retrofitSkyMusic = new Retrofit.Builder()
            .baseUrl("https://skymusicfpolygroup3.000webhostapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ApiSkyMusic apiSkyMusic = retrofitSkyMusic.create(ApiSkyMusic.class);
   @GET("/api/get_all_playlist.php")
    Call<List<PlayList>> getAllPlayList();
    @GET("/api/get_home_content.php")
    Call<List<HomeContent>> getHomeContent();
    @GET("/api/get_slider_playlist.php")
    Call<List<PlayList>> getSliderPlayList();
}
