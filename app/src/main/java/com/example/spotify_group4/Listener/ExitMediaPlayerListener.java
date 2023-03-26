package com.example.spotify_group4.Listener;

import android.content.BroadcastReceiver;

import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.Receiver.MediaPlayerReceiver;

import java.util.List;

public interface ExitMediaPlayerListener {
    List<Song> getSongList();
    int getPosition();
    int getAction();
    int getFullDuration();
    MediaPlayerReceiver getReceiver();
}
