package com.example.spotify_group4.Model;

import com.google.gson.annotations.SerializedName;

//<?php
//        require_once 'connect.php';
//        $song_name = $_POST['songName'];
//        $singer_id = $_POST['singerId'];
//        $url_img = $_POST['songUrlImg'];
//        $song_url = $_POST['songUrl'];
//        $genre = $_POST['genre'];
//        $sql = "INSERT INTO SONG (NAME, URL_IMG,SINGER_ID,GENRE,SONG_URL) VALUES ('$song_name','$url_img','$singer_id','$genre','$song_url')";
//        if (mysqli_query($conn, $sql)) {
//        echo "New record created successfully";
//        } else {
//        echo "Error: " . $sql . "<br>" . mysqli_error($conn);
//        // Đóng kết nối
//        mysqli_close($conn);
//        }
//        ?>
public class UpSongRQ {
    @SerializedName("songName")
    String songName;
    @SerializedName("singerId")
    int singerId;
    @SerializedName("songUrlImg")
    String songUrlImg;
    @SerializedName("songUrl")
    String songUrl;
    @SerializedName("genre")
    String genre;
    public UpSongRQ(String songName, int singerId, String songUrlImg, String songUrl, String genre) {
        this.songName = songName;
        this.singerId = singerId;
        this.songUrlImg = songUrlImg;
        this.songUrl = songUrl;
        this.genre = genre;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getSingerId() {
        return singerId;
    }

    public void setSingerId(int singerId) {
        this.singerId = singerId;
    }

    public String getSongUrlImg() {
        return songUrlImg;
    }

    public void setSongUrlImg(String songUrlImg) {
        this.songUrlImg = songUrlImg;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
