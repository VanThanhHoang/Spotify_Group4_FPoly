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
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiSkyMusic {
    Retrofit retrofitSkyMusic = new Retrofit.Builder()
            .baseUrl("https://skymusicfpolygroup3.000webhostapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ApiSkyMusic apiSkyMusic = retrofitSkyMusic.create(ApiSkyMusic.class);

    @GET("get_song_from_play_list.php")
    Call<List<Song>> getSongByPlayListId(@Query("playListId") int playListId);

    @GET("get_song_from_user_play_list")
    Call<List<Song>> getSongByUserPlayListId(@Query("playListId") int playListId);

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

    @GET("get_song_liked")
    Call<List<Song>> getSongLiked(@Query("userId") String userId);

    @GET("get_sum_like")
    Call<Integer> getSumLike(@Query("songId") int songId);

    @POST("insert_user")
    Call<Void> insertUser(@Query("userId") String userId);

    @POST("like_song")
    Call<Void> likeSong(@Query("userId") String userId, @Query("songId") int songId);

    @POST("check_is_like_song")
    Call<String> isLikeSong(@Query("userId") String userId, @Query("songId") int songId);

    @POST("insert_user_playlist")
    Call<Void> add_play_list(@Query("userId") String userId, @Query("playListName") String playListName);

    @POST("delete_user_play_list")
    Call<Void> delete_play_list(@Query("playlistId") int playlistId);

    @POST("update_user_play_list")
    Call<Void> update_play_list(@Query("playlistId") int playlistId,@Query("playListName") String playlistName);

    @POST("insert_detail_user_play_list")
    Call<String> add_song_to_play_list(@Query("playlistId") int playlistId,@Query("songId") int songId);

    @GET("get_user_playlist")
    Call<List<PlayList>> getUserPlayList(@Query("userId") String userId);
}
