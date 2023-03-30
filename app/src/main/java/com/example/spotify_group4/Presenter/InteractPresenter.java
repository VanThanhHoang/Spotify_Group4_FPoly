package com.example.spotify_group4.Presenter;

import android.content.Context;
import android.content.Intent;

import com.example.spotify_group4.Helper.Constants;
import com.example.spotify_group4.Model.Song;

public class InteractPresenter {
    Context context;

    public InteractPresenter(Context context) {
        this.context = context;
    }

    public void shareWithAnotherApp(Song song) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Cùng nghe " + song.getName() + " của " + song.getSingerName() + " với tôi trên Skymusic nhé " + Constants.SONG_INFO_BASE_LINK + song.getId());
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, song.getName());
        context.startActivity(shareIntent);
    }

    public void likeSong(Song song) {

    }

    public void getStatusSong() {

    }
}
