package com.example.spotify_group4.Listener;

import com.example.spotify_group4.Receiver.MediaPlayerReceiver;

public interface HandleMiniPlayerListener {
    void hideMiniPlayer();
    void updateDurationMiniPlayer(int currentProcess);
    void transSong(int position);
    MediaPlayerReceiver getMediaPlayerReceiver();
}
