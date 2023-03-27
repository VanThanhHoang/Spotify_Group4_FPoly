package com.example.spotify_group4.Listener;

import com.example.spotify_group4.Receiver.MediaPlayerReceiver;

public interface HandleMiniPlayerListener {
    void hideMiniPlayer();
    void updateDurationMiniPlayer(int currentProcess);
    void transSong(int position);
    void onSongResume();
    void onSongPause();
    MediaPlayerReceiver getMediaPlayerReceiver();
}
