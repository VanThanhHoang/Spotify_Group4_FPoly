package com.example.spotify_group4.Listener;

import com.example.spotify_group4.Model.Song;

import java.util.List;

public interface SearchListener {
    void onSearching();
    void onResponse(List<Song> songList);
}
