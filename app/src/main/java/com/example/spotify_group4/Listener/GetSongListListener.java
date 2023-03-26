package com.example.spotify_group4.Listener;

import com.example.spotify_group4.Model.Song;

import java.util.List;

public interface GetSongListListener {
    void onGetSongListComplete(List<Song> songList);
    void onGetSongListFail();
}
