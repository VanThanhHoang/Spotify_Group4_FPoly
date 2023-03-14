package com.example.spotify_group4.Listener;

public interface MediaPlayerListener {
    void onSongLoad();
    void onMusicPlay();
    void onMusicPause();
    void onMusicStop();
    void onUpdateSeekbar();
    void onUpdateTime();
    void onNextSong();
    void onPrevSong();
}
