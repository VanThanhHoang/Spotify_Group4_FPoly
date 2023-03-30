package com.example.spotify_group4.Listener;
import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Receiver.MediaPlayerReceiver;

import java.util.List;

public interface ExitMediaPlayerListener {
    List<Song> getSongList();
    int getPosition();
    String getAction();
    int getFullDuration();
    MediaPlayerReceiver getReceiver();
    MediaPlayerListener getMediaListener();
}
