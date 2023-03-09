package com.example.spotify_group4.View.Fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Listener.MediaPlayerListener;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Presenter.MediaPlayerPresenter;
import com.example.spotify_group4.R;
import com.example.spotify_group4.databinding.FragmentPlayMusicBinding;

public class MusicPlayFragment extends Fragment implements MediaPlayerListener {
    FragmentPlayMusicBinding layoutBinding;
    ReplaceFragmentListener replaceFragmentListener;
    String urlData;
    MediaPlayerPresenter playMusicPresenter;
    int CURRENT_ACTION = 1;
    public MusicPlayFragment(String url) {
        this.urlData = url;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentPlayMusicBinding.inflate(getLayoutInflater(), null, false);
        playMusicPresenter = new MediaPlayerPresenter(getContext(),this);
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
        playMusicPresenter.playMusic();
    }

    void initEvent() {
        if (getActivity() != null) {
            layoutBinding.btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        }
        layoutBinding.btnPlayPause.setOnClickListener(v -> playButtonAction());
    }
    void playButtonAction(){
        if(CURRENT_ACTION == MediaPlayerPresenter.ACTION_PLAY){
            playMusicPresenter.stopMusic();
            CURRENT_ACTION = MediaPlayerPresenter.ACTION_STOP;
        }else if(CURRENT_ACTION == MediaPlayerPresenter.ACTION_STOP){
            playMusicPresenter.playMusic();
            CURRENT_ACTION = MediaPlayerPresenter.ACTION_PLAY;
        }
    }
    void setUpLayout() {
        playMusicPresenter.loadSongImg("https://lastfm.freetls.fastly.net/i/u/770x0/725cbf01f1b2b49bf17b3cb6e956283b.jpg#725cbf01f1b2b49bf17b3cb6e956283b")
                .into(layoutBinding.imgSong);
    }

    @Override
    public void onMusicPlay() {
        layoutBinding.btnPlayPause.setImageResource(R.drawable.ic_pause);
    }

    @Override
    public void onMusicPause() {

    }

    @Override
    public void onMusicStop() {
        layoutBinding.btnPlayPause.setImageResource(R.drawable.ic_play);
    }
}
