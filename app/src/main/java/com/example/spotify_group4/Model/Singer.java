package com.example.spotify_group4.Model;

import com.google.gson.annotations.SerializedName;

public class Singer {
    @SerializedName("ID")
    int id;
    @SerializedName("NAME")
    String name;
    @SerializedName("URL_IMG")
    String urlImg;

    public Singer(int id, String name, String urlImg) {
        this.id = id;
        this.name = name;
        this.urlImg = urlImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
}
