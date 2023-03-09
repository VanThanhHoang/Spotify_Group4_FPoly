package com.example.spotify_group4.View.Fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.databinding.FragmentPlayMusicBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class MusicPlayFragment extends Fragment {
    FragmentPlayMusicBinding layoutBinding;
    ReplaceFragmentListener replaceFragmentListener;
    String urlData;

    public MusicPlayFragment(String url) {
        this.urlData = url;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentPlayMusicBinding.inflate(getLayoutInflater(), null, false);
        return layoutBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        replaceFragmentListener = (ReplaceFragmentListener) context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replaceFragmentListener.hideComponents();
        initEvent();
        setUpLayout();
        playMusic();
    }
    void initEvent(){
        if(getActivity()!=null){
            layoutBinding.btnBack.setOnClickListener(v-> getActivity().onBackPressed());
        }
    }
    void setUpLayout(){

        Picasso.get().load("https://cdn.shopify.com/s/files/1/0570/3640/6943/products/the-weeknd-blinded-by-the-lights-poster-815179.jpg?v=1650952639").
                into(layoutBinding.imgSong);

    }
    void playMusic() {
        new Handler(Looper.getMainLooper()).postDelayed(this::startMediaPlayer, 100);
    }

    void startMediaPlayer() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(urlData);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {

        }
    }
}
