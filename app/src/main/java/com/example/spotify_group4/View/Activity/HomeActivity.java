package com.example.spotify_group4.View.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.spotify_group4.Helper.Constants;
import com.example.spotify_group4.Listener.ExitMediaPlayerListener;
import com.example.spotify_group4.Listener.HandleMiniPlayerListener;
import com.example.spotify_group4.Listener.LoadListener;
import com.example.spotify_group4.Listener.MediaPlayerListener;
import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Presenter.MediaPlayerPresenter;
import com.example.spotify_group4.R;
import com.example.spotify_group4.Receiver.MediaPlayerReceiver;
import com.example.spotify_group4.View.Dialog.LoadingDialog;
import com.example.spotify_group4.View.Fragment.AccountFragment;
import com.example.spotify_group4.View.Fragment.HomeFragment;
import com.example.spotify_group4.View.Fragment.LibraryFragment;
import com.example.spotify_group4.View.Fragment.MusicPlayFragment;
import com.example.spotify_group4.View.Fragment.PlayListFragment;
import com.example.spotify_group4.View.Fragment.SearchFragment;
import com.example.spotify_group4.databinding.ActivityHomeBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements ReplaceFragmentListener, LoadListener, HandleMiniPlayerListener {
    ActivityHomeBinding layoutBinding;
    HomeFragment homeFragment;
    SearchFragment searchFragment;
    LibraryFragment libraryFragment;
    AccountFragment accountFragment;
    LoadingDialog loadingDialog;
    ExitMediaPlayerListener exitMediaPlayerListener;
    int mCurrentSongPosition;
    String mCurrentAction;
    List<Song> songList;
    Song mCurrentSong;
    MediaPlayerPresenter mediaPlayerPresenter;
    MediaPlayerReceiver mediaPlayerReceiver;
    int mFullIntDuration;
    FragmentTransaction fragmentTransaction;
    MediaPlayerListener mediaPlayerListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    void initFragment() {
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        libraryFragment = new LibraryFragment();
        accountFragment = new AccountFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding = ActivityHomeBinding.inflate(getLayoutInflater(),
                null, false);
        setContentView(layoutBinding.getRoot());
        loadingDialog = new LoadingDialog(this);
        layoutBinding.layoutBottomCurrentPlayingSong.setOnClickListener(v ->
                resumeMediaPlayerFragment());
        initEvent();
        initFragment();
        replaceFragment(homeFragment);
        layoutBinding.layoutBottomCurrentPlayingSong.setVisibility(View.GONE);
    }

    void setEventMediaPlayer() {
        layoutBinding.btnPlayPause.setOnClickListener(v -> {
            if (mCurrentAction.equals(Constants.MEDIA_PLAYER_ACTION_RESUME)) {
                mediaPlayerPresenter.pauseMusic();
            } else {
                mediaPlayerPresenter.resumeMusic();
            }
        });
        layoutBinding.btnNext.setOnClickListener(v -> mediaPlayerPresenter.playNextSong());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackFragment() {

    }

    void resumeMediaPlayerFragment() {
        replaceFragment(new MusicPlayFragment(songList, mCurrentSongPosition, true));
    }

    void setUpMiniPlayer() {
        Picasso.get().load(mCurrentSong.getUrlImg()).into(layoutBinding.imgSong);
        layoutBinding.tvSingerName.setText(mCurrentSong.getSingerName());
        layoutBinding.tvSongName.setText(mCurrentSong.getName());
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerHome);
        if (fragment instanceof MusicPlayFragment) {
            exitMediaPlayerListener = (ExitMediaPlayerListener) fragment;
            mCurrentAction = exitMediaPlayerListener.getAction();
            if (mCurrentAction.equals(Constants.MEDIA_PLAYER_ACTION_RESUME)) {
                prepareMiniMediaPlayer();
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void prepareMiniMediaPlayer() {
        mCurrentSongPosition = exitMediaPlayerListener.getPosition();
        songList = exitMediaPlayerListener.getSongList();
        mFullIntDuration = exitMediaPlayerListener.getFullDuration();
        mCurrentSong = songList.get(mCurrentSongPosition);
        mediaPlayerReceiver = exitMediaPlayerListener.getReceiver();
        mediaPlayerListener = exitMediaPlayerListener.getMediaListener();
        mediaPlayerPresenter = new MediaPlayerPresenter(this, mediaPlayerListener);
        layoutBinding.layoutBottomCurrentPlayingSong.setVisibility(View.VISIBLE);
        setEventMediaPlayer();
        setUpMiniPlayer();
    }

    @Override
    protected void onDestroy() {
        loadingDialog.dismiss();
        if (mediaPlayerReceiver != null) {
            unregisterReceiver(mediaPlayerReceiver);
            mediaPlayerPresenter.stopService();
        }
        super.onDestroy();
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerHome, fragment);
        fragmentTransaction.setCustomAnimations(R.anim.anm_replace_fragment, R.anim.anm_replace_fragment);
        if (fragment instanceof MusicPlayFragment || fragment instanceof PlayListFragment) {
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return;
        }
        fragmentTransaction.commit();
    }

    void initEvent() {
        layoutBinding.bottomNavigation.setOnItemSelectedListener(item -> {
            int idItem = item.getItemId();
            if (idItem == R.id.it_Home_MenuHomeNavigation) {
                replaceFragment(homeFragment);
            } else if (idItem == R.id.it_Search_MenuHomeNavigation) {
                replaceFragment(searchFragment);
            } else if (idItem == R.id.it_Library_MenuHomeNavigation) {
                replaceFragment(libraryFragment);
            } else if (idItem == R.id.it_Account_MenuHomeNavigation) {
                replaceFragment(accountFragment);
            }
            return true;
        });
    }

    @Override
    public void hideComponents() {
        if (layoutBinding.bottomNavigation.isShown()) {
            layoutBinding.bottomNavigation.setVisibility(View.GONE);
        }
    }

    @Override
    public void showComponents() {
        if (!layoutBinding.bottomNavigation.isShown()) {
            layoutBinding.bottomNavigation.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onLoad() {
        loadingDialog.show();
    }

    @Override
    public void onComplete() {
        loadingDialog.dismiss();
    }



    @Override
    public void hideMiniPlayer() {
        layoutBinding.layoutBottomCurrentPlayingSong.setVisibility(View.GONE);
    }

    @Override
    public void updateDurationMiniPlayer(int currentProcess) {
        layoutBinding.seekBarDuration.setProgress(currentProcess);
    }

    @Override
    public void transSong(int position) {
        if (mCurrentSong != null) {
            mCurrentSongPosition = position;
            mCurrentSong = songList.get(mCurrentSongPosition);
            setUpMiniPlayer();
        }
    }

    @Override
    public void onSongResume() {
        if (mCurrentSong != null) {
            mCurrentAction = Constants.MEDIA_PLAYER_ACTION_RESUME;
            layoutBinding.btnPlayPause.setImageResource(R.drawable.ic_pause_50);
        }
    }

    @Override
    public void onSongPause() {
        if (mCurrentSong != null) {
            mCurrentAction = Constants.MEDIA_PLAYER_ACTION_PAUSE;
            layoutBinding.btnPlayPause.setImageResource(R.drawable.ic_play_50);
        }
    }

    @Override
    public MediaPlayerReceiver getMediaPlayerReceiver() {
        return mediaPlayerReceiver;
    }

}