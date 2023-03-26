package com.example.spotify_group4.View.Fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.spotify_group4.Adapter.SongVpgAdapter;
import com.example.spotify_group4.Helper.AnimationZoomViewPager;
import com.example.spotify_group4.Helper.Constants;
import com.example.spotify_group4.Helper.TimeFormatter;
import com.example.spotify_group4.Listener.ExitMediaPlayerListener;
import com.example.spotify_group4.Listener.HandleMiniPlayerListener;
import com.example.spotify_group4.Listener.LoadListener;
import com.example.spotify_group4.Listener.MediaPlayerListener;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Presenter.MediaPlayerPresenter;
import com.example.spotify_group4.R;
import com.example.spotify_group4.Receiver.MediaPlayerReceiver;
import com.example.spotify_group4.SharedPreferences.AppSharedPreferenceHelper;
import com.example.spotify_group4.View.Activity.HomeActivity;
import com.example.spotify_group4.databinding.FragmentPlayMusicBinding;

import java.util.List;

public class MusicPlayFragment extends Fragment implements MediaPlayerListener, ExitMediaPlayerListener {
    AppSharedPreferenceHelper appSharedPreferenceHelper;
    FragmentPlayMusicBinding layoutBinding;
    ReplaceFragmentListener replaceFragmentListener;
    MediaPlayerPresenter playMusicPresenter;
    int currentAction = Constants.MEDIA_PLAYER_ACTION_PLAY;
    MediaPlayerReceiver mediaPlayerReceiver;
    int mFullIntDuration;
    LoadListener loadListener;
    List<Song> mSongList;
    int mCurrentSongPosition;
    Song song;
    SongVpgAdapter songVpgAdapter;
    boolean isUserSwipe;
    boolean mIsContinueMusicPlayer;
    HandleMiniPlayerListener handleMiniPlayerListener;
    HomeActivity homeActivity;

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public MusicPlayFragment(List<Song> songList, int songPosition) {
        this.mSongList = songList;
        this.mCurrentSongPosition = songPosition;
        this.song = songList.get(mCurrentSongPosition);
    }

    public MusicPlayFragment(List<Song> songList, int songPosition, boolean isContinueMusicPlayer) {
        this.mSongList = songList;
        this.mIsContinueMusicPlayer = isContinueMusicPlayer;
        this.mCurrentSongPosition = songPosition;
        this.song = songList.get(mCurrentSongPosition);
    }

