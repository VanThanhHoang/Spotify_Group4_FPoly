package com.example.spotify_group4.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


public class Song implements Parcelable {
    @SerializedName("ID")
    int id;
    @SerializedName("NAME")
    String name;
    @SerializedName("SINGER_NAME")
    String singerName;
    @SerializedName("URL_SONG")
    String url;
    @SerializedName("URL_IMG")
    String urlImg;

    public Song(String name, String Singer, String url, String urlImg) {
        this.name = name;
        this.singerName = Singer;
        this.url = url;
        this.urlImg = urlImg;
    }

    public Song() {
    }

    public Song(Parcel source) {
        this.url = source.readString();
        this.singerName = source.readString();
        this.name = source.readString();
        this.id = source.readInt();
        this.urlImg = source.readString();
    }

    public String getUrlImg() {
        return urlImg;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSingerName() {
        return singerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.singerName);
        dest.writeString(this.name);
        dest.writeInt(this.id);
        dest.writeString(this.urlImg);
    }

    public static final Parcelable.Creator<Song> CREATOR =
            new Parcelable.Creator<Song>() {
                @Override
                public Song createFromParcel(Parcel source) {
                    return new Song(source);
                }

                @Override
                public Song[] newArray(int size) {
                    return new Song[size];
                }
            };
}