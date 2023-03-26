package com.example.spotify_group4.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.spotify_group4.Model.Song;
import com.example.spotify_group4.View.Fragment.SongInfoFragment;

import java.util.List;

public class SongVpgAdapter extends FragmentStateAdapter {
    List<Song> mSongList;

    public SongVpgAdapter(@NonNull FragmentActivity fragmentActivity, List<Song> songList) {
        super(fragmentActivity);
        mSongList = songList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new SongInfoFragment(mSongList.get(position));
    }

    @Override
    public int getItemCount() {
        if (!mSongList.isEmpty()) {
            return mSongList.size();
        }
        return 0;
    }
}
