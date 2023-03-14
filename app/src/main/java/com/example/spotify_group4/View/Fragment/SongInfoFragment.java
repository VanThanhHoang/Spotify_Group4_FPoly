package com.example.spotify_group4.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.databinding.FragmentSongInfoBinding;
import com.squareup.picasso.Picasso;

public class SongInfoFragment extends Fragment {
    FragmentSongInfoBinding layoutBinding;
    Song mSong;

    public SongInfoFragment(Song song) {
        mSong = song;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = FragmentSongInfoBinding.inflate(getLayoutInflater(), null, false);
        return layoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        layoutBinding.tvSinger.setText(mSong.getSingerName());
        layoutBinding.tvSongName.setText(mSong.getName());
        Picasso.get().load(mSong.getUrlImg()).into(layoutBinding.imgSong);
        super.onViewCreated(view, savedInstanceState);
    }
}
