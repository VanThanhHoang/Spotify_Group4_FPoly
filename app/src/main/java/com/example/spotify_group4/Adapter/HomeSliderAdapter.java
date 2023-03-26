package com.example.spotify_group4.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.spotify_group4.Listener.ReplaceFragmentListener;
import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.View.Fragment.HomeSliderFragment;

import java.util.List;

public class HomeSliderAdapter extends FragmentStateAdapter {
    List<PlayList> playLists;
    ReplaceFragmentListener replaceFragmentListener;

    public HomeSliderAdapter(@NonNull FragmentActivity fragmentActivity, List<PlayList> playLists, ReplaceFragmentListener replaceFragmentListener) {
        super(fragmentActivity);
        this.replaceFragmentListener = replaceFragmentListener;
        this.playLists = playLists;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new HomeSliderFragment(playLists.get(position), replaceFragmentListener);
    }

    @Override
    public int getItemCount() {
        return playLists.size();
    }
}
