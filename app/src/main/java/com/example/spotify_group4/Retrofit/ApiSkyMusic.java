package com.example.spotify_group4.Retrofit;

import com.example.spotify_group4.Model.PlayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
public interface ApiSkyMusic {
    Retrofit retrofitSkyMusic = new Retrofit.Builder()
            .baseUrl("https://skymusicfpolygroup3.000webhostapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ApiSkyMusic apiSkyMusic = retrofitSkyMusic.create(ApiSkyMusic.class);
   @GET("get_all_playlist.php")
    Call<List<PlayList>> getAllPlayList();
}
