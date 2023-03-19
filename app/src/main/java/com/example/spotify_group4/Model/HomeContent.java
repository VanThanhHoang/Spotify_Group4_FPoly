package com.example.spotify_group4.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeContent {
    @SerializedName("BIG_CONTENT_NAME")
    String name;
    @SerializedName("PLAY_LISTS")
    List<PlayList> playLists;

    public HomeContent(String name, List<PlayList> playLists) {
        this.name = name;
        this.playLists = playLists;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlayList> getPlayLists() {
        return playLists;
    }

    public void setPlayLists(List<PlayList> playLists) {
        this.playLists = playLists;
    }
}
