package com.example.spotify_group4.Listener;

import com.example.spotify_group4.Model.Song;

import java.util.List;

public interface MediaPlayerListener {
    void onSongLoad();

    void onMusicPlay();

    void onMusicPause();

    void onInitInfo(int fullDuration);

    void onMusicStop();

    void onUpdateSeekbar(String currentDuration, int currentProcess);

    void onTransSong(int currentSongPosition);

    void onChangeRepeatMode(String repeatMode);

    void onChangeShuffleMode(boolean repeatMode);

    void onPlayListShuffled(List<Song> listSong);
    void likeSong(boolean isLikeSong);
    void onGetSumLike(int sumLike);
}
