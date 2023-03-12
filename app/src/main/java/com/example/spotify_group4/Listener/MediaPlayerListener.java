package com.example.spotify_group4.Listener;

public interface MediaPlayerListener {
    void ondSongLoad();
    void onMusicPlay();
    void onMusicPause();
    void onMusicStop();
    void updateSeekbar();
    void updateTime();
}
