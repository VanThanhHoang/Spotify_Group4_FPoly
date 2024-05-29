package com.example.spotify_group4.Presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spotify_group4.Listener.GetDataHomeFragmentListener;
import com.example.spotify_group4.Model.HomeContent;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.Retrofit.ApiSkyMusic;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragmentPresenter {
    GetDataHomeFragmentListener mGetDataHomeFragmentListener;
    List<HomeContent> homeContents;
    List<PlayList> playListSlider;
    public HomeFragmentPresenter(GetDataHomeFragmentListener getDataHomeFragmentListener) {
        mGetDataHomeFragmentListener = getDataHomeFragmentListener;
    }
    public void getPlayListForSlider() {
        playListSlider = new ArrayList<>();
        Call<List<PlayList>> callGetSliderPlayLists = ApiSkyMusic.apiSkyMusic.getSliderPlayList();
        callGetSliderPlayLists.enqueue(new Callback<List<PlayList>>() {
            @Override
            public void onResponse(@NonNull Call<List<PlayList>> call, @NonNull Response<List<PlayList>> response) {
                playListSlider = response.body();
                mGetDataHomeFragmentListener.onGetListPlayListComplete(playListSlider);

            }
            @Override
            public void onFailure(@NonNull Call<List<PlayList>> call, @NonNull Throwable t) {
                mGetDataHomeFragmentListener.onGetListPlayListFail();
            }
        });
    }
    public void getHomeContent(){
        Call<List<HomeContent>> callGetHomeContent = ApiSkyMusic.apiSkyMusic.getHomeContent();
        callGetHomeContent.enqueue(new Callback<List<HomeContent>>() {
            @Override
            public void onResponse(@NonNull Call<List<HomeContent>> call, @NonNull Response<List<HomeContent>> response) {
                homeContents = response.body();
                mGetDataHomeFragmentListener.onGetHomeContentComplete(homeContents);
            }

            @Override
            public void onFailure(@NonNull Call<List<HomeContent>> call, @NonNull Throwable t) {
                mGetDataHomeFragmentListener.onGetHomeContentFail();
            }
        });
    }
}