    @Override
    public void onStart() {
        if (!mIsContinueMusicPlayer) {
            registerBroadcast();
        }
        super.onStart();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleMiniPlayerListener.hideMiniPlayer();
        playMusicPresenter.getRepeatMode();
        playMusicPresenter.getShuffleMode();
        replaceFragmentListener.hideComponents();
        initEvent();
        initViewPager();
        if (!mIsContinueMusicPlayer) {
            mediaPlayerReceiver = new MediaPlayerReceiver(this);
            playMusicPresenter.startPlayList(mSongList, mCurrentSongPosition);
        } else {
            onPlayListShuffled(mSongList);
            mediaPlayerReceiver = homeActivity.getMediaPlayerReceiver();
            mediaPlayerReceiver.setMediaPlayerListener(this);
            playMusicPresenter.continuesMediaPlayer();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentPlayMusicBinding.inflate(getLayoutInflater(), null, false);
        playMusicPresenter = new MediaPlayerPresenter(getContext(), this);
        return layoutBinding.getRoot();
    }

    void setShuffleButton(boolean isShuffle) {
        if (isShuffle) {
            layoutBinding.btnShuffle.setIconTintResource(R.color.green);
        } else {
            layoutBinding.btnShuffle.setIconTintResource(R.color.white);
        }
    }

    void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaPlayerReceiver.ACTION_SHUFFLED_PLAY_LIST);
        intentFilter.addAction(MediaPlayerReceiver.ACTION_INIT_DURATION);
        intentFilter.addAction(MediaPlayerReceiver.ACTION_TRANS_SONG);
        intentFilter.addAction(MediaPlayerReceiver.ACTION_MUSIC_COMPLETE);
        intentFilter.addAction(MediaPlayerReceiver.ACTION_UPDATE_DURATION);
        homeActivity.registerReceiver(mediaPlayerReceiver, intentFilter);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) getContext();
        loadListener = (LoadListener) context;
        replaceFragmentListener = (ReplaceFragmentListener) context;
        appSharedPreferenceHelper = new AppSharedPreferenceHelper(context);
        handleMiniPlayerListener = (HandleMiniPlayerListener) context;
    }
     void resetTime() {
        layoutBinding.timeSeekBar.setProgress(0);
        layoutBinding.tvTimeStart.setText(R.string.defaultDuration);
        layoutBinding.tvTimeEnd.setText(R.string.defaultDuration);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    void initEvent() {
        layoutBinding.btnShuffle.setOnClickListener(v -> playMusicPresenter.setShuffleMode());
        layoutBinding.btnRepeatMode.setOnClickListener(v -> playMusicPresenter.setRepeatMode());
        if (getActivity() != null) {
            layoutBinding.btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        }
        layoutBinding.btnNext.setOnClickListener(v -> playMusicPresenter.playNextSong());
        layoutBinding.btnPlayPause.setOnClickListener(v -> playButtonAction());
        layoutBinding.btnPrev.setOnClickListener(v -> {
            playMusicPresenter.playPrevSong();
            resetTime();
        });

        layoutBinding.timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int positionUpdate = (mFullIntDuration / 1000) * seekBar.getProgress();
                    playMusicPresenter.seekMusic(positionUpdate);
                    seekBar.setProgress(progress);
                    layoutBinding.tvTimeStart.setText(TimeFormatter.
                            formatMillisecondToMinuteAndSecond(positionUpdate));
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
        if (currentAction == Constants.MEDIA_PLAYER_ACTION_PAUSE) {
            currentAction = Constants.MEDIA_PLAYER_ACTION_PLAY;
            playMusicPresenter.resumeMusic();
        } else if (currentAction == Constants.MEDIA_PLAYER_ACTION_PLAY) {
            currentAction = Constants.MEDIA_PLAYER_ACTION_PAUSE;
            playMusicPresenter.pauseMusic();
        }
    }

    void initViewPager() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> layoutBinding.vpgSongInfo.setCurrentItem(mCurrentSongPosition), 100);
        layoutBinding.vpgSongInfo.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    isUserSwipe = true;
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == -1 || position == mSongList.size()) {
                    return;
                }
                if (isUserSwipe) {
                    resetTime();
                    loadListener.onLoad();
                    playMusicPresenter.transSongByViewPager(position);
                }
                super.onPageSelected(position);
            }

        });
        layoutBinding.vpgSongInfo.setPageTransformer(new AnimationZoomViewPager());
    }

    void setDataViewPager(List<Song> songList) {
        songVpgAdapter = new SongVpgAdapter(homeActivity, songList);
        layoutBinding.vpgSongInfo.setAdapter(songVpgAdapter);
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
    public void onInitInfo(int fullIntDuration) {
        mFullIntDuration = fullIntDuration;
        layoutBinding.tvTimeEnd.setText(TimeFormatter.formatMillisecondToMinuteAndSecond(fullIntDuration));
    }

    @Override
    public void onMusicStop() {
        layoutBinding.btnPlayPause.setImageResource(R.drawable.ic_play);
    }

    @Override
    public void onUpdateSeekbar(String currentDuration, int currentProcess) {
        layoutBinding.tvTimeStart.setText(currentDuration);
        layoutBinding.timeSeekBar.setProgress(currentProcess);
        handleMiniPlayerListener.updateDurationMiniPlayer(currentProcess);
    }

    @Override
    public void onTransSong(int currentSongPosition) {
        mCurrentSongPosition = currentSongPosition;
        layoutBinding.vpgSongInfo.setCurrentItem(mCurrentSongPosition, true);
        handleMiniPlayerListener.transSong(mCurrentSongPosition);
    }

    @Override
    public void onChangeRepeatMode(String repeatMode) {
        if (repeatMode.equals(Constants.MEDIA_PLAYER_EXTRA_REPEAT_MODE_NOT_REPEAT)) {
            layoutBinding.btnRepeatMode.setIconResource(R.drawable.ic_not_repeat);
            layoutBinding.btnRepeatMode.setIconTintResource(R.color.white);
        } else if (repeatMode.equals(Constants.MEDIA_PLAYER_EXTRA_REPEAT_MODE_REPEAT_ALL)) {
            layoutBinding.btnRepeatMode.setIconResource(R.drawable.ic_repeat);
            layoutBinding.btnRepeatMode.setIconTintResource(R.color.green);
        } else {
            layoutBinding.btnRepeatMode.setIconResource(R.drawable.ic_repeat_one);
            layoutBinding.btnRepeatMode.setIconTintResource(R.color.green);
        }

    }

    @Override
    public void onChangeShuffleMode(boolean repeatMode) {
        setShuffleButton(repeatMode);
    }

    @Override
    public void onPlayListShuffled(List<Song> songList) {
        mSongList = songList;
        setDataViewPager(mSongList);
        layoutBinding.vpgSongInfo.setCurrentItem(mCurrentSongPosition, false);
    }

    @Override
    public List<Song> getSongList() {
        return mSongList;
    }

    @Override
    public int getPosition() {
        return mCurrentSongPosition;
    }

    @Override
    public int getAction() {
        return currentAction;
    }

    @Override
    public int getFullDuration() {
        return mFullIntDuration;
    }

    @Override
    public MediaPlayerReceiver getReceiver() {
        return mediaPlayerReceiver;
    }

}
