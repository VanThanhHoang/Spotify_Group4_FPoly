package com.example.spotify_group4.View.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Listener.MediaPlayerListener;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Presenter.MediaPlayerPresenter;
import com.example.spotify_group4.R;
import com.example.spotify_group4.View.Dialog.LoadingDialog;
import com.example.spotify_group4.databinding.FragmentPlayMusicBinding;

public class MusicPlayFragment extends Fragment implements MediaPlayerListener {
    //ACTION
    public static final String ACTION_INIT_DURATION = "INIT_UI_FRAGMENT_MUSIC_PLAYER";
    public static final String ACTION_UPDATE_DURATION = "UPDATE_UI_FRAGMENT_MUSIC_PLAYER";
    FragmentPlayMusicBinding layoutBinding;
    ReplaceFragmentListener replaceFragmentListener;
    String urlData;
    MediaPlayerPresenter playMusicPresenter;
    int CURRENT_ACTION = 1;
    LoadingDialog loadingDialog;
    mediaPlayerReceiver mediaPlayerReceiver;
    int fullIntDuration ;
    private class mediaPlayerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_INIT_DURATION)) {
                onMusicPlay();
                fullIntDuration = intent.getIntExtra("fullIntDuration",0);
                String fullDuration = intent.getStringExtra("fullDuration");
                layoutBinding.tvTimeEnd.setText(fullDuration);
            } else {

                String currentDuration = intent.getStringExtra("currentDuration");
                int positionSeekbar = intent.getIntExtra("positionSeekbar",0);
                layoutBinding.tvTimeStart.setText(currentDuration);
                layoutBinding.timeSeekBar.setProgress(positionSeekbar);
            }
        }
    }

    @Override
    public void onStart() {
        registerBroadcast();
        super.onStart();
    }
    void registerBroadcast(){
        mediaPlayerReceiver = new mediaPlayerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_INIT_DURATION);
        intentFilter.addAction(ACTION_UPDATE_DURATION);
        if (getContext() != null) {
            getContext().registerReceiver(mediaPlayerReceiver, intentFilter);
        }
    }
    public MusicPlayFragment(String url) {
        this.urlData = url;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentPlayMusicBinding.inflate(getLayoutInflater(), null, false);
        playMusicPresenter = new MediaPlayerPresenter(getContext(), this);
        if (getContext() != null) {
            loadingDialog = new LoadingDialog(this.getContext());
        }
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
        loadingDialog.show();
        replaceFragmentListener.hideComponents();
        initEvent();
        setUpLayout();
        playMusicPresenter.playMusic(urlData);
    }

    @SuppressLint("ClickableViewAccessibility")
    void initEvent() {
        if (getActivity() != null) {
            layoutBinding.btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        }
        layoutBinding.btnPlayPause.setOnClickListener(v -> playButtonAction());
        layoutBinding.timeSeekBar.setOnTouchListener((v, event) ->{
            SeekBar seekBar =(SeekBar) v;
            int positionUpdate = (fullIntDuration/1000)*seekBar.getProgress();
            playMusicPresenter.seekMusic(positionUpdate);
            return false;
        });
    }

    void playButtonAction() {
        if (CURRENT_ACTION == MediaPlayerPresenter.ACTION_PAUSE) {
            CURRENT_ACTION = MediaPlayerPresenter.ACTION_PLAY;
            playMusicPresenter.resumeMusic();
        } else if (CURRENT_ACTION == MediaPlayerPresenter.ACTION_PLAY) {
            CURRENT_ACTION = MediaPlayerPresenter.ACTION_PAUSE;
            playMusicPresenter.pauseMusic();
        }
    }

    void setUpLayout() {
        playMusicPresenter.loadSongImg("https://skymusicfpoly.000webhostapp.com/imgsinger/anhquan.png")
                .into(layoutBinding.imgSong);
    }

    @Override
    public void onSongLoad() {
        loadingDialog.show();
    }

    @Override
    public void onMusicPlay() {
        loadingDialog.hide();
        layoutBinding.btnPlayPause.setImageResource(R.drawable.ic_pause);
    }

    @Override
    public void onMusicPause() {
        layoutBinding.btnPlayPause.setImageResource(R.drawable.ic_play);
    }

    @Override
    public void onMusicStop() {
        layoutBinding.btnPlayPause.setImageResource(R.drawable.ic_play);
    }

    @Override
    public void updateSeekbar() {

    }

    @Override
    public void updateTime() {

    }
}
