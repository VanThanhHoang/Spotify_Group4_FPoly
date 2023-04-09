package com.example.spotify_group4.Listener;

import com.example.spotify_group4.Model.PlayList;

import java.util.List;

public interface UserPlayListListener {
    void onGetListPlayListComplete(List<PlayList> playLists);
    void onUpdatePlaylist();
}
