package com.example.spotify_group4.View.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Helper.TimeFormatter;
import com.example.spotify_group4.Listener.LoadListener;
import com.example.spotify_group4.Listener.MediaPlayerListener;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Presenter.MediaPlayerPresenter;
import com.example.spotify_group4.R;
import com.example.spotify_group4.databinding.FragmentPlayMusicBinding;

import java.util.List;

public class MusicPlayFragment extends Fragment implements MediaPlayerListener {
    //ACTION
    public static final String ACTION_MUSIC_COMPLETE = "MUSIC_COMPLETE_FRAGMENT_MUSIC_PLAYER";
    public static final String ACTION_INIT_DURATION = "INIT_UI_FRAGMENT_MUSIC_PLAYER";
    public static final String ACTION_UPDATE_DURATION = "UPDATE_UI_FRAGMENT_MUSIC_PLAYER";
    FragmentPlayMusicBinding layoutBinding;
    ReplaceFragmentListener replaceFragmentListener;
    MediaPlayerPresenter playMusicPresenter;
    int CURRENT_ACTION = MediaPlayerPresenter.ACTION_PLAY;
    mediaPlayerReceiver mediaPlayerReceiver;
    int fullIntDuration;
    LoadListener loadListener;
    List<Song> songList;
    int positionSong;
    Song song;

    private class mediaPlayerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_INIT_DURATION:
                    onMusicPlay();
                    fullIntDuration = intent.getIntExtra("fullIntDuration", 0);
                    String fullDuration = intent.getStringExtra("fullDuration");
                    layoutBinding.tvTimeEnd.setText(fullDuration);
                    break;
                case ACTION_UPDATE_DURATION:
                    String currentDuration = intent.getStringExtra("currentDuration");
                    int positionSeekbar = intent.getIntExtra("positionSeekbar", 0);
                    layoutBinding.tvTimeStart.setText(currentDuration);
                    layoutBinding.timeSeekBar.setProgress(positionSeekbar);
                    break;
                case ACTION_MUSIC_COMPLETE:
                    onMusicStop();
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        registerBroadcast();
        super.onStart();
    }

    void registerBroadcast() {
        mediaPlayerReceiver = new mediaPlayerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_INIT_DURATION);
        intentFilter.addAction(ACTION_UPDATE_DURATION);
        intentFilter.addAction(ACTION_MUSIC_COMPLETE);
        if (getContext() != null) {
            getContext().registerReceiver(mediaPlayerReceiver, intentFilter);
        }
    }

    public MusicPlayFragment(List<Song> songList, int songPosition) {
        this.songList = songList;
        this.positionSong = songPosition;
        this.song = songList.get(positionSong);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentPlayMusicBinding.inflate(getLayoutInflater(), null, false);
        playMusicPresenter = new MediaPlayerPresenter(getContext(), this);
        return layoutBinding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        loadListener = (LoadListener) context;
        replaceFragmentListener = (ReplaceFragmentListener) context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replaceFragmentListener.hideComponents();
        initEvent();
        setUpLayout();
        playMusicPresenter.startPlayList(songList, positionSong);
    }

    @SuppressLint("ClickableViewAccessibility")
    void initEvent() {
        if (getActivity() != null) {
            layoutBinding.btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        }
        layoutBinding.btnNext.setOnClickListener(v -> playMusicPresenter.playNextSong());
        layoutBinding.btnPlayPause.setOnClickListener(v -> playButtonAction());
        layoutBinding.btnPrev.setOnClickListener(v -> playMusicPresenter.playPrevSong());
        layoutBinding.timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int positionUpdate = (fullIntDuration / 1000) * seekBar.getProgress();
                    playMusicPresenter.seekMusic(positionUpdate);
                    seekBar.setProgress(progress);
                    layoutBinding.tvTimeStart.setText(TimeFormatter.formatMillisecondToMinuteAndSecond(positionUpdate));
                    playMusicPresenter.seekMusic(positionUpdate);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
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
        layoutBinding.tvSinger.setText(song.getSingerName());
        layoutBinding.tvSongName.setText(song.getName());
        playMusicPresenter.loadSongImg(song.getUrlImg())
                .into(layoutBinding.imgSong);
    }

    @Override
    public void onSongLoad() {
        loadListener.onLoad();
    }

    @Override
    public void onMusicPlay() {
        loadListener.onComplete();
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
    public void onUpdateSeekbar() {

    }

    @Override
    public void onUpdateTime() {

    }

    @Override
    public void onNextSong() {

    }

    @Override
    public void onPrevSong() {

    }
}
