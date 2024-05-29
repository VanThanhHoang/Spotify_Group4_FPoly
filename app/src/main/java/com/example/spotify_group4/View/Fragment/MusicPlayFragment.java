package com.example.spotify_group4.View.Fragment;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

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
import com.example.spotify_group4.Presenter.InteractPresenter;
import com.example.spotify_group4.Presenter.MediaPlayerPresenter;
import com.example.spotify_group4.R;
import com.example.spotify_group4.Receiver.MediaPlayerReceiver;
import com.example.spotify_group4.SharedPreferences.AppSharedPreferenceHelper;
import com.example.spotify_group4.View.Activity.HomeActivity;
import com.example.spotify_group4.View.Dialog.DialogAddSongToPlayList;
import com.example.spotify_group4.databinding.FragmentPlayMusicBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class MusicPlayFragment extends Fragment implements MediaPlayerListener, ExitMediaPlayerListener {
    AppSharedPreferenceHelper appSharedPreferenceHelper;
    FragmentPlayMusicBinding layoutBinding;
    ReplaceFragmentListener replaceFragmentListener;
    MediaPlayerPresenter playMusicPresenter;
    String currentAction = Constants.MEDIA_PLAYER_ACTION_RESUME;
    MediaPlayerReceiver mediaPlayerReceiver;
    int mFullIntDuration;
    LoadListener loadListener;
    List<Song> mSongList;
    int mCurrentSongPosition;
    Song mCurrentSong;
    SongVpgAdapter songVpgAdapter;
    boolean isUserSwipe;
    boolean mIsContinueMusicPlayer;
    HandleMiniPlayerListener handleMiniPlayerListener;
    HomeActivity homeActivity;
    InteractPresenter interactPresenter;
    String userId;
    boolean isLikeSong;
    int mSumLike = 0;

    @Override
    public void onDetach() {
        super.onDetach();
    }

    void getUserId() {
//        (loginReq.getStatus().equals("success")) {
//            SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", 0);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("token", loginReq.getEmail());
//            editor.apply();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MySharedPref", 0);
        userId = sharedPreferences.getString("token", "");
    }

    public MusicPlayFragment(List<Song> songList, int songPosition) {
        this.mSongList = songList;
        this.mCurrentSongPosition = songPosition;
        mCurrentSong = songList.get(mCurrentSongPosition);
    }

    public MusicPlayFragment(List<Song> songList, int songPosition, boolean isContinueMusicPlayer) {
        this.mSongList = songList;
        this.mIsContinueMusicPlayer = isContinueMusicPlayer;
        this.mCurrentSongPosition = songPosition;
        mCurrentSong = songList.get(mCurrentSongPosition);
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
        getUserId();
        playMusicPresenter.getSumLike(mCurrentSong.getId());
        playMusicPresenter.isSongLiked(userId, mCurrentSong.getId());
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
        intentFilter.addAction(MediaPlayerReceiver.ACTION_PAUSE_MUSIC);
        intentFilter.addAction(MediaPlayerReceiver.ACTION_RESUME_MUSIC);
        homeActivity.registerReceiver(mediaPlayerReceiver, intentFilter);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) getContext();
        loadListener = (LoadListener) context;
        interactPresenter = new InteractPresenter(context);
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

    @SuppressLint("SetTextI18n")
    void initEvent() {
        layoutBinding.btnDown.setOnClickListener(v -> checkPermission());
        layoutBinding.btnLikeSong.setOnClickListener(v -> {
            if (isLikeSong) {
                layoutBinding.tvSumLike.setText(--mSumLike + "");
            } else {
                layoutBinding.tvSumLike.setText(++mSumLike + "");
            }
            isLikeSong = !isLikeSong;
            playMusicPresenter.likeSong(userId, mCurrentSong.getId());
            likeSong(isLikeSong);
        });
        layoutBinding.btnShare.setOnClickListener(v ->
                interactPresenter.shareWithAnotherApp(mSongList.get(mCurrentSongPosition))
        );
        layoutBinding.btnShuffle.setOnClickListener(v -> playMusicPresenter.setShuffleMode());
        layoutBinding.btnRepeatMode.setOnClickListener(v -> playMusicPresenter.setRepeatMode());
        if (getActivity() != null) {
            layoutBinding.btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        }
        layoutBinding.btnNext.setOnClickListener(v -> {
            playMusicPresenter.playNextSong();
            playMusicPresenter.isSongLiked(userId, mCurrentSong.getId());
            playMusicPresenter.getSumLike(mCurrentSong.getId());
            if (mCurrentSongPosition == mSongList.size() - 1) {
                mCurrentSongPosition = 0;
            } else {
                mCurrentSong = mSongList.get(++mCurrentSongPosition);
            }
        });
        layoutBinding.btnPrev.setOnClickListener(v -> {
            playMusicPresenter.playPrevSong();
            if (mCurrentSongPosition == 0) {
                mCurrentSongPosition = mSongList.size()-1;
            } else {
                mCurrentSong = mSongList.get(--mCurrentSongPosition);
            }
            playMusicPresenter.getSumLike(mCurrentSong.getId());
            resetTime();
        });
        layoutBinding.btnPlayPause.setOnClickListener(v -> playButtonAction());
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
        layoutBinding.btnAddPlayList.setOnClickListener(v -> {
            assert getContext() != null;
            DialogAddSongToPlayList dialogAddSongToPlayList = new DialogAddSongToPlayList(this.getContext(), userId, mCurrentSong.getId());
            dialogAddSongToPlayList.show();
        });
    }

    void playButtonAction() {
        if (currentAction.equals(Constants.MEDIA_PLAYER_ACTION_PAUSE)) {
            currentAction = Constants.MEDIA_PLAYER_ACTION_RESUME;
            playMusicPresenter.resumeMusic();
        } else if (currentAction.equals(Constants.MEDIA_PLAYER_ACTION_RESUME)) {
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
                    mCurrentSongPosition = position;
                    mCurrentSong = mSongList.get(position);
                    resetTime();
                    loadListener.onLoad();
                    playMusicPresenter.transSongByViewPager(position);
                    playMusicPresenter.isSongLiked(userId, mCurrentSong.getId());
                    playMusicPresenter.getSumLike(mCurrentSong.getId());
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
        handleMiniPlayerListener.onSongResume();
        layoutBinding.btnPlayPause.setImageResource(R.drawable.ic_pause);
    }

    @Override
    public void onMusicPause() {
        handleMiniPlayerListener.onSongPause();
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
    public void likeSong(boolean isLikeSong) {
        this.isLikeSong = isLikeSong;
        if (isLikeSong) {
            layoutBinding.btnLikeSong.setImageResource(R.drawable.ic_liked);
        } else {
            layoutBinding.btnLikeSong.setImageResource(R.drawable.ic_like);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onGetSumLike(int sumLike) {
        mSumLike = sumLike;
        layoutBinding.tvSumLike.setText(sumLike + "");
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
    public String getAction() {
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

    @Override
    public MediaPlayerListener getMediaListener() {
        return this;
    }
    private void downloadSong(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(mSongList.get(mCurrentSongPosition).getName());
        request.setDescription("Đang tải xuống");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mSongList.get(mCurrentSongPosition).getName() + ".mp3");
        DownloadManager manager = (DownloadManager) requireContext().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
    void checkPermission() {
            downloadSong(mSongList.get(mCurrentSongPosition).getUrl());
    }
}
