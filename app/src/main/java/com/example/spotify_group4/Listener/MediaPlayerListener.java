package com.example.spotify_group4.Listener;

public interface MediaPlayerListener {
    void onSongLoad();
    void onMusicPlay();
    void onMusicPause();
    void onInitInfo(int fullDuration);
    void onMusicStop();
    void onUpdateSeekbar(String currentDuration,int currentProcess);
    void onTransSong(int currentSongPosition);
    void onChangeRepeatMode(String repeatMode);
}
